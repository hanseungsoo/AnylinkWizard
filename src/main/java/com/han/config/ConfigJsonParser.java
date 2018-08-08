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
import com.han.config.pojo.Domain;
import com.han.config.pojo.Node;
import com.han.wizard.AnylinkWizard.AppMain;

public class ConfigJsonParser {
	private static Logger logger = LoggerFactory.getLogger(ConfigJsonParser.class);
	private String propertyName;
	
	public ConfigJsonParser() {
		if(AppMain.dev) {
			this.propertyName = "C:\\Users\\Han\\Desktop\\workspace\\config\\setting.json";
		}else {
			this.propertyName = System.getProperty("setting.config.json");
		}
	}
	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> load(String key) {
		logger.info("설정파일을 로딩합니다.");
		logger.info("파일 경로 : " + propertyName);
		JsonParser jsonParser = new JsonParser();
		JsonObject nodesObject = null;
		try {
			nodesObject = (JsonObject)jsonParser.parse(new FileReader(propertyName));
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		logger.info(key + " 설정 파싱합니다.");
		JsonArray nodesArray = (JsonArray)nodesObject.get(key).getAsJsonArray();
		ArrayList<T> list = new ArrayList<T>();
		Iterator<JsonElement> iterator = nodesArray.iterator();
		while(iterator.hasNext()){
            JsonElement json = (JsonElement)iterator.next();
            T object = null;
            if(key.equals("nodes")) {
            	object = (T) new Gson().fromJson(json, Node.class);
            }else if(key.equals("domain")){
            	object = (T) new Gson().fromJson(json, Domain.class);
            }
            list.add(object);
        }
		return list;
	}
}

