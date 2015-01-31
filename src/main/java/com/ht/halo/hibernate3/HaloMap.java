package com.ht.halo.hibernate3;

import java.util.Map;

import com.ht.halo.hibernate3.map.LinkedHashMap;

/**
 * @ClassName: HtMapObject
 * @Description: TODO (HibernateDao专用且必须) 支持重复Key
 * @author fengchangyi
 * @date 2014-11-6 上午9:16:45
 */
public class HaloMap extends LinkedHashMap<String, Object> implements Map<String, Object> {
	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = -6169144882263038794L;
	private int addColumn = 0;
	private int addOrder = 0;
	private int addGroup = 0;
	private int addHql = 0;
	//public static final String  ADDMETHOD="addMethod";
	public HaloMap() {
	}

	public HaloMap(Map<String, Object> map) {
		if(null==map){
			map= new HaloMap();
		}
		for (Entry<String, Object> entry:map.entrySet()) {
			this.set(entry.getKey(), entry.getValue());
		}
		//super(map);
	}
	public HaloMap  setAll(Map<String, Object> map) {
		if(null==map){
			map= new HaloMap();
		}
		for (Entry<String, Object> entry:map.entrySet()) {
			this.set(entry.getKey(), entry.getValue());
		}
		return this;
	}

	/**
	 * 设置数组
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public HaloMap sets(String key, Object... value) {
		Object valueTemp = value;
		return this.set(key, valueTemp);
	}

	public HaloMap set(String key, Object value) {
		if (key.startsWith(HaloDao.ADDORDER)) {
			addOrder++;
			super.put(HaloDao.ADDORDER + addOrder, value);
			return this;
		}

		if (key.startsWith(HaloDao.ADDHQL)) {
			addHql++;
			super.put(HaloDao.ADDHQL + addHql, value);
			return this;
		}

		if (key.startsWith(HaloDao.ADDCOLUMN)) {
			addColumn++;
			super.put(HaloDao.ADDCOLUMN + addColumn, value);
			return this;
		}
		if (key.startsWith(HaloDao.ADDGROUP)) {
			addGroup++;
			super.put(HaloDao.ADDGROUP + addGroup, value);
			return this;
		}

		super.put(key, value);
		return this;
	}

	public HaloMap put(String key, Object value) {
		//throw new RuntimeException("本版本HaloMap不再支持put链式操作,同时也放弃单独put操作,请使用set");
		return this.set(key, value);
	}

	/**
	 *  TODO 添加排序(可追加)
	 * @param orders
	 * @return
	 */
	public HaloMap addOrder(String... orders) {
		for (String order : orders) {
			this.set(HaloDao.ADDORDER, order);
		}
		return this;
	}
	public HaloMap addHql(String... hqls) {
		for (String hql : hqls) {
			this.set(HaloDao.ADDHQL, hql);
		}
		return this;
	}
	public HaloMap addViewId(String  value) {
		this.set(HaloViewDao.VIEWID, value);
		return this;
	}
	public HaloMap addColumn(String... columnNames) {
		for (String columnName : columnNames) {
			this.set(HaloDao.ADDCOLUMN, columnName);
		}
		return this;
	}

	public HaloMap addGroup(String... groups) {
		for (String group : groups) {
			this.set(HaloDao.ADDGROUP, group);
		}
		return this;
	}
	/**
	 *  TODO 添加开始条数
	 * @param value
	 * @return
	 */
	public HaloMap addBegin( Object value) {
		this.set(HaloDao.ADDBEGIN, value);
		return this;
	}
	/**
	 *  TODO 添加结束条数
	 * @param value
	 * @return
	 */
	public HaloMap addEnd( Object value) {
		this.set(HaloDao.ADDEND, value);
		return this;
	}
	/**
	 * @Title: addParameter
	 * @Description: TODO key:prm
	 * @param key
	 * @param value
	 * @return
	 */
	public HaloMap addParameter(String key, Object value) {
		this.set(key + HaloDao.MYSPACE+HaloDao.PRM, value);
		return this;
	}

	public HaloMap addData(String key, Object value) {
		this.set(key +  HaloDao.MYSPACE+HaloViewDao.DATA, value);
		return this;
	}


	/**
	 * @Title: put
	 * @Description: TODO 给Map设置数组 变参数模式
	 * @author fengchangyi
	 * @param key
	 * @param value
	 * @return
	 */
	public HaloMap put(String key, Object... value) {
		Object valueTemp = value;
		return this.set(key, valueTemp);
	}
}
