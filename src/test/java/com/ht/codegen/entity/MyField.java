package com.ht.codegen.entity;

public class MyField {
	    private String fieldId;
	    private String entityId;
	    private String fieldName;
	    private String fieldNameCap;
	    private Integer position;
	    private String defaultValue;
	    private Boolean nullable;
	    private String type;
	    private Integer length;// 空或255
		private Integer precision;// 精度
		private Integer scale;
	    private Boolean iskey;
	    private String fieldComment;
	    private HaloViewColumn viewColumn;
		public String getFieldId() {
			return fieldId;
		}
		public void setFieldId(String fieldId) {
			this.fieldId = fieldId;
		}
		public String getFieldName() {
			return fieldName;
		}
		
		public String getEntityId() {
			return entityId;
		}
		public void setEntityId(String entityId) {
			this.entityId = entityId;
		}
		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
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
		public Boolean getNullable() {
			return nullable;
		}
		public void setNullable(Boolean nullable) {
			this.nullable = nullable;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public Integer getLength() {
			return length;
		}
		public void setLength(Integer length) {
			this.length = length;
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
		public Boolean getIskey() {
			return iskey;
		}
		public void setIskey(Boolean iskey) {
			this.iskey = iskey;
		}
		
		public String getFieldComment() {
			return fieldComment;
		}
		public void setFieldComment(String fieldComment) {
			this.fieldComment = fieldComment;
		}
	
		public HaloViewColumn getViewColumn() {
			return viewColumn;
		}
		public void setViewColumn(HaloViewColumn viewColumn) {
			this.viewColumn = viewColumn;
		}
		public String getFieldNameCap() {
			return fieldNameCap;
		}
		public void setFieldNameCap(String fieldNameCap) {
			this.fieldNameCap = fieldNameCap;
		}
	
	    
	    
}
