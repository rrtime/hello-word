package com.bjsasc.twc.brace.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BackUp {
	/**
	 * getexp 获取备份数据库命令
	 * @param userName
	 * @param passWord
	 * @param dataBaseName
	 * @param address
	 * @param fileName
	 * @return
	 */
	public StringBuffer getExp(String userName, String passWord,String dataBaseName, String address, String fileName){
		StringBuffer exp = new StringBuffer("exp ");
		exp.append(userName+"/"+passWord+"@"+dataBaseName);
		exp.append(" rows=y indexes=n compress=n buffer=65536 feedback=100000 owner="+userName+" ");
		exp.append(" file="+address+File.separator+fileName+".dmp ");
		exp.append(" log="+address+File.separator+fileName+".log");
		return exp;
	}
	
	/**
	 * 备份oracle数据库
	 * backUpDbOracle
	 * @param userName
	 * @param passWord
	 * @param dataBaseName
	 * @param address
	 * @param fileName
	 * @param ip
	 * @return
	 */
	public String backUpDbOracle(String userName, String passWord,String dataBaseName, String address, String fileName, String ip,String port) {
		String retrunval = "";
		address = UtilStatusDictionary.dbbakPath;
		File file = new File(address);
		if (!file.exists()) {
			file.mkdirs();
		}
		boolean exist = CommonUtilTool.existFile(address+File.separator, fileName + ".dmp");
		if (!exist) {
			try {
				boolean existT = true;
				boolean bl = existCon(userName, passWord, dataBaseName, ip,port);
				if (bl) {
					Process p = Runtime.getRuntime().exec(getExp(userName, passWord, dataBaseName, address, fileName).toString());
					InputStreamReader isr = new InputStreamReader(p.getErrorStream());
					BufferedReader br = new BufferedReader(isr);
					String line = null;  
					while ((line = br.readLine()) != null) {
						if (line.indexOf("错误") != -1) {
							System.out.println("错误"+line);
							//break;
						}
					}
					p.destroy();
					p.waitFor(); 
					existT = CommonUtilTool.existFile(address+File.separator, fileName + ".dmp");
				}
				if (existT && bl) {
					retrunval = "success";
				} else {
					retrunval = "failure";
				}
			} catch (Exception e) {
				System.out.println("backUpDbOracle_error:"+e.getMessage());
				retrunval = "failure";
			}
		} else {
			retrunval = "already";
		}
		return retrunval;
	}
	
	/**
	 * 判断是否存在数据库连接
	 * existcon
	 * @param userName
	 * @param passWord
	 * @param dataBaseName
	 * @param ip
	 * @return
	 */
	public static boolean existCon(String userName, String passWord,String dataBaseName,String ip,String port) {
		boolean bl = true;
		Connection conn = null;
		try {
			if(CommonUtilTool.emptyString(ip) ){
				ip="localhost";
			}
			if(CommonUtilTool.emptyString(ip)){
				port = "1521";
			}
			conn = getCon(userName, passWord, dataBaseName, ip,port);
		} catch (Exception e) {
			bl = false;
			System.out.println("getCon_error"+e.getMessage());
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
				System.out.println("existCon_getCon_close_error"+e.getMessage());
			}
		}
		return bl;
	}
	
	/**
	 * getcon
	 * @param userName
	 * @param passWord
	 * @param dataBaseName
	 * @param ip
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection getCon(String userName, String passWord,String dataBaseName,String ip,String port) {
		Connection conn = null;
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			String url = "jdbc:oracle:thin:@"+ip+":"+port+":" + dataBaseName;
			conn = DriverManager.getConnection(url, userName, passWord);
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return conn;
	}

}