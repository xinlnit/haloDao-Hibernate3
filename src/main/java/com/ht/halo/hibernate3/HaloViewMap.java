package com.ht.halo.hibernate3;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang.StringUtils;

import com.ht.halo.hibernate3.map.HashMap;

/**
 * TODO haloView封装的Map,扩展类型转化
 * 
 * @author fengchangyi@haitao-tech.com
 * @date 2015-1-12 上午10:51:33
 */
public class HaloViewMap extends HashMap<String, Object> implements Map<String, Object> {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 8955941754407113803L;

	public HaloViewMap() {
	}

	public HaloViewMap(Map<String, Object> map) {
		if (null == map) {
			map = new HaloViewMap();
		}
		for (Entry<String, Object> entry : map.entrySet()) {
			this.set(entry.getKey(), entry.getValue());
		}
	}

	public HaloViewMap set(String key, Object value) {
		super.put(key, value);
		return this;
	}

	public HaloViewMap put(String key, Object value) {
		return this.set(key, value);
	}

	private String filterKey(String key) {
		return key.replaceAll("3", "#");
	}

	public Object get(String key) {
		key = filterKey(key);
		if (key.indexOf("#") == -1) {
			return super.get(key);
		}
		if (key.endsWith("#Integer")) {
			key = StringUtils.substringBefore(key, "#Integer");
			return getInteger(key);
		}
		if (key.endsWith("#Date")) {
			key = StringUtils.substringBefore(key, "#Date");
			return getDate(key);
		}
		if (key.endsWith("#Double")) {
			key = StringUtils.substringBefore(key, "#Double");
			return getDouble(key);
		}
		if (key.endsWith("#BigDecimal")) {
			key = StringUtils.substringBefore(key, "#BigDecimal");
			return getBigDecimal(key);
		}
		if (key.endsWith("#Float")) {
			key = StringUtils.substringBefore(key, "#Float");
			return getFloat(key);
		}
		if (key.endsWith("#Long")) {
			key = StringUtils.substringBefore(key, "#Long");
			return getLong(key);
		}
		if (key.endsWith("#Short")) {
			key = StringUtils.substringBefore(key, "#Short");
			return getShort(key);
		}
		if (key.endsWith("#String")) {
			key = StringUtils.substringBefore(key, "#String");
			return getString(key);
		}

		return super.get(key);
	}

	public Integer getInteger(String key) {
		Object value = super.get(key);
		return (Integer) ConvertUtils.convert(value, Integer.class);
	}

	public Date getDate(String key) {
		Object value = super.get(key);
		ConvertUtils.register(new DateConverter(null), java.util.Date.class);
		return (Date) ConvertUtils.convert(value, Date.class);
	}

	public Boolean getBoolean(String key) {
		Object value = super.get(key);
		return (Boolean) ConvertUtils.convert(value, Boolean.class);
	}

	public BigDecimal getBigDecimal(String key) {
		Object value = super.get(key);
		return (BigDecimal) ConvertUtils.convert(value, BigDecimal.class);
	}

	public Double getDouble(String key) {
		Object value = super.get(key);
		return (Double) ConvertUtils.convert(value, Double.class);
	}

	public Float getFloat(String key) {
		Object value = super.get(key);
		return (Float) ConvertUtils.convert(value, Float.class);
	}

	public Short getShort(String key) {
		Object value = super.get(key);
		return (Short) ConvertUtils.convert(value, Short.class);
	}

	public Long getLong(String key) {
		Object value = super.get(key);
		return (Long) ConvertUtils.convert(value, Long.class);
	}

	public String getString(String key) {
		Object value = super.get(key);
		return (String) ConvertUtils.convert(value, String.class);
	}

}
