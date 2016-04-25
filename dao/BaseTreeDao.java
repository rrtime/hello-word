package com.bjsasc.twc.brace.dao;

import java.util.List;
import java.util.Map;

import com.bjsasc.twc.brace.data.BaseTree;
import com.bjsasc.twc.brace.data.TreeObject;
import com.bjsasc.twc.demand.data.DemandPoint;

/**
 * BaseTreeDao 接口
 * @author 陈建立
 * 2015-12-18
 */
public interface BaseTreeDao extends BraceDao{
	
	/**
	 * 根据父节点、对象模型查询子节点
	 * @param parentid
	 * @param basetree
	 * @return
	 */
	public List<Map<String,Object>> getBaseTreeList(String parentid,BaseTree basetree);
	
	/**
	 * 根据父节点、对象模型查询子节点
	 * @param parentid
	 * @param basetree
	 * @return
	 */
	public List<?> getBaseTreeListByTemplate(String parentid,BaseTree basetree);
	
	
	/**
	 * 根据父节点、有版本无引用的对象模型查询子节点
	 * @param parentid
	 * @param basetree
	 * @return
	 */
	public List<Map<String,Object>> getBaseTreeHasRevList(String parentid,BaseTree basetree);
	
	/**
	 * 根据父节点查询有引用节点树的子节点,
	 * @param parentid
	 * @param basetree
	 * @return
	 */
	public List<Map<String,Object>> getlogictreeList(String parentid,BaseTree basetree);
	
	
	/**
	 * 根据主键、对象模型查询对象
	 * @param innerid
	 * @param basetree
	 * @return
	 */
	public Map<String,Object> getBaseTreeMap(String innerid,BaseTree basetree);
	
	/**
	 * 保存所有树节点子集
	 * @param tempnextlist
	 * @param lastobj
	 * @return
	 */
	public boolean saveBaseTreeNextList(List<TreeObject> nextList,Object lastobj);
	
	/**
	 * 更新树节点子集
	 * @param nextList
	 * @param lastobj
	 * @return
	 */
	public boolean updateBaseTreeNextList(List<TreeObject> nextList,Object lastobj);
	
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
	public Map<String,String> saveMaxRevsionByRoot(DemandPoint demandPoint,String type);
	
}
