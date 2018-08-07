package com.han.xml.util;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tmaxsoft.xml.ns.jeus.Nodes;

public class NodesFileRW {
	private static Logger logger = LoggerFactory.getLogger(NodesFileRW.class);
	private String FilePath;
	
	public NodesFileRW(String InstallPath) {
		this.FilePath = InstallPath;
	}
	
	public Nodes XmlToObject() {
		logger.info("Nodes.xml 파일 로딩합니다.");
		logger.info("파일 경로 : " + this.FilePath);
    	File file = new File(this.FilePath);
        Nodes nodes = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Nodes.class);
	        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			nodes = (Nodes) unmarshaller.unmarshal(file);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		
		if(nodes == null) {
			logger.info("Nodes.xml 파일 로딩 실패");
			System.exit(1);
		}
        return nodes;
	}
	
	public void ObjectToXml(Nodes nodes) {
        JAXBContext jaxbContext;
		logger.info("Nodes.xml 파일 생성합니다.");
		logger.info("파일 경로 : " + this.FilePath);
		try {
			jaxbContext = JAXBContext.newInstance(Nodes.class);
	        Marshaller marshaller = jaxbContext.createMarshaller();
	        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
	        marshaller.setProperty("com.sun.xml.internal.bind.xmlHeaders", "<?xml version=\"1.0\"?>");
	        //marshaller.setProperty("com.sun.xml.internal.bind.xmlHeaders", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
	        marshaller.marshal(nodes, new File(FilePath));
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	}

}
