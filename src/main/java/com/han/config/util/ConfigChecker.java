package com.han.config.util;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.han.config.generator.FileGenerator;
import com.han.config.pojo.Clusters;
import com.han.config.pojo.Repository;
import com.han.config.pojo.Domain;
import com.han.config.pojo.Domain.Listeners;
import com.han.wizard.AnylinkWizard.AppMain;
import com.han.config.pojo.Node;
import com.han.config.pojo.CustomInfo;

public class ConfigChecker {
	private static Logger logger = LoggerFactory.getLogger(ConfigChecker.class);
	List<Domain> domainList;
	List<Clusters> clusterList;
	List<Node> nodeList;
	List<Repository> repository;
	CustomInfo customInfo = null;
	public ConfigChecker(List<Domain> domainList, List<Clusters> clusterList, List<Node> nodeList, List<Repository> repository, CustomInfo customInfo) {
		this.domainList = domainList;
		this.clusterList = clusterList;
		this.nodeList = nodeList;
		this.customInfo = customInfo;
		this.repository = repository;
	}
	
	public boolean node() {
		try {
			if(nodeList.size() <= 0) {
				logger.error("Node 설정이 존재하지 않습니다.");
				return false;
			}
			
			for(Node node : nodeList) {
				if(node.isNull() || node.isEmpty()) {
					logger.error("Node 설정 값이 비어 있습니다. 확인 바랍니다.");
					return false;
				}
				
				int port = Integer.parseInt(node.getPort());
				if(!((0 < port) && (port < 65535))) {
					logger.error("Node 포트 범위를 확인 바랍니다.");
					return false;
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean domian() {
		boolean flag = false;
		try {
			if(domainList.size() <= 0) {
				logger.error("Domain 설정이 존재하지 않습니다.");
				return false;
			}
			
			for(Domain domain : domainList) {
				
				if(domain.isNull() || domain.isEmpty()) {
					logger.error("Domain 설정 값이 비어 있습니다. 확인 바랍니다.");
					return false;
				}
				
				if(nodeList.stream().filter(node -> node.getName().equals(domain.getNode_name())).count() == 0) {
					logger.error("Domain에 설정된 node-name을 확인 바랍니다.");
					return false;
				}
				
				List<Listeners> listeners = domain.getListeners();
				if(listeners.size() <= 0) {
					logger.error("listener 설정이 존재하지 않습니다.");
					return false;
				}
				for(Listeners listener : listeners) {
					
					if(listener.isNull() || listener.isEmpty()) {
						logger.error("listener 설정 값이 비어 있습니다. 확인 바랍니다.");
						return false;
					}
					
					int port = Integer.parseInt(listener.getPort());
					if(!((0 < port) && (port < 65535))) {
						logger.error("listener 포트 범위를 확인 바랍니다.");
						return false;
					}
				}
				
			}
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return flag;
	}
	
	public boolean cluster() {
		List<String> msName = new ArrayList<String>();
		try {
			if(clusterList.size() == 0) {
				logger.info("싱글 설정 입니다.");
				return true;
			}
			
			for(Domain domain : domainList) {
				msName.add(domain.getName());
			}
			for(Clusters cluster : clusterList) {
				
				if(cluster.isNull() || cluster.isEmpty()) {
					logger.error("cluster 설정 값이 비어 있습니다. 확인 바랍니다.");
					return false;
				}
				
				if(cluster.getServers().stream().filter(nodeName -> msName.contains(nodeName)).count() == 0) {
					logger.error("cluster에 포함된 서버가 Domain설정에 존재하지 않습니다.");
					return false;
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean repository() {
		try{
			if(repository.size() == 0 || repository.size() > 1) {
				logger.error("설정 파일에  Repository 데이터 소스는 1개 여야합니다.");
				return false;
			}
			
			Repository source = repository.get(0);
			if(source.isNull() || source.isEmpty()) {
				logger.error("Repository 설정 값이 비어 있습니다. 확인 바랍니다.");
				return false;
			}
			
			int port = Integer.parseInt(source.getPort_number());
			if(!((0 < port) && (port < 65535))) {
				logger.error("Repository 포트 범위를 확인 바랍니다.");
				return false;
			}
			if(source.getVendor().equals("others")) {
				logger.warn("others 타입은 maria DB만 지원합니다.");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean schemaFile() {
		if(!AppMain.adminServerHostName.equals(customInfo.getHostName())) {
			logger.info("MS 설정이므로 schemaFile 검사를 진행하지 않습니다.");
			return true;
		}
		
		File dir = new File("datasource");
		FileFilter fileFilter = new WildcardFileFilter("create-anylink-*.sql");
		File[] files = dir.listFiles(fileFilter);
		if(files.length > 1 || files.length == 0) {
			logger.error("create-anylink 스키마 파일은 오직 1개여야 합니다.");
			return false;
		}
		fileFilter = new WildcardFileFilter("insert-anylink-*.sql");
		files = dir.listFiles(fileFilter);
		if(files.length > 1 || files.length == 0) {
			logger.error("insert-anylink 스키마 파일은 오직 1개여야 합니다.");
			return false;
		}
		
		return true;
	}
}
