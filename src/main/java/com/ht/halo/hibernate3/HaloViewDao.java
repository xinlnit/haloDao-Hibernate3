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

import com.ht.halo.hibernate3.base.Assert;
import com.ht.halo.hibernate3.base.ColumnToBean;
import com.ht.halo.hibernate3.base.ColumnToMap;
import com.ht.halo.hibernate3.base.DateUtils;
import com.ht.halo.hibernate3.base.MyUUID;
import com.ht.halo.hibernate3.base.Page;
import com.ht.halo.hibernate3.base.TableUtil;
import com.ht.halo.hibernate3.bean.ColumnWithCondition;
import com.ht.halo.hibernate3.bean.SqlWithParameter;
import com.ht.halo.hibernate3.feemarker.FreemarkerUtils;
import com.ht.halo.hibernate3.utils.StringUtils;
import com.ht.halo.hibernate3.utils.annotations.HaloView;
import com.ht.halo.hibernate3.utils.file.FileUtils;
import com.ht.halo.hibernate3.utils.properties.PropertiesUtil;

/**
 *  基于haloView视图sql的Dao层
 * @author fengchangyi@haitao-tech.com
 * @date 2015-1-21 下午8:04:14
 * @param <T>
 */
public class HaloViewDao<T> {
	private static final Log logger = LogFactory.getLog(HaloViewDao.class);
	private static final String SPACE = "\u0020";
	private static final char SPACECHAR = '\u0020';
	private static final String FORMATESPT = "?";
	private static final String MYSPACE = ":";
	private static final String TAPESPT = "#";
	private static final String EX = "ex";
	private static final String IN = "in";
	private static final String PRM = "prm";// 添加查询hql中的参数标识
	private static final String DATA = "data";// haloView中的模板数据
	private static final String[] NUMS = new String[] { "1", "3", "5", "6", "7", "8", "9", "0" };
	private static final String[] NUMREPLACE = new String[] { "|", "#", "%", ":", "?", ".", "(", ")" };
	public static final String[] PATTERN = new String[] { "yyyy", "yyyy-MM", "yyyy-MM-dd", "MM-dd", "yyyy-MM-dd HH", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss", "yyyy年MM月dd日", "yyyy年MM月dd日 HH:mm:ss", "yyyyMM", "yyyyMMdd", "yyyy/MM", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss" };
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

	private String removeAllSpace(String value) {
		if (value.startsWith("_")) {
			value = value.substring(1, value.length());
		}
		return value.replaceAll(SPACE, "");
	}

	private String replaceNum(String value) {
		return StringUtils.replaceEach(value, NUMS, NUMREPLACE);
	}

