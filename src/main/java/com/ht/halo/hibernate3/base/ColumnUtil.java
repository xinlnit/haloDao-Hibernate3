package com.ht.halo.hibernate3.base;

public class ColumnUtil {
	public static String toEntity(String tableColName) {
		StringBuffer sb = new StringBuffer();
		boolean flag=false;
		for (int i = 0; i < tableColName.length(); i++) {
			char cur = tableColName.charAt(i);
			if (cur=='_') {
				flag=true;
				
			} else {
				if(flag){
					sb.append(Character.toUpperCase(cur));
					flag=false;
				}else{
					sb.append(Character.toLowerCase(cur));
					
				}
				
			}
		}
		//System.out.println(sb);
		return sb.toString();
	}
	public static String toTable(String entityColName) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < entityColName.length(); i++) {
			char cur = entityColName.charAt(i);
			if (Character.isUpperCase(cur)) {
				sb.append("_");
				sb.append(cur);
			} else {
				sb.append(cur);
			}
		}
		return sb.toString();
	}
}
