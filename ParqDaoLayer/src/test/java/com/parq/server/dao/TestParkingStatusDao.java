package com.parq.server.dao;

import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import com.parq.server.dao.model.object.ParkingInstance;
import com.parq.server.dao.model.object.Payment;
import com.parq.server.dao.model.object.Payment.PaymentType;
import com.parq.server.dao.support.SupportScriptForDaoTesting;

/**
 * @author GZ
 * 
 */
public class TestParkingStatusDao extends TestCase {

	private ParkingStatusDao statusDao;
	private UserDao userDao;

	@Override
	protected void setUp() throws Exception {
		SupportScriptForDaoTesting.setupParkingPaymentData();
		statusDao = new ParkingStatusDao();
		userDao = new UserDao();
	}

	public void testGetParkingStatusBySpaceIds() {
		statusDao.getParkingStatusBySpaceIds(new long[] { 1, 2, 3 });
	}

	public void testAddNewParkingAndPayment() {
		assertTrue(statusDao
				.addNewParkingAndPayment(SupportScriptForDaoTesting.testParkingInstance));

		List<ParkingInstance> result = statusDao
				.getParkingStatusBySpaceIds(new long[] { SupportScriptForDaoTesting.testParkingLocationList
						.get(0).getSpaces().get(0).getSpaceId() });
		assertNotNull(result);
		assertFalse(result.isEmpty());
		ParkingInstance resultInstance = result.get(0);
		assertEquals(resultInstance.getSpaceId(),
				SupportScriptForDaoTesting.testParkingInstance.getSpaceId());
		assertEquals(resultInstance.getUserId(),
				SupportScriptForDaoTesting.testParkingInstance.getUserId());
		// time comparison is truncated to the nearest seconds
		assertEquals(resultInstance.getParkingBeganTime().getTime() / 1000,
				SupportScriptForDaoTesting.testParkingInstance
						.getParkingBeganTime().getTime() / 1000);
		// time comparison is truncated to the nearest seconds
		assertEquals(resultInstance.getParkingEndTime().getTime() / 1000,
				SupportScriptForDaoTesting.testParkingInstance
						.getParkingEndTime().getTime() / 1000);
		assertEquals(resultInstance.getPaymentInfo().getAmountPaidCents(),
				SupportScriptForDaoTesting.testParkingInstance.getPaymentInfo()
						.getAmountPaidCents());
		assertEquals(resultInstance.getPaymentInfo().getParkingInstId(),
				resultInstance.getParkingInstId());
		assertEquals(resultInstance.getPaymentInfo().getPaymentRefNumber(),
				SupportScriptForDaoTesting.testParkingInstance.getPaymentInfo()
						.getPaymentRefNumber());
		// time comparison is truncated to the nearest seconds
		assertEquals(resultInstance.getPaymentInfo().getPaymentDateTime()
				.getTime() / 1000,
				SupportScriptForDaoTesting.testParkingInstance.getPaymentInfo()
						.getPaymentDateTime().getTime() / 1000);
		assertEquals(resultInstance.getPaymentInfo().getPaymentType(),
				SupportScriptForDaoTesting.testParkingInstance.getPaymentInfo()
						.getPaymentType());
		assertEquals(resultInstance.getPaymentInfo().getAmountPaidCents(),
				SupportScriptForDaoTesting.testParkingInstance.getPaymentInfo()
						.getAmountPaidCents());
	}

