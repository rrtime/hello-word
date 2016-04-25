package com.bjsasc.twc.brace.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bjsasc.twc.brace.dao.BaseTreeDao;
import com.bjsasc.twc.brace.data.BaseTree;
import com.bjsasc.twc.brace.service.BaseTreeJService;
import com.bjsasc.twc.brace.util.CommonUtilTool;
import com.bjsasc.twc.brace.util.ReadConfig;
import com.bjsasc.twc.demand.data.DemandPoint;

/**
 * BaseTree服务层JDBC方式实现
 * @author 陈建立
 * 2015-12-18
 */
public class BaseTreeJServiceImpl implements BaseTreeJService{

	private BaseTreeDao jdbcDao;
	
	public BaseTreeDao getJdbcDao() {
		return jdbcDao;
	}
	public void setJdbcDao(BaseTreeDao jdbcDao) {
		this.jdbcDao = jdbcDao;
	}
	
	@Override
	public List<Map<String,Object>> getBaseTreeList(String parentid,BaseTree basetree){
		try{
			List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> list = null;
			String type = ReadConfig.gettreeTypeMap(basetree);//获取树引用节点标识，有引用节点则一定要有版本
			if(!CommonUtilTool.emptyString(type)){
				if(type.equalsIgnoreCase("1")){//有引用节点，按照引用模式查询
					list = getJdbcDao().getlogictreeList(parentid, basetree);
				}else{//存储树等无引用节点直接查询
					String typerevision = ReadConfig.getTreeRevisionType(basetree);
					if(typerevision.equals("1")){//为1时有节点版本，为0时无节点版本
						list = getJdbcDao().getBaseTreeHasRevList(parentid, basetree);
					}else{
						list = getJdbcDao().getBaseTreeList(parentid, basetree);
					}
					
				}
			}
			if(list!=null){
				result = list;
			}
			return result;
		}catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
		
	}
	
	@Override
	public void deleteFileXmlByInnerid(String innerid){
		getJdbcDao().deleteFileXmlByInnerid(innerid);
	}
	
	@Override
	public void deleteFileXmlByFileiid(String fileiid){
		getJdbcDao().deleteFileXmlByFileiid(fileiid);
	}
	@Override
	public Map<String, String> saveMaxRevsionByRootByJdbc(DemandPoint demandPoint) {
		return getJdbcDao().saveMaxRevsionByRoot(demandPoint,"0");
	}
	
	
}
