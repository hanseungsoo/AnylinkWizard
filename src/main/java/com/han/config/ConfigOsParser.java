package com.han.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigOsParser {
	private static Logger logger = LoggerFactory.getLogger(ConfigOsParser.class);
	
	public String getEnv(String key) {
		String env = System.getenv(key);
		logger.info("환경 변수 로딩합니다.");
		
		if(env == null) {
			logger.error("" + key + " 환경 변수 읽을 수 없음!");
			System.exit(1);
		}else {
			logger.info("" + key + " : " + env);
		}
		return env;
	}
	
	public String getProp(String key) {
		String env = System.getProperty(key);
		logger.info("환경 변수 로딩합니다.");
		
		if(env == null) {
			logger.error("" + key + " 환경 변수 읽을 수 없음!");
			System.exit(1);
		}else {
			logger.info("" + key + " : " + env);
		}
		return env;
	}
	
	public String getIp() {
		try {
			InetAddress ip = InetAddress.getLocalHost();
			logger.info("IP 정보를 로딩합니다.");
			return ip.getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			logger.error("아이피를 찾을 수 없습니다.", e);
			System.exit(1);
		}
		return null;
	}
	
	public String getHostName() {
		try {
			InetAddress ip = InetAddress.getLocalHost();
			logger.info("호스트네임 정보를 로딩합니다.");
			return ip.getHostName();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			logger.error("호스트네임을 찾을 수 없습니다.", e);
			System.exit(1);
		}
		return null;
	}
}
