package com.ht.halo.hibernate3;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ht.halo.annotations.Halo;
import com.ht.halo.dao.IHaloViewDao;
import com.ht.halo.hibernate3.base.Assert;
import com.ht.halo.hibernate3.base.ColumnToBean;
import com.ht.halo.hibernate3.base.ColumnToMap;
import com.ht.halo.hibernate3.base.Page;
import com.ht.halo.hibernate3.base.TableUtil;
import com.ht.halo.hibernate3.bean.ColumnWithCondition;
import com.ht.halo.hibernate3.bean.SqlWithParameter;
import com.ht.halo.hibernate3.map.MyHashMap;
import com.ht.halo.hibernate3.utils.DateUtils;
import com.ht.halo.hibernate3.utils.MyUUID;
import com.ht.halo.hibernate3.utils.StringUtils;
import com.ht.halo.hibernate3.utils.file.FileUtils;
import com.ht.halo.hibernate3.utils.tpl.ITplUtils;
import com.ht.halo.hibernate3.utils.tpl.freemarker.FreemarkerUtils;
import com.ht.halo.hibernate3.utils.xml.XmlUtils;

/**
 *  基于haloView视图sql的Dao层
 * @author fengchangyi@haitao-tech.com
 * @date 2015-1-21 下午8:04:14
 * @param <T>
 */
public class HaloViewDao<T> implements IHaloViewDao<T>{
	private static final Log logger = LogFactory.getLog(HaloViewDao.class);
	private static final String SPACE = "\u0020";
	private static final char SPACECHAR = '\u0020';
	private static final String DATA = "data";// haloView中的模板数据
	public static final String VIEWID = "viewId";
	private static final String HALO= "Halo";
	
	protected SessionFactory sessionFactory;

	@Autowired(required = false)
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	private Session getSession() {
		return getSessionFactory().getCurrentSession();
	}

	protected Class<T> entityType = getEntityType();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Class<T> getEntityType() {
		Class cl = getClass();
		Class<T> resultType = null;
		java.lang.reflect.Type superType = cl.getGenericSuperclass();
		if (superType instanceof ParameterizedType) {
			java.lang.reflect.Type[] paramTypes = ((ParameterizedType) superType).getActualTypeArguments();
			if (paramTypes.length > 0) {
				resultType = (Class<T>) paramTypes[0];
			} else {
				logger.warn("Can not determine EntityType for class [" + cl.getSimpleName() + "].");
			}
		} else {
			logger.warn("[" + cl.getSimpleName() + "] is not a parameterized type.");
		}
		return resultType;
	}

	private String CheckSpace(String value) {
		if(value.indexOf(SPACE)!=-1){
			throw new RuntimeException("不允许包含空格!");
		}
		if (value.startsWith("_")) {
			value = value.substring(1, value.length());
		}
		return value;
	}

	private String replaceNum(String value) {
		return StringUtils.replaceEach(value, HaloDao.NUMS, HaloDao.NUMREPLACE);
	}

	private String filterValue(String value) {
		return replaceNum(CheckSpace(value));
	}

	private StringBuffer getColumn(StringBuffer column, Object value) {
		if (value instanceof String) {
			String valueStr = filterValue((String) value);
			column.append(String.format(" %s as %s ,", valueStr, valueStr));
		}
		if (value instanceof String[]) {
			for (String str : (String[]) value) {
				String valueStr = filterValue(str);
				column.append(String.format(" %s as %s ,", valueStr, valueStr));
			}
		}
		return column;
	}

	/**
	 * @Title: getOrder
	 * @Description: TODO 获得orderBy语句
	 * @param value
	 * @return
	 */

