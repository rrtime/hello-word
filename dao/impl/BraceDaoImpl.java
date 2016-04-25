package com.bjsasc.twc.brace.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataAccessException;

import com.bjsasc.platform.objectmodel.business.persist.PTFactor;
import com.bjsasc.platform.objectmodel.business.persist.PersistTemplate;
import com.bjsasc.platform.objectmodel.managed.external.util.ModelInfoUtil;
import com.bjsasc.platform.objectmodel.managed.modelinfo.data.CommonModel;
import com.bjsasc.platform.webframework.bean.FilterParam;
import com.bjsasc.platform.webframework.bean.Table;
import com.bjsasc.platform.webframework.spring.jdbc.JdbcDao;
import com.bjsasc.platform.webframework.util.FilterParamUtil;
import com.bjsasc.twc.brace.dao.BraceDao;
import com.bjsasc.twc.brace.util.CommonUtilTool;
import com.bjsasc.twc.flow.data.IdToLong;
import com.cascc.platform.aa.api.util.AAUtil;
import com.cascc.platform.dbmanager.ConnectionManager2;
import com.cascc.platform.util.PlatformException;
import com.cascc.platform.uuidservice.UUID;
import com.uid.longuid.LUUID;



/**
 * 基础Dao 层实现
 * @author 陈建立
 * 2015-12-18
 */
public class BraceDaoImpl implements BraceDao{
	private PersistTemplate template;
	private JdbcDao jdbc;// 平台自带用于分页
	public PersistTemplate getTemplate() {
		return template;
	}
	public void setTemplate(PersistTemplate template) {
		this.template = template;
	}
	public JdbcDao getJdbc() {
		return jdbc;
	}
	public void setJdbc(JdbcDao jdbc) {
		this.jdbc = jdbc;
	}
	
	public void testSaveObj(Object obj){
		getTemplate().save(obj);
	}
	
	/**
	 * 分页查询
	 * @param fp
	 * @param sql
	 * @param orderby
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected Table getTableBysql(FilterParam fp, String sql,String orderby) {
		String countSQL = "";
		String querySQL = "";
		if (fp != null) {
			String whereStr = FilterParamUtil.toWhere4E(fp);
			if (!(AAUtil.isEmpty(whereStr))) {
				sql = " from ( select * "+ sql + ") where (" + whereStr + ")";
			}
			countSQL = "select count(*) " + sql;
			String orderByStr = FilterParamUtil.toOrder4E(fp);
			if (!(AAUtil.isEmpty(orderByStr))) {
				sql = sql + " ORDER BY " + orderByStr;
			}else{
				if(!CommonUtilTool.emptyString(orderby)){
					sql = sql + " ORDER BY " + orderby;
				}
			}
			int start = fp.getStart();
			int limit = fp.getLimit();
			querySQL = "select * " + sql +"";
			if(limit>0){
				querySQL = ConnectionManager2.getOraclePageSQL(querySQL, start + 1,
						start + limit);
			}
			int count = getJdbc().queryForInt(countSQL);
			if(count>0){
				List data = getJdbc().queryForList(querySQL.toString());
				return new Table(data, count);
			}else{
				List data = new ArrayList();
				return new Table(data, 0);
			}
			
		} else {
			if(!CommonUtilTool.emptyString(orderby)){
				sql = sql + " ORDER BY " + orderby;
			}
			querySQL = "select * " + sql;
			List data = getJdbc().queryForList(querySQL.toString());
			if(data!=null){
				return new Table(data, data.size());
			}else{
				return new Table(new ArrayList(), 0);
			}
			
		}
		
	}
	
	/**
	 * 保存对象到数据库
	 */
	public void saveObj(Object object){
		getTemplate().save(object);
	}
	
	/**
	 * 更新对象
	 */
	public void updateObj(Object obj){
		getTemplate().update(obj);
	}
	
	/**
	 * 删除对象
	 */
	public void deleteObj(Object obj){
		getTemplate().delete(obj);
	}
	
