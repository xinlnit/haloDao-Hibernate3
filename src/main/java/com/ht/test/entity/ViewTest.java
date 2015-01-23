package com.ht.test.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import com.ht.halo.hibernate3.utils.annotations.FieldInfo;
import com.ht.halo.hibernate3.utils.gson.GsonUtils;


/**
 *  测试
 * @author fengchangyi
 * @date 2015年01月04日 17:07:53 
 */
@Entity
@Table(name = "view_test")
public class  ViewTest implements java.io.Serializable {
	  private static final long serialVersionUID = 1L;
     @Id
     @Column(name = "company_id", unique = true, nullable = false, length = 32)
     @FieldInfo(desc="企业ID")
     private String  companyId;

     @Column(name = "code", length = 32)
     @FieldInfo(desc="企业编号")
     private String  code;

     @Column(name = "brand", length = 32)
     @FieldInfo(desc="企业品牌")
     private String  brand;

     @Column(name = "full_name", length = 32)
     @FieldInfo(desc="企业全称")
     private String  fullName;

     @Column(name = "short_name", length = 32)
     @FieldInfo(desc="企业简称")
     private String  shortName;

     @Column(name = "type", length = 2)
     @FieldInfo(desc="企业类型")
     private String  type;

     @Column(name = "addr", length = 100)
     @FieldInfo(desc="地址")
     private String  addr;

     @Column(name = "zip", length = 32)
     @FieldInfo(desc="邮政编码")
     private String  zip;

     @Column(name = "tel", length = 32)
     @FieldInfo(desc="电话")
     private String  tel;

     @Column(name = "fax", length = 32)
     @FieldInfo(desc="传真")
     private String  fax;

     @Column(name = "url", length = 200)
     @FieldInfo(desc="网站")
     private String  url;

     @Column(name = "email", length = 32)
     @FieldInfo(desc="电子邮箱")
     private String  email;

     @Column(name = "decimal_digits", precision = 10)
     @FieldInfo(desc="保留几位小数")
     private Integer  decimalDigits;

     @Column(name = "national_tax", length = 32)
     @FieldInfo(desc="国税号")
     private String  nationalTax;

     @Column(name = "land_tax", length = 32)
     @FieldInfo(desc="地税号")
     private String  landTax;

     @Column(name = "open_bank", length = 32)
     @FieldInfo(desc="开户银行")
     private String  openBank;

     @Column(name = "bank_account", length = 32)
     @FieldInfo(desc="银行账户")
     private String  bankAccount;

     @Column(name = "remark", length = 1000)
     @FieldInfo(desc="备注")
     private String  remark;

     @Column(name = "flag", precision = 10)
     @FieldInfo(desc="标志")
     private Integer  flag;
      
      
     public ViewTest() {
     }
     public ViewTest( String companyId) {
         this.companyId = companyId;
      }
      
     public String  getCompanyId() {
         return this.companyId;
     }
     public ViewTest setCompanyId(String  companyId) {
         this.companyId = companyId;
         return this;
      }
     public String  getCode() {
         return this.code;
     }
     public ViewTest setCode(String  code) {
         this.code = code;
         return this;
      }
     public String  getBrand() {
         return this.brand;
     }
     public ViewTest setBrand(String  brand) {
         this.brand = brand;
         return this;
      }
     public String  getFullName() {
         return this.fullName;
     }
     public ViewTest setFullName(String  fullName) {
         this.fullName = fullName;
         return this;
      }
     public String  getShortName() {
         return this.shortName;
     }
     public ViewTest setShortName(String  shortName) {
         this.shortName = shortName;
         return this;
      }
     public String  getType() {
         return this.type;
     }
     public ViewTest setType(String  type) {
         this.type = type;
         return this;
      }
     public String  getAddr() {
         return this.addr;
     }
     public ViewTest setAddr(String  addr) {
         this.addr = addr;
         return this;
      }
     public String  getZip() {
         return this.zip;
     }
     public ViewTest setZip(String  zip) {
         this.zip = zip;
         return this;
      }
     public String  getTel() {
         return this.tel;
     }
     public ViewTest setTel(String  tel) {
         this.tel = tel;
         return this;
      }
     public String  getFax() {
         return this.fax;
     }
     public ViewTest setFax(String  fax) {
         this.fax = fax;
         return this;
      }
     public String  getUrl() {
         return this.url;
     }
     public ViewTest setUrl(String  url) {
         this.url = url;
         return this;
      }
     public String  getEmail() {
         return this.email;
     }
     public ViewTest setEmail(String  email) {
         this.email = email;
         return this;
      }
     public Integer  getDecimalDigits() {
         return this.decimalDigits;
     }
     public ViewTest setDecimalDigits(Integer  decimalDigits) {
         this.decimalDigits = decimalDigits;
         return this;
      }
     public String  getNationalTax() {
         return this.nationalTax;
     }
     public ViewTest setNationalTax(String  nationalTax) {
         this.nationalTax = nationalTax;
         return this;
      }
     public String  getLandTax() {
         return this.landTax;
     }
     public ViewTest setLandTax(String  landTax) {
         this.landTax = landTax;
         return this;
      }
     public String  getOpenBank() {
         return this.openBank;
     }
     public ViewTest setOpenBank(String  openBank) {
         this.openBank = openBank;
         return this;
      }
     public String  getBankAccount() {
         return this.bankAccount;
     }
     public ViewTest setBankAccount(String  bankAccount) {
         this.bankAccount = bankAccount;
         return this;
      }
     public String  getRemark() {
         return this.remark;
     }
     public ViewTest setRemark(String  remark) {
         this.remark = remark;
         return this;
      }
     public Integer  getFlag() {
         return this.flag;
     }
     public ViewTest setFlag(Integer  flag) {
         this.flag = flag;
         return this;
      }
     @Override
	 public String toString() {
		 return GsonUtils.getGsonIn().toJson(this);
	 }
	 public String getJson(){
		 return GsonUtils.getGsonIn().toJson(this);
	 }
}
