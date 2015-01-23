package com.ht.halo.hibernate3.map;

import java.util.Map;

public class MyLinkedHashMap extends LinkedHashMap<String, Object> implements Map<String, Object>{
	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 3409416404907094871L;

	public MyLinkedHashMap() {
	}

	public MyLinkedHashMap(Map<String, Object> map) {
		if (null == map) {
			map = new MyHashMap();
		}
		for (Entry<String, Object> entry : map.entrySet()) {
			this.set(entry.getKey(), entry.getValue());
		}
	}

	public MyLinkedHashMap set(String key, Object value) {
		super.put(key, value);
		return this;
	}

	public MyLinkedHashMap put(String key, Object value) {
		return this.set(key, value);
	}
}
