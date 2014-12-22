package com.ht.halo.hibernate3.map;

import java.util.Map;

/**
 * @ClassName: HtMap
 * @Description: TODO LinkedHashMap链式实现 按存放循序排列
 * @author fengchangyi
 * @date 2014-11-4 下午7:48:03
 * @param <K>
 * @param <V>
 */
public class LinkedHashMap<K, V> extends java.util.LinkedHashMap<K, V> implements Map<K, V> {
	public LinkedHashMap() {
		super();
	}

	public LinkedHashMap(Map<? extends K, ? extends V> map) {
		super(map);
	}

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = -7709853214708221134L;

	public LinkedHashMap<K, V> set(K key, V value) {
		super.put(key, value);
		return this;
	}
	
}
