package com.ht.halo.hibernate3.gson;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class GsonInclude  implements ExclusionStrategy{
	private static final Log logger = LogFactory.getLog(GsonInclude.class);
 private Set<String> exclusionSet= new  HashSet<String>();
	public boolean shouldSkipClass(Class<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	public  GsonInclude addExclusionList(String...  fieldNames){
		 for (String field : fieldNames) {
			 exclusionSet.add(field);
		}	
		 return this;
	}
    public GsonInclude  addExclusion(String fieldName){
    	exclusionSet.add(fieldName);
    	return this;
    }
	public boolean shouldSkipField(FieldAttributes field) {
		
		if(field.getDeclaredType().toString().endsWith("String")){
			return false;
		}
		if(field.getDeclaredType().toString().endsWith("Integer")){
			return false;
		}
		if(field.getDeclaredType().toString().endsWith("Double")){
			return false;
		}
		if(field.getDeclaredType().toString().endsWith("Boolean")){
			return false;
		}
		if(field.getDeclaredType().toString().endsWith("Date")){
			return false;
		}
		if(field.getDeclaredType().toString().endsWith("BigDecimal")){
			return false;
		}
		if(field.getDeclaredType().toString().endsWith("long")){
			return false;
		}
		if(field.getDeclaredType().toString().endsWith("int")){
			return false;
		}
		if(field.getDeclaredType().toString().endsWith("Float")){
			return false;
		}
		for (String exclusionStr: exclusionSet) {
		if(field.getName().equals(exclusionStr)){
			  return false;
		      }
		}
		logger.info(field.getName());
		logger.info(field.getDeclaredType().toString());
		// TODO Auto-generated method stub
		return true;
	}
	public static void main(String[] args) {
		/*	for (String exclusionStr: exclusionSet) {
		String fieldName = field.getDeclaredType().toString();
		String[]  strs =exclusionStr.split("#");
		int i=0;
		for (String string : strs) {
			if(fieldName.indexOf(string)!=-1){
				i++;
			}
		}	
		if(strs.length==i){
			return false;
		}
	}*/
	}

}
