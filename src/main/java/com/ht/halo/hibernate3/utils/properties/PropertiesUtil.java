package com.ht.halo.hibernate3.utils.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import jodd.props.Props;

import org.hibernate.util.PropertiesHelper;

import com.ht.halo.hibernate3.utils.file.FileUtils;

public class PropertiesUtil {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public  static  Map<String,Object> loadProperties(File properties ){
		 Properties prop = new Properties();
		 Map<String, Object> map = null;
		try {
			FileInputStream fileInput = new FileInputStream(properties);
			prop.load(fileInput);
		   map = new HashMap<String, Object>((Map) prop); 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	public static void main(String[] args) throws FileNotFoundException, IOException {
		Props p = new Props();
		p.load(FileUtils.getClassPath("halo.hql", "messages_zh_CN.properties"));

		String story = p.getValue("cms.function.assistant");
/*		Map<String,Object> map =
				PropertiesUtil.loadProperties(FileUtils.getClassPath("halo.hql", "messages_zh_CN.properties"));
	   for(Entry<String, Object> entry:map.entrySet()){
		    System.out.println(entry.getKey()+":"+entry.getValue());
	  }*/
	
	    System.out.println("ZZZZZ::::"+story);
		
	}
}
