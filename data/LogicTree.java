package com.bjsasc.twc.brace.data;

/**
 * 逻辑树对象
 * @author 陈建立
 * 2015-12-18
 */
public class LogicTree extends BaseTree{
	
	private static final long serialVersionUID = 8981963965755419113L;
	
	private String nodetype;//节点类型
	private String revision;//版本
	private String id;
	private String hrevsion;//引用节点版本
	private String hinnerid;//引用节点主键
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getNodetype() {
		return nodetype;
	}
	public void setNodetype(String nodetype) {
		this.nodetype = nodetype;
	}
	public String getRevision() {
		return revision;
	}
	public void setRevision(String revision) {
		this.revision = revision;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHrevsion() {
		return hrevsion;
	}
	public void setHrevsion(String hrevsion) {
		this.hrevsion = hrevsion;
	}
	public String getHinnerid() {
		return hinnerid;
	}
	public void setHinnerid(String hinnerid) {
		this.hinnerid = hinnerid;
	}
	
}
