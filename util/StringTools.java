package com.bjsasc.twc.brace.util;


import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;


public class StringTools {
	
	
	public static String returnNull(String str) {
		
		if("".equals(str) || str == null||"\"\"".equals(str))
			return null;
		else
			return str;
	}
	
	public static String returnNullStr(String str){
		if(str==null||str.equals("null")||"\"\"".equals(str)){
			return "";
		}else{
			return str;
		}
	}
	
	
	public static String returnStrFromInt(int num){
		if(num==0){
			return "";
		}else{
			return new Integer(num).toString();
		}
	}
	
	public static long returnLongFromStr(String str){
		long defVal = 0;
		if(str==null||"".equals(str)){
			defVal = 0;
		}else{
			defVal = Long.valueOf(str);
		}
		return defVal;
	}
	
	public static String returnStrFromLong(Long lon){
		if(lon==null){
			return "";
		}else{
			return lon.toString();
		}
	}
	
	public static int returnInt(String intStr){
		if(intStr==null||"".equals(intStr)){
			return 0;
		}else{
			return new Integer(intStr).intValue();
		}
	}
	
	public static String mapToJSON(Map<String, Object> map) {
		return JSONObject.fromObject(map).toString();
	}
	
	public static String strDecode(String str,String pattern){
		
		if(str==null||"".equals(str)){
			return "";
		}
		if(pattern==null||"".equals(pattern)){
			pattern = "UTF-8";
		}
		
		try {
			str =  new String(str.getBytes("ISO-8859-1"),pattern);
		} catch (Exception e) {

		}
		return str;
	}
	/**
	 * 将List转化成sql中使用的字符串集合
	 * @param idList
	 * @return
	 */
	public static String listToString(List<Map<String,Object>> idList){
		StringBuffer result = new StringBuffer();
		for(int i=0;i<=idList.size()-1;i++){
			if(i==idList.size()-1){
				result.append("'").append((String)idList.get(i).get("ID")).append("'");
			}else{
				result.append("'").append((String)idList.get(i).get("ID")).append("',");
			}
		}
		return result.toString();
	}
}