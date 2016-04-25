package com.bjsasc.twc.brace.service.impl;

import java.util.List;
import java.util.Map;

import com.bjsasc.platform.objectmodel.business.persist.PTFactor;
import com.bjsasc.platform.webframework.bean.FilterParam;
import com.bjsasc.twc.brace.dao.BraceDao;
import com.bjsasc.twc.brace.service.BraceService;

/**
 * 基础服务层template方式实现
 * @author 陈建立
 * 2015-12-18
 */
public class BraceServiceImpl implements BraceService{
	private BraceDao templateDao;
	public BraceDao getTemplateDao() {
		return templateDao;
	}
	public void setTemplateDao(BraceDao templateDao) {
		this.templateDao = templateDao;
	}

	@Override
	public void saveObj(Object object) {
		getTemplateDao().saveObj(object);
	}
	@Override
	public void updateObj(Object obj) {
		getTemplateDao().updateObj(obj);
	}
	@Override
	public void deleteObj(Object obj) {
		getTemplateDao().deleteObj(obj);
	}
	@Override
	public Object getObject(Object obj, String innerid) {
		return getTemplateDao().getObject(obj, innerid);
	}
	
	@Override
	public List<PTFactor> getObjs(FilterParam fp, String classId) {
		return getTemplateDao().getObjs(fp, classId);
	}
	@Override
	public Map<String, Long> saveMapIdLong(Map<String, String> map) {
		return getTemplateDao().saveMapIdLong(map);
	}
	@Override
	public Map<Long, String> getMapIdLong(Map<Long, Long> map) {
		return getTemplateDao().getMapIdLong(map);
	}
	
}
