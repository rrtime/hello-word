package com.bjsasc.twc.brace.util;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.bjsasc.platform.webframework.bean.FilterParam;
import com.cascc.avidm.auditadm.bean.AULogInfo;
import com.cascc.avidm.auditadm.bean.AULogMessage;
import com.cascc.avidm.auditadm.bean.AULogger;
import com.cascc.avidm.login.model.PersonModel;
import com.cascc.avidm.util.SysConfig;
import com.bjsasc.platform.filecomponent.util.*;
import com.bjsasc.platform.filecomponent.service.*;
import com.bjsasc.platform.filecomponent.model.*;
public class CommonUtil {

	/**
	 * 获取web目录绝对路径
	 * 
	 * @return
	 */
	public static String getWebAbsolutePath() {
		String classesPath = CommonUtil.class.getClassLoader().getResource("/")
				.getPath();
		if (classesPath.startsWith("file:/")) {
			classesPath = classesPath.substring(6);
		}
		if (classesPath.startsWith("/")) {
			classesPath = classesPath.substring(1);
		}
		return classesPath.substring(0, classesPath.indexOf("WEB-INF"));
	}

	/**
	 * 从传入的参数Map中取得指定key的值，如果没有取到返回""。
	 * 
	 * @param parameterMap
	 *            数据Map
	 * @param key
	 *            键
	 * @return 值
	 */
	@SuppressWarnings("rawtypes")
	public static String getParameter(Map parameterMap, String key) {
		return getParameter(parameterMap, key, "");
	}

	/**
	 * 从传入的参数Map中取得指定key的值，如果没有取到返回指定的defaultValue。
	 * 
	 * @param parameterMap
	 *            数据Map
	 * @param key
	 *            键
	 * @param defaultValue
	 *            默认值
	 * @return 值
	 */
	@SuppressWarnings("rawtypes")
	public static String getParameter(Map parameterMap, String key,
			String defaultValue) {
		return parameterMap.get(key) == null ? defaultValue : parameterMap.get(
				key).toString();
	}

	/**
	 * 获取ocx控件的数据库连接
	 * 
	 * @return String
	 * @throws Exception
	 */
	public static String getOcxConnUrl() throws Exception {
		Properties prop = new Properties();
		InputStream in = new BufferedInputStream(new FileInputStream(
				SysConfig.getAvidmHome() + File.separator
						+ "DBConfig.properties"));
		prop.load(in);
		String url = (String) prop.get("url");
		in.close();
		return url;
	}

	public static PtFileStorageObj getPtFileStorageObj() throws Exception {

		// 获取文件柜为‘default’的文件服务器的iP和端口
		PtStorageService ptStorageService = PtFileCptServiceProvider
				.getStorageService();
		FilterParam F = new FilterParam();
		F.addItem("ID", "default");
		List<PtFileStorageObj> stlist = ptStorageService.getStorage(F);
		return stlist.get(0);
	}
	
	/**
	 * @author Alex.ma 2015-3-20下午02:22:41
	 * @param iLevel  为日志级别(参考文档-ADP V1.5.1-日志记录开发指南)
	 * @param sLogType 为日志类型
	 * @param iObjectSecurity 操作对象的密级  (10:普通;20:内部;30:秘密;40:机密)
	 * @param sOperation 操作对象的动作，如增加、修改、删除等动作可自行定义
	 * @param sObjectType 操作对象的类型，可以规定为模块的名称，如用户管理。可自行定义
	 * @param sModuleSource 系统管理
	 * @param sMsg 事件描述
	 * @param user session对象
	 * @return
	 * @throws Exception
	 */
	public static boolean insertLog(int iLevel, String sLogType,
			int iObjectSecurity, String sOperation, String sObjectType,
			String sModuleSource, String sMsg, PersonModel user)
			throws Exception {
		boolean boo = false;
		AULogInfo cauloginfo = new AULogInfo();
		cauloginfo.iLevel = iLevel;
		cauloginfo.sLogType = sLogType;
		cauloginfo.iObjectSecurity = iObjectSecurity;
		cauloginfo.sComputerName = user.getHostName();
		cauloginfo.sIPAddress = user.getIP();
		cauloginfo.sNetCardId = user.getMac();
		cauloginfo.sLoginUserName = user.getUserName();
		cauloginfo.sLoginUserID = user.getLoginName();
		cauloginfo.userType = user.getUsertype();
		cauloginfo.domainName = user.getDomainName();
		cauloginfo.domainRef = user.getDomainRef();
		cauloginfo.sObjectId = user.getUserId();
		cauloginfo.sObjectName = user.getUserName();
		cauloginfo.sOperation = AULogMessage.getLogMessage(sOperation);
		cauloginfo.sObjectType = sObjectType;
		cauloginfo.sModuleSource = AULogMessage.getLogMessage(sModuleSource);
		cauloginfo.sMsg = sMsg;
		boo = AULogger.addLog(cauloginfo);
		return boo;
	}
   public static  void getList (List list,List lists){
	   int k=0;
	   List zlist = new ArrayList();
	   int m = list.size();
	   for(int i=0;i<m;i++){
		  if(list.size()==1){
			  zlist.add(list.get(0));
		      list.remove(0);
		  }else{
			  zlist.add(list.get(i));
		      list.remove(i);
		  }
	      
	      k++;
	      if(k==2){
	    	  break;
	      }
	     }
	   if(zlist!=null){
		   lists.add(zlist);
	   }
	   if(list.size()>0){
	      getList (list,lists);
	   }
   }
}
