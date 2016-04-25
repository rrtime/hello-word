package com.bjsasc.twc.brace.util;

import java.io.File;

/**
 *静态变量类
 * @author 陈建立
 * 2015-12-18
 */
public class UtilStatusDictionary {
	/**
	 * 系统定义常量
	 */
	public final static String dateFormat ="yyyy-MM-dd HH:mm:ss";//转换成时间数据格式
	public final static String shortDateFormat ="yyyy-MM-dd";//转换成时间数据格式
	public final static String encodeType = "GB2312";//文件及数据流编码方式
	public final static String beginRevision = "1";//初始版本
	public final static String avidm_home = System.getProperty("AVIDM_HOME");//AVIDM_HOME路径
	public final static String dbbakPath = avidm_home+File.separator+"DBbak";//数据库备份路径
	
	public final static String GannttBiggest = "20";//任务管理关联流程完成情况默认显示精度
	
	/**
	 * 任务管理状态标识
	 */
	public final static String taskStatusEdit = "0";//任务管理编辑状态值
	public final static String taskStatusExecute = "1";//发布、开始执行
	public final static String taskStatusAdjust = "2";//调整
	public final static String taskStatusStop = "3";//停止
	public final static String taskStatusReady = "4";//任务管理完成状态值
	
	/**
	 * 树删除作废状态
	 */
	public final static String TreeDelStatus = "1";//0作废、1删除
	public final static String TreeInvalidStatus = "0";//0作废、1删除
	
	/**
	 * 树更新、删除、作废判断值
	 */
	public final static String HasUpdateTrue = "true";//BaseTree.hasupdate更新字段值
	public final static String HasUpdateDelete = "delete";//BaseTree.hasupdate删除字段值
	public final static String HasUpdateInvalid = "invalid";//BaseTree.hasupdate作废字段值
	
}
