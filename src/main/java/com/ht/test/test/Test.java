package com.ht.test.test;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.ht.halo.hibernate3.HaloMap;

public class Test {
    public static void main(String[] args) throws IllegalAccessException, InvocationTargetException {
     /*  System.out.println(new HaloMap().put("addColumn", "createtime")
    		   .put("name", "等等").put("addColumn", "1111"));
       System.out.println(new HaloMap().put("addColumn", "33")
    		   .put("name", "等等").put("addColumn1", "1111").addColumn("ddd"));
       System.out.println(new Gson().toJson("a#ss".split("\\#")));
       System.out.println(StringUtils.substringBefore("啥啥啥", "order by"));
    	Object value=new Object[]{"44","33"};
    	StringBuffer sb= new StringBuffer();
		if(value instanceof Object[]){
			for (Object obj :(Object[])value) {
				sb.append("'"+String.valueOf(obj)+"',");
			}
		}else{
			sb.append("'"+String.valueOf(value)+"'");
		}
       System.out.println(sb.toString());*/
       Map<String,Object> parameter= new HashMap<String, Object>();
     //  parameter.put("02","eee");
      // parameter.put("01","eee");
       
       System.out.println(new HaloMap(parameter).set("02", "eee").set("01", "eee"));
       
	}
}
