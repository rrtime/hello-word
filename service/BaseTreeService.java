package com.bjsasc.twc.brace.service;

import java.util.List;
import java.util.Map;

import com.bjsasc.twc.brace.dao.BaseTreeDao;
import com.bjsasc.twc.brace.data.BaseTree;
import com.bjsasc.twc.brace.data.TreeFloor;
import com.bjsasc.twc.brace.data.TreeObject;
import com.bjsasc.twc.demand.data.DemandPoint;

/**
 * BaseTree 服务层Template方式接口
 * @author 陈建立
 * 2015-12-18
 */
public interface BaseTreeService {
	
	public BaseTreeDao getTemplateDao();
	
	/**
	 * 保存树结构
	 * 树对象保存仅支持继承BaseTree的树
	 * 存储规则：根节点不存在Innerid,则整棵树均插入
	 * 根节点存在Innerid,则树为更新状态，节点存在Innerid且
	 * @param treeObject根节点集合
	 * @return
	 */
	public boolean saveBaseTreeList(TreeObject treeObject);
	
	/**
	 * 根据文件服务器（需求指标保存历史以xml）返回标识，插入记录
	 * @param fileiid 文件标识
	 * @param cellName 存储单元
	 * @return 返回保存对象主键
	 */
	public String saveHisxmlByFileid(String fileiid,String cellName);
	
	/**
	 * 保存一层树节点
	 * @param list
	 * @param parentid
	 * @return
	 */
	public boolean saveBaseTreeList(List<TreeFloor> list,String parentid);
	
	/**
	 * 根据父节点、对象模型查询子节点
	 * @param parentid
	 * @param basetree
	 * @return
	 */
	public List<?> getBaseTreeListByTemplate(String parentid,BaseTree basetree);
	
	
	/**
	 * 根据需求指标文件指向对象（根节点内部标识）查询上传历史xml文件最大版本,并更新需求指标文件指向对象的版本后保存新记录
	 * demandPoint 需求指标文件指向对象
	 * @param type 查询方式类型 0-jdbc，1-template 
	 * @return
	 */
	public Map<String,String> saveMaxRevsionByRootByTemplate(DemandPoint demandPoint);
}
