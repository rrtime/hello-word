package com.bjsasc.twc.brace.data;

import java.io.Serializable;
import java.util.List;
/**
 * 一个树节点对象及其子集
 * @author 陈建立
 * 2015-12-18
 */
public class TreeObject implements Serializable{
	
	private static final long serialVersionUID = -6472492103501555471L;
	
	private Object obj;//树节点对象
	private Object expObj;//扩展子对象
	private String contenttype;//树节点对象（如需求指标有contenttype=0内容在twc_demandtext_tab保存，contenttype=1内容在twc_demandqualitative_tab保存）新类型
	private String contentoldtype;//树节点对象（如需求指标有contenttype=0内容在twc_demandtext_tab保存，contenttype=1内容在twc_demandqualitative_tab保存）修改前的类型
//	private String fkinnerid;//树节点主对象与扩展对象管理外键，需与扩展子对象外键相同，不需赋值
	private List<TreeObject> nextList;//子集

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
	public List<TreeObject> getNextList() {
		return nextList;
	}
	public void setNextList(List<TreeObject> nextList) {
		this.nextList = nextList;
	}
	
	
}
