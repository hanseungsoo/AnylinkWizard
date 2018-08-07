package com.han.config.generator;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tmaxsoft.xml.ns.jeus.Nodes;
import com.tmaxsoft.xml.ns.jeus.Nodes.Node;
import com.tmaxsoft.xml.ns.jeus.Nodes.Node.Java;
import com.tmaxsoft.xml.ns.jeus.NodesObjectFactory;

public class NodeGenerator {
	private static Logger logger = LoggerFactory.getLogger(NodeGenerator.class);
	private JSONArray nodeArray;
	private NodesObjectFactory nodesObjectFactory;
	private Nodes nodes;
	
	public NodeGenerator(JSONArray nodeArray, Nodes nodes) {
		this.nodeArray = nodeArray;
		this.nodesObjectFactory = new NodesObjectFactory();
		this.nodes = nodes;
	}
	
	public Nodes generator() {
		logger.info("설정 파일을 반영 합니다.");
		
		List<Node> nodeList = nodes.getNode();
		nodeList.clear();

		for(int i=0 ; i<nodeArray.size() ; i++){
			JSONObject nodeObj = (JSONObject) nodeArray.get(i);
			
			Node node = nodesObjectFactory.createNodesNode();
			node.setName((String)nodeObj.get("name"));
			node.setHost((String)nodeObj.get("host"));
			
			Java java = nodesObjectFactory.createNodesNodeJava();
			int port = Integer.parseInt((String)nodeObj.get("port"));
			java.setPort(port);
			java.setUseSsl((String)nodeObj.get("use-ssl"));
			node.setJava(java);
			
			nodeList.add(node);
			
		}
		return nodes;
	}
}
