package ${pro.basePath!}.service;
 [#if bean.entityType!="view"]
import com.ht.halo.service.ICURDService;[#else]
import com.ht.halo.service.IReadService;[/#if]
import ${pro.basePath}.entity.${bean.entityName!};

public interface I${bean.entityName!}Service extends  [#if bean.entityType!="view"] ICURDService[#else]IReadService[/#if]<${bean.entityName!},${bean.idType}>{
      
}