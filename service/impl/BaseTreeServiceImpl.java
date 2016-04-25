package com.bjsasc.twc.brace.service.impl;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.bjsasc.platform.objectmodel.managed.modelinfo.data.CommonModel;
import com.bjsasc.twc.brace.dao.BaseTreeDao;
import com.bjsasc.twc.brace.data.BaseTree;
import com.bjsasc.twc.brace.data.TreeFloor;
import com.bjsasc.twc.brace.data.TreeObject;
import com.bjsasc.twc.brace.service.BaseTreeService;
import com.bjsasc.twc.brace.util.BaseTreeUtil;
import com.bjsasc.twc.brace.util.BeanXMLMapping;
import com.bjsasc.twc.brace.util.CommonUtilTool;
import com.bjsasc.twc.brace.util.FileSer;
import com.bjsasc.twc.brace.util.UtilStatusDictionary;
import com.bjsasc.twc.brace.data.Hisxml;
import com.bjsasc.twc.brace.util.ReadConfig;
import com.bjsasc.twc.demand.data.DemandPoint;
import com.cascc.platform.uuidservice.UUID;

/**
 * BaseTree服务层template方式实现
 * @author 陈建立
 * 2015-12-18
 */
public class BaseTreeServiceImpl implements BaseTreeService{

	private BaseTreeDao templateDao;
	
	public BaseTreeDao getTemplateDao() {
		return templateDao;
	}
	public void setTemplateDao(BaseTreeDao templateDao) {
		this.templateDao = templateDao;
	}
	
