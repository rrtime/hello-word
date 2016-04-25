package com.bjsasc.twc.brace.data;


import com.bjsasc.platform.objectmodel.managed.modelinfo.data.CommonModel;

/**
 * 型号与其它业务关系对象
 * @author 陈建立
 * 2015-12-31
 *
 */
public class ClassifyToAny extends CommonModel {

	private static final long serialVersionUID = 161377876572911554L;
	
	private String fkinnerid1 ;//外键1
	private String fkinnerid2 ;//外键2
	private String tablename ;//其它表名
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getFkinnerid1() {
		return fkinnerid1;
	}
	public void setFkinnerid1(String fkinnerid1) {
		this.fkinnerid1 = fkinnerid1;
	}
	public String getFkinnerid2() {
		return fkinnerid2;
	}
	public void setFkinnerid2(String fkinnerid2) {
		this.fkinnerid2 = fkinnerid2;
	}
	public String getTablename() {
		return tablename;
	}
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	
	
}

