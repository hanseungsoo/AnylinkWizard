package com.han.config.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.han.config.pojo.Clusters;
import com.han.config.pojo.Repository;
import com.han.config.pojo.Domain;
import com.han.config.pojo.Domain.Listeners;
import com.han.wizard.AnylinkWizard.AppMain;
import com.han.config.pojo.Node;
import com.han.config.pojo.CustomInfo;
import com.han.config.util.EncryptUtil;

public class FileGenerator {
	private static Logger logger = LoggerFactory.getLogger(FileGenerator.class);
	
	public void runGenerator(List<Node> nodeList,List<Domain> domainList, List<Clusters> clustersList, String key, String path, CustomInfo customInfo, Repository repository) {
		FileOutputStream fos = null;
		JtwigModel model = null;
		JtwigTemplate template = null;
		boolean overWriteFlag = false;
		
		logger.info( key + " 파일 생성합니다.");
		logger.info("파일 경로 : " + path);
		
		// 파일 생성
		File file = new File(path);
		// 경로 폴더 만들기
		file.getParentFile().mkdirs();
		
		
		if(key.equals("nodes")) {
			
			template = JtwigTemplate.classpathTemplate("nodes");
			model = nodeGenerator(nodeList);
			
		}else if(key.equals("dasProfile")) {
			
			overWriteFlag = true;
			template = JtwigTemplate.classpathTemplate("dasAlias");
			model = dasProfileGenerator(nodeList, domainList, customInfo);

		}else if(key.equals("msProfile")) {
			
			overWriteFlag = true;
			template = JtwigTemplate.classpathTemplate("msAlias");
			model = msProfileGenerator(nodeList, domainList, customInfo);

		}else if(key.equals("domain")) {
			
			overWriteFlag = false;
			template = JtwigTemplate.classpathTemplate("domain");
			model = domainGenerator(domainList, clustersList, customInfo, repository);

		}else if(key.equals("bizSystem")) {
			
			template = JtwigTemplate.classpathTemplate("bizSystem");
			model = bizSystemGenerator(path);
			
		}else if(key.equals("nodeManager")) {
			
			template = JtwigTemplate.classpathTemplate("nodeManager");
			model = nodeManagerGenerator(nodeList, customInfo);
		
		}else if(key.equals("disConfig")) {
		
			template = JtwigTemplate.classpathTemplate("dis_config");
			model = disConfigGenerator(customInfo, repository);
		}
		
		// 파일 생성 시작
		try {
			fos = new FileOutputStream(file, overWriteFlag);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		template.render(model, fos);
		
		logger.info(key + " 파일 생성 완료");
	}
	
	public JtwigModel nodeGenerator(List<Node> nodeList) {
		JtwigModel model = null;
		model = JtwigModel.newModel().with("nodeList" , nodeList);
		
		return model;
	}
	public JtwigModel dasProfileGenerator(List<Node> nodeList, List<Domain> domainList, CustomInfo customInfo) {
		JtwigModel model = null;
		String dasNodePort = null;
		List<String> msBasePorts = new ArrayList<String>();
		List<String> msNames = new ArrayList<String>();
		Domain adminServer = null;
		
		for(Domain domainItem : domainList) {
			
			// adminServer 확인
			if(domainItem.getName().equals("adminServer")) {
				adminServer = domainItem;
				continue;
			}
			
			if(domainItem.getNode_name().equals(customInfo.getHostName()) && !domainItem.getName().equals("adminServer")) {
				// DAS 서버의 MS 리스트
				msNames.add(domainItem.getName());
				
				// MS BASE 포트 확인
				for(Listeners listeners : domainItem.getListeners()) {
					if(listeners.getName().equals("BASE")) {
						msBasePorts.add(listeners.getPort());
					}
				}
			}
		}
		
		// NodeManager 리스트
		for(Node node : nodeList) {
			if(node.getName().equals(customInfo.getHostName())) {
				dasNodePort = node.getPort();
				break;
			}
		}
		model = JtwigModel.newModel().with("msNames" , msNames)
				 .with("customInfo", customInfo)
				 .with("msBasePorts", msBasePorts)
				 .with("dasNodePort", dasNodePort)
				 .with("adminServer", adminServer);
		
		return model;
	}
	
	public JtwigModel msProfileGenerator(List<Node> nodeList, List<Domain> domainList, CustomInfo customInfo) {
		JtwigModel model = null;
		String msNodePort = null;
		List<String> msBasePorts = new ArrayList<String>();
		List<String> msNames = new ArrayList<String>();
		Domain adminServer = null;
		
		for(Domain domainItem : domainList) {
			
			// adminServer 확인
			if(domainItem.getName().equals("adminServer")) {
				adminServer = domainItem;
				continue;
			}
			
			if(domainItem.getNode_name().equals(customInfo.getHostName()) && !domainItem.getName().equals("adminServer")) {
				// MS 서버의 MS 리스트
				msNames.add(domainItem.getName());
				
				// MS BASE 포트 확인
				for(Listeners listeners : domainItem.getListeners()) {
					if(listeners.getName().equals("BASE")) {
						msBasePorts.add(listeners.getPort());
					}
				}
			}
		}
		
		// NodeManager 리스트
		for(Node node : nodeList) {
			if(node.getName().equals(customInfo.getHostName())) {
				msNodePort = node.getPort();
				break;
			}
		}
		
		model = JtwigModel.newModel().with("msNames" , msNames)
				 .with("customInfo", customInfo)
				 .with("msBasePorts", msBasePorts)
				 .with("msNodePort", msNodePort)
				 .with("adminServer", adminServer);
		return model;
	}
	
	public JtwigModel domainGenerator(List<Domain> domainList, List<Clusters> clustersList, CustomInfo customInfo, Repository repository) {
		JtwigModel model = null;
		Random rnd = new Random();
		Domain adminServer = null;
		List<Domain> msServers = new ArrayList<Domain>();
		// 도메인 ID 랜덤 생성
		int n = 100000000 + rnd.nextInt(900000000);
		
		for(Domain domainItem : domainList) {
			// adminServer 확인
			if(domainItem.getName().equals("adminServer")) {
				adminServer = domainItem;
				continue;
			}else {
				msServers.add(domainItem);
			}
			
			// MS서버가 클러스터인지 아닌지에 따라 라이프사이클 생성 여부 판별
			for(Clusters cluster : clustersList) {
				if(cluster.getServers().contains(domainItem.getName())) {
					domainItem.setLifeCycle("false");
				}else {
					domainItem.setLifeCycle("true");
				}
			}
		}
		
		model = JtwigModel.newModel().with("adminServer" , adminServer)
				 .with("msServers", msServers)
				 .with("customInfo", customInfo)
				 .with("clustersList", clustersList)
				 .with("repository", repository)
				 .with("id", n);
		
		return model;
	}
	
	public JtwigModel bizSystemGenerator(String path) {
		JtwigModel model = null;
		String msName = null;
		
		// 생성하려는 bizSystem의 ms이름을 추출
		String[] msNames = path.split(File.separator);
		msName = msNames[msNames.length-4];
		
		model = JtwigModel.newModel().with("msName" , msName);
		
		return model;
	}
	
	public JtwigModel nodeManagerGenerator(List<Node> nodeList, CustomInfo customInfo) {
		JtwigModel model = null;
		
		
		// 호스트 이름이랑 같은 노드매니저 설정 추출
		for(Node node: nodeList) {
			if(node.getName().equals(customInfo.getHostName())) {
				model = JtwigModel.newModel().with("customInfo" , customInfo)
											 .with("node", node);
				break;
			}
		}
		
		return model;
	}
	
	public JtwigModel disConfigGenerator(CustomInfo customInfo, Repository repository) {
		JtwigModel model = null;
		
		String vendor = null;
		String encryptStr = null;
		
		// jeus 패스워드 입력을 위해 암호화 유틸 필요
		EncryptUtil encUtil = new EncryptUtil();
		encryptStr = encUtil.encode(customInfo.getPassWord());
		
		// 레파지토리 DB는 3가지 지원
		switch(repository.getVendor().toLowerCase()){
			case "oracle" :
				vendor = "oracle";
				break;
			case "tibero" :
				vendor = "tibero";
				break;
			case "others" :
				vendor = "maria";
				break;
		}
		
		model = JtwigModel.newModel().with("customInfo" , customInfo)
				 .with("repository", repository)
				 .with("vendor", vendor)
				 .with("encryptStr", encryptStr);
		
		return model;
	}
}
