package com.han.wizard.AnylinkWizard;


import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.jtwig.JtwigModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.han.config.ConfigJsonParser;
import com.han.config.generator.FileGenerator;
import com.han.config.pojo.Clusters;
import com.han.config.pojo.Domain;
import com.han.config.pojo.Node;
import com.han.config.pojo.UserPath;


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
	String propertyName = null;
	String devPath = "/home/tmax/wizard/fin";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		logger.info("CREATE BY Seungsoo_Han");
		AppMain appMain = new AppMain();
		appMain.InitConfig();
		appMain.NodesBoot();
		appMain.ProfileBoot();
		appMain.DomainBoot();
	}
	public void InitConfig() {
		logger.info("초기화 과정 시작");
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
		fileGenerator = new FileGenerator();;
		if(AppMain.dev) {
			this.propertyName = "/home/tmax/wizard/setting.json";
		}else {
			this.propertyName = System.getProperty("setting.config.json");
		}
		configJsonParser = new ConfigJsonParser(propertyName);
		nodes = configJsonParser.load("nodes");
		domain = configJsonParser.load("domain");
		clusters = configJsonParser.load("clusters");
	}
	
	public void ValidChecker() {
		
	}
	
	public void NodesBoot() {
		String fileName = null;
		if(AppMain.dev) {
			fileName = "devPath/nodes_test.xml";
		}else {
			fileName = userPath.getAnylinkHome() + File.separator + "domains" + File.separator + "nodes.xml";
		}
		logger.info("Nodes 과정 시작");
		fileGenerator.runGenerator(nodes, domain, clusters, "nodes", fileName, userPath);
	}
	
	public void ProfileBoot() {
		String fileName = null;
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
		
		fileGenerator.runGenerator(nodes, domain, clusters, "dasProfile", fileName, userPath);
	}
	
	public void DomainBoot() {
		String fileName = null;
		if(AppMain.dev) {
			fileName = "devPath/domain_test.xml";
		}else {
			fileName = userPath.getAnylinkHome() + File.separator + "domains" + File.separator + userPath.getDoaminName() + File.separator + "config" + File.separator + "domain.xml";
		}
		logger.info("Domain 과정 시작");
		
		fileGenerator.runGenerator(nodes, domain, clusters, "domain", fileName, userPath);
	}
}
