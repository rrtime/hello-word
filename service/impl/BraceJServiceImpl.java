package com.bjsasc.twc.brace.service.impl;


import java.util.List;

import com.bjsasc.platform.webframework.bean.FilterParam;
import com.bjsasc.platform.webframework.bean.Table;
import com.bjsasc.twc.brace.dao.BraceDao;
import com.bjsasc.twc.brace.service.BraceJService;

/**
 * 基础服务层jdbc方式实现
 * @author 陈建立
 * 2015-12-18
 */
public class BraceJServiceImpl implements BraceJService{
	private BraceDao jdbcDao;
	
	public BraceDao getJdbcDao() {
		return jdbcDao;
	}
	public void setJdbcDao(BraceDao jdbcDao) {
		this.jdbcDao = jdbcDao;
	}
	
	@Override
	public Table getObjsForPage(FilterParam filter, String classId) {
		return getJdbcDao().getObjsForPage(filter, classId);
	}
	@Override
	public List<Object> getAllByClassId(String classId) {
		return getJdbcDao().getAllByClassId(classId);
	}
	
	@Override
	public int deleteObjList(List<String> list,String classId) {
		for(int i=0;i<list.size();i++){
			int ret = getJdbcDao().deleteObj(list.get(i),  classId);
			if(ret<1){
				throw new RuntimeException(String.valueOf(ret));
			}
		}
		return 1;
	}
	
}
