package com.han.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigOsParser {
	private static Logger logger = LoggerFactory.getLogger(ConfigOsParser.class);
	
	public String getenv(String key) {
		String env = System.getenv(key);
		logger.info("환경 변수 로딩합니다.");
		logger.info("" + key + " : " + env);
		if(env == null) {
			logger.info("" + key + " 환경 변수 읽을 수 없음!");
			System.exit(1);
		}
		return env;
	}
	
	public String getIp() {
		try {
			InetAddress ip = InetAddress.getLocalHost();
			return ip.getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		
		return null;
	}
}
