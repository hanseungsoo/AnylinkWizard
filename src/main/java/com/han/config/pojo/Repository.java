package com.han.config.pojo;

import com.google.gson.annotations.SerializedName;

public class Repository {
	@SerializedName("data-source-id")
	private String data_source_id;
	@SerializedName("data-source-class-name")
	private String data_source_class_name;
	@SerializedName("data-source-type")
	private String data_source_type;
	@SerializedName("vendor")
	private String vendor;
	@SerializedName("server-name")
	private String server_name;
	@SerializedName("port-number")
	private String port_number;
	@SerializedName("database-name")
	private String database_name;
	@SerializedName("user")
	private String user;
	@SerializedName("password")
	private String password;

	public String getData_source_id() {
		return data_source_id;
	}
	public void setData_source_id(String data_source_id) {
		this.data_source_id = data_source_id;
	}
	public String getData_source_class_name() {
		return data_source_class_name;
	}
	public void setData_source_class_name(String data_source_class_name) {
		this.data_source_class_name = data_source_class_name;
	}
	public String getData_source_type() {
		return data_source_type;
	}
	public void setData_source_type(String data_source_type) {
		this.data_source_type = data_source_type;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public String getServer_name() {
		return server_name;
	}
	public void setServer_name(String server_name) {
		this.server_name = server_name;
	}
	public String getPort_number() {
		return port_number;
	}
	public void setPort_number(String port_number) {
		this.port_number = port_number;
	}
	public String getDatabase_name() {
		return database_name;
	}
	public void setDatabase_name(String database_name) {
		this.database_name = database_name;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
