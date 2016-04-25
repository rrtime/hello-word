package com.bjsasc.twc.brace.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.bjsasc.platform.filecomponent.model.PtFileStorageObj;
import com.bjsasc.platform.filecomponent.service.PtStorageService;
import com.bjsasc.platform.filecomponent.util.PtFileCptServiceProvider;
import com.bjsasc.twc.brace.data.XMLData;

/**
 * 读取配置文件工具类（xml/properties）
 * @author 陈建立
 * 2015-12-18
 */
public class ReadConfig {
	private static List<XMLData>  xmllist = null;//xml中所有一级、二级节点
	private static Map<String,String> treeTypeMap = null;//树类型配置type 为1时查询有引用节点，按照引用模式查询，为0时按照无引用节点查询
	private static Map<String,String> treeRevisionTypeMap = null;//树版本类型配置type 为1时有节点版本，为0时无节点版本
	private static Map<String,String> twc_treeWritexml_map = null;//继承BaseTree的树结构是否写入xml文件1，写入文件，0不写入文件
	private static Map<String,String> fileServerMap = null;//文件服务器配置信息
	private static String fileservid = "";//文件服务器配置id
	private static Map<String,String> xmlFileServerMap = null;//需求指标历史文件服务器配置信息
	private static String xmlfileservid = "";//需求指标历史文件服务器配置id
	private static Map<String,String> messageTypeMap= null;//消息提醒机制类型处理配置页面
	private static Map<String,String> messageNameMap=null;//消息提醒机制类型对应的名称配置
	private static Map<String,String> ContenMap = null;//树结构内容类型映射表名，如需求指标需求内容类型0字符串、1定性、2为图片；需求内容表通过配置与需求内容类型一一映射
	
	public static Map<String, String> getTreeTypeMap() {
		return treeTypeMap;
	}
	public static Map<String, String> getTreeRevisionTypeMap() {
		return treeRevisionTypeMap;
	}
	public static Map<String, String> getTwc_treeWritexml_map() {
		return twc_treeWritexml_map;
	}
	
	public static Map<String, String> getFileServerMap() {
		return fileServerMap;
	}
	
	public static Map<String, String> getXMLFileServerMap() {
		return xmlFileServerMap;
	}
	
	public static Map<String, String> getMessageTypeMap() {
		return messageTypeMap;
	}
	
	public static Map<String, String> getMessageNameMap() {
		return messageNameMap;
	}
	public static Map<String, String> getContenMap() {
		return ContenMap;
	}
	/**
	 * 读取xml中所有一级、二级节点集合
	 * @param path
	 * @return
	 */
	public static List<XMLData> readXml(String path){
		List<XMLData> list = new ArrayList<XMLData>();
		//得到DOM解析器的工厂实例得到javax.xml.parsers.DocumentBuilderFactory;类的实例就是解析器工厂
		DocumentBuilderFactory domfac=DocumentBuilderFactory.newInstance();
		try{
			//从DOM工厂获得DOM解析器,通过javax.xml.parsers.DocumentBuilderFactory实例的静态方法newDocumentBuilder()得到DOM解析器
			DocumentBuilder dombuilder=domfac.newDocumentBuilder();
			//把要解析的XML文档转化为输入流，以便DOM解析器解析它s
		    InputStream is=new FileInputStream(path);
		    //解析XML文档的输入流，得到一个Document,由XML文档的输入流得到一个org.w3c.dom.Document对象，以后的处理都是对Document对象进行的
		    Document doc=dombuilder.parse(is);
		    //得到XML文档的根节点,在DOM中只有根节点是一个org.w3c.dom.Element对象
		    Element root=doc.getDocumentElement();
		    //得到节点的子节点,这是用一个org.w3c.dom.NodeList接口来存放它所有子节点的
			NodeList nodes=root.getChildNodes();
			if(nodes!=null){
				for(int i=0;i<nodes.getLength();i++){
					Node nodelist=nodes.item(i);
					if(nodelist.getNodeType()==Node.ELEMENT_NODE){
						//轮循子节点
						XMLData xmlData = new XMLData();
						xmlData.setNodeName(CommonUtilTool.DelNull(nodelist.getNodeName()));
						if(nodelist.getFirstChild()!=null){
							Map<String,String> maplist = new HashMap<String,String>();
							boolean hasm = false;
							for(Node node=nodelist.getFirstChild();node!=null;node=node.getNextSibling()){
								if(node.getNodeType()==Node.ELEMENT_NODE){
									String	key = node.getNodeName();
									if(!CommonUtilTool.emptyString(key)){
										if(node.getFirstChild()!=null){
											String  value= CommonUtilTool.DelNull(node.getFirstChild().getNodeValue());
											maplist.put(key, value);
											hasm = true;
										}
									}
								}
							}
							if(hasm){
								xmlData.setMaplist(maplist);
							}else{
								xmlData.setNodeValue(CommonUtilTool.DelNull(nodelist.getFirstChild().getNodeValue()));
							}
						}//if(nodelist.getFirstChild()!=null)
						list.add(xmlData);
					}
				}
			}
			is.close();
		} catch(Exception e){
			e.printStackTrace();
		} 
	
		return list;
	}
	
