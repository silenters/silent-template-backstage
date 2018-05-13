package com.silent.template.backstage.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.silent.framework.base.utils.StringUtils;

/**
 * 请求数据处理工具类
 * @author TanJin
 * @date 2017-12-28
 */
public class RequestUtils {
	protected static Logger logger = LoggerFactory.getLogger(RequestUtils.class);
	private static Map<Class<?>, Field[]> fieldCacheMap = new ConcurrentHashMap<Class<?>, Field[]>();	//Bean对应所有字段集合

	/**
	 * 获取class的所有field
	 * @param clazz
	 * @return
	 */
	public static Field[] getFields(Class<?> clazz){
		Field[] fields = fieldCacheMap.get(clazz);
		if(fields == null) {
			fields = clazz.getDeclaredFields();
			fieldCacheMap.put(clazz, fields);
		}
		return fields;
	}
	
	/**
	 * 从请求中封装对象
	 * @param request
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static <T> T build(HttpServletRequest request, Class<T> clazz) throws Exception {
		final Field[] fields = getFields(clazz);
		final T t = clazz.newInstance();
		for (Field field : fields) {
			String value = request.getParameter(field.getName());
			if(value == null) {
				continue;
			}
			Class<?> c = field.getType();
			field.setAccessible(true);
			if (c == int.class) {
				field.setInt(t, StringUtils.parseInt(value, 0));
			} else if (c == long.class) {
				field.setLong(t, StringUtils.parseLong(value, 0));
			} else if (c == boolean.class) {
				field.setBoolean(t, StringUtils.parseBoolean(value, false));
			} else if (c == double.class) {
				field.setDouble(t, StringUtils.parseDouble(value, 0));
			} else if (c == String.class) {
				field.set(t, value);
			} else {
				field.set(t, value);
			}
		}
		return t;
	}
	
	/**
	 * 获取指定对象中的字段及对应的值，存入Map返回
	 * @param 
	 * @return
	 */
	public static Map<String, Object> getFieldValueMap(Object object) {
		Map<String, Object> fieldValueMap = new HashMap<String, Object>();
		if(null != object) {
			Class<?> clazz = object.getClass();
			try {
				Field[] fields = getFields(clazz);
				for (Field field : fields) {
					field.setAccessible(true);
					Object value = field.get(object);
					fieldValueMap.put(field.getName(), value);
				}
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		return fieldValueMap;
	}
}
