package com.ht.test.entity;

// Generated 2014-6-30 10:10:45 by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "charge_receivable_detail", catalog = "halo")
public class ChargeReceivableDetail implements java.io.Serializable {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 4803236918583024882L;
	private String receivableDetailId;
	private String feeItemId;
	private String selfFeeItemId;
	private String receivableDetailCode;
	private String houseId;
	private String houseName;
	private String houseOwnerId;
	private String houseOwnerName;
	private String feeItemName;
	private BigDecimal rebate;

	private String chargeMethodId;
	private BigDecimal feeUnit;
	private Integer collectFeePeriod;
	private BigDecimal lastNum;
	private BigDecimal thisNum;
	private Integer houseCount;
	private BigDecimal feeAmount;
	private BigDecimal feeAmountBak;
	private BigDecimal periodNum;
	private String thisMouth;
	private Date beginDate;
	private Date endDate;
	
	private String beginDateStr;
    private String endDateStr;
	
	private Date paymentDeadline;
	private String operatorId;
	private String operator;
	private boolean state;
	private String companyId;
	private String treeCode;
	private Date updateDate;
	private Date createDate;
	private BigDecimal latePrice;
	private Float latePriceRatio;
	private Integer latePriceType;
	private Integer paymentDealinePeriod;

	private BigDecimal prefAmount;
	private BigDecimal paymentAmount;
	private BigDecimal collAmount;
	private String collectFeeId;
	private String alipayId;
	private BigDecimal uncoll;
	private Integer ratio;
	private String printAlias;
	private EstateHouse estateHouse;
	private ChargeFeeItem chargeFeeItem;

	public ChargeReceivableDetail() {
	}

	public ChargeReceivableDetail(String receivableDetailId) {
		this.receivableDetailId = receivableDetailId;
	}

