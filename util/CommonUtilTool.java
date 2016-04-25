package com.bjsasc.twc.brace.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;

/**
 * 通用数据处理类
 *@author 陈建立
 *2013-1-9
 */
public class CommonUtilTool {
	/**
	 * 输入字符串时间格式，转换成long类型的时间，Format为输入的时间格式，如："MM/dd/yyyy HH:mm:ss"
	 * @param DateFormat
	 * @param strdate
	 * @return
	 */
	public static long strtolong(String DateFormat,String strdate){
		SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);
		long longdate = 0;
		try {
			if (strdate != null && strdate != "") {
				longdate = sdf.parse(strdate).getTime() / 1000;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return longdate;
	}
	
	/**
	 * 输入long类型时间，转换成String类型的时间，Format为要转换的时间格式，如："MM/dd/yyyy HH:mm:ss"
	 * @param DateFormat
	 * @param date
	 * @return
	 */
	public static String longtostr(String DateFormat,long date){
		 SimpleDateFormat sdf= new SimpleDateFormat(DateFormat);
		 String strdate="";
		 if(date>0){
			 Date dt = new Date(date * 1000);  
			 strdate = sdf.format(dt);  
		 }
		 return strdate;
	}
	
	
	/**
	 * 输入long类型时间，转换成String类型的时间，Format为要转换的时间格式，如："MM/dd/yyyy HH:mm:ss"
	 * @param DateFormat
	 * @param date
	 * @return 如果时间为0 返回1970年1月1日8:00:00
	 */
	public static String longtostrbegin(String DateFormat,long date){
		 SimpleDateFormat sdf= new SimpleDateFormat(DateFormat);
		 String strdate="";
		 Date dt = new Date(date * 1000);  
		 strdate = sdf.format(dt); 
		 return strdate;
	}
	
	
	/**
	 * 获取现在时间
	 * @param DateFormat
	 * @return返回时间类型 DateFormat,如yyyy-MM-dd HH:mm:ss
	 */
	public static Date getNowDate(String DateFormat) {
		  Date currentTime = new Date();
		  SimpleDateFormat formatter = new SimpleDateFormat(DateFormat);
		  String dateString = formatter.format(currentTime);
		  ParsePosition pos = new ParsePosition(0);
		  Date currentTime_2 = formatter.parse(dateString, pos);
		  return currentTime_2;
	}
	
	 /**
	  * 获取现在时间 返回秒
	  * @return
	  */
	public static long getLongDate(){
		Date currentTime = new Date();
		long dateString = currentTime.getTime() / 1000;
		return dateString;
	}
	
	/**
	  * 获取现在时间 返回毫秒
	  * @return
	  */
	public static long getLongMinsDate(){
		Date currentTime = new Date();
		long dateString = currentTime.getTime();
		return dateString;
	}
	 
	 /**
	  * 获取现在时间
	  * @param DateFormat
	  * @return	返回字符串格式 DateFormat,如yyyy-MM-dd HH:mm:ss
	  */
	 public static String getStringDate(String DateFormat) {
		  Date currentTime = new Date();
		  SimpleDateFormat formatter = new SimpleDateFormat(DateFormat);
		  String dateString = formatter.format(currentTime);
		  return dateString;
	 }

	/**
	 * 根据字符串的回车符号进行分割，分割后的结果保存到List中,dataString要分割的字符串，division分隔符号
	 * @param dataString
	 * @param division
	 * @return
	 */
	public static List<String> StringToList(String dataString,String division){
		List<String> list = new ArrayList<String>();
		String str = dataString;
		int fat = 0;
		if (CommonUtilTool.emptyString(str) == false) {
			fat = str.indexOf(division);
			while (fat > 0) {
				String s = str.substring(0, fat);
				list.add(s);
				str = str.substring(fat + division.length(), str.length());
				fat = str.indexOf(division);
			}
			if (str.length() > 0) {
				list.add(str);
			}
		}
		return list;
	}
	
	/**
	 * 判断字符串是否为空
	 * @param s
	 * @return
	 */
	public static boolean emptyString(String s){
		if(s==null || s=="" || s.equals("") || s.equals("null")){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断字符串是否为空
	 * @param s
	 * @return
	 */
	public static boolean emptyString(Object s){
		if(s==null || String.valueOf(s)=="" || String.valueOf(s).equals("") || String.valueOf(s).equals("null")){
			return true;
		}
		return false;
	}
	
	/**
	 * map转json
	 * 
	 * @param map
	 * @return
	 */
	public static String mapToJSON(Map<String, ?> map) {
		return JSONObject.fromObject(map).toString();
	}
	
	/**
	 * 解密数据字符串
	 * @param str
	 * @return
	 */
	public static String decipherString(String str){
		String EncodeType = UtilStatusDictionary.encodeType;//文件及数据流编码方式
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] fileBytes = null;
		String result = "";
		try {
			fileBytes = decoder.decodeBuffer(str);
			result = new String(fileBytes,EncodeType);
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	

	/**
	 * 加密数据字符串
	 * @param str
	 * @return
	 */
	public static String encodeString(String str){
		String EncodeType = UtilStatusDictionary.encodeType;//文件及数据流编码方式
		String s = "";
		try {
			byte[] bstr = str.getBytes(EncodeType);
			s =new sun.misc.BASE64Encoder().encode(bstr);
			return s;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return s;

	}
	
	/**
	 * 读取文件，指定编码方式为GB2312
	 * @param path
	 * @return
	 */
	public static String readfile(String path){
		String EncodeType = UtilStatusDictionary.encodeType;//文件及数据流编码方式
		StringBuffer buff = new StringBuffer();;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path),EncodeType));
			String line = null;  
			while ((line = br.readLine()) != null) {  
				buff.append(line).append("\r\n");
			}  
			br.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		}  
		return buff.toString();
	}
	
	/**
	 * 根据路径创建文件夹
	 * @param path
	 */
	public static void createFileByPath(String path){
	    StringTokenizer st = new StringTokenizer(path, File.separator);
	    String path1 = st.nextToken() + "/";
	    String path2 = path1;
	    while (st.hasMoreTokens()) {
	      path1 = st.nextToken() + "/";
	      path2 = path2 + path1;
	      File inbox = new File(path2);
	      if (!(inbox.exists()))
	        inbox.mkdir();
	    }
	}
	
	
	/**
	 * 根据路径创建文件夹，后创建文件
	 * @param path
	 * @return
	 */
	public static File getFile(String path){
		//创建文件夹
		int lastf = path.lastIndexOf(File.separator);
		String tempPath = path.substring(0,lastf);
		createFileByPath(tempPath);
		File f=new File(path);
        try{
            f.createNewFile();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return f;
	}
	
	/**
	 * 向文件中写入字符串
	 * @param path
	 * @param str
	 * @param EncodeType
	 */
	public static void writeStringToFile(String path,String str,String EncodeType){
        File file = getFile(path);
		try {
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file),EncodeType),true);  
			pw.println(str);  
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//测试自用
	public static void mapprint(Map<String, ?> map){
		Set<String> key = map.keySet();
		for(Iterator<String> i$ = key.iterator();i$.hasNext();){
			   String strKey = (String)i$.next();
			   System.out.println(strKey+"="+map.get(strKey));
		}
	}
	
	/**
	 * 判断list是否为null或者空，如果不为null或空，返回true，否则返回false
	 * @param list
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isListNotNull(List list){
		if(list!=null && list.size()>0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 生成N位流水号
	 * @param serialnumber
	 * @param N
	 * @return
	 */
	public static String getSerialnumber(String serialnumber,int N){
		char zero = '0';
		if(serialnumber==null){
			char[] first = new char[N];
			for(int i=0;i<N;i++){
				first[i] = zero;
			}
			return String.valueOf(first);
		}
		long num = Long.valueOf(serialnumber)+1;
		double maxnum = Math.pow(10,N);
		if(num>=maxnum){//验证位数是否超出，超出返回null
			return null;
		}
		String newsernumber = String.valueOf(num);
		char[] LiuShuiHao = new char[N-newsernumber.length()];
		for(int i=0;i<N-newsernumber.length();i++){
			LiuShuiHao[i] = zero;
		}
		String result =String.valueOf(LiuShuiHao);
		result = result.concat(newsernumber);
		return result;
	}
	
	/**
	 * 从输入流中拷贝内容到输入流中
	 * @param is
	 * @param os
	 * @throws IOException
	 */
	public static void copyStream(InputStream is, OutputStream os) throws IOException {
        // 这个读过过程可以参阅 readToBuffer 中的注释
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(os));
        line = reader.readLine();
        while (line != null) {
            writer.println(line);
            line = reader.readLine();
        }
        writer.flush();     // 最后确定要把输出流中的东西都写出去了
                            // 这里不关闭 writer 是因为 os 是从外面传进来的
                            // 既然不是从这里打开的，也就不从这里关闭
                            // 如果关闭的 writer，封装在里面的 os 也就被关了
    }
	
