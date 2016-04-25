package com.bjsasc.twc.brace.data;

import com.bjsasc.platform.objectmodel.managed.modelinfo.data.CommonModel;

/**
 * 基础树对象
 * @author 陈建立
 * 2015-12-18
 */
public class BaseTree extends CommonModel {
	
	private static final long serialVersionUID = 6098475520961460168L;
	
	private String name;//节点名称
	private String creator;//创建人
	private String parentid;//父节点
	private int sequencenumber;//排序号
	private long createtime;//创建日期
	private String delstatus ;//删除、作废状态
	private String rootinnerid;//根节点内部标识
	private String hasupdate;//是否更新，不作为存储字段，仅作为更新判断用，且不写入xml
	
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	
	public int getSequencenumber() {
		return sequencenumber;
	}
	public void setSequencenumber(int sequencenumber) {
		this.sequencenumber = sequencenumber;
	}
	public long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(long createtime) {
		this.createtime = createtime;
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
	public String getHasupdate() {
		return hasupdate;
	}
	public void setHasupdate(String hasupdate) {
		this.hasupdate = hasupdate;
	}
	
}
