package com.han.config.generator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tmaxsoft.schemas.anylink.BizSystemInfo;


public class BizconfigGenerator {
	private static Logger logger = LoggerFactory.getLogger(BizconfigGenerator.class);
	private JSONArray domainArray = null;
	private String home;
	public BizconfigGenerator(JSONArray domainArray, String home) {
		this.domainArray = domainArray;
		this.home = home;
	}
	
	public List<String> generator() {
		logger.info("BizSystem.config 파일을 반영 합니다.");
		List<String> fileList = new ArrayList<String>();
		
		for(int i = 0; i < domainArray.size(); i++) {
			JSONObject msJson = (JSONObject) domainArray.get(i);
			
			if(!((String)msJson.get("name")).equals("adminServer")) {
				String file = home + File.separator + "domains" + File.separator + System.getProperty("anylink.domain") + File.separator + "servers" + File.separator + (String)msJson.get("name") + File.separator + "repository" + File.separator + "bizsystem";
				fileList.add(file);
			}
		}
		
		return fileList;
	}
}
