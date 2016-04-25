package com.bjsasc.twc.brace.data;

import com.bjsasc.platform.objectmodel.managed.modelinfo.data.CommonModel;

/**
 * 功能权限对象
 * @author 陈建立
 * 2016-01-15
 *
 */
public class Funright extends CommonModel{

	private static final long serialVersionUID = -7453343928128528439L;
	
	private String id ;//标识
	private String name ;//名称
	private String type ;//分类
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}

