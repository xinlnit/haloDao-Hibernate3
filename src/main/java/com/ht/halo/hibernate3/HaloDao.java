package com.ht.halo.hibernate3;

import static org.hibernate.EntityMode.POJO;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.EntityMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.EntityType;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;

import com.ht.halo.base.HaloBase;
import com.ht.halo.dao.IHaloDao;
import com.ht.halo.hibernate3.base.Assert;
import com.ht.halo.hibernate3.base.ColumnToBean;
import com.ht.halo.hibernate3.base.ColumnToMap;
import com.ht.halo.hibernate3.base.HibernateUtils;
import com.ht.halo.hibernate3.base.MyBeanUtils;
import com.ht.halo.hibernate3.base.MyEntityUtils;
import com.ht.halo.hibernate3.base.Page;
import com.ht.halo.hibernate3.base.Updater;
import com.ht.halo.hibernate3.bean.ColumnWithCondition;
import com.ht.halo.hibernate3.bean.HqlWithParameter;
import com.ht.halo.hibernate3.map.MyHashMap;
import com.ht.halo.hibernate3.map.MyLinkedHashMap;
import com.ht.halo.hibernate3.utils.ConvertUtils;
import com.ht.halo.hibernate3.utils.DateUtils;
import com.ht.halo.hibernate3.utils.HaloUtils;
import com.ht.halo.hibernate3.utils.StringUtils;
import com.ht.halo.hibernate3.utils.file.FileUtils;
import com.ht.halo.hibernate3.utils.tpl.ITplUtils;
import com.ht.halo.hibernate3.utils.tpl.freemarker.FreemarkerUtils;
import com.ht.halo.hibernate3.utils.xml.XmlUtils;

/**
 * @ClassName: HaloDao
 * @Description: TODO 基于hibernate的通用Dao
 * @author fengchangyi
 * @date 2014-12-20 下午3:14:10
 * @version 3.0
 */
public class HaloDao<T, PK extends Serializable> extends HaloBase implements IHaloDao<T, PK> {

	protected SessionFactory sessionFactory;
	protected Class<T> entityType = getEntityType();

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

	protected String getIdPropertyName() {
		ClassMetadata meta = getSessionFactory().getClassMetadata(entityType);
		return meta.getIdentifierPropertyName();
	}

	/**
	 * @Title: get
	 * @Description: TODO 不提供使用get方式,还是load方式
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T get(PK id) {
		return (T) getSession().load(entityType, id);
	}

	@SuppressWarnings("unchecked")
	public T load(PK id) {
		return (T) getSession().load(entityType, id);
	}

	@SuppressWarnings("unchecked")
	public T load(T entity) {
		ClassMetadata cm = sessionFactory.getClassMetadata(this.entityType);
		return (T) getSession().load(this.entityType, cm.getIdentifier(entity, (SessionImplementor) getSession()));
	}

	public Query createQuery(String hql, Object... parameters) {
		Query query = getSession().createQuery(hql);
		if (parameters != null) {
			for (int i = 0; i < parameters.length; ++i) {
				query.setParameter(i, parameters[i]);
			}
		}
		return query;
	}

	public Query createQuery(String queryString, Map<String, ?> parameters) {
		Query query = getSession().createQuery(queryString);
		if (parameters != null) {
			query.setProperties(parameters);
		}
		return query;
	}

	protected long countHqlResult(String hql, Object... parameters) {
		String countHql = generateCountHql(hql);
		return ((Number) findUnique(countHql, parameters)).longValue();
	}

	protected long countHqlResult(String hql, Map<String, ?> parameters) {
		String countHql = generateCountHql(hql);
		return ((Number) findUnique(countHql, parameters)).longValue();
	}

	private String generateCountHql(String hql) {
		hql = "from " + StringUtils.substringAfter(hql, "from");
		hql = StringUtils.substringBefore(hql, "order by");
		String countHql = "select count(*) " + hql;
		return countHql;
	}

	protected Query setPageParameterToQuery(Query q, Page<T> page) {
		q.setFirstResult(page.getFirstEntityIndex());
		q.setMaxResults(page.getPageSize());
		return q;
	}

	@SuppressWarnings("unchecked")
	public <X> X findUnique(String hql, Object... parameters) {
		return (X) createQuery(hql, parameters).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public <X> X findUnique(String hql, Map<String, ?> parameters) {
		return (X) createQuery(hql, parameters).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public <X> List<X> find(String hql, Object... parameters) {
		return createQuery(hql, parameters).list();
	}

	@SuppressWarnings("unchecked")
	public <X> List<X> find(String hql, Map<String, ?> parameters) {
		return createQuery(hql, parameters).list();
	}

	@SuppressWarnings("unchecked")
	public Page<T> find(Page<T> page, String hql, Object... parameters) {
		notNull(page, "page");
		Query q = createQuery(hql, parameters);
		long totalCount = countHqlResult(hql, parameters);
		page.setEntityCount((int) totalCount);
		setPageParameterToQuery(q, page);
		page.setEntities(q.list());
		return page;
	}

	@SuppressWarnings("unchecked")
	public Page<T> find(Page<T> page, String hql, Map<String, ?> parameters) {
		notNull(page, "page");
		Query q = createQuery(hql, parameters);
		long totalCount = countHqlResult(hql, parameters);
		page.setEntityCount((int) totalCount);
		setPageParameterToQuery(q, page);
		page.setEntities(q.list());
		return page;
	}

	/**
	 * 保存
	 * 
	 * @param entity
	 */
	public void save(T entity) {
		getSession().save(entity);
	}

