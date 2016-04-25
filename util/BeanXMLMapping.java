package com.bjsasc.twc.brace.util;

import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.bjsasc.platform.objectmodel.managed.modelinfo.data.CommonModel;
import com.bjsasc.twc.brace.data.BaseTree;
import com.bjsasc.twc.brace.data.LogicTree;
import com.bjsasc.twc.brace.data.TreeObject;
import com.thoughtworks.xstream.XStream;
/**
 * JavaBean XML转换
 * @author 陈建立
 * 2015-12-18
 */
public class BeanXMLMapping extends XStream{
	 /**
     * 复制对象
     * @param object
     * @return
     */
    public static Object copy(Object object) {
		try{
			Class<?> class1 = object.getClass();
			Object objectCopy = class1.newInstance(); 
			//Field[] fields = class1.getDeclaredFields();
			String[][] fields = getFieldForBaseTree(object);
		    for (int i = 0; i < fields.length; i++) {
		    	String fieldName = fields[i][0];
		    	Object value = getFieldValueByName(fieldName,object);
				setFileValue(objectCopy,fieldName,value);
		    }
		    Object innerid = getFieldValueByName("innerId",object);
		    setFileValue(objectCopy,"innerId",innerid);
		    Object classid = getFieldValueByName("classId",object);
		    setFileValue(objectCopy,"classId",classid);
		    return objectCopy;
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}
    
	
    /**
     * 根据属性名设置属性值
     * @param fieldName
     * @param o
     * @return
     */
    public static Object setFileValue(Object o,String fieldName,Object value) {
        try {  
        	 String firstLetter = fieldName.substring(0, 1).toUpperCase();  
             String setter = "set" + firstLetter + fieldName.substring(1);  
             Method method = o.getClass().getMethod(setter, new Class[] {value.getClass()});  
             method.invoke(o, value); 
             return o;
        } catch (Exception e1){
        	try{
        		Class<?> clazz = o.getClass();
    			//使用符合JavaBean规范的属性访问器
    		    PropertyDescriptor pd = new PropertyDescriptor(fieldName, clazz);
    			//调用setter		
    			Method writeMethod = pd.getWriteMethod();	//setName()
    			writeMethod.invoke(o, value);
                return o;
        	}catch (Exception e) {
        		e.printStackTrace();
    			throw new RuntimeException(e.toString());
			}
        }  
    } 
	
    /**
     * 根据属性名获取属性值
     * @param fieldName
     * @param o
     * @return
     */
    public static Object getFieldValueByName(String fieldName, Object o) {
        try {  
        	 Object value = null;
        	 String firstLetter = fieldName.substring(0, 1).toUpperCase();  
             String getter = "get" + firstLetter + fieldName.substring(1);  
             Method method = o.getClass().getMethod(getter, new Class[] {});  
             value = method.invoke(o, new Object[] {});  
        	return value; 
        } catch (Exception e){
        	e.printStackTrace();
			throw new RuntimeException(e.toString());
        }  
    } 
   
    
    
    /**
     * 迭代把树节点写入到xml文件
     * @param doc
     * @param element
     */
    private static void writeNextNode(Document doc,Element lastElement,List<TreeObject> nextList,int index,String[][] mfields){
    	if(CommonUtilTool.isListNotNull(nextList)){
    		for(int i=0;i<nextList.size();i++){
    			TreeObject treeObject = nextList.get(i);
    			Object obj = treeObject.getObj();//节点对象
    			Class<?> thisclass = obj.getClass();
    			String objname = thisclass.getName();
    			List<TreeObject> tempnextlist=treeObject.getNextList();//子集
    			/**
    			List<Map<String,Object>> filedvaluelist = getFiledsInfo(obj);
				for(int j=0;j<filedvaluelist.size();j++){
					Map<String,Object> map = filedvaluelist.get(j);
					String name = CommonUtilTool.DelNull(map.get("name"));
					String value = CommonUtilTool.DelNull(map.get("value"));
					element.setAttribute(name,CommonUtilTool.DelNull(value));
				}
				*/
    			Element element = doc.createElement(objname+index);
    			for(int j=0;j<mfields.length;j++){
    				String name = mfields[j][0];
    				String value = CommonUtilTool.DelNull(getFieldValueByName(name,obj));
    				element.setAttribute(name,value);
    			}
				element.appendChild(doc.createTextNode("\n"));
				if(CommonUtilTool.isListNotNull(tempnextlist)){
					writeNextNode(doc,element,tempnextlist,index+1,mfields);
				}
				lastElement.appendChild(element);
    		}
    	}
    }
    
    
    /**
     * 默认含innerid,classid
     * 仅支持继承BaseTree的树，或者继承CommonModel自定义的树结构
     * @param obj
     * @return
     */
    public static String[][] getFieldForBaseTree(Object obj){
    	String[][] mfields = null;
    	if(obj!=null){
    		Field[] fields = null;
    		Class<?> thisclass = obj.getClass();
			String objname = thisclass.getName();
			boolean hasup = true;
			if(objname.equals(BaseTree.class.getName())){//如果为BaseTree
				  fields = thisclass.getDeclaredFields();
			}else{
				 //取得父类名称
	 	        Class<?> superclass = thisclass.getSuperclass();
	 	        String superclassname = superclass.getName();
	 	        if(superclassname.equals(BaseTree.class.getName())){//继承BaseTree
	 	        	Field[] basefields = superclass.getDeclaredFields();
	 	        	Field[] thisfields = thisclass.getDeclaredFields();
	 	        	fields =CommonUtilTool.concat(basefields,thisfields);
	 	        }else if(superclassname.equals(CommonModel.class.getName())){//继承 CommonModel
	 	        	 fields = thisclass.getDeclaredFields();
	 	        	 hasup = false;
	 	        }else{//不满足要求返回空
	 	        	return null;
	 	        }
			}
			if(fields!=null){
				if(hasup){
					List<Map<String,String>> templist = new ArrayList<Map<String,String>>();
					for(int i=0;i<fields.length;i++){
						if(!fields[i].getName().equalsIgnoreCase("hasupdate")){
							String mod = Modifier.toString(fields[i].getModifiers());
			        		if(!mod.contains("static")){//静态变量
			        			Map<String,String> map =  new HashMap<String,String>();
			        			map.put("0", fields[i].getName());
			        			map.put("1", fields[i].getType().toString());
			        			templist.add(map);
			        		}
						}
					}
					mfields = new String[templist.size()+2][2];
					for(int i=0;i<templist.size();i++){
						Map<String,String> map =  templist.get(i);
						mfields[i][0] = map.get("0");
						mfields[i][1] = map.get("1");
						
					}
					mfields[templist.size()][0] = "innerId";
					mfields[templist.size()][1] = "class java.lang.String";
					mfields[templist.size()+1][0] = "classId";
					mfields[templist.size()+1][1] = "class java.lang.String";
				}else{
					List<Map<String,String>> templist = new ArrayList<Map<String,String>>();
					for(int i=0;i<fields.length;i++){
						String mod = Modifier.toString(fields[i].getModifiers());
		        		if(!mod.contains("static")){//静态变量
		        			Map<String,String> map =  new HashMap<String,String>();
		        			map.put("0", fields[i].getName());
		        			map.put("1", fields[i].getType().toString());
		        			templist.add(map);
		        		}
					}
					mfields = new String[templist.size()+2][2];
					for(int i=0;i<templist.size();i++){
						Map<String,String> map =  templist.get(i);
						mfields[i][0] = map.get("0");
						mfields[i][1] = map.get("1");
						
					}
					mfields[templist.size()][0] = "innerId";
					mfields[templist.size()][1] = "class java.lang.String";
					mfields[templist.size()+1][0] = "classId";
					mfields[templist.size()+1][1] = "class java.lang.String";
				}
			}
    	}
    	return mfields;
    }
    
    /**
     * 获取TreeObject的字段集合
     * 默认含innerid,classid
     * 仅支持继承BaseTree的树，或者继承CommonModel自定义的树结构
     * @param treeObject
     * @return
     */
    public static String[][] getFieldForTreeObject(TreeObject treeObject){
    	String[][] mfields = null;
    	if(treeObject!=null){
			Object obj = treeObject.getObj();
			mfields = getFieldForBaseTree(obj);
    	}
    	return mfields;
    }
	
	/**
	 * 把树对象写入xml文件，返回xml字符串
	 * 树对象仅支持继承BaseTree的树，或者继承CommonModel自定义的树结构
	 * xml文件中默认含innerid,classid
	 * 其余属性为树节点自身属性及继承的BaseTree属性
	 * 树结构属性要含get方法
	 * @param treeObject根节点集合
	 * @return
	 */
	public static String ObjectToXml(TreeObject treeObject){
		String result = "";
		String[][] mfields = getFieldForTreeObject(treeObject);
		if(mfields!=null){
			Object obj = treeObject.getObj();//根节点对象
			Class<?> thisclass = obj.getClass();
			String objname = thisclass.getName();
			try {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = dbf.newDocumentBuilder();
				Document doc = docBuilder.newDocument();	
				Element treeObjectElement = doc.createElement("TreeObject");//集合节点
				treeObjectElement.appendChild(doc.createTextNode("\n"));
				doc.appendChild(treeObjectElement);
				//集合下挂根节点
				int index = 0;
				Element rootElement = doc.createElement(objname);
				/**
				List<Map<String,Object>> filedvaluelist = getFiledsInfo(obj);
				for(int j=0;j<filedvaluelist.size();j++){
					Map<String,Object> map = filedvaluelist.get(j);
					String name = CommonUtilTool.DelNull(map.get("name"));
					String value = CommonUtilTool.DelNull(map.get("value"));
					rootElement.setAttribute(name,CommonUtilTool.DelNull(value));
				}
				*/
				for(int j=0;j<mfields.length;j++){
    				String name = mfields[j][0];
    				String value = CommonUtilTool.DelNull(getFieldValueByName(name,obj));
    				rootElement.setAttribute(name,value);
    			}
				rootElement.appendChild(doc.createTextNode("\n"));
				treeObjectElement.appendChild(rootElement);
				List<TreeObject> nextList=treeObject.getNextList();//子集
				writeNextNode(doc,rootElement,nextList,index+1,mfields);
				/**
				 * 把xml生成字符串
				 */
				TransformerFactory tf = TransformerFactory.newInstance();//开始把Document映射到文件
				Transformer transformer = tf.newTransformer();
				StringWriter writerStr = new StringWriter();
				Result resultStr = new StreamResult(writerStr);
				transformer.transform(new DOMSource(doc),resultStr);
				result = writerStr.getBuffer().toString();
			}catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.toString());
			}
		}
		return result;
	}
	
	/**
	 * 迭代把对象下的 BeanXMlMapping写入xml文件中的树结构XML文件字符串转集合换成树对象
	 * @return
	 */
	private static List<TreeObject> getNextTreeObjectByNextNodes(String[][] mFileds,Node lastnode,Class<?> thisclass){
		List<TreeObject> list = new ArrayList<TreeObject>();
		try{
			for(Node node=lastnode.getFirstChild();node!=null;node=node.getNextSibling()){
				if(node.getNodeType()==Node.ELEMENT_NODE){
					TreeObject treeObject = new TreeObject();
					Object treeobj = thisclass.newInstance();//获取节点对象
					NamedNodeMap map = node.getAttributes();//获取属性集合
					for(int j=0;j<mFileds.length;j++){
						String filedName = mFileds[j][0];
						String filedType = mFileds[j][1];
						String nodevalue = map.getNamedItem(filedName).getNodeValue();
						Object ondeval = CommonUtilTool.getObjectType(filedType, nodevalue);
						treeobj = setFileValue(treeobj,filedName,ondeval);
					}
					treeObject.setObj(treeobj);
					List<TreeObject> nextList = getNextTreeObjectByNextNodes(mFileds,node,thisclass);
					treeObject.setNextList(nextList);
					list.add(treeObject);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			 throw new RuntimeException(e.toString());
		}
		return list;
	}
	
	/**
	 * BeanXMlMapping写入xml文件中的树结构XML文件字符串转换成树对象
	 * @param inputstr
	 * @return
	 */
	public static TreeObject XMLStrToTreeObject(String inputstr){
		TreeObject treeObject = null;
		if(!CommonUtilTool.emptyString(inputstr) ){
			Document doc= getDocument(inputstr);
			if(doc==null){
				throw new RuntimeException("非法字符串");
			}
			
			Element root=doc.getDocumentElement();
			if(root!=null && !CommonUtilTool.emptyString(root.getNodeName()) && root.getNodeName().equalsIgnoreCase("TreeObject")){
				NodeList roottreenodes=root.getChildNodes();//一层树的根节点
				if(roottreenodes!=null && roottreenodes.getLength()>0){//如果跟节点不为空则解析
					try{
						treeObject = new TreeObject();
						int hasrootcount = 0;
						for(int i=0;i<roottreenodes.getLength();i++){
							Node rootnode=roottreenodes.item(i);
							if(rootnode.getNodeType()==Node.ELEMENT_NODE){//如果为节点对象
								hasrootcount++;
							}
						}
						if(hasrootcount!=1){
							throw new RuntimeException("该文件中根节点不符合要求");
						}
						for(int i=0;i<roottreenodes.getLength();i++){
							Node rootnode=roottreenodes.item(i);
							if(rootnode.getNodeType()==Node.ELEMENT_NODE){//如果为节点对象
								String treeObjectStr =rootnode.getNodeName();
								Class<?> thisclass = Class.forName(treeObjectStr);
								Object treeobj = thisclass.newInstance();//获取根节点对象
								String[][] mFileds = getFieldForBaseTree(treeobj);
								NamedNodeMap map = rootnode.getAttributes();//获取属性集合
								for(int j=0;j<mFileds.length;j++){
									String filedName = mFileds[j][0];
									String filedType = mFileds[j][1];
									String nodevalue = map.getNamedItem(filedName).getNodeValue();
									Object nodeval = CommonUtilTool.getObjectType(filedType, nodevalue);
									treeobj = setFileValue(treeobj,filedName,nodeval);
								}
								treeObject.setObj(treeobj);
								List<TreeObject> nextList = getNextTreeObjectByNextNodes(mFileds,rootnode,thisclass);
								treeObject.setNextList(nextList);
							}

						}
					}catch (Exception e) {
						 e.printStackTrace();
						 throw new RuntimeException(e.toString());
					}
				}else{
					throw new RuntimeException("该文件中根节点不符合要求");
				}
			}else{
				throw new RuntimeException("该文件不符合转换要求");
			}
		}
		return treeObject;
	}
	
	
	
	/**
	 * BeanXMlMapping写入xml文件中的树结构XML文件字符串转换成树对象
	 * @param inputstr
	 * @return
	 */
	public static TreeObject XMLStrToTreeObject(InputStream input){
		TreeObject treeObject = null;
		if(input!=null ){
			Document doc= null;
			try{
				//得到DOM解析器的工厂实例得到javax.xml.parsers.DocumentBuilderFactory;类的实例就是解析器工厂
				DocumentBuilderFactory domfac=DocumentBuilderFactory.newInstance();
				//从DOM工厂获得DOM解析器,通过javax.xml.parsers.DocumentBuilderFactory实例的静态方法newDocumentBuilder()得到DOM解析器
				DocumentBuilder dombuilder=domfac.newDocumentBuilder();
				doc= dombuilder.parse(input);
			}catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("非法字符串");
			}
			if(doc==null){
				throw new RuntimeException("非法字符串");
			}
			Element root=doc.getDocumentElement();
			if(root!=null && !CommonUtilTool.emptyString(root.getNodeName()) && root.getNodeName().equalsIgnoreCase("TreeObject")){
				NodeList roottreenodes=root.getChildNodes();//一层树的根节点
				if(roottreenodes!=null && roottreenodes.getLength()>0){//如果根节点不为空则解析
					try{
						treeObject = new TreeObject();
						int hasrootcount = 0;
						for(int i=0;i<roottreenodes.getLength();i++){
							Node rootnode=roottreenodes.item(i);
							if(rootnode.getNodeType()==Node.ELEMENT_NODE){//如果为节点对象
								hasrootcount++;
							}
						}
						if(hasrootcount!=1){
							throw new RuntimeException("该文件中根节点不符合要求");
						}
						for(int i=0;i<roottreenodes.getLength();i++){
							Node rootnode=roottreenodes.item(i);
							if(rootnode.getNodeType()==Node.ELEMENT_NODE){//如果为节点对象
								String treeObjectStr =rootnode.getNodeName();
								Class<?> thisclass = Class.forName(treeObjectStr);
								Object treeobj = thisclass.newInstance();//获取根节点对象
								String[][] mFileds = getFieldForBaseTree(treeobj);
								NamedNodeMap map = rootnode.getAttributes();//获取属性集合
								for(int j=0;j<mFileds.length;j++){
									String filedName = mFileds[j][0];
									String filedType = mFileds[j][1];
									String nodevalue = map.getNamedItem(filedName).getNodeValue();
									Object nodeval = CommonUtilTool.getObjectType(filedType, nodevalue);
									treeobj = setFileValue(treeobj,filedName,nodeval);
								}
								treeObject.setObj(treeobj);
								List<TreeObject> nextList = getNextTreeObjectByNextNodes(mFileds,rootnode,thisclass);
								treeObject.setNextList(nextList);
							}
						}
					}catch (Exception e) {
						 e.printStackTrace();
						 throw new RuntimeException(e.toString());
					}
				}else{
					throw new RuntimeException("该文件中根节点不符合要求");
				}
				
			}else{
				throw new RuntimeException("该文件不符合转换要求");
			}
		}
		return treeObject;
	}
	
	 /**
     * 将字符串的xml转换成org.w3c.dom.Document对象
     * @param xml
     * @return
     */
    public static Document getDocument(String xml) {
        Document document = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputStream is = new ByteArrayInputStream(xml.getBytes());
            document = db.parse(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return document;
    }
    /**
     * 将org.w3c.dom.Document对象写入到指定文件
     * @param doc
     * @param fileName
     * @throws Exception
     */
    public static void outputXml(Document doc, String fileName) {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(doc);
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");//增加换行缩进，但此时缩进默认为0  
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");//设置缩进为2
            PrintWriter pw = new PrintWriter(( new OutputStreamWriter(  new FileOutputStream(fileName), "UTF-8")));
            StreamResult result = new StreamResult(pw);
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    } 

	public static void main(String[] args) {
		BaseTree baseTree = new LogicTree();
		baseTree.setInnerId("daf");
		baseTree.setClassId("BaseTree");
		baseTree.setName("sddd");
		baseTree.setCreator("zhangsan");
		TreeObject treeObject = new TreeObject();//根
		treeObject.setObj(baseTree);//根
		
		
		List<TreeObject> nextList = new ArrayList<TreeObject>();//一级子
		baseTree = new LogicTree();
		baseTree.setInnerId("daf1");
		baseTree.setClassId("BaseTree");
		baseTree.setName("sddd1");
		baseTree.setCreator("zhangsan");
		//
		List<TreeObject> nextList1 = new ArrayList<TreeObject>();
		TreeObject treeObject2 = new TreeObject();//一级子1
		treeObject2.setObj(baseTree);//一级子1
		
		baseTree = new LogicTree();
		baseTree.setInnerId("daf1-1");
		baseTree.setClassId("BaseTree");
		baseTree.setName("sddd1-1");
		baseTree.setCreator("zhangsan");
		TreeObject treeObject3 = new TreeObject();//二级子1-1
		treeObject3.setObj(baseTree);
		nextList1.add(treeObject3);
		treeObject2.setNextList(nextList1);
		nextList.add(treeObject2);
		
		baseTree = new LogicTree();
		baseTree.setInnerId("daf2");
		baseTree.setClassId("BaseTree");
		baseTree.setName("sddd2");
		baseTree.setCreator("zhangsan");
		TreeObject treeObject4 = new TreeObject();
		treeObject4.setObj(baseTree);
		nextList.add(treeObject4);
		treeObject.setNextList(nextList);
		String str =  ObjectToXml(treeObject);
		System.out.println(str);
		Document doc= getDocument(str);
		System.out.println(doc);
		TreeObject t = XMLStrToTreeObject(str);
		String s2=  ObjectToXml(t);
		System.out.println("========");
		System.out.println(s2);
		
	}
}
