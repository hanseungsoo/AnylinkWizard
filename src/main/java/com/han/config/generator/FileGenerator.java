package com.han.config.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.han.config.pojo.Clusters;
import com.han.config.pojo.DataSource;
import com.han.config.pojo.Domain;
import com.han.config.pojo.Domain.Listeners;
import com.han.wizard.AnylinkWizard.AppMain;
import com.han.config.pojo.Node;
import com.han.config.pojo.UserPath;

public class FileGenerator {
	private static Logger logger = LoggerFactory.getLogger(FileGenerator.class);
	
	public void runGenerator(List<Node> nodeList,List<Domain> domainList, List<Clusters> clustersList, String key, String path, UserPath userPath, DataSource dataSource) {
		logger.info( key + " 파일 생성합니다.");
		logger.info("파일 경로 : " + path);
		FileOutputStream fos = null;
		JtwigModel model = null;
		JtwigTemplate template = null;
		File file = new File(path);
		file.getParentFile().mkdirs();
		boolean flag = false;
		if(key.equals("nodes")) {
			
			template = JtwigTemplate.classpathTemplate("nodes");
			model = JtwigModel.newModel().with("nodeList" , nodeList);
			
		}else if(key.equals("dasProfile")) {
			List<Listeners> msServerListeners = new ArrayList<Domain.Listeners>();
			List<Domain> msList = new ArrayList<Domain>();
			for(int i = 0; i < domainList.size(); i++) {
				Domain item = (Domain) domainList.get(i);
				if(item.getNode_name().equals(userPath.getHostName()) && !item.getName().equals("adminServer")) {
					msList.add(item);
					for(int j = 0; j< item.getListeners().size(); j++) {
						if(item.getListeners().get(j).getName().equals("BASE")) {
							msServerListeners.add(item.getListeners().get(j));
						}
					}
				}
			}
			flag = true;
			template = JtwigTemplate.classpathTemplate("dasAlias");
			model = JtwigModel.newModel().with("msServerList" , msList)
										 .with("userPath", userPath)
										 .with("msServerListeners", msServerListeners);
		}else if(key.equals("domain")) {
//			Map adminMap = new HashMap<String, String>();
//			adminMap.put("aa", "bb");'
			Domain adminServer = (Domain)domainList.remove(0);
			for(int i = 0; i < domainList.size(); i++) {
				Domain item = (Domain) domainList.get(i);
				for(int j = 0; j < clustersList.size(); j++) {
					Clusters cluster = (Clusters) clustersList.get(j);
					if(cluster.getServers().contains(item.getName())) {
						item.setLifeCycle("false");
					}else {
						item.setLifeCycle("true");
					}
				}
			}
			
			
			template = JtwigTemplate.classpathTemplate("domain");
			model = JtwigModel.newModel().with("adminServer" , adminServer)
										 .with("msServerList", domainList)
										 .with("userPath", userPath)
										 .with("clustersList", clustersList)
										 .with("dataSource", dataSource);
		}else if(key.equals("bizConfig")) {
			String msName;
			if(AppMain.dev) {
				msName = "test";
			}else {
				String[] msNames = path.split(File.separator);
				msName = msNames[msNames.length-3];
			}
			
			template = JtwigTemplate.classpathTemplate("bizSystem");
			model = JtwigModel.newModel().with("msName" , msName);
		}else if(key.equals("nodeManager")) {
			template = JtwigTemplate.classpathTemplate("nodeManager");
			model = JtwigModel.newModel().with("userPath" , userPath);
		}
		
		try {
			fos = new FileOutputStream(file, flag);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		template.render(model, fos);
		
		logger.info(key + " 파일 생성 완료");
	}
}
