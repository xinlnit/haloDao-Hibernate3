package ${pro.basePath}.dao.impl;
import org.springframework.stereotype.Repository;
import com.ht.halo.hibernate3.HaloDao;
import ${pro.basePath}.dao.I${bean.entityName}Dao;
import ${pro.entityPath}.${bean.entityName};
@Repository
public class ${bean.entityName}DaoImpl  extends HaloDao<${bean.entityName}, ${bean.idType}> implements I${bean.entityName}Dao{

}