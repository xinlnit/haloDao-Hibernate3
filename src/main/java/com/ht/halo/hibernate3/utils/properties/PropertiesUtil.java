package com.ht.halo.hibernate3.utils.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import com.ht.halo.hibernate3.map.HashMap;
import com.ht.halo.hibernate3.utils.file.FileUtils;
import com.ht.halo.hibernate3.utils.md5.MD5Util;

public class PropertiesUtil {
     private Properties prop;
     private static Map<String,Object> fileTimeMap= new HashMap<String, Object>();
	 public  PropertiesUtil(File properties) {  
	    	long lastModifiedTime=properties.lastModified();
	    	if(lastModifiedTime!=0){
	    	String md5File=	MD5Util.getMD5(properties.getPath());
	    	if(null!=fileTimeMap.get(md5File)){
	    		OnceProp onceProp =	(OnceProp) fileTimeMap.get(md5File);
	    		if(onceProp.getLastModified()==lastModifiedTime){
	    			prop=onceProp.getProp();
	    		}else{
	    			prop=load(properties);
	    		}
	    	}else{
	    		prop=load(properties);
	    		fileTimeMap.put(MD5Util.getMD5(properties.getPath()), 
		    			new OnceProp().setLastModified(lastModifiedTime).setProp(prop));
	    	  }
	
	    	}else{
				throw new  RuntimeException("文件找不到!");
			}
	    
	    	
	    }
	 private Properties load(File properties){
		    prop= new Properties();
			FileInputStream fileInput;
			try {
				fileInput = new FileInputStream(properties);
				prop.load(fileInput);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return prop;
	 }
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public    Map<String,Object> loadProperties(File properties ){
		 Map<String, Object> map = null;
		   map = new HashMap<String, Object>((Map) prop); 
		return map;
	}
	public    String  getValue(String key){
		return prop.getProperty(key.trim());
	}
	public    Object  setValue(String key,String value){
		return prop.setProperty(key, value);
	}
	public    String  getValue(String key,String defaultValue){
		return prop.getProperty(key.trim(),defaultValue);
	}
	public    String  getHql(String key){
		return " "+getValue(key,"hallo")+" ";
	}
	public    String  getData(String key){
		return getValue(key,"hallo");
	}
	public static void main(String[] args) throws FileNotFoundException, IOException {
		System.out.println(System.currentTimeMillis());
          PropertiesUtil propertiesUtil= new PropertiesUtil(
        		  FileUtils.getClassPath("halo.hql", "fcy.properties"));
	    System.out.println("ZZZZZ::::"+propertiesUtil.getHql("charge.chargeReceivableDetail.ee"));
		System.out.println(System.currentTimeMillis());
		  PropertiesUtil propertiesUtil2= new PropertiesUtil(
        		  FileUtils.getClassPath("halo.hql", "fcy2.properties"));
	    System.out.println("ZZZZZ::::"+propertiesUtil2.getHql("charge.chargeReceivableDetail.ee"));
		System.out.println(System.currentTimeMillis());
	  
	}
}
