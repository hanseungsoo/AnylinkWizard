package com.han.config.pojo;

public class CustomInfo {
	private String anylinkHome;
	private String userHome;
	private String ip;
	private String doaminName;
	private String passWord;
	private String hostName;
	private String logHome;
	
	public String getLogHome() {
		return logHome;
	}
	public void setLogHome(String logHome) {
		this.logHome = logHome;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public String getAnylinkHome() {
		return anylinkHome;
	}
	public void setAnylinkHome(String anylinkHome) {
		this.anylinkHome = anylinkHome;
	}
	public String getUserHome() {
		return userHome;
	}
	public void setUserHome(String userHome) {
		this.userHome = userHome;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getDoaminName() {
		return doaminName;
	}
	public void setDoaminName(String doaminName) {
		this.doaminName = doaminName;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\n")	
		  .append("애니링크 홈 경로 : " + getAnylinkHome() +"\n")
		  .append("유저 홈 경로 : " + getUserHome() + "\n")
		  .append("서버 IP : " + getIp() + "\n")
		  .append("도메인 이름 : " + getDoaminName() + "\n")
		  .append("제우스 패스워드 : " + getPassWord() + "\n")
		  .append("호스트 네임 : " + getHostName());
		
		return sb.toString();
	}
}
