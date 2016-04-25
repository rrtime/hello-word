package com.bjsasc.twc.brace.loader;

import java.io.File;

import com.bjsasc.platform.spring.loader.PlatformConfigFileLoader;
import com.cascc.avidm.util.SysConfig;

/**
 * 基础树配置加载器
 * @author 陈建立
 * 2015-12-18
 */
public class BaseTreeLoader extends PlatformConfigFileLoader{
	public String[] getConfigFileLocation() {
		String[] files = new String[] { SysConfig.getAvidmHome()
				+ File.separator + "springconfig" + File.separator+"twc"+ File.separator+"brace"+ File.separator
				+ "twc.basetree.xml" };
		return files;
	}
}
