package com.bjsasc.twc.brace.data;

import com.bjsasc.platform.objectmodel.managed.modelinfo.data.CommonModel;

/**
 * 文件实体对象
 * @author 陈建立
 * 2015-12-18
 */
public class File extends CommonModel{
	
	private static final long serialVersionUID = -9118369333667028009L;
	
	private String name;//名称
	private String filesize;//文件大小
	private String fileextname;//文件扩展名
	private String fileiid;//文件服务器的实体标识
	private String cellName;//存储单元名称
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFilesize() {
		return filesize;
	}
	public void setFilesize(String filesize) {
		this.filesize = filesize;
	}
	public String getFileextname() {
		return fileextname;
	}
	public void setFileextname(String fileextname) {
		this.fileextname = fileextname;
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
	
}
