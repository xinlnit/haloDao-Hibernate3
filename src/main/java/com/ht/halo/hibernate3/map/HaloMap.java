package com.ht.halo.hibernate3.map;

import java.util.Map;

import com.ht.halo.hibernate3.HaloDao;

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
    private int addColumn=0;
    private int addOrder=0;
    private int addGroup=0;
    private int addHql=0;
    private StringBuffer tempStr=new StringBuffer();
	public HaloMap() {
	}
	public HaloMap(Map<String, Object> map) {
		super(map);
	}
	
	public HaloMap  set(String key, Object value) {
		if(key.startsWith(HaloDao.ADDORDER)){
			addOrder++;
			super.put(HaloDao.ADDORDER+addOrder, value);
			return this;	
		}
	
		if(key.startsWith(HaloDao.ADDHQL)){
			addHql++;
			super.put(HaloDao.ADDHQL+addHql, value);
			return this;	
		}
		if(key.startsWith(HaloDao.ADDCOLUMN)){
			addColumn++;
			super.put(HaloDao.ADDCOLUMN+addColumn, value);
			return this;	
		}
		if(key.startsWith(HaloDao.ADDGROUP)){
			addGroup++;
			super.put(HaloDao.ADDGROUP+addGroup, value);
			return this;	
		}
	
        super.put(key, value);
	    return this;  
	  }

	public HaloMap put(String key, Object value){
		return this.set(key, value);	
	}
	
	public HaloMap addOrder(String... orders){
		for (String order : orders) {
			this.set(HaloDao.ADDORDER, order);
		}
		return this;
	}
	public HaloMap addOrderDesc(String... orderDescs){
		for (String orderDesc : orderDescs) {
			if (orderDesc.indexOf(":desc")==-1) {
				orderDesc=orderDesc+":desc";
			}
			this.set(HaloDao.ADDORDER, orderDesc);
		}
		return this;
	}
	public HaloMap addHql(String... hqls){
		for (String hql : hqls) {
			this.set(HaloDao.ADDHQL, hql);
		}
		return this;
	}
	public HaloMap addColumn(String... columnNames){
		for (String columnName : columnNames) {
			this.set(HaloDao.ADDCOLUMN, columnName);
		}
		return this;
	}
	public HaloMap addGroup(String... groups){
		for (String group : groups) {
			this.set(HaloDao.ADDGROUP, group);
		}
		return this;
	}
	/**
	 * @Title: addParameter
	 * @Description: TODO key:prm
	 * @param key
	 * @param value
	 * @return
	 */
	public HaloMap addParameter(String key ,Object value){

		this.set(key+":prm", value);
		return this;
	}
	public HaloMap addData(String key ,Object value){
		this.set(key+":data", value);
		return this;
	}

	public HaloMap setColumn(String columnName){
		tempStr.append(columnName);
		return this;
	}
	public HaloMap leftBracket(){
		tempStr.append("(");
		return this;
	}
	public HaloMap rightBracket(){
		tempStr.append(")");
		return this;
	}
	public HaloMap or(){
		tempStr.append("|");
		return this;
	}
	public HaloMap in(){
		tempStr.append(":in");
		return this;
	}
	public HaloMap neq(){
		tempStr.append(":!=");
		return this;
	}
	public HaloMap gt(){
		tempStr.append(":>");
		return this;
	}
	public HaloMap ge(){
		tempStr.append(":>=");
		return this;
	}
	public HaloMap lt(){
		tempStr.append(":<");
		return this;
	}
	public HaloMap le(){
		tempStr.append(":<=");
		return this;
	}
	public HaloMap likeAll(){
		tempStr.append(":%like%");
		return this;
	}
	public HaloMap likeLeft(){
		tempStr.append(":%like");
		return this;
	}
	public HaloMap like(){
		tempStr.append(":like");
		return this;
	}
	public HaloMap eq(){
		tempStr.append(":=");
		return this;
	}
	public HaloMap setValue(Object value){
		this.set(tempStr.toString(), value);
		tempStr=new StringBuffer();
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
	public HaloMap put(String key, Object... value){
		Object  valueTemp=value;
		return this.set(key, valueTemp);
	}
}

	

