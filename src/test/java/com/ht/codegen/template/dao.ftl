package ${pro.basePath}.dao;
import org.springframework.stereotype.Repository;
import com.ht.halo.hibernate3.HaloDao;
import ${pro.entityPath}.${bean.entityName};
/**
 * ${bean.entityComment!}Dao
 * @author ${pro.author!}
 * @date ${now!}
 */
@Repository
public class ${bean.entityName}Dao extends HaloDao<${bean.entityName}, ${bean.idType}>{
       
}