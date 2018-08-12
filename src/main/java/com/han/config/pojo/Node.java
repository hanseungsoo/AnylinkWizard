package com.han.config.pojo;


import com.google.gson.annotations.SerializedName;


public class Node{
	@SerializedName("name")
    private String name;
	@SerializedName("host")
	private String host;
	@SerializedName("port")
	private String port;
	@SerializedName("use-ssl")
	private String use_ssl;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getUse_ssl() {
		return use_ssl;
	}
	public void setUse_ssl(String use_ssl) {
		this.use_ssl = use_ssl;
	}
	
}