	public void testRefillParkingForParkingSpace() {
		assertTrue(statusDao
				.addNewParkingAndPayment(SupportScriptForDaoTesting.testParkingInstance));

		// add the new parking instance
		List<ParkingInstance> resultInsts = statusDao
				.getParkingStatusBySpaceIds(new long[] { SupportScriptForDaoTesting.testParkingLocationList
						.get(0).getSpaces().get(0).getSpaceId() });
		ParkingInstance initialParkingInst = resultInsts.get(0);

		// refill the parking
		assertTrue(statusDao.refillParkingForParkingSpace(
				SupportScriptForDaoTesting.testParkingInstanceRefil
						.getSpaceId(),
				SupportScriptForDaoTesting.testParkingInstanceRefil
						.getParkingEndTime(),
				SupportScriptForDaoTesting.testParkingInstanceRefil
						.getPaymentInfo()));

		// get the newly refilled parking info
		List<ParkingInstance> resultInsts2 = statusDao
				.getParkingStatusBySpaceIds(new long[] { SupportScriptForDaoTesting.testParkingLocationList
						.get(0).getSpaces().get(0).getSpaceId() });
		assertFalse(resultInsts2.isEmpty());
		ParkingInstance refilledParkingInst = resultInsts2.get(0);

		assertEquals(refilledParkingInst.getSpaceId(), initialParkingInst
				.getSpaceId());
		assertEquals(refilledParkingInst.getUserId(), initialParkingInst
				.getUserId());
		// time comparison is truncated to the nearest minute
		assertEquals(
				refilledParkingInst.getParkingBeganTime().getTime() / 60000,
				System.currentTimeMillis() / 60000);
		// time comparison is truncated to the nearest seconds
		assertEquals(refilledParkingInst.getParkingEndTime().getTime() / 1000,
				SupportScriptForDaoTesting.testParkingInstanceRefil
						.getParkingEndTime().getTime() / 1000);
		assertEquals(refilledParkingInst.getParkingRefNumber(),
				initialParkingInst.getParkingRefNumber());
		assertEquals(refilledParkingInst.getPaymentInfo().getAmountPaidCents(),
				SupportScriptForDaoTesting.testParkingInstanceRefil
						.getPaymentInfo().getAmountPaidCents());
		assertFalse(refilledParkingInst.getPaymentInfo().getParkingInstId() == initialParkingInst
				.getPaymentInfo().getParkingInstId());
		assertEquals(
				refilledParkingInst.getPaymentInfo().getPaymentRefNumber(),
				SupportScriptForDaoTesting.testParkingInstanceRefil
						.getPaymentInfo().getPaymentRefNumber());
		// time comparison is truncated to the nearest seconds
		assertEquals(refilledParkingInst.getPaymentInfo().getPaymentDateTime()
				.getTime() / 1000,
				SupportScriptForDaoTesting.testParkingInstanceRefil
						.getPaymentInfo().getPaymentDateTime().getTime() / 1000);
		assertEquals(refilledParkingInst.getPaymentInfo().getPaymentType(),
				SupportScriptForDaoTesting.testParkingInstanceRefil
						.getPaymentInfo().getPaymentType());
	}

	public void testGetUserParkingStatus() {
		assertTrue(statusDao
				.addNewParkingAndPayment(SupportScriptForDaoTesting.testParkingInstance));

		ParkingInstance userParkingStatus = statusDao
				.getUserParkingStatus(SupportScriptForDaoTesting.testUser
						.getUserID());
		assertNotNull(userParkingStatus);
		assertEquals(SupportScriptForDaoTesting.testUser.getUserID(),
				userParkingStatus.getUserId());
		assertEquals(userParkingStatus.getSpaceId(),
				SupportScriptForDaoTesting.testParkingInstance.getSpaceId());
		assertEquals(userParkingStatus.getUserId(),
				SupportScriptForDaoTesting.testParkingInstance.getUserId());
		// time comparison is truncated to the nearest seconds
		assertEquals(userParkingStatus.getParkingBeganTime().getTime() / 1000,
				SupportScriptForDaoTesting.testParkingInstance
						.getParkingBeganTime().getTime() / 1000);
		// time comparison is truncated to the nearest seconds
		assertEquals(userParkingStatus.getParkingEndTime().getTime() / 1000,
				SupportScriptForDaoTesting.testParkingInstance
						.getParkingEndTime().getTime() / 1000);
		assertEquals(userParkingStatus.getPaymentInfo().getAmountPaidCents(),
				SupportScriptForDaoTesting.testParkingInstance.getPaymentInfo()
						.getAmountPaidCents());
		assertEquals(userParkingStatus.getPaymentInfo().getParkingInstId(),
				userParkingStatus.getParkingInstId());
		assertEquals(userParkingStatus.getPaymentInfo().getPaymentRefNumber(),
				SupportScriptForDaoTesting.testParkingInstance.getPaymentInfo()
						.getPaymentRefNumber());
		// time comparison is truncated to the nearest seconds
		assertEquals(userParkingStatus.getPaymentInfo().getPaymentDateTime()
				.getTime() / 1000,
				SupportScriptForDaoTesting.testParkingInstance.getPaymentInfo()
						.getPaymentDateTime().getTime() / 1000);
		assertEquals(userParkingStatus.getPaymentInfo().getPaymentType(),
				SupportScriptForDaoTesting.testParkingInstance.getPaymentInfo()
						.getPaymentType());
		assertEquals(userParkingStatus.getPaymentInfo().getAmountPaidCents(),
				SupportScriptForDaoTesting.testParkingInstance.getPaymentInfo()
						.getAmountPaidCents());

		// user object cleanup
		boolean deleteSuccessful = userDao
				.deleteUserById(SupportScriptForDaoTesting.testUser.getUserID());
		assertTrue(deleteSuccessful);
	}

