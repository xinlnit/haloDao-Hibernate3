package com.ht.codegen.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ht.codegen.entity.MyEntity;
import com.ht.codegen.entity.MyField;
import com.ht.codegen.entity.ViewColumn;
import com.ht.codegen.entity.ViewTable;
import com.ht.codegen.utils.DateUtils;
import com.ht.codegen.utils.FileUtils;
import com.ht.codegen.utils.PropertiesUtil;
import com.ht.codegen.utils.TableUtil;
import com.ht.codegen.utils.freemarker.Freemarker;
import com.ht.halo.hibernate3.HaloMap;
import com.ht.halo.hibernate3.map.HashMap;

@Service
@Transactional
public class GenService {
	private static final Log logger = LogFactory.getLog(GenService.class);
	@Resource
	private ViewColumnService viewColumnService;
	@Resource
	private ViewTableService viewTableService;
	@Resource
    private HaloViewCommentService haloViewCommentService;
	private MyEntity toEntity(HaloMap parameter) {
		ViewTable viewTable = viewTableService.findViewTable(parameter);
		MyEntity myEntity = new MyEntity();
		myEntity.setEntityId(viewTable.getTableId());
		myEntity.setEntityName(TableUtil.toEntity(viewTable.getTableName()));
		String entityComment = viewTable.getTableComment();
		if (!viewTable.getTableType().equalsIgnoreCase("VIEW")&&(null == entityComment || StringUtils.isBlank(entityComment))) {
			throw new RuntimeException(viewTable.getTableName() + "表请加入注释!");
		}
	   if (viewTable.getTableType().equalsIgnoreCase("VIEW")){
			entityComment =haloViewCommentService.getViewCommentByName(viewTable.getTableName());
	 }
			
		
		if (entityComment.indexOf("_") != -1) {
			entityComment = StringUtils.substringAfterLast(entityComment, "_");
		}
		entityComment = entityComment.replaceAll("表", "");
		myEntity.setEntityComment(entityComment);
		String entityType = null;
		if (viewTable.getTableType().equalsIgnoreCase("BASE TABLE")) {
			entityType = "base";
		}
		if (viewTable.getTableType().equalsIgnoreCase("VIEW")) {
			entityType = "view";
		}
		myEntity.setEntityType(entityType);
		myEntity.setViewTable(viewTable);
		return myEntity;
	}

	private List<MyField> toFields(HaloMap parameter, MyEntity myEntity) {
		List<ViewColumn> viewColumns = viewColumnService.findViewColumnList(parameter);
		List<MyField> myFileds = new ArrayList<MyField>();
		MyField myField;
		for (ViewColumn viewColumn : viewColumns) {
			myField = new MyField();
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
		
			if (fieldComment.indexOf(":") != -1) {
				fieldComment = StringUtils.substringBefore(fieldComment, ":").trim();
			}
			if (fieldComment.indexOf("(") != -1) {
				fieldComment = StringUtils.substringBefore(fieldComment, "(").trim();
			}
			myField.setFieldComment(fieldComment);
			Boolean iskey = false;
			if (myEntity.getEntityType().equalsIgnoreCase("view")) {
				if (viewColumn.getPosition() == 1) {
					iskey = true;// 视图默认位置1为主键
				}
			}
			if ("PRI".equalsIgnoreCase(viewColumn.getColumnKey())) {
				iskey = true;
			}
			myField.setIskey(iskey);

			Boolean nullable = true;
			if (viewColumn.getNullable().equalsIgnoreCase("YES")) {
				nullable = false;
			}
			myField.setNullable(nullable);
			String type = "String";
			String typeInDb = viewColumn.getDataType();
			if (typeInDb.equalsIgnoreCase("varchar") || typeInDb.equalsIgnoreCase("char")) {
				type = "String";
			}
			if (typeInDb.equalsIgnoreCase("int") || typeInDb.equalsIgnoreCase("tinyint")) {
				type = "Integer";
			}
			if (typeInDb.equalsIgnoreCase("float")) {
				type = "Float";
			}
			if (typeInDb.equalsIgnoreCase("datetime") || typeInDb.equalsIgnoreCase("date") || typeInDb.equalsIgnoreCase("time")) {
				type = "Date";
				myEntity.setDateFlag(true);
			}
			if (typeInDb.equalsIgnoreCase("decimal")) {
				type = "BigDecimal";
				myEntity.setBigDecimalFlag(true);
			}
			if (typeInDb.equalsIgnoreCase("bit")) {
				type = "Boolean";
				if (!nullable) {
					type = "boolean";
				}
			}
			if (typeInDb.equalsIgnoreCase("bigint")) {
				type = "Long";
			}
			myField.setType(type);
			if (iskey) {
				myEntity.setIdType(type);
				myEntity.setIdName(myField.getFieldName());
			}
			//
			if (!myEntity.getEntityType().equalsIgnoreCase("view")&&(null == fieldComment || StringUtils.isBlank(fieldComment))) {
				throw new RuntimeException(viewColumn.getColumnName() + "字段请加入注释!");
			}
			if(null == fieldComment || StringUtils.isBlank(fieldComment)){
				fieldComment=	haloViewCommentService.getViewCommentByName(
						TableUtil.toHql(myEntity.getEntityName())+"."+viewColumn.getColumnName());
				  myField.setFieldComment(fieldComment);
			}
		
			myField.setViewColumn(viewColumn);
			myFileds.add(myField);
		}
		return myFileds;
	}

