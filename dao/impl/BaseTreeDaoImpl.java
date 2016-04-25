package com.bjsasc.twc.brace.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bjsasc.platform.objectmodel.business.persist.PersistTemplate;
import com.bjsasc.platform.objectmodel.managed.modelinfo.data.CommonModel;
import com.bjsasc.platform.objectmodel.managed.modelinfo.util.ModelUtil;
import com.bjsasc.platform.webframework.spring.jdbc.JdbcDao;
import com.bjsasc.twc.brace.dao.BaseTreeDao;
import com.bjsasc.twc.brace.data.BaseTree;
import com.bjsasc.twc.brace.data.TreeObject;
import com.bjsasc.twc.brace.util.BaseTreeUtil;
import com.bjsasc.twc.brace.util.BeanXMLMapping;
import com.bjsasc.twc.brace.util.CommonUtilTool;
import com.bjsasc.twc.brace.util.ReadConfig;
import com.bjsasc.twc.brace.util.UtilStatusDictionary;
import com.bjsasc.twc.demand.data.DemandPoint;

/**
 * BaseTree Dao 实现层
 * @author 陈建立
 * 2015-12-18
 */
public class BaseTreeDaoImpl extends BraceDaoImpl implements BaseTreeDao{
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
	
	@Override
	public List<Map<String,Object>> getBaseTreeList(String parentid,BaseTree basetree){
		CommonModel modelInfo = ModelUtil.getPublishedService().getCommonModel(basetree.getClass().getSimpleName());
		String talbename = modelInfo.getTableName() ;
		String sql = "select t.*, to_date('1970-01-01 08:00:00','yyyy-MM-dd HH24:MI:SS')+(t.createtime /86400000 ) as strcreatetime  from  "+talbename+" t where parentid='"+parentid+"' order by sequencenumber";
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> result = getJdbc().queryForList(sql);
		if(result==null){
			result=new ArrayList<Map<String,Object>>();
		}
		return result;
	}
	
	@Override
	public List<?> getBaseTreeListByTemplate(String parentid,BaseTree basetree){
		String simpleName = basetree.getClass().getSimpleName();
		String hql = "from "+simpleName+" t where parentid='"+parentid+"' order by sequencenumber";
		List<?> result = getTemplate().find(hql);
		if(result==null){
			result=new ArrayList<BaseTree>();
		}
		return result;
	}
	
	@Override
	public List<Map<String,Object>> getBaseTreeHasRevList(String parentid,BaseTree basetree){
		CommonModel modelInfo = ModelUtil.getPublishedService().getCommonModel(basetree.getClass().getSimpleName());
		String talbename = modelInfo.getTableName() ;
		String groupidsql = "select max(revision) as revision,id from "+talbename+" where parentid='"+parentid+"' and nodetype='0' group by id ";//
		String sql1 = "select b.* from "+talbename +" b , ("+groupidsql+") t"+" where nvl(b.revision,0)=nvl(t.revision,0) and b.id=t.id and b.parentid='"+parentid+"' and b.nodetype='0'" ;//查询正常节点,最新版本
		String sql = " select t.*, to_date('1970-01-01 08:00:00','yyyy-MM-dd HH24:MI:SS')+(t.createtime /86400000 ) as strcreatetime from ("+sql1+") t order by sequencenumbert";
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> result = getJdbc().queryForList(sql);
		if(result==null){
			result=new ArrayList<Map<String,Object>>();
		}
		return result;
	}
	
