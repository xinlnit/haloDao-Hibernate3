package com.ht.halo.base;

import java.io.File;
import java.util.Map;

import com.ht.halo.hibernate3.map.HashMap;
import com.ht.halo.hibernate3.utils.StringUtils;
import com.ht.halo.hibernate3.utils.file.FileUtils;


public class HaloViewBase  extends Base{
	protected static final String SPACE = "\u0020";
	protected static final char SPACECHAR = '\u0020';
	public static final String DATA = "data";// haloView中的模板数据
	public static final String ADDVIEW = "addView";
	public static final String ADDXML = "addXml";
	protected static final String HALO= "Halo";
	
	public static Map<String,String> xmlMap=getXmlMap();
	private static Map<String,String> resultMap;
	private static Map<String,String> getXmlMap(){
		getAllXmls(FileUtils.getClassPath("halo", ""));
		return resultMap;
	}
	private static   void getAllXmls(File folder){
		if(null==resultMap){
			resultMap= new HashMap<String, String>();
		}
		File[]  files = folder.listFiles();
		for (File file : files) {
			if(file.isDirectory()){
				if(!file.getName().equals("config")){
				getAllXmls(file);
				}
			}
			if(file.isFile()){
				String fileName=file.getName();
				if(fileName.endsWith(".xml")){
					
				fileName =StringUtils.substringBeforeLast(fileName, ".xml");
				String filePath=file.getPath();
				filePath=StringUtils.substringAfterLast(filePath, File.separator+"halo");
				filePath=StringUtils.substringBeforeLast(filePath,File.separator);
				filePath = filePath.replaceAll("\\"+File.separator, ".");
				if(filePath.endsWith(".xml")){
					filePath="";
				}
				if(null!=resultMap.get(fileName)){
					throw new RuntimeException("包"+resultMap.get(fileName)+"下存在同名xml!请改名!");
				}
				resultMap.put(fileName, filePath);
				}
			}
		}
	}
	public static void main(String[] args) {

		System.out.println(HaloViewBase.xmlMap);
	}
	
}
