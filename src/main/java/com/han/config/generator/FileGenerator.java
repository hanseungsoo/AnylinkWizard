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

import com.han.config.pojo.Domain;
import com.han.config.pojo.Domain.Listeners;
import com.han.config.pojo.UserPath;

public class FileGenerator {
	private static Logger logger = LoggerFactory.getLogger(FileGenerator.class);
	
	public <T> void runGenerator(List<T> list, String key, String path, UserPath userPath) {
		logger.info( key + " 파일 생성합니다.");
		logger.info("파일 경로 : " + path);
		FileOutputStream fos = null;
		JtwigModel model = null;
		JtwigTemplate template = null;
		File file = new File(path);
		file.getParentFile().mkdirs();
		
		if(key.equals("nodes")) {
			
			template = JtwigTemplate.classpathTemplate("nodes");
			model = JtwigModel.newModel().with("nodeList" , list);
			
		}else if(key.equals("dasProfile")) {
			List<Listeners> listeners = new ArrayList<Domain.Listeners>();
			List<Domain> domainList = new ArrayList<Domain>();
			for(int i = 0; i < list.size(); i++) {
				Domain item = (Domain) list.get(i);
				if(item.node_name.equals(userPath.getHostName())) {
					domainList.add(item);
					for(int j = 0; j< item.listeners.size(); j++) {
						if(item.listeners.get(j).name.equals("BASE")) {
							listeners.add(item.listeners.get(j));
						}
					}
				}
			}
			
			template = JtwigTemplate.classpathTemplate("dasAlias");
			model = JtwigModel.newModel().with("domainList" , domainList)
										 .with("userPath", userPath)
										 .with("listeners", listeners);
		}else if(key.equals("domain")) {
//			Map adminMap = new HashMap<String, String>();
//			adminMap.put("aa", "bb");
			Domain adminServer = (Domain)list.remove(0);
			
			template = JtwigTemplate.classpathTemplate("server");
			model = JtwigModel.newModel().with("adminServer" , adminServer);
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