	public void testCacheRunTime() {
		assertTrue(statusDao
				.addNewParkingAndPayment(SupportScriptForDaoTesting.testParkingInstance));

		ParkingInstance gups = statusDao
				.getUserParkingStatus(SupportScriptForDaoTesting.testUser
						.getUserID());

		long curSysTimeBeforeCacheCall = System.currentTimeMillis();
		for (int i = 0; i < 1000; i++) {
			ParkingInstance ups = statusDao
					.getUserParkingStatus(SupportScriptForDaoTesting.testUser
							.getUserID());
			assertNotNull(ups);
			assertEquals(SupportScriptForDaoTesting.testUser.getUserID(), ups
					.getUserId());
			assertSame(gups, ups);
		}

		ParkingInstance gpsbsi = statusDao.getParkingStatusBySpaceIds(
				new long[] { gups.getSpaceId() }).get(0);
		for (int i = 0; i < 1000; i++) {
			ParkingInstance ps = statusDao.getParkingStatusBySpaceIds(
					new long[] { gups.getSpaceId() }).get(0);
			assertNotNull(ps);
			assertEquals(SupportScriptForDaoTesting.testUser.getUserID(), ps
					.getUserId());
			assertSame(gpsbsi, ps);
		}

		long curSysTimeAfterCacheCall = System.currentTimeMillis();
		// check to see that the a call to the get method 2000 time result in
		// runtime of less then 1 second
		assertTrue(curSysTimeAfterCacheCall - curSysTimeBeforeCacheCall < 1000);
		// System.out.println("20000 cache hit time in milliseconds: "
		// + (curSysTimeAfterCacheCall - curSysTimeBeforeCacheCall));

	}