	/**
	 * 更新
	 * 
	 * @param entity
	 */
	public void update(T entity) {
		getSession().update(entity);
	}

	public void delete(T entity) {
		getSession().delete(entity);
	}

	public void deleteById(PK id) {
		delete(get(id));
	}

	@SuppressWarnings("unchecked")
	public T checkById(PK id) {
		return (T) getSession().get(entityType, id);
	}

	@SuppressWarnings("unchecked")
	public T findById(PK id) {
		return (T) getSession().load(entityType, id);
	}

	/**
	 * 保存或更新
	 * 
	 * @param entity
	 */
	public void saveOrUpdate(T entity) {
		getSession().saveOrUpdate(entity);
	}

	private String CheckSpace(String value) {
		if (value.indexOf(SPACE) != -1) {
			throw new RuntimeException("不允许包含空格!");
		}
		if (value.startsWith("_")) {
			value = value.substring(1, value.length());
		}
		return value;
	}

	private String replaceNum(String value) {
		return StringUtils.replaceEach(value, NUMS, NUMREPLACE);
	}

	private String filterValue(String value) {
		return replaceNum(CheckSpace(value));
	}

	private String getGenColumnName(String str) {
		str = str.split("\\" + FORMATESPT)[0];
		str = StringUtils.replaceEach(str, NUMREPLACE, NUMREPLACELETTER);
		return str;
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
			if (orderValue.endsWith(MYSPACE + HQL)) {
				String hqlValue = getHqlSnippet(StringUtils.substringBefore(orderValue, MYSPACE + HQL));
				order.append(hqlValue);
			} else {
				order.append(orderValue.replace(MYSPACECHAR, SPACECHAR)).append(",");
			}
		}
		if (value instanceof String[]) {
			for (String str : (String[]) value) {
				String orderValue = filterValue(str);
				if (orderValue.endsWith(MYSPACE + HQL)) {
					String hqlValue = getHqlSnippet(StringUtils.substringBefore(orderValue, MYSPACE + HQL));
					order.append(hqlValue);
				} else {
					order.append(orderValue.replace(MYSPACECHAR, SPACECHAR)).append(",");
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
		if (value instanceof String) {
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
	 * TODO 判断是否需要查询
	 * 
	 * @param keys
	 * @param value
	 * @return
	 */
	private boolean queryFlag(String[] keys, Object value) {
		if (keys.length == 3 && (keys[2].equals(IN) || keys[2].endsWith(RX))) {
			return true;
		}
		if (null != value && !StringUtils.isBlank(String.valueOf(value))) {
			if (keys.length > 1) {
				if (keys.length == 3 && keys[2].equals(EX)) {
				} else {
					return true;
				}
			}
		}
		if (keys.length == 3 && keys[2].equals(CK)) {
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
		if ("gt".equals(condition)) {
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
		if (condition.equals("notin") || condition.equals("notIn")) {
			columnWithCondition.setCondition("not in");
			return columnWithCondition;
		}
		if (condition.equals("not")) {//比如not 3 为!=3 or is null 
			columnWithCondition.setCondition("not");
			return columnWithCondition;
		}
		if (StringUtils.isEnglish(condition)) {
			columnWithCondition.setCondition(condition);
			return columnWithCondition;
		}
		throw new RuntimeException("本版本不再允许带符号条件传入!比如=,>换用eq,gt");
		// columnWithCondition.setCondition(condition);
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
		// 判断是否跳过查询
		boolean queryFlag = queryFlag(keys, value);
		if (keys.length >= 2) {
			columnWithCondition = analyzeKeys(columnWithCondition, queryFlag, keys, value);
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
	 * 
	 * @param proType
	 * @param value
	 * @return
	 */
	private Object convert(ColumnWithCondition columnWithCondition) {
		Object value = columnWithCondition.getValue();
		String type = columnWithCondition.getType();
		logger.info("Type::" + value.getClass().getName());
		logger.info(type);
		String valueType = StringUtils.substringAfterLast(value.getClass().getName(), ".");
		if (!valueType.equalsIgnoreCase(type)) {
			if ("string".equalsIgnoreCase(type)) {
				value = ConvertUtils.toString(value);
				return value;
			}
			if ("integer".equalsIgnoreCase(type)) {
				value = ConvertUtils.toInteger(value);
				return value;
			}
			if ("long".equalsIgnoreCase(type)) {
				value = ConvertUtils.toLong(value);
				return value;
			}
			if ("boolean".equalsIgnoreCase(type)) {
				value = ConvertUtils.toBoolean(value);
				return value;
			}
			if ("double".equalsIgnoreCase(type)) {
				value = ConvertUtils.toDouble(value);
				return value;
			}
			if ("float".equalsIgnoreCase(type)) {
				value = ConvertUtils.toFloat(value);
				return value;
			}
			if ("short".equalsIgnoreCase(type)) {
				value = ConvertUtils.toShort(value);
				return value;
			}
			if ("byte".equalsIgnoreCase(type)) {
				value = ConvertUtils.toByte(value);
				return value;
			}
			if ("timestamp".equalsIgnoreCase(type) || "datetime".equalsIgnoreCase(type) || "date".equalsIgnoreCase(type)) {
				if(value instanceof Integer){
					value=ConvertUtils.toString(value);
				}
				if(value instanceof Long){
					value=ConvertUtils.toString(value);
				}
				if (value instanceof String) {
					value = getDate(columnWithCondition);
				}
				return value;
			}
			if ("big_decimal".equalsIgnoreCase(type) || "bigDecimal".equalsIgnoreCase(type)) {
				if (!(value instanceof BigDecimal)) {
					value = ConvertUtils.toBigDecimal(value);
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
		return getHql(value, null);
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
		logger.info("hqlWithParameter:time:begin:" + System.currentTimeMillis());
		HqlWithParameter hqlWithParameter = new HqlWithParameter();
		StringBuffer hql = new StringBuffer();
		StringBuffer hqlSelect = new StringBuffer();
		StringBuffer order = new StringBuffer();
		StringBuffer group = new StringBuffer();
		StringBuffer column = new StringBuffer();

		HaloMap hqlPrmMap = new HaloMap();// ....
		ClassMetadata cm = sessionFactory.getClassMetadata(this.entityType);
		String entityName = cm.getEntityName();

		if (null == parameter || null == addSelect) {// addSelect
														// 作用只用于拼接Upadter和delete语句,便于重用
			hqlSelect.append(String.format(" from %s ", entityName));
		} else {
			hqlSelect.append(String.format(" %s ", addSelect));
		}

		if (null != parameter) {
			String link = "where";
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
				if (!columnWithCondition.getIfQuery()) {// 不查询该字段
					continue;
				}
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
					hql.append(String.format(" %s (%s) ",link, hqlValue));
					link="and";
					Map<String,Object> map = HaloUtils.getHqlSnippetMap(hqlValue, value);
					hqlPrmMap.setAll(map);
					continue;// 添加参数
				}
				if (columnWithCondition.getIfQuery()) {// 查询该字段
					if (null == value || "null".equalsIgnoreCase(String.valueOf(entry.getValue()).trim())) {
						hql.append(String.format(" %s %s %s %s null %s ", link, columnWithCondition.getLeftBracket(), columnWithCondition.getColumnName(), columnWithCondition.getCondition(), columnWithCondition.getRightBracket()));
						link = columnWithCondition.getAndOr();
						continue;
					} else {
						if (null != columnWithCondition.getDirectValue()) {
							hql.append(String.format(" %s %s %s %s %s %s ", link, columnWithCondition.getLeftBracket(), columnWithCondition.getColumnName(), columnWithCondition.getCondition(), columnWithCondition.getDirectValue(), columnWithCondition.getRightBracket()));
							link = columnWithCondition.getAndOr();
							continue;
						}
						if (null != type) {
							value = convert(columnWithCondition);
						}
						if(columnWithCondition.getCondition().equals("not")){
							hql.append(String.format(" %s (%s <>:%s or %s is null) ", link,  columnWithCondition.getColumnName(),  columnWithCondition.getGenColumnName(),columnWithCondition.getColumnName()));
							link = columnWithCondition.getAndOr();
							hqlPrmMap.put(columnWithCondition.getGenColumnName(), value);
							continue;
						}
						hql.append(String.format(" %s %s %s %s:%s %s ", link, columnWithCondition.getLeftBracket(), columnWithCondition.getColumnName(), columnWithCondition.getCondition(), columnWithCondition.getGenColumnName(), columnWithCondition.getRightBracket()));
						link = columnWithCondition.getAndOr();
						hqlPrmMap.put(columnWithCondition.getGenColumnName(), value);
					}
				}

			}// for
			String columnStr = column.toString();
			if (!"".equals(columnStr)) {
				hqlWithParameter.setAddColumn(true);
				String entityIdName = cm.getIdentifierPropertyName();
				hqlSelect = new StringBuffer(String.format("select %s as %s,%s from %s ", entityIdName, entityIdName, columnStr.substring(0, columnStr.length() - 1), entityName));

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
		logger.info("hqlWithParameter:time:end:" + System.currentTimeMillis());
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
		Query query = createQuery(hql, hqlPrmMap);
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
	public <X> List<X> findListByMap(HaloMap parameter) {
		Integer begin = 0;
		Integer end = null;
		if (null != parameter.get(ADDBEGIN)) {
			begin = (Integer) ConvertUtils.convert(parameter.get(ADDBEGIN), Integer.class);
			parameter.remove(ADDBEGIN);
		}
		if (null != parameter.get(ADDEND)) {
			end = (Integer) ConvertUtils.convert(parameter.get(ADDEND), Integer.class);
			parameter.remove(ADDEND);
		}
		Query query = createMyQuery(parameter);
		if (null != end) {
			query.setFirstResult(begin);
			query.setMaxResults(end - begin);
		}
		logger.info("queryListByMap:end:" + System.currentTimeMillis());
		return query.list();
	}

	/**
	 * 根据实体值查询.
	 * 
	 * @param entity
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public <X> List<X> findListByEntity(T entity) {
		HaloMap haloMap = MyEntityUtils.toHaloMap(entity);
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
	public <X> List<X> findListByMap(HaloMap parameter, int num) {
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
	 * 计算总条数
	 * 
	 * @param hql
	 * @param parameter
	 * @return 总条数
	 */
	protected long countMyHqlResult(String hql, HaloMap parameter) {
		String countHql = generateMyCountHql(hql);
		if (countHql.indexOf("group by") != -1) {
			String tempSQL =HibernateUtils.hqlToSqlByPlaceholder(countHql, this.getSessionFactory());
			String countSQL = "select count(*) from (" + tempSQL + ") temp";
			Query query = this.createSQLQuery(countSQL, parameter);
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
		Query query = createQuery(hql, hqlPrmMap);
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
	public SQLQuery createSQLQuery(String sql, Object... parameter) {
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
	public SQLQuery createSQLQuery(String sql, Map<String, ?> parameter) {
		SQLQuery q = getSession().createSQLQuery(sql);
		q.setProperties(parameter);
		return q;
	}

	private String generateProc(String procedureName, MyLinkedHashMap parameter) {
		StringBuffer stringBuffer = new StringBuffer();
		for (Entry<String, ?> entry : parameter.entrySet()) {
			stringBuffer.append(":" + entry.getKey() + ",");
		}
		String result = stringBuffer.toString();
		if (!"".equals(result)) {
			result = result.substring(0, result.length() - 1);
		}
		String sql = String.format("{Call %s(%s) }", procedureName, result);
		return sql;
	}

	/**
	 * 生成存储过程的SQLQuery.
	 * 
	 * @param 存储过程名及参数占位符
	 * @param parameter
	 * @return SQLQuery
	 */
	public SQLQuery createProcQuery(String procedureName, MyLinkedHashMap parameter) {
		if (null == parameter) {
			parameter = new MyLinkedHashMap();
		}
		String sql = generateProc(procedureName, parameter);
		SQLQuery query = createSQLQuery(sql, parameter);
		return query;
	}

	public int executeProc(String procedureName, MyLinkedHashMap parameter) {
		SQLQuery query = createProcQuery(procedureName, parameter);
		return query.executeUpdate();
	}

	/**
	 * 生成的存储过程结果集可以映射到当前实体上.
	 * 
	 * @param procedureName
	 * @param parameter
	 * @return List
	 */

	@SuppressWarnings("rawtypes")
	public List findListByProc(String procedureName, MyLinkedHashMap parameter, Class<?> resultClass) {
		SQLQuery query = createProcQuery(procedureName, parameter);
		query.setResultTransformer(new ColumnToBean(resultClass));
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<HaloViewMap> findListByProc(String procedureName, MyLinkedHashMap parameter) {
		SQLQuery query = createProcQuery(procedureName, parameter);
		query.setResultTransformer(new ColumnToMap());
		return query.list();
	}

	/**
	 * 根据HaloMap删除
	 * 
	 * @param parameter
	 * @return 返回行数 失败返回-1
	 */
	public int deleteByMap(HaloMap parameter) {
		ClassMetadata cm = sessionFactory.getClassMetadata(this.entityType);
		String entityName = cm.getEntityName();
		String selectHql = String.format("delete %s ", entityName);
		HqlWithParameter hqlWithParameter = createQueryHql(parameter, selectHql);
		String hql = hqlWithParameter.getHql();
		HaloMap hqlPrmMap = hqlWithParameter.getParamterMap();
		if (hqlPrmMap.isEmpty()) {
			logger.warn("不允许无条件删除!防止全表更新(可通过条件实现)");
			return -1;
		}
		Query query = createQuery(hql, hqlPrmMap);
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
	@SuppressWarnings({ "unchecked", "deprecation" })
	private T updateByUpdater(Updater<T> updater) {
		ClassMetadata cm = sessionFactory.getClassMetadata(this.entityType);
		T bean = updater.getBean();
		T po = (T) getSession().get(this.entityType, cm.getIdentifier(bean, POJO));
		//T po = (T) getSession().get(this.entityType, cm.getIdentifier(bean, (SessionImplementor) this.getSession()));
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
				parameter.put(propName + MYSPACE + PRM+MYSPACE+RX, value);
				updateHql.append(String.format(" %s=:%s ,", propName, propName));
			}
		}
		String tempUpdateHql = updateHql.toString();
		if ("".equals(updateHql)) {
			return null;
		}
		Object value = MyBeanUtils.getSimpleProperty(entity, identifierName);
		if (null != value) {
			parameter.put(identifierName + MYSPACE + "eq", value);
		}
		updateHql = new StringBuffer();
		updateHql.append(String.format("update %s set %s ", entityName, tempUpdateHql.substring(0, tempUpdateHql.length() - 1)));
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
		if (hql.indexOf("where") == -1) {
			logger.warn("不允许无条件更新!防止全表更新(可通过条件实现)");
			return -1;
		}
		HaloMap hqlPrmMap = hqlWithParameter.getParamterMap();
		
		Query query = createQuery(hql, hqlPrmMap);
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

	private String getPosition(String fileName) {
		return HaloViewDao.xmlMap.get(fileName);
	}

	// hql------------------------------------------------------

	private String getHql(String id, MyHashMap tplMap) {
		String entityName = this.entityType.getSimpleName();
		File xmlPath = FileUtils.getClassPath(HALOPACH + getPosition(entityName), entityName + ".xml");
		XmlUtils xmlUtils = new XmlUtils(xmlPath);
		String hql = xmlUtils.getHql(id);
		if (null != tplMap) {
			ITplUtils tplUtils = new FreemarkerUtils();
			hql = tplUtils.generateString(tplMap, hql);
		}
		return hql;
	}

	public Query createQueryByXml(String id, MyHashMap tplMap, MyHashMap parameter) {
		return createQuery(getHql(id, tplMap), parameter);
	}

	public Query createQueryByXml(String id, MyHashMap parameter) {
		return createQueryByXml(id, null, parameter);
	}

	public int executeByHql(String id, MyHashMap tplMap, MyHashMap parameter) {
		return createQueryByXml(id, tplMap, parameter).executeUpdate();
	}

	public int executeByHql(String id, MyHashMap parameter) {
		return createQueryByXml(id, null, parameter).executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public <X> List<X> findByHql(String id, MyHashMap tplMap, MyHashMap parameter) {
		Integer begin = 0;
		Integer end = null;
		if (null != parameter.get(ADDBEGIN)) {
			begin = (Integer) ConvertUtils.convert(parameter.get(ADDBEGIN), Integer.class);
			parameter.remove(ADDBEGIN);
		}
		if (null != parameter.get(ADDEND)) {
			end = (Integer) ConvertUtils.convert(parameter.get(ADDEND), Integer.class);
			parameter.remove(ADDEND);
		}
		Query query = createQueryByXml(id, tplMap, parameter);
		if (null != end) {
			query.setFirstResult(begin);
			query.setMaxResults(end - begin);
		}
		return query.list();
	}

	public <X> List<X> findByHql(String id, MyHashMap parameter) {
		return findByHql(id, null, parameter);
	}

	@SuppressWarnings("unchecked")
	public Page<T> findPageByHql(Page<T> page, String id, MyHashMap tplMap, MyHashMap parameter) {
		notNull(page, "page");
		String hql = getHql(id, tplMap);
		Query q = createQuery(hql, parameter);
		long totalCount = countHqlResult(hql, parameter);
		page.setEntityCount((int) totalCount);
		setPageParameterToQuery(q, page);
		page.setEntities(q.list());
		return page;
	}

	public Page<T> findPageByHql(Page<T> page, String id, MyHashMap parameter) {
		return findPageByHql(page, id, null, parameter);
	}

	// hql------------------------------------------------------
	// sql------------------------------------------------------
	private String getSql(String id, MyHashMap tplMap) {
		String entityName = this.entityType.getSimpleName();
		File xmlPath = FileUtils.getClassPath(HALOPACH + getPosition(entityName), entityName + ".xml");
		XmlUtils xmlUtils = new XmlUtils(xmlPath);
		String sql = xmlUtils.getSql(id);
		if (null != tplMap) {
			ITplUtils tplUtils = new FreemarkerUtils();
			sql = tplUtils.generateString(tplMap, sql);
		}
		return sql;
	}

	public SQLQuery createSQLQueryByXml(String id, MyHashMap tplMap, MyHashMap parameter) {
		return createSQLQuery(getSql(id, tplMap), parameter);
	}

	public SQLQuery createSQLQueryByXml(String id, MyHashMap parameter) {
		return createSQLQueryByXml(id, null, parameter);
	}

	public int executeBySql(String id, MyHashMap tplMap, MyHashMap parameter) {
		return createSQLQueryByXml(id, tplMap, parameter).executeUpdate();
	}

	public int executeBySql(String id, MyHashMap parameter) {
		return createSQLQueryByXml(id, null, parameter).executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public T findFirstBySql(String id, MyHashMap tplMap, MyHashMap parameter) {
		SQLQuery query = createSQLQueryByXml(id, tplMap, parameter);
		query.setFirstResult(0);
		query.setMaxResults(1);
		return (T) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public T findUniqueBySql(String id, MyHashMap tplMap, MyHashMap parameter) {
		SQLQuery query = createSQLQueryByXml(id, tplMap, parameter);
		return (T) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public <X> List<X> findBySql(String id, MyHashMap tplMap, MyHashMap parameter) {
		Integer begin = 0;
		Integer end = null;
		if (null != parameter.get(ADDBEGIN)) {
			begin = (Integer) ConvertUtils.convert(parameter.get(ADDBEGIN), Integer.class);
			parameter.remove(ADDBEGIN);
		}
		if (null != parameter.get(ADDEND)) {
			end = (Integer) ConvertUtils.convert(parameter.get(ADDEND), Integer.class);
			parameter.remove(ADDEND);
		}
		SQLQuery query = createSQLQueryByXml(id, tplMap, parameter);
		if (null != end) {
			query.setFirstResult(begin);
			query.setMaxResults(end - begin);
		}
		return query.list();
	}

	public <X> List<X> findBySql(String id, MyHashMap parameter) {
		return findBySql(id, null, parameter);
	}

	@SuppressWarnings("unchecked")
	public Page<T> findPageBySql(Page<T> page, String id, MyHashMap tplMap, MyHashMap parameter) {
		notNull(page, "page");
		String sql = getSql(id, tplMap);
		SQLQuery q = createSQLQuery(sql, parameter);
		long totalCount = countHqlResult(sql, parameter);
		page.setEntityCount((int) totalCount);
		setPageParameterToQuery(q, page);
		page.setEntities(q.list());
		return page;
	}

	public Page<T> findPageBySql(Page<T> page, String id, MyHashMap parameter) {
		return findPageBySql(page, id, null, parameter);
	}

	// sql-------------------------------------------------------
	protected void notNull(Object obj, String name) {
		Assert.notNull(obj, "[" + name + "] must not be null.");
	}

}
