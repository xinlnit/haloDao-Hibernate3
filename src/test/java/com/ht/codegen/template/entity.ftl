package ${pro.entityPath!};
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import com.ht.halo.annotations.FieldInfo;
import com.ht.halo.hibernate3.utils.gson.GsonUtils;
[#if bean.bigDecimalFlag]
import java.math.BigDecimal;[/#if]
[#if bean.dateFlag]
import java.util.Date;[/#if]
[#if bean.entityType='base'&&bean.idType='Integer']
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;[/#if]
[#if bean.entityType='base'&&bean.idType='String']
import javax.persistence.GeneratedValue;
import org.hibernate.annotations.GenericGenerator;
[/#if]
/**
 *  ${bean.entityComment!}
 * @author ${pro.author!}
 * @date ${now!}
 */
@Entity
@Table(name = "${bean.viewTable.tableName!}")
public class  ${bean.entityName!} implements java.io.Serializable {
	  private static final long serialVersionUID = 1L;
      [#list bean.fields as field][#if field.iskey]
     @Id[/#if]
      [#if field.iskey&&bean.entityType='base'&&bean.idType='Integer']
     @GeneratedValue(strategy = IDENTITY)[/#if]
     [#if field.iskey&&bean.entityType='base'&&bean.idType='String']
     @GeneratedValue(generator = "idGenerator")
     @GenericGenerator(name = "idGenerator", strategy = "uuid")[/#if]
     @Column(name = "${field.viewColumn.columnName}"[#if field.iskey], unique = true, nullable = false[/#if][#if field.length??&&field.length>0&&field.length!=255], length = ${field.length?c}[/#if][#if field.precision??&&field.precision>0], precision = ${field.precision}[/#if][#if field.scale??&&field.scale>0], scale = ${field.scale}[/#if])
     @FieldInfo(info="${field.fieldComment!}")
     private ${field.type!}  ${field.fieldName!};
      [/#list]
      
      
     public ${bean.entityName}() {
     }
     public ${bean.entityName!}( ${bean.idType!} ${bean.idName!}) {
         this.${bean.idName!} = ${bean.idName!};
      }
      
     [#list bean.fields as field]
     public ${field.type!}  get${field.fieldNameCap!}() {
         return this.${field.fieldName!};
     }
     public void set${field.fieldNameCap!}(${field.type!}  ${field.fieldName!}) {
         this.${field.fieldName!} = ${field.fieldName!};
       }
     [/#list]
     @Override
	 public String toString() {
		 return GsonUtils.getGsonIn().toJson(this);
	 }
	 public String getJson(){
		 return GsonUtils.getGsonIn().toJson(this);
	 }
}
