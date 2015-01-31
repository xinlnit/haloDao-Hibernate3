package com.ht.halo.hibernate3.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.SessionFactory;
import org.hibernate.hql.FilterTranslator;
import org.hibernate.hql.QueryTranslatorFactory;
import org.hibernate.impl.SessionFactoryImpl;

public class HibernateUtils {
	/**
	 * hql转sql
	 * 
	 * @param hql
	 * @return sql语句
	 */
	public static String hqlToSql(String hql,SessionFactory sessionFactory) {
		SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) sessionFactory;
		QueryTranslatorFactory queryTranslatorFactory = sessionFactoryImpl.getSettings().getQueryTranslatorFactory();
		FilterTranslator filterTranslator = queryTranslatorFactory.createFilterTranslator(hql, hql, Collections.EMPTY_MAP, sessionFactoryImpl);
		filterTranslator.compile(Collections.EMPTY_MAP, false);
		return filterTranslator.getSQLString();
	}
	private static String getPlaceholderSql(String hql,String sql){
		List<String>  placeholders= new ArrayList<String>() ;
		Matcher matcherHql=Pattern.compile("\\:([\\w]*)").matcher(hql);
	        while(matcherHql.find()){
	        	   placeholders.add(matcherHql.group());
	        }
	        Matcher matcherSql=Pattern.compile("\\?").matcher(sql);
	        int i=0;
	        while(matcherSql.find()){
	        	sql=sql.replace(matcherSql.group(),placeholders.get(i));
	            i++;
	        }
	        return sql;
	}
	/**
	 *  TODO 占位符方式转sql
	 * @param hql
	 * @param sessionFactory
	 * @return
	 */
	public static String hqlToSqlByPlaceholder(String hql,SessionFactory sessionFactory) {
		SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) sessionFactory;
		QueryTranslatorFactory queryTranslatorFactory = sessionFactoryImpl.getSettings().getQueryTranslatorFactory();
		FilterTranslator filterTranslator = queryTranslatorFactory.createFilterTranslator(hql, hql, Collections.EMPTY_MAP, sessionFactoryImpl);
		filterTranslator.compile(Collections.EMPTY_MAP, false);
		String sql=filterTranslator.getSQLString();
		return getPlaceholderSql(hql, sql);
	}
}
