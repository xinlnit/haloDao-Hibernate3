package com.ht.codegen.utils;

import java.util.Properties;

public class OnceProp {
     private long lastModified;
     private Properties prop;
	public long getLastModified() {
		return lastModified;
	}
	public OnceProp setLastModified(long lastModified) {
		this.lastModified = lastModified;
		return this;
	}
	public Properties getProp() {
		return prop;
	}
	public OnceProp setProp(Properties prop) {
		this.prop = prop;
		return this;
	}
     
}
