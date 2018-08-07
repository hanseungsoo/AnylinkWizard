package com.han.xml.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tmaxsoft.schemas.anylink.BizSystemInfo;
import com.tmaxsoft.xml.ns.jeus.Domain.Clusters.Cluster;
import com.tmaxsoft.xml.ns.jeus.Domain.LifecycleInvocation;
import com.tmaxsoft.xml.ns.jeus.Domain.Servers.Server;


public class TemplateServerRead<T>  {
	private static Logger logger = LoggerFactory.getLogger(TemplateServerRead.class);

	@SuppressWarnings("unchecked")
	public T XmlToObject(String fileName) {
		
		logger.info(fileName + " 파일 로딩합니다.");
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName + ".xml");
		
		File file = null;
		try {
			file = File.createTempFile(fileName, ".xml");
			IOUtils.copy(is, new FileOutputStream(file));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(1);
		}
		
        T templet = null;
        
        
		try {
			JAXBContext jaxbContext = null;
			if(fileName.equals("server")) {
				jaxbContext = JAXBContext.newInstance(Server.class);
			}else if(fileName.equals("cluster")) {
				jaxbContext = JAXBContext.newInstance(Cluster.class);
			}else if(fileName.equals("lifeCycle")) {
				jaxbContext = JAXBContext.newInstance(LifecycleInvocation.class);
			}else if(fileName.equals("adminServer")) {
				jaxbContext = JAXBContext.newInstance(Server.class);
			}else if(fileName.equals("bizSystem")) {
				jaxbContext = JAXBContext.newInstance(BizSystemInfo.class);
			}
			 
	        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
	        templet = (T) unmarshaller.unmarshal(file);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		
		if(templet == null) {
			logger.info("템플릿 파일 언마샬 실패");
			System.exit(1);
		}
        return templet;
	}

}
