package com.ht.halo.hibernate3.bean;

import com.ht.halo.hibernate3.HaloMap;

public class SqlWithParameter {
	private String sql;
	private HaloMap paramterMap;
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public HaloMap getParamterMap() {
		return paramterMap;
	}
	public void setParamterMap(HaloMap paramterMap) {
		this.paramterMap = paramterMap;
	}


	
}
