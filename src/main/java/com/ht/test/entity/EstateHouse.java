package com.ht.test.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@SuppressWarnings("serial")
@Table(name = "estate_house", catalog = "halo")
public class EstateHouse implements java.io.Serializable {

	private String houseId;
	private String unitId;
	private String treeCode;
	private String houseCode;
	private String houseStatus;
	private String houseNo;
	private Integer storey;
	private BigDecimal storeyHeight;
	private String houseType;
	private String decorateType;
	private String orientation;
	private BigDecimal structureArea;
	private BigDecimal useArea;
	private BigDecimal pooledArea;
	private String remark;
	private int isBe;
	private String companyId;

	public EstateHouse() {
	}

	public EstateHouse(String houseId) {
		this.houseId = houseId;
	}


	@Id
	@GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "uuid")
	@Column(name = "house_id", unique = true, nullable = false, length = 32)
	public String getHouseId() {
		return this.houseId;
	}

	public void setHouseId(String houseId) {
		this.houseId = houseId;
	}

	@Column(name = "unit_id", nullable = false, length = 32)
	public String getUnitId() {
		return this.unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	@Column(name = "tree_code", nullable = false, length = 23)
	public String getTreeCode() {
		return this.treeCode;
	}

	public void setTreeCode(String treeCode) {
		this.treeCode = treeCode;
	}

	@Column(name = "house_code", nullable = false, length = 32)
	public String getHouseCode() {
		return this.houseCode;
	}

	public void setHouseCode(String houseCode) {
		this.houseCode = houseCode;
	}

	@Column(name = "house_status", nullable = false, length = 2)
	public String getHouseStatus() {
		return this.houseStatus;
	}

	public void setHouseStatus(String houseStatus) {
		this.houseStatus = houseStatus;
	}

	@Column(name = "house_no", length = 32)
	public String getHouseNo() {
		return this.houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	@Column(name = "storey")
	public Integer getStorey() {
		return this.storey;
	}

	public void setStorey(Integer storey) {
		this.storey = storey;
	}

	@Column(name = "storey_height", precision = 9)
	public BigDecimal getStoreyHeight() {
		return this.storeyHeight;
	}

	public void setStoreyHeight(BigDecimal storeyHeight) {
		this.storeyHeight = storeyHeight;
	}

	@Column(name = "house_type", length = 2)
	public String getHouseType() {
		return this.houseType;
	}

	public void setHouseType(String houseType) {
		this.houseType = houseType;
	}

	@Column(name = "decorate_type", length = 2)
	public String getDecorateType() {
		return this.decorateType;
	}

	public void setDecorateType(String decorateType) {
		this.decorateType = decorateType;
	}

	@Column(name = "orientation", length = 2)
	public String getOrientation() {
		return this.orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	@Column(name = "structure_area", precision = 9)
	public BigDecimal getStructureArea() {
		return this.structureArea;
	}

	public void setStructureArea(BigDecimal structureArea) {
		this.structureArea = structureArea;
	}

	@Column(name = "use_area", precision = 9)
	public BigDecimal getUseArea() {
		return this.useArea;
	}

	public void setUseArea(BigDecimal useArea) {
		this.useArea = useArea;
	}

	@Column(name = "pooled_area", precision = 9)
	public BigDecimal getPooledArea() {
		return this.pooledArea;
	}

	public void setPooledArea(BigDecimal pooledArea) {
		this.pooledArea = pooledArea;
	}

	@Column(name = "remark", length = 1000)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	@Column(name = "is_be", nullable = false, length = 1)
	public int getIsBe() {
		return isBe;
	}

	public void setIsBe(int isBe) {
		this.isBe = isBe;
	}

	@Column(name = "company_id", nullable = false, length = 32)
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}


	
}
