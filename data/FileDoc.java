package com.bjsasc.twc.brace.data;

import com.bjsasc.platform.objectmodel.managed.modelinfo.data.CommonModel;

/**
 * 文档对象
 * @author 陈建立
 * 2015-12-18
 */
public class FileDoc extends CommonModel{
	
	private static final long serialVersionUID = -4641541104303934096L;
	
	private String name;//文件名称
	private String code;//文件编号
	private String phase;//阶段
	private String system;//专业
	private String status;//状态
	private String seclevel;//密级
	private String doctype;//文档类型名称
	private String creator;//创建人
	private String createunit;//创建单位
	private long createtime;//创建日期
	private long controltime;//受控日期
	private String dociid;//文档版本标识
	private String version;//版本
	private String treeid;//存储域标识
	private String doctypeiid;//文档模型id
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPhase() {
		return phase;
	}
	public void setPhase(String phase) {
		this.phase = phase;
	}
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSeclevel() {
		return seclevel;
	}
	public void setSeclevel(String seclevel) {
		this.seclevel = seclevel;
	}
	public String getDoctype() {
		return doctype;
	}
	public void setDoctype(String doctype) {
		this.doctype = doctype;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getCreateunit() {
		return createunit;
	}
	public void setCreateunit(String createunit) {
		this.createunit = createunit;
	}
	public long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}
	public long getControltime() {
		return controltime;
	}
	public void setControltime(long controltime) {
		this.controltime = controltime;
	}
	public String getDociid() {
		return dociid;
	}
	public void setDociid(String dociid) {
		this.dociid = dociid;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getTreeid() {
		return treeid;
	}
	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}
	public String getDoctypeiid() {
		return doctypeiid;
	}
	public void setDoctypeiid(String doctypeiid) {
		this.doctypeiid = doctypeiid;
	}
	
}
