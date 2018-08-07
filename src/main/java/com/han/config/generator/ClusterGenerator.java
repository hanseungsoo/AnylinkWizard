package com.han.config.generator;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.han.xml.util.TemplateServerRead;
import com.tmaxsoft.xml.ns.jeus.Domain;
import com.tmaxsoft.xml.ns.jeus.Domain.Clusters;
import com.tmaxsoft.xml.ns.jeus.Domain.Clusters.Cluster;
import com.tmaxsoft.xml.ns.jeus.Domain.DeployedApplications.DeployedApplication;
import com.tmaxsoft.xml.ns.jeus.Domain.LifecycleInvocation;
import com.tmaxsoft.xml.ns.jeus.Domain.Servers.Server;

public class ClusterGenerator {
	private static Logger logger = LoggerFactory.getLogger(ClusterGenerator.class);
	private JSONArray clusterArray;
	private JSONArray domainArray;
	private Domain domain;
	private TemplateServerRead<LifecycleInvocation> lifeCycleRead;
	private TemplateServerRead<Cluster> clusterRead;
	
	public ClusterGenerator(JSONArray clusterArray, JSONArray domainArray, Domain domain) {
		this.lifeCycleRead = new TemplateServerRead<LifecycleInvocation>();
		this.clusterRead = new TemplateServerRead<Cluster>();
		this.clusterArray = clusterArray;
		this.domainArray = domainArray;
		this.domain = domain;
	}
	
	public Domain generator() {
		logger.info("설정 파일을 반영 합니다.");
		
		Clusters clusters = new Clusters();
		
		for(int i=0 ; i< clusterArray.size() ; i++){
			JSONObject clusterObj = (JSONObject) clusterArray.get(i);
			Cluster cluster = clusterRead.XmlToObject("cluster");
			
			cluster.setName((String)clusterObj.get("name"));
			
			JSONArray serverItem = (JSONArray) clusterObj.get("servers");
			List<String> serverList = cluster.getServers().getServerName();
			serverList.clear();
			for(int x = 0; x < serverItem.size(); x++) {
				String serverName = (String) serverItem.get(x);
				if(serverName.equals("adminServer")) {
					logger.error("클러스터 서버 리스트 설정 오류");
					System.exit(1);
				}
				for(int y = 0; y < domainArray.size(); y++) {
					JSONObject domainObj = (JSONObject) domainArray.get(i);
					
					String configServer = (String)domainObj.get("name");
					if(!serverName.equals(configServer) && !configServer.equals("adminServer")){
						logger.error("클러스터 서버 리스트 설정 오류");
						System.exit(1);
					}
				}
				serverList.add(serverName);
			}
			
			clusters.getCluster().add(cluster);
		}
		domain.setClusters(clusters);
		logger.info("클러스터 라이프사이클 추가");
		LifecycleInvocation lifeCycle = lifeCycleRead.XmlToObject("lifeCycle");
		domain.setLifecycleInvocation(lifeCycle);
		logger.info("기존 라이프사이클 제거");
		removeLifeCycle();
		logger.info("anylink-http-servlet 추가");
		addHttpServlet();
		return domain;
	}
	
	public void removeLifeCycle() {
		for(int i=0 ; i< clusterArray.size() ; i++){
			JSONObject clusterObj = (JSONObject) clusterArray.get(i);
			JSONArray serversObj = (JSONArray)clusterObj.get("servers");
			List<Server> serverList = domain.getServers().getServer();
			
			for(int x = 0; x < serversObj.size(); x++) {
				String serverName = (String)serversObj.get(x);
				
				for(Server server : serverList) {
					if(server.getName().equals(serverName)) {
						server.setLifecycleInvocation(null);
					}
				}
			}
			
		}
	}
	public void addHttpServlet() {
		List<DeployedApplication> deployedList = domain.getDeployedApplications().getDeployedApplication();
		
		for(DeployedApplication application : deployedList) {
			if(application.getId().equals("anylink-http-servlet")) {
				Domain.DeployedApplications.DeployedApplication.Targets.Cluster cluster =  new Domain.DeployedApplications.DeployedApplication.Targets.Cluster();
				
				
				Domain.DeployedApplications.DeployedApplication.Targets.Server server = new Domain.DeployedApplications.DeployedApplication.Targets.Server();
				for(int x=0; x < domainArray.size(); x++) {
					JSONObject serverObj = (JSONObject) domainArray.get(x);
					String serverName = (String)serverObj.get("name");
					Boolean flag = true;
					if(!serverName.equals("adminServer")) {
						for(int i=0; i< clusterArray.size(); i++){
							JSONObject clusterObj = (JSONObject) clusterArray.get(i);
							
							JSONArray clusterServerList = (JSONArray) clusterObj.get("servers");
							if(clusterServerList.contains(serverName)) {
								flag = false;
							}
						}
						if(flag) {
							server.getName().add(serverName);
						}
					}
				}
				for(int i=0; i< clusterArray.size(); i++){
					JSONObject clusterObj = (JSONObject) clusterArray.get(i);
					String clusterName = (String)clusterObj.get("name");
					cluster.getName().add(clusterName);
				}
				if(cluster.getName().size() > 0) {
					application.getTargets().setCluster(cluster);
				}else {
					application.getTargets().setCluster(null);
				}
				if(server.getName().size() > 0) {
					application.getTargets().setServer(server);
				}else {
					application.getTargets().setServer(null);
				}
				
			}
		}
	}
}
