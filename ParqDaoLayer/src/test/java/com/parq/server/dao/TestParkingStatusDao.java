package com.parq.server.dao;

import java.util.Date;
import java.util.List;

import com.parq.server.dao.model.object.ParkingInstance;
import com.parq.server.dao.model.object.Payment;
import com.parq.server.dao.model.object.Payment.PaymentType;
import com.parq.server.dao.support.ParqUnitTestParent;

/**
 * @author GZ
 *
 */
public class TestParkingStatusDao extends ParqUnitTestParent {


	@Override
	protected void setUp() throws Exception {
		setupParkingPaymentData();
	}
	
	
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		// user object cleanup
		deleteUserTestData();
	}



	public void testGetParkingStatusBySpaceIds() {
		statusDao.getParkingStatusBySpaceIds(new long[]{1,2,3});
	}
	
	public void testAddNewParkingAndPayment() {
		assertTrue(statusDao.addNewParkingAndPayment(pi));
		
		List<ParkingInstance> result = statusDao.getParkingStatusBySpaceIds(new long[]{parkingLocationList.get(0).getSpaces().get(0).getSpaceId()});
		assertNotNull(result);
		assertFalse(result.isEmpty());
		ParkingInstance resultInstance = result.get(0);
		assertEquals(resultInstance.getSpaceId(), pi.getSpaceId());
		assertEquals(resultInstance.getUserId(), pi.getUserId());
		// time comparison is truncated to the nearest seconds
		assertEquals(resultInstance.getParkingBeganTime().getTime() / 1000, pi.getParkingBeganTime().getTime() / 1000);
		// time comparison is truncated to the nearest seconds
		assertEquals(resultInstance.getParkingEndTime().getTime() / 1000, pi.getParkingEndTime().getTime() / 1000);
		assertEquals(resultInstance.getPaymentInfo().getAmountPaidCents(), pi.getPaymentInfo().getAmountPaidCents());
		assertEquals(resultInstance.getPaymentInfo().getParkingInstId(), resultInstance.getParkingInstId());
		assertEquals(resultInstance.getPaymentInfo().getPaymentRefNumber(), pi.getPaymentInfo().getPaymentRefNumber());
		// time comparison is truncated to the nearest seconds
		assertEquals(resultInstance.getPaymentInfo().getPaymentDateTime().getTime() / 1000, 
					pi.getPaymentInfo().getPaymentDateTime().getTime() / 1000);
		assertEquals(resultInstance.getPaymentInfo().getPaymentType(), pi.getPaymentInfo().getPaymentType());
		assertEquals(resultInstance.getPaymentInfo().getAmountPaidCents(), pi.getPaymentInfo().getAmountPaidCents());
	}
	
	public void testRefillParkingForParkingSpace() {
		assertTrue(statusDao.addNewParkingAndPayment(pi));
		
		// add the new parking instance
		List<ParkingInstance> resultInsts = statusDao.getParkingStatusBySpaceIds(new long[]{parkingLocationList.get(0).getSpaces().get(0).getSpaceId()});
		ParkingInstance initialParkingInst = resultInsts.get(0);
		
		// refill the parking
		assertTrue(statusDao.refillParkingForParkingSpace(piRefil.getSpaceId(), piRefil.getParkingEndTime(), piRefil.getPaymentInfo()));

		// get the newly refilled parking info
		List<ParkingInstance> resultInsts2 = statusDao.getParkingStatusBySpaceIds(new long[]{parkingLocationList.get(0).getSpaces().get(0).getSpaceId()});
		assertFalse(resultInsts2.isEmpty());
		ParkingInstance refilledParkingInst = resultInsts2.get(0);
		
		
		assertEquals(refilledParkingInst.getSpaceId(), initialParkingInst.getSpaceId());
		assertEquals(refilledParkingInst.getUserId(), initialParkingInst.getUserId());
		// time comparison is truncated to the nearest minute
		assertEquals(refilledParkingInst.getParkingBeganTime().getTime() / 60000, System.currentTimeMillis() / 60000);
		// time comparison is truncated to the nearest seconds
		assertEquals(refilledParkingInst.getParkingEndTime().getTime() / 1000, piRefil.getParkingEndTime().getTime() / 1000);
		assertEquals(refilledParkingInst.getParkingRefNumber(), initialParkingInst.getParkingRefNumber());
		assertEquals(refilledParkingInst.getPaymentInfo().getAmountPaidCents(), piRefil.getPaymentInfo().getAmountPaidCents());
		assertFalse(refilledParkingInst.getPaymentInfo().getParkingInstId() == initialParkingInst.getPaymentInfo().getParkingInstId());
		assertEquals(refilledParkingInst.getPaymentInfo().getPaymentRefNumber(), piRefil.getPaymentInfo().getPaymentRefNumber());
		// time comparison is truncated to the nearest seconds
		assertEquals(refilledParkingInst.getPaymentInfo().getPaymentDateTime().getTime() / 1000, 
				piRefil.getPaymentInfo().getPaymentDateTime().getTime() / 1000);
		assertEquals(refilledParkingInst.getPaymentInfo().getPaymentType(), piRefil.getPaymentInfo().getPaymentType());
	}
	
	public void testGetUserParkingStatus() {
		assertTrue(statusDao.addNewParkingAndPayment(pi));
		
		ParkingInstance userParkingStatus = statusDao.getUserParkingStatus(testUser.getUserID());
		assertNotNull(userParkingStatus);
		assertEquals(testUser.getUserID(), userParkingStatus.getUserId());
		assertEquals(userParkingStatus.getSpaceId(), pi.getSpaceId());
		assertEquals(userParkingStatus.getUserId(), pi.getUserId());
		// time comparison is truncated to the nearest seconds
		assertEquals(userParkingStatus.getParkingBeganTime().getTime() / 1000, pi.getParkingBeganTime().getTime() / 1000);
		// time comparison is truncated to the nearest seconds
		assertEquals(userParkingStatus.getParkingEndTime().getTime() / 1000, pi.getParkingEndTime().getTime() / 1000);
		assertEquals(userParkingStatus.getPaymentInfo().getAmountPaidCents(), pi.getPaymentInfo().getAmountPaidCents());
		assertEquals(userParkingStatus.getPaymentInfo().getParkingInstId(), userParkingStatus.getParkingInstId());
		assertEquals(userParkingStatus.getPaymentInfo().getPaymentRefNumber(), pi.getPaymentInfo().getPaymentRefNumber());
		// time comparison is truncated to the nearest seconds
		assertEquals(userParkingStatus.getPaymentInfo().getPaymentDateTime().getTime() / 1000, 
					pi.getPaymentInfo().getPaymentDateTime().getTime() / 1000);
		assertEquals(userParkingStatus.getPaymentInfo().getPaymentType(), pi.getPaymentInfo().getPaymentType());
		assertEquals(userParkingStatus.getPaymentInfo().getAmountPaidCents(), pi.getPaymentInfo().getAmountPaidCents());
		
		// user object cleanup
		 boolean deleteSuccessful = userDao.deleteUserById(testUser.getUserID());
		 assertTrue(deleteSuccessful);
	}

	public void testCacheRunTime() {
		assertTrue(statusDao.addNewParkingAndPayment(pi));
		
		ParkingInstance gups = statusDao.getUserParkingStatus(testUser.getUserID());
		
		long curSysTimeBeforeCacheCall = System.currentTimeMillis();
		for (int i = 0; i < 1000; i++) {
			ParkingInstance ups = statusDao.getUserParkingStatus(testUser.getUserID());
			assertNotNull(ups);
			assertEquals(testUser.getUserID(), ups.getUserId());
			assertSame(gups, ups);
		}
		
		ParkingInstance gpsbsi = statusDao.getParkingStatusBySpaceIds(new long[]{gups.getSpaceId()}).get(0);
		for (int i = 0; i < 1000; i++) {
			ParkingInstance ps = statusDao.getParkingStatusBySpaceIds(new long[]{gups.getSpaceId()}).get(0);
			assertNotNull(ps);
			assertEquals(testUser.getUserID(), ps.getUserId());
			assertSame(gpsbsi, ps);
		}
		
		long curSysTimeAfterCacheCall = System.currentTimeMillis();
		// check to see that the a call to the get method 2000 time result in runtime of less then 1 second
		assertTrue(curSysTimeAfterCacheCall - curSysTimeBeforeCacheCall < 1000);
		// System.out.println("20000 cache hit time in milliseconds: " 
		//	+ (curSysTimeAfterCacheCall - curSysTimeBeforeCacheCall));
		
		// user object cleanup
		boolean deleteSuccessful = userDao.deleteUserById(testUser.getUserID());
		assertTrue(deleteSuccessful);
	}
	
	public void testCacheUpdate() {
		assertTrue(statusDao.addNewParkingAndPayment(pi));
		
		ParkingInstance oldGUPS = statusDao.getUserParkingStatus(testUser.getUserID());
		
		// make multiple cache hits to ensure current result are cached.
		for (int i = 0; i < 10; i++) {
			ParkingInstance ups = statusDao.getUserParkingStatus(testUser.getUserID());
			assertNotNull(ups);
			assertEquals(testUser.getUserID(), ups.getUserId());
			assertSame(oldGUPS, ups);
		}
		
		ParkingInstance oldGPSBSI = statusDao.getParkingStatusBySpaceIds(new long[]{oldGUPS.getSpaceId()}).get(0);
		for (int i = 0; i < 10; i++) {
			ParkingInstance ps = statusDao.getParkingStatusBySpaceIds(new long[]{oldGUPS.getSpaceId()}).get(0);
			assertNotNull(ps);
			assertEquals(testUser.getUserID(), ps.getUserId());
			assertSame(oldGPSBSI, ps);
		}
		
		// update the payment information
		ParkingInstance newPi = new ParkingInstance();
		newPi.setPaidParking(true);
		newPi.setParkingBeganTime(new Date(System.currentTimeMillis() + 5000));
		newPi.setParkingEndTime(new Date(System.currentTimeMillis() + 3600000));
		newPi.setSpaceId(parkingLocationList.get(0).getSpaces().get(0).getSpaceId());
		newPi.setUserId(testUser.getUserID());
		
		Payment newPaymentInfo = new Payment();
		newPaymentInfo.setAmountPaidCents(550);
		newPaymentInfo.setPaymentDateTime(new Date(System.currentTimeMillis() + 5000));
		newPaymentInfo.setPaymentRefNumber("Test_Payment_Ref_Num_2");
		newPaymentInfo.setPaymentType(PaymentType.CreditCard);
		newPi.setPaymentInfo(newPaymentInfo);
		
		assertTrue(statusDao.addNewParkingAndPayment(newPi));
		
		ParkingInstance newGUPS = statusDao.getUserParkingStatus(testUser.getUserID());
		ParkingInstance newGPSBSI = statusDao.getParkingStatusBySpaceIds(new long[]{oldGUPS.getSpaceId()}).get(0);
		
		assertNotSame(oldGUPS, newGUPS);
		assertNotSame(oldGPSBSI, newGPSBSI);
		assertTrue(newGUPS.getParkingInstId() > oldGUPS.getParkingInstId());
		assertTrue(newGUPS.getPaymentInfo().getPaymentId() > oldGUPS.getPaymentInfo().getPaymentId());
		assertEquals("Test_Payment_Ref_Num_2" ,newGUPS.getPaymentInfo().getPaymentRefNumber());
		assertEquals(550 ,newGUPS.getPaymentInfo().getAmountPaidCents());
		
		// test cache
		for (int i = 0; i < 10; i++) {
			ParkingInstance ups = statusDao.getUserParkingStatus(testUser.getUserID());
			assertNotNull(ups);
			assertEquals(testUser.getUserID(), ups.getUserId());
			assertSame(newGUPS, ups);
		}
		
		// test cache
		for (int i = 0; i < 10; i++) {
			ParkingInstance ps = statusDao.getParkingStatusBySpaceIds(new long[]{oldGUPS.getSpaceId()}).get(0);
			assertNotNull(ps);
			assertEquals(testUser.getUserID(), ps.getUserId());
			assertSame(newGPSBSI, ps);
		}
		
		// user object cleanup
		boolean deleteSuccessful = userDao.deleteUserById(testUser.getUserID());
		assertTrue(deleteSuccessful);
	}
	
	public void testUnparkBySpaceIdAndParkingInstId() {
		assertTrue(statusDao.addNewParkingAndPayment(pi));

		// update the parking endtime to current time minus 60 sec
		ParkingInstance oldPiStatus = statusDao.getUserParkingStatus(testUser
				.getUserID());
		long curTime = System.currentTimeMillis() - (60 * 1000);

		statusDao.unparkBySpaceIdAndParkingRefNum(oldPiStatus.getSpaceId(),
				oldPiStatus.getParkingRefNumber(), new Date(curTime));
		
		ParkingInstance newPiStatus = statusDao.getUserParkingStatus(testUser
				.getUserID());
		// timestamp stored in the DB is only accurate to the second interval.
		assertEquals(newPiStatus.getParkingEndTime().getTime() / 1000,  curTime / 1000);
		
		// user object cleanup
		 boolean deleteSuccessful = userDao.deleteUserById(testUser.getUserID());
		 assertTrue(deleteSuccessful);
	}
	
}
