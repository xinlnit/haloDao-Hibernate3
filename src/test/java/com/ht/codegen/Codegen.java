package com.ht.codegen;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.ht.codegen.utils.DateUtils;
import com.ht.codegen.utils.PropertiesUtil;
import com.ht.codegen.utils.freemarker.Freemarker;
import com.ht.halo.hibernate3.feemarker.UUIDDirective;

/**
 * 
 * @ClassName: Codegen
 * @Description: TODO 模块生成器
 * @author fengchangyi
 * @date 2014-5-26 下午4:26:47
 */
public class Codegen {
//	private final String encoding = "UTF-8";
	private final String proFileName = "htcode.properties";
	private final static String entityStr = "entity";
	private final static String entityPath = "entity_path";
	private final static String baseStr = "base";
	private final static String templateDir = "template_dir";
	private final static String isAction = "is_action";
	private final static String isService = "is_service";
	private final static String isDao = "is_dao";
	private final static String isView = "is_view";
	private final static String suffix = ".ftl";
	private static final String daoTplStr = "dao" + suffix;
	private static final String serviceTplStr = "service" + suffix;
	private static final String serviceImplTplStr = "service_impl" + suffix;
	private static final String actionTplStr = "action" + suffix;
	private static final String viewTplStr = "view" + suffix;
	private static final String viewJSTplStr = "viewJS" + suffix;
	private String packName;
	private File daoFile;
	private File serviceFile;
	private File serviceImplFile;
	private File actionFile;
	private File viewFile;
	private File viewJSFile;
	private Map<String, Object> data = new HashMap<String, Object>();
	public Codegen(String packName) {
		this.packName = packName;
	}
	private String getFilePath(String packageName, String name) {
		String path = packageName.replaceAll("\\.", "/");
		return "src/" + path + "/" + name;
	}

	private Map<String, Object> loadProperties() {
		PropertiesUtil propertiesUtil = new PropertiesUtil(new File(getFilePath(packName, proFileName)));
		return propertiesUtil.loadProperties();
	}

	@SuppressWarnings("rawtypes")
	private void dataExt() {
		String entityUp = data.get(entityStr).toString();
		String entityLow = entityUp.substring(0, 1).toLowerCase() + entityUp.substring(1);
		data.put("entity_low", entityLow);
		data.put("now", DateUtils.format("yyyy年MM月dd日 HH:mm:ss ", new Date()));
		data.put("entity_full", data.get(entityPath).toString()+"."+entityUp);
		String entityPack=data.get(entityPath).toString()+"."+entityUp;
		try {
			Class bean=Class.forName(entityPack);
			data.put("bean", bean);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		data.put("uuid", new UUIDDirective());
		data.put("typeShort", new TypeShortDirective());
		data.put("fieldInfo", new FieldInfoDirective());
	}
	private void prepareFile() {
		String basePath = data.get(baseStr).toString();
		String entityString = data.get(entityStr).toString();
		String entityStringCap=StringUtils.capitalize(entityString);
		String daoFilePath = getFilePath(basePath + ".dao", entityStringCap + "Dao.java");
		daoFile = new File(daoFilePath);
		String serviceFilePath = getFilePath(basePath + ".service", "I" + entityString + "Service.java");
		serviceFile = new File(serviceFilePath);
		String serviceImplFilePath = getFilePath(basePath + ".service.impl", entityString + "ServiceImpl.java");
		serviceImplFile = new File(serviceImplFilePath);
		String actionFilePath = getFilePath(basePath + ".web.action", entityString + "Action.java");
		actionFile = new File(actionFilePath);
		String viewFilePath = getFilePath(basePath + ".web.view2", entityString + "Manage.view.xml");
		viewFile = new File(viewFilePath);
		String viewJSFilePath = getFilePath(basePath + ".web.view2", entityString + "Manage.js");
		viewJSFile = new File(viewJSFilePath);
	}

	private void tplToFile(File fileTo, String templateName) throws IOException {
		if (!fileTo.exists()) {
			Freemarker freemarker = new Freemarker();
			File templateFolder = new File(getFilePath(data.get(templateDir).toString(), ""));
			freemarker.generateBytemplate(data, templateFolder, templateName, fileTo);
			System.out.println("生成" + fileTo.getPath() + "成功!");
		}
	}

	private void writeFile() {   
		try {
			if ("true".equals(data.get(isDao).toString())) {
				tplToFile(daoFile, daoTplStr);
			}
			if ("true".equals(data.get(isService).toString())) {
				tplToFile(serviceFile, serviceTplStr);
				tplToFile(serviceImplFile, serviceImplTplStr);
			}
			if ("true".equals(data.get(isAction).toString())) {
				tplToFile(actionFile, actionTplStr);
			}
			if ("true".equals(data.get(isView).toString())) {
				tplToFile(viewFile, viewTplStr);
				tplToFile(viewJSFile, viewJSTplStr);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void generate() throws IOException {	
		data.putAll(loadProperties());
		String entity = null;
		if (!StringUtils.isBlank(data.get(entityStr).toString())) {
			entity = data.get(entityStr).toString();
		}
		if (null != entity) {
			//File entityFile = new File(getFilePath(data.get(entityPath).toString(), entity + ".java"));
				dataExt();
				prepareFile();
				writeFile();
		} else {
			File entityFiles = new File(getFilePath(data.get(entityPath).toString(), ""));
			for (File entityFile : entityFiles.listFiles()) {
				String entityStr = StringUtils.substringBefore(entityFile.getName(), ".java");
						data.put("entity", entityStr);
						dataExt();
						prepareFile();
						writeFile();
			}
		}
		System.out.println("代码生成结束......");
	}


}
