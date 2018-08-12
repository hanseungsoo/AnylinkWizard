package com.han.config.pojo;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Clusters {
	@SerializedName("name")
	private String name;
	@SerializedName("servers")
	private List<String> servers;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getServers() {
		return servers;
	}
	public void setServers(List<String> servers) {
		this.servers = servers;
	}
	
	
}
