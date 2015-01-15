package com.ht.halo.hibernate3.base;

import java.util.List;

import org.hibernate.transform.ResultTransformer;

import com.ht.halo.hibernate3.HaloViewMap;

public class ColumnToMap implements ResultTransformer {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 903475531810186943L;

	//结果转换时，HIBERNATE调用此方法
	public Object transformTuple(Object[] tuple, String[] aliases) {
		HaloViewMap 	haloViewMap= new HaloViewMap();

		for (int i = 0; i < aliases.length; i++) {
			String aliase=aliases[i];
			Object value=tuple[i];
		    String filedName =	 TableUtil.toFiled(aliase);
		    haloViewMap.set(filedName, value);
		}
		return haloViewMap;
	}



	@SuppressWarnings({  "rawtypes" })
	public List transformList(List collection) {
		return collection;
	}
}