	@Override
	public List<Map<String,Object>> getlogictreeList(String parentid,BaseTree basetree){
		CommonModel modelInfo = ModelUtil.getPublishedService().getCommonModel(basetree.getClass().getSimpleName());
		String talbename = modelInfo.getTableName() ;
		String groupidsql = "select max(revision) as revision,id from  "+talbename+" where parentid='"+parentid+"' and nodetype!='-1' group by id ";//
		String sql1 = "select b.*,b.sequencenumber as sequencenumbert,b.nodetype as nodetypet,b.hrevsion as hrevsiont,b.hinnerid as hinneridt,b.revision as revisiont,b.parentid as parentidt,b.id as idt from "+talbename +" b , ("+groupidsql+") t"+" where nvl(b.revision,0)=nvl(t.revision,0) and b.id=t.id and b.parentid='"+parentid+"' and b.nodetype='0'" ;//查询正常节点,最新版本
		//一个节点引用关系只能引用一次，不能引用已经是引用节点的节点
		//查询引用节点最新版本
		String quotesql = "select * from  "+talbename+" where parentid='"+parentid+"' and nodetype='-1' and hrevsion='-1'";
		//String groupquotesql = "select max(b.revision) as revision,b.id as id ,t.sequencenumber from  "+talbename+" b , ("+quotesql+") t where b.id=t.hinnerid group by b.id";//查询引用节点
		String groupquotesql = "select max(b.revision) as revision,b.id as id,max(b.sequencenumbert) as sequencenumbert,max(b.nodetypet) as nodetypet,max(b.hrevsiont) as hrevsiont,max(b.hinneridt) as hinneridt,max(b.revisiont) as revisiont,max(b.parentidt) as parentidt,max(b.idt) as idt  from (select b.*,t.sequencenumber as sequencenumbert,t.nodetype as nodetypet,t.hrevsion as hrevsiont,t.hinnerid as hinneridt,t.revision as revisiont,t.parentid as parentidt,t.id as idt  from "+talbename+"  b , ("+quotesql+") t where b.id=t.hinnerid ) b group by b.id";
		String sql2 = "select b.*,t.sequencenumbert as sequencenumbert,t.nodetypet as nodetypet,t.hrevsiont as hrevsiont,t.hinneridt as hinneridt,t.revisiont as revisiont,t.parentidt as parentidt,t.idt as idt from  "+talbename+" b,("+groupquotesql+") t  where b.id=t.id and nvl(b.revision,0)=nvl(t.revision,0) ";
		//查询引用节点按照版本号查询
		String quotesql2 = "select * from  "+talbename+" where parentid='"+parentid+"' and nodetype='-1' and hrevsion!='-1'";
		String sql3 = "select b.*,t.sequencenumber as sequencenumbert,t.nodetype as nodetypet,t.hrevsion as hrevsiont,t.hinnerid as hinneridt,t.revision as revisiont,t.parentid as parentidt,t.id as idt from "+talbename+" b,("+quotesql2+") t  where b.id=t.id and nvl(b.revision,0)=nvl(t.hrevsion,0) ";
		String sql = " select t.*, to_date('1970-01-01 08:00:00','yyyy-MM-dd HH24:MI:SS')+(t.createtime /86400000 ) as strcreatetime from ("+sql1+" union "+sql2+" union "+sql3 +") t order by sequencenumbert";
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> result = getJdbc().queryForList(sql);
		if(result==null){
			result=new ArrayList<Map<String,Object>>();
		}
		return result;
	}
	
	@Override
	public Map<String, Object> getBaseTreeMap(String innerid, BaseTree basetree) {
		CommonModel modelInfo = ModelUtil.getPublishedService().getCommonModel(basetree.getClass().getSimpleName());
		String talbename = modelInfo.getTableName() ;
		String sql = "select t.*, to_date('1970-01-01 08:00:00','yyyy-MM-dd HH24:MI:SS')+(t.createtime /86400000 ) as strcreatetime  from  "+talbename+" t where innerid='"+innerid+"'";
		@SuppressWarnings("unchecked")
		Map<String,Object> result = getJdbc().queryForMap(sql);
		return result;
	}
	
