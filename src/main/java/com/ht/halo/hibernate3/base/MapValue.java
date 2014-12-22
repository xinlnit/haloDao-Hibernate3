package com.ht.halo.hibernate3.base;

import java.util.HashMap;
import java.util.Map;
  /**
     * @ClassName: MapValue
     * @Description: TODO 给Map<String, Object> parameter 设置值
     * @author fengchangyi
     * @date 2014-6-19 下午5:28:41
     */
public class MapValue {

	public static Map<String, Object> set(Map<String, Object> parameter, String key, Object value) {
		if (null == parameter) {
			parameter = new HashMap<String, Object>();
		}
		parameter.put(key, value);
		return parameter;
	}
	public static Map<String, Object> set(String key, Object value) {	
		Map<String,Object>	parameter = new HashMap<String, Object>();
		parameter.put(key, value);
		return parameter;
	}
	public static Map<String, Object> set(String[] key, Object[] value) {
		Map<String,Object>	parameter = new HashMap<String, Object>();
		for(int i=0;i<key.length;i++){
			parameter.put(key[i], value[i]);
		}
		return parameter;
	}
	public static Map<String, Object> set(Map<String, Object> parameter, String[] key, Object[] value) {
		if (null == parameter) {
			parameter = new HashMap<String, Object>();
		}
		for(int i=0;i<key.length;i++){
			parameter.put(key[i], value[i]);
		}
		return parameter;
	}

	/**
	 * @Title: main
	 * @Description: TODO
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