	public MyEntity getMyEntity(HaloMap parameter) {
		MyEntity myEntity = new MyEntity();
		myEntity = toEntity(parameter);
		List<MyField> myFields = toFields(parameter, myEntity);
		myEntity.setFields(myFields);
		return myEntity;
	}

	/**
	 *  TODO entityFlag true  实体将覆盖
	 * @param falg
	 */
	public void gen(boolean entityFlag) {
		// 获取配置
		File properties = FileUtils.getClassPath("com.ht.utils.junit", "jdbc.properties");
		PropertiesUtil propertiesUtil = new PropertiesUtil(properties);
		Map<String, Object> pro = propertiesUtil.loadProperties();
		Map<String, Object> proNew = new HashMap<String, Object>();
		for (Entry<String, ?> entry : pro.entrySet()) {
			if (entry.getKey().startsWith("codegen")) {
				proNew.put(StringUtils.substringAfter(entry.getKey(), "codegen."), entry.getValue());
			}
		}// 获得codegen属性
		String jdbcURL = propertiesUtil.getValue("jdbc.url");
		String dbName = null;
		if (jdbcURL.indexOf("?") != -1) {
			dbName = StringUtils.substringAfterLast(StringUtils.substringBefore(jdbcURL, "?"), "/");
		}// 获得数据库名
		MyEntity myEntity = getMyEntity(new HaloMap().set("tableName", propertiesUtil.getValue("codegen.tableName")).set("dbName:prm", dbName));

		Freemarker freemarker = new Freemarker();
		String now = DateUtils.format("yyyy年MM月dd日 HH:mm:ss ", new Date());
		HashMap<String, Object> dataMap = new HashMap<String, Object>().set("bean", myEntity).set("pro", proNew).set("now", now);
		File templateFolder = FileUtils.getClassPath("com.ht.codegen.template", "");
		File entityFile = FileUtils.getSrcPath(propertiesUtil.getValue("codegen.entityPath"), myEntity.getEntityName() + ".java");
		// 生成实体
		if (!entityFile.exists()||entityFlag) {
			freemarker.generateBytemplate(dataMap, templateFolder, "entity.ftl", entityFile);
			logger.info(myEntity.getEntityName() + "生成成功!!");
		} else {
			logger.warn(myEntity.getEntityName() + "已存在!!");
		}
		// 生成Dao
		File daoFile = FileUtils.getSrcPath(propertiesUtil.getValue("codegen.basePath") + ".dao", myEntity.getEntityName() + "Dao.java");
		if (!daoFile.exists()) {
			freemarker.generateBytemplate(dataMap, templateFolder, "dao.ftl", daoFile);
			logger.info(myEntity.getEntityName() + "Dao生成成功!!");
		} else {
			logger.warn(myEntity.getEntityName() + "Dao已存在!!");
		}
		//生成Service
		File iBaseServiceFile = FileUtils.getSrcPath(propertiesUtil.getValue("codegen.basePath") + ".service.base","I"+ myEntity.getEntityName() + "BaseService.java");
		File baseServiceFile = FileUtils.getSrcPath(propertiesUtil.getValue("codegen.basePath") + ".service.base.impl", myEntity.getEntityName() + "BaseServiceImpl.java");
		File iServiceFile = FileUtils.getSrcPath(propertiesUtil.getValue("codegen.basePath") + ".service","I"+ myEntity.getEntityName() + "Service.java");
		File serviceFile = FileUtils.getSrcPath(propertiesUtil.getValue("codegen.basePath") + ".service.impl", myEntity.getEntityName() + "ServiceImpl.java");
	//	if (!IBaseServiceFile.exists()) {
			freemarker.generateBytemplate(dataMap, templateFolder, "baseService.ftl", iBaseServiceFile);
			freemarker.generateBytemplate(dataMap, templateFolder, "baseServiceImpl.ftl", baseServiceFile);
			freemarker.generateBytemplate(dataMap, templateFolder, "service.ftl", iServiceFile);
			freemarker.generateBytemplate(dataMap, templateFolder, "serviceImpl.ftl", serviceFile);
			logger.info(myEntity.getEntityName() + "Service生成成功!!");
	//	}else{
		//	logger.warn(myEntity.getEntityName() + "Service已存在!!");
	//	}
	}
}
