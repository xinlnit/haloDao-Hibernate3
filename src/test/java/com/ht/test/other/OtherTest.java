package com.ht.test.other;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import com.ht.halo.hibernate3.map.MyHashMap;
import com.ht.halo.hibernate3.utils.file.FileUtils;

public class OtherTest{

	@Test
	public void testConvert() {
	    String str="我是{test.HaloView},我来自{133},今年{2}岁";
	    MyHashMap myHashMap = 
	    		new MyHashMap().set("test.HaloView", "中国人").set("133", "北京")
	    		;
	       System.out.println(fillStringByMap(str, myHashMap));
	}
    private static String fillStringByMap(String str,Map<String,Object> data){
        Matcher m=Pattern.compile("\\{([\\w\\.]*)\\}").matcher(str);
        while(m.find()){
        	   System.out.println(m.group());
        	   String group=m.group();
        	   group= group.replaceAll("\\{|\\}", "");
        	   System.out.println(group);
            str=str.replace(m.group(),String.valueOf(data.get(group)));
        }
        return str;
    }
    @Test
    public void testXml() throws DocumentException{
    	  // 创建saxReader对象  
    	System.out.println(System.currentTimeMillis());
        SAXReader reader = new SAXReader();  
        // 通过read方法读取一个文件 转换成Document对象 ;          
        Document document = reader.read(FileUtils.getClassPath("halo", "Test.xml"));  
    	System.out.println(System.currentTimeMillis());
        //获取根节点元素对象  
        Element rootElement = document.getRootElement();    
        System.out.println("根节点名称：" + rootElement.getName());//获取节点的名称  
        System.out.println("根节点有多少属性：" + rootElement.attributeCount());//获取节点属性数目  
        System.out.println("根节点id属性的值：" + rootElement.attributeValue("id"));//获取节点的属性id的值
        Element hqlsElement= rootElement.element("hqls");
        System.out.println(hqlsElement.getName());
        System.out.println(hqlsElement.getTextTrim());
        List<Element> hqlElements=    hqlsElement.elements("hql");
        for (Element element : hqlElements) {
     Attribute a=    element.attribute("id");
     System.out.println(a.getName());
     System.out.println(a.getValue());
        	  System.out.println(element.getName());
        
              System.out.println(element.getTextTrim());
		}
        //遍历所有的元素节点  
       // listNodes(node);  
    	System.out.println(System.currentTimeMillis());
    
    }
    
}
