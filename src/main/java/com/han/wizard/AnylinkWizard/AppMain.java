package com.han.wizard.AnylinkWizard;


import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.han.config.ConfigJsonParser;
import com.han.config.ConfigOsParser;
import com.han.config.generator.FileGenerator;
import com.han.config.generator.SchemaGenerator;
import com.han.config.pojo.Clusters;
import com.han.config.pojo.Domain;
import com.han.config.pojo.Node;
import com.han.config.pojo.Repository;
import com.han.config.pojo.CustomInfo;
import com.han.config.util.ConfigChecker;
import com.han.config.util.FileMover;


public class AppMain {
	public static Boolean dev = false;
	String userHome = null;
	private static Logger logger = LoggerFactory.getLogger(AppMain.class);
	ConfigJsonParser configJsonParser = null;
	ConfigOsParser configOsParser = null;
	FileGenerator fileGenerator = null;
	SchemaGenerator schemaGenerator = null;
	CustomInfo customInfo = null;
	ArrayList<Node> nodes = null;
	ArrayList<Domain> domain = null;
	ArrayList<Clusters> clusters = null;
	ArrayList<Repository> repository = null;
	ArrayList<String> logHome = null;
	String propertyName = null, patchPath = null; 
	public static String adminServerHostName = null;
	FileMover fileMover = null;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		logger.info("CREATE BY Seungsoo_Han");
		AppMain appMain = new AppMain();
		
		// 기본 초기화 과정
		appMain.initConfig();
		
		// 설정파일 검증 과정
		appMain.validCheck();
		appMain.NodesBoot();
		appMain.ProfileBoot();
		appMain.DomainBoot();
		appMain.BizSystemBoot();
		appMain.RepositoryBoot();
		appMain.PatchBoot();
		appMain.DisConfigBoot();

	}
	
	/*
	 *  설정파일 읽기 및 환경설정 읽기
	 */
	public void initConfig() {
		logger.info("초기화 과정 시작");
		
		// 설정파일 경로 읽기
		if(AppMain.dev) {
			this.propertyName = "/home/tmax/wizard/setting.json";
		}else {
			this.propertyName = System.getProperty("setting.config.json");
		}
		
		// 필요 객체 생성
		configJsonParser = new ConfigJsonParser(propertyName);
		configOsParser = new ConfigOsParser();
		fileGenerator = new FileGenerator();
		schemaGenerator = new SchemaGenerator();
		fileMover = new FileMover();
		
		//설정파일 파싱
		nodes = configJsonParser.load("nodes");
		domain = configJsonParser.load("domain");
		clusters = configJsonParser.load("clusters");
		logHome = configJsonParser.load("log_home");
		repository = configJsonParser.load("Repository");
		
		
		// 커스텀 설정 읽기
		customInfo = new CustomInfo();
		customInfo.setAnylinkHome(configOsParser.getEnv("ANYLINK_HOME"));
		customInfo.setUserHome(configOsParser.getProp("user.home"));
		customInfo.setPassWord(configOsParser.getProp("jeus.passwd"));
		customInfo.setDoaminName(configOsParser.getProp("domain.name"));
		customInfo.setIp(configOsParser.getIp());
		customInfo.setHostName(configOsParser.getHostName());
		customInfo.setLogHome(logHome.get(0));
		
		// 커스텀 설정 출력
		logger.info(customInfo.toString());
		
		// DAS 호스트네임 찾기 
		for(int i = 0; i < domain.size(); i++) {
			Domain ms = domain.get(i);
			if(ms.getName().equals("adminServer")){
				adminServerHostName = ms.getNode_name();
				break;
			}		
		}
		logger.info("DAS 지정 서버 호스트이름 : " + adminServerHostName);
	}
	
	/*
	 * 설정파일 검증
	 */
	public void validCheck() {
		logger.info("설정 검증 시작");
		ConfigChecker configChecker = new ConfigChecker(domain, clusters, nodes, repository, customInfo);
		if(!configChecker.node()) {
			logger.error("Node 설정파일 검증 실패");
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
		if(!configChecker.repository()) {
			logger.error("Repository 설정파일 검증 실패");
			System.exit(1);
		}
		if(!configChecker.schemaFile()) {
			logger.error("schema파일 확인 실패");
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
			fileName = userPath.getUserHome() + File.separator + ".bash_profile";
			File tmpFile = new File(fileName);
			if(!tmpFile.exists()) {
				fileName = userPath.getUserHome() + File.separator + ".profile";
			}
		}
		logger.info("Profile 과정 시작");
		
		
		if(adminServerHostName.equals(userPath.getHostName())) {
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
		
		if(adminServerName.equals(userPath.getHostName())) {
			fileGenerator.runGenerator(nodes, domain, clusters, "domain", fileName, userPath, repository.get(0));
		}else {
			logger.info("ms 서버는 domain.xml 설정을 진행 하지 않습니다.");
		}
		
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
		if(!adminServerName.equals(userPath.getHostName())){
			logger.info("ms서버는 RepositoryDB 스키마 생성을 하지 않습니다.");
			return;
		}
		String srcPath = null, destPath = null;
		logger.info("RepositoryDB 스키마 생성 시작");
		if(AppMain.dev) {
			srcPath = "datasource";
			destPath = "devPath";
		}else {
			srcPath = "datasource";
			destPath = userPath.getAnylinkHome() + File.separator + "lib" + File.separator + "datasource";
		}
		
		
		schemaGenerator.runGenerator(repository.get(0));
		
		switch(repository.get(0).getVendor().toLowerCase()) {
		case "tibero":
			fileMover.runFileMove(srcPath, destPath, "tibero*.jar");
			break;
		case "oracle":
			fileMover.runFileMove(srcPath, destPath, "ojdbc*.jar");
			break;
		case "others":
			fileMover.runFileMove(srcPath, destPath, "mariadb*.jar");
			break;
		}
	}
	
	public void PatchBoot() {
		String srcPath = null, destPath = null;
		logger.info("바이너리 패치 시작");
		if(AppMain.dev) {
			srcPath = "patch";
			destPath = "devPath";
		}else {
			srcPath = "patch";
			destPath = userPath.getAnylinkHome() + File.separator + "domains" + File.separator + userPath.getDoaminName() + File.separator + "lib" + File.separator + "application";
		}
		fileMover.runFileMove(srcPath, destPath, "anylink-distribution*.jar");
		
		if(AppMain.dev) {
			srcPath = "patch";
			destPath = "devPath";
		}else {
			srcPath = "patch";
			destPath = userPath.getAnylinkHome() + File.separator + "domains" + File.separator + userPath.getDoaminName() + File.separator + ".applications" + File.separator + "anylink-admin";
		}
		fileMover.runFileMove(srcPath, destPath, "anylink-admin*.war");
	}
	
	public void DisConfigBoot() {
		if(!adminServerName.equals(userPath.getHostName())){
			logger.info("ms서버는 dis-config.xml 생성을 하지 않습니다.");
			return;
		}
		String fileName = null;
		
		logger.info("dis-config 과정 시작");
		if(AppMain.dev) {
			fileName = "devPath/dis-config.xml";
		}else {
			fileName = userPath.getAnylinkHome() + File.separator + "dis_config" + File.separator + "dis-config.xml";
		}
		fileGenerator.runGenerator(nodes, domain, clusters, "dis-config", fileName, userPath, repository.get(0));
	}
}
