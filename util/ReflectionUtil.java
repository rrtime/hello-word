package com.bjsasc.twc.brace.util;


import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Timestamp;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.bjsasc.platform.objectmodel.business.persist.CommonObject;
import com.bjsasc.platform.objectmodel.business.persist.ObjectReference;
import com.bjsasc.platform.objectmodel.business.persist.PTFactor;
import com.bjsasc.platform.objectmodel.managed.constants.DataType;
import com.bjsasc.platform.objectmodel.managed.constants.InfoType;
import com.bjsasc.platform.objectmodel.managed.external.data.ClassAttrInfoDef;
import com.bjsasc.platform.objectmodel.managed.external.data.ClassInfoDef;
import com.bjsasc.platform.objectmodel.managed.external.util.ModelInfoUtil;
import com.bjsasc.platform.objectmodel.util.AssertUtil;

/**
 * 用于处理JAVA反射相关内容的工具类
 * 
 * @author wuhj
 * 
 */
@SuppressWarnings("rawtypes")
public class ReflectionUtil {

	/**
	 * 缓存属性集合
	 */
	private static Map<Class, List<Field>> fieldMap = new HashMap<Class, List<Field>>();
	/**
	 * 缓存方法集合
	 */
	private static Map<Class, List<Method>> methodMap = new HashMap<Class, List<Method>>();

	/**
	 * 获取扩展属性并填充值
	 * 
	 * @param map
	 *            数据Map
	 * @param classId
	 *            类标识
	 * @return 填充好数据的扩展属性Map
	 * @throws UnsupportedEncodingException
	 */
	public static Map<String, Object> fillExtFields(Map<String, String> map,
			String classId) throws RuntimeException,
			UnsupportedEncodingException {
		Map<String, Object> attrMap = new HashMap<String, Object>();

		List<ClassAttrInfoDef> classAttrDefList = ModelInfoUtil.getService()
				.getExtClassAttrInfos(classId);
		for (ClassAttrInfoDef cad : classAttrDefList) {
			String attrId = cad.getAttrDispId();
			String attrValue = map.get(attrId.toUpperCase());
			DataType dataType = cad.getDataType();
			if ((attrValue != null && !attrValue.equals(""))
					&& dataType != null) {
				Object val = convertOmType(dataType, attrValue);

				// Map<String, Object> cfg =
				// DynamicUiConfigPool.getFormFieldConfig(classId, attrId);
				// if (cfg != null &&
				// "symbol".equalsIgnoreCase(DynamicUiUtil.getParameter(cfg,
				// "style"))) {
				// val = SymbolUtil.html2Symbol((String)val);
				// }
				attrMap.put(cad.getAttrId(), val);
			}
		}
		return attrMap;
	}

	/**
	 * 获取内部属性并填充值
	 * 
	 * @param map
	 *            数据Map
	 * @param classId
	 *            类标识
	 * @return 填充好数据的基本属性Map
	 * @throws UnsupportedEncodingException
	 */
	public static Map<String, Object> fillInnerFields(Map<String, String> map,
			String classId) throws RuntimeException,
			UnsupportedEncodingException {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put("innerId", map.get("INNERID"));
		attrMap.put("classId", map.get("CLASSID"));
		return attrMap;
	}

	/**
	 * 获取基本属性并填充值
	 * 
	 * @param map
	 *            数据Map
	 * @param classId
	 *            类标识
	 * @return 填充好数据的基本属性Map
	 * @throws UnsupportedEncodingException
	 */
	public static Map<String, Object> fillBaseFields(Map<String, String> map,
			String classId) throws RuntimeException,
			UnsupportedEncodingException {
		Map<String, Object> attrMap = new HashMap<String, Object>();

		List<ClassAttrInfoDef> classAttrDefList = ModelInfoUtil.getService()
				.getClassAttrInfos(classId);
		for (ClassAttrInfoDef cad : classAttrDefList) {
			if (cad.getInfoType() == InfoType.BASIC) {
				String attrId = cad.getAttrDispId();
				String attrValue = map.get(attrId.toUpperCase()) == null ? ""
						: map.get(attrId.toUpperCase());
				DataType dataType = cad.getDataType();
				if ((attrValue != null && !attrValue.equals(""))
						&& dataType != null && dataType != DataType.REFERENCE) {

					Object val = convertOmType(dataType, attrValue);
					attrMap.put(attrId, val);

				}
			}
		}
		return attrMap;
	}