	private StringBuffer getOrder(StringBuffer order, Object value) {
		if (value instanceof String) {
			String orderValue = filterValue((String) value);
			if (orderValue.endsWith(HaloDao.MYSPACE + HaloDao.HQL)) {
				String hqlValue = getHqlSnippet(StringUtils.substringBefore(orderValue,HaloDao.MYSPACE + HaloDao.HQL));
				order.append(hqlValue);
			} else {
				order.append(orderValue.replace(HaloDao.MYSPACECHAR, SPACECHAR)).append(",");
			}
		}
		if (value instanceof String[]) {
			for (String str : (String[]) value) {
				String orderValue = filterValue(str);
				if (orderValue.endsWith(HaloDao.MYSPACE + HaloDao.HQL)) {
					String hqlValue = getHqlSnippet(StringUtils.substringBefore(orderValue, HaloDao.MYSPACE + HaloDao.HQL));
					order.append(hqlValue);
				} else {
					order.append(orderValue.replace(HaloDao.MYSPACECHAR, SPACECHAR)).append(",");
				}
			}
		}
		return order;
	}

	private StringBuffer getGroup(StringBuffer group, Object value) {
		if (value instanceof String) {
			group.append((filterValue((String) value))).append(",");
		}
		if (value instanceof String[]) {
			for (String str : (String[]) value) {
				group.append((filterValue(str))).append(",");
			}
		}
		return group;
	}

