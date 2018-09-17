package com.han.config.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xeustechnologies.jcl.JarClassLoader;

import com.han.config.pojo.Repository;

public class SchemaGenerator {
	private static Logger logger = LoggerFactory.getLogger(SchemaGenerator.class);
	
	public void runGenerator(Repository repository) {
		logger.info("RepositoryDB에 테이블을 생성 시작");
		File dir = new File("datasource");
		Connection conn = null;
		Statement st = null;
		FileFilter fileFilter = new WildcardFileFilter("create-anylink-*.sql");
		File[] files = dir.listFiles(fileFilter);
		int max = 0, success = 0, fail = 0;
		logger.info("파일 경로 : " + files[0].getAbsolutePath());
		String sqlString[] = getQuery(files[0]);
		List<String> failList = new ArrayList<String>();
		
		
		conn = getConnection(repository);
		try {
			st = conn.createStatement();
			st.executeUpdate("USE " + repository.getDatabase_name());
			for(int i = 0; i < sqlString.length; i++)
            {
                // we ensure that there is no spaces before or after the request string
                // in order to not execute empty statements
                if(!sqlString[i].trim().equals(""))
                {
                	max++;
                	
                	try {
                		st.executeUpdate(sqlString[i].trim());
                		success++;
                	}catch(SQLException e) {
                		failList.add(sqlString[i].trim());
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
		logger.info("총 " + max + "건, 성공 " + success + "건, 실패 " + fail + "건");
		for(String failSql : failList) {
			logger.error("run fail : " + failSql);
		}
	}
	
	public Connection getConnection(Repository repository) {
		Connection conn = null;
		
		String URL = null;
		
		if(repository.getVendor().equals("oracle")) {
			URL = "jdbc:oracle:thin:@" + repository.getServer_name() + ":" + repository.getPort_number() + ":" + repository.getDatabase_name();
		}else if(repository.getVendor().equals("tibero")) {
			URL = "jdbc:tibero:thin:@" + repository.getServer_name() + ":" + repository.getPort_number() + ":" + repository.getDatabase_name();
		}else if(repository.getVendor().equals("others")){
			URL = "jdbc:mariadb://" + repository.getServer_name() + ":" + repository.getPort_number() + ":" + repository.getDatabase_name();	
		}
		logger.info("연결 정보 : " + URL);
		try {
			Class.forName(repository.getData_source_class_name());
			conn = DriverManager.getConnection(URL, repository.getUser(), repository.getPassword());
			
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
