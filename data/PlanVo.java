package com.bjsasc.twc.brace.data;

import java.io.Serializable;

/**
 * 排产任务报表显示封装
 * @author 陈建立
 * 2015-12-18
 */
public class PlanVo implements Serializable{
	
	private static final long serialVersionUID = -468079225039997700L;

	/**
	 * 任务数
	 */
	private int size;
	
	/**
	 * 报表的xml字符串
	 */
	private String xmlStr;
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getXmlStr() {
		return xmlStr;
	}

	public void setXmlStr(String xmlStr) {
		this.xmlStr = xmlStr;
	}
	
	
}