	/**
	 * 根据对象建模类型转换数据类型
	 * 
	 * @param dataType
	 *            数据类型（对象建模中的类型）
	 * @param attrValue
	 *            属性值
	 * @return 类型转换后的值
	 * @throws RuntimeException
	 */
	public static Object convertOmType(DataType dataType, String attrValue)
			throws RuntimeException {

		switch (dataType) {
		case INT:
			if (!attrValue.equals(" ")) {
				return Integer.parseInt(attrValue);
			} else {
				return 0;
			}
		case DOUBLE:
			if (!attrValue.equals(" ")) {
				return Double.parseDouble(attrValue);
			} else {
				return 0;
			}
			// OTHER　代表 TIMESTAMP
		case OTHER:
			if (!attrValue.equals(" ")) {
				return new Timestamp(Long.parseLong(attrValue));
			}else{
				return new Timestamp(0);
			}
		default:
			return attrValue;
		}
	}

	/**
	 * 把request.getParameterMap()转换为标准Map&lt;String, String&gt;
	 * 
	 * @param requestMap
	 *            request.getParameterMap(),类型为Map&lt;String, String[]&gt;
	 * @return 转换后的数据Map
	 */
	public static Map<String, String> convertRequestMap(
			Map<String, String[]> requestMap) {
		Map<String, String> map = new HashMap<String, String>();
		for (String key : requestMap.keySet()) {
			System.out.println(key);
			String[] values = requestMap.get(key);
			if (values != null && values.length > 0) {
				map.put(key, values[0].equals("") ? " " : values[0]);
			}
		}
		return map;
	}

	/**
	 * 根据classId填充属性值，从request中获取具体数据
	 * 
	 * @param classId
	 *            类标识
	 * @param request
	 * @return
	 * @throws IllegalArgumentException
	 * @throws UnsupportedEncodingException
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws RuntimeException
	 */
	public static Object fillObjectFromRequset(String classId,
			HttpServletRequest request) throws IllegalArgumentException,
			UnsupportedEncodingException, ClassNotFoundException,
			NoSuchMethodException, InstantiationException,
			IllegalAccessException, InvocationTargetException, RuntimeException {
		AssertUtil.assertNull(classId);
		// 把request.getParameterMap()转换为标准Map<String, String>
		Map<String, String> map = ReflectionUtil.convertRequestMap(request
				.getParameterMap());
		if (null == map.get("CLASSID") || "".equals(map.get("CLASSID"))) {
			map.put("CLASSID", classId);
		}
		// 处理表单域带的类前缀
		// Map<String, String> data = new HashMap<String, String>();
		// for (String key : map.keySet()) {
		// if (key.startsWith(classId.toUpperCase() + "_")) {
		// data.put(key.replaceFirst(classId.toUpperCase() + "_", ""),
		// map.get(key));
		// }
		// }

		// 根据classId自动创建对象实例，并自动从map中获取属性值并填充属性值
		Object obj = ReflectionUtil.fillObject(classId, map);
		return obj;
	}

	/**
	 * 根据classId填充属性值，从request中获取具体数据
	 * 
	 * @param classId
	 *            类标识
	 * @param request
	 * @return
	 * @throws IllegalArgumentException
	 * @throws UnsupportedEncodingException
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws RuntimeException
	 */
	@SuppressWarnings("unchecked")
	public static Object fillObjectFromRequset(CommonObject ptObject,
			HttpServletRequest request) throws IllegalArgumentException,
			UnsupportedEncodingException, ClassNotFoundException,
			NoSuchMethodException, InstantiationException,
			IllegalAccessException, InvocationTargetException, RuntimeException {
		// 把request.getParameterMap()转换为标准Map<String, String>
		Map<String, String> map = ReflectionUtil.convertRequestMap(request.getParameterMap());
		if (null == map.get("CLASSID") || "".equals(map.get("CLASSID"))) {
			map.put("CLASSID", ptObject.getClassId());
		}
		// 根据ptObject对象实例，自动从map中获取属性值并填充属性值
		Object obj = ReflectionUtil.fillObject(ptObject, map);
		return obj;
	}

