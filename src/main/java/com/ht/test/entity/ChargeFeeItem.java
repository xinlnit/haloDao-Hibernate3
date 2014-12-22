package com.ht.test.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "charge_fee_item", catalog = "halo")
public class ChargeFeeItem implements java.io.Serializable {
    private static final long serialVersionUID = 570279785896630354L;

    private String feeItemId;
    private String chargeMethodId;
    private String chargeMethodName;
    private String poolMethodId;
    private String poolMethodName;
    private String feeItemTypeId;
    private String feeItemTypeName;
    private String feeCode;
    private String name;
    private BigDecimal feeUnitPrice;
    private Integer feePeriod;
    private BigDecimal latePrice;
    private Float latePriceRatio;
    private Integer latePriceType;
    private Integer paymentDealinePeriod;
    private Integer roomCount;
    private String description;
    private String remark;
    private Integer state;
    private Date createDate;
    private Date updateDate;
    private String selfFeeItemId;
    private String selfFeeItemName;
    private String companyId;
    private String treeCode;
    private Integer ratio;
    private String printAlias;
    private ChargeFeeItem chargeFeeItem;
    private String feeItemType;
    private BigDecimal sort;

    public ChargeFeeItem() {
    }

    public ChargeFeeItem(String feeItemId) {
        this.feeItemId = feeItemId;
    }

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @Column(name = "fee_item_id", unique = true, nullable = false, length = 32)
    public String getFeeItemId() {
        return this.feeItemId;
    }

    public void setFeeItemId(String feeItemId) {
        this.feeItemId = feeItemId;
    }

    @Column(name = "charge_method_id", length = 32)
    public String getChargeMethodId() {
        return this.chargeMethodId;
    }

    public void setChargeMethodId(String chargeMethodId) {
        this.chargeMethodId = chargeMethodId;
    }

    @Column(name = "charge_method_name", length = 32)
    public String getChargeMethodName() {
        return this.chargeMethodName;
    }

    public void setChargeMethodName(String chargeMethodName) {
        this.chargeMethodName = chargeMethodName;
    }

    @Column(name = "fee_item_type_id", length = 36)
    public String getFeeItemTypeId() {
        return this.feeItemTypeId;
    }

    public void setFeeItemTypeId(String feeItemTypeId) {
        this.feeItemTypeId = feeItemTypeId;
    }

    @Column(name = "fee_item_type_name", length = 32)
    public String getFeeItemTypeName() {
        return this.feeItemTypeName;
    }

    public void setFeeItemTypeName(String feeItemTypeName) {
        this.feeItemTypeName = feeItemTypeName;
    }

    @Column(name = "fee_code", length = 32)
    public String getFeeCode() {
        return this.feeCode;
    }

    public void setFeeCode(String feeCode) {
        this.feeCode = feeCode;
    }

    @Column(name = "name", length = 32)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "fee_unit_price", precision = 9)
    public BigDecimal getFeeUnitPrice() {
        return this.feeUnitPrice;
    }

    public void setFeeUnitPrice(BigDecimal feeUnitPrice) {
        this.feeUnitPrice = feeUnitPrice;
    }

    @Column(name = "fee_period")
    public Integer getFeePeriod() {
        return this.feePeriod;
    }

    public void setFeePeriod(Integer feePeriod) {
        this.feePeriod = feePeriod;
    }

    @Column(name = "late_price", precision = 9)
    public BigDecimal getLatePrice() {
        return this.latePrice;
    }

    public void setLatePrice(BigDecimal latePrice) {
        this.latePrice = latePrice;
    }

    @Column(name = "late_price_ratio", precision = 9, scale = 2)
    public Float getLatePriceRatio() {
        return this.latePriceRatio;
    }

  
	public void setLatePriceRatio(Float latePriceRatio) {
        this.latePriceRatio = latePriceRatio;
    }
	 @Column(name = "sort", precision = 9, scale = 2)
	  public BigDecimal getSort() {
			return sort;
		}

		public void setSort(BigDecimal sort) {
			this.sort = sort;
		}

    @Column(name = "late_price_type")
    public Integer getLatePriceType() {
        return latePriceType;
    }

    public void setLatePriceType(Integer latePriceType) {
        this.latePriceType = latePriceType;
    }

    @Column(name = "payment_dealine_period")
    public Integer getPaymentDealinePeriod() {
        return this.paymentDealinePeriod;
    }

    public void setPaymentDealinePeriod(Integer paymentDealinePeriod) {
        this.paymentDealinePeriod = paymentDealinePeriod;
    }

    @Column(name = "room_count")
    public Integer getRoomCount() {
        return this.roomCount;
    }

    public void setRoomCount(Integer roomCount) {
        this.roomCount = roomCount;
    }

    @Column(name = "description")
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "remark", length = 100)
    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "state")
    public Integer getState() {
        return this.state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Column(name = "pool_method_id")
    public String getPoolMethodId() {
        return poolMethodId;
    }

    public void setPoolMethodId(String poolMethodId) {
        this.poolMethodId = poolMethodId;
    }

    @Column(name = "pool_method_name")
    public String getPoolMethodName() {
        return poolMethodName;
    }

    public void setPoolMethodName(String poolMethodName) {
        this.poolMethodName = poolMethodName;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", length = 19)
    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date", length = 19)
    public Date getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Column(name = "company_id", length = 32)
    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    @Column(name = "tree_code", length = 32)
    public String getTreeCode() {
        return treeCode;
    }

    public void setTreeCode(String treeCode) {
        this.treeCode = treeCode;
    }

    @Column(name = "self_fee_item_id", length = 32)
    public String getSelfFeeItemId() {
        return selfFeeItemId;
    }

    public void setSelfFeeItemId(String selfFeeItemId) {
        this.selfFeeItemId = selfFeeItemId;
    }

    @Column(name = "self_fee_item_name", length = 36)
    public String getSelfFeeItemName() {
        return selfFeeItemName;
    }

    @Column(name = "ratio")
    public Integer getRatio() {
        return ratio;
    }

    public void setRatio(Integer ratio) {
        this.ratio = ratio;
    }

    @Column(name = "print_alias", length = 50)
    public String getPrintAlias() {
        return printAlias;
    }

    public void setPrintAlias(String printAlias) {
        this.printAlias = printAlias;
    }

    public void setSelfFeeItemName(String selfFeeItemName) {
        this.selfFeeItemName = selfFeeItemName;
    }

    @Column(name = "fee_item_type", length = 32)
    public String getFeeItemType() {
        return feeItemType;
    }

    public void setFeeItemType(String feeItemType) {
        this.feeItemType = feeItemType;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "self_fee_item_id", insertable = false, updatable = false)
    public ChargeFeeItem getChargeFeeItem() {
        return chargeFeeItem;
    }

    public void setChargeFeeItem(ChargeFeeItem chargeFeeItem) {
        this.chargeFeeItem = chargeFeeItem;
    }

}
