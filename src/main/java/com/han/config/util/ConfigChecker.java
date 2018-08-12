package com.han.config.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.han.config.generator.FileGenerator;
import com.han.config.pojo.Clusters;
import com.han.config.pojo.DataSource;
import com.han.config.pojo.Domain;
import com.han.config.pojo.Domain.Listeners;
import com.han.config.pojo.Node;

public class ConfigChecker {
	private static Logger logger = LoggerFactory.getLogger(ConfigChecker.class);
	List<Domain> domainList;
	List<Clusters> clusterList;
	List<Node> nodeList;
	List<DataSource> dataSource;
	public ConfigChecker(List<Domain> domainList, List<Clusters> clusterList, List<Node> nodeList) {
		this.domainList = domainList;
		this.clusterList = clusterList;
		this.nodeList = nodeList;
	}
	
	public boolean node() {
		try {
			if(nodeList.size() == 0) {
				return false;
			}
			for(int i = 0; i < nodeList.size(); i++) {
				Node node = nodeList.get(i);
				
				if(node.getHost().equals("") || node.getName().equals("") || node.getPort().equals("") || node.getUse_ssl().equals("")) {
					return false;
				}
				
				int port = Integer.parseInt(node.getPort());
				if(!((0 < port) && (port < 665535))) {
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
			for(int i = 0; i < domainList.size(); i++) {
				Domain domain = domainList.get(i);
				
				if(domain.getJvm_option().equals("") || domain.getName().equals("") || domain.getNode_name().equals("") || domain.getLog_home().equals("") ||
				   domain.getRotation_dir().equals("") || domain.getUse_web_engine().equals("") || domain.getUse_ejb_engine().equals("") || domain.getUse_jms_engine().equals("")) {
					return false;
				}
				
				
				for(int j = 0; j < nodeList.size(); j ++) {
					Node node = nodeList.get(j);
					if(domain.getNode_name().equals(node.getName())) {
						flag = true;
					}
				}
				
				List<Listeners> listeners = domain.getListeners();
				if(listeners.size() <= 0) {
					return false;
				}
				for(int x = 0; x < listeners.size(); x++) {
					Listeners listenersItem = listeners.get(x);
					if(listenersItem.getAddress().equals("") || listenersItem.getName().equals("") || listenersItem.getPort().equals("")) {
						return false;
					}
					
					int port = Integer.parseInt(listenersItem.getPort());
					if(!((0 < port) && (port < 665535))) {
						flag = false;
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
				logger.info("000");
				return true;
			}
			for(int i = 0; i < domainList.size(); i++) {
				msName.add(domainList.get(i).getName());
			}
			for(int j = 0; j < clusterList.size(); j++) {
				Clusters cluster = clusterList.get(j);
				if(cluster.getName().equals("")) {
					return false;
				}
				
				for(int x = 0; x < cluster.getServers().size(); x++) {
					if(cluster.getServers().size() <= 0 ) {
						return false;
					}
					if(!msName.contains(cluster.getServers().get(x))) {
						return false;
					}
						
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean dataSource() {
		List dataSourceClassNameList = new ArrayList<String>(
					Arrays.asList("com.tmax.tibero.jdbc.ext.TbConnectionPoolDataSource", "oracle.jdbc.pool.OracleConnectionPoolDataSource", "org.mariadb.jdbc.MySQLDataSource", "org.mariadb.jdbc.MariaDbDataSource"));
		List dataSourceType = new ArrayList<String>(
				Arrays.asList("ConnectionPoolDataSource", "DataSource"));
		try{
			if(dataSource.size() == 0) {
				return false;
			}
			DataSource source = dataSource.get(0);
			if(!(dataSourceClassNameList.contains(source.getData_source_class_name()) || dataSourceType.contains(source.getData_source_type()))) {
				return false;
			}
			int port = Integer.parseInt(source.getPort_number());
			if(!((0 < port) && (port < 665535))) {
				return false;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}
