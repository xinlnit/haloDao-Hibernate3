package com.ht.test.dao;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.ht.halo.hibernate3.HaloMap;
import com.ht.halo.hibernate3.base.Page;
import com.ht.halo.hibernate3.map.MyLinkedHashMap;
import com.ht.halo.hibernate3.utils.gson.GsonUtils;
import com.ht.test.entity.ChargeReceivableDetail;
import com.ht.utils.junit.BaseDaoTestCase;

public class ChargeReceivableDetailDaoTest extends BaseDaoTestCase {
	private static final Log logger = LogFactory.getLog(ChargeReceivableDetailDaoTest.class);
	@Resource
	private ChargeReceivableDetailDao chargeReceivableDetailDao;
	@Test
	public void testFindListByMap() {
        long a= System.currentTimeMillis();
		List<ChargeReceivableDetail> chargeReceivableDetails = chargeReceivableDetailDao.queryListByMap(new HaloMap()
		.set("houseId_eq", "4028813a47f2772c0147f2780578056a")
		.set("ratio_notin", "1.0")
		.set("state_=", 1)
		.set("houseName_5like5", "1")
		.set("periodNum_eq_ck", 0) 
		.addOrder("periodNum_desc")
		//.set("periodNum", 3)
		//.addHql("fcy.charge.chargeReceivableDetail.aa")
		//.set("houseOwnerName:prm", "李树")
		//.sets("fcy.charge.chargeReceivableDetail.ab:hql", "李树平","4028813a47f2772c0147f2780578056a")
		 //  .set("houseOwnerName:in", new String[]{"33","44"})
		//.addColumn("houseOwnerName")
		); 
	//	chargeReceivableDetails= chargeReceivableDetailDao.findListByMap(null);
		logger.info(chargeReceivableDetails.size());
	    long b= System.currentTimeMillis();
	    System.out.println(b-a);
		for (ChargeReceivableDetail chargeReceivableDetail : chargeReceivableDetails) {
			System.out.println(chargeReceivableDetail.isState());
			logger.info(chargeReceivableDetail.getHouseOwnerId());
			logger.info(chargeReceivableDetail.getHouseOwnerName());
			//logger.info(chargeReceivableDetail.getFeeItemName());
			//logger.info(chargeReceivableDetail.getChargeFeeItem().getName());
			logger.info(GsonUtils.getGsonIn().toJson(chargeReceivableDetail));
		}
	}
	@Test       
	public void testFindPageByMap(){
		Page<ChargeReceivableDetail> page= new Page<ChargeReceivableDetail>(7, 1);
		page=chargeReceivableDetailDao.queryPageByMap(page, new HaloMap()
		.set("houseOwnerId_eq", "4028813a47f2772c0147f278059605f3")
		.addGroup("feeItemId")
		.addGroup("houseOwnerId")
		//.sets("fcy.charge.chargeReceivableDetail.ab:hql", "李树平","4028813a47f2772c0147f2780578056a")
		.set("feeItemName_like", "水")
		);
		System.out.println(GsonUtils.getGsonIn("entities").toJson(page));
	}
	@Test
	public void testGet() {
		ChargeReceivableDetail entity = chargeReceivableDetailDao.get("3bf72ba07a8511e484510023243f49a7");
		logger.info(entity.getHouseOwnerName());
	}

	@Test
	public void testSave() {
		ChargeReceivableDetail entity = new ChargeReceivableDetail();
		entity.setHouseName("333");
		entity.setCreateDate(new Date());
		chargeReceivableDetailDao.save(entity);
		logger.info(entity.getHouseOwnerName());
	}
	@Test
	public void testupdateWithNotNull() {
		ChargeReceivableDetail entity = new ChargeReceivableDetail();
		entity.setReceivableDetailId("3bf72ba07a8511e484510023243f49a7");
		entity.setHouseName("rrrr");
		//entity.setCreateDate(new Date());
		chargeReceivableDetailDao.updateWithNotNull(entity);
		logger.info(entity.getHouseOwnerName());
	}
	@Test
	public void testDeleteByMap() {
		int  result = chargeReceivableDetailDao.deleteByMap(null);
		int  result2 = chargeReceivableDetailDao.deleteByMap(new HaloMap().set("houseName", null));
		//result2=chargeReceivableDetailDao.deleteByMap(new HaloMap().set("houseName:=:in", null));
		int  result3 = chargeReceivableDetailDao.deleteByMap(new HaloMap().set("houseName", "333"));
		logger.info(result);
		logger.info(result2);
		logger.info(result3);	
	}

	@Test
	public void testFindFirstByMap(){
		ChargeReceivableDetail entity=	chargeReceivableDetailDao.queryFirstByMap(new HaloMap()
		.set("houseOwnerId", "4028813a47f2772c0147f278059605f3"));
		logger.info(entity.getHouseOwnerName());
	}
	@Test
	public void testFindListByMapNum(){
		List<ChargeReceivableDetail> chargeReceivableDetails = chargeReceivableDetailDao.queryListByMap(new HaloMap()
		.set("houseOwnerId", "4028813a47f2772c0147f278059605f3") ,3);
		logger.info(chargeReceivableDetails.size());
		for (ChargeReceivableDetail chargeReceivableDetail : chargeReceivableDetails) {
			logger.info(chargeReceivableDetail.getHouseOwnerId());
			logger.info(chargeReceivableDetail.getHouseOwnerName());
			//logger.info(chargeReceivableDetail.getFeeItemName());
			//logger.info(chargeReceivableDetail.getChargeFeeItem().getName());
			logger.info(GsonUtils.getGsonIn().toJson(chargeReceivableDetail));
		}
	}
   @Test
	public void testUpdateWithNotNullByHql(){
	   ChargeReceivableDetail entity = new ChargeReceivableDetail();
		entity.setReceivableDetailId("3bf72ba07a8511e484510023243f49a7");
		entity.setHouseName("tttttt");
		entity.setCreateDate(new Date());
		
	    int result= chargeReceivableDetailDao.updateWithNotNullByHql(entity);
	    System.out.println(result);
	  	    int result2= chargeReceivableDetailDao.updateWithNotNullByHql(entity,new HaloMap().set("houseOwnerName:like", "111"));
	    System.out.println(result2);
	}
   @SuppressWarnings("unchecked")
   @Test
	public void testCreateProcQuery(){
		List<ChargeReceivableDetail> chargeReceivableDetails =  chargeReceivableDetailDao.queryListByProc("pro_test", new MyLinkedHashMap()
		.set("first", 1).set("last", 4),ChargeReceivableDetail.class);
		System.out.println(chargeReceivableDetails.size());
		for (ChargeReceivableDetail chargeReceivableDetail : chargeReceivableDetails) {
			logger.info(GsonUtils.getGsonIn().toJson(chargeReceivableDetail));
		}
   }

  

	
}
