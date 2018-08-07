package com.han.xml.util;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tmaxsoft.xml.ns.jeus.Domain;

public class DomainFileRW  {
	private static Logger logger = LoggerFactory.getLogger(DomainFileRW.class);
	private String FilePath;
	
	public DomainFileRW(String InstallPath) {
		this.FilePath = InstallPath;
	}

	public Domain XmlToObject() {
		logger.info("domain.xml 파일 로딩합니다.");
		logger.info("파일 경로 : " + FilePath);
    	File file = new File(FilePath);
        Domain domain = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Domain.class);
	        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			domain = (Domain) unmarshaller.unmarshal(file);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		
		if(domain == null) {
			logger.info("domain.xml 파일 로딩 실패");
			System.exit(1);
		}
        return domain;
	}
	
	public void ObjectToXml(Domain domains) {
        JAXBContext jaxbContext;
		logger.info("domain.xml 파일 생성합니다.");
		logger.info("파일 경로 : " + FilePath);
		try {
			jaxbContext = JAXBContext.newInstance(Domain.class);
	        Marshaller marshaller = jaxbContext.createMarshaller();
	        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
	        //marshaller.setProperty("com.sun.xml.internal.bind.xmlHeaders", "<?xml version=\"1.0\"?>");
	        marshaller.setProperty("com.sun.xml.internal.bind.xmlHeaders", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
	        
	        
	        marshaller.marshal(domains, new File(FilePath));
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	}

}
