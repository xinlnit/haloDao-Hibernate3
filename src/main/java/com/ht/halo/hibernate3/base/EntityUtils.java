package com.ht.halo.hibernate3.base;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ht.halo.hibernate3.map.HaloMap;


public class EntityUtils {
	private static final Log logger = LogFactory.getLog(EntityUtils.class);
	 /**
	   * @Title: setEntity
	   * @Description: TODO Action层  设置实体某字段值 map转entity
	   * @param entity
	   * @param parameters
	    */
	 public  static  Object setEntity(Object entity,HaloMap parameter){
		if(null!=parameter){
			for (Entry<String, ?> entry : parameter.entrySet()) {
				MyBeanUtils.setFieldValue(entity, entry.getKey(), entry.getValue());
			}
		}
		return entity;
	 }
	
	   public static <T> T toEntity(Class<T> clazz, HaloMap map) {
	        T obj = null;
	        try {
	            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
	            obj = clazz.newInstance(); // 创建 JavaBean 对象
	            // 给 JavaBean 对象的属性赋值
	            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
	            for (int i = 0; i < propertyDescriptors.length; i++) {
	                PropertyDescriptor descriptor = propertyDescriptors[i];
	                String propertyName = descriptor.getName();
	                if (map.containsKey(propertyName)) {
	                    // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
	                    Object value = map.get(propertyName);
	                    if ("".equals(value)) {
	                        value = null;
	                    }
	                    Object[] args = new Object[1];
	                    args[0] = value;
	                    try {
	                        descriptor.getWriteMethod().invoke(obj, args);
	                    } catch (InvocationTargetException e) {
	                    	logger.warn("字段映射失败");
	                    }
	                }
	            }
	        } catch (IllegalAccessException e) {
	        	logger.error("实例化 JavaBean 失败");
	        } catch (IntrospectionException e) {
	        	logger.error("分析类属性失败");
	        } catch (IllegalArgumentException e) {
	        	logger.error("映射错误");
	        } catch (InstantiationException e) {
	        	logger.error("实例化 JavaBean 失败");
	        }
	        return (T) obj;
	    }
	  
	    public static HaloMap toHaloMap(Object bean) {
	        Class<? extends Object> clazz = bean.getClass();
	        HaloMap returnMap = new HaloMap();
	        BeanInfo beanInfo = null;
	        try {
	            beanInfo = Introspector.getBeanInfo(clazz);
	            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
	            for (int i = 0; i < propertyDescriptors.length; i++) {
	                PropertyDescriptor descriptor = propertyDescriptors[i];
	                String propertyName = descriptor.getName();
	                if (!propertyName.equals("class")) {
	                    Method readMethod = descriptor.getReadMethod();
	                     Object result  = readMethod.invoke(bean, new Object[0]);
	                    if (null != propertyName) {
	                        propertyName = propertyName.toString();
	                    }
	                    if(null==result){
	                    	continue;
	                    }
	                    if (null != result) {
	                        result = result.toString();
	                    }
	                    returnMap.put(propertyName, result);
	                }
	            }
	        } catch (IntrospectionException e) {
	        	logger.error("分析类属性失败");
	        } catch (IllegalAccessException e) {
	        	logger.error("实例化 JavaBean 失败");
	        } catch (IllegalArgumentException e) {
	        	logger.error("映射错误");
	        } catch (InvocationTargetException e) {
	        	logger.error("调用属性的 setter 方法失败");
	        }
	        return returnMap;
	    }


}
