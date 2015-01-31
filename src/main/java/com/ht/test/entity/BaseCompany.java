package com.ht.test.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ht.halo.annotations.FieldInfo;
import com.ht.halo.hibernate3.utils.gson.GsonUtils;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.GeneratedValue;
import org.hibernate.annotations.GenericGenerator;
/**
 *  物业企业
 * @author fengchangyi
 * @date 2015年01月04日 16:53:11 
 */
@Entity
@Table(name = "base_company")
public class  BaseCompany implements java.io.Serializable {
	  private static final long serialVersionUID = 1L;
     @Id
     @GeneratedValue(generator = "idGenerator")
     @GenericGenerator(name = "idGenerator", strategy = "uuid")
     @Column(name = "company_id", unique = true, nullable = false, length = 32)
     @FieldInfo(info="企业ID")
     private String  companyId;

     @Column(name = "code", length = 32)
     @FieldInfo(info="企业编号")
     private String  code;

     @Column(name = "brand", length = 32)
     @FieldInfo(info="企业品牌")
     private String  brand;

     @Column(name = "full_name", length = 32)
     @FieldInfo(info="企业全称")
     private String  fullName;

     @Column(name = "short_name", length = 32)
     @FieldInfo(info="企业简称")
     private String  shortName;

     @Column(name = "type", length = 2)
     @FieldInfo(info="企业类型")
     private String  type;

     @Column(name = "addr", length = 100)
     @FieldInfo(info="地址")
     private String  addr;

     @Column(name = "zip", length = 32)
     @FieldInfo(info="邮政编码")
     private String  zip;

     @Column(name = "tel", length = 32)
     @FieldInfo(info="电话")
     private String  tel;

     @Column(name = "fax", length = 32)
     @FieldInfo(info="传真")
     private String  fax;

     @Column(name = "url", length = 200)
     @FieldInfo(info="网站")
     private String  url;

     @Column(name = "email", length = 32)
     @FieldInfo(info="电子邮箱")
     private String  email;

     @Column(name = "decimal_digits", precision = 10)
     @FieldInfo(info="保留几位小数")
     private Integer  decimalDigits;

     @Column(name = "national_tax", length = 32)
     @FieldInfo(info="国税号")
     private String  nationalTax;

     @Column(name = "land_tax", length = 32)
     @FieldInfo(info="地税号")
     private String  landTax;

     @Column(name = "open_bank", length = 32)
     @FieldInfo(info="开户银行")
     private String  openBank;

     @Column(name = "bank_account", length = 32)
     @FieldInfo(info="银行账户")
     private String  bankAccount;

     @Column(name = "remark", length = 1000)
     @FieldInfo(info="备注")
     private String  remark;

     @Column(name = "money", precision = 9, scale = 2)
     @FieldInfo(info="钱")
     private BigDecimal  money;

     @Column(name = "create_time")
     @FieldInfo(info="创建时间")
     private Date  createTime;

     @Column(name = "update_time")
     @FieldInfo(info="更新时间")
     private Date  updateTime;

     @Column(name = "state", precision = 10)
     @FieldInfo(info="状态")
     private Integer  state;
   
      
     public BaseCompany() {
     }
     public BaseCompany( String companyId) {
         this.companyId = companyId;
      }
      
     public String  getCompanyId() {
         return this.companyId;
     }
     public BaseCompany setCompanyId(String  companyId) {
         this.companyId = companyId;
         return this;
      }
     public String  getCode() {
         return this.code;
     }
     public BaseCompany setCode(String  code) {
         this.code = code;
         return this;
      }
     public String  getBrand() {
         return this.brand;
     }
     public BaseCompany setBrand(String  brand) {
         this.brand = brand;
         return this;
      }
     public String  getFullName() {
         return this.fullName;
     }
     public BaseCompany setFullName(String  fullName) {
         this.fullName = fullName;
         return this;
      }
     public String  getShortName() {
         return this.shortName;
     }
     public BaseCompany setShortName(String  shortName) {
         this.shortName = shortName;
         return this;
      }
     public String  getType() {
         return this.type;
     }
     public BaseCompany setType(String  type) {
         this.type = type;
         return this;
      }
     public String  getAddr() {
         return this.addr;
     }
     public BaseCompany setAddr(String  addr) {
         this.addr = addr;
         return this;
      }
     public String  getZip() {
         return this.zip;
     }
     public BaseCompany setZip(String  zip) {
         this.zip = zip;
         return this;
      }
     public String  getTel() {
         return this.tel;
     }
     public BaseCompany setTel(String  tel) {
         this.tel = tel;
         return this;
      }
     public String  getFax() {
         return this.fax;
     }
     public BaseCompany setFax(String  fax) {
         this.fax = fax;
         return this;
      }
     public String  getUrl() {
         return this.url;
     }
     public BaseCompany setUrl(String  url) {
         this.url = url;
         return this;
      }
     public String  getEmail() {
         return this.email;
     }
     public BaseCompany setEmail(String  email) {
         this.email = email;
         return this;
      }
     public Integer  getDecimalDigits() {
         return this.decimalDigits;
     }
     public BaseCompany setDecimalDigits(Integer  decimalDigits) {
         this.decimalDigits = decimalDigits;
         return this;
      }
     public String  getNationalTax() {
         return this.nationalTax;
     }
     public BaseCompany setNationalTax(String  nationalTax) {
         this.nationalTax = nationalTax;
         return this;
      }
     public String  getLandTax() {
         return this.landTax;
     }
     public BaseCompany setLandTax(String  landTax) {
         this.landTax = landTax;
         return this;
      }
     public String  getOpenBank() {
         return this.openBank;
     }
     public BaseCompany setOpenBank(String  openBank) {
         this.openBank = openBank;
         return this;
      }
     public String  getBankAccount() {
         return this.bankAccount;
     }
     public BaseCompany setBankAccount(String  bankAccount) {
         this.bankAccount = bankAccount;
         return this;
      }
     public String  getRemark() {
         return this.remark;
     }
     public BaseCompany setRemark(String  remark) {
         this.remark = remark;
         return this;
      }
     public BigDecimal  getMoney() {
         return this.money;
     }
     public BaseCompany setMoney(BigDecimal  money) {
         this.money = money;
         return this;
      }
     public Date  getCreateTime() {
         return this.createTime;
     }
     public BaseCompany setCreateTime(Date  createTime) {
         this.createTime = createTime;
         return this;
      }
     public Date  getUpdateTime() {
         return this.updateTime;
     }
     public BaseCompany setUpdateTime(Date  updateTime) {
         this.updateTime = updateTime;
         return this;
      }
     public Integer  getState() {
         return this.state;
     }
     public BaseCompany setState(Integer  state) {
         this.state = state;
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
