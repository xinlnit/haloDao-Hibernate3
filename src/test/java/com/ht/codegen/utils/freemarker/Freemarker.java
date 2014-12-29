package com.ht.codegen.utils.freemarker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class Freemarker {
	private String tplSuffix=".ftl";
	private String toSuffix;
	private String defaultCharacter = "UTF-8";
	private Configuration cfg;

	public Freemarker() {
		cfg = new Configuration();
		cfg.setDefaultEncoding(defaultCharacter);
		 cfg.setTagSyntax(Configuration.AUTO_DETECT_TAG_SYNTAX); 
	}

	public String getTplSuffix() {
		return tplSuffix;
	}

	public void setTplSuffix(String tplSuffix) {
		this.tplSuffix = tplSuffix;
	}

	public String getToSuffix() {
		return toSuffix;
	}

	public void setToSuffix(String toSuffix) {
		this.toSuffix = toSuffix;
	}

	/**
	 * @Title: generateBytemplate
	 * @Description: TODO 根据模板生成文件
	 * @param data
	 * @param templateDirectory
	 * @param templateName
	 * @param targetFile
	 */
	public void generateBytemplate(Map<String, Object> data, File templateDirectory, String templateName, File targetFile) {
		try {
			cfg.setDirectoryForTemplateLoading(templateDirectory);
			Template template = cfg.getTemplate(templateName, defaultCharacter);
			template.setEncoding(defaultCharacter);
			createFolder(targetFile);
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile)));
			template.process(data, out);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @Title: createFolder
	 * @Description: TODO 生成文件夹
	 * @param targetFile
	 */
	private void  createFolder(File targetFile){
		String path=targetFile.getAbsolutePath();
		String folder=	 StringUtils.substringBefore(path, targetFile.getName());
		File folderFile=new File(folder);
			if (!folderFile.exists()) {
				folderFile.mkdirs();
			}
	}
	  /**
	 * @Title: generateString 
	 * @Description: TODO 根据模板生成String字符串 :替代拼接字符串 可维护性强
	 * @param data
	 * @param templateDirectory
	 * @param templateName
	 * @return
	 */
	public  String generateString(
	           Map<String, Object> data,  File templateDirectory, String templateName) {
		  String result = null;
		  try { 
	            cfg.setDirectoryForTemplateLoading(templateDirectory); 
	            Template template = cfg.getTemplate(templateName, defaultCharacter);
	            template.setEncoding(defaultCharacter);  
	            StringWriter out = new StringWriter(); 
	            template.process(data, out);  
	            out.flush();  
	            result= out.toString();
	            out.close(); 
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
		  return result;
	  }
	public static void main(String[] args) {
          Freemarker freemarker =new Freemarker();
         File file1=new File(FilePathUtil.getClassPath("com.ht.ourally.utils.test",""));
         File file2=new File(FilePathUtil.getClassPath("com.ht.ourally.utils.test","2.txt"));
         Map<String,Object> data=new HashMap<String, Object>();
         data.put("entity", "ChargeView");
         data.put("model", "啥啥啥");
          freemarker.generateBytemplate(data, file1, "1.ftl", file2);
	}

}
