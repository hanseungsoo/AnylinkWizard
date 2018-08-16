package com.han.config.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
		logger.info("RepositoryDB에 테이블을 생성 시작");
		File dir = new File(".");
		Connection conn = null;
		Statement st = null;
		FileFilter fileFilter = new WildcardFileFilter("create-anylink-ERD*.sql");
		File[] files = dir.listFiles(fileFilter);
		int max = 0, success = 0, fail = 0;
		logger.info("파일 경로 : " + files[0].getAbsolutePath());
		String sqlString[] = getQuery(files[0]);
		
		conn = getConnection();
		try {
			st = conn.createStatement();
			
			for(int i = 0; i < sqlString.length; i++)
            {
                // we ensure that there is no spaces before or after the request string
                // in order to not execute empty statements
                if(!sqlString[i].trim().equals(""))
                {
                	max++;
                	try {
                		st.executeUpdate(sqlString[i]);
                		logger.info("run success : " + sqlString[i]);
                		success++;
                	}catch(SQLException e) {
                		logger.error("run fail : " + sqlString[i]);
                		fail++;
                	}                    
                }
            }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		} finally {
			if(st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		logger.info("RepositoryDB에 테이블을 생성 완료");
		logger.info("총 %d건 성공 %d건 실패 %d건", max, success, fail);
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
	
	public String[] getQuery(File sqlFile) {
		FileReader fr;
		BufferedReader br;
		String s            = new String();
        StringBuffer sb = new StringBuffer();
		try {
			fr = new FileReader(sqlFile);
			br = new BufferedReader(fr);
			
			while((s = br.readLine()) != null){
	            sb.append(s);
	        }
	        br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
         String[] inst = sb.toString().split(";");
         
         return inst;
	}
}
