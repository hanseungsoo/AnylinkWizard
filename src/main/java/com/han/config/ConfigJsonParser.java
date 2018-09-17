package com.han.config;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.han.config.pojo.Clusters;
import com.han.config.pojo.Repository;
import com.han.config.pojo.Domain;
import com.han.config.pojo.Node;
import com.han.wizard.AnylinkWizard.AppMain;

public class ConfigJsonParser {
	private static Logger logger = LoggerFactory.getLogger(ConfigJsonParser.class);
	private String propertyName;
	
	public ConfigJsonParser(String propertyName) {
		this.propertyName = propertyName;
	}
	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> load(String key) {
		logger.info("설정파일을 로딩합니다.");
		logger.info("파일 경로 : " + propertyName);
		JsonParser jsonParser = new JsonParser();
		JsonObject configObject = null;
		try {
			configObject = (JsonObject)jsonParser.parse(new FileReader(propertyName));
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
			// TODO Auto-generated catch block
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		logger.info(key + " 설정 파싱합니다.");
		ArrayList<T> list = new ArrayList<T>();
		if(!key.equals("log_home")) {
			JsonArray configArray = (JsonArray)configObject.get(key).getAsJsonArray();
			Iterator<JsonElement> iterator = configArray.iterator();
			while(iterator.hasNext()){
	            JsonElement json = (JsonElement)iterator.next();
	            
	            T object = null;
	            if(key.equals("nodes")) {
	            	object = (T) new Gson().fromJson(json, Node.class);
	            }else if(key.equals("domain")){
	            	object = (T) new Gson().fromJson(json, Domain.class);
	            	Domain tmpDomain = (Domain) object;
	            	String logHome = (String)configObject.get("log_home").getAsString();
	            	tmpDomain.changeLogHome(logHome);
	            }else if(key.equals("clusters")) {
	            	object = (T) new Gson().fromJson(json, Clusters.class);
	            }else if(key.equals("Repository")) {
	            	object = (T) new Gson().fromJson(json, Repository.class);
	            	Repository tmpRepo = (Repository)object;
	            	if(tmpRepo.getVendor().toLowerCase().equals("tibero")) {
	            		tmpRepo.setData_source_class_name("com.tmax.tibero.jdbc.ext.TbConnectionPoolDataSource");
	            		tmpRepo.setData_source_id("LogAdapter_DataSource");
	            		tmpRepo.setData_source_type("ConnectionPoolDataSource");
	            	}else if(tmpRepo.getVendor().toLowerCase().equals("oracle")) {
	            		tmpRepo.setData_source_class_name("oracle.jdbc.pool.OracleConnectionPoolDataSource");
	            		tmpRepo.setData_source_id("LogAdapter_DataSource");
	            		tmpRepo.setData_source_type("ConnectionPoolDataSource");
	            	}else if(tmpRepo.getVendor().toLowerCase().equals("others")) {
	            		tmpRepo.setData_source_class_name("org.mariadb.jdbc.MariaDbDataSource");
	            		tmpRepo.setData_source_id("LogAdapter_DataSource");
	            		tmpRepo.setData_source_type("ConnectionPoolDataSource");
	            	}else {
	            		logger.error("해당 DataBase는 지원하지 않습니다.");
	            		System.exit(1);
	            	}
	            }
	            list.add(object);
	        }
		}else {
			String logHome = (String)configObject.get("log_home").getAsString();
			list.add((T)logHome);
		}
		
		return list;
	}
}

