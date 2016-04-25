package com.bjsasc.twc.brace.data;

import java.io.Serializable;
import java.util.Map;
/**
 * 配置文件对象
 * 仅解析2级对象
 * @author 陈建立
 * 2015-12-18
 */
public class XMLData implements Serializable{
	
	private static final long serialVersionUID = -362260354494696031L;
	
	private String nodeName;//节点名称
	private String nodeValue;//节点值
	private Map<String,String> maplist;//节点下的集合
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getNodeValue() {
		return nodeValue;
	}
	public void setNodeValue(String nodeValue) {
		this.nodeValue = nodeValue;
	}
	public Map<String, String> getMaplist() {
		return maplist;
	}
	public void setMaplist(Map<String, String> maplist) {
		this.maplist = maplist;
	}
	
}
