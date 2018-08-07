package com.han.config.generator;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jtwig.JtwigModel;
import com.han.config.ConfigOsParser;

public class ProfileGenerator {
	private JSONArray configJson = null;
	
	public ProfileGenerator(JSONArray configJson ) {
		this.configJson = configJson;
	}
	
	public JtwigModel generator() {
		ConfigOsParser configOsParser = new ConfigOsParser();
		String ip = configOsParser.getIp();
		String passWd = System.getProperty("jeus.passwd");
		String domain = System.getProperty("anylink.domain");
		String home = System.getProperty("anylink.home");
		
		List<String> msList = new ArrayList<String>();
		for(int i = 0; i < configJson.size(); i++) {
			JSONObject msJson = (JSONObject) configJson.get(i);
			if(!((String)msJson.get("name")).equals("adminServer")) {
				msList.add((String)msJson.get("name"));
			}
		}
		List<String> portList = new ArrayList<String>();
		for(int i = 0; i < configJson.size(); i++) {
			JSONObject msJson = (JSONObject) configJson.get(i);
			if(!((String)msJson.get("name")).equals("adminServer")) {
				JSONArray listenerJson = (JSONArray)msJson.get("listeners");
				for(int j = 0; j < listenerJson.size(); j++) {
					JSONObject baseJson = (JSONObject) listenerJson.get(j);
					if(((String)baseJson.get("name")).equals("BASE")) {
						portList.add((String)baseJson.get("port"));
					}
				}
			}
		}
		JtwigModel model = JtwigModel.newModel().with("IP", ip)
												.with("PASSWD", passWd)
												.with("DOMAIN", domain)
												.with("msList", msList)
												.with("portList", portList)
												.with("ANYLINK_HOME", home);
		return model;
	}
}
