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
import com.ht.halo.hibernate3.base.FileUtils;
import com.ht.halo.hibernate3.base.MyBeanUtils;
import com.ht.halo.hibernate3.base.Page;
import com.ht.halo.hibernate3.base.TableUtil;
import com.ht.halo.hibernate3.base.Updater;
import com.ht.halo.hibernate3.bean.ColumnWithCondition;
import com.ht.halo.hibernate3.bean.HqlWithParameter;
import com.ht.halo.hibernate3.bean.SqlWithParameter;
import com.ht.halo.hibernate3.feemarker.FreemarkerUtils;
import com.ht.halo.hibernate3.map.HaloMap;

/**
 * @ClassName: HaloDao
 * @Description: TODO 基于hibernate的通用Dao
 * @author fengchangyi
 * @date 2014-12-20 下午3:14:10
 * @version 3.0 
 */
public class HaloDao<T, PK extends Serializable> extends BaseHibernateDao<T, Serializable> {
	private static final Log logger = LogFactory.getLog(HaloDao.class);
	private static final String ADDSELECT = "addSelect";// 查询表

	public static final String ADDCOLUMN = "addColumn";// 添加查询字段,默认查询出主键
	public static final String ADDORDER = "addOrder";// 添加排序
	public static final String ADDGROUP = "addGroup";// 添加排序
	public static final String ADDHQL = "addHql";// 添加查询hql片段
	private static final String PRM = "prm";// 添加查询hql中的参数标识
	private static final String DATA = "data";//haloView中的模板数据
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
	private static final String[] PATTERN = new String[] { "yyyy", "yyyy-MM", "yyyy-MM-dd", "MM-dd", "yyyy-MM-dd HH", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss", "yyyy年MM月dd日", "yyyy年MM月dd日 HH:mm:ss", "yyyyMM", "yyyyMMdd", "yyyy/MM", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss" };

	private SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	private Session getSession() {
		return getSessionFactory().getCurrentSession();
	}

	private String removeAllSpace(String value) {
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
	 * 扩展like条件.
	 * 1.全模糊5like5,左模糊5like
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
	 * 扩展日期条件.
	 * 1.daylike,monthlike 当天,当月
	 * 2.dayge dayle 重某天到某天
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
			columnWithCondition.setDirectValue("'"+DateUtils.format("yyyy-MM-dd", date) + like+"'");
			return columnWithCondition;
		}
		if ("month".equals(extCondtion) || "MM".equals(extCondtion)) {
			Date date = (Date) getDate(columnWithCondition);
			if (addOne) {
				date = DateUtils.addMonths(date, 1);
				columnWithCondition.setCondition("<");
			}
			columnWithCondition.setDirectValue("'"+DateUtils.format("yyyy-MM", date) + like+"'");
			return columnWithCondition;
		}
		if ("year".equals(extCondtion) || "yyyy".equals(extCondtion)) {
			Date date = (Date) getDate(columnWithCondition);
			if (addOne) {
				date = DateUtils.addYears(date, 1);
				columnWithCondition.setCondition("<");
			}
			columnWithCondition.setDirectValue("'"+DateUtils.format("yyyy", date) + like+"'");
			return columnWithCondition;
		}
		if ("hour".equals(extCondtion) || "HH".equals(extCondtion)) {
			Date date = (Date) getDate(columnWithCondition);
			if (addOne) {
				date = DateUtils.addHours(date, 1);
				columnWithCondition.setCondition("<");
			}
			columnWithCondition.setDirectValue("'"+DateUtils.format("yyyy-MM-dd HH", date) + like+"'");
			return columnWithCondition;
		}
		if ("minute".equals(extCondtion) || "mm".equals(extCondtion)) {
			Date date = (Date) getDate(columnWithCondition);
			if (addOne) {
				date = DateUtils.addMinutes(date, 1);
				columnWithCondition.setCondition("<");
			}
			columnWithCondition.setDirectValue("'"+DateUtils.format("yyyy-MM-dd HH:mm", date) + like+"'");
			return columnWithCondition;
		}

		if ("second".equals(extCondtion) || "ss".equals(extCondtion)) {
			Date date = (Date) getDate(columnWithCondition);
			if (addOne) {
				date = DateUtils.addSeconds(date, 1);
				columnWithCondition.setCondition("<");
			}
			columnWithCondition.setDirectValue("'"+DateUtils.format("yyyy-MM-dd HH:mm:ss", date) + like+"'");
			return columnWithCondition;
		}

