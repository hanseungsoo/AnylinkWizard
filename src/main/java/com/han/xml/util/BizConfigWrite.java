package com.han.xml.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tmaxsoft.schemas.anylink.BizSystemInfo;
import com.tmaxsoft.xml.ns.jeus.Domain;

public class BizConfigWrite {
	private static Logger logger = LoggerFactory.getLogger(BizConfigWrite.class);
	private String bizSystemConfig = null;
	private String loggingTemplate = null;
	private JSONArray domainArray = null;
	public BizConfigWrite(JSONArray domainArray) {
		this.domainArray = domainArray;
	}
	
	public void ObjectToFile(List<BizSystemInfo> bizSystemList, List<String> fileList) {
        JAXBContext jaxbContext;
		logger.info("bizsystem 파일 생성합니다.");
		
		try {
			jaxbContext = JAXBContext.newInstance(BizSystemInfo	.class);
	        Marshaller marshaller = jaxbContext.createMarshaller();
	        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
	        //marshaller.setProperty("com.sun.xml.internal.bind.xmlHeaders", "<?xml version=\"1.0\"?>");
	        marshaller.setProperty("com.sun.xml.internal.bind.xmlHeaders", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
	        
	        for(int i = 0; i < fileList.size(); i++) {
	        	String filePath = fileList.get(i);
	        	BizSystemInfo bizSystem = bizSystemList.get(i);
	        	
	        	bizSystemConfig = filePath + File.separator + "bizsystem.config";
	        	loggingTemplate = filePath + File.separator + "logging.properties.template";
	        	logger.info("파일 경로 : " + bizSystemConfig);
	        	
	        	File bizFile = new File(bizSystemConfig);
	        	if(!bizFile.getParentFile().mkdirs()) {
		        	logger.info(bizSystemConfig + " 파일 생성 실패");
		        	System.exit(1);
		        }
	        	
		        marshaller.marshal(bizSystem, bizFile);
		        
		        logger.info("logging.properties.template 파일 생성합니다.");
				logger.info("파일 경로 : " + loggingTemplate);
				File loggingFile = new File(loggingTemplate);
				try {
					FileWriter fw = new FileWriter(loggingFile);
					fw.write("");
					fw.flush();
					
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.exit(1);
				}
	        }

	        
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public List<BizSystemInfo> FileToObject() {
		
		List<BizSystemInfo> bizList = new ArrayList<BizSystemInfo>();
		TemplateServerRead<BizSystemInfo> bizTemp = new TemplateServerRead<BizSystemInfo>();
		
		for(int i = 0; i < domainArray.size(); i++) {
			JSONObject msJson = (JSONObject) domainArray.get(i);
			if(!((String)msJson.get("name")).equals("adminServer")) {
				BizSystemInfo bizSystem = bizTemp.XmlToObject("bizSystem");
				bizSystem.getNodeList().getNode().setName((String)msJson.get("name"));
				
				bizList.add(bizSystem);
			}
		}
		
		return bizList;
	}
}
