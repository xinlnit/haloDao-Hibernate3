package com.ht.halo.hibernate3.utils;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;

public class MyUUID {
	public static String    create(){
	String uuid =	UUID.randomUUID().toString();
		return StringUtils.remove(uuid, "-");
		
	}
	
}
