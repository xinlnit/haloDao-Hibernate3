package com.ht.halo.hibernate3.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils extends org.apache.commons.lang.StringUtils{
	public static boolean isEnglish(String checkValue) {
		String el = "^[A-Za-z]+$";
		Pattern p = Pattern.compile(el);
		Matcher m = p.matcher(checkValue);
		return m.matches();
	}
	public static void main(String[] args) {
		System.out.println(isEnglish("ww Q"));
	}
}
