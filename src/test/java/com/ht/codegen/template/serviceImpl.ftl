package ${pro.basePath}.service.impl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ht.halo.dao.IHaloDao;
 [#if bean.entityType!="view"]
import java.util.Date;
import com.ht.halo.service.impl.CURDServiceImpl;[#else]
import com.ht.halo.service.impl.ReadServiceImpl;[/#if]
import ${pro.basePath}.dao.I${bean.entityName!}Dao;
import ${pro.entityPath}.${bean.entityName};
import ${pro.basePath}.service.I${bean.entityName!}Service;
@Service
@Transactional
public class ${bean.entityName!}ServiceImpl extends [#if bean.entityType!="view"]CURDServiceImpl[#else]IReadService[/#if]<${bean.entityName!}, ${bean.idType}> implements I${bean.entityName!}Service{
   @Resource
	private I${bean.entityName!}Dao ${bean.entityName!?uncap_first}Dao;
     [#if bean.entityType!="view"]
    public void initSave(${bean.entityName!} entity){
		entity.setCreateTime(new  Date());
	}
	[/#if]
	@Override
	public IHaloDao<${bean.entityName!}, ${bean.idType}> getDao() {
		return ${bean.entityName!?uncap_first}Dao;
	}
	@Override
	public ${bean.entityName!} create() {
		return new ${bean.entityName!}();
	}
}