package ${pro.basePath}.service.base;

import java.util.List;

import com.ht.halo.hibernate3.HaloMap;
import com.ht.halo.hibernate3.base.Page;
import ${pro.entityPath}.${bean.entityName};

/**
 * TODO ${bean.entityComment!}BaseService
 * 
 * @author ${pro.author!}
 * @date ${now!}
 */
public interface I${bean.entityName!}BaseService {
	/**
	 * TODO 根据Id查找${bean.entityComment!}
	 * 
	 * @param id
	 * @return ${bean.entityComment!}
	 */
	public ${bean.entityName!} find${bean.entityName!}ById(${bean.idType!} id);

	/**
	 * TODO 查找第一条${bean.entityComment!}记录
	 * 
	 * @param HaloMap
	 * @return ${bean.entityComment!}
	 */
	public ${bean.entityName!} find${bean.entityName!}First(HaloMap parameter);

	/**
	 * TODO 根据HaloMap查找${bean.entityComment!}列表
	 * 
	 * @param parameter
	 * @return ${bean.entityComment!}列表
	 */
	public List<${bean.entityName!}> find${bean.entityName!}ListByMap(HaloMap parameter);

	/**
	 * TODO 根据HaloMap分页查找${bean.entityComment!}
	 * 
	 * @param page
	 * @param HaloMap
	 * @return ${bean.entityComment!}列表页
	 */
	public Page<${bean.entityName!}> find${bean.entityName!}PageByMap(Page<${bean.entityName!}> page, HaloMap parameter);
	[#if bean.entityType!="view"]
	/**
	 *  TODO 添加${bean.entityComment!}
	 * @param ${bean.entityComment!}实体 
	 * @return ${bean.entityComment!}实体 
	 */
	public ${bean.entityName!} add${bean.entityName!}(${bean.entityName!} entity);
	/**
	 *  TODO  修改${bean.entityComment!}
	 * @param 物业${bean.entityComment!} 
	 * @return 物业${bean.entityComment!}
	 */
	public ${bean.entityName!} change${bean.entityName!}(${bean.entityName!} entity);
	/**
	 * TODO 修改${bean.entityComment!}(不为null的属性)
	 * 
	 * @param ${bean.entityComment!}实体
	 * @return ${bean.entityComment!}实体
	 */
	public ${bean.entityName!} change${bean.entityName!}NotNull(${bean.entityName!} entity);

	/**
	 * TODO 根据HaloMap查询修改${bean.entityComment!}(不为null的属性)
	 * 
	 * @param ${bean.entityComment!}实体
	 * @param HaloMap
	 * @return 修改行数
	 */
	public int change${bean.entityName!}ByMap(${bean.entityName!} entity, HaloMap parameter);

	/**
	 * TODO 根据Id删除${bean.entityComment!}
	 * 
	 * @param id
	 */
	public void delete${bean.entityName!}ById(${bean.idType!} id);

	/**
	 * TODO 根据HaloMap查询删除${bean.entityComment!}记录
	 * 
	 * @param HaloMap
	 * @return 删除的行数
	 */
	public int delete${bean.entityName!}ByMap(HaloMap parameter);	[/#if]
}