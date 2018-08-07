package com.han.config.generator;

import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.han.xml.util.TemplateServerRead;
import com.tmaxsoft.xml.ns.jeus.Domain;
import com.tmaxsoft.xml.ns.jeus.Domain.Servers.Server;
import com.tmaxsoft.xml.ns.jeus.Domain.Servers.Server.Listeners.Listener;

public class DomainGenerator {
	private static Logger logger = LoggerFactory.getLogger(DomainGenerator.class);
	private JSONArray domainArray;
	private Domain domains;
	private TemplateServerRead<Server> templateServerRead;
	public DomainGenerator(JSONArray domainArray, Domain domains) {
		this.domainArray = domainArray;
		this.templateServerRead = new TemplateServerRead<Server>();
		this.domains = domains;
	}
	
	public Domain generator() {
		logger.info("설정 파일을 반영 합니다.");
		List<Server> serverList = domains.getServers().getServer();
		serverList.clear();
		
		for(int i=0 ; i<domainArray.size() ; i++){
			JSONObject domainObj = (JSONObject) domainArray.get(i);
			Server server = (Server)templateServerRead.XmlToObject("server");
			if(((String)domainObj.get("name")).equals("adminServer")) {
				server = (Server)templateServerRead.XmlToObject("adminServer");
			}
			
			server.setName((String)domainObj.get("name"));
			server.setNodeName((String)domainObj.get("node-name"));
			server.setLogHome((String)domainObj.get("log-home"));
			Iterator<Listener> iter = server.getListeners().getListener().iterator();
			
			while(iter.hasNext()) {
				Listener listener = iter.next();
				JSONArray listenerArray = (JSONArray) domainObj.get("listeners");
				for(int index = 0; index < listenerArray.size(); index++) {
					JSONObject listenerObj = (JSONObject) listenerArray.get(index);

					if(listener.getName().equals((String)listenerObj.get("name"))) {
						Short port = Short.parseShort((String)listenerObj.get("port"));
						listener.setListenPort(port);
						listener.setListenAddress((String)listenerObj.get("address"));
					}
				}
			}
			server.getJvmConfig().setJvmOption((String)domainObj.get("jvm-option"));
			server.getSystemLogging().getHandler().getFileHandler().setRotationDir((String)domainObj.get("rotation-dir"));
			serverList.add(server);
		}
		
		return domains;
	}
	
}