	public void testCacheUpdate() {
		assertTrue(statusDao
				.addNewParkingAndPayment(SupportScriptForDaoTesting.testParkingInstance));

		ParkingInstance oldGUPS = statusDao
				.getUserParkingStatus(SupportScriptForDaoTesting.testUser
						.getUserID());

		// make multiple cache hits to ensure current result are cached.
		for (int i = 0; i < 10; i++) {
			ParkingInstance ups = statusDao
					.getUserParkingStatus(SupportScriptForDaoTesting.testUser
							.getUserID());
			assertNotNull(ups);
			assertEquals(SupportScriptForDaoTesting.testUser.getUserID(), ups
					.getUserId());
			assertSame(oldGUPS, ups);
		}

		ParkingInstance oldGPSBSI = statusDao.getParkingStatusBySpaceIds(
				new long[] { oldGUPS.getSpaceId() }).get(0);
		for (int i = 0; i < 10; i++) {
			ParkingInstance ps = statusDao.getParkingStatusBySpaceIds(
					new long[] { oldGUPS.getSpaceId() }).get(0);
			assertNotNull(ps);
			assertEquals(SupportScriptForDaoTesting.testUser.getUserID(), ps
					.getUserId());
			assertSame(oldGPSBSI, ps);
		}

		// update the payment information
		ParkingInstance newPi = new ParkingInstance();
		newPi.setPaidParking(true);
		newPi.setParkingBeganTime(new Date(System.currentTimeMillis() + 5000));
		newPi.setParkingEndTime(new Date(System.currentTimeMillis() + 3600000));
		newPi.setSpaceId(SupportScriptForDaoTesting.testParkingLocationList
				.get(0).getSpaces().get(0).getSpaceId());
		newPi.setUserId(SupportScriptForDaoTesting.testUser.getUserID());

		Payment newPaymentInfo = new Payment();
		newPaymentInfo.setAmountPaidCents(550);
		newPaymentInfo.setPaymentDateTime(new Date(
				System.currentTimeMillis() + 5000));
		newPaymentInfo.setPaymentRefNumber("Test_Payment_Ref_Num_2");
		newPaymentInfo.setPaymentType(PaymentType.CreditCard);
		newPi.setPaymentInfo(newPaymentInfo);

		assertTrue(statusDao.addNewParkingAndPayment(newPi));

		ParkingInstance newGUPS = statusDao
				.getUserParkingStatus(SupportScriptForDaoTesting.testUser
						.getUserID());
		ParkingInstance newGPSBSI = statusDao.getParkingStatusBySpaceIds(
				new long[] { oldGUPS.getSpaceId() }).get(0);

		assertNotSame(oldGUPS, newGUPS);
		assertNotSame(oldGPSBSI, newGPSBSI);
		assertTrue(newGUPS.getParkingInstId() > oldGUPS.getParkingInstId());
		assertTrue(newGUPS.getPaymentInfo().getPaymentId() > oldGUPS
				.getPaymentInfo().getPaymentId());
		assertEquals("Test_Payment_Ref_Num_2", newGUPS.getPaymentInfo()
				.getPaymentRefNumber());
		assertEquals(550, newGUPS.getPaymentInfo().getAmountPaidCents());

		// test cache
		for (int i = 0; i < 10; i++) {
			ParkingInstance ups = statusDao
					.getUserParkingStatus(SupportScriptForDaoTesting.testUser
							.getUserID());
			assertNotNull(ups);
			assertEquals(SupportScriptForDaoTesting.testUser.getUserID(), ups
					.getUserId());
			assertSame(newGUPS, ups);
		}

		// test cache
		for (int i = 0; i < 10; i++) {
			ParkingInstance ps = statusDao.getParkingStatusBySpaceIds(
					new long[] { oldGUPS.getSpaceId() }).get(0);
			assertNotNull(ps);
			assertEquals(SupportScriptForDaoTesting.testUser.getUserID(), ps
					.getUserId());
			assertSame(newGPSBSI, ps);
		}
	}

	public void testUnparkBySpaceIdAndParkingInstId() {
		assertTrue(statusDao
				.addNewParkingAndPayment(SupportScriptForDaoTesting.testParkingInstance));

		// update the parking endtime to current time minus 60 sec
		ParkingInstance oldPiStatus = statusDao
				.getUserParkingStatus(SupportScriptForDaoTesting.testUser
						.getUserID());
		long curTime = System.currentTimeMillis() - (60 * 1000);

		statusDao.unparkBySpaceIdAndParkingRefNum(oldPiStatus.getSpaceId(),
				oldPiStatus.getParkingRefNumber(), new Date(curTime));

		ParkingInstance newPiStatus = statusDao
				.getUserParkingStatus(SupportScriptForDaoTesting.testUser
						.getUserID());
		// timestamp stored in the DB is only accurate to the second interval.
		assertEquals(newPiStatus.getParkingEndTime().getTime() / 1000,
				curTime / 1000);
	}

}
