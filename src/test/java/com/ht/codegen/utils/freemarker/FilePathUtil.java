package com.ht.codegen.utils.freemarker;

public class FilePathUtil {
	public  static String getClassPath(String packageName, String name) {
		String path = packageName.replaceAll("\\.", "/");
		return "src/" + path + "/" + name;
	}
}
