package com.ht.halo.hibernate3.utils.tpl;

import java.io.File;
import java.util.Map;

public interface ITplUtils {
	public String tpl(Map<String, Object> data, String tplStr);
	public String generateString(Map<String, Object> data, String tplStr);
	public String generateString(Map<String, Object> data, File templateDirectory, String templateName);
	public void generateBytemplate(Map<String, Object> data, File templateDirectory, String templateName, File targetFile);
   
}
