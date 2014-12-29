package ${base}.web.action;

import java.util.Collection;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import java.util.List;
import ${entity_path!}.${entity};
import ${base}.service.I${entity}Service;

/**
 * @ClassName: ${entity}Action
 * @Description: TODO ${module}Action
 * @author ${author}
 * @date ${now}
 */
@Component("${entity_low}Action")
public class ${entity}Action {

	@Resource
	private I${entity}Service ${entity_low}Service;

	/**
	 * @Title: find${entity}ByPage
	 * @Description: TODO 分页查询${module}
	 * @param page
	 * @param parameter
	 */
	@DataProvider
	public Page<${entity}> find${entity}ByPage(Page<${entity}> page, Map<String, Object> parameter) {
		return ${entity_low}Service.find${entity}ByPage(page, parameter);
	}
	
    /**
	   * @Title: find${entity}ListByMap
	   * @Description: TODO 查询${module}列表
	   * @param parameter
	   * @return
	   */
	@DataProvider
	public List<${entity}> find${entity}ListByMap(Map<String, Object> parameter) {
		return ${entity_low}Service.find${entity}ListByMap(parameter);
	}

	/**
	 * @Title: find${entity}ById
	 * @Description: TODO 根据主键获得${module}
	 * @param id
	 * @return
	 */
	@DataProvider
	public ${entity} find${entity}ById(${id_type} id) {
		return ${entity_low}Service.find${entity}ById(id);
	}
	
	/**
	 * @Title: cud${entity}s
	 * @Description: TODO 增删改${module}
	 * @param entitys
	 * @throws Exception
	 */
	@DataResolver
	public void cud${entity}s(Collection<${entity}> entitys) throws Exception {
		${entity_low}Service.cud${entity}s(entitys,null);
	}

}