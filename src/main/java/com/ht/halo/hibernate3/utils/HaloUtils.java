package com.ht.halo.hibernate3.utils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ht.halo.base.Base;
import com.ht.halo.hibernate3.map.HashMap;

public class HaloUtils extends Base {

	/**
	 *  获取片段占位符 并赋值 
	 * @param hqlSnippet
	 * @param value
	 * @return
	 */
	public static Map<String, Object> getHqlSnippetMap(String hqlSnippet, Object value) {
		Object[] newValue = null;
		if (value instanceof Object[]) {
			newValue = (Object[]) value;
		} else {
			newValue = new Object[] { value };
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Matcher matcherHql = Pattern.compile("\\:([\\w]*)").matcher(hqlSnippet);
		int i = 0;
		while (matcherHql.find()) {
			String key = matcherHql.group().substring(1);
			map.put(key, newValue[i]);
			i++;

		}
		return map;
	}

	public static void main(String[] args) {
		Map<String, Object> map = getHqlSnippetMap("and houseId =:houseId and 1=?1 and a=e_q  ", 7);
		System.out.println(map);
	}

}