	public Object getObject(Object obj,String innerid){
		return getTemplate().get(obj.getClass(), innerid);
	}
	@SuppressWarnings("rawtypes")
	@Override
	public Table getObjsForPage(FilterParam fp, String classId) {
		if(!CommonUtilTool.emptyString(classId)){
			//ModelInfo modelInfo = ModelInfoUtil.getModel(classId);
			CommonModel modelInfo = ModelInfoUtil.getCommonModel(classId);
			if(modelInfo!=null){
				String countSQL = "";
				String querySQL = "";
				String tableName = modelInfo.getTableName();
				String sql = " from "+ tableName;
				if (fp != null) {
					String whereStr = FilterParamUtil.toWhere4E(fp);
					if (!(AAUtil.isEmpty(whereStr))) {
						sql = " from ( select * "+ sql + ") where (" + whereStr + ")";
					}
					countSQL = "select count(*) " + sql;
					String orderByStr = FilterParamUtil.toOrder4E(fp);
					if (!(AAUtil.isEmpty(orderByStr))) {
						sql = sql + " ORDER BY " + orderByStr;
					}
					int start = fp.getStart();
					int limit = fp.getLimit();
					querySQL = "select * " + sql +"";
					if(limit>0){
						querySQL = ConnectionManager2.getOraclePageSQL(querySQL, start + 1,
								start + limit);
					}
					int count = getJdbc().queryForInt(countSQL);
					if(count>0){
						List data = getJdbc().queryForList(querySQL.toString());
						return new Table(data, count);
					}else{
						List data = new ArrayList();
						return new Table(data, 0);
					}
					
				} else {
					querySQL = "select * " + sql;
					List data = getJdbc().queryForList(querySQL.toString());
					if(data!=null){
						return new Table(data, data.size());
					}else{
						return new Table(new ArrayList(), 0);
					}
					
				}
				
			}
		}
		return new Table(new ArrayList(), 0);
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Object> getAllByClassId(String classId) {
		if(!CommonUtilTool.emptyString(classId)){
			//ModelInfo modelInfo = ModelInfoUtil.getModel(classId);
			CommonModel modelInfo = ModelInfoUtil.getCommonModel(classId);
			if(modelInfo!=null){
				String querySQL = "";
				String tableName = modelInfo.getTableName();
				String sql = " from "+ tableName;
				querySQL = "select * " + sql;
				List data = getJdbc().queryForList(querySQL.toString());
				if(data!=null){
					return data;
				}else{
					return new ArrayList<Object>();
				}
			}
		}
		return new ArrayList<Object>();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<PTFactor> getObjs(FilterParam fp, String classId) {
		if(!CommonUtilTool.emptyString(classId)){
			CommonModel modelInfo = ModelInfoUtil.getCommonModel(classId);
			if(modelInfo!=null){
				String className = modelInfo.getClassName();
				StringBuffer from = new StringBuffer();
				from.append(" FROM ");
				from.append(className);
				if (fp != null) {
					String whereStr = FilterParamUtil.toWhere4E(fp);
					if (!(AAUtil.isEmpty(whereStr))) {
						from.append(" where "+whereStr) ;
					}
					String orderByStr = FilterParamUtil.toOrder4E(fp);
					if (!(AAUtil.isEmpty(orderByStr))) {
						from.append(" ORDER BY " + orderByStr);
					}
					int start = fp.getStart();
					int limit = fp.getLimit();
					if(limit>0){
						String querySQL = (new StringBuilder()).append(from.toString()).toString();
						List<PTFactor> pts = getTemplate().findForPage(querySQL, start, limit);
						return pts;
					}else{
						String querySQL = (new StringBuilder()).append(from.toString()).toString();
						List<PTFactor> pts = getTemplate().find(querySQL);
						return pts;
					}
				}else{
					String querySQL = (new StringBuilder()).append(from.toString()).toString();
					List<PTFactor> pts = getTemplate().find(querySQL);
					return pts;
				}
				
			}
		}
		return null;
	}
	
	@Override
	public int deleteObj(String innerid, String classId) {
		CommonModel modelInfo = ModelInfoUtil.getCommonModel(classId);
		String tableName = modelInfo.getTableName();
		String sql = "delete from "+tableName+" where innerid ='"+innerid+"'"; 
		return getJdbc().update(sql);
	}
	@SuppressWarnings("rawtypes")
	@Override
	public void deleteAll(Collection entities) {
		try {
			getTemplate().deleteAll(entities);
		} catch (DataAccessException ex) {
			throw new PlatformException("pt.ptBase.PersistTemplateImpl.deleteAll.err", "删除多个对象时出错。", ex);
		}

	}
	@Override
	public Map<String, Long> saveMapIdLong(Map<String, String> map) {
		if(map!=null){
			Map<String,Long> resultmap = new HashMap<String,Long>();
			Set<String> key = map.keySet();
			for(Iterator<String> i$ = key.iterator();i$.hasNext();){
			   String strKey = (String)i$.next();
			   //根据key生成long 类型的映射lid，并保存到Map中
			   long lid = LUUID.getLongUid();
			   resultmap.put(strKey, lid);
			   IdToLong itl = new IdToLong();
			   itl.setInnerId(UUID.getUID());
			   itl.setClassId("IdToLong");
			   itl.setCid(strKey);
			   itl.setLid(lid);
			   getTemplate().save(itl);
			}
			return resultmap;
		}
		return null;
	}
	@Override
	public Map<Long,String> getMapIdLong(Map<Long, Long> map) {
		Map<Long,String> resultmap = null;
		if(map!=null){
			String hql = "from IdToLong where lid in (";
			Set<Long> key = map.keySet();
			String ids = "";
			for(Iterator<Long> i$ = key.iterator();i$.hasNext();){
				Long strKey = (Long)i$.next();
				ids = ids+"'"+strKey+"',";
			}
			if(ids.length()>0){
				hql = hql+ids.substring(0,ids.length()-1)+")";
				List<?> list = getTemplate().find(hql);
				if(CommonUtilTool.isListNotNull(list)){
					resultmap = new HashMap<Long,String>();
					for(int i=0;i<list.size();i++){
						IdToLong itl = (IdToLong)list.get(i);
						resultmap.put(itl.getLid(),itl.getCid());
					}
				}
			}
			
		}
		return resultmap;
	}
	@Override
	public void deleteObjByTableAndInnerID(String tableName, String innerid) {
		String sql = "delete from "+tableName+" where innerid='"+innerid+"'";
		getTemplate().bulkUpdateBySql(sql);
	}
	
}
