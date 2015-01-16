package com.ht.halo.hibernate3.map;

import java.util.Map;


public class MyHashMap extends HashMap<String, Object> implements Map<String, Object>{
	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 198382668771840146L;

	public MyHashMap() {
	}

	public MyHashMap(Map<String, Object> map) {
		if (null == map) {
			map = new MyHashMap();
		}
		for (Entry<String, Object> entry : map.entrySet()) {
			this.set(entry.getKey(), entry.getValue());
		}
	}

	public MyHashMap set(String key, Object value) {
		super.put(key, value);
		return this;
	}

	public MyHashMap put(String key, Object value) {
		return this.set(key, value);
	}
}
