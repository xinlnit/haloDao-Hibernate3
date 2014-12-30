package com.ht.codegen.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ViewColumn {
	@Id
    private String columnId;
	 private String tableId;
    private String dbName;
    private String tableName;
    private String columnName;
    private Integer position;
    private String defaultValue;
    private String nullable;
    private String dataType;
    private Integer maxLength;
    private Integer octelLength;
    private Integer precision;
    private Integer scale;
    private String columnType;
    private String columnKey;
    private String columnComment;
	public String getColumnId() {
		return columnId;
	}
	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}
	
	public String getTableId() {
		return tableId;
	}
	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getNullable() {
		return nullable;
	}
	public void setNullable(String nullable) {
		this.nullable = nullable;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public Integer getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}
	public Integer getOctelLength() {
		return octelLength;
	}
	public void setOctelLength(Integer octelLength) {
		this.octelLength = octelLength;
	}
	public Integer getPrecision() {
		return precision;
	}
	public void setPrecision(Integer precision) {
		this.precision = precision;
	}
	public Integer getScale() {
		return scale;
	}
	public void setScale(Integer scale) {
		this.scale = scale;
	}
	public String getColumnType() {
		return columnType;
	}
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public String getColumnKey() {
		return columnKey;
	}
	public void setColumnKey(String columnKey) {
		this.columnKey = columnKey;
	}
	public String getColumnComment() {
		return columnComment;
	}
	public void setColumnComment(String columnComment) {
		this.columnComment = columnComment;
	}
    
    

}
