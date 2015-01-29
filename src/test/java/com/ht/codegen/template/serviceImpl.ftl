package ${pro.basePath}.service.impl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ht.halo.dao.IHaloDao;
 [#if bean.entityType!="view"]
import com.ht.halo.service.ICURDService;[/#else]
import com.ht.halo.service.IReadService;[/#if]
import ${pro.basePath}.dao.${bean.entityName!}Dao;
import ${pro.entityPath}.${bean.entityName};
import ${pro.basePath}.service.I${bean.entityName!}Service;
@Service
@Transactional
public class ${bean.entityName!}ServiceImpl extends [#if bean.entityType!="view"]CURDServiceImpl[/#else]IReadService[/#if]<BaseCompany, String> implements I${bean.entityName!}Service{
   @Resource
	private ${bean.entityName!}Dao ${bean.entityName!?uncap_first}Dao;
	@Override
	public IHaloDao<${bean.entityName!}, ${bean.idType}> getDao() {
		return ${bean.entityName!?uncap_first}Dao;
	}
}