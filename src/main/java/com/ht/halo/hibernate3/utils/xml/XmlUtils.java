package com.ht.halo.hibernate3.utils.xml;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.ht.halo.hibernate3.map.HashMap;
import com.ht.halo.hibernate3.utils.file.FileUtils;

public class XmlUtils {
	private static String HQLS="hqls";
	private static String HQL="hql";
	private static String DATAS="datas";
	private static String DATA="data";
	private static String SQLS="sqls";
	private static String SQL="sql";
	private static String ID="id";
	private Document document;
	private static Map<String, Object> fileTimeMap = new HashMap<String, Object>();

	public XmlUtils(File xml){
		long lastModifiedTime=xml.lastModified();
    	if(lastModifiedTime!=0){
    	String md5File=xml.getPath();	//MD5Util.getMD5(xml.getPath());
    	if(null!=fileTimeMap.get(md5File)){
    		OnceXml onceXml =	(OnceXml) fileTimeMap.get(md5File);
    		if(onceXml.getLastModified()==lastModifiedTime){
    			document=onceXml.getDocument();
    		}else{
    			document=load(xml);
    		}
    	}else{
    		 document=load(xml);
    		 fileTimeMap.put(md5File,
	    	 new OnceXml().setLastModified(lastModifiedTime).setDocument(document));
    	  }

    	}else{
			throw new  RuntimeException("xml文件找不到!");
		}
	}

	public Document load(File xml) {
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(xml);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;
	}
	@SuppressWarnings("unchecked")
	public String getHql(String id){
		  Element rootElement = document.getRootElement();    
	        Element hqlsElement= rootElement.element(HQLS);
	        if (null==hqlsElement) {
				return null;
			}
	        List<Element> hqlElements=    hqlsElement.elements(HQL);
	        for (Element element : hqlElements) {
	           Attribute attribute=    element.attribute(ID);
	           if(attribute.getValue().equals(id)){
	        	   return element.getTextTrim();
	           }
			}
			return null;
	}
	@SuppressWarnings("unchecked")
	public String getData(String id){
		  Element rootElement = document.getRootElement();    
	        Element hqlsElement= rootElement.element(DATAS);
	        if (null==hqlsElement) {
				return null;
			}
	        List<Element> hqlElements=    hqlsElement.elements(DATA);
	        for (Element element : hqlElements) {
	           Attribute attribute=    element.attribute(ID);
	           if(attribute.getValue().equals(id)){
	        	   return element.getTextTrim();
	           }
			}
			return null;
	}
	@SuppressWarnings("unchecked")
	public String getSql(String id){
		  Element rootElement = document.getRootElement();    
	        Element hqlsElement= rootElement.element(SQLS);
	        if (null==hqlsElement) {
				return null;
			}
	        List<Element> hqlElements=    hqlsElement.elements(SQL);
	        for (Element element : hqlElements) {
	           Attribute attribute=    element.attribute(ID);
	           if(attribute.getValue().equals(id)){
	        	   return element.getTextTrim();
	           }
			}
			return null;
	}
	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
		XmlUtils xmlUtils = new XmlUtils(FileUtils.getClassPath("halo", "Test.xml"));
		System.out.println(xmlUtils.getData("aa"));
		System.out.println(xmlUtils.getHql("aa"));
		System.out.println(System.currentTimeMillis());
		XmlUtils xmlUtils2 = new XmlUtils(FileUtils.getClassPath("halo", "Test.xml"));
		System.out.println(xmlUtils2.getSql("updateById"));
		System.out.println(System.currentTimeMillis());
	}
	

}