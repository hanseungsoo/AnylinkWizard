package com.han.wizard.AnylinkWizard;


import java.io.File;
import java.util.List;

import org.json.simple.JSONArray;
import org.jtwig.JtwigModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.han.config.ConfigJsonParser;
import com.han.config.generator.BizconfigGenerator;
import com.han.config.generator.ClusterGenerator;
import com.han.config.generator.DomainGenerator;
import com.han.config.generator.NodeGenerator;
import com.han.config.generator.ProfileGenerator;
import com.han.xml.util.AliasFileRW;
import com.han.xml.util.BizConfigWrite;
import com.han.xml.util.DomainFileRW;
import com.han.xml.util.NodesFileRW;
import com.tmaxsoft.schemas.anylink.BizSystemInfo;
import com.tmaxsoft.xml.ns.jeus.Domain;
import com.tmaxsoft.xml.ns.jeus.Nodes;

public class AppMain {
	ConfigJsonParser configJsonParser;
	JSONArray domainConfigJson, nodeConfigJson, clusterConfigJson;
	public static Boolean dev = false;
	String home = null;
	private static Logger logger = LoggerFactory.getLogger(AppMain.class);
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		logger.info("CREATE BY Seungsoo_Han");
		AppMain appMain = new AppMain();
		appMain.Init();
		appMain.NodesBoot();
		appMain.DomainBoot();
		appMain.ProfileBoot();
		appMain.BizConfigBoot();
	}
	public void Init() {
		logger.info("초기화 과정 시작");
		configJsonParser = new ConfigJsonParser();
		home = System.getProperty("anylink.home");
		domainConfigJson = configJsonParser.load("domain");
		nodeConfigJson = configJsonParser.load("nodes");
		clusterConfigJson = configJsonParser.load("clusters");
	}
	
	public void ProfileBoot() {
		logger.info("프로파일 설정 페이즈 시작");
		AliasFileRW alias = new AliasFileRW(System.getProperty("user.home"));
		
		logger.info("프로파일 설정 생성 시작");
		ProfileGenerator profile = new ProfileGenerator(domainConfigJson);
		JtwigModel model = profile.generator();
		alias.setProfile("das", model);
	}
	
	public void NodesBoot() {
		logger.info("노드 설정 페이즈 시작");
		
		NodesFileRW nfrw = null;
		if(AppMain.dev) {
			nfrw = new NodesFileRW("C:\\Users\\Han\\Desktop\\소스\\tmaxProBus\\workspace\\config\\nodes.xml");
		}else {
			String fileName = home + File.separator + "domains" + File.separator + "nodes.xml";
			nfrw = new NodesFileRW(fileName);
		}
		
		Nodes XmlNodes = nfrw.XmlToObject();
		
		NodeGenerator nodeGenerator = new NodeGenerator(nodeConfigJson, XmlNodes);
		Nodes newNodes = nodeGenerator.generator();
		
		logger.info("노드파일 생성 시작");
		nfrw.ObjectToXml(newNodes);
	}
	
	public void DomainBoot() {
		logger.info("도메인 설정 페이즈 시작");
		DomainFileRW dfrw = null;
		if(AppMain.dev) {
			dfrw = new DomainFileRW("C:\\Users\\Han\\Desktop\\소스\\tmaxProBus\\workspace\\config\\domain.xml");
		}else {
			String fileName = home + File.separator + "domains" + File.separator + System.getProperty("anylink.domain") + File.separator + "config" + File.separator + "domain.xml";
			dfrw = new DomainFileRW(fileName);
		}

		Domain XmlDomain = dfrw.XmlToObject();
		
		DomainGenerator domainGenerator = new DomainGenerator(domainConfigJson, XmlDomain);
		Domain newDomain = domainGenerator.generator();
		
		newDomain = addClusters(newDomain, domainConfigJson);
		
		logger.info("도메인파일 생성 시작");
		dfrw.ObjectToXml(newDomain);
	}
	
	public Domain addClusters(Domain newDomain, JSONArray domainArray) {
		logger.info("클러스터 설정 페이즈 시작");
		
		ClusterGenerator clustersGenerator = new ClusterGenerator(clusterConfigJson, domainArray, newDomain);
		return clustersGenerator.generator();
	}
	
	public void BizConfigBoot() {
		logger.info("비즈컨피그 파일 설정 페이즈 시작");
		BizConfigWrite bcw = null;
		bcw = new BizConfigWrite(domainConfigJson);
		List<BizSystemInfo> bizSystemList = bcw.FileToObject();
		
		BizconfigGenerator bizGenerator = new BizconfigGenerator(domainConfigJson, home);
		List<String> fileList = bizGenerator.generator();
		
		bcw.ObjectToFile(bizSystemList, fileList);
	}

}
