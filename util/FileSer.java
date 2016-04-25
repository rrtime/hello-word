package com.bjsasc.twc.brace.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.bjsasc.platform.fileserver.client.PtFSClient;
import com.bjsasc.platform.fileserver.client.PtFSClient.TransferProtocol;
import com.bjsasc.platform.fileserver.client.util.FilePartProgress;
import com.bjsasc.twc.brace.data.*;
import com.bjsasc.twc.brace.service.BaseTreeJService;
import com.bjsasc.twc.brace.service.BaseTreeService;
import com.cascc.platform.fileservice.FileManagerStatic;

/**
 * 文件服务，与文件服务器进行交互
 * @author 陈建立
 * 2015-12-18
 */
public class FileSer {
	/**
	 * 需求指标保存历史以xml文件形式存在
	 * 文件上传，将文件流传输到文件服务器，注意input流需要调用方关闭
	 * @param fileName  文件名称
	 * @param input 文件流
	 * @param progress 传输过程回调，可以为null
	 * @return 返回文件标识
	 */
	public static String uploadFileForXML(String fileName,InputStream input,FilePartProgress progress){
		if(ReadConfig.getXMLFileServerMap()!=null){
			String xmlfileServerIP = ReadConfig.getXMLFileServerMap().get("fileServerIP") ;//需求指标历史文件服务器ip
			String xmlfileServerPort= ReadConfig.getXMLFileServerMap().get("fileServerPort") ;//需求指标历史文件服务器端口
			String xmlcellName = ReadConfig.getXMLFileServerMap().get("cellName") ;//需求指标历史存储空间名
			PtFSClient client = new PtFSClient(xmlfileServerIP,xmlfileServerPort,TransferProtocol.http);
			try {
				//上传到文件服务器
				String fileiid = client.upload(xmlcellName,fileName,input,progress);
				//文件标识写入到数据库
				BaseTreeService service = BaseTreeUtil.getBaseTreeServiceTemplate();
				service.saveHisxmlByFileid(fileiid, xmlcellName);
				return fileiid;
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.toString());
			}
		}
		return null;
	}
	
	
	/**
	* 文件上传，将本地文件传输到文件服务器,需求指标
	* @param cellName 存储单元名称
	* @param file 文件
	* @param progress 传输过程回调，可以为null
	* @return 返回文件标识
	*/
	public static String uploadFileForXML(File file, FilePartProgress progress){
		if(ReadConfig.getXMLFileServerMap()!=null){
			String xmlfileServerIP = ReadConfig.getXMLFileServerMap().get("fileServerIP") ;//需求指标历史文件服务器ip
			String xmlfileServerPort= ReadConfig.getXMLFileServerMap().get("fileServerPort") ;//需求指标历史文件服务器端口
			String xmlcellName = ReadConfig.getXMLFileServerMap().get("cellName") ;//需求指标历史存储空间名
			PtFSClient client = new PtFSClient(xmlfileServerIP,xmlfileServerPort,TransferProtocol.http);
			try {
				//上传到文件服务器
				String fileiid = client.upload(xmlcellName,file,progress);
				//文件标识写入到数据库
				BaseTreeService service = BaseTreeUtil.getBaseTreeServiceTemplate();
				service.saveHisxmlByFileid(fileiid, xmlcellName);
				return fileiid;
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.toString());
			}
		}
		return null;
	}
	
	/**
	 * 文件上传，将文件流传输到文件服务器，注意input流需要调用方关闭
	 * @param fileName  文件名称
	 * @param input 文件流
	 * @param progress 传输过程回调，可以为null
	 * @return 返回文件标识
	 */
	public static String uploadFile(String fileName,InputStream input,FilePartProgress progress){
		if(ReadConfig.getFileServerMap()!=null){
			String fileServerIP = ReadConfig.getFileServerMap().get("fileServerIP") ;//文件服务器ip
			String fileServerPort= ReadConfig.getFileServerMap().get("fileServerPort") ;//文件服务器端口
			String cellName = ReadConfig.getFileServerMap().get("cellName") ;//存储空间名
			PtFSClient client = new PtFSClient(fileServerIP,fileServerPort,TransferProtocol.http);
			try {
				//上传到文件服务器
				String fileiid = client.upload(cellName,fileName,input,progress);
				return fileiid;
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.toString());
			}
		}
		return null;
	}
	
	/**
	* 文件上传，将本地文件传输到文件服务器
	* @param cellName 存储单元名称
	* @param file 文件
	* @param progress 传输过程回调，可以为null
	* @return 返回文件标识
	*/
	public static String uploadFile(File file, FilePartProgress progress){
		if(ReadConfig.getFileServerMap()!=null){
			String fileServerIP = ReadConfig.getFileServerMap().get("fileServerIP") ;//文件服务器ip
			String fileServerPort= ReadConfig.getFileServerMap().get("fileServerPort") ;//文件服务器端口
			String cellName = ReadConfig.getFileServerMap().get("cellName") ;//存储空间名
			PtFSClient client = new PtFSClient(fileServerIP,fileServerPort,TransferProtocol.http);
			try {
				//上传到文件服务器
				String fileiid = client.upload(cellName,file,progress);
				return fileiid;
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.toString());
			}
		}
		return null;
	}
	
	/**
	 * 根据请求将文件上传到文件服务器
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map uploadFilesToFileServer(HttpServletRequest request){
		String fileServerIP = ReadConfig.getFileServerMap().get("fileServerIP") ;//文件服务器ip
		String fileServerPort= ReadConfig.getFileServerMap().get("fileServerPort") ;//文件服务器端口
		String cellName = ReadConfig.getFileServerMap().get("cellName") ;//存储空间名
		Map map = FileManagerStatic.uploadFilesToFileServer(request, fileServerIP, fileServerPort, cellName);
		if(map!=null){
			map.put("cellName", cellName);
		}
		return map;
	}
	
	
	/**
	* 文件下载，从文件服务器上下载一个文件，返回文件输入流，需要调用方关闭该流
	* @param cellName 存储单元名称
	* @param fileId 文件标识
	* @param type 文件服务器类型 0为需求指标，其其它为默认
	* @return 文件流
	* @throws Exception
	*/
	public static InputStream download(String cellName, String fileId,String type){
		String fileServerIP = null;//文件服务器ip
		String fileServerPort = null;//文件服务器端口
		//String tcellName = null;//存储空间名
		if(!CommonUtilTool.emptyString(type) && type.equals("0")){
			if(ReadConfig.getXMLFileServerMap()!=null){
				fileServerIP = ReadConfig.getXMLFileServerMap().get("fileServerIP") ;//需求指标历史文件服务器ip
				fileServerPort= ReadConfig.getXMLFileServerMap().get("fileServerPort") ;//需求指标历史文件服务器端口
				//tcellName = ReadConfig.getXMLFileServerMap().get("cellName") ;//需求指标历史存储空间名
			}
		}else{
			if(ReadConfig.getFileServerMap()!=null){
				fileServerIP = ReadConfig.getFileServerMap().get("fileServerIP") ;//需求指标历史文件服务器ip
				fileServerPort= ReadConfig.getFileServerMap().get("fileServerPort") ;//需求指标历史文件服务器端口
				//tcellName = ReadConfig.getFileServerMap().get("cellName") ;//需求指标历史存储空间名
			}
		}
		if(!CommonUtilTool.emptyString(cellName) && !CommonUtilTool.emptyString(fileId)){
			try{
				PtFSClient client = new PtFSClient(fileServerIP,fileServerPort,TransferProtocol.http);
				InputStream input = client.download(cellName,fileId);
				return input;
			}catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.toString());
			}
		}
		return null;
	}
	
	
	/**
	* 文件下载，从文件服务器上下载一个文件，返回树对象
	* @param cellName 存储单元名称
	* @param fileId 文件标识
	* @param type 文件服务器类型 0为需求指标，其其它为默认
	* @return 文件流
	* @throws Exception
	*/
	public static TreeObject downloadTreeObject(String cellName, String fileId,String type){
		String fileServerIP = null;//文件服务器ip
		String fileServerPort = null;//文件服务器端口
		//String tcellName = null;//存储空间名
		if(!CommonUtilTool.emptyString(type) && type.equals("0")){
			if(ReadConfig.getXMLFileServerMap()!=null){
				fileServerIP = ReadConfig.getXMLFileServerMap().get("fileServerIP") ;//需求指标历史文件服务器ip
				fileServerPort= ReadConfig.getXMLFileServerMap().get("fileServerPort") ;//需求指标历史文件服务器端口
				//tcellName = ReadConfig.getXMLFileServerMap().get("cellName") ;//需求指标历史存储空间名
			}
		}else{
			if(ReadConfig.getFileServerMap()!=null){
				fileServerIP = ReadConfig.getFileServerMap().get("fileServerIP") ;//文件服务器ip
				fileServerPort= ReadConfig.getFileServerMap().get("fileServerPort") ;//文件服务器端口
				//tcellName = ReadConfig.getFileServerMap().get("cellName") ;//需求指标历史存储空间名
			}
		}
		TreeObject treeObject = null;
		if(!CommonUtilTool.emptyString(cellName) && !CommonUtilTool.emptyString(fileId)){
			InputStream input = null;
			try{
				PtFSClient client = new PtFSClient(fileServerIP,fileServerPort,TransferProtocol.http);
				input = client.download(cellName,fileId);
				//封装成树结构
				treeObject = BeanXMLMapping.XMLStrToTreeObject(input);
			}catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.toString());
			}
		}
		return treeObject;
	}
	
	
	
	/**
	* 文件下载，从文件服务器上下载一个文件，返回文件保存路径
	* @param cellName 存储单元名称
	* @param fileId 文件标识
	* @param type 文件服务器类型 0为需求指标，其其它为默认
	* @return 文件流
	* @throws Exception
	*/
	public static String download(String cellName, String fileId,String type,String path){
		String fileServerIP = null;//文件服务器ip
		String fileServerPort = null;//文件服务器端口
		//String tcellName = null;//存储空间名
		if(!CommonUtilTool.emptyString(type) && type.equals("0")){
			if(ReadConfig.getXMLFileServerMap()!=null){
				fileServerIP = ReadConfig.getXMLFileServerMap().get("fileServerIP") ;//需求指标历史文件服务器ip
				fileServerPort= ReadConfig.getXMLFileServerMap().get("fileServerPort") ;//需求指标历史文件服务器端口
				//tcellName = ReadConfig.getXMLFileServerMap().get("cellName") ;//需求指标历史存储空间名
			}
		}else{
			if(ReadConfig.getFileServerMap()!=null){
				fileServerIP = ReadConfig.getFileServerMap().get("fileServerIP") ;//需求指标历史文件服务器ip
				fileServerPort= ReadConfig.getFileServerMap().get("fileServerPort") ;//需求指标历史文件服务器端口
				//tcellName = ReadConfig.getFileServerMap().get("cellName") ;//需求指标历史存储空间名
			}
		}
		if(!CommonUtilTool.emptyString(cellName) && !CommonUtilTool.emptyString(fileId)){
			try{
				PtFSClient client = new PtFSClient(fileServerIP,fileServerPort,TransferProtocol.http);
				InputStream input = client.download(cellName,fileId);
				File file = CommonUtilTool.getFile(path);
				FileOutputStream output = new FileOutputStream(file);
				byte[] buff = new byte[16 * 1024];
				int bytesRead = 0;
				while (-1 != (bytesRead = input.read(buff, 0, 16 * 1024))) {
				output.write(buff, 0, bytesRead);
				}
				output.close();
				input.close();
				return path;
			}catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.toString());
			}
		}
		return null;
	}
	
	
	/**
	* 更新文件
	* @param cellName 存储单元名称
	* @param fileId 文件标识
	* @param file 本地文件
	* @throws Exception
	*/
	public static void updateFile(String cellName, String fileId, File file, FilePartProgress progress,String type){
		String fileServerIP = null;//文件服务器ip
		String fileServerPort = null;//文件服务器端口
		//String tcellName = null;//存储空间名
		if(!CommonUtilTool.emptyString(type) && type.equals("0")){
			if(ReadConfig.getXMLFileServerMap()!=null){
				fileServerIP = ReadConfig.getXMLFileServerMap().get("fileServerIP") ;//需求指标历史文件服务器ip
				fileServerPort= ReadConfig.getXMLFileServerMap().get("fileServerPort") ;//需求指标历史文件服务器端口
				//tcellName = ReadConfig.getXMLFileServerMap().get("cellName") ;//需求指标历史存储空间名
			}
		}else{
			if(ReadConfig.getFileServerMap()!=null){
				fileServerIP = ReadConfig.getFileServerMap().get("fileServerIP") ;//需求指标历史文件服务器ip
				fileServerPort= ReadConfig.getFileServerMap().get("fileServerPort") ;//需求指标历史文件服务器端口
				//tcellName = ReadConfig.getFileServerMap().get("cellName") ;//需求指标历史存储空间名
			}
		}
		if(!CommonUtilTool.emptyString(cellName) && !CommonUtilTool.emptyString(fileId)){
			try{
				PtFSClient client = new PtFSClient(fileServerIP,fileServerPort,TransferProtocol.http);
				client.update(cellName, fileId, file, progress);
			}catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.toString());
			}
		}
		
	}
	
	/**
	* 更新文件，带哦用结束后需关闭输入流
	* @param cellName 存储单元名称
	* @param fileId 文件标识
	* @param input 文件流
	*/
	public static void updateFile(String cellName, String fileId, String fileName, InputStream input, FilePartProgress progress,String type){
		String fileServerIP = null;//文件服务器ip
		String fileServerPort = null;//文件服务器端口
		//String tcellName = null;//存储空间名
		if(!CommonUtilTool.emptyString(type) && type.equals("0")){
			if(ReadConfig.getXMLFileServerMap()!=null){
				fileServerIP = ReadConfig.getXMLFileServerMap().get("fileServerIP") ;//需求指标历史文件服务器ip
				fileServerPort= ReadConfig.getXMLFileServerMap().get("fileServerPort") ;//需求指标历史文件服务器端口
				//tcellName = ReadConfig.getXMLFileServerMap().get("cellName") ;//需求指标历史存储空间名
			}
		}else{
			if(ReadConfig.getFileServerMap()!=null){
				fileServerIP = ReadConfig.getFileServerMap().get("fileServerIP") ;//需求指标历史文件服务器ip
				fileServerPort= ReadConfig.getFileServerMap().get("fileServerPort") ;//需求指标历史文件服务器端口
				//tcellName = ReadConfig.getFileServerMap().get("cellName") ;//需求指标历史存储空间名
			}
		}
		if(!CommonUtilTool.emptyString(cellName) && !CommonUtilTool.emptyString(fileId)){
			try{
				PtFSClient client = new PtFSClient(fileServerIP,fileServerPort,TransferProtocol.http);
				client.update(cellName, fileId,fileName, input, progress);
			}catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.toString());
			}
		}
		
	}
	
	
	/**
	* 删除文件
	* @param cellName
	* 存储单元名称
	* @param fileId
	* 文件标识
	* @return
	* @throws Exception
	*/ 
	public static boolean removeFileForXMl(String cellName, String fileId){
		if(ReadConfig.getXMLFileServerMap()!=null){
			String xmlfileServerIP = ReadConfig.getXMLFileServerMap().get("fileServerIP") ;//需求指标历史文件服务器ip
			String xmlfileServerPort= ReadConfig.getXMLFileServerMap().get("fileServerPort") ;//需求指标历史文件服务器端口
			String xmlcellName = ReadConfig.getXMLFileServerMap().get("cellName") ;//需求指标历史存储空间名
			PtFSClient client = new PtFSClient(xmlfileServerIP,xmlfileServerPort,TransferProtocol.http);
			try {
				//删除文件
				boolean hasdel = client.removeFile(xmlcellName,fileId);
				//根据文件标识删除数据库中数据
				BaseTreeJService service = BaseTreeUtil.getBaseTreeJServiceJdbc();
				service.deleteFileXmlByFileiid(fileId);
				return hasdel;
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.toString());
			}
		}
		return false;
	}
	
	
	/**
	* 删除文件
	* @param cellName
	* 存储单元名称
	* @param fileId
	* 文件标识
	* @return
	* @throws Exception
	*/ 
	public static boolean removeFile(String cellName, String fileId){
		if(ReadConfig.getFileServerMap()!=null){
			String fileServerIP = ReadConfig.getFileServerMap().get("fileServerIP") ;//文件服务器ip
			String fileServerPort= ReadConfig.getFileServerMap().get("fileServerPort") ;//文件服务器端口
			PtFSClient client = new PtFSClient(fileServerIP,fileServerPort,TransferProtocol.http);
			try {
				//删除文件
				boolean hasdel = client.removeFile(cellName,fileId);
				return hasdel;
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.toString());
			}
		}
		return false;
	}
	
	
	
	/**
	* 文件上传，将本地文件传输到文件服务器
	* @param cellName 存储单元名称
	* @param file 文件
	* @param progress 传输过程回调，可以为null
	* @return 返回文件标识
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map uploadAppendFile(File file, FilePartProgress progress){
		if(ReadConfig.getFileServerMap()!=null){
			String fileServerIP = ReadConfig.getFileServerMap().get("fileServerIP") ;//文件服务器ip
			String fileServerPort= ReadConfig.getFileServerMap().get("fileServerPort") ;//文件服务器端口
			String cellName = ReadConfig.getFileServerMap().get("cellName") ;//存储空间名
			try {
				PtFSClient client = new PtFSClient(fileServerIP,fileServerPort,TransferProtocol.http);
				String fileiid = client.createAppendFile(cellName);
				client.appendFile(cellName, fileiid, file, null);
				Map map = new HashMap();
				map.put("cellName", cellName);
				map.put("fileId", fileiid);
				return map;
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.toString());
			}
		}
		return null;
	}
	
	
	
	
	public static void main(String[] args) {
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
		treeObject5.setNextList(nextList2);
		
		nextList.add(treeObject4);
		treeObject.setNextList(nextList);
		String writetype=BeanXMLMapping.ObjectToXml(treeObject);
		InputStream input =CommonUtilTool.getStringStream(writetype);
		String iid = uploadFile("ceshi.xml",input,null);
		System.out.println(writetype);
		System.out.println("================");
		System.out.println(iid);
		*/
		InputStream input = download("FilesecLev6_10", "32fc54834e4847c6be9cb0fa8742d100","0");
		String str = CommonUtilTool.getStreamString(input);
		System.out.println(str);
		
		String path  = download("FilesecLev6_10", "32fc54834e4847c6be9cb0fa8742d100","0","D:\\testttt.xml");
		System.out.println("================"+path);
	}
	
}