	/**
	 * 根据classId填充属性值，从给定map获取具体数据。一般用于新建对象时使用。
	 * 
	 * @param classId
	 *            类标识
	 * @param map
	 *            数据Map。 <b>从request获取的map需要使用<font
	 *            color="#ff0000">convertRequestMap</font>方法处理。 如<font
	 *            color="#ff0000"
	 *            >convertRequestMap(request.getParameterMap())</font></b>
	 * @return 填充好属性值的对象
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws RuntimeException
	 * @throws UnsupportedEncodingException
	 * @throws IllegalArgumentException
	 */
	public static Object fillObject(String classId, Map<String, String> map)
			throws ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException, IllegalArgumentException,
			UnsupportedEncodingException, RuntimeException {
		AssertUtil.assertNull(classId);

		ClassInfoDef classDef = ModelInfoUtil.getService()
				.getClassInfo(classId);
		String className = classDef.getTargetClass();

		Class clazz = Class.forName(className);// 加载类
		Constructor cons = clazz.getConstructor();// 构造方法
		Object obj = cons.newInstance();// 创建实例

		List<Method> methodList = getClassMethods(clazz);

		Map<String, Object> baseAttrs = fillBaseFields(map, classId);
		baseAttrs.putAll(fillInnerFields(map, classId));
		for (String key : baseAttrs.keySet()) {
			Object val = baseAttrs.get(key);
			if (val != null) {
				Method method = getSetterByField(methodList, key);
				if (method != null) {
//					 System.out.println(key+"***"+val);
					method.invoke(obj, val);
				}
			}
		}
		// 以下用于处理ObjectReference类型的对应set方法
		/*
		 * for (Method method : methodList) { String methodName =
		 * method.getName(); if (methodName.startsWith("set")) { Class<?>[]
		 * parameterTypes = method.getParameterTypes(); if
		 * (parameterTypes.length > 0) { Class cls = parameterTypes[0]; if
		 * (PTFactor.class.isAssignableFrom(cls)) { String param =
		 * methodName.substring(3, methodName.length()).toUpperCase(); String
		 * innerId = map.get(param); if (innerId == null || innerId.equals(""))
		 * { continue; } Object refObj = getObject(cls, innerId); if (refObj !=
		 * null) { method.invoke(obj, refObj); } } } } }
		 */
		Method method = clazz.getMethod("setAttrContainer", Map.class);
		method.invoke(obj, fillExtFields(map, classId));
		return obj;
	}

	/**
	 * 根据现有ptObject更新属性值，从给定map获取具体数据。一般用于更新对象时使用。
	 * 
	 * @param ptObject对象
	 * @param map
	 *            数据Map。 <b>从request获取的map需要使用<font
	 *            color="#ff0000">convertRequestMap</font>方法处理。 如<font
	 *            color="#ff0000"
	 *            >convertRequestMap(request.getParameterMap())</font></b>
	 * @return 填充好属性值的对象
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws RuntimeException
	 * @throws UnsupportedEncodingException
	 * @throws IllegalArgumentException
	 */
	public static Object fillObject(CommonObject ptObject, Map<String, String> map)
			throws ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException, IllegalArgumentException,
			UnsupportedEncodingException, RuntimeException {

		List<Method> methodList = getClassMethods(ptObject.getClass());

		Map<String, Object> baseAttrs = fillBaseFields(map,
				ptObject.getClassId());
		baseAttrs.putAll(fillInnerFields(map, ptObject.getClassId()));
		for (String key : baseAttrs.keySet()) {
			Object val = baseAttrs.get(key);
			// System.out.println("****"+key);
			if (val != null) {
				// System.out.println("*PP***"+key);
				Method method = getSetterByField(methodList, key);
				if (method != null) {
					method.invoke(ptObject, val);
				}
			}
		}

		// 以下用于处理ObjectReference类型的对应set方法
		/*
		 * for (Method method : methodList) { String methodName =
		 * method.getName(); if (methodName.startsWith("set")) { Class<?>[]
		 * parameterTypes = method.getParameterTypes(); if
		 * (parameterTypes.length > 0) { Class cls = parameterTypes[0]; if
		 * (PTFactor.class.isAssignableFrom(cls)) { String param =
		 * methodName.substring(3, methodName.length()).toUpperCase(); String
		 * innerId = map.get(param); if (innerId == null || innerId.equals(""))
		 * { continue; } Object refObj = getObject(cls, innerId); if (refObj !=
		 * null) { method.invoke(ptObject, refObj); } } } } }
		 */
		Method method = ptObject.getClass().getMethod("setAttrContainer",
				Map.class);
		method.invoke(ptObject, fillBaseFields(map, ptObject.getClassId()));
		return ptObject;
	}