		return columnWithCondition;
	}

	/**
	 * @Title: analyzeKey
	 * @Description: TODO 分析key值
	 * @param key
	 * @return
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
	 * @Title: getHibernateType
	 * @Description: TODO 取自锐道hibernateUtil类 获取类型 包含关联的
	 * @param property
	 * @param classMetadata
	 * @param sessionFactory
	 * @return
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
	 * 
	 * @Title: convert
	 * @Description: TODO 转换成当前字段类型 如果值是String类型但字段类型不为String 其他类型转换(请扩展)
	 * @param proType
	 * @param value
	 * @return
	 */
	private Object convert(ColumnWithCondition columnWithCondition) {
		Object value = columnWithCondition.getValue();
		String type = columnWithCondition.getType();
		if (value instanceof String) {
			if (!"string".equalsIgnoreCase(type)) {
				if ("integer".equalsIgnoreCase(type)) {
					value = Integer.parseInt(String.valueOf(value));
					return value;
				}
				if ("long".equalsIgnoreCase(type)) {
					value = Long.parseLong(String.valueOf(value));
					return value;
				}
				if ("boolean".equalsIgnoreCase(type)) {
					if ("1".equals(String.valueOf(value))) {
						value = "true";
					}// 为1时也可以

					value = Boolean.parseBoolean(String.valueOf(value));
					return value;
				}
				if ("double".equalsIgnoreCase(type)) {
					value = Double.parseDouble(String.valueOf(value));
					return value;
				}
				if ("float".equalsIgnoreCase(type)) {
					value = Float.parseFloat(String.valueOf(value));
					return value;
				}
				if ("short".equalsIgnoreCase(type)) {
					value = Short.parseShort(String.valueOf(value));
					return value;
				}
				if ("byte".equalsIgnoreCase(type)) {
					value = Byte.parseByte(String.valueOf(value));
					return value;
				}
				if ("timestamp".equalsIgnoreCase(type) || "datetime".equalsIgnoreCase(type) || "date".equalsIgnoreCase(type)) {
					value = getDate(columnWithCondition);
					return value;
				}
				if ("big_decimal".equalsIgnoreCase(type) || "bigDecimal".equalsIgnoreCase(type)) {
					value = new BigDecimal(String.valueOf(value));
					return value;
				}

			}// 不为string类型
		}

		return value;
	}

	/**
	 * @Title: convertColumn
	 * @Description: TODO 转换成当前字段类型 如果值是String类型但字段类型不为String
	 * @param column
	 * @param value
	 * @return
	 */
	public Object convertColumn(String column, Object value) {
		ClassMetadata cm = sessionFactory.getClassMetadata(this.entityType);
		String proType = cm.getPropertyType(column).getName();
		return convert(new ColumnWithCondition().setType(proType).setValue(value));
	}

	public HqlWithParameter createQueryHql(Map<String,?> parameter) {
		HqlWithParameter hqlWithParameter = new HqlWithParameter();
		StringBuffer hql = new StringBuffer();
		StringBuffer hqlSelect = new StringBuffer();
		StringBuffer order = new StringBuffer();
		StringBuffer group = new StringBuffer();
		StringBuffer column = new StringBuffer();
		boolean queryByBlankFlag = false;
		Map<String,Object> hqlPrmMap = new HaloMap();// ....
		ClassMetadata cm = sessionFactory.getClassMetadata(this.entityType);
		String entityName = cm.getEntityName();

		if (null == parameter || null == parameter.get(ADDSELECT)) {
			hqlSelect.append(String.format(" from %s where 1=1 ", entityName));
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
				if (ADDSELECT.equals(key)) {
					if (String.valueOf(value).indexOf("where") == -1) {
						hqlSelect.append(String.format(" %s where 1=1 ", value));
					} else {
						hqlSelect.append(String.format(" %s ", value));
					}
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
					hql.append(String.format(" and (%s) ", value.toString()));
					continue;// 灵活但不安全接口:hql查询片段
				}
				ColumnWithCondition columnWithCondition = analyzeKey(key, value);
				String type = columnWithCondition.getType();
				if (null == type) {
					try {
					type = getHibernateType(columnWithCondition.getColumnName(), cm, sessionFactory).getName();
					} catch (Exception e) {
						logger.warn("could not resolve property(无法得到字段类型)");
						type=null;
					}
					columnWithCondition.setType(type);
				}
				if (EX.equals(columnWithCondition.getCondition())) {
					continue;// 条件为"ex"时排除该参数
				}

				if (columnWithCondition.getCondition().equals(PRM)) {
					if(null!=type){
					value = convert(columnWithCondition);
					}
					hqlPrmMap.put(columnWithCondition.getColumnName(), value);
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
						if(null!=type){
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
	 * @Title: createMyQuery
	 * @Description: TODO 自定义参数生成Query
	 * @param parameter
	 * @return
	 */
	public Query createMyQuery(Map<String,?> parameter) {
		HqlWithParameter hqlWithParameter = createQueryHql(parameter);
		String hql = hqlWithParameter.getHql();

		Map<String,Object> hqlPrmMap = hqlWithParameter.getParamterMap();
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

	@SuppressWarnings("unchecked")
	public <X> List<X> findListByMap(Map<String,?> parameter) {
		return createMyQuery(parameter).list();
	}

	@SuppressWarnings("unchecked")
	public <X> List<X> findListByEntity(T entity) {
		Map<String,Object> haloMap = EntityUtils.toHaloMap(entity);
		return createMyQuery(haloMap).list();
	}

	@SuppressWarnings("unchecked")
	public <X> List<X> findListByMap(Map<String,?> parameter, int begin, int end) {
		Query query = createMyQuery(parameter);
		query.setFirstResult(begin);
		query.setMaxResults(end - begin);
		return query.list();
	}

	/**
	 * @Title: findEntityList
	 * @Description: TODO 查询前num条数据
	 * @param parameters
	 * @param num
	 * @return
	 */
	public <X> List<X> findListByMap(Map<String,?> parameter, int num) {
		return findListByMap(parameter, 0, num);
	}

	@SuppressWarnings({ "unchecked" })
	public T findFirstByMap(Map<String,?> parameter) {
		Query query = createMyQuery(parameter);
		query.setFirstResult(0);
		query.setMaxResults(1);
		return (T) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public T findUnique(Map<String,?> parameter) {
		return (T) createMyQuery(parameter).uniqueResult();
	}

	private String generateMyCountHql(String hql) {
		hql = "from " + StringUtils.substringAfter(hql, "from");
		hql = StringUtils.substringBefore(hql, "order by");
		return "select count(*) " + hql;
	}

	private String hqlToSql(String hql) {
		SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) this.getSessionFactory();
		QueryTranslatorFactory queryTranslatorFactory = sessionFactoryImpl.getSettings().getQueryTranslatorFactory();
		FilterTranslator filterTranslator = queryTranslatorFactory.createFilterTranslator(hql, hql, Collections.EMPTY_MAP, sessionFactoryImpl);
		filterTranslator.compile(Collections.EMPTY_MAP, false);
		return filterTranslator.getSQLString();

	}

	protected long countMyHqlResult(String hql, Map<String,?> parameter) {
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

	@SuppressWarnings("unchecked")
	public Page<T> findPageByMap(Page<T> page, Map<String,?> parameter) {
		notNull(page, "page");
		HqlWithParameter hqlWithParameter = createQueryHql(parameter);
		String hql = hqlWithParameter.getHql();
		Map<String,Object> hqlPrmMap = hqlWithParameter.getParamterMap();
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

	private SQLQuery createSQLQuery(String sql, Object... parameter) {
		SQLQuery q = getSession().createSQLQuery(sql);
		if (parameter != null) {
			for (int i = 0; i < parameter.length; i++) {
				q.setParameter(i, parameter[i]);
			}
		}
		return q;
	}


	private SQLQuery createSQLQuery(String sql, Map<String,?> parameter) {
		SQLQuery q = getSession().createSQLQuery(sql);
		q.setProperties(parameter);
		return q;
	}

	public SQLQuery createProcQuery(String procedureName, Map<String,?> parameter) {
		if (null == parameter) {
			parameter = new HaloMap();
		}
		StringBuffer stringBuffer = new StringBuffer();
		for (Entry<String, ?> entry : parameter.entrySet()) {
			stringBuffer.append(":" + entry.getKey() + ",");
		}
		String result = stringBuffer.toString();
		if (!"".equals(result)) {
			result = result.substring(0, result.length() - 1);
		}
		String sql = String.format("{Call %s(%s) }", procedureName, result);
		SQLQuery query = createSQLQuery(sql, parameter);
		return query;
	}

	@SuppressWarnings("unchecked")
	public <X> List<X> findListByProc(String procedureName, Map<String,?> parameter) {
		SQLQuery query = createProcQuery(procedureName, parameter);
		query.setResultTransformer(new ColumnToBean(this.entityType));
		return query.list();
	}

	/**
	 * @Title: deleteByMap
	 * @Description: TODO 根据HaloMap删除
	 * @param parameter
	 * @return 返回行数 失败返回-1
	 */
	public int deleteByMap(Map<String,Object> parameter) {
		if (null == parameter || parameter.isEmpty()) {
			return -1;
		}
		ClassMetadata cm = sessionFactory.getClassMetadata(this.entityType);
		String entityName = cm.getEntityName();
		String selectHql = String.format("delete %s ", entityName);
		parameter.put(ADDSELECT, selectHql);
		HqlWithParameter hqlWithParameter = createQueryHql(parameter);
		String hql = hqlWithParameter.getHql();
		String tempHql = StringUtils.substringAfter(hql, "where 1=1");
		if ("".equals(tempHql.replaceAll(SPACE, ""))) {
			return -1;
		}
		Map<String,Object> hqlPrmMap = hqlWithParameter.getParamterMap();
		Query query = super.createQuery(hql, hqlPrmMap);
		return query.executeUpdate();
	}

	/**
	 * 
	 * @Title: updateWithNotNull
	 * @Description: TODO 更新不为null的字段
	 * @param entity
	 */
	public void updateWithNotNull(T entity) {
		Updater<T> updater = new Updater<T>(entity);
		updateByUpdater(updater);
	}

	/**
	 * 
	 * @Title: updateByUpdater
	 * @Description: TODO 根据Upadter 更新实体
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
	 * 
	 * @Title: updaterCopyToPersistentObject
	 * @Description: TODO 将更新对象拷贝至实体对象
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

	private String getUpdateHql(T entity, Map<String,Object> parameter) {
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
	 * @Title: updateByHql
	 * @Description: TODO 根据hql更新 不更新为null的值
	 * @param entity
	 * @param parameter
	 * @return
	 */
	public int updateWithNotNullByHql(T entity, Map<String,?> parameter) {
		Map<String,Object> newParameter = new HaloMap();
		String updateHql = getUpdateHql(entity, newParameter);
		if (null == updateHql) {
			return -1;
		}
		newParameter.put(ADDSELECT, updateHql);
		newParameter.putAll(parameter);
		HqlWithParameter hqlWithParameter = createQueryHql(newParameter);
		String hql = hqlWithParameter.getHql();
		String tempHql = StringUtils.substringAfter(hql, "where 1=1");
		if ("".equals(tempHql.replaceAll(SPACE, ""))) {
			return -1;
		}
		Map<String,Object> hqlPrmMap = hqlWithParameter.getParamterMap();
		Query query = super.createQuery(hql, hqlPrmMap);
		return query.executeUpdate();
	}

	public int updateWithNotNullByHql(T entity) {
		return updateWithNotNullByHql(entity, new HaloMap());
	}

	private String getViewSql(String viewName,Map<String,Object> tplMap) {
		String packStr = "";
		if (viewName.indexOf(".") != -1) {
			packStr = "." + StringUtils.substringBeforeLast(viewName, ".");
			viewName = StringUtils.substringAfterLast(viewName, ".");
		}
		FreemarkerUtils freemarkerUtils= new FreemarkerUtils();
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
	 *  直接获得文件中的sql语句的SQLQuery,不支持动态拼接.
	 * @param haloview文件名
	 * @param parameter 
	 * @return SQLQuery
	 */
	public SQLQuery CreateSqlQueryByHaloView(String viewName, Map<String,?> parameter) {
		return createSQLQuery(getViewSql(viewName), parameter);
	}

	/**
	 *  根据haloview文件结果集查询获得SQLQuery和参数.
	 *  方式代码基本与createMyQuery相同,未重用
	 * @param viewName
	 * @param parameter
	 * @return SqlWithParameter
	 */
	public SqlWithParameter createMySqlQuery(String viewName, Map<String,?> parameter) {
	
		String viewAs="temp";
		SqlWithParameter sqlWithParamter = new SqlWithParameter();
		StringBuffer sql = new StringBuffer();
		StringBuffer sqlSelect = new StringBuffer();
		StringBuffer order = new StringBuffer();
		StringBuffer group = new StringBuffer();
		StringBuffer column = new StringBuffer();
		boolean queryByBlankFlag = false;
		Map<String,Object> sqlPrmMap = new HaloMap();// ....
		Map<String,Object> tplMap= new HaloMap();
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
				sql.append(String.format(" and (%s) ",TableUtil.toTable(value.toString())));
				continue;// 灵活但不安全接口:sql查询片段
			}

			ColumnWithCondition columnWithCondition = analyzeKey(key, value);
			String type = columnWithCondition.getType();
			if (null == type) {
				try {
				type = getHibernateType(columnWithCondition.getColumnName(), cm, sessionFactory).getName();
				}catch (Exception e) {
					logger.warn("could not resolve property(无法得到字段类型)");
					type=null;
				}
				columnWithCondition.setType(type);
			}
			if (EX.equals(columnWithCondition.getCondition())) {
				continue;// 条件为"ex"时排除该参数
			}
			if (columnWithCondition.getCondition().equals(PRM)) {
				if(null!=type){
				value = convert(columnWithCondition);
				}
				sqlPrmMap.put(columnWithCondition.getColumnName(), value);
				continue;// 添加参数
			}
			if (columnWithCondition.getCondition().equals(DATA)) {
				if(value instanceof String){
					value=TableUtil.toTable(String.valueOf(value));
				}
				tplMap.put(columnWithCondition.getColumnName(), value);
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
					if(null!=type){
					value = convert(columnWithCondition);
					}
					sql.append(String.format(" %s %s %s %s :%s %s ", columnWithCondition.getAndOr(), columnWithCondition.getLeftBracket(), TableUtil.toTable(columnWithCondition.getColumnName()), columnWithCondition.getCondition(), columnWithCondition.getGenColumnName(), columnWithCondition.getRightBracket()));
					sqlPrmMap.put(columnWithCondition.getGenColumnName(), value);
				}
			}
		}// for
		String columnStr = TableUtil.toTable(column.toString());
		String viewSql = getViewSql(viewName,tplMap);
		if (!"".equals(columnStr)) {
			String entityIdName = TableUtil.toTable(cm.getIdentifierPropertyName());
			sqlSelect = new StringBuffer(String.format("select %s as %s ,%s from (%s) %s where 1=1 ", entityIdName, entityIdName, columnStr.substring(0, columnStr.length() - 1), viewSql, viewAs));
		}else{
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
	 * @param viewName
	 * @param parameter
	 * @return SQLQuery
	 */
	public SQLQuery CreateMySqlQueryByHaloView(String viewName, Map<String,?> parameter) {
		SqlWithParameter sqlWithParameter = createMySqlQuery(viewName, parameter);
		String sql = sqlWithParameter.getSql();
		Map<String,Object> hqlPrmMap = sqlWithParameter.getParamterMap();
		SQLQuery query = createSQLQuery(sql, hqlPrmMap);
		query.setResultTransformer(new ColumnToBean(this.entityType));
		return query;
	}

	/**
	 * 
	 * @param viewName:haloView的sql文件名,可以加入包名:test.ViewTest(全:halo.view.test.ViewTest.halo)
	 * @param parameter
	 * @return {@link List}
	 */
	@SuppressWarnings("unchecked")
	public <X> List<X> findListByHaloView(String viewName, Map<String,?> parameter) {
		SQLQuery query = CreateMySqlQueryByHaloView(viewName, parameter);
		return query.list();
	}

	private String generateMyCountSql(String sql) {
		sql = StringUtils.substringAfter(sql, " from ");
		sql = StringUtils.substringBefore(sql, "order by");
		ClassMetadata cm = sessionFactory.getClassMetadata(this.entityType);
		String entityIdName = TableUtil.toTable(cm.getIdentifierPropertyName());
		return String.format("select count(%s) from (select %s from %s)temp ", entityIdName, entityIdName, sql);
	}

	protected long countMySqlResult(String sql, Map<String,?> parameter) {
		String countSql = generateMyCountSql(sql);
		Query query = this.createSQLQuery(countSql, parameter);
		return ((Number) query.uniqueResult()).longValue();
	}

	@SuppressWarnings("unchecked")
	public Page<T> findPageByHaloView(String viewName, Page<T> page, Map<String,?> parameter) {
		notNull(page, "page");
		SqlWithParameter sqlWithParameter = createMySqlQuery(viewName, parameter);
		String sql = sqlWithParameter.getSql();
		Map<String,Object> hqlPrmMap = sqlWithParameter.getParamterMap();
		SQLQuery query = createSQLQuery(sql, hqlPrmMap);
		query.setResultTransformer(new ColumnToBean(this.entityType));
		long totalCount = countMySqlResult(sql, hqlPrmMap);
		page.setEntityCount((int) totalCount);
		setPageParameterToQuery(query, page);
		page.setEntities(query.list());
		return page;
	}
}
