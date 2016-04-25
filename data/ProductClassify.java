package com.bjsasc.twc.brace.data;

/**
 * 型号分类对象
 * @author 陈建立
 * 2015-12-30
 *
 */
public class ProductClassify extends BaseTree{
	
	private static final long serialVersionUID = -5389140287218304658L;
	
	private String nodetype ;//节点类型
	private String delstatus ;//删除、作废状态
	private String rootinnerid ;//根节点内部标识
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getNodetype() {
		return nodetype;
	}
	public void setNodetype(String nodetype) {
		this.nodetype = nodetype;
	}
	public String getDelstatus() {
		return delstatus;
	}
	public void setDelstatus(String delstatus) {
		this.delstatus = delstatus;
	}
	public String getRootinnerid() {
		return rootinnerid;
	}
	public void setRootinnerid(String rootinnerid) {
		this.rootinnerid = rootinnerid;
	}
	
}

