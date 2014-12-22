package com.ht.halo.hibernate3.gson;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class GsonIncludeX  implements ExclusionStrategy{
	private static final Log logger = LogFactory.getLog(GsonIncludeX.class);
 private Set<String> exclusionSet= new  HashSet<String>();
	public boolean shouldSkipClass(Class<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	public  GsonIncludeX addExclusionList(String...  fieldNames){
		 for (String field : fieldNames) {
			 exclusionSet.add(field);
		}	
		 return this;
	}
    public GsonIncludeX  addExclusion(String fieldName){
    	exclusionSet.add(fieldName);
    	return this;
    }
	public boolean shouldSkipField(FieldAttributes field) {
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

	}

}