	/**
	 * 拷贝文件
	 * @param inFilename
	 * @param outFilename
	 */
	public static void copyTextFile(String inFilename, String outFilename){
        // 先根据输入/输出文件生成相应的输入/输出流
		try {
			 InputStream is = new FileInputStream(inFilename);
			 OutputStream os = new FileOutputStream(outFilename);
	         copyStream(is, os);     // 用 copyStream 拷贝内容
	         is.close(); // is 是在这里打开的，所以需要关闭
	         os.close(); // os 是在这里打开的，所以需要关闭
		} catch (Exception e) {
			e.printStackTrace();
		}
       
	}
	
	/**
	 * 根据指定路径查询所有文件名称（路径+名称）
	 * @param dir
	 * @return
	 */
	public static List<String> getPathFiles(String dir){
		List<String> list = new ArrayList<String>();
		File dirFile = new File(dir);
		if(dirFile.isDirectory()){//如果传入的是目录
			File[] files = dirFile.listFiles();//获取所有的文件
			for (int i = 0; i < files.length; i++) {
				if(files[i].isFile()){//如果是文件
					String filename = files[i].toString();
					list.add(filename);
				}
			}
		}
		return list;
	}
	
	/**
	 * 根据路径名称获取文件名称带后缀
	 * @param pathname
	 * @return
	 */
	public static String getFileNameByPathName(String pathname){
		int lastindex = pathname.lastIndexOf(File.separator);
		String filename = pathname.substring(lastindex+1,pathname.length());
		return filename;
	}
	
