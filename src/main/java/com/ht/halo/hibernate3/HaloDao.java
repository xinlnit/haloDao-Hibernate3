package com.ht.halo.hibernate3;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.EntityMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.hql.FilterTranslator;
import org.hibernate.hql.QueryTranslatorFactory;
import org.hibernate.impl.SessionFactoryImpl;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.EntityType;
import org.hibernate.type.Type;

import com.ht.halo.hibernate3.base.BaseHibernateDao;
import com.ht.halo.hibernate3.base.ColumnToBean;
import com.ht.halo.hibernate3.base.DateUtils;
import com.ht.halo.hibernate3.base.EntityUtils;
import com.ht.halo.hibernate3.base.MyBeanUtils;
import com.ht.halo.hibernate3.base.MyUUID;
import com.ht.halo.hibernate3.base.Page;
import com.ht.halo.hibernate3.base.TableUtil;
import com.ht.halo.hibernate3.base.Updater;
import com.ht.halo.hibernate3.bean.ColumnWithCondition;
import com.ht.halo.hibernate3.bean.HqlWithParameter;
import com.ht.halo.hibernate3.bean.SqlWithParameter;
import com.ht.halo.hibernate3.feemarker.FreemarkerUtils;
import com.ht.halo.hibernate3.utils.file.FileUtils;
import com.ht.halo.hibernate3.utils.properties.PropertiesUtil;

/**
 * @ClassName: HaloDao
 * @Description: TODO 基于hibernate的通用Dao
 * @author fengchangyi
 * @date 2014-12-20 下午3:14:10
 * @version 3.0
 */
public class HaloDao<T, PK extends Serializable> extends BaseHibernateDao<T, Serializable> {
	private static final Log logger = LogFactory.getLog(HaloDao.class);
	public static final String ADDCOLUMN = "addColumn";// 添加查询字段,默认查询出主键
	public static final String ADDORDER = "addOrder";// 添加排序
	public static final String ADDGROUP = "addGroup";// 添加排序
	public static final String ADDHQL = "addHql";// 添加查询hql片段的key值,比如fcy.user.baseUser.a(最前是文件名,放到halo.hql中)
	public static final String HQL = "hql";
	private static final String PRM = "prm";// 添加查询hql中的参数标识
	private static final String DATA = "data";// haloView中的模板数据
	private static final String SPACE = "\u0020";
	private static final char SPACECHAR = '\u0020';
	private static final String TAPESPT = "#";
	private static final String FORMATESPT = "?";
	private static final String MYSPACE = ":";
	private static final String EX = "ex";
	private static final String IN = "in";
	private static final String TOBEAN = "toBean";
	private static final String HALOPACH = "halo";
	private static final String[] NUMS = new String[] { "1", "3", "5", "6", "7", "8", "9", "0" };
	private static final String[] NUMREPLACE = new String[] { "|", "#", "%", ":", "?", ".", "(", ")" };
	public static final String[] PATTERN = new String[] { "yyyy", "yyyy-MM", "yyyy-MM-dd", "MM-dd", "yyyy-MM-dd HH", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss", "yyyy年MM月dd日", "yyyy年MM月dd日 HH:mm:ss", "yyyyMM", "yyyyMMdd", "yyyy/MM", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss" };

