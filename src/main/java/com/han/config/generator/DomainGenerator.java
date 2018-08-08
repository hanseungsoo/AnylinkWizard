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

	public DomainGenerator(JSONArray domainArray, Domain domains) {
		this.domainArray = domainArray;
		this.templateServerRead = new TemplateServerRead<Server>();
		this.domains = domains;
	}
	
	public void generator(List<Node> list) {
		

	}
	
}
