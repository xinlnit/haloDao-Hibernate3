
package com.ht.halo.hibernate3.base;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.hibernate.HibernateException;
import org.hibernate.transform.ResultTransformer;

/**
 * 自定义的数据库字库转换成POJO
 */
public class ColumnToBean implements ResultTransformer {
	private static final long serialVersionUID = 1L;
	private final Class<?> resultClass;
	
	public ColumnToBean(Class<?> resultClass) {
		if(resultClass==null) throw new IllegalArgumentException("resultClass cannot be null");
		this.resultClass = resultClass;
	}
	//结果转换时，HIBERNATE调用此方法
	public Object transformTuple(Object[] tuple, String[] aliases) {
		Object result = null;
		try {
			result = resultClass.newInstance();
		for (int i = 0; i < aliases.length; i++) {
			String aliase=aliases[i];
			Object value=tuple[i];
		    String filedName =	 TableUtil.toFiled(aliase);
		    ConvertUtils.register(new DateConverter(null), java.util.Date.class);  
		     BeanUtils.setProperty(result, filedName, value);
		}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			throw new HibernateException("Could not instantiate resultclass: " + resultClass.getName());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			throw new HibernateException("Could not instantiate resultclass: " + resultClass.getName());
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}



	@SuppressWarnings({  "rawtypes" })
	public List transformList(List collection) {
		return collection;
	}

}

