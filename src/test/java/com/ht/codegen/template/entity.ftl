package ${pro.entityPath!};
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import com.ht.halo.hibernate3.utils.annotations.FieldInfo;
import com.ht.halo.hibernate3.gson.HtGson;
[#if bean.bigDecimalFlag]
import java.math.BigDecimal;[/#if]
[#if bean.dateFlag]
import java.util.Date;[/#if]
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
     @Id[/#if][#if field.iskey&&bean.entityType='base'&&bean.idType='String']
     @GeneratedValue(generator = "idGenerator")
     @GenericGenerator(name = "idGenerator", strategy = "uuid")[/#if]
     @Column(name = "${field.viewColumn.columnName}"[#if field.iskey], unique = true, nullable = false[/#if][#if field.length??&&field.length>0&&field.length!=255], length = ${field.length?c}[/#if][#if field.precision??&&field.precision>0], precision = ${field.precision}[/#if][#if field.scale??&&field.scale>0], scale = ${field.scale}[/#if])
     @FieldInfo(desc="${field.fieldComment!}")
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
     public ${bean.entityName!} set${field.fieldNameCap!}(${field.type!}  ${field.fieldName!}) {
         this.${field.fieldName!} = ${field.fieldName!};
         return this;
      }
     [/#list]
     @Override
	 public String toString() {
		 return HtGson.getGsonIn().toJson(this);
	 }
	 public String getJson(){
		 return HtGson.getGsonIn().toJson(this);
	 }
}