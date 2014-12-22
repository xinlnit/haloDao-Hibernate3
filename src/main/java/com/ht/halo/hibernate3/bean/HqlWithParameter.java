package com.ht.halo.hibernate3.bean;

import java.util.Map;

public class HqlWithParameter {
	private String hql;
	private Map<String,Object> paramterMap;
	private Boolean addColumn=false;
	private Boolean addCache=false;

	public Boolean getAddColumn() {
		return addColumn;
	}

	public void setAddColumn(Boolean addColumn) {
		this.addColumn = addColumn;
	}

	public Boolean getAddCache() {
		return addCache;
	}

	public void setAddCache(Boolean addCache) {
		this.addCache = addCache;
	}

	public String getHql() {
		return hql;
	}

	public void setHql(String hql) {
		this.hql = hql;
	}

	public Map<String, Object> getParamterMap() {
		return paramterMap;
	}

	public void setParamterMap(Map<String, Object> paramterMap) {
		this.paramterMap = paramterMap;
	}



}
