package com.han.xml.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AliasFileRW {
	private static Logger logger = LoggerFactory.getLogger(AliasFileRW.class);
	private String path;
	public AliasFileRW(String homePath) {
		logger.info("프로파일 파일 경로 : " + homePath);
		this.path = homePath;
	}
	
	public void setProfile(String serverType, JtwigModel model) {
		File file = new File(path+File.separator+".bash_profile");
		if(!file.exists()) {
			file = new File(path+File.separator+".profile");
		}
		logger.info("파일 이름 : " + file.getName());
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file, true);
			JtwigTemplate template = null;
			if(serverType.equals("das")) {
				template = JtwigTemplate.classpathTemplate("dasAlias");
			}else {
				template = JtwigTemplate.classpathTemplate("msAlias");
			}
			
	        template.render(model, fos);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	}
}
