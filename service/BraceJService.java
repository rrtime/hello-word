package com.bjsasc.twc.brace.service;

import java.util.List;

import com.bjsasc.platform.webframework.bean.FilterParam;
import com.bjsasc.platform.webframework.bean.Table;
import com.bjsasc.twc.brace.dao.BraceDao;

/**
 * 基础服务层JDBC方式接口
 * @author 陈建立
 * 2015-12-18
 */
public interface BraceJService {
	public BraceDao getJdbcDao();
	
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
	 * 根据主键集合,classID，批量删除对象集合
	 * @param list
	 * @return
	 */
	public int deleteObjList(List<String> list,String classId);
	
	
}
