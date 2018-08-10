package com.han.config.pojo;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Domain {
	@SerializedName("name")
    public String name;
	@SerializedName("node-name")
    public String node_name;
	@SerializedName("log-home")
    public String log_home;
	@SerializedName("listeners")
    public List<Listeners> listeners;
	@SerializedName("jvm-option")
    public String jvm_option;
	@SerializedName("rotation-dir")
    public String rotation_dir;
	@SerializedName("use-web-engine")
    public String use_web_engine = "false";
	@SerializedName("use-ejb-engine")
    public String use_ejb_engine = "false";
	@SerializedName("use-jms-engine")
    public String use_jms_engine = "false";
	public String lifeCycle = "false";
	
	public class Listeners{
		@SerializedName("name")
	    public String name;
		@SerializedName("port")
	    public String port;
		@SerializedName("address")
	    public String address;
	}
}
