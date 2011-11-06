package com.parq.server.dao;

import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import com.parq.server.dao.model.object.ParkingLocation;
import com.parq.server.dao.model.object.ParkingInstance;
import com.parq.server.dao.model.object.Payment;
import com.parq.server.dao.model.object.User;
import com.parq.server.dao.model.object.Payment.PaymentType;
import com.parq.server.dao.support.SupportScriptForDaoTesting;

public class TestParkingStatusDao extends TestCase {

	private ParkingStatusDao statusDao;

	@Override
	protected void setUp() throws Exception {
		statusDao = new ParkingStatusDao();
	}
	
	public void testGetParkingStatusBySpaceIds() {
		statusDao.getParkingStatusBySpaceIds(new int[]{1,2,3});
	}
	
	public void testAddPaymentForParking() {
		SupportScriptForDaoTesting.insertFakeData();
		
		User newUser = new User();
		newUser.setUserName("userName");
		newUser.setPassword("password");
		newUser.setEmail("eMail");
		UserDao userDao = new UserDao();
		boolean userCreationSuccessful = userDao.createNewUser(newUser);
		assertTrue(userCreationSuccessful);
		User user = userDao.getUserByUserName("userName");
		
		ClientDao clientDao = new ClientDao();
		List<ParkingLocation> buildingList = clientDao.getParkingLocationsAndSpacesByClientId(
				clientDao.getClientByName(SupportScriptForDaoTesting.clientNameMain).getId());
		
		ParkingInstance pi = new ParkingInstance();
		pi.setPaidParking(true);
		pi.setParkingBeganTime(new Date(System.currentTimeMillis()));
		pi.setParkingEndTime(new Date(System.currentTimeMillis() + 3600000));
		pi.setSpaceId(buildingList.get(0).getSpaces().get(0).getSpaceId());
		pi.setUserId(user.getUserID());
		
		Payment paymentInfo = new Payment();
		paymentInfo.setAmountPaid(10.05);
		paymentInfo.setPaymentDateTime(new Date(System.currentTimeMillis()));
		paymentInfo.setPaymentRefNumber("Test_Payment_Ref_Num_1");
		paymentInfo.setPaymentType(PaymentType.CreditCard);
		pi.setPaymentInfo(paymentInfo);
		
		assertTrue(statusDao.addPaymentForParking(pi));
		
		List<ParkingInstance> result = statusDao.getParkingStatusBySpaceIds(new int[]{buildingList.get(0).getSpaces().get(0).getSpaceId()});
		assertNotNull(result);
		assertFalse(result.isEmpty());
		ParkingInstance resultInstance = result.get(0);
		assertEquals(resultInstance.getSpaceId(), pi.getSpaceId());
		assertEquals(resultInstance.getUserId(), pi.getUserId());
		// time comparison is truncated to the nearest seconds
		assertEquals(resultInstance.getParkingBeganTime().getTime() / 1000, pi.getParkingBeganTime().getTime() / 1000);
		// time comparison is truncated to the nearest seconds
		assertEquals(resultInstance.getParkingEndTime().getTime() / 1000, pi.getParkingEndTime().getTime() / 1000);
		assertEquals(resultInstance.getPaymentInfo().getAmountPaid(), pi.getPaymentInfo().getAmountPaid());
		assertEquals(resultInstance.getPaymentInfo().getParkingInstId(), resultInstance.getParkingInstId());
		assertEquals(resultInstance.getPaymentInfo().getPaymentRefNumber(), pi.getPaymentInfo().getPaymentRefNumber());
		// time comparison is truncated to the nearest seconds
		assertEquals(resultInstance.getPaymentInfo().getPaymentDateTime().getTime() / 1000, 
					pi.getPaymentInfo().getPaymentDateTime().getTime() / 1000);
		assertEquals(resultInstance.getPaymentInfo().getPaymentType(), pi.getPaymentInfo().getPaymentType());
		
		 boolean deleteSuccessful = userDao.deleteUserById(user.getUserID());
		 assertTrue(deleteSuccessful);
	}
	
	
}
