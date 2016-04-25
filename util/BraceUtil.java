package com.bjsasc.twc.brace.util;

import com.bjsasc.platform.spring.PlatformApplicationContext;
import com.bjsasc.twc.brace.service.BraceJService;
import com.bjsasc.twc.brace.service.BraceService;
/**
 * 基础服务工具
 * @author 陈建立
 * 2015-12-18
 */
public class BraceUtil {
	public static BraceService getBraceServiceTemplate(){
		BraceService service = (BraceService)PlatformApplicationContext.getBean("braceservicetemplate");
		return service;
	}
	public static BraceJService getBraceJServiceJdbc(){
		BraceJService service = (BraceJService)PlatformApplicationContext.getBean("braceservicejdbc");
		return service;
	}
	
	public static void main(String[] args) {
		/**
		List<String> list = new ArrayList<String>();
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		getBraceServiceJdbc().deleteObjList(list,"Hisxml");
		*/
		System.out.println("111");
	}
}