	/**
	 * 给现有对象填充指定s的引用属性值
	 * 
	 * @param obj
	 *            引用对象
	 * @param refObj
	 *            被引用对象
	 * @param refField
	 *            引用属性名
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void setRefObjField(PTFactor obj, PTFactor refObj,
			String refField) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		ClassAttrInfoDef caid = ModelInfoUtil.getService().getClassAttrInfo(
				obj.getClassId(), refField);
		if (caid == null) {
			throw new RuntimeException("在类" + obj.getClassId() + "中找不到属性"
					+ refField);
		}
		if (caid.getInfoType() == InfoType.BASIC) {// 引用是基本属性的情况
			String methodName = "set" + refField;
			List<Method> methodList = getClassMethods(obj.getClass());
			for (Method method : methodList) {
				if (method.getName().equalsIgnoreCase(methodName)) {
					Class<?>[] parameterTypes = method.getParameterTypes();
					if (parameterTypes.length == 1) {
						Class cls = parameterTypes[0];
						if (PTFactor.class.isAssignableFrom(cls)) {
							if (refObj != null) {
								method.invoke(obj, refObj);
							}
						}
					}
				}
			}
		} else {// 引用是扩展属性的情况
			ObjectReference objRef = new ObjectReference();
			objRef.setClassId(refObj.getClassId());
			objRef.setInnerId(refObj.getInnerId());

			((CommonObject) obj).setValue(refField + "Ref", objRef);
		}
	}

	/**
	 * 根据属性的类型转换对应的值
	 * 
	 * @param field
	 *            属性
	 * @param val
	 *            属性值
	 * @return 根据类型转换后的值
	 * @throws RuntimeException
	 */
	public static Object convertType(Field field, String val)
			throws RuntimeException {
		String type = field.getType().getName();
		if (type.equalsIgnoreCase("long")) {
			return Long.parseLong(val);
		}
		if (type.equalsIgnoreCase("double")) {
			return Double.parseDouble(val);
		}
		if (type.equalsIgnoreCase("int")) {
			return Integer.parseInt(val);
		}
		return val;
	}

	/**
	 * 根据属性获取对应的setter方法
	 * 
	 * @param methodList
	 *            方法列表
	 * @param field
	 *            属性
	 * @return setter方法
	 * @throws RuntimeException
	 */
	public static Method getSetterByField(List<Method> methodList, String field)
			throws RuntimeException {
		String ss = "set(\\w+)";
		Pattern setM = Pattern.compile(ss);
		String rapl = "$1";
		for (Method method : methodList) {
			String methodName = method.getName();
			if (Pattern.matches(ss, methodName)) {
				String param = setM.matcher(methodName).replaceAll(rapl);
				if (field.equalsIgnoreCase(param)) {
					return method;
				}
			}
		}
		return null;
	}

	/**
	 * 根据属性获取对应的getter方法
	 * 
	 * @param methodList
	 *            方法列表
	 * @param field
	 *            属性
	 * @return getter方法
	 * @throws RuntimeException
	 */
	public static Method getGetterByField(List<Method> methodList, String field)
			throws RuntimeException {
		String ss = "get(\\w+)";
		Pattern setM = Pattern.compile(ss);
		String rapl = "$1";
		for (Method method : methodList) {
			String methodName = method.getName();
			if (Pattern.matches(ss, methodName)) {
				String param = setM.matcher(methodName).replaceAll(rapl);
				if (field.equalsIgnoreCase(param)) {
					return method;
				}
			}
		}
		return null;
	}

