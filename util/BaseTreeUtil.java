package com.bjsasc.twc.brace.util;


import com.bjsasc.platform.spring.PlatformApplicationContext;
import com.bjsasc.twc.brace.service.BaseTreeJService;
import com.bjsasc.twc.brace.service.BaseTreeService;
import com.cascc.platform.uuidservice.UUID;

/**
 * BaseTree基础树工具类
 * @author 陈建立
 * 2015-12-18
 */
public class BaseTreeUtil {
	public static BaseTreeService getBaseTreeServiceTemplate(){
		BaseTreeService service = (BaseTreeService)PlatformApplicationContext.getBean("basetreeservicetemplate");
		return service;
	}
	public static BaseTreeJService getBaseTreeJServiceJdbc(){
		BaseTreeJService service = (BaseTreeJService)PlatformApplicationContext.getBean("basetreeservicejdbc");
		return service;
	}
	
	 /**
	 * 设置树节点字段值
	 * @param obj
	 * @param parentid
	 * @param type
	 * @param revision
	 * @return
	 */
	public static Object setTreePro(Object obj,String parentid,String type,String revision,String id,String innerid){
		//设置父节点
		obj = BeanXMLMapping.setFileValue(obj,"parentid",parentid);
		String uuid = UUID.getUID();
		if(!CommonUtilTool.emptyString(innerid)){
			uuid =innerid;
		}
		  //设置主键
		obj = BeanXMLMapping.setFileValue(obj,"innerId",uuid); 
		//设置记录创建时间
		obj = BeanXMLMapping.setFileValue(obj,"createtime",CommonUtilTool.getLongMinsDate()); 
		if(type.equalsIgnoreCase("1")){//为1时有节点版本
			if(CommonUtilTool.emptyString(revision)){//无版本号
				//设置初始版本
				obj= BeanXMLMapping.setFileValue(obj,"revision",UtilStatusDictionary.beginRevision);
			}else{//版本号加1
				int irevision =CommonUtilTool.RevisionToInt(revision)+1;
				revision = CommonUtilTool.IntToRevision(irevision);
				obj= BeanXMLMapping.setFileValue(obj,"revision",revision);
			}
			if(CommonUtilTool.emptyString(id)){
				//设置ID
				obj= BeanXMLMapping.setFileValue(obj,"id",uuid);
			}else{
				//设置ID
				obj= BeanXMLMapping.setFileValue(obj,"id",id);
			}
			
		}
		
		return obj;
	}
	
	/**
	 * 设置扩展对象的基本属性值
	 * 
	 * @param expObj
	 * @param Obj
	 * @return
	 */
	public static Object setExpObjPro(Object expObj,Object Obj,String innerid){
		String uuid = UUID.getUID();
		if(!CommonUtilTool.emptyString(innerid)){
			uuid =innerid;
		}
		//设置主键
		expObj = BeanXMLMapping.setFileValue(expObj,"innerId",uuid); 
		String fkinnerid = CommonUtilTool.DelNull(BeanXMLMapping.getFieldValueByName("innerId", Obj));
		expObj = BeanXMLMapping.setFileValue(expObj,"fkinnerid",fkinnerid);
		return expObj;
	}
	
