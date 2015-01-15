package com.ht.test.other;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.ConvertUtils;
import org.junit.Test;

public class OtherTest{

	@Test
	public void testConvert() {
	    String str="我是{0},我来自{1},今年{2}岁";
	       String[] arr={"中国人","北京","22"};
	       System.out.println(fillStringByArgs(str, arr));
	}
    private static String fillStringByArgs(String str,String[] arr){
        Matcher m=Pattern.compile("\\{(\\d)\\}").matcher(str);
        while(m.find()){
            str=str.replace(m.group(),arr[Integer.parseInt(m.group(1))]);
        }
        return str;
    }
}
