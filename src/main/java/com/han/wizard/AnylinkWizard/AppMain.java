package com.han.wizard.AnylinkWizard;


import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.DomainLoadStoreParameter;
import java.util.ArrayList;
import java.util.List;

import org.jtwig.JtwigModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.han.config.ConfigJsonParser;
import com.han.config.generator.FileGenerator;
import com.han.config.pojo.Clusters;
import com.han.config.pojo.Repository;
import com.han.config.pojo.Domain;
import com.han.config.pojo.Node;
import com.han.config.pojo.UserPath;
import com.han.config.util.ConfigChecker;


public class AppMain {
	public static Boolean dev = true;
	String userHome = null;
	private static Logger logger = LoggerFactory.getLogger(AppMain.class);
	ConfigJsonParser configJsonParser = null;
	FileGenerator fileGenerator = null;
	UserPath userPath = null;
	ArrayList<Node> nodes = null;
	ArrayList<Domain> domain = null;
	ArrayList<Clusters> clusters = null;
	ArrayList<Repository> dataSource = null;
	ArrayList<String> logHome = null;
	String propertyName = null;
	String devPath = "/home/tmax/wizard/fin";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		logger.info("CREATE BY Seungsoo_Han");
		AppMain appMain = new AppMain();
		appMain.InitConfig();
		appMain.ValidChecker();
		appMain.NodesBoot();
		appMain.ProfileBoot();
		appMain.DomainBoot();
		appMain.BizSystemBoot();
	}
	public void InitConfig() {
		logger.info("초기화 과정 시작");
		if(AppMain.dev) {
			this.propertyName = "/home/tmax/wizard/setting.json";
		}else {
			this.propertyName = System.getProperty("setting.config.json");
		}
		configJsonParser = new ConfigJsonParser(propertyName);
		nodes = configJsonParser.load("nodes");
		domain = configJsonParser.load("domain");
		clusters = configJsonParser.load("clusters");
		logHome = configJsonParser.load("log_home");
		dataSource = configJsonParser.load("Repository");
		
		userPath = new UserPath();
		userPath.setAnylinkHome(System.getProperty("anylink.home"));
		userPath.setUserHome(System.getProperty("user.dir"));
		userPath.setPassWord(System.getProperty("jeus.passwd"));
		InetAddress local = null;
		try {
			local = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		userPath.setIp(local.getHostAddress());
		userPath.setHostName(local.getHostName());
		userPath.setLogHome(logHome.get(0));
		logger.info(userPath.toString());
		
		fileGenerator = new FileGenerator();;

	}
	
	public void ValidChecker() {
		logger.info("설정 검증 시작");
		ConfigChecker configChecker = new ConfigChecker(domain, clusters, nodes, userPath);
		if(!configChecker.node()) {
			logger.error("node 설정파일 검증 실패");
			System.exit(1);
		}
		if(!configChecker.domian()) {
			logger.error("domain 설정파일 검증 실패");
			System.exit(1);
		}
		if(!configChecker.cluster()) {
			logger.error("clusters 설정파일 검증 실패");
			System.exit(1);
		}
		if(!configChecker.schemaFile()) {
			logger.error("schema 생성 파일 확인 실패");
			System.exit(1);
		}
		
		logger.info("설정 검증 성공");
	}
	
	public void NodesBoot() {
		String fileName = null;
		if(AppMain.dev) {
			fileName = "devPath/nodes_test.xml";
		}else {
			fileName = userPath.getAnylinkHome() + File.separator + "domains" + File.separator + "nodes.xml";
		}
		logger.info("Nodes 과정 시작");
		fileGenerator.runGenerator(nodes, domain, clusters, "nodes", fileName, userPath, null);
		
		logger.info("nodeManager 과정 시작");
		if(AppMain.dev) {
			fileName = "devPath/jeusnm.properties";
		}else {
			fileName = userPath.getAnylinkHome() + File.separator + "nodemanager" + File.separator + "jeusnm.properties";
		}
		fileGenerator.runGenerator(nodes, domain, clusters, "nodeManager", fileName, userPath, null);
	}
	
	public void ProfileBoot() {
		String fileName = null;
		Domain adminServer = null;
		if(AppMain.dev) {
			fileName = "devPath/profile";
		}else {
			fileName = userHome + File.separator + ".bash_profile";
			File tmpFile = new File(fileName);
			if(!tmpFile.exists()) {
				fileName = userHome + File.separator + ".profile";
			}
		}
		logger.info("Profile 과정 시작");
		
		for(int i = 0; i < domain.size(); i++) {
			Domain ms = domain.get(i);
			if(ms.getName().equals("adminServer")){
				adminServer = ms;
				break;
			}		
		}
		
		if(adminServer.getNode_name().equals(userPath.getHostName())) {
			logger.info("DAS Profile 생성");
			fileGenerator.runGenerator(nodes, domain, clusters, "dasProfile", fileName, userPath, null);
		}else {
			logger.info("MS Profile 생성");
			fileGenerator.runGenerator(nodes, domain, clusters, "msProfile", fileName, userPath, null);
		}
	}
	
	public void DomainBoot() {
		String fileName = null;
		if(AppMain.dev) {
			fileName = "devPath/domain_test.xml";
		}else {
			fileName = userPath.getAnylinkHome() + File.separator + "domains" + File.separator + userPath.getDoaminName() + File.separator + "config" + File.separator + "domain.xml";
		}
		logger.info("Domain 과정 시작");
		
		fileGenerator.runGenerator(nodes, domain, clusters, "domain", fileName, userPath, dataSource.get(0));
	}
	
	public void BizSystemBoot() {
		String fileName = null;
		
		logger.info("BizSystemConfig 과정 시작");
		
		for(int i = 0; i < domain.size(); i++) {
			Domain ms = domain.get(i);
			if(userPath.getHostName().equals(ms.getNode_name()) && !ms.getName().equals("adminServer")) {
				if(AppMain.dev) {
					fileName = "devPath/bizConfigs" + i + ".xml";
				}else {
					fileName = userPath.getAnylinkHome() + File.separator + "domains" + File.separator + userPath.getDoaminName() + File.separator + "servers" + File.separator + ms.getName() + File.separator + "repository" + File.separator + "bizsystem" + File.separator + "bizsystem.config";
				}
				fileGenerator.runGenerator(nodes, domain, clusters, "bizConfig", fileName, userPath, null);
			}
		}
	}
	
	public void RepositoryBoot() {
		logger.info("RepositoryDB 스키마 생성 시작");
		
	}
}
