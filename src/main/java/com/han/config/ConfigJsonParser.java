package com.han.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.han.wizard.AnylinkWizard.AppMain;

public class ConfigJsonParser {
	
	private static Logger logger = LoggerFactory.getLogger(ConfigJsonParser.class);
	private String propertyName;
	
	public ConfigJsonParser() {
		if(AppMain.dev) {
			this.propertyName = "C:\\Users\\Han\\Desktop\\소스\\tmaxProBus\\workspace\\config\\setting.json";
		}else {
			this.propertyName = System.getProperty("setting.config.json");
		}
	}
	public JSONObject getJsonFile() {
		File file = null;
		logger.info("설정파일을 로딩합니다.");
		logger.info("파일 경로 : " + propertyName);
		JSONObject jsonObject = null;
		try {
			file = new File(propertyName);
			JSONParser jsonParser = new JSONParser();
			jsonObject = (JSONObject) jsonParser.parse(new FileReader(file));
		}catch(NullPointerException nullEx){
			nullEx.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	public JSONArray load(String key) {
		logger.info(key + " 파싱을 시작합니다.");
		JSONObject jsonObject = null;
		
		jsonObject = getJsonFile();
		if(jsonObject == null) {
			logger.info(key + " 설정 파일 로딩 실패");
			System.exit(1);
		}
		if(!validChecker(key, jsonObject)) {
			logger.info(key + " 설정 파일이 잘못 구성되어 있습니다.");
			System.exit(1);
		}
		return (JSONArray) jsonObject.get(key);
	}

	public Boolean validChecker(String key,JSONObject jsonObject) {
		if( key.equals("nodes")) {
			JSONArray jsonarray = (JSONArray) jsonObject.get(key);
			for (int i = 0; i < jsonarray.size(); i++) {
				JSONObject item = (JSONObject) jsonarray.get(i);
				if(item.containsKey("name") && item.containsKey("host") && 
					item.containsKey("port") && item.containsKey("use-ssl") ) {
				}else {
					return false;
				}
			}
			return true;
		}else if(key.equals("domain")) {
			JSONArray jsonarray = (JSONArray) jsonObject.get(key);
			JSONArray nodeArray = load("nodes");
			for (int i = 0; i < jsonarray.size(); i++) {
				JSONObject item = (JSONObject) jsonarray.get(i);
				if(item.containsKey("name") && item.containsKey("log-home") && (item.containsKey("listeners") && item.containsKey("jvm-option") && item.containsKey("rotation-dir") && item.containsKey("node-name"))) {
					if(item.get("log-home").equals("")) {
						//item.replace("log-home", "", null);
						item.remove("log-home");
					}
					
					int flag = 0;
					for(int y = 0; y < nodeArray.size(); y++) {
						JSONObject nodeObj = (JSONObject)nodeArray.get(y);
						if(((String)item.get("node-name")).equals((String)nodeObj.get("name"))) {
							flag++;
						}
					}
					if(flag != 1) {
						logger.error("NodeManager is Not Matched");
						return false;
					}
					
					
					JSONArray listeners = (JSONArray) item.get("listeners");
					for(int index = 0; index < listeners.size(); index++) {
						JSONObject item2 = (JSONObject) listeners.get(index);
						if(item2.containsKey("name") && item2.containsKey("port") && item2.containsKey("address")) {
						}else {
							logger.error("Listeners config is Not Valid");
							return false;
						}
					}	
				}else {
					logger.error("Server config is Not Valid");
					return false;
				}
			}
			return true;
		}else if(key.equals("clusters")){
			JSONArray jsonarray = (JSONArray) jsonObject.get(key);
			for (int i = 0; i < jsonarray.size(); i++) {
				JSONObject item1 = (JSONObject) jsonarray.get(i);
				if(item1.containsKey("name") && item1.containsKey("servers")) {
					JSONArray serverNames1 = (JSONArray)item1.get("servers");
					String clusterName1 = (String) item1.get("name");
					
					for(int index1 = 0; index1 < jsonarray.size(); index1++) {
						JSONObject item2 = (JSONObject) jsonarray.get(index1);
						String clusterName2 = (String) item2.get("name");
						JSONArray serverNames2 = (JSONArray)item2.get("servers");
						
						if(!clusterName1.equals(clusterName2)) {
							for(int x = 0; x < serverNames1.size(); x++) {
								String name1 = (String) serverNames1.get(x);
								for(int y = 0; y < serverNames1.size(); y++) {
									String name2 = (String) serverNames2.get(y);
									if(name1.equals(name2)) {
										return false;
									}
								}
							}
						}
					}
					
				}else {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}

