package com.han.config.generator;

import java.io.File;
import java.io.FileFilter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.han.config.pojo.Repository;

public class SchemaGenerator {
	private static Logger logger = LoggerFactory.getLogger(SchemaGenerator.class);
	
	private Repository dataSource;
	public SchemaGenerator(Repository dataSource) {
		this.dataSource = dataSource;
	}
	
	public void runGenerator() {
		File dir = new File(".");
		FileFilter fileFilter = new WildcardFileFilter("create-anylink-ERD*.sql");
		File[] files = dir.listFiles(fileFilter);
		
		
	}
	
	public Connection getConnection() {
		Connection conn = null;
		
		String URL = null;
		
		if(dataSource.getVendor().equals("oracle")) {
			URL = "jdbc:oracle:thin:@" + dataSource.getServer_name() + ":" + dataSource.getPort_number() + ":" + dataSource.getDatabase_name();
		}else if(dataSource.getVendor().equals("tibero")) {
			URL = "jdbc:tibero:thin:@" + dataSource.getServer_name() + ":" + dataSource.getPort_number() + ":" + dataSource.getDatabase_name();
		}
		
		try {
			Class.forName(dataSource.getData_source_class_name());
			conn = DriverManager.getConnection(URL, dataSource.getUser(), dataSource.getPassword());
			
			logger.info("데이터베이스에 연결되었습니다.");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		
		return conn;
	}
}
