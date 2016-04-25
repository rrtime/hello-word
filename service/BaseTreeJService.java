package com.bjsasc.twc.brace.service;

import java.util.List;
import java.util.Map;

import com.bjsasc.twc.brace.dao.BaseTreeDao;
import com.bjsasc.twc.brace.data.BaseTree;
import com.bjsasc.twc.demand.data.DemandPoint;

/**
 * BaseTree 服务层JDBC方式接口
 * @author 陈建立
 * 2015-12-18
 */
public interface BaseTreeJService {
	
	public BaseTreeDao getJdbcDao();
	/**
	 *  根据父节点、对象模型、树结构类型查询子节点
	 * @param parentid
	 * @param basetree
	 * @param type 为1时查询有引用节点，按照引用模式查询，为0时按照无引用节点查询
	 * @return
	 */
	public List<Map<String,Object>> getBaseTreeList(String parentid,BaseTree basetree);
	
	/**
	 * 根据主键删除需求指标文件表中的数据
	 * @param innerid
	 */
	public void deleteFileXmlByInnerid(String innerid);
	
	/**
	 * 根据文件id删除需求指标文件表中的数据
	 * @param innerid
	 */
	public void deleteFileXmlByFileiid(String fileiid);
	
	/**
	 * 根据需求指标文件指向对象（根节点内部标识）查询上传历史xml文件最大版本,并更新需求指标文件指向对象的版本后保存新记录
	 * @param demandPoint 需求指标文件指向对象
	 * @param type 查询方式类型 0-jdbc，1-template 
	 * @return
	 */
	public Map<String, String> saveMaxRevsionByRootByJdbc(DemandPoint demandPoint);
	
}
