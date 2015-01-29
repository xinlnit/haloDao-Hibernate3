package ${pro.basePath}.service;
 [#if bean.entityType!="view"&&pro.isDorado=="true"]
import java.util.Collection;

import com.ht.halo.hibernate3.HaloMap;
import ${pro.entityPath}.${bean.entityName};[/#if]
import ${pro.basePath}.service.base.I${bean.entityName!}BaseService;

public interface I${bean.entityName!}Service extends I${bean.entityName!}BaseService{
}
package ${pro.basePath!}.service;
 [#if bean.entityType!="view"]
import com.ht.halo.service.ICURDService;[/#else]
import com.ht.halo.service.IReadService;[/#if]
import ${pro.basePath}.entity.${bean.entityName!};

public interface I${bean.entityName!}Service extends  [#if bean.entityType!="view"] ICURDService[/#else]IReadService[/#if]<${bean.entityName!},${bean.idType}>{
      
}