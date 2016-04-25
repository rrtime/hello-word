package com.bjsasc.twc.brace.data;

import com.bjsasc.platform.objectmodel.managed.modelinfo.data.CommonModel;
/**
 * 需求指标等历史文件转换成xml文件存储
 * xml文件服务器存储对象
 * @author 陈建立
 * 2015-12-18
 */
public class Hisxml extends CommonModel{

	private static final long serialVersionUID = 1626686435323306895L;
	
	private String fileiid;//文件服务器的实体标识
	private String cellName;//存储单元名称
	private long createtime;//	创建日期
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getFileiid() {
		return fileiid;
	}
	public void setFileiid(String fileiid) {
		this.fileiid = fileiid;
	}
	public String getCellName() {
		return cellName;
	}
	public void setCellName(String cellName) {
		this.cellName = cellName;
	}
	public long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}
	
}