	private String filterValue(String value) {
		return replaceNum(removeAllSpace(value));
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
			order.append((filterValue((String) value)).replace(':', SPACECHAR)).append(",");
		}
		if (value instanceof String[]) {
			for (String str : (String[]) value) {
				order.append((filterValue(str)).replace(':', SPACECHAR)).append(",");
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

	/**
	 * 从属性文件或得hql片段
	 * 
	 * @param value
	 * @return hql语句片段
	 */
	private String getHqlSnippet(String value) {
		String fileName = StringUtils.substringBefore(value, ".");
		String property = StringUtils.substringAfter(value, ".");
		PropertiesUtil propertiesUtil = new PropertiesUtil(FileUtils.getClassPath("halo.hql", fileName + ".properties"));
		return propertiesUtil.getHql(property);
	}

	private Object getDate(ColumnWithCondition columnWithCondition) {
		Object value = columnWithCondition.getValue();
		try {
			if (null != columnWithCondition.getFormate()) {
				value = DateUtils.parse(columnWithCondition.getFormate(), String.valueOf(value));
			} else {
				value = DateUtils.parseDate(String.valueOf(value), PATTERN);
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
		str = str.split("\\" + FORMATESPT)[0];
		str = StringUtils.replaceEach(str, NUMREPLACE, new String[] { "Q", "E", "T", "Y", "U", "I", "O", "P" });
		return StringUtils.replaceEach(str, new String[] { ">=", ">", "<=", "<", "!=", "=" }, new String[] { "ge", "gt", "le", "lt", "neq", "eq" });
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

		boolean flag = false;
		String[] keyWithFormate = key.split("\\" + FORMATESPT);
		if (keyWithFormate.length == 2) {
			columnWithCondition.setFormate(keyWithFormate[1]);
			key = keyWithFormate[0];
		}
		String[] keyWithType = key.split(TAPESPT);

		if (keyWithType.length == 2) {
			columnWithCondition.setType(keyWithType[1]);
			key = keyWithType[0];
		}
		String[] keys = key.split(MYSPACE);
		if (keys.length == 1) {
			columnWithCondition.setCondition("=");
			orgKey.append(":eq");
			columnWithCondition.setColumnName(keys[0]);
		}
		if (keys.length >= 2) {
			String condition = keys[1];
			columnWithCondition.setColumnName(keys[0]);
			if ("=".equals(condition) || "eq".equals(condition)) {
				columnWithCondition.setCondition("=");
				flag = true;
			}
			if (!flag) {
				if (">".equals(condition) || "gt".equals(condition)) {
					columnWithCondition.setCondition(">");
					flag = true;
				}
			}
			if (!flag) {
				if (condition.indexOf(">=") != -1 || condition.indexOf("ge") != -1) {
					condition = condition.replaceFirst("ge|\\>=", "");
					columnWithCondition.setCondition(">=");
					columnWithCondition = extDateCondition(condition, "", false, columnWithCondition);
					flag = true;
				}
			}
			if (!flag) {
				if ("<".equals(condition) || "lt".equals(condition)) {
					columnWithCondition.setCondition("<");
					flag = true;
				}
			}
			if (!flag) {
				if (condition.indexOf("<=") != -1 || condition.indexOf("le") != -1) {
					condition = condition.replaceFirst("le|\\<=", "");
					columnWithCondition.setCondition("<=");
					columnWithCondition = extDateCondition(condition, "", true, columnWithCondition);
					flag = true;
				}
			}
			if (!flag) {
				if ("<>".equals(condition) || "!=".equals(condition) || "neq".equals(condition)) {
					columnWithCondition.setCondition("<>");
					flag = true;
				}
			}
			if (!flag) {
				if (condition.indexOf("like") != -1 || condition.indexOf("Like") != -1) {
					columnWithCondition.setCondition("like");
					condition = condition.replaceFirst("like|Like", "");
					columnWithCondition = extLikeCondition(condition, value, columnWithCondition);
					if (columnWithCondition.getTempFlag()) {
						columnWithCondition = extDateCondition(condition, "%", false, columnWithCondition);
					}
					flag = true;
				}
			}
			if (!flag) {
				if (condition.equals("notIn")) {
					columnWithCondition.setCondition("not in");
					flag = true;
				}
			}
			if (!flag) {
				columnWithCondition.setCondition(condition);
			}
		}
		if (keys.length == 3) {
			columnWithCondition.setIfQuery(keys[2]);
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

	private String getDataSnippet(String value) {
		String fileName = StringUtils.substringBefore(value, ".");
		String property = StringUtils.substringAfter(value, ".");
		PropertiesUtil propertiesUtil = new PropertiesUtil(FileUtils.getClassPath("halo.data", fileName + ".properties"));
		return propertiesUtil.getData(property);
	}

	private String getViewSql(String viewName, HaloMap tplMap) {
		String packStr = "";
		if (viewName.indexOf(".") != -1) {
			packStr = "." + StringUtils.substringBeforeLast(viewName, ".");
			viewName = StringUtils.substringAfterLast(viewName, ".");
		}
		FreemarkerUtils freemarkerUtils = new FreemarkerUtils();
		File viewPath = FileUtils.getClassPath(HaloDao.HALOPACH + ".view" + packStr, "");
		return freemarkerUtils.generateString(tplMap, viewPath, viewName + ".halo");
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

	/**
	 * 根据haloview文件结果集查询获得SQLQuery和参数. 方式代码基本与createMyQuery相同,未重用
	 * 
	 * @param viewName
	 * @param parameter
	 * @return SqlWithParameter
	 */
	public SqlWithParameter createMySqlQuery(String viewName, HaloMap parameter) {
		String viewAs = "temp";
		SqlWithParameter sqlWithParamter = new SqlWithParameter();
		StringBuffer sql = new StringBuffer();
		StringBuffer sqlSelect = new StringBuffer();
		StringBuffer order = new StringBuffer();
		StringBuffer group = new StringBuffer();
		StringBuffer column = new StringBuffer();
		boolean queryByBlankFlag = false;
		HaloMap sqlPrmMap = new HaloMap();// ....
		HaloMap tplMap = new HaloMap();
		if (null == parameter) {
			parameter = new HaloMap();
		}
		if (viewName.indexOf(".") != -1) {
			viewAs = StringUtils.substringAfterLast(viewName, ".");
		}
		for (Entry<String, ?> entry : parameter.entrySet()) {
			String key = filterValue(entry.getKey());
			Object value = entry.getValue();
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
				sql.append(String.format(" and (%s) ", TableUtil.toHql(value.toString())));
				continue;// 灵活但不安全接口:sql查询片段
			}

			ColumnWithCondition columnWithCondition = analyzeKey(key, value);
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
			if (EX.equals(columnWithCondition.getCondition())) {
				continue;// 条件为"ex"时排除该参数
			}
			if (columnWithCondition.getCondition().equals(PRM)) {
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
				hqlValue = TableUtil.toHql(hqlValue);
				sql.append(String.format(" and (%s) ", hqlValue));
				continue;// 添加参数
			}
			if (columnWithCondition.getCondition().equals(DATA)) {
				boolean flag = false;
				if (value instanceof String) {
					String valueStr = String.valueOf(value);
					if (StringUtils.indexOf(valueStr, ".data.") != -1) {
						flag = true;
					}// 前缀为data的不转换
					value = getDataSnippet(valueStr);
					if (!flag) {
						value = SPACE + TableUtil.toHql(String.valueOf(value)) + SPACE;
					}
					tplMap.put(columnWithCondition.getColumnName(), value);
				} else {
					tplMap.put(columnWithCondition.getColumnName(), value);
				}
				continue;// 添加参数
			}
			if (null != columnWithCondition.getIfQuery()) {
				if (EX.equals(columnWithCondition.getIfQuery())) {// 始终不查询该字段
					continue;
				}
				if (IN.equals(columnWithCondition.getIfQuery())) {// 始终查询该字段
					if (null == value || "null".equalsIgnoreCase(String.valueOf(entry.getValue()).trim())) {
						sql.append(String.format(" %s %s %s %s null %s ", columnWithCondition.getAndOr(), columnWithCondition.getLeftBracket(), TableUtil.toHql(columnWithCondition.getColumnName()), columnWithCondition.getCondition(), columnWithCondition.getRightBracket()));
					} else {
						queryByBlankFlag = true;
					}
					continue;
				}
			}// in,ex
			if (null == value && !queryByBlankFlag) {
				logger.warn("参数值为空!");// 处理?
			}
			if (null != value) {
				if (null != columnWithCondition.getDirectValue()) {
					sql.append(String.format(" %s %s %s %s %s %s ", columnWithCondition.getAndOr(), columnWithCondition.getLeftBracket(), TableUtil.toHql(columnWithCondition.getColumnName()), columnWithCondition.getCondition(), columnWithCondition.getDirectValue(), columnWithCondition.getRightBracket()));
					continue;
				}
				if (!StringUtils.isBlank(String.valueOf(value)) || queryByBlankFlag) {
					if (null != type) {
						value = convert(columnWithCondition);
					}
					sql.append(String.format(" %s %s %s %s :%s %s ", columnWithCondition.getAndOr(), columnWithCondition.getLeftBracket(), TableUtil.toHql(columnWithCondition.getColumnName()), columnWithCondition.getCondition(), columnWithCondition.getGenColumnName(), columnWithCondition.getRightBracket()));
					sqlPrmMap.put(columnWithCondition.getGenColumnName(), value);
				}
			}
		}// for
		String columnStr = TableUtil.toHql(column.toString());
		String viewSql = getViewSql(viewName, tplMap);
		if (StringUtils.isNotBlank(columnStr)) {
			sqlSelect = new StringBuffer(String.format("select %s from (%s) %s where 1=1 ", columnStr.substring(0, columnStr.length() - 1), viewSql, viewAs));
		} else {
			sqlSelect.append(String.format("select *  from (%s) %s  where 1=1 ", viewSql, viewAs));
		}
		String groupStr = TableUtil.toHql(group.toString());
		if (StringUtils.isNotBlank(groupStr)) {
			sql.append(String.format(" group by %s ", groupStr.substring(0, groupStr.length() - 1)));
		}
		String orderStr = TableUtil.toHql(order.toString());
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
	 * 
	 * @param sql
	 * @param Map
	 * @return SQLQuery
	 */
	public SQLQuery createSQLQuery(String sql, HaloMap parameter) {
		SQLQuery q = getSession().createSQLQuery(sql);
		q.setProperties(parameter);
		return q;
	}

	/**
	 * 根据haloview文件结果集查询获得SQLQuery.
	 * 
	 * @param viewName
	 * @param parameter
	 * @return SQLQuery
	 */
	public SQLQuery CreateMySqlQueryByHaloView(String viewName, HaloMap parameter) {
		SqlWithParameter sqlWithParameter = createMySqlQuery(viewName, parameter);
		String sql = sqlWithParameter.getSql();
		HaloMap hqlPrmMap = sqlWithParameter.getParamterMap();
		SQLQuery query = createSQLQuery(sql, hqlPrmMap);
		return query;
	}

	public SQLQuery CreateMySqlQueryByHaloViewToBean(String viewName, HaloMap parameter) {
		SQLQuery query = CreateMySqlQueryByHaloView(viewName, parameter);
		query.setResultTransformer(new ColumnToBean(this.entityType));
		return query;
	}

	public SQLQuery CreateMySqlQueryByHaloViewToMap(String viewName, HaloMap parameter) {
		SQLQuery query = CreateMySqlQueryByHaloView(viewName, parameter);
		query.setResultTransformer(new ColumnToMap());
		return query;
	}

	/**
	 * 根据haloView查询. 首先在halo.view包中在ViewTest编写好sql语句:select * from base_user
	 * where role=:role ${email} ${groupBy} 调用findListByHaloView("ViewTest",new
	 * HaloMap().set("role:prm",1).set("groupBy:data"," group by role")
	 * .set("email:data"
	 * ," and email=:eamil ").set("email:prm","123@ww.com").set(
	 * "userName:like","von")
	 * .addColumn("userName","password").addOrder("createDate","role");
	 * 为:查询出角色为1,邮箱为123@ww.com,并按角色分组的结果集中查询用户名左模糊von,并按照createDate和role正序,
	 * 并只查询出用户名及密码字段并封装到实体中 拼接使用freemarker技术
	 * 
	 * @param viewName
	 *            :haloView的sql文件名,可以加入包名:test.ViewTest(全:halo.view.test.
	 *            ViewTest.halo)
	 * @param parameter
	 * @return {@link List}
	 */
	@SuppressWarnings("unchecked")
	public <X> List<X> findListByHaloView(String viewName, HaloMap parameter) {
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
			query = CreateMySqlQueryByHaloViewToMap(viewName, parameter);
		} else {
			query = CreateMySqlQueryByHaloViewToBean(viewName, parameter);
		}
	    if(null!=end){
        	query.setFirstResult(begin);
    		query.setMaxResults(end - begin);
        }
		return query.list();
	}
	private String getViewName(){
		String entityName=this.entityType.getSimpleName();
	    HaloView haloView =	this.entityType.getAnnotation(HaloView.class);
	    if(null!=haloView&&StringUtils.isNotBlank(haloView.position())){
	    	return haloView.position()+"."+entityName;
	    }
		return entityName;
	}
	/**
	 *  TODO 默认halo.view下相同实体名的HaloView文件,建议添加@HaloView注解,加入模块包名
	 *  比如@HaloView(position="test")
	 * @param parameter
	 * @return
	 */
	public <X> List<X> findListByHaloView(HaloMap parameter) {	
		return findListByHaloView(getViewName(),parameter) ;
	}
	private SQLQuery CreateSqlQueryByHaloView(String viewName, HaloMap parameter){
		SQLQuery query = null;
		if (this.entityType.getName().equals("com.ht.halo.hibernate3.HaloViewMap")) {
			query = CreateMySqlQueryByHaloViewToMap(viewName, parameter);
		} else {
			query = CreateMySqlQueryByHaloViewToBean(viewName, parameter);
		}
		return query;
	}
	@SuppressWarnings("unchecked")
	public T  findByHaloView(String viewName, HaloMap parameter) {
		SQLQuery query = CreateSqlQueryByHaloView(viewName,parameter);
		query.setFirstResult(0);
		query.setMaxResults(1);
		return (T) query.uniqueResult();
	}
	public T  findByHaloView(HaloMap parameter) {	
		return findByHaloView(getViewName(),parameter) ;
	}
	@SuppressWarnings("unchecked")
	public <X> List<X>   findListByHaloView(String viewName, HaloMap parameter,int begin,int end) {
		SQLQuery query = CreateSqlQueryByHaloView(viewName,parameter);
		query.setFirstResult(begin);
		query.setMaxResults(end);
		return query.list();
	}
	public <X> List<X>  findListByHaloView( HaloMap parameter,int begin,int end) {
		return findListByHaloView(getViewName(), parameter,begin,end);
	}
	/**
	 *  TODO 查询前几条数据
	 * @param viewName
	 * @param parameter
	 * @param num
	 * @return
	 */
	public <X> List<X>  findListByHaloView(String viewName, HaloMap parameter,int num) {
		return findListByHaloView(viewName, parameter,0, num);
	}
	public <X> List<X>  findListByHaloView( HaloMap parameter,int num) {
		return findListByHaloView(getViewName(), parameter,num);
	}
	@SuppressWarnings("unchecked")
	public T  findUniqueByHaloView(String viewName, HaloMap parameter) {
		SQLQuery query = CreateSqlQueryByHaloView(viewName,parameter);
		return (T) query.uniqueResult() ;
	}
	public T  findUniqueByHaloView( HaloMap parameter) {
		return findUniqueByHaloView(getViewName(), parameter) ;
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
	protected Query setPageParameterToQuery( Query q,  Page<T> page) {
		q.setFirstResult(page.getFirstEntityIndex());
		q.setMaxResults(page.getPageSize());
		return q;
	}
	@SuppressWarnings("unchecked")
	public Page<T> findPageByHaloView(String viewName, Page<T> page, HaloMap parameter) {
		notNull(page, "page");
		SqlWithParameter sqlWithParameter = createMySqlQuery(viewName, parameter);
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
	public Page<T> findPageByHaloView(Page<T> page, HaloMap parameter){
		return findPageByHaloView(getViewName(), page, parameter);
      }
	protected void notNull(Object obj, String name) {
		Assert.notNull(obj, "[" + name + "] must not be null.");
	}

}
