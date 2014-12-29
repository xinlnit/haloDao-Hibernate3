package ${base}.service;

import java.util.List;
import java.util.Map;
import com.ht.utils.hibernate.base.Page;
import ${entity_path!}.${entity};

/**
 * @ClassName: I${entity}Service
 * @Description: TODO ${module}Service
 * @author ${author}
 * @date ${now}
 */
public interface I${entity}Service {

	/**
	 * @Title: find${entity}ListByMap
	 * @Description: TODO 查询${module}列表
	 * @param parameter
	 *            自定义参数
	 * @return
	 */
    public List<${entity}> find${entity}ListByMap(Map<String, Object> parameter);

    /**
     * @Title: find${entity}ByPage
     * @Description: TODO 分页查询${entity}:
     * @param page
	 * @param parameter
	 *            自定义参数
     * @return
     */
    public Page<${entity}> find${entity}ByPage(Page<${entity}> page,Map<String, Object> parameter);


    /**
     * @Title: find${entity}ById
     * @Description: TODO 根据主键查找${module}
     * @param id
     * @return
     */
    public ${entity}  find${entity}ById(${id_type} id); 
    /**
     * @Title: add${entity}
     * @Description: TODO 添加${module}
     * @param entity
     * @return
     */
    public ${entity}  add${entity}(${entity} entity);
    /**
     * @Title: change${entity}
     * @Description: TODO 修改${module}
     * @param entity
     * @return
     */
    public ${entity}  change${entity}(${entity} entity);
    /**
     * @Title: delete${entity}ById
     * @Description: TODO 删除${module}
     * @param id
     */
    public void  delete${entity}ById(${id_type} id);

}
