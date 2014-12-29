package ${base}.dao.impl;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.ht.utils.hibernate.base.Page;
import ${utils}.hibernate.HtHibernateDao;
import ${base}.dao.I${entity}Dao;
import ${entity_path!}.${entity};

@Repository
public class ${entity}DaoImpl extends HtHibernateDao<${entity}, ${id_type}> implements I${entity}Dao {

	/**
	 * @Title: find${entity}ByPage
	 * @Description: TODO 
	 * @param page
	 * @param parameter
	 * @return
	 * @see ${base}.dao.I${entity}Dao#find${entity}ByPage(com.bstek.dorado.data.provider.Page,java.util.Map)
	 */
	public Page<${entity}> find${entity}ByPage(Page<${entity}> page, Map<String, Object> parameter) {
		return super.findEntityByPage(page, parameter);
	}
	
	/**
	 * @Title: find${entity}ListByMap
	 * @Description: TODO 
	 * @param parameter
	 * @return
	 * @see ${base}.dao.I${entity}Dao#find${entity}ListByMap(java.util.Map)
	 */
	public List<${entity}> find${entity}ListByMap(Map<String, Object> parameter) {
		return super.findEntityList(parameter);
	}
	
	/**
	 * @Title: find${entity}ListBy${entity}
	 * @Description: TODO 
	 * @param bean
	 * @return
	 * @see ${base}.dao.I${entity}Dao#find${entity}ListBy${entity}(${entity})
	 */
	public List<${entity}> find${entity}ListBy${entity}(${entity} entity) {
		return super.findByEntity(entity);
	}
	
	/**
	 * @Title: find${entity}ListByProperty
	 * @Description: TODO 
	 * @param property
	 * @param value
	 * @return
	 * @see ${base}.dao.I${entity}Dao#find${entity}ListByProperty(java.lang.String, java.lang.Object)
	 */
    public List<${entity}> find${entity}ListByProperty(String property, Object value) {
		return super.findByProperty(property, value);
	}
	
	/**
	 * @Title: find${entity}ListByProperty
	 * @Description: TODO 
	 * @param property
	 * @param value
	 * @return
	 * @see ${base}.dao.I${entity}Dao#find${entity}ListByProperty(java.lang.String, java.lang.Object)
	 */
    @Override
	public List<${entity}> find${entity}ListByProperty(String[] property, Object[] value) {
		return super.findByProperty(property, value);
	}
	
	/**
	 * @Title: find${entity}ById
	 * @Description: TODO 
	 * @param id
	 * @return
	 * @see ${base}.dao.I${entity}Dao#find${entity}ById(${id_type})
	 */
	public ${entity} find${entity}ById(${id_type} id) {
		return super.get(id);
	}
	
    /**
	 * @Title: add${entity}
	 * @Description: TODO 
	 * @param entity
	 * @return
	 * @see ${base}.dao.I${entity}Dao#add${entity}(${entity})
	 */
	public ${entity} add${entity}(${entity} entity) {
		super.getSession().save(entity);
		return entity;
	}
	
	/**
	 * @Title: addOrUpdate${entity}
	 * @Description: TODO 
	 * @param entity
	 * @return
	 * @see ${base}.dao.I${entity}Dao#addOrUpdate${entity}(${entity})
	 */
	public ${entity} addOrUpdate${entity}(${entity} entity) {
		super.getSession().saveOrUpdate(entity);
		return entity;
	}
	
	/**
	 * @Title: update${entity}
	 * @Description: TODO 
	 * @param entity
	 * @return
	 * @see ${base}.dao.I${entity}Dao#update${entity}(${entity})
	 */
	public ${entity} update${entity}(${entity} entity) {
		super.update(entity);
		return entity;
	}
	
	/**
	 * @Title: update${entity}NotNull
	 * @Description: TODO 
	 * @param entity
	 * @return
	 * @see ${base}.dao.I${entity}Dao#updateIsNotNull${entity}(${entity})
	 */
	public ${entity} update${entity}NotNull(${entity} entity) {
		super.updateIsNotNull(entity);
		return entity;
	}
	
	/**
	 * @Title: delete${entity}ByProperty
	 * @Description: TODO
	 * @param property        
	 * @param value
	 * @see ${base}.dao.I${entity}Dao#delete${entity}ByProperty(java.lang.String[], java.lang.Object[])
	 */
	 
	@Override
	public void delete${entity}ByProperty(String[] property, Object[] value) {
		super.deleteByProperty(property, value);
	}

	/**
	 * @Title: delete${entity}ByProperty
	 * @Description: TODO
	 * @param property
	 * @param value
	 * @see ${base}.dao.I${entity}Dao#delete${entity}ByProperty(java.lang.String, java.lang.Object)
	 */
	@Override
	public void delete${entity}ByProperty(String property, Object value) {
		super.deleteByProperty(property, value);
	}
	
	/**
	 * @Title: delete${entity}
	 * @Description: TODO 
	 * @param entity
	 * @see ${base}.dao.I${entity}Dao#delete${entity}(${entity})
	 */
	public void delete${entity}(${entity} entity) {
		super.delete(entity);
	}
	
	/**
	 * @Title: delete${entity}ById
	 * @Description: TODO 
	 * @param id
	 * @see ${base}.dao.I${entity}Dao#delete${entity}ById(${id_type})
	 */
	public void delete${entity}ById(${id_type} id) {
		super.delete(id);
	}
}
