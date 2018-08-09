package com.han.config.pojo;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Clusters {
	@SerializedName("name")
    public String name;
	@SerializedName("servers")
    public List<String> servers;
}