	@Id
	@GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "uuid")
	@Column(name = "receivable_detail_id", unique = true, nullable = false, length = 32)
	public String getReceivableDetailId() {
		return this.receivableDetailId;
	}

	public void setReceivableDetailId(String receivableDetailId) {
		this.receivableDetailId = receivableDetailId;
	}

	@Column(name = "fee_item_id", length = 32)
	public String getFeeItemId() {
		return this.feeItemId;
	}

	public void setFeeItemId(String feeItemId) {
		this.feeItemId = feeItemId;
	}

	@Column(name = "self_fee_item_id", length = 32)
	public String getSelfFeeItemId() {
		return selfFeeItemId;
	}

	public void setSelfFeeItemId(String selfFeeItemId) {
		this.selfFeeItemId = selfFeeItemId;
	}

	@Column(name = "receivable_detail_code", length = 32)
	public String getReceivableDetailCode() {
		return this.receivableDetailCode;
	}

	public void setReceivableDetailCode(String receivableDetailCode) {
		this.receivableDetailCode = receivableDetailCode;
	}

	@Column(name = "house_id", length = 32)
	public String getHouseId() {
		return houseId;
	}

	public void setHouseId(String houseId) {
		this.houseId = houseId;
	}

	@Column(name = "house_name")
	public String getHouseName() {
		return this.houseName;
	}

	public void setHouseName(String houseName) {
		this.houseName = houseName;
	}

	@Column(name = "house_owner_id", length = 32)
	public String getHouseOwnerId() {
		return this.houseOwnerId;
	}

	public void setHouseOwnerId(String houseOwnerId) {
		this.houseOwnerId = houseOwnerId;
	}

	@Column(name = "house_owner_name", length = 36)
	public String getHouseOwnerName() {
		return this.houseOwnerName;
	}
	@Column(name = "ratio")
	public Integer getRatio() {
		return ratio;
	}

	public void setRatio(Integer ratio) {
		this.ratio = ratio;
	}
	@Column(name = "print_alias", length = 36)
	public String getPrintAlias() {
		return printAlias;
	}

	public void setPrintAlias(String printAlias) {
		this.printAlias = printAlias;
	}

	public void setHouseOwnerName(String houseOwnerName) {
		this.houseOwnerName = houseOwnerName;
	}

	@Column(name = "fee_item_name", length = 32)
	public String getFeeItemName() {
		return feeItemName;
	}

	public void setFeeItemName(String feeItemName) {
		this.feeItemName = feeItemName;
	}

	@Column(name = "fee_unit", precision = 9)
	public BigDecimal getFeeUnit() {
		return feeUnit;
	}

	public void setFeeUnit(BigDecimal feeUnit) {
		this.feeUnit = feeUnit;
	}

	@Column(name = "rebate", precision = 9)
	public BigDecimal getRebate() {
		return rebate;
	}

	public void setRebate(BigDecimal rebate) {
		this.rebate = rebate;
	}

	@Column(name = "house_count")
	public Integer getHouseCount() {
		return houseCount;
	}

	public void setHouseCount(Integer houseCount) {
		this.houseCount = houseCount;
	}

	@Column(name = "late_price", precision = 9)
	public BigDecimal getLatePrice() {
		return this.latePrice;
	}

	public void setLatePrice(BigDecimal latePrice) {
		this.latePrice = latePrice;
	}

	@Column(name = "late_price_ratio", precision = 12, scale = 0)
	public Float getLatePriceRatio() {
		return this.latePriceRatio;
	}

	public void setLatePriceRatio(Float latePriceRatio) {
		this.latePriceRatio = latePriceRatio;
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

	@Column(name = "collect_fee_period")
	public Integer getCollectFeePeriod() {
		return this.collectFeePeriod;
	}

	public void setCollectFeePeriod(Integer collectFeePeriod) {
		this.collectFeePeriod = collectFeePeriod;
	}

	@Column(name = "last_num", precision = 9)
	public BigDecimal getLastNum() {
		return this.lastNum;
	}

	public void setLastNum(BigDecimal lastNum) {
		this.lastNum = lastNum;
	}

	@Column(name = "this_num", precision = 9)
	public BigDecimal getThisNum() {
		return this.thisNum;
	}

	public void setThisNum(BigDecimal thisNum) {
		this.thisNum = thisNum;
	}

	@Column(name = "fee_amount_bak", precision = 9)
	public BigDecimal getFeeAmountBak() {
		return feeAmountBak;
	}

	public void setFeeAmountBak(BigDecimal feeAmountBak) {
		this.feeAmountBak = feeAmountBak;
	}

	@Column(name = "fee_amount", precision = 9)
	public BigDecimal getFeeAmount() {
		return this.feeAmount;
	}

	public void setFeeAmount(BigDecimal feeAmount) {
		this.feeAmount = feeAmount;
	}

	@Column(name = "period_num", precision = 9)
	public BigDecimal getPeriodNum() {
		return this.periodNum;
	}

	public void setPeriodNum(BigDecimal periodNum) {
		this.periodNum = periodNum;
	}

	@Column(name = "this_mouth", length = 32)
	public String getThisMouth() {
		return thisMouth;
	}

	public void setThisMouth(String thisMouth) {
		this.thisMouth = thisMouth;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "begin_date", length = 10)
	public Date getBeginDate() {
		return this.beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "end_date", length = 10)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@Transient
	public String getBeginDateStr() {
        return beginDateStr;
    }

    public void setBeginDateStr(String beginDateStr) {
        this.beginDateStr = beginDateStr;
    }

    @Transient
    public String getEndDateStr() {
        return endDateStr;
    }

    public void setEndDateStr(String endDateStr) {
        this.endDateStr = endDateStr;
    }

    @Temporal(TemporalType.DATE)
	@Column(name = "payment_deadline", length = 10)
	public Date getPaymentDeadline() {
		return this.paymentDeadline;
	}

	public void setPaymentDeadline(Date paymentDeadline) {
		this.paymentDeadline = paymentDeadline;
	}

	@Column(name = "operator_id", length = 36)
	public String getOperatorId() {
		return this.operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	@Column(name = "operator", length = 32)
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	@Column(name = "state")
	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	//@Column(name = "state")
/*	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}*/

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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_date", length = 19)
	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date", length = 19)
	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "charge_method_id", length = 32)
	public String getChargeMethodId() {
		return chargeMethodId;
	}

	public void setChargeMethodId(String chargeMethodId) {
		this.chargeMethodId = chargeMethodId;
	}

	@Column(name = "pref_amount", precision = 9)
	public BigDecimal getPrefAmount() {
		return this.prefAmount;
	}

	public void setPrefAmount(BigDecimal prefAmount) {
		this.prefAmount = prefAmount;
	}

	@Column(name = "payment_amount", precision = 9)
	public BigDecimal getPaymentAmount() {
		return this.paymentAmount;
	}

	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	@Column(name = "coll_amount", precision = 9)
	public BigDecimal getCollAmount() {
		return this.collAmount;
	}

	public void setCollAmount(BigDecimal collAmount) {
		this.collAmount = collAmount;
	}

	@Column(name = "uncoll", precision = 9)
	public BigDecimal getUncoll() {
		return this.uncoll;
	}

	public void setUncoll(BigDecimal uncoll) {
		this.uncoll = uncoll;
	}

	@Column(name = "collect_fee_id", length = 32)
	public String getCollectFeeId() {
		return this.collectFeeId;
	}

	public void setCollectFeeId(String collectFeeId) {
		this.collectFeeId = collectFeeId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "house_id", insertable = false, updatable = false)
	public EstateHouse getEstateHouse() {
		return estateHouse;
	}

	public void setEstateHouse(EstateHouse estateHouse) {
		this.estateHouse = estateHouse;
	}
	@Column(name = "alipay_id", length = 32)
	public String getAlipayId() {
		return alipayId;
	}

	public void setAlipayId(String alipayId) {
		this.alipayId = alipayId;
	}
    
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fee_item_id", insertable = false, updatable = false)
	public ChargeFeeItem getChargeFeeItem() {
		return chargeFeeItem;
	}

	public void setChargeFeeItem(ChargeFeeItem chargeFeeItem) {
		this.chargeFeeItem = chargeFeeItem;
	}
}
