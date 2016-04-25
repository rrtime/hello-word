package com.bjsasc.twc.brace.util;

import com.cascc.avidm.auditadm.bean.AULogInfo;
import com.cascc.avidm.auditadm.bean.AULogMessage;
import com.cascc.avidm.auditadm.bean.AULogger;
import com.cascc.avidm.sysadm.web.util.LogUtility;
import com.cascc.avidm.util.DebugLog;

import java.util.*;

public class LogAddUtil {

	public void addLog(long dateTime,int level,String logType,int objectSecurity,
						String computerName,String IPAddress,String netCardId,
						String loginUserName,String loginUserID,String moduleSource,
						String objectProduct,String innerId,String objectName,String seclevel,
						String objectType,String operation,String logMessage
						){
		//添加日志־
		AULogInfo cauloginfo = new AULogInfo();
		cauloginfo.lDatetime = dateTime;//
		cauloginfo.iLevel = level;
		cauloginfo.sLogType = logType;
		cauloginfo.iObjectSecurity = objectSecurity;
		cauloginfo.sComputerName = computerName;
		cauloginfo.sIPAddress = IPAddress;
		cauloginfo.sNetCardId = netCardId;
		cauloginfo.sLoginUserName = loginUserName;
		cauloginfo.sLoginUserID = loginUserID;
		cauloginfo.sModuleSource = AULogMessage
				.getLogMessage(moduleSource);
		cauloginfo.sObjectProduct = objectProduct;
		cauloginfo.sObjectId = innerId;
		cauloginfo.sObjectName = objectName;	
		String miji=seclevel;
		if(miji!=null&&!"".equals(miji)){			
		cauloginfo.iObjectSecurity=Integer.parseInt(miji);
		}
		cauloginfo.sObjectType = objectType;//
		cauloginfo.sOperation = AULogMessage
				.getLogMessage(operation);
		cauloginfo.sMsg = AULogMessage.getLogMessage(logMessage, new Vector());
		if (!AULogger.addLog(cauloginfo)) {
			DebugLog.debug(LogUtility.class, moduleSource+"：日志添加失败："+objectType+"失败");
		}
	}
}
