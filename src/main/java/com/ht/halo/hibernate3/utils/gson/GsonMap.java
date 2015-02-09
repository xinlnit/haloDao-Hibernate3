package com.ht.halo.hibernate3.utils.gson;

import com.ht.halo.hibernate3.map.MyHashMap;

public class GsonMap extends MyHashMap{
   
	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 3411879774530659720L;
	public GsonMap addDateFormat(String dateFormat) {
		super.set("dateFormat", dateFormat);
		return this;
	}

}
