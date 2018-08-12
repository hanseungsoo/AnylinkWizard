package com.han.config.pojo;

import java.io.File;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Domain {
	@SerializedName("name")
    private String name;
	@SerializedName("node-name")
	private String node_name;
	@SerializedName("log-home")
	private String log_home;
	@SerializedName("listeners")
	private List<Listeners> listeners;
	@SerializedName("jvm-option")
	private String jvm_option;
	@SerializedName("rotation-dir")
	private String rotation_dir;
	@SerializedName("use-web-engine")
	private String use_web_engine = "false";
	@SerializedName("use-ejb-engine")
	private String use_ejb_engine = "false";
	@SerializedName("use-jms-engine")
	private String use_jms_engine = "false";
	private String lifeCycle = "false";
	
	public class Listeners{
		@SerializedName("name")
		private String name;
		@SerializedName("port")
		private String port;
		@SerializedName("address")
		private String address;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getPort() {
			return port;
		}
		public void setPort(String port) {
			this.port = port;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		
		
	}
	
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getNode_name() {
		return node_name;
	}


	public void setNode_name(String node_name) {
		this.node_name = node_name;
	}


	public String getLog_home() {
		return log_home;
	}


	public void setLog_home(String log_home) {
		this.log_home = log_home;
	}


	public List<Listeners> getListeners() {
		return listeners;
	}


	public void setListeners(List<Listeners> listeners) {
		this.listeners = listeners;
	}


	public String getJvm_option() {
		return jvm_option;
	}


	public void setJvm_option(String jvm_option) {
		this.jvm_option = jvm_option;
	}


	public String getRotation_dir() {
		return rotation_dir;
	}


	public void setRotation_dir(String rotation_dir) {
		this.rotation_dir = rotation_dir;
	}


	public String getUse_web_engine() {
		return use_web_engine;
	}


	public void setUse_web_engine(String use_web_engine) {
		this.use_web_engine = use_web_engine;
	}


	public String getUse_ejb_engine() {
		return use_ejb_engine;
	}


	public void setUse_ejb_engine(String use_ejb_engine) {
		this.use_ejb_engine = use_ejb_engine;
	}


	public String getUse_jms_engine() {
		return use_jms_engine;
	}


	public void setUse_jms_engine(String use_jms_engine) {
		this.use_jms_engine = use_jms_engine;
	}


	public String getLifeCycle() {
		return lifeCycle;
	}


	public void setLifeCycle(String lifeCycle) {
		this.lifeCycle = lifeCycle;
	}


	public void changeLogHome(String logHome) {
		setLog_home(getLog_home().replace("log_home", logHome) + File.separator + "jeus");
		setRotation_dir(getRotation_dir().replace("log_home", logHome) + File.separator + "jeus" + File.separator + getName());
	}
}