	/**
	 * xml中所有一级、二级节点集合写入xml文件
	 * @param path
	 * @param list
	 */
	public static void WriteXml(String path,List<XMLData> list){
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbf.newDocumentBuilder();
			Document doc = docBuilder.newDocument();	
			Element rootElement = doc.createElement("config");//集合节点
			rootElement.appendChild(doc.createTextNode("\n"));
			doc.appendChild(rootElement);
			for(int i=0;i<list.size();i++){
				XMLData xmlData = list.get(i);
				String nodeName = xmlData.getNodeName();//节点名称
				String nodeValue = xmlData.getNodeValue();//节点值
				Map<String,String> maplist = xmlData.getMaplist();//节点下的集合
				Element element = doc.createElement(nodeName);
				if(!CommonUtilTool.emptyString(nodeValue)){
					element.setTextContent(nodeValue);
				}else{
					if(maplist!=null){
						Set<String> key = maplist.keySet();
						for(Iterator<String> i$ = key.iterator();i$.hasNext();){
							String strKey = (String)i$.next();
							String strValue = CommonUtilTool.DelNull(maplist.get(strKey));
							Element melement = doc.createElement(strKey);
							melement.setTextContent(strValue);
							element.appendChild(melement);
						}
						element.appendChild(doc.createTextNode("\n"));
					}
				}
				rootElement.appendChild(element);
			}
			
			/**
			 * 生成的xml写入文件
			 */
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			int lastf = path.lastIndexOf(File.separator);
			String tempPath = path.substring(0,lastf);
			CommonUtilTool.createFileByPath(tempPath);
			File file = new File(path);
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"GB2312")));
			StreamResult xmlResult = new StreamResult(out);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING,"GB2312");
			transformer.transform(new DOMSource(doc),xmlResult);
			out.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * xml中所有一级、二级节点集合更新写入xml文件
	 * @param path
	 * @param list
	 */
	public static void WriteUpdateXml(String path,List<XMLData> list){
		List<XMLData> haslist = readXml(path);
		if(CommonUtilTool.isListNotNull(haslist)){
			//如果有值，则先生成原有值
			try{
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = dbf.newDocumentBuilder();
				Document doc = docBuilder.newDocument();	
				Element rootElement = doc.createElement("config");//集合节点
				rootElement.appendChild(doc.createTextNode("\n"));
				doc.appendChild(rootElement);
				for(int i=0;i<haslist.size();i++){
					XMLData xmlData = haslist.get(i);
					String nodeName = xmlData.getNodeName();//节点名称
					String nodeValue = xmlData.getNodeValue();//节点值
					Map<String,String> maplist = xmlData.getMaplist();//节点下的集合
					boolean needupdate = false;//是否需要更新
					String upNodeValue = "";
					Map<String,String> upMap = null;
					for(int j=0;j<list.size();j++){
						XMLData upxmlData = list.get(j);
						String upNodeName = upxmlData.getNodeName();
						if(nodeName.equals(upNodeName)){
							needupdate = true;
							upNodeValue = upxmlData.getNodeValue();
							upMap = upxmlData.getMaplist();
							list.remove(j);//更新后移除
						}
					}
					
					Element element = doc.createElement(nodeName);
					if(needupdate){//更新
						if(!CommonUtilTool.emptyString(upNodeValue)){
							element.setTextContent(upNodeValue);
						}else{
							if(maplist!=null){
								Set<String> key = maplist.keySet();
								for(Iterator<String> i$ = key.iterator();i$.hasNext();){
									String strKey = (String)i$.next();
									if(upMap!=null && upMap.get(strKey)!=null){
										String strValue = upMap.get(strKey);
										Element melement = doc.createElement(strKey);
										melement.setTextContent(strValue);
										element.appendChild(melement);
										upMap.remove(strKey);
									}else{
										String strValue = CommonUtilTool.DelNull(maplist.get(strKey));
										Element melement = doc.createElement(strKey);
										melement.setTextContent(strValue);
										element.appendChild(melement);
									}
								}
								
							}
							if(upMap!=null && !upMap.isEmpty()){
								Set<String> key = upMap.keySet();
								for(Iterator<String> i$ = key.iterator();i$.hasNext();){
									String strKey = (String)i$.next();
									String strValue = CommonUtilTool.DelNull(upMap.get(strKey));
									Element melement = doc.createElement(strKey);
									melement.setTextContent(strValue);
									element.appendChild(melement);
								}
							}
							if(maplist!=null || upMap!=null){
								element.appendChild(doc.createTextNode("\n"));
							}
						}
						rootElement.appendChild(element);
					}else{
						if(!CommonUtilTool.emptyString(nodeValue)){
							element.setTextContent(nodeValue);
						}else{
							if(maplist!=null){
								Set<String> key = maplist.keySet();
								for(Iterator<String> i$ = key.iterator();i$.hasNext();){
									String strKey = (String)i$.next();
									String strValue = CommonUtilTool.DelNull(maplist.get(strKey));
									Element melement = doc.createElement(strKey);
									melement.setTextContent(strValue);
									element.appendChild(melement);
								}
								element.appendChild(doc.createTextNode("\n"));
							}
						}
						rootElement.appendChild(element);
					}
				}
				for(int i=0;i<list.size();i++){
					XMLData xmlData = list.get(i);
					String nodeName = xmlData.getNodeName();//节点名称
					String nodeValue = xmlData.getNodeValue();//节点值
					Map<String,String> maplist = xmlData.getMaplist();//节点下的集合
					Element element = doc.createElement(nodeName);
					if(!CommonUtilTool.emptyString(nodeValue)){
						element.setTextContent(nodeValue);
					}else{
						if(maplist!=null){
							Set<String> key = maplist.keySet();
							for(Iterator<String> i$ = key.iterator();i$.hasNext();){
								String strKey = (String)i$.next();
								String strValue = CommonUtilTool.DelNull(maplist.get(strKey));
								Element melement = doc.createElement(strKey);
								melement.setTextContent(strValue);
								element.appendChild(melement);
							}
							element.appendChild(doc.createTextNode("\n"));
						}
					}
					rootElement.appendChild(element);
				}
				
				/**
				 * 生成的xml写入文件
				 */
				TransformerFactory tf = TransformerFactory.newInstance();
				Transformer transformer = tf.newTransformer();
				int lastf = path.lastIndexOf(File.separator);
				String tempPath = path.substring(0,lastf);
				CommonUtilTool.createFileByPath(tempPath);
				File file = new File(path);
				PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"GB2312")));
				StreamResult xmlResult = new StreamResult(out);
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty(OutputKeys.ENCODING,"GB2312");
				transformer.transform(new DOMSource(doc),xmlResult);
				out.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			WriteXml(path,list);
		}
	}
	
	/**
	 * xml中所有一级、二级节点集合如果存在path,则更新写入xml文件，否则直接写入
	 * @param path
	 * @param list
	 */
	public static void WriteUpXml(String path,List<XMLData> list){
		File file = new File(path);
		if(file.exists()){
			WriteUpdateXml(path,list);
		}else{
			WriteXml(path,list);
		}
	}
	
	
	/**
	 * 静态代码块加载配置文件，并读取到相应集合
	 */
	static{
		xmllist = readXml(System.getProperty("AVIDM_HOME")+ File.separator+"TWCXML"+File.separator+"ADPTWCConfig.xml");
		for(int i=0;i<xmllist.size();i++){
			XMLData xmlData = xmllist.get(i);
			String xmlnodename = CommonUtilTool.DelNull(xmlData.getNodeName());
			if(xmlnodename.equalsIgnoreCase("TreeType")){//树类型配置type 为1时查询有引用节点，按照引用模式查询，为0时按照无引用节点查询
				treeTypeMap = xmlData.getMaplist();
			}else if(xmlnodename.equalsIgnoreCase("TreeRevisionType")){//树版本类型配置type 为1时有节点版本，为0时无节点版本
				treeRevisionTypeMap = xmlData.getMaplist();
			}else if(xmlnodename.equalsIgnoreCase("TreeWriteType")){//继承BaseTree的树结构是否写入xml文件1，写入文件，0不写入文件
				twc_treeWritexml_map = xmlData.getMaplist();
			}else if(xmlnodename.equalsIgnoreCase("FileServer")){//文件服务器配置
				fileservid = xmlData.getNodeValue();
			}else if(xmlnodename.equalsIgnoreCase("XMLFileServer")){//XML文件服务器配置
				xmlfileservid = xmlData.getNodeValue();
			}else if(xmlnodename.equalsIgnoreCase("MessageTypePage")){//消息提醒机制类型处理配置页面
				messageTypeMap = xmlData.getMaplist();
			}else if(xmlnodename.equalsIgnoreCase("MessageTypeName")){//消息提醒机制类型对应的名称配置
				messageNameMap = xmlData.getMaplist();
			}else if(xmlnodename.equalsIgnoreCase("ContentType")){//树结构内容类型映射表名，如需求指标需求内容类型0字符串、1定性、2为图片；需求内容表通过配置与需求内容类型一一映射
				ContenMap = xmlData.getMaplist();
			}
		}
		
		PtStorageService s = PtFileCptServiceProvider.getStorageService();
 		PtFileStorageObj storage = s.getStorageById(fileservid);
 		String ip = storage.getIp();
 		String port = storage.getPort();
 		String cellName = storage.getCellName();
 		fileServerMap = new HashMap<String,String>();
 		fileServerMap.put("fileServerIP", ip);
 		fileServerMap.put("fileServerPort", port);
 		fileServerMap.put("cellName", cellName);
 		
 		storage = s.getStorageById(xmlfileservid);
 		ip = storage.getIp();
 		port = storage.getPort();
 		cellName = storage.getCellName();
 		xmlFileServerMap = new HashMap<String,String>();
 		xmlFileServerMap.put("fileServerIP", ip);
 		xmlFileServerMap.put("fileServerPort", port);
 		xmlFileServerMap.put("cellName", cellName);
	}
	
	/**
	 * 获取树是否带引用节点标识
	 * @param obj
	 * @return
	 */
	public static String gettreeTypeMap(Object obj){
		Map<String,String>  treeTypeMap = ReadConfig.getTreeTypeMap();
		String type = null;
		if(treeTypeMap!=null && obj!=null){
			String simpleName = obj.getClass().getSimpleName();
			type = treeTypeMap.get(simpleName);
		}
		return type;
	}
	
	/**
	 * 获取树是否带版本标识
	 * @param obj
	 * @return
	 */
	public static String getTreeRevisionType(Object obj ){
		Map<String,String> treeRevisionTypeMap = ReadConfig.getTreeRevisionTypeMap();
		String type = null;
		if(treeRevisionTypeMap!=null && obj!=null){
			String simpleName = obj.getClass().getSimpleName();
			type = treeRevisionTypeMap.get(simpleName);
		}
		return type;
	}
	
	/**
	 * 获取树是否写入xml
	 * @param obj
	 * @return
	 */
	public static String getTreeWriteType(Object obj){
		Map<String,String> twc_treeWritexml_map= ReadConfig.getTwc_treeWritexml_map();
		String type = null;
		if(twc_treeWritexml_map!=null && obj!=null){
			String simpleName = obj.getClass().getSimpleName();
			type = twc_treeWritexml_map.get(simpleName);
		}
		return type;
	}
	
	/**
	 * 根据消息类型，获取消息处理url
	 * @param type
	 * @return
	 */
	public static String getMessageTypeUrl(String type){
		Map<String, String> messagetypemap = ReadConfig.getMessageTypeMap();
		String url = null;
		if(messagetypemap!=null && !CommonUtilTool.emptyString(type)){
			url = messagetypemap.get("MessageType"+type);
		}
		return url;
	}
	
	/**
	 * 根据消息类型，获取消息类型名称
	 * @param type
	 * @return
	 */
	public static String getMessageName(String type){
		Map<String, String> messagenamemap = ReadConfig.getMessageNameMap();
		String name = null;
		if(messagenamemap!=null && !CommonUtilTool.emptyString(type)){
			name = messagenamemap.get("MessageName"+type);
		}
		return name;
	}
	
	/**
	 * 根据内容类型映射表名，如需求指标树节点类型，获取内容存放表名
	 * @param contenttype
	 * @return
	 */
	public static String getContenTable(String simpleNametype){
		Map<String, String> contenMap = ReadConfig.getContenMap();
		String tableName = null;
		if(contenMap!=null && !CommonUtilTool.emptyString(simpleNametype)){
			tableName = contenMap.get(simpleNametype);
		}
		return tableName;
	}
	
	
}
