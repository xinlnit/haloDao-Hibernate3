package com.ht.test.service;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.ht.halo.hibernate3.HaloMap;
import com.ht.halo.hibernate3.base.Page;
import com.ht.halo.hibernate3.utils.gson.HtGson;
import com.ht.test.entity.ChargeReceivableDetail;
import com.ht.utils.junit.BaseServiceTestCase;

public class ChargeReceivableDetailServiceTest extends BaseServiceTestCase{
    @Resource
	 private  IChargeReceivableDetailService chargeReceivableDetailService;
	@Test
	public void testFindChargeReceivableDetailList() {
	  List<ChargeReceivableDetail> chargeReceivableDetails= 	
				  chargeReceivableDetailService.findChargeReceivableDetailList(new HaloMap()
				  .set("houseOwnerId", "4028813a47f2772c0147f278059605f3")
				 // .leftBracket().setColumn("feeItemName").like().setValue("物业")
				 //  .or().setColumn("feeItemName").eq().in().rightBracket().setValue(null)  
				
				  //.setColumn("feeItemName").like().setValue("物业")
				  // .setColumn("feeItemName").eq().in().setValue(null)	   
				 // .setColumn("feeItemName").in().setValue(new String[]{"物业费","电费@10"})
				 // .set("feeItemName:in",new String[]{"物业费","电费@10"})
				  //.set("chargeFeeItem.name:like", "物业")
				  //.addGroup("createDate","houseOwnerId")
				  //.addGroup("houseOwnerName")
				 // .addOrder("createDate:desc","houseOwnerId")  
				  //.addOrderDesc("houseOwnerName")
				 // .addHql("houseOwnerName like :houseOwnerName and createDate >=:createDate")
				 // .addParameter("houseOwnerName", "济南"+"%")
				 // .addParameter("createDate", "2014-12-03")
				 // .set("houseOwnerName:like", "济南")
				//  .set("houseName:!=:in", null)
				//  .addColumn("houseOwnerId","houseOwnerName")
				  //.addColumn("feeItemName")
				 // .set("(feeItemName:like","水")
				 // .set("feeItemName:5like5","水")
				 // .set("|feeItemName:like)", "物业")
				  // .set("createDate:>=","2014年12月03日")
				 //.set("createDate:>=?yy-MM-dd","14-12-03")
				 // .set("createDate:MMle", "2014-12-04")
				  // .set("createDate:dayle", "2014-12-04")
				  );
		  System.out.println(chargeReceivableDetails.size());
			for (ChargeReceivableDetail chargeReceivableDetail : chargeReceivableDetails) {
				System.out.println(chargeReceivableDetail.getHouseOwnerId());
				System.out.println(chargeReceivableDetail.getHouseOwnerName());
				System.out.println(chargeReceivableDetail.getFeeItemName());
				System.out.println(chargeReceivableDetail.getCreateDate());
				System.out.println(HtGson.getGsonIn().toJson(chargeReceivableDetail));
			}
	}
	@Test
	
	public void testFindChargeReceivableDetailListByEntity() {
		ChargeReceivableDetail  bean = new ChargeReceivableDetail();
			bean.setHouseOwnerId("4028813a47f2772c0147f278059605f3");
			 List<ChargeReceivableDetail> chargeReceivableDetails2= 	
					  chargeReceivableDetailService.findChargeReceivableDetailList(bean);
					  System.out.println(chargeReceivableDetails2.size());
						for (ChargeReceivableDetail chargeReceivableDetail : chargeReceivableDetails2) {
							System.out.println(chargeReceivableDetail.getHouseOwnerId());
							System.out.println(chargeReceivableDetail.getHouseOwnerName());
							System.out.println(chargeReceivableDetail.getFeeItemName());
							System.out.println(chargeReceivableDetail.getCreateDate());
							System.out.println(HtGson.getGsonIn().toJson(chargeReceivableDetail));
						}
	}
	@Test
	public void testFindChargeReceivableDetailPage() {
		Page<ChargeReceivableDetail> page= new Page<ChargeReceivableDetail>(7, 1);
		page=chargeReceivableDetailService.findChargeReceivableDetailPage(page, new HaloMap()
		.set("houseOwnerId", "4028813a47f2772c0147f278059605f3")
		.addGroup("feeItemId")
		.set("feeItemName:like", "水")
		);
		System.out.println(HtGson.getGsonIn("entities").toJson(page));
	}
	@Test
	public void testfindChargeReceivableDetail() {
		ChargeReceivableDetail  entity =	chargeReceivableDetailService.findChargeReceivableDetailById("3bf72ba07a8511e484510023243f49a7");
		System.out.println(entity.getHouseOwnerName());
	}
	@Test
	public void testUpdateChargeReceivableDetailNotNull() {
	
		ChargeReceivableDetail  entity = new ChargeReceivableDetail();
		//ChargeReceivableDetail  entity =	chargeReceivableDetailService.findChargeReceivableDetailById("3bf72ba07a8511e484510023243f49a7");
		entity.setReceivableDetailId("3bf72ba07a8511e484510023243f49a7");
		entity.setHouseName("qqqq");
		//entity.getHouseName();
		//System.out.println(entity);
		//entity.setHouseName("1111");
	//	ChargeReceivableDetail entity2=new Gson().fromJson(HtGson.getGsonIn().toJson(entity), ChargeReceivableDetail.class);
	//	entity.getChargeFeeItem().getName();
		//ChargeFeeItem chargeFeeItem = new ChargeFeeItem();
		//chargeFeeItem.setName("rrr");
		//entity.setChargeFeeItem(chargeFeeItem);
		//chargeReceivableDetailDao.update(entity);
		//session.beginTransaction().commit();
		chargeReceivableDetailService.updateChargeReceivableDetailNotNull(entity);
	}

}
