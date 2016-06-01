package com.bjsasc.twc.brace.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import com.bjsasc.platform.objectmodel.business.persist.PTFactor;
import com.bjsasc.platform.objectmodel.business.persist.PersistTemplate;
import com.bjsasc.platform.webframework.bean.FilterParam;
import com.bjsasc.platform.webframework.bean.Table;
import com.bjsasc.platform.webframework.spring.jdbc.JdbcDao;
/**
 * 基础Dao层接口
 * @author 陈建立
 * 2015-12-18
 */
public interface BraceDao{
	public PersistTemplate getTemplate();
	
	public JdbcDao getJdbc();
	
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
	 * 按照对象模型ClassId，FilterParam过滤条件分页查询
	 * @param filter
	 * @param classId
	 * @return
	 */
	public Table getObjsForPage(FilterParam filter,String classId);
	
	/**
	 * 获取一组对象
	 * 
	 * @param classId类标识
	 * @return 一组对象实例
	 */
	public List<Object> getAllByClassId(String classId);
	
	/**
	 * 查询一组对象，返回对象集合
	 * @param filter
	 * @param classId
	 * @return
	 */
	public List<PTFactor> getObjs(FilterParam fp, String classId);
	
	/**
	 * 根据主键,classid，删除对象
	 * @param list
	 * @return
	 */
	public int deleteObj(String innerid,String classId);
	
	/**
	 * 删除对象集合
	 * @param entities
	 */
	@SuppressWarnings("rawtypes")
	public void deleteAll(Collection entities);
	
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
	
	/**
	 * 根据表名、内部标识删除记录
	 * @param tableName
	 * @param innerid
	 */
	public void deleteObjByTableAndInnerID(String tableName,String innerid);
	
}
