package com.ht.halo.hibernate3.base;

import org.apache.commons.lang.StringUtils;

import com.ht.halo.hibernate3.exception.NotAllowTableColumnException;

public class TableUtil {
	public static String toEntity(String tableName) {
		StringBuffer sb = new StringBuffer();
		boolean flag=false;
		for (int i = 0; i < tableName.length(); i++) {
			char cur = tableName.charAt(i);
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
		return StringUtils.capitalize(sb.toString());
	}
	public static String toFiled(String colName) {
		StringBuffer sb = new StringBuffer();
		boolean flag=false;
		for (int i = 0; i < colName.length(); i++) {
			char cur = colName.charAt(i);
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
		return sb.toString();
	}
	public static String toTable(String entityColName) {
		StringBuffer sb = new StringBuffer();
		boolean flag=false;
		for (int i = 0; i < entityColName.length(); i++) {
			char cur = entityColName.charAt(i);
			if (cur=='_') {
					throw new NotAllowTableColumnException("不允许使用数据库字段!");
			}
			if(cur==':'){
				flag=true;
			}
			if(cur!=':'&&!Character.isLetter(cur)){
				flag=false;
			}
			if(flag){
				sb.append(cur);
				continue;
			}
			if (Character.isUpperCase(cur)) {
				sb.append("_");
				sb.append(Character.toLowerCase(cur));
			} else {
				sb.append(cur);
			}
		}
		return sb.toString();
	}
	public static void main(String[] args) {
		System.out.println(TableUtil.toTable(" userIdName =:userName and tableId=:UUUU"));
		System.out.println(TableUtil.toFiled("receivable_detail_id"));
	}
}
