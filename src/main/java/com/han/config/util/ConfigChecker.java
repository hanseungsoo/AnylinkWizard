package com.han.config.util;

import java.util.ArrayList;
import java.util.List;

import com.han.config.pojo.Clusters;
import com.han.config.pojo.Domain;
import com.han.config.pojo.Domain.Listeners;
import com.han.config.pojo.Node;

public class ConfigChecker {
	List<Domain> domainList;
	List<Clusters> clusterList;
	List<Node> nodeList;
	public ConfigChecker(List<Domain> domainList, List<Clusters> clusterList, List<Node> nodeList) {
		this.domainList = domainList;
		this.clusterList = clusterList;
		this.nodeList = nodeList;
	}
	
	public boolean node() {
		for(int i = 0; i < nodeList.size(); i++) {
			Node node = nodeList.get(i);
			int port = Integer.parseInt(node.port);
			if(!((0 < port) && (port > 665535))) {
				return false;
			}
		}
		return true;
	}
	
	public boolean domian() {
		boolean flag = false;
		for(int i = 0; i < domainList.size(); i++) {
			Domain domain = domainList.get(i);
			for(int j = 0; j < nodeList.size(); j ++) {
				Node node = nodeList.get(j);
				if(domain.node_name.equals(node.name)) {
					flag = true;
				}
			}
			
			List<Listeners> listeners = domain.listeners;
			for(int x = 0; x < listeners.size(); x++) {
				Listeners listenersItem = listeners.get(x);
				
				int port = Integer.parseInt(listenersItem.port);
				if(!((0 < port) && (port > 665535))) {
					flag = false;
				}
			}
			
		}
		return flag;
	}
	
	public boolean cluster() {
		List<String> msName = new ArrayList<String>();
		for(int i = 0; i < domainList.size(); i++) {
			msName.add(domainList.get(i).name);
		}
		for(int j = 0; j < clusterList.size(); j++) {
			Clusters clusters = clusterList.get(j);
			for(int x = 0; x < clusters.servers.size(); x++) {
				if(!msName.contains(clusters.servers.get(x))) {
					return false;
				}
					
			}
		}
		return true;
	}
}