	/**
	 * 根据路径名称获取文件名称不带后缀
	 * @param pathname
	 * @return
	 */
	public static String getFileNameByName(String pathname){
		String filename= getFileNameByPathName(pathname);
		int lastindex =  filename.lastIndexOf(".");
		if(lastindex!=-1){
			filename = filename.substring(0,lastindex);
		}
		return filename;
	}
	
	/**
	 * 检查日期格式
	 * @param sourceDate
	 * @param DateFormat
	 * @return
	 */
	 public static boolean checkDate(String sourceDate,String DateFormat){
		if (sourceDate == null) {
			return false;
		}
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat);
			dateFormat.setLenient(false);
			dateFormat.parse(sourceDate);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
     }
	 /**
	 * 追加文件：使用FileWriter
	 * 
	 * @param fileName
	 * @param content
	 */
	public static void appendMethodB(String fileName, String content) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileName, true),
					UtilStatusDictionary.encodeType));
			out.write(content + "\r\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
	}
	
	/**
	 * 判断文件是否存在
	 * 
	 * @param path
	 * @param name
	 * @return 存在true
	 */
	public static boolean existFile(String path, String name) {
		boolean exist = true;
		try {
			File file = new File(path + name);
			if (!file.exists()) {
				exist = false;
			}
		} catch (Exception e) {
			exist = false;
		}
		return exist;
	}
	
	/**
	 * url编码
	 * @param str
	 * @return
	 */
	public static String urlEncode(String str){
		String newstr = str;
		try {
			newstr =java.net.URLEncoder.encode(str,"UTF-8");
			newstr =java.net.URLEncoder.encode(newstr,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return newstr;
	}
	
	/**
	 * url解码
	 * @param str
	 * @return
	 */
	public static String urlDecode(String str){
		 String newStr=str;
		try {
			newStr = java.net.URLDecoder.decode(str,"UTF-8");
			newStr = java.net.URLDecoder.decode(newStr,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		 return newStr;
	}
	
	/**
	 * 输入double数值及指定保留位数，返回保留的doule
	 * @param dou
	 * @param sum
	 * @return
	 */
	public static double getValueForDouble(double dou,int sum){
		BigDecimal b = new BigDecimal(dou); 
		double df = b.setScale(sum, BigDecimal.ROUND_HALF_UP).doubleValue();
		return df;
	}
	
	/**
	 * 输入double数值及指定保留位数，返回保留的doule的String格式
	 * @param dou
	 * @param sum
	 * @return
	 */
	public static String getStringValueForDouble(double dou,int sum){
		double df = getValueForDouble(dou,sum);
		String str = String.valueOf(df);
		return str;
	}
	 
	/**
	 * 获取文件的更新时间，转换成long类型(秒)
	 * @param file
	 * @return
	 */
	public static long getFileUpdateTime(File file){
		long modifyDate = file.lastModified()/1000;
		return modifyDate;
	}
	
	/**
	 * 根据指定日期获取该日期所在周的周一，按照中国习惯date单位为s
	 * @param date
	 * @return
	 */
	public static String getMondayOFWeekForChina(long date){
		DateFormat sdf = DateFormat.getDateInstance();
		Calendar cd = Calendar.getInstance(Locale.CHINA);
		// 其余的行不变只加入这一行即可，设定每周的起始日。
		cd.setFirstDayOfWeek(Calendar.MONDAY); // 以周1为首日
		cd.setTimeInMillis(date*1000);// 设定时间
		cd.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		String preMonday = sdf.format(cd.getTime());
		return preMonday;
	}
	/**
	 * 根据指定日期获取该日期所在周的周一，并计算该周一距离begintime有几周
	 * @param begintime
	 * @param date
	 * @return
	 */
	public static String[] getMondayOFWeekForChina(long begintime,long date){
		DateFormat sdf = DateFormat.getDateInstance();
		Calendar cd = Calendar.getInstance(Locale.CHINA);
		// 其余的行不变只加入这一行即可，设定每周的起始日。
		cd.setFirstDayOfWeek(Calendar.MONDAY); // 以周1为首日
		cd.setTimeInMillis(date*1000);// 设定时间
		cd.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);		
		String preMonday = sdf.format(cd.getTime());
		String sumday = String.valueOf(((cd.getTime()).getTime() - begintime*1000) / (7*24 * 60 * 60 * 1000));
		String dou[] = new String[2];
		dou[0] = sumday;
		dou[1] = preMonday;
		return dou;
	}

	/**
	 * 计算两个时间之间有多少周，并把周一日期保持到list中，按照中国习,btime已经转换成天的0:点
	 * @param btime
	 * @param bendtime
	 * @param DateFormat
	 * @return
	 */
	public static List<String> getSumWeekByDate(long btime,long bendtime,String DateFormat){
		int sum = -1;
		String btstrtime =  getMondayOFWeekForChina(btime);
		long modytime = strtolong(DateFormat,btstrtime);
		int daysize = (int)(((bendtime-btime)/24/3600+1));//计算一共有多少天
		if(daysize>=0){
			String[] MondayArray = new String[daysize];
			for(int i=0;i<daysize;i++){
				long date =btime+ i*24*3600;
				String[] dou = getMondayOFWeekForChina(modytime,date);
				int index = Integer.valueOf(dou[0]);
				String element = dou[1];
				sum = index+1;
				MondayArray[index]=element;
			}
			List<String> result = new ArrayList<String>();
			for(int i=0;i<sum;i++){
				String str = MondayArray[i];
				result.add(str);
			}
			return result;
		}
		
		return null;
	}
	
	/**
	 * 获得指定日期星期日的日期中国习惯
	 * @param date
	 * @return
	 */
	public static String getSunDayWeekdayForChina(long date) {
		DateFormat sdf = DateFormat.getDateInstance();
		Calendar cd = Calendar.getInstance(Locale.CHINA);
		// 其余的行不变只加入这一行即可，设定每周的起始日。
		cd.setFirstDayOfWeek(Calendar.MONDAY); // 以周1为首日
		cd.setTimeInMillis(date*1000);// 指定时间
		cd.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		String preSunday = sdf.format(cd.getTime());
		return preSunday;
	
	}
	
	/**
	 * 根据指定格式日期，返回新的指定格式日期
	 * @param DateFormat
	 * @param strdate
	 * @param ResultDateFormat
	 * @return
	 */
	public static String DelYearForDate(String DateFormat,String strdate,String ResultDateFormat){
		long l = strtolong(DateFormat,strdate);
		String str = longtostr(ResultDateFormat, l);
		return str;
	}
	
	/**
	 * 获取指定时间月第一天
	 * @param date
	 * @param DateFormat
	 * @return
	 */
	public static String getAssignFirstDayOfMonth(long date,String DateFormat) {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);
		Calendar lastDate = Calendar.getInstance();
		lastDate.setTimeInMillis(date*1000);// 指定时间
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		str = sdf.format(lastDate.getTime());
		return str;
	}
	
	/**
	 * 获取指定时间月第18天
	 * @param date
	 * @param DateFormat
	 * @return
	 */
	public static String getAssignEighteenDayOfMonth(long date,String DateFormat) {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);
		Calendar lastDate = Calendar.getInstance();
		lastDate.setTimeInMillis(date*1000);// 指定时间
		lastDate.set(Calendar.DATE, 18);// 设为当前月的18号
		str = sdf.format(lastDate.getTime());
		return str;
	}
	
	/**
	 * 获取指定时间上月第19天
	 * @param date
	 * @param DateFormat
	 * @return
	 */
	public static String getLastMothNinteenDayOfMonth(long date,String DateFormat){
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);
		Calendar lastDate = Calendar.getInstance();	
		lastDate.setTimeInMillis(date*1000);// 指定时间	
		lastDate.set(Calendar.DATE, 19);// 设为当前月的19号
		lastDate.add(Calendar.MONTH, -1);// 减一个月，变为下月的19号
		str = sdf.format(lastDate.getTime());
		return str;
		
	}
	
	/**
	 * 计算当月最后一天,返回字符串
	 * @param date
	 * @param DateFormat
	 * @return
	 */
	public static String getDefaultDayOFMonth(long date,String DateFormat) {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);
		Calendar lastDate = Calendar.getInstance();
		lastDate.setTimeInMillis(date*1000);// 指定时间
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
		lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天
		str = sdf.format(lastDate.getTime());
		return str;
	}
	
	/**
	 * 获取指定时间的年
	 * @param date
	 * @return
	 */
	public static int getYear(long date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date*1000);// 指定时间
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 获取指定时间的月
	 * @param date
	 * @return
	 */
	public static int getMonth(long date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date*1000);// 指定时间
		return calendar.get(Calendar.MONTH) + 1;
	}
	
	/**
	 * 计算两个时间相隔的月数
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getMonthsBetweenDate(long date1,long date2){
		int sum = -1;
		if(date1>date2){
			return sum;
		}
		int year2 = getYear(date2);
		int month2 = getMonth(date2);
		int year1 = getYear(date1);
		int month1 = getMonth(date1);
		int ym = (year2-year1)*12;
		sum = ym+(month2-month1)+1;
		return sum;
	} 
	
	/**
	 * 获得指定日期是日期所在月的第几周
	 * 
	 * @return
	 */
	public static int getWeekOfMonth(long date) {
		Calendar lastDate = Calendar.getInstance();
		lastDate.setTimeInMillis(date*1000);// 指定时间
		return lastDate.get(Calendar.DAY_OF_WEEK_IN_MONTH);
	}
	
	/**
	 * 根据开始时间及月的个数，生产月的集合
	 * @param btime
	 * @param mons
	 * @return
	 */
	public static List<Map<String,String>> getCalMonth(long btime,int mons){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Calendar lastDate = Calendar.getInstance(Locale.CHINA);
		lastDate.setTimeInMillis(btime*1000);// 设定时间
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		int btyear = getYear(btime);
		int btmonth = getMonth(btime);
		Map<String,String> btmap = new HashMap<String,String>();
		btmap.put("YEAR",String.valueOf(btyear));
		btmap.put("MONTH",String.valueOf(btmonth));
		list.add(btmap);
		for(int i=1;i<mons;i++){
			lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
			long t = (lastDate.getTime()).getTime()/1000;
			int year = getYear(t);
			int mon = getMonth(t);
			Map<String,String> map = new HashMap<String,String>();
			map.put("YEAR",String.valueOf(year));
			map.put("MONTH",String.valueOf(mon));
			list.add(map);
		}
		return list;
	}
	
	
	/**
	 * java.util.Date类型转换成long类型
	 * @param dt
	 * @return
	 */
	public static long dateToLong(Date dt) {
		long lSysTime = dt.getTime() / 1000; // 得到秒数，Date类型的getTime()返回毫秒数
		return lSysTime;
	}
	
	/**
	 * url传参数时，如果参数有“+”号，则需要用特殊字符串进行转义，否则会出现空格,在服务端再转义回来
	 * 
	 * @param str
	 * @return
	 */
	public static String encodeplus(String str) {
		String newstr = "";
		if (str != null && str != "") {
			newstr = str.replaceAll("\\+", "#FFFAFA#");
		}
		return newstr;
	}

	/**
	 * 在服务端再转义encodeplus处理的字符串
	 * @param str
	 * @return
	 */
    public static String decodeplus(String str){
    	String newstr ="";
    	if(str!=null && str!=""){
    		newstr = str.replaceAll("#FFFAFA#","\\+");
    	}
    	return newstr;
    }
    
	/**
	 * 将时间格式字符串转换为时间 DateFormat
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date strToDate(String strDate, String DateFormat) {
		SimpleDateFormat formatter = new SimpleDateFormat(DateFormat);
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}
	
	/**
	 * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param dateDate
	 * @return
	 */
	public static String dateToString(Date dateDate, String DateFormat) {
		SimpleDateFormat formatter = new SimpleDateFormat(DateFormat);
		String dateString = formatter.format(dateDate);
		return dateString;
	}
	
	/**
	 * 版本的字符串（仅一位如:A、B、C）
	 * @param str
	 * @return
	 */
	public static int RevisionToInt(String str){
		int value = -1;
		if(str==null || str=="" || str.equals("") || str.length()>1){
			return value;
		}
		char c_str = str.charAt(0);
		value = c_str;
		return value;
	}
	/**
	 * 把数字版本（ASCLL）转换成字符串版本（仅一位如:A、B、C），
	 * @param value
	 * @return
	 */
	public static String IntToRevision(int value){
		String str = null;
		if(value==-1){
			return str;
		}
		char c_str = (char)value;
		str = c_str+"";
		return str;
	}
	
	/**
	 * 根据版本号进行升级
	 * @param revision
	 * @return
	 */
	public static String revisionToNext(String revision){
		String newstr = UtilStatusDictionary.beginRevision;
		if(!CommonUtilTool.emptyString(revision)){
			int strint =CommonUtilTool.RevisionToInt(revision)+1;
			newstr = CommonUtilTool.IntToRevision(strint);
		}
		return newstr;
	}
	
	/**
	 * 生成文件保存路径,按照yyyy/mm/dd/hhmissmmfilename
	 * @return
	 */
	public static String creatTimePath(String savePath,String fileName){
		Calendar calendar = Calendar.getInstance();
		Date date = new Date();
		calendar.setTime(date);
		savePath = new StringBuffer().append(savePath)
				.append(calendar.get(Calendar.YEAR))
				.append(File.separator)
				.append(calendar.get(Calendar.MONTH) + 1)
				.append(File.separator)
				.append(calendar.get(Calendar.DATE)).toString();

		String saveFileName = new StringBuffer().append(savePath)
				.append(File.separator)
				.append(calendar.get(Calendar.HOUR_OF_DAY)).append("_")
				.append(calendar.get(Calendar.MINUTE)).append("_")
				.append(calendar.get(Calendar.SECOND)).append("_")
				.append(calendar.get(Calendar.MILLISECOND))
				.append(fileName).toString();
		return saveFileName;
	}
	
	/**
	 * 生成文件保存路径,按照yyyy/mm/dd/filename
	 * @return
	 */
	public static String creatDatePath(String savePath,String fileName){
		Calendar calendar = Calendar.getInstance();
		Date date = new Date();
		calendar.setTime(date);
		savePath = new StringBuffer().append(savePath)
				.append(calendar.get(Calendar.YEAR))
				.append(File.separator)
				.append(calendar.get(Calendar.MONTH) + 1)
				.append(File.separator)
				.append(calendar.get(Calendar.DATE)).toString();

		String saveFileName = new StringBuffer().append(savePath)
				.append(File.separator)
				.append(fileName).toString();
		return saveFileName;
	}
	
	/**
	 * 正则去掉特殊字符 /*\<>?:"
	 * @param str
	 * @return
	 */
	public static String delSpecialCharacter(String str){
		if(emptyString(str)){
			return "";
		}
		String regEx="[//*\\\\<>/?:\"]";  
		Pattern   p   =   Pattern.compile(regEx);      
		Matcher   m   =   p.matcher(str);      
		return   m.replaceAll("").trim();      
	}
	
	/**
	 * 生成N位流水号
	 * @param serialnumber
	 * @param N
	 * @return
	 */
	public static String getSerialnumberone(String serialnumber,int N){
		char zero = '0';
		if(serialnumber==null){
			char[] first = new char[N];
			for(int i=0;i<N-1;i++){
				first[i] = zero;
			}
			first[N-1]='1';
			return String.valueOf(first);
		}
		long num = Long.valueOf(serialnumber)+1;
		double maxnum = Math.pow(10,N);
		if(num>=maxnum){//验证位数是否超出，超出返回null
			return null;
		}
		String newsernumber = String.valueOf(num);
		char[] LiuShuiHao = new char[N-newsernumber.length()];
		for(int i=0;i<N-newsernumber.length();i++){
			LiuShuiHao[i] = zero;
		}
		String result =String.valueOf(LiuShuiHao);
		result = result.concat(newsernumber);
		return result;
	}
	
	/**
	 * 判断字符串是否为Null，如果是则返回"",否则返回字符串
	 * @param s
	 * @return
	 */
	public static String DelNull(Object obj){
		if(null == obj){
			return "";
		}else if("null".equalsIgnoreCase(String.valueOf(obj))){
			return "";
		}else{
			return String.valueOf(obj);
		}
	}
	
	/**
	 * 把字符串强转成int
	 * @param str
	 * @return
	 */
	public static int strtoint(String str){
		if(str==null || str.length()==0){
			return 0;
		}else{
			return Integer.parseInt(str);
		}
	}
	
	/**
	 * 把字符串强转成long
	 * @param str
	 * @return
	 */
	public static long strtolong(String str){
		if(str==null || str.length()==0){
			return 0;
		}else{
			return Long.valueOf(str);
		}
	}
	
	/**
	 * 把字符串强转成long
	 * @param str
	 * @return
	 */
	public static long objecttolong(Object obj){
		if(null == obj){
			return 0;
		}else if("null".equalsIgnoreCase(String.valueOf(obj))){
			return 0;
		}else{
			return Long.valueOf(String.valueOf(obj));
		}
	}
	/**
	 * 生成颜色字符串
	 * @param str
	 * @param color
	 * @return
	 */
	public static String getColorString(String str,String color){
		return "<font color='"+color+"'>"+str+"</font>";
	}
	
	/**
	 * 合并数组
	 * @param first
	 * @param second
	 * @return
	 */
	public static <T> T[] concat(T[] first, T[] second) {
		  T[] result = Arrays.copyOf(first, first.length + second.length);
		  System.arraycopy(second, 0, result, first.length, second.length);
		  return result;
	}
	
	/**
	 * 将一个字符串转化为输入流
	 * @param sInputString 字符串
	 * @return 输入流
	 */
	public static InputStream getStringStream(String sInputString){
		if (sInputString != null && !sInputString.trim().equals("")){
			try{
				ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(sInputString.getBytes());
				return tInputStringStream;
			}catch (Exception ex){
				ex.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 将一个输入流转化为字符串
	 * @param tInputStream 输入流
	 * @return 字符串
	 */
	public static String getStreamString(InputStream tInputStream){
		if (tInputStream != null){
			try{
				BufferedReader tBufferedReader = new BufferedReader(new InputStreamReader(tInputStream));
				StringBuffer tStringBuffer = new StringBuffer();
				String sTempOneLine = new String("");
				while ((sTempOneLine = tBufferedReader.readLine()) != null){
					tStringBuffer.append(sTempOneLine);
				}
				return tStringBuffer.toString();
			}catch (Exception ex){
				ex.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * byte（字节） java.lang.Byte 
	 * char（字符） java.lang.Character 
	 * short（短整型） java.lang.Short 
	 * int（整型） java.lang.Integer 
	 * long（长整型） java.lang.Long 
	 * float（浮点型） java.lang.Float 
	 * double（双精度） java.lang.Double 
	 * boolean（布尔型） java.lang.Boolean 
	 * @param type
	 * @param val
	 * @return
	 */
	public static Object getObjectType(Object type ,String val){
		// 如果类型是String
		Object obj = "";
		String typestr = type.toString();
	    if (typestr.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
	    	return val;
	    }else if (typestr.equals("class java.lang.Integer")) { // 如果类型是Integer
	    	if (!emptyString(val)) {
	    		obj = Integer.valueOf(val);
	    	}else{
	    		return Integer.valueOf(0);
	    	}
	    }else if(typestr.equals("int")){
	    	if (!emptyString(val)) {
	    		int mobj =  Integer.valueOf(val).intValue();
	    		obj = mobj;
	    	}else{
	    		return Integer.valueOf(val).intValue();
	    	}
	    }else if (typestr.equals("class java.lang.Double")) { // 如果类型是Double
	    	if (!emptyString(val)) {
	    		obj = Double.valueOf(val);
	    	}else{
	    		return Double.valueOf(0);
	    	}
	    }else if (typestr.equals("double")) { // 如果类型是double
	    	if (!emptyString(val)) {
	    		obj = Double.valueOf(val).doubleValue();
	    	}else{
	    		return Double.valueOf(0).doubleValue();
	    	}
	    }else if (typestr.equals("class java.lang.Boolean")){ // 如果类型是Boolean 是封装类
	    	if (!emptyString(val)) {
	    		obj = Boolean.valueOf(val);
	    	}else{
	    		return  Boolean.valueOf(true);
	    	}
	    }else if (typestr.equals("boolean")) { // 如果类型是boolean 基本数据类型不一样 这里有点说名如果定义名是 isXXX的 那就全都是isXXX的
		    // 反射找不到getter的具体名
	    	if (!emptyString(val)) {
	    		obj = Boolean.valueOf(val).booleanValue();
	    	}else{
	    		return  Boolean.valueOf(true).booleanValue();
	    	}
	    }else if (typestr.equals("class java.util.Date")) {//如果类型是Date
	    	if (!emptyString(val)) {
	    		obj = CommonUtilTool.strToDate(val,UtilStatusDictionary.dateFormat);
	    	}
	    }else if (typestr.equals( "class java.lang.Short")) {//如果类型是Short
	    	if (!emptyString(val)) {
	    		obj = new Short(val);
	    	}else{
	    		return null;
	    	}
	    }else if (typestr.equals( "short")) {//如果类型是short
	    	if (!emptyString(val)) {
	    		obj = new Short(val).shortValue();
	    	}else{
	    		return null;
	    	}
	    }else if(typestr.equals( "class java.lang.Long")){
	    	if (!emptyString(val)) {
	    		obj = Long.valueOf(val);
	    	}else{
	    		return Long.valueOf(0);
	    	}
	    }else if(typestr.equals("long")){
	    	if (!emptyString(val)) {
	    		obj = Long.valueOf(val).longValue();
	    	}else{
	    		return Long.valueOf(0).longValue();
	    	}
	    }else if(typestr.equals("class java.lang.Float")){
	    	if (!emptyString(val)) {
	    		obj = Float.valueOf(val);
	    	}else{
	    		return Float.valueOf(0);
	    	}
	    }else if(typestr.equals("float")){
	    	if (!emptyString(val)) {
	    		obj = Float.valueOf(val).floatValue();
	    	}else{
	    		return Float.valueOf(0).floatValue();
	    	}
	    }
	    return obj;

	}
	
	
	/**
	 * 判断是否为基本数据类型
	 * 基本数据类型 包装类 
	 * byte（字节） java.lang.Byte 
	 * char（字符） java.lang.Character 
	 * short（短整型） java.lang.Short 
	 * int（整型） java.lang.Integer 
	 * long（长整型） java.lang.Long 
	 * float（浮点型） java.lang.Float 
	 * double（双精度） java.lang.Double 
	 * boolean（布尔型） java.lang.Boolean 
	 * @param value
	 * @return
	 */
	public static boolean isPrimitive(Object value) {
        try {
        	if(value instanceof Byte){
        		 return false;
        	}else if(value instanceof Character){
        		 return false;
        	}else if(value instanceof Short){
        		 return false;
        	}else if(value instanceof Integer){
        		 return false;
        	}else if(value instanceof Long){
        		 return false;
        	}else if(value instanceof Float){
        		 return false;
        	}else if(value instanceof Double){
        		 return false;
        	}else if(value instanceof Boolean){
        		 return false;
        	}

        } catch (Exception e) {
            return false;
        }
        return true;
    }

	/**
	 * 结果集转换为List
	 * @param rs
	 * @return
	 */
	public static List<Map<String, Object>> RestltSetToList(ResultSet rs) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		if (rs == null) {
			return result;
		}
		try {
			ResultSetMetaData meta = rs.getMetaData();
			int cols = meta.getColumnCount();// 总列数
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 1; i <= cols; i++) {
					String key = meta.getColumnName(i);
					Object value = DelNull(rs.getObject(i));
					map.put(key, value);
				}
				result.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * id拼接，连接成的字符串，需要转换成sql中能使用的集合串
	 * @param idlist
	 * @return
	 */
	public static String listChangfordb(List<String> idlist){
		String mid="";
		if(isListNotNull(idlist)){
			for(int i=0;i<idlist.size();i++){
				String id = idlist.get(i);
				mid = mid+"'"+id+"',";
			}
			mid=mid.substring(0,mid.length()-1);
		}
		return mid;
	}
	
	/**
	 * id拼接，连接成的字符串，需要转换成sql中能使用的集合串
	 * @param ids
	 * @return
	 */
	public static String arryChangefordb(String[] ids){
		String mid="";
		if(ids!=null && ids.length>0){
			for(int i=0;i<ids.length;i++){
				String id = ids[i];
				mid = mid+"'"+id+"',";
			}
			mid=mid.substring(0,mid.length()-1);
		}
		return mid;
	}
	
	public static void main(String[] args) {
		//String s = getStringDate("yyyyMM");
		//System.out.println(s);
		
//		Integer a = 0;
//		System.out.println(a.getClass());
		
	}
	
	
}
