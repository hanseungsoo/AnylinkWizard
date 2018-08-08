package com.han.config.pojo;


import com.google.gson.annotations.SerializedName;


public class Node{
	@SerializedName("name")
    public String name;
	@SerializedName("host")
    public String host;
	@SerializedName("port")
    public String port;
	@SerializedName("use-ssl")
    public String use_ssl;
	
}
