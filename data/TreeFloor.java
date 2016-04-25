package com.bjsasc.twc.brace.data;

import java.io.Serializable;

/**
 * 复合树节点对象
 * 树节点对象与扩展对象
 * @author 陈建立
 * 2015-12-18
 */
public class TreeFloor implements Serializable{
	
	private static final long serialVersionUID = -1028150928160166906L;
	
	private Object obj;//树节点对象
	private Object expObj;//扩展对象
	private String contenttype;//树节点对象（如需求指标有contenttype=0内容在twc_demandtext_tab保存，contenttype=1内容在twc_demandqualitative_tab保存）新类型
	private String contentoldtype;//树节点对象（如需求指标有contenttype=0内容在twc_demandtext_tab保存，contenttype=1内容在twc_demandqualitative_tab保存）修改前的类型
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	public Object getExpObj() {
		return expObj;
	}
	public void setExpObj(Object expObj) {
		this.expObj = expObj;
	}
	public String getContenttype() {
		return contenttype;
	}
	public void setContenttype(String contenttype) {
		this.contenttype = contenttype;
	}
	public String getContentoldtype() {
		return contentoldtype;
	}
	public void setContentoldtype(String contentoldtype) {
		this.contentoldtype = contentoldtype;
	}
	
}
