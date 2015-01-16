package com.ht.test.other;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.ht.halo.hibernate3.map.MyHashMap;

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
}
