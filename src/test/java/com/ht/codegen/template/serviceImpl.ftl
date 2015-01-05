package ${pro.basePath}.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ${pro.basePath}.service.I${bean.entityName!}Service;
import ${pro.basePath}.service.base.impl.${bean.entityName!}BaseServiceImpl;
@Service
@Transactional
public class ${bean.entityName!}ServiceImpl extends ${bean.entityName!}BaseServiceImpl implements I${bean.entityName!}Service{
 
}