package com.ht.halo.hibernate3.utils.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @ClassName: HtGson
 * @Description: TODO
 * @author fengchangyi
 * @date 2014-12-2 下午1:21:11
 */
public class HtGson {
	/**
	 * @Title: getGsonIn
	 * @Description: TODO 默认查询普通属性,其他关联实体属性,请写上字段名fieldNames
	 * @param fieldNames
	 * @return
	 */
	public static Gson getGsonIn(String... fieldNames){
		Gson gson=	new GsonBuilder()
		.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY)
		.setExclusionStrategies(new GsonInclude().addExclusionList(fieldNames))
		.create();
		return gson;
	}
	/**
	 * @Title: getGsonInX
	 * @Description: TODO 默认不查询 只查询指定fieldNames的字段
	 * @param fieldNames
	 * @return
	 */
	public static Gson getGsonInX(String... fieldNames){
		Gson gson=	new GsonBuilder()
		.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY)
		.setExclusionStrategies(new GsonIncludeX().addExclusionList(fieldNames))
		.create();
		return gson;
	}
	
	/**
	 * @Title: getGsonEx
	 * @Description: TODO 默认查询全部 排除查询指定fieldNames的字段
	 * @param fieldNames
	 * @return
	 */
	public static Gson getGsonEx(String... fieldNames){
		Gson gson=	new GsonBuilder()
		.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY)
		.setExclusionStrategies(new GsonExclude().addExclusionList(fieldNames))
		.create();
		return gson;
	}
}