	private SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	private Session getSession() {
		return getSessionFactory().getCurrentSession();
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

	private String getGenColumnName(String str) {
		str = str.split("\\" + FORMATESPT)[0];
		str = StringUtils.replaceEach(str, NUMREPLACE, new String[] { "Q", "E", "T", "Y", "U", "I", "O", "P" });
		return StringUtils.replaceEach(str, new String[] { ">=", ">", "<=", "<", "!=", "=" }, new String[] { "ge", "gt", "le", "lt", "neq", "eq" });
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

	private Object getDate(ColumnWithCondition columnWithCondition) {
		Object value = columnWithCondition.getValue();
		try {
			if (null != columnWithCondition.getFormate()) {
				value = DateUtils.parse(columnWithCondition.getFormate(), String.valueOf(value));
			} else {
				value = DateUtils.parseDate(String.valueOf(value), PATTERN);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;

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
				if ("!=".equals(condition) || "neq".equals(condition)) {
					columnWithCondition.setCondition("!=");
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
	 * 取自锐道hibernateUtil类 获取类型 包含关联的
	 * 
	 * @param property
	 * @param classMetadata
	 * @param sessionFactory
	 * @return Type
	 */
	private Type getHibernateType(String property, ClassMetadata classMetadata, SessionFactory sessionFactory) {
		String[] tokens = StringUtils.split(property, '.');
		if (tokens.length == 1)
			return classMetadata.getPropertyType(property);
		if (tokens.length > 1) {
			Type type = null;
			ClassMetadata meta = classMetadata;
			for (String token : tokens) {
				type = meta.getPropertyType(token);
				if ((type instanceof EntityType)) {

					EntityType entityType = (EntityType) type;
					String entityName = entityType.getAssociatedEntityName();
					meta = sessionFactory.getClassMetadata(entityName);

				}
			}
			return type;
		}
		return null;
	}

	/**
	 * convert 转换成当前字段类型 如果值类型不匹配
	 * @param proType
	 * @param value
	 * @return
	 */
	private Object convert(ColumnWithCondition columnWithCondition) {
		Object value = columnWithCondition.getValue();
		String type = columnWithCondition.getType();
		logger.info("Type::"+value.getClass().getName());
		logger.info(type);
		String valueType = StringUtils.substringAfterLast(value.getClass().getName(), ".");
		if (!valueType.equalsIgnoreCase(type)) {
			if ("integer".equalsIgnoreCase(type)) {
				value=ConvertUtils.convert(value,Double.class);
				value = ConvertUtils.convert(value, Integer.class);
				return value;
			}
			if ("long".equalsIgnoreCase(type)) {
				value=ConvertUtils.convert(value,Double.class);
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
				value=ConvertUtils.convert(value,Double.class);
				value = ConvertUtils.convert(value, Short.class);
				return value;
			}
			if ("byte".equalsIgnoreCase(type)) {
				value = ConvertUtils.convert(value, Byte.class);
				return value;
			}
			if ("timestamp".equalsIgnoreCase(type) || "datetime".equalsIgnoreCase(type) || "date".equalsIgnoreCase(type)) {
				if(value instanceof String){
				value = getDate(columnWithCondition);
				}
				return value;
			}
			if ("big_decimal".equalsIgnoreCase(type) || "bigDecimal".equalsIgnoreCase(type)) {
				if(!(value instanceof BigDecimal)){
					value = ConvertUtils.convert(value, BigDecimal.class);
				}
				return value;
			}
		}

		return value;
	}

	/**
	 * 转换成当前字段类型 如果值是String类型但字段类型不为String
	 * 
	 * @param column
	 * @param value
	 * @return
	 */
	public Object convertColumn(String column, Object value) {
		ClassMetadata cm = sessionFactory.getClassMetadata(this.entityType);
		String proType = cm.getPropertyType(column).getName();
		return convert(new ColumnWithCondition().setType(proType).setValue(value));
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

	private String getDataSnippet(String value) {
		String fileName = StringUtils.substringBefore(value, ".");
		String property = StringUtils.substringAfter(value, ".");
		PropertiesUtil propertiesUtil = new PropertiesUtil(FileUtils.getClassPath("halo.data", fileName + ".properties"));
		return propertiesUtil.getData(property);
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

	public HqlWithParameter createQueryHql(HaloMap parameter) {
		return createQueryHql(parameter, null);
	}

	/**
	 * 生成动态hql及其参数Map.
	 * 
	 * @param parameter
	 * @return HqlWithParameter
	 */
	private HqlWithParameter createQueryHql(HaloMap parameter, String addSelect) {
		HqlWithParameter hqlWithParameter = new HqlWithParameter();
		StringBuffer hql = new StringBuffer();
		StringBuffer hqlSelect = new StringBuffer();
		StringBuffer order = new StringBuffer();
		StringBuffer group = new StringBuffer();
		StringBuffer column = new StringBuffer();
		boolean queryByBlankFlag = false;
		HaloMap hqlPrmMap = new HaloMap();// ....
		ClassMetadata cm = sessionFactory.getClassMetadata(this.entityType);
		String entityName = cm.getEntityName();

		if (null == parameter || null == addSelect) {
			hqlSelect.append(String.format(" from %s where 1=1 ", entityName));
		} else {
			if (addSelect.indexOf("where") == -1) {
				hqlSelect.append(String.format(" %s where 1=1 ", addSelect));
			} else {
				hqlSelect.append(String.format(" %s ", addSelect));
			}
		}

		if (null != parameter) {
			for (Entry<String, ?> entry : parameter.entrySet()) {
				String key = filterValue(entry.getKey());
				Object value = entry.getValue();
				if (TOBEAN.equals(key)) {
					hqlWithParameter.setAddColumn(true);
					continue;
				}
				if (key.startsWith(ADDCOLUMN)) {
					column = getColumn(column, value);
					continue;
				}
				if (key.startsWith(ADDORDER)) {
					order = getOrder(order, value);
					continue;
				}
				if (key.startsWith(ADDGROUP)) {
					group = getGroup(group, value);
					continue;
				}
				if (key.startsWith(ADDHQL)) {
					value = getHqlSnippet(value.toString());
					hql.append(String.format(" and (%s) ", value));
					continue;// 灵活安全实现接口:根据属性key值 获得hql片段
				}
				ColumnWithCondition columnWithCondition = analyzeKey(key, value);
				String type = columnWithCondition.getType();
				if (null == type) {
					try {
						type = getHibernateType(columnWithCondition.getColumnName(), cm, sessionFactory).getName();
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
					hqlPrmMap.put(columnWithCondition.getColumnName(), value);
					continue;// 添加参数
				}
				if (columnWithCondition.getCondition().equals(HQL)) {
					String hqlKey = columnWithCondition.getColumnName();
					String hqlValue = getHqlSnippet(hqlKey);
					hql.append(String.format(" and (%s) ", hqlValue));
					HaloMap map = getHqlSnippetMap(hqlValue, value);
					hqlPrmMap.putAll(map);
					continue;// 添加参数
				}
				if (null != columnWithCondition.getIfQuery()) {
					if (EX.equals(columnWithCondition.getIfQuery())) {// 始终不查询该字段
						continue;
					}

					if (IN.equals(columnWithCondition.getIfQuery())) {// 始终查询该字段
						if (null == value || "null".equalsIgnoreCase(String.valueOf(entry.getValue()).trim())) {
							hql.append(String.format(" %s %s %s %s null %s ", columnWithCondition.getAndOr(), columnWithCondition.getLeftBracket(), columnWithCondition.getColumnName(), columnWithCondition.getCondition(), columnWithCondition.getRightBracket()));
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
						hql.append(String.format(" %s %s %s %s %s %s ", columnWithCondition.getAndOr(), columnWithCondition.getLeftBracket(), columnWithCondition.getColumnName(), columnWithCondition.getCondition(), columnWithCondition.getDirectValue(), columnWithCondition.getRightBracket()));
						continue;
					}
					if (!StringUtils.isBlank(String.valueOf(value)) || queryByBlankFlag) {
						if (null != type) {
							value = convert(columnWithCondition);
						}
						hql.append(String.format(" %s %s %s %s:%s %s ", columnWithCondition.getAndOr(), columnWithCondition.getLeftBracket(), columnWithCondition.getColumnName(), columnWithCondition.getCondition(), columnWithCondition.getGenColumnName(), columnWithCondition.getRightBracket()));
						hqlPrmMap.put(columnWithCondition.getGenColumnName(), value);
					}
				}
			}// for
			String columnStr = column.toString();
			if (!"".equals(columnStr)) {
				hqlWithParameter.setAddColumn(true);
				String entityIdName = cm.getIdentifierPropertyName();
				hqlSelect = new StringBuffer(String.format("select %s as %s,%s from %s where 1=1 ", entityIdName, entityIdName, columnStr.substring(0, columnStr.length() - 1), entityName));
			}
			String groupStr = group.toString();
			if (!"".equals(groupStr)) {
				hql.append(String.format(" group by %s ", groupStr.substring(0, groupStr.length() - 1)));
			}
			String orderStr = order.toString();
			if (!"".equals(orderStr)) {
				hql.append(String.format(" order by %s ", orderStr.substring(0, orderStr.length() - 1)));
			}

		}
		String hqlStr = hqlSelect.toString() + hql.toString();
		hqlWithParameter.setHql(hqlStr);
		logger.info("HQL:" + hqlStr);
		logger.info("hqlPrmMap:" + hqlPrmMap);
		logger.info("time::" + System.currentTimeMillis());
		hqlWithParameter.setParamterMap(hqlPrmMap);
		return hqlWithParameter;
	}

	/**
	 * 生成动态hql的Query.
	 * 
	 * @param parameter
	 * @return Query
	 */
	public Query createMyQuery(HaloMap parameter) {
		HqlWithParameter hqlWithParameter = createQueryHql(parameter);
		String hql = hqlWithParameter.getHql();

		HaloMap hqlPrmMap = hqlWithParameter.getParamterMap();
		Query query = super.createQuery(hql, hqlPrmMap);
		if (hqlWithParameter.getAddColumn()) {
			query.setResultTransformer(new ColumnToBean(this.entityType));
			// query.setResultTransformer(Transformers.aliasToBean(this.entityType));
		}
		if (hqlWithParameter.getAddCache()) {
			query.setCacheable(true);
		}
		return query;
	}

	/**
	 * 动态生成的hql查询. 一般不会查询值为null值或者为空值的条件. 比如:findListByMap(new
	 * HaloMap().set("userName"
	 * ,"vonchange").set("password","123").set("email",null)).
	 * 查询用户名为vonchange和password为123的结果集. 若要查询email为null
	 * 则为set("email:=:in",null). 比如:findListByMap(new
	 * HaloMap().set("userName:like","vonchange").set("createDate:<=",new
	 * Date()) .set("email:in",new
	 * String[]{"123@vonchange.com","345@vonchange.com"}))
	 * 查询用户名左模糊于vonchange,创建时间小于当前
	 * ,邮箱在"123@vonchange.com","345@vonchange.com"中的结果集 findListByMap(new
	 * HaloMap().set("userName:like","vonchange").set("(createDate:<=",new
	 * Date()) .set("|email:in)",new
	 * String[]{"123@vonchange.com","345@vonchange.com"}))
	 * 查询用户名左模糊于vonchange,创建时间小于当前或者邮箱在
	 * "123@vonchange.com","345@vonchange.com"中的结果集
	 * 
	 * @param parameter
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public <X> List<X> findListByMap(HaloMap parameter) {
		return createMyQuery(parameter).list();
	}

	/**
	 * 根据实体值查询.
	 * 
	 * @param entity
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public <X> List<X> findListByEntity(T entity) {
		HaloMap haloMap = EntityUtils.toHaloMap(entity);
		return createMyQuery(haloMap).list();
	}

	/**
	 * 查询从某条到某条的记录
	 * 
	 * @param parameter
	 * @param begin
	 * @param end
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public <X> List<X> findListByMap(HaloMap parameter, int begin, int end) {
		Query query = createMyQuery(parameter);
		query.setFirstResult(begin);
		query.setMaxResults(end - begin);
		return query.list();
	}

	/**
	 * 查询前num条数据记录
	 * 
	 * @param parameters
	 * @param num
	 * @return List
	 */
	public <X> List<X> findListByMap(HaloMap  parameter, int num) {
		return findListByMap(parameter, 0, num);
	}

	/**
	 * 查询第一条记录
	 * 
	 * @param parameter
	 * @return entity
	 */
	@SuppressWarnings({ "unchecked" })
	public T findFirstByMap(HaloMap parameter) {
		Query query = createMyQuery(parameter);
		query.setFirstResult(0);
		query.setMaxResults(1);
		return (T) query.uniqueResult();
	}

	/**
	 * 查询唯一记录
	 * 
	 * @param parameter
	 * @return entity
	 */
	@SuppressWarnings("unchecked")
	public T findUnique(HaloMap parameter) {
		return (T) createMyQuery(parameter).uniqueResult();
	}

	/**
	 * 生成计算总条数的hql
	 * 
	 * @param hql
	 * @return String
	 */
	private String generateMyCountHql(String hql) {
		hql = "from " + StringUtils.substringAfter(hql, "from");
		hql = StringUtils.substringBefore(hql, "order by");
		return "select count(*) " + hql;
	}

	/**
	 * hql转sql
	 * 
	 * @param hql
	 * @return sql语句
	 */
	public String hqlToSql(String hql) {
		SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) this.getSessionFactory();
		QueryTranslatorFactory queryTranslatorFactory = sessionFactoryImpl.getSettings().getQueryTranslatorFactory();
		FilterTranslator filterTranslator = queryTranslatorFactory.createFilterTranslator(hql, hql, Collections.EMPTY_MAP, sessionFactoryImpl);
		filterTranslator.compile(Collections.EMPTY_MAP, false);
		return filterTranslator.getSQLString();

	}

	/**
	 * 计算总条数
	 * 
	 * @param hql
	 * @param parameter
	 * @return 总条数
	 */
	protected long countMyHqlResult(String hql, HaloMap parameter) {
		String countHql = generateMyCountHql(hql);
		if (countHql.indexOf("group by") != -1) {
			String tempSQL = hqlToSql(countHql);
			String countSQL = "select count(*) from (" + tempSQL + ") temp";
			Query query = this.createSQLQuery(countSQL, parameter.values().toArray());
			return ((Number) query.uniqueResult()).longValue();
		} else {
			return ((Number) findUnique(countHql, parameter)).longValue();
		}

	}

	/**
	 * 分页查询(支持groupBy).
	 * 
	 * @param page
	 * @param parameter
	 * @return Page
	 */
	@SuppressWarnings("unchecked")
	public Page<T> findPageByMap(Page<T> page, HaloMap parameter) {
		notNull(page, "page");
		HqlWithParameter hqlWithParameter = createQueryHql(parameter);
		String hql = hqlWithParameter.getHql();
		HaloMap hqlPrmMap = hqlWithParameter.getParamterMap();
		Query query = super.createQuery(hql, hqlPrmMap);
		if (hqlWithParameter.getAddColumn()) {
			query.setResultTransformer(new ColumnToBean(this.entityType));// Transformers.aliasToBean(this.entityType));
		}
		if (hqlWithParameter.getAddCache()) {
			query.setCacheable(true);
		}
		long totalCount = countMyHqlResult(hql, hqlPrmMap);
		page.setEntityCount((int) totalCount);
		setPageParameterToQuery(query, page);
		page.setEntities(query.list());
		return page;
	}

	/**
	 * 生成SQLQuery.
	 * 
	 * @param sql
	 * @param 可变参数
	 * @return SQLQuery
	 */
	private SQLQuery createSQLQuery(String sql, Object... parameter) {
		SQLQuery q = getSession().createSQLQuery(sql);
		if (parameter != null) {
			for (int i = 0; i < parameter.length; i++) {
				q.setParameter(i, parameter[i]);
			}
		}
		return q;
	}

	/**
	 * 生成SQLQuery
	 * 
	 * @param sql
	 * @param Map
	 * @return SQLQuery
	 */
	private SQLQuery createSQLQuery(String sql, Map<String, ?> parameter) {
		SQLQuery q = getSession().createSQLQuery(sql);
		q.setProperties(parameter);
		return q;
	}

	/**
	 * 生成存储过程的SQLQuery.
	 * @param 存储过程名及参数占位符
	 * @param parameter
	 * @return SQLQuery
	 */
	public SQLQuery createProcQuery(String procedure, Map<String, ?> parameter) {
		if (null == parameter) {
			parameter = new HaloMap();
		}
		String sql = String.format("{Call %s }", procedure);
		SQLQuery query = createSQLQuery(sql, parameter);
		return query;
	}

	/**
	 * 生成的存储过程结果集可以映射到实体上.
	 * 
	 * @param procedureName
	 * @param parameter
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public <X> List<X> findListByProc(String procedureName, Map<String, ?> parameter) {
		SQLQuery query = createProcQuery(procedureName, parameter);
		query.setResultTransformer(new ColumnToBean(this.entityType));
		return query.list();
	}

	/**
	 * 根据HaloMap删除
	 * 
	 * @param parameter
	 * @return 返回行数 失败返回-1
	 */
	public int deleteByMap(HaloMap parameter) {
		if (null == parameter || parameter.isEmpty()) {
			return -1;
		}
		ClassMetadata cm = sessionFactory.getClassMetadata(this.entityType);
		String entityName = cm.getEntityName();
		String selectHql = String.format("delete %s ", entityName);
		HqlWithParameter hqlWithParameter = createQueryHql(parameter, selectHql);
		String hql = hqlWithParameter.getHql();
		String tempHql = StringUtils.substringAfter(hql, "where 1=1");
		if ("".equals(tempHql.replaceAll(SPACE, ""))) {
			return -1;
		}
		HaloMap hqlPrmMap = hqlWithParameter.getParamterMap();
		Query query = super.createQuery(hql, hqlPrmMap);
		return query.executeUpdate();
	}

	/**
	 * 
	 * updateWithNotNull 更新不为null的字段
	 * 
	 * @param entity
	 */
	public void updateWithNotNull(T entity) {
		Updater<T> updater = new Updater<T>(entity);
		updateByUpdater(updater);
	}

	/**
	 * 根据Upadter 更新实体
	 * 
	 * @param updater
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	private T updateByUpdater(Updater<T> updater) {
		ClassMetadata cm = sessionFactory.getClassMetadata(this.entityType);
		T bean = updater.getBean();
		T po = (T) getSession().get(this.entityType, cm.getIdentifier(bean, (SessionImplementor) this.getSession()));
		// T po =(T) getSession().get(this.entityType, cm.getIdentifier(bean,
		// POJO));
		updaterCopyToPersistentObject(updater, po, cm);
		return po;
	}

	/**
	 * 将更新对象拷贝至实体对象
	 * 
	 * @param updater
	 * @param po
	 * @param cm
	 */
	private void updaterCopyToPersistentObject(Updater<T> updater, T po, ClassMetadata cm) {
		String[] propNames = cm.getPropertyNames();
		String identifierName = cm.getIdentifierPropertyName();
		T bean = updater.getBean();
		Object value;
		for (String propName : propNames) {
			if (propName.equals(identifierName)) {
				continue;
			}
			try {
				value = MyBeanUtils.getSimpleProperty(bean, propName);
				if (!updater.isUpdate(propName, value)) {
					continue;
				}
				cm.setPropertyValue(po, propName, value, EntityMode.POJO);// ,
																			// POJO
			} catch (Exception e) {
				throw new RuntimeException("copy property to persistent object failed: '" + propName + "'", e);
			}
		}
	}

	/**
	 * 获得更新hql
	 * 
	 * @param entity
	 * @param parameter
	 * @return String
	 */
	private String getUpdateHql(T entity, HaloMap parameter) {
		ClassMetadata cm = sessionFactory.getClassMetadata(this.entityType);
		String[] propNames = cm.getPropertyNames();
		String identifierName = cm.getIdentifierPropertyName();
		String entityName = cm.getEntityName();
		StringBuffer updateHql = new StringBuffer();
		for (String propName : propNames) {
			if (propName.equals(identifierName)) {
				continue;
			}
			Object value = MyBeanUtils.getSimpleProperty(entity, propName);
			if (null != value) {
				parameter.put(propName + ":prm", value);
				updateHql.append(String.format(" %s=:%s ,", propName, propName));
			}
		}
		String tempUpdateHql = updateHql.toString();
		if ("".equals(updateHql)) {
			return null;
		}
		Object value = MyBeanUtils.getSimpleProperty(entity, identifierName);
		if (null != value) {
			parameter.put(identifierName, value);
		}
		updateHql = new StringBuffer();
		updateHql.append(String.format("update %s set %s where 1=1 ", entityName, tempUpdateHql.substring(0, tempUpdateHql.length() - 1)));
		return updateHql.toString();
	}

	/**
	 * 根据haloMap更新不为null的实体
	 * 
	 * @param entity
	 * @param parameter
	 * @return 更新条数
	 */
	public int updateWithNotNullByHql(T entity, HaloMap parameter) {
		HaloMap newParameter = new HaloMap();
		String updateHql = getUpdateHql(entity, newParameter);
		if (null == updateHql) {
			return -1;
		}
		newParameter.putAll(parameter);
		HqlWithParameter hqlWithParameter = createQueryHql(newParameter, updateHql);
		String hql = hqlWithParameter.getHql();
		String tempHql = StringUtils.substringAfter(hql, "where 1=1");
		if ("".equals(tempHql.replaceAll(SPACE, ""))) {
			return -1;
		}
		HaloMap hqlPrmMap = hqlWithParameter.getParamterMap();
		Query query = super.createQuery(hql, hqlPrmMap);
		return query.executeUpdate();
	}

	/**
	 * 自定义hql方式更新实体
	 * 
	 * @param entity
	 * @return 更新条数
	 */
	public int updateWithNotNullByHql(T entity) {
		return updateWithNotNullByHql(entity, new HaloMap());
	}

	private String getViewSql(String viewName, HaloMap tplMap) {
		String packStr = "";
		if (viewName.indexOf(".") != -1) {
			packStr = "." + StringUtils.substringBeforeLast(viewName, ".");
			viewName = StringUtils.substringAfterLast(viewName, ".");
		}
		FreemarkerUtils freemarkerUtils = new FreemarkerUtils();
		File viewPath = FileUtils.getClassPath(HALOPACH + ".view" + packStr, "");
		return freemarkerUtils.generateString(tplMap, viewPath, viewName + ".halo");
	}

	private String getViewSql(String viewName) {
		String packStr = "";
		if (viewName.indexOf(".") != -1) {
			packStr = "." + StringUtils.substringBeforeLast(viewName, ".");
			viewName = StringUtils.substringAfterLast(viewName, ".");
		}
		File viewFile = FileUtils.getClassPath(HALOPACH + ".view" + packStr, viewName + ".halo");
		return FileUtils.writeToString(viewFile);
	}

	/**
	 * 直接获得文件中的sql语句的SQLQuery,不支持动态拼接.
	 * 
	 * @param haloview文件名
	 * @param parameter
	 * @return SQLQuery
	 */
	public SQLQuery CreateSqlQueryByHaloView(String viewName, HaloMap parameter) {
		return createSQLQuery(getViewSql(viewName), parameter);
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
		ClassMetadata cm = sessionFactory.getClassMetadata(this.entityType);
		if (viewName.indexOf(".") != -1) {
			viewAs = StringUtils.substringAfterLast(viewName, ".");
		}
		for (Entry<String, ?> entry : parameter.entrySet()) {
			String key = filterValue(entry.getKey());
			Object value = entry.getValue();
			if (key.startsWith(ADDCOLUMN)) {
				column = getColumn(column, value);
				continue;
			}
			if (key.startsWith(ADDORDER)) {
				order = getOrder(order, value);
				continue;
			}
			if (key.startsWith(ADDGROUP)) {
				group = getGroup(group, value);
				continue;
			}
			if (key.startsWith(ADDHQL)) {
				value = getHqlSnippet(value.toString());
				sql.append(String.format(" and (%s) ", TableUtil.toTable(value.toString())));
				continue;// 灵活但不安全接口:sql查询片段
			}

			ColumnWithCondition columnWithCondition = analyzeKey(key, value);
			String type = columnWithCondition.getType();
			if (null == type) {
				try {
					type = getHibernateType(columnWithCondition.getColumnName(), cm, sessionFactory).getName();
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
			if (columnWithCondition.getCondition().equals(HQL)) {
				String hqlKey = columnWithCondition.getColumnName();
				String hqlValue = getHqlSnippet(hqlKey);
				HaloMap map = getHqlSnippetMap(hqlValue, value);
				sqlPrmMap.putAll(map);
				hqlValue = TableUtil.toTable(hqlValue);
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
						value = SPACE + TableUtil.toTable(String.valueOf(value)) + SPACE;
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
						sql.append(String.format(" %s %s %s %s null %s ", columnWithCondition.getAndOr(), columnWithCondition.getLeftBracket(), TableUtil.toTable(columnWithCondition.getColumnName()), columnWithCondition.getCondition(), columnWithCondition.getRightBracket()));
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
					sql.append(String.format(" %s %s %s %s %s %s ", columnWithCondition.getAndOr(), columnWithCondition.getLeftBracket(), TableUtil.toTable(columnWithCondition.getColumnName()), columnWithCondition.getCondition(), columnWithCondition.getDirectValue(), columnWithCondition.getRightBracket()));
					continue;
				}
				if (!StringUtils.isBlank(String.valueOf(value)) || queryByBlankFlag) {
					if (null != type) {
						value = convert(columnWithCondition);
					}
					sql.append(String.format(" %s %s %s %s :%s %s ", columnWithCondition.getAndOr(), columnWithCondition.getLeftBracket(), TableUtil.toTable(columnWithCondition.getColumnName()), columnWithCondition.getCondition(), columnWithCondition.getGenColumnName(), columnWithCondition.getRightBracket()));
					sqlPrmMap.put(columnWithCondition.getGenColumnName(), value);
				}
			}
		}// for
		String columnStr = TableUtil.toTable(column.toString());
		String viewSql = getViewSql(viewName, tplMap);
		if (!"".equals(columnStr)) {
			String entityIdName = TableUtil.toTable(cm.getIdentifierPropertyName());
			sqlSelect = new StringBuffer(String.format("select %s as %s ,%s from (%s) %s where 1=1 ", entityIdName, entityIdName, columnStr.substring(0, columnStr.length() - 1), viewSql, viewAs));
		} else {
			sqlSelect.append(String.format("select *  from (%s) %s  where 1=1 ", viewSql, viewAs));
		}
		String groupStr = TableUtil.toTable(group.toString());
		if (!"".equals(groupStr)) {
			sql.append(String.format(" group by %s ", groupStr.substring(0, groupStr.length() - 1)));
		}
		String orderStr = TableUtil.toTable(order.toString());
		if (!"".equals(orderStr)) {
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
		query.setResultTransformer(new ColumnToBean(this.entityType));
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
		SQLQuery query = CreateMySqlQueryByHaloView(viewName, parameter);
		return query.list();
	}

	/**
	 * 生成总条数的sql语句.
	 * 
	 * @param sql
	 * @return sql语句
	 */
	private String generateMyCountSql(String sql) {
		sql = StringUtils.substringAfter(sql, " from ");
		//sql = StringUtils.substringBefore(sql, "order by");
		ClassMetadata cm = sessionFactory.getClassMetadata(this.entityType);
		String entityIdName = TableUtil.toTable(cm.getIdentifierPropertyName());
		return String.format("select count(%s) from (select %s from %s)temp ", entityIdName, entityIdName, sql);
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

	/**
	 * haloView的分页查询
	 * 
	 * @param viewName
	 * @param page
	 * @param parameter
	 * @return Page
	 */
	@SuppressWarnings("unchecked")
	public Page<T> findPageByHaloView(String viewName, Page<T> page, HaloMap parameter) {
		notNull(page, "page");
		SqlWithParameter sqlWithParameter = createMySqlQuery(viewName, parameter);
		String sql = sqlWithParameter.getSql();
		HaloMap hqlPrmMap = sqlWithParameter.getParamterMap();
		SQLQuery query = createSQLQuery(sql, hqlPrmMap);
		query.setResultTransformer(new ColumnToBean(this.entityType));
		long totalCount = countMySqlResult(sql, hqlPrmMap);
		page.setEntityCount((int) totalCount);
		setPageParameterToQuery(query, page);
		page.setEntities(query.list());
		return page;
	}
}
