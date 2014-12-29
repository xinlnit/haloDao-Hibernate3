package ${base}.dao;

import java.util.List;
import java.util.Map;
import com.ht.utils.hibernate.base.Page;
import ${entity_path!}.${entity};

/**
 * @ClassName: I${entity}Dao
 * @Description: TODO ${module}Dao
 * @author ${author}
 * @date ${now}
 */
public interface I${entity}Dao {

	/**
	 * @Title: find${entity}ByPage
	 * @Description: TODO 分页查询${module}列表
	 * @param page
	 * @param parameter
	 *            自定义参数
	 * @return
	 */
	public Page<${entity}> find${entity}ByPage(Page<${entity}> page,Map<String, Object> parameter);
	
	/**
	 * @Title: find${entity}ListByMap
	 * @Description: TODO 查询${module}列表
	 * @param parameter
	 *            自定义参数
	 * @return
	 */
	public List<${entity}> find${entity}ListByMap(Map<String, Object> parameter);
  
	/**
	 * @Title: find${entity}ListBy${entity}
	 * @Description: TODO 查询${module}列表
	 * @param entity
	 * @return
	 */
	public List<${entity}> find${entity}ListBy${entity}(${entity} entity);

	/**
	 * @Title: find${entity}ListByProperty
	 * @Description: TODO 根据${module}字段和值查询列表
	 * @param property
	 * @param value
	 * @return
	 */
	public List<${entity}> find${entity}ListByProperty(String property, Object value);

    /**
   	 * @Title: find${entity}ListByProperty
	 * @Description: TODO 根据${module}字段和值查询列表
	 * @param property
	 * @param value
	 * @return
	 */
	public List<${entity}> find${entity}ListByProperty(String[] property, Object[] value);
	/** 
	 * @Title: find${entity}ById
	 * @Description: TODO 根据${module}主键查询
	 * @param id
	 * @return
	 */
	public ${entity} find${entity}ById(${id_type} id);

	/**
	 * @Title: add${entity}
	 * @Description: TODO 保存${module}
	 * @param entity
	 * @return
	 */
	public ${entity} add${entity}(${entity} entity);
    
	/**
	 * @Title: addOrUpdate${entity}
	 * @Description: TODO 保存或更新${module}
	 * @param entity
	 * @return
	 */
	public ${entity} addOrUpdate${entity}(${entity} entity);

	/**
	 * @Title: update${entity}
	 * @Description: TODO 更新${module}
	 * @param entity
	 * @return
	 */
	public ${entity} update${entity}(${entity} entity);
   
	/**
	 * @Title: update${entity}NotNull
	 * @Description: TODO 更新${module} 实体 不包含null值和空值
	 * @param entity
	 * @return
	 */
	public ${entity} update${entity}NotNull(${entity} entity);

	/**
	 * @Title: delete${entity}ByProperty
	 * @Description: TODO 根据${module}字段和值删除
	 * @param property
	 * @param value
	 */
	public void delete${entity}ByProperty(String property,Object value);
	
	/**
	 * @Title: delete${entity}ByProperty
	 * @Description: TODO 根据${module}字段和值删除
	 * @param property
	 * @param value
	 */
	public void delete${entity}ByProperty(String[] property,Object[] value);
	
	/**
	 * @Title: delete${entity}
	 * @Description: TODO 根据${module}实体删除
	 * @param entity
	 */
	public void delete${entity}(${entity} entity);
    
	/**
	 * @Title: delete${entity}ById
	 * @Description: TODO 根据${module}主键删除
	 * @param id
	 */
	public void delete${entity}ById(${id_type} id);
}
