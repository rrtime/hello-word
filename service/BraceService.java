package com.bjsasc.twc.brace.service;

import java.util.List;
import java.util.Map;

import com.bjsasc.platform.objectmodel.business.persist.PTFactor;
import com.bjsasc.platform.webframework.bean.FilterParam;
import com.bjsasc.twc.brace.dao.BraceDao;

/**
 * 基础服务层Template方式接口
 * @author 陈建立
 * 2015-12-18
 */
public interface BraceService {
	
	public BraceDao getTemplateDao() ;
	
	/**
	 * 保存对象到数据库
	 */
	public void saveObj(Object object);
	
	/**
	 * 更新对象
	 * @param obj
	 */
	public void updateObj(Object obj);
	
	/**
	 * 删除对象
	 * @param obj
	 */
	public void deleteObj(Object obj);
	
	/**
	 * 根据对象，主键获取对象
	 * @param obj
	 * @param innerid
	 * @return
	 */
	public Object getObject(Object obj,String innerid);
	
	/**
	 * 查询一组对象，返回对象集合
	 * @param filter
	 * @param classId
	 * @return
	 */
	public List<PTFactor> getObjs(FilterParam fp, String classId);
	
	/**
	 * 保存String 类型的cid，及根据cid生成的long类型的lid,并将结果返回
	 * @param map map<"abc","abc">
	 * @return map<"abc",111>
	 */
	public Map<String,Long> saveMapIdLong(Map<String,String> map);
	/**
	 * 根据String 类型的cid,查询对应的lid
	 * @param map map<111,111>
	 * @return map<111,"abc">
	 */
	public Map<Long,String> getMapIdLong(Map<Long,Long> map);
}