	private String getDataSnippet(String dataId) {
		String entityName = this.entityType.getSimpleName();
		File xmlPath = FileUtils.getClassPath(HaloDao.HALOPACH + getPosition(), entityName + ".xml");
		XmlUtils xmlUtils = new XmlUtils(xmlPath);
		String data = xmlUtils.getData(dataId);
		return data;
	}
	private String getHqlSnippet(String value) {
		String entityName = this.entityType.getSimpleName();
		File xmlPath = FileUtils.getClassPath(HaloDao.HALOPACH + getPosition(), entityName + ".xml");
		XmlUtils xmlUtils = new XmlUtils(xmlPath);
		String hql = xmlUtils.getHql(value);
		return hql;
	}
	private Object getDate(ColumnWithCondition columnWithCondition) {
		Object value = columnWithCondition.getValue();
		try {
			if (null != columnWithCondition.getFormate()) {
				value = DateUtils.parse(columnWithCondition.getFormate(), String.valueOf(value));
			} else {
				value = DateUtils.parseDate(String.valueOf(value), HaloDao.PATTERN);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return value;

	}

	/**
	 * 扩展日期条件. 1.daylike,monthlike 当天,当月 2.dayge dayle 从某天到某天
	 * 
	 * @param extCondtion
	 * @param like
	 * @param addOne
	 * @param columnWithCondition
	 * @return ColumnWithCondition
	 */
	private ColumnWithCondition extDateCondition(String extCondtion, String like, boolean addOne, ColumnWithCondition columnWithCondition) {

		if ("day".equals(extCondtion) || "dd".equals(extCondtion)) {
			Date date = (Date) getDate(columnWithCondition);
			if (addOne) {
				date = DateUtils.addDays(date, 1);
				columnWithCondition.setCondition("<");
			}
			columnWithCondition.setDirectValue("'" + DateUtils.format("yyyy-MM-dd", date) + like + "'");
			return columnWithCondition;
		}
		if ("month".equals(extCondtion) || "MM".equals(extCondtion)) {
			Date date = (Date) getDate(columnWithCondition);
			if (addOne) {
				date = DateUtils.addMonths(date, 1);
				columnWithCondition.setCondition("<");
			}
			columnWithCondition.setDirectValue("'" + DateUtils.format("yyyy-MM", date) + like + "'");
			return columnWithCondition;
		}
		if ("year".equals(extCondtion) || "yyyy".equals(extCondtion)) {
			Date date = (Date) getDate(columnWithCondition);
			if (addOne) {
				date = DateUtils.addYears(date, 1);
				columnWithCondition.setCondition("<");
			}
			columnWithCondition.setDirectValue("'" + DateUtils.format("yyyy", date) + like + "'");
			return columnWithCondition;
		}
		if ("hour".equals(extCondtion) || "HH".equals(extCondtion)) {
			Date date = (Date) getDate(columnWithCondition);
			if (addOne) {
				date = DateUtils.addHours(date, 1);
				columnWithCondition.setCondition("<");
			}
			columnWithCondition.setDirectValue("'" + DateUtils.format("yyyy-MM-dd HH", date) + like + "'");
			return columnWithCondition;
		}
		if ("minute".equals(extCondtion) || "mm".equals(extCondtion)) {
			Date date = (Date) getDate(columnWithCondition);
			if (addOne) {
				date = DateUtils.addMinutes(date, 1);
				columnWithCondition.setCondition("<");
			}
			columnWithCondition.setDirectValue("'" + DateUtils.format("yyyy-MM-dd HH:mm", date) + like + "'");
			return columnWithCondition;
		}

		if ("second".equals(extCondtion) || "ss".equals(extCondtion)) {
			Date date = (Date) getDate(columnWithCondition);
			if (addOne) {
				date = DateUtils.addSeconds(date, 1);
				columnWithCondition.setCondition("<");
			}
			columnWithCondition.setDirectValue("'" + DateUtils.format("yyyy-MM-dd HH:mm:ss", date) + like + "'");
			return columnWithCondition;
		}

		return columnWithCondition;
	}

	/**
	 * 扩展like条件. 1.全模糊5like5,左模糊5like
	 * 
	 * @param extCondtion
	 * @param value
	 * @param columnWithCondition
	 * @return ColumnWithCondition
	 */
	private ColumnWithCondition extLikeCondition(String extCondtion, Object value, ColumnWithCondition columnWithCondition) {
		if ("".equals(extCondtion)) {// 默认左模糊查询
			String valueString = String.valueOf(value);
			if (valueString.indexOf("%") == -1) {
				value = valueString + "%";
			}
			columnWithCondition.setType("string");
			columnWithCondition.setValue(value);
			columnWithCondition.setTempFlag(false);
			return columnWithCondition;
		}
		if ("%".equals(extCondtion)) {// 右模糊查询 5like
			String valueString = String.valueOf(value);
			if (valueString.indexOf("%") == -1) {
				value = "%" + valueString;
			}
			columnWithCondition.setType("string");
			columnWithCondition.setValue(value);
			columnWithCondition.setTempFlag(false);
			return columnWithCondition;
		}
		if ("%%".equals(extCondtion)) {// 模糊查询 5like5
			String valueString = String.valueOf(value);
			if (valueString.indexOf("%") == -1) {
				value = "%" + valueString + "%";
			}
			columnWithCondition.setType("string");
			columnWithCondition.setValue(value);
			columnWithCondition.setTempFlag(false);
			return columnWithCondition;
		}

		return columnWithCondition;
	}

	private String getGenColumnName(String str) {
		str = str.split("\\" + HaloDao.FORMATESPT)[0];
		str = StringUtils.replaceEach(str, HaloDao.NUMREPLACE, HaloDao.NUMREPLACELETTER);
		return str;
				//StringUtils.replaceEach(str, new String[] { ">=", ">", "<=", "<", "!=", "=" }, new String[] { "ge", "gt", "le", "lt", "neq", "eq" });
	}
	/**
	 * TODO 判断是否需要查询
	 * 
	 * @param keys
	 * @param value
	 * @return
	 */
	private boolean queryFlag(String[] keys, Object value) {
		if (keys.length == 3 && (keys[2].endsWith(HaloDao.RX)||keys[2].equals(HaloDao.IN))) {
			return true;
		}
		if (null != value && !StringUtils.isBlank(String.valueOf(value))) {
			if (keys.length > 1) {
				if (keys.length == 3 && keys[2].equals(HaloDao.EX)) {
				} else {
					return true;
				}
			}
		}
		if (keys.length == 3 && keys[2].equals(HaloDao.CK)) {
			throw new RuntimeException("传入值为空!请自行处理!");
		}
		return false;
	}
	private ColumnWithCondition analyzeKeys(ColumnWithCondition columnWithCondition, boolean queryFlag, String[] keys, Object value) {
		String condition = keys[1];
		columnWithCondition.setColumnName(keys[0]);
		if ("eq".equals(condition)) {
			columnWithCondition.setCondition("=");
			return columnWithCondition;
		}
		if ("gt".equals(condition) ) {
			columnWithCondition.setCondition(">");
			return columnWithCondition;
		}
		if (condition.indexOf("ge") != -1) {
			if (queryFlag) {
				condition = condition.replaceFirst("ge", "");
				columnWithCondition.setCondition(">=");
				columnWithCondition = extDateCondition(condition, "", false, columnWithCondition);
			} 
			return columnWithCondition;
		}
		if ("lt".equals(condition)) {
			columnWithCondition.setCondition("<");
			return columnWithCondition;
		}
		if (condition.indexOf("le") != -1) {
			if (queryFlag) {
				condition = condition.replaceFirst("le", "");
				columnWithCondition.setCondition("<=");
				columnWithCondition = extDateCondition(condition, "", true, columnWithCondition);
			} 
			return columnWithCondition;
		}
		if ("neq".equals(condition)) {
			columnWithCondition.setCondition("<>");
			return columnWithCondition;
		}
		if (condition.indexOf("like") != -1) {
			if (queryFlag) {
				columnWithCondition.setCondition("like");
				condition = condition.replaceFirst("like", "");
				columnWithCondition = extLikeCondition(condition, value, columnWithCondition);
				if (columnWithCondition.getTempFlag()) {
					columnWithCondition = extDateCondition(condition, "%", false, columnWithCondition);
				}
			}
			return columnWithCondition;
		}
		if (condition.equals("notin")||condition.equals("notIn")) {
			columnWithCondition.setCondition("not in");
			return columnWithCondition;
		}
		if (StringUtils.isEnglish(condition)) {
			columnWithCondition.setCondition(condition);
			return columnWithCondition;
		}
		throw new RuntimeException("本版本不再允许带符号条件传入!比如=,>换用eq,gt");
		//columnWithCondition.setCondition(condition);
	}

	/**
	 * 分析key值.
	 * 
	 * @param key
	 * @return ColumnWithCondition
	 */
	private ColumnWithCondition analyzeKey(String key, Object value) {
		ColumnWithCondition columnWithCondition = new ColumnWithCondition();
		columnWithCondition.setValue(value);
		if (key.startsWith("(")) {
			columnWithCondition.setLeftBracket("(");
		}
		if (key.startsWith("|")) {
			columnWithCondition.setAndOr("or");
		}
		if (key.endsWith(")")) {
			columnWithCondition.setRightBracket(")");
		}
		StringBuffer orgKey = new StringBuffer(key);
		key = key.replaceAll("\\(|\\||\\)", "");
		String[] keyWithFormate = key.split("\\" + HaloDao.FORMATESPT);
		if (keyWithFormate.length == 2) {
			columnWithCondition.setFormate(keyWithFormate[1]);
			key = keyWithFormate[0];
		}
		String[] keyWithType = key.split(HaloDao.TAPESPT);
		if (keyWithType.length == 2) {
			columnWithCondition.setType(keyWithType[1]);
			key = keyWithType[0];
		}
		String[] keys = key.split(HaloDao.MYSPACE);
		// 判断是否跳过查询
		boolean queryFlag = queryFlag(keys, value);
		if (keys.length >= 2) {
			columnWithCondition=    analyzeKeys(columnWithCondition, queryFlag, keys, value);
		}
		if (queryFlag) {
			columnWithCondition.setIfQuery(true);
		} else {
			columnWithCondition.setIfQuery(false);
		}
		columnWithCondition.setGenColumnName(getGenColumnName(orgKey.toString()));
		return columnWithCondition;
	}


	/**
	 * convert 转换成当前字段类型 如果值类型不匹配
	 * 
	 * @param proType
	 * @param value
	 * @return
	 */
	private Object convert(ColumnWithCondition columnWithCondition) {
		Object value = columnWithCondition.getValue();
		String type = columnWithCondition.getType();
		type = StringUtils.substringAfterLast(value.getClass().getName(), ".");
		logger.info("Type::" + value.getClass().getName());
		logger.info(type);
		String valueType = StringUtils.substringAfterLast(value.getClass().getName(), ".");
		if (!valueType.equalsIgnoreCase(type)) {
			if ("string".equalsIgnoreCase(type)) {
				value = ConvertUtils.convert(value, String.class);
				return value;
			}
			if ("integer".equalsIgnoreCase(type)) {
				value = ConvertUtils.convert(value, Double.class);
				value = ConvertUtils.convert(value, Integer.class);
				return value;
			}
			if ("long".equalsIgnoreCase(type)) {
				value = ConvertUtils.convert(value, Double.class);
				value = ConvertUtils.convert(value, Long.class);
				return value;
			}
			if ("boolean".equalsIgnoreCase(type)) {
				value = ConvertUtils.convert(value, Boolean.class);
				return value;
			}
			if ("double".equalsIgnoreCase(type)) {
				value = ConvertUtils.convert(value, Double.class);
				return value;
			}
			if ("float".equalsIgnoreCase(type)) {
				value = ConvertUtils.convert(value, Float.class);
				return value;
			}
			if ("short".equalsIgnoreCase(type)) {
				value = ConvertUtils.convert(value, Double.class);
				value = ConvertUtils.convert(value, Short.class);
				return value;
			}
			if ("byte".equalsIgnoreCase(type)) {
				value = ConvertUtils.convert(value, Byte.class);
				return value;
			}
			if ("timestamp".equalsIgnoreCase(type) || "datetime".equalsIgnoreCase(type) || "date".equalsIgnoreCase(type)) {
				if (value instanceof String) {
					value = getDate(columnWithCondition);
				}
				return value;
			}
			if ("big_decimal".equalsIgnoreCase(type) || "bigDecimal".equalsIgnoreCase(type)) {
				if (!(value instanceof BigDecimal)) {
					value = ConvertUtils.convert(value, BigDecimal.class);
				}
				return value;
			}
		}

		return value;
	}

	private static HaloMap getHqlSnippetMap(String hqlSnippet, Object value) {
		Object[] newValue = null;
		if (value instanceof Object[]) {
			newValue = (Object[]) value;
		} else {
			newValue = new Object[] { value };
		}
		HaloMap map = new HaloMap();
		StringBuffer sb = new StringBuffer();
		boolean flag = false;
		int j = 0;
		for (int i = 0; i < hqlSnippet.length(); i++) {
			char cur = hqlSnippet.charAt(i);
			if (!Character.isLetter(cur)) {
				if (flag) {
					map.put(sb.toString(), newValue[j]);
					sb = new StringBuffer();
					j++;
				}
				flag = false;
			}
			if (flag) {
				sb.append(cur);
			}
			if (cur == ':') {
				flag = true;
			}
			if (cur == '?') {
				map.put(MyUUID.create(), newValue[j]);
				sb = new StringBuffer();
				j++;
			}
		}
		return map;
	}



	private String getViewSql(String viewAs, MyHashMap tplMap) {
		if(viewAs.startsWith(HALO)){
			String[] viewAss=viewAs.split(HaloDao.MYSPACE);
			String fileName=viewAss[0];
			String id=null;
			File xmlPath = FileUtils.getClassPath(HaloDao.HALOPACH +  getPosition(), fileName + ".xml");
			XmlUtils xmlUtils = new XmlUtils(xmlPath);
			if(viewAss.length>1){
				id=viewAss[1];
			}
			String view = xmlUtils.getView(id);
			if (null != tplMap) {
				ITplUtils tplUtils = new FreemarkerUtils();
				view = tplUtils.generateString(tplMap, view);
			}
			return "("+view+")";
		}else{
			return viewAs;//是个数据库表或者视图对应的实体
		}
	}


	private Type getType(String property) {
		Type type =null;
		 String method=null;
			try {
			      method="get"+StringUtils.capitalize(property);
				type=	 this.entityType.getMethod(method).getReturnType();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				logger.warn("NoSuchMethod:"+method);
			}
	      return type;
	}


	public SqlWithParameter createMySqlQuery( HaloMap parameter) {
		String viewAs =this.entityType.getSimpleName();
		SqlWithParameter sqlWithParamter = new SqlWithParameter();
		StringBuffer sql = new StringBuffer();
		StringBuffer sqlSelect = new StringBuffer();
		StringBuffer order = new StringBuffer();
		StringBuffer group = new StringBuffer();
		StringBuffer column = new StringBuffer();
		HaloMap sqlPrmMap = new HaloMap();// ....
		MyHashMap tplMap = new MyHashMap();
		if (null == parameter) {
			parameter = new HaloMap();
		}

		for (Entry<String, ?> entry : parameter.entrySet()) {
			String key = filterValue(entry.getKey());
			Object value = entry.getValue();
			if(key.equals(VIEWID)){
				    // 子视图???
				viewAs=viewAs+HaloDao.MYSPACE+value;//比如HaloUser_a
				continue;
			}
			if (key.startsWith(HaloDao.ADDCOLUMN)) {
				column = getColumn(column, value);
				continue;
			}
			if (key.startsWith(HaloDao.ADDORDER)) {
				order = getOrder(order, value);
				continue;
			}
			if (key.startsWith(HaloDao.ADDGROUP)) {
				group = getGroup(group, value);
				continue;
			}
			if (key.startsWith(HaloDao.ADDHQL)) {
				value = getHqlSnippet(value.toString());
				sql.append(String.format(" and (%s) ", TableUtil.toSql(value.toString())));
				continue;// 灵活但不安全接口:sql查询片段
			}
			ColumnWithCondition columnWithCondition = analyzeKey(key, value);
			if (!columnWithCondition.getIfQuery()) {// 不查询该字段
				continue;
			}
			String type = columnWithCondition.getType();
			if (null == type) {
				try {
					type = getType(columnWithCondition.getColumnName()).toString();
				} catch (Exception e) {
					logger.warn("could not resolve property(无法得到字段类型)");
					type = null;
				}
				columnWithCondition.setType(type);
			}
			if (HaloDao.EX.equals(columnWithCondition.getCondition())) {
				continue;// 条件为"ex"时排除该参数
			}
			if (columnWithCondition.getCondition().equals(HaloDao.PRM)) {
				if (null != type) {
					value = convert(columnWithCondition);
				}
				sqlPrmMap.put(columnWithCondition.getColumnName(), value);
				continue;// 添加参数
			}
			if (columnWithCondition.getCondition().equals(HaloDao.HQL)) {
				String hqlKey = columnWithCondition.getColumnName();
				String hqlValue = getHqlSnippet(hqlKey);
				HaloMap map = getHqlSnippetMap(hqlValue, value);
				sqlPrmMap.putAll(map);
				hqlValue = TableUtil.toSql(hqlValue);
				sql.append(String.format(" and (%s) ", hqlValue));
				continue;// 添加参数
			}
			if (columnWithCondition.getCondition().equals(DATA)) {
				if (value instanceof String) {
					String dataId = String.valueOf(value);
					value = getDataSnippet(dataId);
					tplMap.put(columnWithCondition.getColumnName(), value);
				} else {
					tplMap.put(columnWithCondition.getColumnName(), value);
				}
				continue;// 添加参数
			}
		
			if (columnWithCondition.getIfQuery()) {// 查询该字段
				if (null == value || "null".equalsIgnoreCase(String.valueOf(entry.getValue()).trim())) {
					sql.append(String.format(" %s %s %s %s null %s ", columnWithCondition.getAndOr(), columnWithCondition.getLeftBracket(),TableUtil.toSql(columnWithCondition.getColumnName()), columnWithCondition.getCondition(), columnWithCondition.getRightBracket()));
					continue;
				} else {
					if (null != columnWithCondition.getDirectValue()) {
						sql.append(String.format(" %s %s %s %s %s %s ", columnWithCondition.getAndOr(), columnWithCondition.getLeftBracket(), TableUtil.toSql(columnWithCondition.getColumnName()), columnWithCondition.getCondition(), columnWithCondition.getDirectValue(), columnWithCondition.getRightBracket()));
						continue;
					}
					if (null != type) {
						value = convert(columnWithCondition);
					}
					sql.append(String.format(" %s %s %s %s:%s %s ", columnWithCondition.getAndOr(), columnWithCondition.getLeftBracket(), TableUtil.toSql(columnWithCondition.getColumnName()), columnWithCondition.getCondition(), columnWithCondition.getGenColumnName(), columnWithCondition.getRightBracket()));
					sqlPrmMap.put(columnWithCondition.getGenColumnName(), value);
				}
			}

		}// for
		String columnStr = TableUtil.toSql(column.toString());
		String viewSql = getViewSql(viewAs, tplMap);
		if (StringUtils.isNotBlank(columnStr)) {
			sqlSelect = new StringBuffer(String.format("select %s from %s %s where 1=1 ", columnStr.substring(0, columnStr.length() - 1), viewSql, viewAs));
		} else {
			sqlSelect.append(String.format("select *  from %s %s  where 1=1 ", viewSql, viewAs));
		}
		String groupStr = TableUtil.toSql(group.toString());
		if (StringUtils.isNotBlank(groupStr)) {
			sql.append(String.format(" group by %s ", groupStr.substring(0, groupStr.length() - 1)));
		}
		String orderStr = TableUtil.toSql(order.toString());
		if (StringUtils.isNotBlank(orderStr)) {
			sql.append(String.format(" order by %s ", orderStr.substring(0, orderStr.length() - 1)));
		}
		String sqlStr = sqlSelect.toString() + sql.toString();
		sqlWithParamter.setSql(sqlStr);
		logger.info("SQL:" + sqlStr);
		logger.info("hqlPrmMap:" + sqlPrmMap);
		logger.info("tplDataMap:" + tplMap);
		logger.info("time::" + System.currentTimeMillis());
		sqlWithParamter.setParamterMap(sqlPrmMap);
		return sqlWithParamter;
	}

	/**
	 * 生成SQLQuery
	 * @param sql
	 * @param Map
	 * @return SQLQuery
	 */
	public SQLQuery createSQLQuery(String sql, HaloMap parameter) {
		SQLQuery q = getSession().createSQLQuery(sql);
		q.setProperties(parameter);
		return q;
	}


	public SQLQuery CreateMySqlQueryByHaloView(HaloMap parameter) {
		SqlWithParameter sqlWithParameter = createMySqlQuery( parameter);
		String sql = sqlWithParameter.getSql();
		HaloMap hqlPrmMap = sqlWithParameter.getParamterMap();
		SQLQuery query = createSQLQuery(sql, hqlPrmMap);
		return query;
	}

	public SQLQuery CreateMySqlQueryByHaloViewToBean( HaloMap parameter) {
		SQLQuery query = CreateMySqlQueryByHaloView(parameter);
		query.setResultTransformer(new ColumnToBean(this.entityType));
		return query;
	}

	public SQLQuery CreateMySqlQueryByHaloViewToMap( HaloMap parameter) {
		SQLQuery query = CreateMySqlQueryByHaloView( parameter);
		query.setResultTransformer(new ColumnToMap());
		return query;
	}


	@SuppressWarnings("unchecked")
	public <X> List<X> findListByHaloView(HaloMap parameter) {
		Integer begin=0;
		Integer end=null;
		if(null!=parameter.get(HaloDao.ADDBEGIN)){
			begin=(Integer) ConvertUtils.convert(parameter.get(HaloDao.ADDBEGIN),Integer.class);
			parameter.remove(HaloDao.ADDBEGIN);
		}
        if(null!=parameter.get(HaloDao.ADDEND)){
        	end=(Integer) ConvertUtils.convert(parameter.get(HaloDao.ADDEND),Integer.class);
        	parameter.remove(HaloDao.ADDEND);
		}
		SQLQuery query = null;
		if (this.entityType.getName().equals("com.ht.halo.hibernate3.HaloViewMap")) {
			query = CreateMySqlQueryByHaloViewToMap( parameter);
		} else {
			query = CreateMySqlQueryByHaloViewToBean( parameter);
		}
	    if(null!=end){
        	query.setFirstResult(begin);
    		query.setMaxResults(end - begin);
        }
		return query.list();
	}
	private String getPosition() {
		Halo halo = this.entityType.getAnnotation(Halo.class);
		if (null != halo && StringUtils.isNotBlank(halo.position())) {
			return "."+halo.position();
		}
		return "";
	}
	
	private SQLQuery CreateSqlQueryByHaloView(HaloMap parameter){
		SQLQuery query = null;
		if (this.entityType.getName().equals("com.ht.halo.hibernate3.HaloViewMap")) {
			query = CreateMySqlQueryByHaloViewToMap(parameter);
		} else {
			query = CreateMySqlQueryByHaloViewToBean(parameter);
		}
		return query;
	}
	@SuppressWarnings("unchecked")
	public T  findFirstByHaloView(HaloMap parameter) {
		SQLQuery query = CreateSqlQueryByHaloView(parameter);
		query.setFirstResult(0);
		query.setMaxResults(1);
		return (T) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public <X> List<X>   findListByHaloView(HaloMap parameter,int begin,int end) {
		SQLQuery query = CreateSqlQueryByHaloView(parameter);
		query.setFirstResult(begin);
		query.setMaxResults(end);
		return query.list();
	}

	/**
	 *  TODO 查询前几条数据
	 * @param viewName
	 * @param parameter
	 * @param num
	 * @return
	 */
	public <X> List<X>  findListByHaloView(String viewName, HaloMap parameter,int num) {
		return findListByHaloView( parameter,0, num);
	}

	@SuppressWarnings("unchecked")
	public T  findUniqueByHaloView(HaloMap parameter) {
		SQLQuery query = CreateSqlQueryByHaloView(parameter);
		return (T) query.uniqueResult() ;
	}

	/**
	 * 生成总条数的sql语句.
	 * @param sql
	 * @return sql语句
	 */
	private String generateMyCountSql(String sql) {
		return String.format("select count(*) from (%s) temp ",  sql);
	}
	/**
	 * 获得查询总条数.
	 * 
	 * @param sql
	 * @param parameter
	 * @return 总条数
	 */
	protected long countMySqlResult(String sql, HaloMap parameter) {
		String countSql = generateMyCountSql(sql);
		Query query = this.createSQLQuery(countSql, parameter);
		return ((Number) query.uniqueResult()).longValue();
	}
	protected Query setPageParameterToQuery(Query q,  Page<T> page) {
		q.setFirstResult(page.getFirstEntityIndex());
		q.setMaxResults(page.getPageSize());
		return q;
	}
	@SuppressWarnings("unchecked")
	public Page<T> findPageByHaloView( Page<T> page, HaloMap parameter) {
		notNull(page, "page");
		SqlWithParameter sqlWithParameter = createMySqlQuery( parameter);
		String sql = sqlWithParameter.getSql();
		HaloMap hqlPrmMap = sqlWithParameter.getParamterMap();
		SQLQuery query = createSQLQuery(sql, hqlPrmMap);
		if (this.entityType.getName().equals("com.ht.halo.hibernate3.HaloViewMap")) {
			query.setResultTransformer(new ColumnToMap());
		}else{
		    query.setResultTransformer(new ColumnToBean(this.entityType));
		}
		long totalCount = countMySqlResult(sql, hqlPrmMap);
		page.setEntityCount((int) totalCount);
		setPageParameterToQuery(query, page);
		page.setEntities(query.list());
		return page;
	}
	protected void notNull(Object obj, String name) {
		Assert.notNull(obj, "[" + name + "] must not be null.");
	}

}
