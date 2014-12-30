package com.ht.codegen.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ht.codegen.entity.MyEntity;
import com.ht.codegen.entity.MyField;
import com.ht.codegen.entity.ViewColumn;
import com.ht.codegen.entity.ViewTable;
import com.ht.codegen.utils.FileUtils;
import com.ht.codegen.utils.TableUtil;
import com.ht.codegen.utils.freemarker.Freemarker;
import com.ht.halo.hibernate3.HaloMap;
import com.ht.halo.hibernate3.map.HashMap;

@Service
@Transactional
public class GenEntityService {
	@Resource
     private ViewColumnService viewColumnService;
	@Resource
     private ViewTableService viewTableService;
	 private  MyEntity toEntity(HaloMap parameter){
		ViewTable viewTable =  viewTableService.findViewTable(parameter);
		MyEntity myEntity = new MyEntity();
		myEntity.setEntityId(viewTable.getTableId());
		myEntity.setEntityName(TableUtil.toEntity(viewTable.getTableName()));
		String entityComment=viewTable.getTableComment();
		if(entityComment.indexOf("_")!=-1){
			entityComment=StringUtils.substringAfterLast(entityComment, "_");
		}
		entityComment=entityComment.replaceAll("表", "");
		myEntity.setEntityComment(entityComment);
		String entityType=null;
		if(viewTable.getTableType().equalsIgnoreCase("BASE TABLE")){
			entityType="base";
		}
		if(viewTable.getTableType().equalsIgnoreCase("VIEW")){
			entityType="view";
		}
		myEntity.setEntityType(entityType);
		myEntity.setViewTable(viewTable);
		 return myEntity;
	 }
	 private List<MyField> toFields(HaloMap parameter,MyEntity myEntity){
		List<ViewColumn> viewColumns = viewColumnService.findViewColumnList(parameter);
		List<MyField>  myFileds= new ArrayList<MyField>();
		MyField myField;
		for (ViewColumn viewColumn : viewColumns) {
			myField= new MyField();
			myField.setFieldId(viewColumn.getColumnId());
			myField.setEntityId(viewColumn.getTableId());
			myField.setFieldName(TableUtil.toFiled(viewColumn.getColumnName()));
			myField.setFieldNameCap(StringUtils.capitalize(myField.getFieldName()));
			myField.setDefaultValue(viewColumn.getDefaultValue());
			myField.setLength(viewColumn.getMaxLength());
			myField.setPosition(viewColumn.getPosition());
			myField.setPrecision(viewColumn.getPrecision());
			myField.setScale(viewColumn.getScale());
			String fieldComment = viewColumn.getColumnComment();
			if(fieldComment.indexOf(":")!=-1){
				fieldComment=StringUtils.substringBefore(fieldComment, ":").trim();
			}
			if(fieldComment.indexOf("(")!=-1){
				fieldComment=StringUtils.substringBefore(fieldComment, "(").trim();
			}
			myField.setFieldComment(fieldComment);
			Boolean iskey=false;
			if(myEntity.getEntityType().endsWith("view")){
				if(viewColumn.getPosition()==1){
					iskey=true;//视图默认位置1为主键
				}
			}
			if("PRI".equalsIgnoreCase(viewColumn.getColumnKey())){
				iskey=true;
			}
			myField.setIskey(iskey);
			
			Boolean nullable=true;
			if(viewColumn.getNullable().equalsIgnoreCase("YES")){
				nullable=false;
			}
			myField.setNullable(nullable);
			String type="String";
			String typeInDb=viewColumn.getColumnType();
			if(typeInDb.equalsIgnoreCase("varchar")||typeInDb.equalsIgnoreCase("char")){
				type="String";
			}
			if(typeInDb.equalsIgnoreCase("int")||typeInDb.equalsIgnoreCase("tinyint")){
				type="Integer";
			}
			if(typeInDb.equalsIgnoreCase("float")){
				type="Float";
			}
			if(typeInDb.equalsIgnoreCase("datetime")){
				type="Date";
			}
			if(typeInDb.equalsIgnoreCase("datetime")||typeInDb.equalsIgnoreCase("date")||typeInDb.equalsIgnoreCase("time")){
				type="Date";
			}
			if(typeInDb.equalsIgnoreCase("decimal")){
				type="BigDecimal";
			}
			if(typeInDb.equalsIgnoreCase("bit")){
				type="Boolean";
				if(!nullable){
					type="boolean";
				}
			}
			if(typeInDb.equalsIgnoreCase("bigint")){
				type="Long";
			}
			myField.setType(type);
			if(iskey){
				myEntity.setIdType(type);
				myEntity.setIdName(myField.getFieldName());
			}
			myField.setViewColumn(viewColumn);
			myFileds.add(myField);
		}
		 return myFileds;
	 }
	 public MyEntity getMyEntity(HaloMap parameter){
		 MyEntity myEntity = new MyEntity();
		 myEntity=toEntity(parameter);
		 List<MyField> myFields=toFields(parameter, myEntity);
		 myEntity.setFields(myFields);
		 return myEntity;
	 }
	
	 public void genEntity(HaloMap parameter){
		 MyEntity myEntity =getMyEntity(parameter);
	      Freemarker freemarker = new Freemarker();
	      File templateFolder = FileUtils.getClassPath("com.ht.codegen.template", "");
		  freemarker.generateBytemplate(new HashMap<String, Object>().set("bean", myEntity), 
				  templateFolder, "entity.ftl",FileUtils.getSrcPath("com.ht.test.entity", "BaseCompany3.java"));
	 }
}
