package com.ht.codegen.entity;

import java.util.List;

public class MyEntity {
    private String entityId;
    private String entityName;
    private String entityComment;
    private String entityType;
    private String idType;
    private String idName;
    private ViewTable viewTable;
    private List<MyField> fields;
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getEntityComment() {
		return entityComment;
	}
	

	
	public ViewTable getViewTable() {
		return viewTable;
	}
	public void setViewTable(ViewTable viewTable) {
		this.viewTable = viewTable;
	}
	public void setEntityComment(String entityComment) {
		this.entityComment = entityComment;
	}
	
	public List<MyField> getFields() {
		return fields;
	}
	public void setFields(List<MyField> fields) {
		this.fields = fields;
	}
	public String getEntityType() {
		return entityType;
	}
	
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getIdName() {
		return idName;
	}
	public void setIdName(String idName) {
		this.idName = idName;
	}
   
}
