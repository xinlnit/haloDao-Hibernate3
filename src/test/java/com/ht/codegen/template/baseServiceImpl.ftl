package ${pro.basePath}.service.base.impl;
[#if bean.entityType!="view"]
import java.util.Date;[/#if]
import java.util.List;

import javax.annotation.Resource;

import com.ht.halo.hibernate3.HaloMap;
import com.ht.halo.hibernate3.base.Page;
import ${pro.basePath}.dao.${bean.entityName!}Dao;
import ${pro.entityPath}.${bean.entityName};
import ${pro.basePath}.service.base.I${bean.entityName!}BaseService;

public  class ${bean.entityName!}BaseServiceImpl  implements I${bean.entityName!}BaseService{
    @Resource
	private ${bean.entityName!}Dao ${bean.entityName!?uncap_first}Dao;
	@Override
	public ${bean.entityName!} find${bean.entityName!}ById(${bean.idType!} id) {
		return ${bean.entityName!?uncap_first}Dao.load(id);
	}
	@Override
	public ${bean.entityName!} find${bean.entityName!}First(HaloMap parameter) {
		return ${bean.entityName!?uncap_first}Dao.findFirstByMap(parameter);
	}
	@Override
	public List<${bean.entityName!}> find${bean.entityName!}ListByMap(HaloMap parameter) {
		return ${bean.entityName!?uncap_first}Dao.findListByMap(parameter);
	}
	@Override
	public Page<${bean.entityName!}> find${bean.entityName!}PageByMap(Page<${bean.entityName!}> page,HaloMap parameter) {
		return ${bean.entityName!?uncap_first}Dao.findPageByMap(page, parameter);
	}
	[#if bean.entityType!="view"]
	@Override
	public ${bean.entityName!} change${bean.entityName!}NotNull(${bean.entityName!} entity) {
		entity.setUpdateTime(new Date());
		${bean.entityName!?uncap_first}Dao.updateWithNotNull(entity);
		return entity;
	}
	@Override
	public int change${bean.entityName!}ByMap(${bean.entityName!} entity, HaloMap parameter) {
		entity.setUpdateTime(new Date());
		return ${bean.entityName!?uncap_first}Dao.updateWithNotNullByHql(entity, parameter);
	}
	@Override
	public void delete${bean.entityName!}ById(${bean.idType!} id) {
		 ${bean.entityName!?uncap_first}Dao.delete(id);
	}
	@Override
	public int delete${bean.entityName!}ByMap(HaloMap parameter) {
		return ${bean.entityName!?uncap_first}Dao.deleteByMap(parameter);
	}
	@Override
	public ${bean.entityName!} add${bean.entityName!}(${bean.entityName!} entity) {
		entity.setCreateTime(new  Date());
		entity.setUpdateTime(new  Date());
		entity.setState(0);
		${bean.entityName!?uncap_first}Dao.save(entity);
		return entity;
	}
	@Override
	public ${bean.entityName!} change${bean.entityName!}(${bean.entityName!} entity) {
		${bean.entityName!?uncap_first}Dao.update(entity);
		return entity;
	}
    [/#if]
}