	@Override
	public boolean saveBaseTreeNextList(List<TreeObject> nextList,Object lastobj){
		try{
			if(CommonUtilTool.isListNotNull(nextList)){
				for(int i=0;i<nextList.size();i++){
	    			TreeObject treeObject = nextList.get(i);
	    			Object obj = treeObject.getObj();//节点对象
	    			String lastinnerid = CommonUtilTool.DelNull(BeanXMLMapping.getFieldValueByName("innerId",lastobj));
	    			String type = ReadConfig.getTreeRevisionType(obj);
	    			obj = BaseTreeUtil.setTreePro(obj,lastinnerid,type,null,null,null);
	    			saveObj(obj);
	    			//扩展对象
	    			Object expObj = treeObject.getExpObj();//节点扩展对象
	    			if(expObj!=null){
						expObj = BaseTreeUtil.setExpObjPro(expObj,obj,null);
						saveObj(expObj);
					}
	    			List<TreeObject> tempnextlist=treeObject.getNextList();//子集
	    			saveBaseTreeNextList(tempnextlist,obj);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}
		return true;
	}
	
	@Override
	public boolean updateBaseTreeNextList(List<TreeObject> nextList,Object lastobj) {
		try{
			if(CommonUtilTool.isListNotNull(nextList)){
				for(int i=0;i<nextList.size();i++){
	    			TreeObject treeObject = nextList.get(i);
	    			Object obj = treeObject.getObj();//节点对象
	    			Class<?> thisclass = obj.getClass();
					String simpleName = thisclass.getSimpleName();
	    			String lastinnerid = CommonUtilTool.DelNull(BeanXMLMapping.getFieldValueByName("innerId",lastobj));
	    			String innerid = CommonUtilTool.DelNull(BeanXMLMapping.getFieldValueByName("innerId",obj));
	    			if(CommonUtilTool.emptyString(innerid)){//插入记录
	    				String type = ReadConfig.getTreeRevisionType(obj);
		    			obj = BaseTreeUtil.setTreePro(obj,lastinnerid,type,null,null,null);
		    			saveObj(obj);
		    			//扩展对象
						Object expObj = treeObject.getExpObj();//节点扩展对象
						if(expObj!=null){
							expObj = BaseTreeUtil.setExpObjPro(expObj,obj,null);
							saveObj(expObj);
						}
		    			List<TreeObject> tempnextlist=treeObject.getNextList();//子集
		    			updateBaseTreeNextList(tempnextlist,obj);
	    			}else{//含记录判断是否为更新状态
	    				String hasupdate = CommonUtilTool.DelNull(BeanXMLMapping.getFieldValueByName("hasupdate",obj));
	    				if(hasupdate.equalsIgnoreCase(UtilStatusDictionary.HasUpdateTrue)){//更新
	    					String type = ReadConfig.getTreeRevisionType(obj);
							if(!CommonUtilTool.emptyString(type)){
								if(type.equalsIgnoreCase("1")){//为1时有节点版本
									Object objl = getObject(obj,innerid);
									String revision =CommonUtilTool.DelNull(BeanXMLMapping.getFieldValueByName("revision", objl));	
									String id = CommonUtilTool.DelNull(BeanXMLMapping.getFieldValueByName("id", objl));
									obj = BaseTreeUtil.setTreePro(obj,lastinnerid,type,revision,id,null);
									saveObj(obj);
									//扩展对象
									Object expObj = treeObject.getExpObj();//节点扩展对象
									if(expObj!=null){
										expObj = BaseTreeUtil.setExpObjPro(expObj,obj,null);
										saveObj(expObj);
									}
								}else{//为0时无节点版本
									obj = BaseTreeUtil.setTreePro(obj,lastinnerid,type,null,null,innerid);
									updateObj(obj);
									
									String contenttype = CommonUtilTool.DelNull(treeObject.getContenttype());
									String contentoldtype = CommonUtilTool.DelNull(treeObject.getContentoldtype());
									//扩展对象
									Object expObj = treeObject.getExpObj();//根节点扩展对象
									if(expObj!=null){
										String expinnerid = CommonUtilTool.DelNull(BeanXMLMapping.getFieldValueByName("innerId",expObj));
										expObj = BaseTreeUtil.setExpObjPro(expObj,obj,expinnerid);
										if(contenttype.equals(contentoldtype)){//类型没变化，则直接更新
											updateObj(expObj);
										}else{//否则删除原对象，保存新对象
											//获取原对象表
											String oldtableName = ReadConfig.getContenTable(simpleName+contentoldtype);
											//删除历史记录
											deleteObjByTableAndInnerID(oldtableName,expinnerid);
											//保存新对象
											saveObj(expObj);
										}
									}	
								}
							}
							List<TreeObject> tempnextlist=treeObject.getNextList();//子集
		    				updateBaseTreeNextList(tempnextlist,obj);
	    				}else if(hasupdate.equalsIgnoreCase(UtilStatusDictionary.HasUpdateDelete)){//删除节点
	    					obj = BeanXMLMapping.setFileValue(obj,"delstatus",UtilStatusDictionary.TreeDelStatus);
							updateObj(obj);
	    				}else if(hasupdate.equalsIgnoreCase(UtilStatusDictionary.HasUpdateInvalid)){//作废节点
	    					obj = BeanXMLMapping.setFileValue(obj,"delstatus",UtilStatusDictionary.TreeInvalidStatus);
							updateObj(obj);
	    				}else{//更新父级parentid
	    					obj = BeanXMLMapping.setFileValue(obj,"parentid",lastinnerid);
							updateObj(obj);
	    				}
	    				
	    			}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}
		return true;
	}
	@Override
	public void deleteFileXmlByInnerid(String innerid) {
		String sql = "delete from twc_hisxml_tab t where t.innerid='"+innerid+"'";
		getJdbc().update(sql);
		
	}
	@Override
	public void deleteFileXmlByFileiid(String fileiid) {
		String sql = "delete from twc_hisxml_tab t where t.fileiid='"+fileiid+"'";
		getJdbc().update(sql);
	}
	@SuppressWarnings("rawtypes")
	@Override
	public Map<String,String> saveMaxRevsionByRoot(DemandPoint demandPoint,String type) {
		Map<String,String> res = null;
		if(demandPoint!=null){
			String rootinnerid = demandPoint.getFkinnerid();
			synchronized(rootinnerid){//根据根节点锁定查询最大版本，防止出现版本相同情况
				if(!CommonUtilTool.emptyString(type)){
					String sql = "select Max(revision) as revision from twc_demandpoint_tab t where t.fkinnerid='"+rootinnerid+"'";
					List list = null;
					if(type.equals("0")){
						list = getJdbc().queryForList(sql);
						if(CommonUtilTool.isListNotNull(list)){
							res = new HashMap<String,String>();
							Map map = (Map)list.get(0);
							String revision = CommonUtilTool.DelNull(map.get("REVISION"));
							res.put("REVISION", revision);
							String newrev =CommonUtilTool.revisionToNext(revision);
							res.put("NEWREVISION", newrev);
							demandPoint.setRevision(newrev);
							//插入新记录
							StringBuffer insertsql = new StringBuffer();
							insertsql.append("insert into twc_demandpoint_tab(innerid, classId, fkinnerid, fkfileinnerid, revision,createtime)values(");
							insertsql.append("'"+demandPoint.getInnerId()+"'");
							insertsql.append(",'"+demandPoint.getClassId()+"'");
							insertsql.append(",'"+demandPoint.getFkinnerid()+"'");
							insertsql.append(",'"+demandPoint.getFkfileinnerid()+"'");
							insertsql.append(",'"+demandPoint.getRevision()+"'");
							insertsql.append(",'"+demandPoint.getCreatetime()+"')");
							getJdbc().update(insertsql.toString());
						}
					}else{
						list = getTemplate().query(sql);
						if(CommonUtilTool.isListNotNull(list)){
							res = new HashMap<String,String>();
							Map map = (Map)list.get(0);
							String revision = CommonUtilTool.DelNull(map.get("REVISION"));
							res.put("REVISION", revision);
							String newrev =CommonUtilTool.revisionToNext(revision);
							res.put("NEWREVISION", newrev);
							demandPoint.setRevision(newrev);
							//插入新记录
							saveObj(demandPoint);
						}
					}
					
				}
			}
		}
		return res;
	}
	
}