	public static void main(String[] args) {
		/**
		BaseTree storeTree = new StoreTree();
		List<Map<String,Object>> list = getBaseTreeServiceJdbc().getBaseTreeList("1", storeTree);
		for(int i=0;i<list.size();i++){
			System.out.println(list.get(i).get("INNERID"));
			System.out.println(list.get(i).get("CLASSID"));
			System.out.println(list.get(i).get("CREATOR"));
			System.out.println(list.get(i).get("PARENTID"));
			System.out.println(list.get(i).get("PARENTIDT"));
			System.out.println(list.get(i).get("SEQUENCENUMBER"));
			System.out.println(list.get(i).get("SEQUENCENUMBERT"));
			System.out.println(list.get(i).get("HREVSION"));
			System.out.println(list.get(i).get("HREVSIONT"));
			System.out.println(list.get(i).get("HINNERID"));
			System.out.println(list.get(i).get("HINNERIDT"));
			System.out.println(list.get(i).get("ID"));
			System.out.println(list.get(i).get("IDT"));
			System.out.println("=====================");
		}
		*/
		/**
		BaseTree baseTree = new LogicTree();
		baseTree.setInnerId("eb0204c0be914c9da320d9805bd6d46b");
		baseTree.setClassId("LogicTree");
		baseTree.setName("sddd");
		baseTree.setCreator("zhangsan");
		baseTree.setSequencenumber(1);
		((LogicTree)baseTree).setNodetype("0");
		baseTree.setHasupdate(false);
		//((LogicTree)baseTree).setCreatetime(createtime);
		TreeObject treeObject = new TreeObject();//根
		treeObject.setObj(baseTree);//根
		
		
		List<TreeObject> nextList = new ArrayList<TreeObject>();//一级子
		baseTree = new LogicTree();
		baseTree.setInnerId("cdcafc7d2ce94e3fa569f8b729b7e403");
		baseTree.setClassId("LogicTree");
		baseTree.setName("sddd1");
		baseTree.setCreator("zhangsan");
		baseTree.setSequencenumber(1);
		((LogicTree)baseTree).setNodetype("0");
		baseTree.setHasupdate(false);
		List<TreeObject> nextList1 = new ArrayList<TreeObject>();
		TreeObject treeObject2 = new TreeObject();//一级子1
		treeObject2.setObj(baseTree);//一级子1
		
		baseTree = new LogicTree();
		baseTree.setInnerId("8e1e5d5105724c298832e4ba051d611d");
		baseTree.setClassId("LogicTree");
		baseTree.setName("sddd1-1111");
		baseTree.setCreator("zhangsan");
		baseTree.setSequencenumber(1);
		((LogicTree)baseTree).setNodetype("0");
		((LogicTree)baseTree).setId("12-1");
		((LogicTree)baseTree).setCreatetime(1447830005194L);
		//设置记录创建时间
	
		baseTree.setHasupdate(true);
		TreeObject treeObject3 = new TreeObject();//二级子1-1
		treeObject3.setObj(baseTree);
		nextList1.add(treeObject3);
		treeObject2.setNextList(nextList1);
		nextList.add(treeObject2);
		
		baseTree = new LogicTree();
		baseTree.setInnerId("4622c2c3724b4b4b9351d4f57013a20f");
		baseTree.setClassId("LogicTree");
		baseTree.setName("sddd2");
		baseTree.setCreator("zhangsan");
		baseTree.setSequencenumber(2);
		((LogicTree)baseTree).setNodetype("0");
		baseTree.setHasupdate(false);
		TreeObject treeObject4 = new TreeObject();
		treeObject4.setObj(baseTree);
		nextList.add(treeObject4);
		treeObject.setNextList(nextList);
		BaseTreeService service = getBaseTreeServiceTemplate();
		service.saveBaseTreeList(treeObject);
		System.out.println("===========");
		*/
		/**
		BaseTree basetree = new LogicTree();
		List<Map<String,Object>> list =  getBaseTreeServiceJdbc().getBaseTreeList("cdcafc7d2ce94e3fa569f8b729b7e403",basetree);
		for(int i=0;i<list.size();i++){
			System.out.println(list.get(i).get("INNERID"));
			System.out.println(list.get(i).get("CLASSID"));
			System.out.println(list.get(i).get("CREATOR"));
			System.out.println(list.get(i).get("PARENTID"));
			System.out.println(list.get(i).get("PARENTIDT"));
			System.out.println(list.get(i).get("SEQUENCENUMBER"));
			System.out.println(list.get(i).get("SEQUENCENUMBERT"));
			System.out.println(list.get(i).get("HREVSION"));
			System.out.println(list.get(i).get("HREVSIONT"));
			System.out.println(list.get(i).get("HINNERID"));
			System.out.println(list.get(i).get("HINNERIDT"));
			System.out.println(list.get(i).get("ID"));
			System.out.println(list.get(i).get("IDT"));
			System.out.println("=====================");
		}
		*/
		
		/**
		BaseTree baseTree = new LogicTree();
		baseTree.setInnerId("eb0204c0be914c9da320d9805bd6d46b");
		baseTree.setClassId("LogicTree");
		baseTree.setName("sddd");
		baseTree.setCreator("zhangsan");
		baseTree.setSequencenumber(1);
		((LogicTree)baseTree).setNodetype("0");
		baseTree.setHasupdate(false);
		//((LogicTree)baseTree).setCreatetime(createtime);
		TreeObject treeObject = new TreeObject();//根
		treeObject.setObj(baseTree);//根
		List<TreeObject> nextList = new ArrayList<TreeObject>();//一级子
		baseTree = new LogicTree();
		baseTree.setInnerId("cdcafc7d2ce94e3fa569f8b729b7e403");
		baseTree.setClassId("LogicTree");
		baseTree.setName("sddd1");
		baseTree.setCreator("zhangsan");
		baseTree.setSequencenumber(1);
		((LogicTree)baseTree).setNodetype("0");
		baseTree.setHasupdate(false);
		List<TreeObject> nextList1 = new ArrayList<TreeObject>();
		TreeObject treeObject2 = new TreeObject();//一级子1
		treeObject2.setObj(baseTree);//一级子1
		baseTree = new LogicTree();
		baseTree.setInnerId("8e1e5d5105724c298832e4ba051d611d");
		baseTree.setClassId("LogicTree");
		baseTree.setName("sddd1-1111");
		baseTree.setCreator("zhangsan");
		baseTree.setSequencenumber(1);
		((LogicTree)baseTree).setNodetype("0");
		((LogicTree)baseTree).setId("12-1");
		((LogicTree)baseTree).setCreatetime(1447830005194L);
		//设置记录创建时间
		baseTree.setHasupdate(true);
		TreeObject treeObject3 = new TreeObject();//二级子1-1
		treeObject3.setObj(baseTree);
		nextList1.add(treeObject3);
		treeObject2.setNextList(nextList1);
		nextList.add(treeObject2);
		baseTree = new LogicTree();
		baseTree.setInnerId("4622c2c3724b4b4b9351d4f57013a20f");
		baseTree.setClassId("LogicTree");
		baseTree.setName("sddd2");
		baseTree.setCreator("zhangsan");
		baseTree.setSequencenumber(2);
		((LogicTree)baseTree).setNodetype("0");
		baseTree.setHasupdate(false);
		TreeObject treeObject4 = new TreeObject();
		treeObject4.setObj(baseTree);
		
		baseTree = new LogicTree();
		baseTree.setClassId("LogicTree");
		baseTree.setName("sddd2-2");
		baseTree.setCreator("zhangsan");
		baseTree.setSequencenumber(1);
		((LogicTree)baseTree).setNodetype("0");
		((LogicTree)baseTree).setId("2-2");
		((LogicTree)baseTree).setCreatetime(1447830005194L);
		TreeObject treeObject5 = new TreeObject();
		treeObject5.setObj(baseTree);
		List<TreeObject> nextList2 = new ArrayList<TreeObject>();
		nextList2.add(treeObject5);
		treeObject4.setNextList(nextList2);
		
		nextList.add(treeObject4);
		treeObject.setNextList(nextList);
		BaseTreeService service = getBaseTreeServiceTemplate();
		service.saveBaseTreeList(treeObject);
		System.out.println("===========");
		
		
		
		DemandText dt = new DemandText();
		dt.setInnerId("1");
		dt.setClassId("DemandText");
		dt.setFkinnerid("222");
		String str ="测试";
		System.out.println(str.length());
		dt.setContent(str);
		BaseTreeService service3 = getBaseTreeServiceTemplate();
		service3.getTemplateDao().saveObj(dt);
		
		BaseTreeService service2 = getBaseTreeServiceJdbc();
		Table table = service2.getJdbcDao().getObjsForPage(null,"LogicTree");
		System.out.println(table.getTotalRowCount());
		*/
		
		/**
		TreeObject treeObject = FileSer.downloadTreeObject("FilesecLev6_10","32fc54834e4847c6be9cb0fa8742d100","0");
		String s2=  BeanXMLMapping.ObjectToXml(treeObject);
		System.out.println("========");
		System.out.println(s2);
		*/
		
	}
}
