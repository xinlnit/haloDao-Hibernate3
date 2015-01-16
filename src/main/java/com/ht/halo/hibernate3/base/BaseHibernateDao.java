package com.ht.halo.hibernate3.base;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 此类的实现在相当程度上借鉴了SpringSide3.x
 * 
 * @author  
 * @since 2011-1-21
 */
public class BaseHibernateDao<T, PK extends Serializable> {
	private static final Log logger = LogFactory.getLog(BaseHibernateDao.class);
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
		Type superType = cl.getGenericSuperclass();

		if (superType instanceof ParameterizedType) {
			Type[] paramTypes = ((ParameterizedType) superType).getActualTypeArguments();
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
	public void delete( T entity) {
		getSession().delete(entity);
	}
	public void delete( PK id) {
		delete(get(id));
	}

	/**
	 * @Title: get
	 * @Description: TODO 不提供使用get方式,还是load方式
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T get( PK id) {
		return (T) getSession().load(entityType, id);
	}
	@SuppressWarnings("unchecked")
	public T load( PK id) {
		return (T) getSession().load(entityType, id);
	}

	@SuppressWarnings("unchecked")
	public T load( T entity) {
		ClassMetadata cm = sessionFactory.getClassMetadata(this.entityType);
		return (T) getSession().load(this.entityType, cm.getIdentifier(entity, (SessionImplementor) getSession()));
	}

	public Query createQuery( String hql, Object... parameters) {
		Query query = getSession().createQuery(hql);
		if (parameters != null) {
			for (int i = 0; i < parameters.length; ++i) {
				query.setParameter(i, parameters[i]);
			}
		}
		return query;
	}

	public Query createQuery( String queryString, Map<String, ?> parameters) {
		Query query = getSession().createQuery(queryString);
		if (parameters != null) {
			query.setProperties(parameters);
		}
		return query;
	}



	protected long countHqlResult( String hql,  Object... parameters) {
		String countHql = generateCountHql(hql);
		return ((Number) findUnique(countHql, parameters)).longValue();
	}

	protected long countHqlResult( String hql,  Map<String, ?> parameters) {
		String countHql = generateCountHql(hql);
		return ((Number) findUnique(countHql, parameters)).longValue();
	}

	private String generateCountHql(String hql) {
		hql = "from " + StringUtils.substringAfter(hql, "from");
		hql = StringUtils.substringBefore(hql, "order by");
		String countHql = "select count(*) " + hql;
		return countHql;
	}

	protected Query setPageParameterToQuery( Query q,  Page<T> page) {
		q.setFirstResult(page.getFirstEntityIndex());
		q.setMaxResults(page.getPageSize());
		return q;
	}

	@SuppressWarnings("unchecked")
	public <X> X findUnique( String hql,  Object... parameters) {
		return (X) createQuery(hql, parameters).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public <X> X findUnique( String hql,  Map<String, ?> parameters) {
		return (X) createQuery(hql, parameters).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public <X> List<X> find( String hql,  Object... parameters) {
		return createQuery(hql, parameters).list();
	}

	@SuppressWarnings("unchecked")
	public <X> List<X> find( String hql,  Map<String, ?> parameters) {
		return createQuery(hql, parameters).list();
	}

	@SuppressWarnings("unchecked")
	public Page<T> find( Page<T> page,  String hql,  Object... parameters) {
		notNull(page, "page");
		Query q = createQuery(hql, parameters);
		long totalCount = countHqlResult(hql, parameters);
		page.setEntityCount((int) totalCount);
		setPageParameterToQuery(q, page);
		page.setEntities(q.list());
		return page;
	}

	@SuppressWarnings("unchecked")
	public Page<T> find( Page<T> page,  String hql,  Map<String, ?> parameters) {
		notNull(page, "page");
		Query q = createQuery(hql, parameters);
		long totalCount = countHqlResult(hql, parameters);
		page.setEntityCount((int) totalCount);
		setPageParameterToQuery(q, page);
		page.setEntities(q.list());
		return page;
	}

	protected void notNull(Object obj, String name) {
		Assert.notNull(obj, "[" + name + "] must not be null.");
	}
}
