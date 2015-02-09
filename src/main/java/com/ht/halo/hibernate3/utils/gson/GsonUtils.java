package com.ht.halo.hibernate3.utils.gson;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.ht.halo.hibernate3.utils.ConvertUtils;
import com.ht.halo.hibernate3.utils.gson.bean.GsonHelpBean;

/**
 * @ClassName: HtGson
 * @Description: TODO
 * @author fengchangyi
 * @date 2014-12-2 下午1:21:11
 */
public class GsonUtils {
	private static String DATEFORMAT = "dateFormat";
	private static String HIBERNATE = "hibernate";

	private static GsonHelpBean getCommonBuilder(Object... parameter) {
		GsonHelpBean gsonHelpBean = new GsonHelpBean();
		Object[] parameters = parameter;
		GsonMap gsonMap = new GsonMap();
		List<String> filedNameList = new ArrayList<String>();
		for (Object object : parameters) {
			if (object instanceof GsonMap) {
				gsonMap = (GsonMap) object;
			} else {
				filedNameList.add(String.valueOf(object));
			}
		}
		String[] fieldNames = filedNameList.toArray(new String[filedNameList.size()]);
		GsonBuilder gsonBuilder = new GsonBuilder();
		Boolean hiberanteFlag = true;
		if (null != gsonMap.get(HIBERNATE)) {
			hiberanteFlag = ConvertUtils.toBoolean(gsonMap.get(HIBERNATE));
		}
		if (hiberanteFlag) {
			gsonBuilder.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
		}
		String dateFormat = null;
		if (null != gsonMap.get(DATEFORMAT)) {
			dateFormat = String.valueOf(gsonMap.get(DATEFORMAT));
		}
		if (null != dateFormat) {
			gsonBuilder.setDateFormat(dateFormat);
		}
		gsonHelpBean.setGsonBuilder(gsonBuilder);
		gsonHelpBean.setFieldNames(fieldNames);
		return gsonHelpBean;
	}

	/**
	 * @Title: getGsonIn
	 * @Description: TODO 默认查询普通属性,其他关联实体属性,请写上字段名fieldNames
	 * @param fieldNames
	 * @return
	 */
	public static Gson getGsonIn(Object... parameter) {
		GsonHelpBean gsonHelpBean = getCommonBuilder(parameter);
		GsonBuilder gsonBuilder = gsonHelpBean.getGsonBuilder();
		String[] fieldNames = gsonHelpBean.getFieldNames();
		gsonBuilder.setExclusionStrategies(new GsonInclude().addExclusionList(fieldNames));
		return gsonBuilder.create();
	}

	/**
	 * @Title: getGsonInX
	 * @Description: TODO 默认不查询 只查询指定fieldNames的字段
	 * @param fieldNames
	 * @return
	 */
	public static Gson getGsonInX(Object... parameter) {
		GsonHelpBean gsonHelpBean = getCommonBuilder(parameter);
		GsonBuilder gsonBuilder = gsonHelpBean.getGsonBuilder();
		String[] fieldNames = gsonHelpBean.getFieldNames();
		gsonBuilder.setExclusionStrategies(new GsonIncludeX().addExclusionList(fieldNames));
		return gsonBuilder.create();
	}

	/**
	 * @Title: getGsonEx
	 * @Description: TODO 默认查询全部 排除查询指定fieldNames的字段
	 * @param fieldNames
	 * @return
	 */
	public static Gson getGsonEx(Object... parameter) {
		GsonHelpBean gsonHelpBean = getCommonBuilder(parameter);
		GsonBuilder gsonBuilder = gsonHelpBean.getGsonBuilder();
		String[] fieldNames = gsonHelpBean.getFieldNames();
		gsonBuilder.setExclusionStrategies(new GsonExclude().addExclusionList(fieldNames));
		return gsonBuilder.create();
	}

	/**
	 * TODO 美化json
	 * 
	 * @param str
	 * @return
	 */
	public static String format(String str) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonElement = jsonParser.parse(str);
		return gson.toJson(jsonElement);
	}
}
