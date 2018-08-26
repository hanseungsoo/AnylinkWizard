package com.han.config.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileMover {
	private static Logger logger = LoggerFactory.getLogger(FileMover.class);
	
	public void runFileMove(String srcPath, String destPath, String namePattern) {
		boolean flag = true;
		logger.info(namePattern + " 파일 이동 시작");
		
		try {
			File srcDir = new File(srcPath);
			FileFilter srcFilter = new WildcardFileFilter(namePattern);
			File[] srcFiles = srcDir.listFiles(srcFilter);
			
			File destDir = new File(destPath);
			FileFilter destFilter = new WildcardFileFilter(namePattern);
			File[] destFiles = destDir.listFiles(destFilter);
			
			if(	destFiles.length == 0) {
				logger.error("삭제할 파일이 없습니다.");
			}else {
				for(int i = 0; i < destFiles.length; i++) {
					if(destFiles[i].delete()) {
						logger.info("기존 파일 삭제 성공 : " + destFiles[i].getName());
					}else {
						logger.error("기존 파일 삭제 실패 : " + destFiles[i].getName());
						flag = false;
					}
				}
			}
			
			if(	srcFiles.length == 0) {
				logger.error("옮길 파일들을 찾을 수 없거나 파일이 한개 이상입니다.");
				flag = false;
			}else {
				for(int i = 0; i < srcFiles.length; i++) {
					try {
						FileUtils.moveFileToDirectory(srcFiles[i], destDir, true);
						logger.info("파일 이동 성공 : " + srcFiles[i]);
						
						if(namePattern.equals("anylink-admin*.war")) {
							
							File fileToMove = new File(destPath + File.separator + srcFiles[i].getName().split(File.separator)[0]);
							boolean isMoved = fileToMove.renameTo(new File(destPath + File.separator + "anylink-admin.war"));
							if(!isMoved) {
								logger.error("파일 이름 변경 실패 : " + fileToMove.getName());
							}
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						logger.error("파일 이동 실패 : " + srcFiles[i]);
						flag = false;
					}
				}
			}
			
			if(!flag) {
				logger.error(namePattern + " 파일 이동에 실패하였습니다. 확인바랍니다.");
			}
		}catch(Exception e) {
			e.printStackTrace();
			logger.error(namePattern + " 파일 이동에 실패하였습니다. 확인바랍니다.");
			return;
		}
		
	}

}
