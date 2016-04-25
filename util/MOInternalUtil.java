package com.bjsasc.twc.brace.util;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.Converter;

/**
 * 对象转换
 * @author 陈建立
 * 2015-12-18
 */
public class MOInternalUtil {

	/**
	 * map 转bean
	 * @param data
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> List<T> mapToBean(List<Map<String, Object>> data,Class<T> clazz) {
		List retVal = new ArrayList();
		for (Iterator it = data.iterator(); it.hasNext();) {
			Map map = (Map) it.next();
			Object obj = null;
			try {
				obj = clazz.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}

			Field[] fields = getAllFields(clazz);
			for (Iterator it2 = map.entrySet().iterator(); it2.hasNext();) {
				Map.Entry entry = (Map.Entry) it2.next();
				String key = (String) entry.getKey();
				Object value = entry.getValue();
				Field[] arr = fields;
				int len = arr.length;
				for (int i = 0; i < len; ++i) {
					Field f = arr[i];
					f.setAccessible(true);
					if (key.equalsIgnoreCase(f.getName())) {
						try {
							Class fieldType = f.getType();
							f.set(obj, convert(fieldType, value));
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}
			}
			retVal.add(obj);
		}
		return retVal;
	}

	/**
	 * 获取字段集合
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static <T> Field[] getAllFields(Class<T> clazz) {
		List fieldList = new ArrayList();
		fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
		Class parent = clazz.getSuperclass();
		if (!(parent.equals(Object.class)))
			fieldList.addAll(Arrays.asList(getAllFields(parent)));

		Field[] fields = new Field[fieldList.size()];
		return ((Field[]) fieldList.toArray(fields));
	}

	/**
	 * 数据类型转换
	 * @param fieldType
	 * @param value
	 * @return
	 */
	private static Object convert(Class<?> fieldType, Object value) {
		if (value == null) {
			return null;
		}

		if (fieldType.isAssignableFrom(Boolean.TYPE)) {
			String strVal = (String) value;
			if (("F".equals(strVal)) || ("T".equals(strVal))) {
				return databaseValToBool(strVal);
			}
		}

		Converter converter = BeanUtilsBean.getInstance().getConvertUtils()
				.lookup(fieldType);

		if (converter != null)
			return converter.convert(fieldType, value);

		return value;
	}
	/**
	 * 存储boolean字段值转义  
	 * @param value
	 * @return
	 */
	public static Boolean databaseValToBool(String value) {
		if ("T".equals(value))
			return Boolean.TRUE;

		return Boolean.FALSE;
	}
}