	/**
	 * 获取类的所有属性，包括父类的属性
	 * 
	 * @param clazz
	 *            类
	 * @return 类的所有属性列表
	 * @throws RuntimeException
	 */
	public static List<Field> getClassFields(Class clazz)
			throws RuntimeException {
		if (fieldMap.containsKey(clazz)) {
			return fieldMap.get(clazz);
		} else {
			List<Field> fieldList = new ArrayList<Field>();
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				fieldList.add(field);
			}
			if (clazz.getSuperclass() != null) {
				fieldList.addAll(getClassFields(clazz.getSuperclass()));
			}
			fieldMap.put(clazz, fieldList);
			return fieldList;
		}
	}

	/**
	 * 获取类的所有方法，包括父类的方法
	 * 
	 * @param clazz
	 *            类
	 * @return 类的所有方法列表
	 * @throws RuntimeException
	 */
	public static List<Method> getClassMethods(Class clazz)
			throws RuntimeException {
		if (methodMap.containsKey(clazz)) {
			return methodMap.get(clazz);
		} else {
			List<Method> methodList = new ArrayList<Method>();
			Method[] methods = clazz.getDeclaredMethods();
			for (Method method : methods) {
				methodList.add(method);
			}
			if (clazz.getSuperclass() != null) {
				methodList.addAll(getClassMethods(clazz.getSuperclass()));
			}
			methodMap.put(clazz, methodList);
			return methodList;
		}
	}

	/**
	 * 获取给定PTFactor对象的给定属性值（利用getter获取，如果没有提供getter将无法获取）
	 * 
	 * @param factor
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	public static Object getFieldValue(PTFactor factor, String fieldName)
			throws Exception {
		List<Field> fieldList = getClassFields(factor.getClass());
		for (Field field : fieldList) {
			if (field.getName().equalsIgnoreCase(fieldName)) {
				fieldName = field.getName();
				break;
			}
		}
		List<Method> methodList = getClassMethods(factor.getClass());
		Method getter = getGetterByField(methodList, fieldName);
		if (getter != null) {
			return getter.invoke(factor);
		}
		return "";
	}

	/**
	 * 根据两个类标识判断对应的两个类是否是继承关系
	 * 
	 * @param pclassId
	 * @param classId
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static boolean isAssignable(String pclassId, String classId)
			throws Exception {
		if (pclassId.equals(classId)) {
			return true;
		}
		ClassInfoDef pclassCid = ModelInfoUtil.getService().getClassInfo(
				pclassId);
		ClassInfoDef classCid = ModelInfoUtil.getService()
				.getClassInfo(classId);
		Class pclass = Class.forName(pclassCid.getTargetClass());
		Class clazz = Class.forName(classCid.getTargetClass());
		return pclass.isAssignableFrom(clazz);
	}

	/**
	 * 将PTObject对象转换为Map，把扩展属性同时转换为标准Map属性，把引用关系的对象同时加载
	 * 
	 * @param obj
	 * @param refFieldMap
	 *            引用属性Map，结构为key：引用属性名，value：引用属性对象中的显示属性
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> ptObject2Map(CommonObject obj,
			Map<String, String> refFieldMap) throws Exception {
		return ptObject2Map(obj, refFieldMap, false);
	}

	/**
	 * 将PTObject对象转换为Map，把扩展属性同时转换为标准Map属性，把引用关系的对象同时加载
	 * 
	 * @param obj
	 * @param refFieldMap
	 *            引用属性Map，结构为key：引用属性名，value：引用属性对象中的显示属性
	 * @param toUpper
	 *            引用属性Map，结构为key：引用属性名，value：引用属性对象中的显示属性
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> ptObject2Map(CommonObject obj,
			Map<String, String> refFieldMap, boolean toUpper) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Field> fieldList = getClassFields(obj.getClass());
		List<Method> methodList = getClassMethods(obj.getClass());

		for (Field field : fieldList) {
			if ("ATTRCONTAINER".equalsIgnoreCase(field.getName())) {
				continue;
			}
			Method getter = getGetterByField(methodList, field.getName());
			if (getter != null) {
				Object v = getter.invoke(obj);
				if (v instanceof ObjectReference) {// 处理引用的属性
					String fieldName = field.getName().substring(0,
							field.getName().lastIndexOf("Ref"));
					PTFactor factor = ((ObjectReference) v).getObject();
					if (factor == null) {
						continue;
					}
					String displayValue = factor.getInnerId();
					if (refFieldMap != null && !refFieldMap.isEmpty()) {
						String textField = CommonUtil.getParameter(refFieldMap,
								fieldName);
						if (!textField.equals("")) {
							Object textFieldValue = getFieldValue(factor,
									textField);
							if (textFieldValue != null
									&& !textFieldValue.equals("")) {
								displayValue = textFieldValue.toString();
							}
						}
					}
					if (toUpper) {
						map.put(fieldName.toUpperCase(), factor.getInnerId());
						map.put(fieldName.toUpperCase() + "_TEXT", displayValue);
					} else {
						map.put(fieldName, factor.getInnerId());
						map.put(fieldName + "_TEXT", displayValue);
					}
				} else {
					if (toUpper) {
						map.put(field.getName().toUpperCase(), v);
					} else {
						map.put(field.getName(), v);
					}
				}
			}
		}
		// 处理平台新版本扩展属性标识
		for (String key : obj.getAttrContainer().keySet()) {
			String k = key;
			if (k.toUpperCase().startsWith("E_")) {
				k = k.replaceFirst("E_", "").replaceFirst("e_", "");
			}
			if (toUpper) {
				map.put(k.toUpperCase(), obj.getAttrContainer().get(key));
			} else {
				map.put(k, obj.getAttrContainer().get(key));
			}
		}
		return map;
	}
}
