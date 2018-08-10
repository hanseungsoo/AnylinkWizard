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
import com.han.config.pojo.Domain;
import com.han.config.pojo.Domain.Listeners;
import com.han.config.pojo.Node;
import com.han.config.pojo.UserPath;

public class FileGenerator {
	private static Logger logger = LoggerFactory.getLogger(FileGenerator.class);
	
	public void runGenerator(List<Node> nodeList,List<Domain> domainList, List<Clusters> clustersList, String key, String path, UserPath userPath) {
		logger.info( key + " 파일 생성합니다.");
		logger.info("파일 경로 : " + path);
		FileOutputStream fos = null;
		JtwigModel model = null;
		JtwigTemplate template = null;
		File file = new File(path);
		file.getParentFile().mkdirs();
		
		if(key.equals("nodes")) {
			
			template = JtwigTemplate.classpathTemplate("nodes");
			model = JtwigModel.newModel().with("nodeList" , nodeList);
			
		}else if(key.equals("dasProfile")) {
			List<Listeners> listeners = new ArrayList<Domain.Listeners>();
			List<Domain> msList = new ArrayList<Domain>();
			for(int i = 0; i < domainList.size(); i++) {
				Domain item = (Domain) domainList.get(i);
				if(item.node_name.equals(userPath.getHostName())) {
					msList.add(item);
					for(int j = 0; j< item.listeners.size(); j++) {
						if(item.listeners.get(j).name.equals("BASE")) {
							listeners.add(item.listeners.get(j));
						}
					}
				}
			}
			
			template = JtwigTemplate.classpathTemplate("dasAlias");
			model = JtwigModel.newModel().with("domainList" , msList)
										 .with("userPath", userPath)
										 .with("listeners", listeners);
		}else if(key.equals("domain")) {
//			Map adminMap = new HashMap<String, String>();
//			adminMap.put("aa", "bb");
			Domain adminServer = (Domain)domainList.remove(0);
			for(int i = 0; i < domainList.size(); i++) {
				Domain item = (Domain) domainList.get(i);
				for(int j = 0; j < clustersList.size(); j++) {
					Clusters cluster = (Clusters) clustersList.get(j);
					if(cluster.servers.contains(item.name)) {
						item.lifeCycle = "false";
					}else {
						item.lifeCycle = "true";
					}
				}
			}
			
			
			template = JtwigTemplate.classpathTemplate("domain");
			model = JtwigModel.newModel().with("adminServer" , adminServer)
										 .with("msServers", domainList)
										 .with("userPath", userPath)
										 .with("clustersList", clustersList);
		}
		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		template.render(model, fos);
		
		logger.info(key + " 파일 생성 완료");
	}
}
