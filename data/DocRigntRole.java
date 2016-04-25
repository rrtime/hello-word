package com.bjsasc.twc.brace.data;

import com.bjsasc.platform.objectmodel.managed.modelinfo.data.CommonModel;

/**
 * 文档授权存储对象
 * @author 陈建立
 * 2016-01-15
 *
 */
public class DocRigntRole extends CommonModel{
	
	private static final long serialVersionUID = -2224858077680698144L;
	
	private String funrightid ;//功能权限标识
	private String docid ;//文档标识
	private String roleid ;//角色标识
	private String creator ;//授权人
	private long createtime ;//创建日期
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getFunrightid() {
		return funrightid;
	}
	public void setFunrightid(String funrightid) {
		this.funrightid = funrightid;
	}
	public String getDocid() {
		return docid;
	}
	public void setDocid(String docid) {
		this.docid = docid;
	}
	public String getRoleid() {
		return roleid;
	}
	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}
	
}

