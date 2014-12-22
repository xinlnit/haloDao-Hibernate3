package com.ht.halo.hibernate3.bean;

import java.util.Map;

public class SqlWithParameter {
	private String sql;
	private Map<String,Object> paramterMap;
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public Map<String, Object> getParamterMap() {
		return paramterMap;
	}
	public void setParamterMap(Map<String, Object> paramterMap) {
		this.paramterMap = paramterMap;
	}
	

	
}
