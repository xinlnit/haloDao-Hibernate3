package com.ht.test.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import com.ht.halo.annotations.FieldInfo;
import com.ht.halo.hibernate3.utils.gson.GsonUtils;
import java.math.BigDecimal;


import javax.persistence.GeneratedValue;
import org.hibernate.annotations.GenericGenerator;
/**
 *  单元
 * @author fengchangyi@haitao-tech.com
 * @date 2015年02月05日 17:21:57 
 */
@Entity
@Table(name = "estate_unit")
public class  EstateUnit implements java.io.Serializable {
	  private static final long serialVersionUID = 1L;
     @Id

     @GeneratedValue(generator = "idGenerator")
     @GenericGenerator(name = "idGenerator", strategy = "uuid")
     @Column(name = "unit_id", unique = true, nullable = false, length = 32)
     @FieldInfo(info="单元ID")
     private String  unitId;



     @Column(name = "building_id", length = 32)
     @FieldInfo(info="楼宇ID")
     private String  buildingId;



     @Column(name = "tree_code", length = 18)
     @FieldInfo(info="单元树号")
     private String  treeCode;



     @Column(name = "unit_code", length = 32)
     @FieldInfo(info="单元号")
     private String  unitCode;



     @Column(name = "hourse_num", precision = 10)
     @FieldInfo(info="房间数")
     private Integer  hourseNum;



     @Column(name = "unit_height", precision = 9, scale = 2)
     @FieldInfo(info="高度")
     private BigDecimal  unitHeight;



     @Column(name = "story_num", precision = 10)
     @FieldInfo(info="单元层数")
     private Integer  storyNum;



     @Column(name = "ladder_num", precision = 10)
     @FieldInfo(info="楼梯数量")
     private Integer  ladderNum;



     @Column(name = "elevator_num", precision = 10)
     @FieldInfo(info="电梯数量")
     private Integer  elevatorNum;



     @Column(name = "charge_man", length = 32)
     @FieldInfo(info="负责人")
     private String  chargeMan;



     @Column(name = "lng", length = 32)
     @FieldInfo(info="经度")
     private String  lng;



     @Column(name = "lat", length = 32)
     @FieldInfo(info="纬度")
     private String  lat;



     @Column(name = "addr", length = 100)
     @FieldInfo(info="地址")
     private String  addr;



     @Column(name = "description", length = 1000)
     @FieldInfo(info="介绍")
     private String  description;



     @Column(name = "pic", length = 1000)
     @FieldInfo(info="照片")
     private String  pic;



     @Column(name = "remark", length = 1000)
     @FieldInfo(info="备注")
     private String  remark;



     @Column(name = "is_be", precision = 3)
     @FieldInfo(info="是否删除")
     private Integer  isBe;



     @Column(name = "company_id", length = 32)
     @FieldInfo(info="物业ID")
     private String  companyId;
      
      
     public EstateUnit() {
     }
     public EstateUnit( String unitId) {
         this.unitId = unitId;
      }
      
     public String  getUnitId() {
         return this.unitId;
     }
     public void setUnitId(String  unitId) {
         this.unitId = unitId;
       }
     public String  getBuildingId() {
         return this.buildingId;
     }
     public void setBuildingId(String  buildingId) {
         this.buildingId = buildingId;
       }
     public String  getTreeCode() {
         return this.treeCode;
     }
     public void setTreeCode(String  treeCode) {
         this.treeCode = treeCode;
       }
     public String  getUnitCode() {
         return this.unitCode;
     }
     public void setUnitCode(String  unitCode) {
         this.unitCode = unitCode;
       }
     public Integer  getHourseNum() {
         return this.hourseNum;
     }
     public void setHourseNum(Integer  hourseNum) {
         this.hourseNum = hourseNum;
       }
     public BigDecimal  getUnitHeight() {
         return this.unitHeight;
     }
     public void setUnitHeight(BigDecimal  unitHeight) {
         this.unitHeight = unitHeight;
       }
     public Integer  getStoryNum() {
         return this.storyNum;
     }
     public void setStoryNum(Integer  storyNum) {
         this.storyNum = storyNum;
       }
     public Integer  getLadderNum() {
         return this.ladderNum;
     }
     public void setLadderNum(Integer  ladderNum) {
         this.ladderNum = ladderNum;
       }
     public Integer  getElevatorNum() {
         return this.elevatorNum;
     }
     public void setElevatorNum(Integer  elevatorNum) {
         this.elevatorNum = elevatorNum;
       }
     public String  getChargeMan() {
         return this.chargeMan;
     }
     public void setChargeMan(String  chargeMan) {
         this.chargeMan = chargeMan;
       }
     public String  getLng() {
         return this.lng;
     }
     public void setLng(String  lng) {
         this.lng = lng;
       }
     public String  getLat() {
         return this.lat;
     }
     public void setLat(String  lat) {
         this.lat = lat;
       }
     public String  getAddr() {
         return this.addr;
     }
     public void setAddr(String  addr) {
         this.addr = addr;
       }
     public String  getDescription() {
         return this.description;
     }
     public void setDescription(String  description) {
         this.description = description;
       }
     public String  getPic() {
         return this.pic;
     }
     public void setPic(String  pic) {
         this.pic = pic;
       }
     public String  getRemark() {
         return this.remark;
     }
     public void setRemark(String  remark) {
         this.remark = remark;
       }
     public Integer  getIsBe() {
         return this.isBe;
     }
     public void setIsBe(Integer  isBe) {
         this.isBe = isBe;
       }
     public String  getCompanyId() {
         return this.companyId;
     }
     public void setCompanyId(String  companyId) {
         this.companyId = companyId;
       }
     @Override
	 public String toString() {
		 return GsonUtils.getGsonIn().toJson(this);
	 }
	 public String getJson(){
		 return GsonUtils.getGsonIn().toJson(this);
	 }
}