	@Override
	public boolean saveBaseTreeList(TreeObject treeObject) {
		boolean has = false;
		try{
			if(treeObject!=null){
				Object obj = treeObject.getObj();//根节点对象
				Class<?> thisclass = obj.getClass();
				String objname = thisclass.getName();
				String simpleName = thisclass.getSimpleName();
				if(objname.equals(BaseTree.class.getName())){//如果为BaseTree
				}else{
					 //取得父类名称
		 	        Class<?> superclass = thisclass.getSuperclass();
		 	        String superclassname = superclass.getName();
		 	        if(superclassname.equals(BaseTree.class.getName())){//继承BaseTree
		 	        }else if(superclassname.equals(CommonModel.class.getName())){//继承 CommonModel
		 	        }else{//不满足要求返回空
		 	        	throw new RuntimeException("该对象不符合存储要求");
		 	        }
				}
				//根节点是否为新，如果为新，则底层节点均插入
				//String[] mfields = BeanXMLMapping.getFieldForTreeObject(treeObject);
				String rootinnerid = CommonUtilTool.DelNull(BeanXMLMapping.getFieldValueByName("innerId",obj));
				if(CommonUtilTool.emptyString(rootinnerid)){//新增状态，均为插入
					String type = ReadConfig.getTreeRevisionType(obj);
					obj = BaseTreeUtil.setTreePro(obj,"-1",type,null,null,null);
					getTemplateDao().saveObj(obj);
					//扩展对象
					Object expObj = treeObject.getExpObj();//根节点扩展对象
					if(expObj!=null){
						expObj = BaseTreeUtil.setExpObjPro(expObj,obj,null);
						getTemplateDao().saveObj(expObj);
					}
					List<TreeObject> nextlist=treeObject.getNextList();//子集
					has = getTemplateDao().saveBaseTreeNextList(nextlist,obj);
				}else{//更新状态，按照hasupdate进行更新
					String hasupdate = CommonUtilTool.DelNull(BeanXMLMapping.getFieldValueByName("hasupdate",obj));
					if(hasupdate.equalsIgnoreCase(UtilStatusDictionary.HasUpdateTrue)){//更新根节点
						String type = ReadConfig.getTreeRevisionType(obj);
						if(!CommonUtilTool.emptyString(type)){
							if(type.equalsIgnoreCase("1")){//为1时有节点版本
								Object objl = getTemplateDao().getObject(obj,rootinnerid);
								String revision = CommonUtilTool.DelNull(BeanXMLMapping.getFieldValueByName("revision", objl));
								String id = CommonUtilTool.DelNull(BeanXMLMapping.getFieldValueByName("id", objl));
								obj = BaseTreeUtil.setTreePro(obj,"-1",type,revision,id,null);
								getTemplateDao().saveObj(obj);
								//扩展对象
								Object expObj = treeObject.getExpObj();//根节点扩展对象
								if(expObj!=null){
									expObj = BaseTreeUtil.setExpObjPro(expObj,obj,null);
									getTemplateDao().saveObj(expObj);
								}
							}else{//为0时无节点版本
								obj = BaseTreeUtil.setTreePro(obj,"-1",type,null,null,rootinnerid);
								getTemplateDao().updateObj(obj);
								String contenttype = CommonUtilTool.DelNull(treeObject.getContenttype());
								String contentoldtype = CommonUtilTool.DelNull(treeObject.getContentoldtype());
								//扩展对象
								Object expObj = treeObject.getExpObj();//根节点扩展对象
								if(expObj!=null){
									String expinnerid = CommonUtilTool.DelNull(BeanXMLMapping.getFieldValueByName("innerId",expObj));
									expObj = BaseTreeUtil.setExpObjPro(expObj,obj,expinnerid);
									if(contenttype.equals(contentoldtype)){//类型没变化，则直接更新
										getTemplateDao().updateObj(expObj);
									}else{//否则删除原对象，保存新对象
										//获取原对象表
										String oldtableName = ReadConfig.getContenTable(simpleName+contentoldtype);
										//删除历史记录
										getTemplateDao().deleteObjByTableAndInnerID(oldtableName,expinnerid);
										//保存新对象
										getTemplateDao().saveObj(expObj);
									}
								}	
							}
						}
						List<TreeObject> nextlist=treeObject.getNextList();//子集
						has = getTemplateDao().updateBaseTreeNextList(nextlist,obj);
					}else if(hasupdate.equalsIgnoreCase(UtilStatusDictionary.HasUpdateDelete)){//删除根节点
						obj = BeanXMLMapping.setFileValue(obj,"delstatus",UtilStatusDictionary.TreeDelStatus);
						getTemplateDao().updateObj(obj);
					}else if(hasupdate.equalsIgnoreCase(UtilStatusDictionary.HasUpdateInvalid)){//作废根节点
						obj = BeanXMLMapping.setFileValue(obj,"delstatus",UtilStatusDictionary.TreeInvalidStatus);
						getTemplateDao().updateObj(obj);
					}
				}
				//判断是否调用存储xml接口
				 String writetype=ReadConfig.getTreeWriteType(obj);
				 if(!CommonUtilTool.emptyString(writetype)){//调用写入文件接口
					 if(writetype.equals("1")){//上传到文件服务器
						 String writerStr=BeanXMLMapping.ObjectToXml(treeObject);//树生成xml字符串
						 InputStream input =CommonUtilTool.getStringStream(writerStr);//字符串转换输入流
						 String iid = FileSer.uploadFileForXML(rootinnerid+".xml",input,null);//上传到文件服务器（需求指标xml）
						 input.close();//关闭流
						 //建立关联关系
						 DemandPoint demandPoint = new DemandPoint();
						 demandPoint.setInnerId(UUID.getUID());
						 demandPoint.setClassId("DemandPoint");
						 demandPoint.setFkinnerid(rootinnerid);
						 demandPoint.setFkfileinnerid(iid);
						 demandPoint.setCreatetime(CommonUtilTool.getLongMinsDate());
						 //版本升级，不以root id为准，以升级保存的次数为准，取最大版本值
						 @SuppressWarnings("unused")
						 Map<String, String> revmap = saveMaxRevsionByRootByTemplate(demandPoint);
//						 String revision = CommonUtilTool.DelNull(BeanXMLMapping.getFieldValueByName("revision", obj));
					 }
				 }
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}
		return has;
	}
	@Override
	public String saveHisxmlByFileid(String fileiid,String cellName) {
		try{
			 Hisxml hisxml = new Hisxml();
			 String innerid = UUID.getUID();
			 hisxml.setClassId(innerid);
			 hisxml.setClassId("Hisxml");
			 hisxml.setFileiid(fileiid);
			 hisxml.setCellName(cellName);
			 hisxml.setCreatetime(CommonUtilTool.getLongMinsDate());
			 getTemplateDao().saveObj(hisxml);
			 return innerid;
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}
		
	}
	
	@Override
	public boolean saveBaseTreeList(List<TreeFloor> list,String parentid){
		if(CommonUtilTool.isListNotNull(list)){
			for(int i=0;i<list.size();i++){
				TreeFloor treeFloor = list.get(i);
				Object obj = treeFloor.getObj();
				Class<?> thisclass = obj.getClass();
				String objname = thisclass.getName();
				String simpleName = thisclass.getSimpleName();
				if(objname.equals(BaseTree.class.getName())){//如果为BaseTree
				}else{
					 //取得父类名称
		 	        Class<?> superclass = thisclass.getSuperclass();
		 	        String superclassname = superclass.getName();
		 	        if(superclassname.equals(BaseTree.class.getName())){//继承BaseTree
		 	        }else{//不满足要求返回空
		 	        	throw new RuntimeException("该对象不符合存储要求");
		 	        }
				}
				String innerid = CommonUtilTool.DelNull(BeanXMLMapping.getFieldValueByName("innerId",obj));
				if(CommonUtilTool.emptyString(innerid)){//新增状态，均为插入
					String type = ReadConfig.getTreeRevisionType(obj);
					obj = BaseTreeUtil.setTreePro(obj,parentid,type,null,null,null);
					getTemplateDao().saveObj(obj);
					//扩展对象
					Object expObj = treeFloor.getExpObj();//根节点扩展对象
					if(expObj!=null){
						expObj = BaseTreeUtil.setExpObjPro(expObj,obj,null);
						getTemplateDao().saveObj(expObj);
					}
				}else{//更新状态，按照hasupdate进行更新
					String hasupdate = CommonUtilTool.DelNull(BeanXMLMapping.getFieldValueByName("hasupdate",obj));
					if(hasupdate.equalsIgnoreCase(UtilStatusDictionary.HasUpdateTrue)){//更新节点
						String type = ReadConfig.getTreeRevisionType(obj);
						if(!CommonUtilTool.emptyString(type)){
							if(type.equalsIgnoreCase("1")){//为1时有节点版本
								Object objl = getTemplateDao().getObject(obj,innerid);
								String revision = CommonUtilTool.DelNull(BeanXMLMapping.getFieldValueByName("revision", objl));
								String id = CommonUtilTool.DelNull(BeanXMLMapping.getFieldValueByName("id", objl));
								obj = BaseTreeUtil.setTreePro(obj,parentid,type,revision,id,null);
								getTemplateDao().saveObj(obj);
								//扩展对象
								Object expObj = treeFloor.getExpObj();//节点扩展对象
								if(expObj!=null){
									expObj = BaseTreeUtil.setExpObjPro(expObj,obj,null);
									getTemplateDao().saveObj(expObj);
								}
							}else{//为0时无节点版本
								obj = BaseTreeUtil.setTreePro(obj,parentid,type,null,null,innerid);
								getTemplateDao().updateObj(obj);
								
								String contenttype = CommonUtilTool.DelNull(treeFloor.getContenttype());
								String contentoldtype = CommonUtilTool.DelNull(treeFloor.getContentoldtype());
								//扩展对象
								Object expObj = treeFloor.getExpObj();//根节点扩展对象
								if(expObj!=null){
									String expinnerid = CommonUtilTool.DelNull(BeanXMLMapping.getFieldValueByName("innerId",expObj));
									expObj = BaseTreeUtil.setExpObjPro(expObj,obj,expinnerid);
									if(contenttype.equals(contentoldtype)){//类型没变化，则直接更新
										getTemplateDao().updateObj(expObj);
									}else{//否则删除原对象，保存新对象
										//获取原对象表
										String oldtableName = ReadConfig.getContenTable(simpleName+contentoldtype);
										//删除历史记录
										getTemplateDao().deleteObjByTableAndInnerID(oldtableName,expinnerid);
										//保存新对象
										getTemplateDao().saveObj(expObj);
									}
								}	
							}
						}
					}else if(hasupdate.equalsIgnoreCase(UtilStatusDictionary.HasUpdateDelete)){//删除根节点
						obj = BeanXMLMapping.setFileValue(obj,"delstatus",UtilStatusDictionary.TreeDelStatus);
						getTemplateDao().updateObj(obj);
					}else if(hasupdate.equalsIgnoreCase(UtilStatusDictionary.HasUpdateInvalid)){//作废根节点
						obj = BeanXMLMapping.setFileValue(obj,"delstatus",UtilStatusDictionary.TreeInvalidStatus);
						getTemplateDao().updateObj(obj);
					}else{//更新父级parentid
    					obj = BeanXMLMapping.setFileValue(obj,"parentid",parentid);
    					getTemplateDao().updateObj(obj);
    				}
				}
			}
		}
		return true;
	}
	@Override
	public List<?> getBaseTreeListByTemplate(String parentid, BaseTree basetree) {
		return getTemplateDao().getBaseTreeListByTemplate(parentid, basetree);
	}
	@Override
	public Map<String, String> saveMaxRevsionByRootByTemplate(DemandPoint demandPoint) {
		return getTemplateDao().saveMaxRevsionByRoot(demandPoint,"1");
	}
	
	
}
