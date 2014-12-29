package ${base}.service.impl;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ht.utils.hibernate.base.Page;
import ${base}.dao.I${entity}Dao;
import ${base}.service.I${entity}Service;
import ${entity_path!}.${entity};

/**
 * @ClassName: ${entity}ServiceImpl
 * @Description: TODO ${module}ServiceImpl
 * @author ${author}
 * @date ${now}
 */
@Service
@Transactional
public class ${entity}ServiceImpl implements I${entity}Service{

    @Resource
	private I${entity}Dao ${entity_low}Dao;
	
	/**
	 * @Title: find${entity}ListByMap
	 * @Description: TODO
	 * @param parameter
	 * @return
	 * @see ${base}.service.I${entity}Service#find${entity}ListByMap(java.util.Map)
	 */
	public List<${entity}> find${entity}ListByMap(Map<String, Object> parameter) {
		return ${entity_low}Dao.find${entity}ListByMap(parameter);
	}
	
	/**
	 * @Title: find${entity}ByPage
	 * @Description: TODO
	 * @param page
	 * @param parameter
	 * @return
	 * @see ${base}.service.I${entity}Service#find${entity}ByPage(com.bstek.dorado.data.provider.Page, java.util.Map)
	 */
	public Page<${entity}> find${entity}ByPage(Page<${entity}> page, Map<String, Object> parameter) {
		return ${entity_low}Dao.find${entity}ByPage(page, parameter);
	}

	/**
	 * @Title: find${entity}ById
	 * @Description: TODO
	 * @param id
	 * @return
	 * @see ${base}.service.I${entity}Service#find${entity}ById(${id_type})
	 */
	@Override
	public ${entity} find${entity}ById(${id_type} id) {
		return ${entity_low}Dao.find${entity}ById(id);
	}
	
 /**
	 * @Title: add${entity}
	 * @Description: TODO
	 * @param entity
	 * @return
	 * @see ${base}.service.I${entity}Service#add${entity}(${base}.entity.${entity})
	 */
	@Override
	public ${entity} add${entity}(${entity} entity) {
		return ${entity_low}Dao.add${entity}(entity);
	}

	/**
	 * @Title: change${entity}
	 * @Description: TODO
	 * @param entity
	 * @return
	 * @see ${base}.service.I${entity}Service#change${entity}(${base}.entity.${entity})
	 */
	@Override
	public ${entity} change${entity}(${entity} entity) {
	
		return ${entity_low}Dao.update${entity}NotNull(entity);
	}

	/**
	 * @Title: delete${entity}ById
	 * @Description: TODO
	 * @param id
	 * @see ${base}.service.I${entity}Service#delete${entity}ById(${id_type})
	 */
	@Override
	public void delete${entity}ById(${id_type} id) {
		${entity_low}Dao.delete${entity}ById(id);
	
	}
}
