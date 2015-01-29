package com.ht.halo.hibernate3.bean;

public class ColumnWithCondition {
	private String  leftBracket="";
	private String  rightBracket="";
	private String andOr="and";
	private String genColumnName;// 生成的hql中字段参数名,比如createTimeGt
	private String columnName;
	private String condition;// 条件
	private Object value;//值
	private String directValue;//值
	private String type;
	private boolean ifQuery;
	private String formate;
    private Boolean tempFlag=true;

	



	public Boolean getTempFlag() {
		return tempFlag;
	}

	public ColumnWithCondition setTempFlag(Boolean tempFlag) {
		this.tempFlag = tempFlag;
		return this;
	}

	public String getDirectValue() {
		return directValue;
	}

	public ColumnWithCondition setDirectValue(String directValue) {
		this.directValue = directValue;
		return this;
	}

	public String getLeftBracket() {
		return leftBracket;
	}

	public ColumnWithCondition setLeftBracket(String leftBracket) {
		this.leftBracket = leftBracket;
		return this;
	}

	public String getRightBracket() {
		return rightBracket;
	}

	public ColumnWithCondition setRightBracket(String rightBracket) {
		this.rightBracket = rightBracket;
		return this;
	}

	public String getAndOr() {
		return andOr;
	}

	public ColumnWithCondition setAndOr(String andOr) {
		this.andOr = andOr;
		return this;
	}

	public String getFormate() {
		return formate;
	}

	public ColumnWithCondition setFormate(String formate) {
		this.formate = formate;
		return this;
	}



	public Object getValue() {
		return value;
	}

	public ColumnWithCondition setValue(Object value) {
		this.value = value;
		return this;
	}

	public String getGenColumnName() {
		return genColumnName;
	}

	public ColumnWithCondition setGenColumnName(String genColumnName) {
		this.genColumnName = genColumnName;
		return this;
	}

	public String getColumnName() {
		return columnName;
	}

	public ColumnWithCondition setColumnName(String columnName) {
		this.columnName = columnName;
		return this;
	}

	public String getCondition() {
		return condition;
	}

	public ColumnWithCondition setCondition(String condition) {
		this.condition = condition;
		return this;
	}

	public String getType() {
		return type;
	}

	public ColumnWithCondition setType(String type) {
		this.type = type;
		return this;
	}

	public boolean getIfQuery() {
		return ifQuery;
	}

	public ColumnWithCondition setIfQuery(boolean ifQuery) {
		this.ifQuery = ifQuery;
		return this;
	}

}
