package com.parq.server.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.parq.server.dao.model.object.Payment;
import com.parq.server.dao.model.object.PaymentAccount;
import com.parq.server.dao.model.object.PendingCharge;
import com.parq.server.dao.support.SupportScriptForDaoTesting;

import junit.framework.TestCase;

public class TestBatchCCProcessingDao extends TestCase {

	private BatchCCProcessingDao batchProcessingDao; 
	private ParkingStatusDao statusDao;
	
	private static long numOfTestPayment = 100;
	private static long hourDifferenceBetweenPayment = 24;
	
	private static final String fakeCCChargeToken = "FAKE_CHARGE";
	private static final String updatedFakeCCChargeToken = "UPDATED_FAKE_CHARGE";
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		if (batchProcessingDao == null) {
			batchProcessingDao = new BatchCCProcessingDao();
			statusDao = new ParkingStatusDao();
		}
		SupportScriptForDaoTesting.setupParkingPaymentData();
		SupportScriptForDaoTesting.createUserPaymentAccount();
	}

	public void testGetPendingCharge() {
		
		SupportScriptForDaoTesting.testParkingInstance.getPaymentInfo()
				.setAccountId(
						SupportScriptForDaoTesting.testPaymentAccount
								.getAccountId());
		SupportScriptForDaoTesting.testParkingInstance
				.getPaymentInfo().setPaymentRefNumber(fakeCCChargeToken);
		
		// setup 100 payment with 24 hour difference between them
		long curTime = System.currentTimeMillis();
		for (long i = numOfTestPayment; i > 0; i--) {
			long startTime = curTime - (hourDifferenceBetweenPayment * i * 60 * 60 * 1000);
			long endTime = startTime + (30 * 60 * 1000); // half hour parking interval
			// assertTrue(curTime > startTime);
			// assertTrue(curTime > endTime);	
			SupportScriptForDaoTesting.testParkingInstance.setParkingBeganTime(new Date(startTime));
			SupportScriptForDaoTesting.testParkingInstance.setParkingBeganTime(new Date(endTime));
			SupportScriptForDaoTesting.testParkingInstance
					.getPaymentInfo().setPaymentDateTime(new Date(startTime));
			
			boolean insertSuccessful = statusDao
				.addNewParkingAndPayment(SupportScriptForDaoTesting.testParkingInstance);	
			assertTrue(insertSuccessful);
		}
		
		// check to see that the insert was successful
		Map<PaymentAccount, List<PendingCharge>> mapOfCharges = 
				batchProcessingDao.getPendingCharge(fakeCCChargeToken, new Date(0), new Date(curTime));
		assertNotNull(mapOfCharges);
		assertFalse(mapOfCharges.isEmpty());
		// check that there is 100 entry in the map
		int sizeOfMap = 0;
		for (Entry<PaymentAccount, List<PendingCharge>> e : mapOfCharges.entrySet()) {
			assertNotNull(e.getValue());
			sizeOfMap += e.getValue().size();
		}
		assertEquals(numOfTestPayment, sizeOfMap);
		
		// check that there is only 1 paymentAccount
		assertEquals(1, mapOfCharges.entrySet().size());
		
		// check the charge list object is fully populated
		Entry<PaymentAccount, List<PendingCharge>> entry = mapOfCharges.entrySet().iterator().next();
		PaymentAccount paymentAccount = entry.getKey();
		List<PendingCharge> chargeList = entry.getValue();
		
		assertEquals(SupportScriptForDaoTesting.testCCStub, paymentAccount.getCcStub());
		assertEquals(SupportScriptForDaoTesting.testCustomerId, paymentAccount.getCustomerId());
		assertEquals(true, paymentAccount.isDefaultPaymentMethod());
		assertEquals(SupportScriptForDaoTesting.testPaymentMethodId, paymentAccount.getPaymentMethodId());
		assertEquals(SupportScriptForDaoTesting.testUser.getUserID(), paymentAccount.getUserId());
		assertTrue(paymentAccount.getAccountId() > 0);
		
		Payment orgPayment = SupportScriptForDaoTesting.testParkingInstance.getPaymentInfo();
		for (PendingCharge pc : chargeList) {
			assertEquals(paymentAccount, pc.getPaymentAccount());
			assertEquals(orgPayment.getAccountId(), pc.getPaymentAccount().getAccountId());
			assertEquals(orgPayment.getAmountPaidCents(), pc.getAmountPaid());
			assertTrue(pc.getParkingInstId() > 0);
			assertTrue(pc.getPaymentId() > 0);
			assertEquals(orgPayment.getPaymentRefNumber(), pc
					.getPaymentRefNumber());
			assertEquals(fakeCCChargeToken, pc.getPaymentRefNumber());
			assertNotNull(pc.getPaymentDateTime().getTime());
			assertEquals(orgPayment.getPaymentType(), pc.getPaymentType());
		}
		
		// check for date range partial retrieve (7 day)
		int numOfPartialRetrieve = 7;
		long retrieveStartDate = curTime - (60L * 60 * 1000 * 24 * numOfPartialRetrieve);
		retrieveStartDate = retrieveStartDate - (60L * 60 * 1000); // add an hour to fix time comparison issues
		mapOfCharges = batchProcessingDao.getPendingCharge(
				fakeCCChargeToken, new Date(retrieveStartDate), new Date(curTime));
		assertNotNull(mapOfCharges);
		assertFalse(mapOfCharges.isEmpty());
		assertEquals(1, mapOfCharges.entrySet().size());
		assertEquals(numOfPartialRetrieve, mapOfCharges.entrySet().iterator().next().getValue().size());
	}
	
	public void testBatchUpdatePaymentInfo() {

		long curTime = System.currentTimeMillis();
		// update the reference number
		Map<PaymentAccount, List<PendingCharge>> mapOfCharges = 
			batchProcessingDao.getPendingCharge(fakeCCChargeToken, new Date(0), new Date(curTime));
		List<PendingCharge> chargeList = mapOfCharges.entrySet().iterator().next().getValue();
		
		for (PendingCharge pc : chargeList) {
			pc.setPaymentRefNumber(updatedFakeCCChargeToken);
		}
		batchProcessingDao.batchUpdatePaymentInfo(chargeList);
		
		// check to make sure the updated payment_ref_num shows correctly
		Map<PaymentAccount, List<PendingCharge>> updatedMapOfCharges = 
			batchProcessingDao.getPendingCharge(updatedFakeCCChargeToken, new Date(0), new Date(curTime));
		assertNotNull(updatedMapOfCharges);
		assertEquals(1, updatedMapOfCharges.entrySet().size());
		assertEquals(numOfTestPayment, updatedMapOfCharges.entrySet().iterator().next().getValue().size());
		List<PendingCharge> updatedChargeList = 
			updatedMapOfCharges.entrySet().iterator().next().getValue();
		for (PendingCharge upc : updatedChargeList) {
			assertEquals(updatedFakeCCChargeToken, upc.getPaymentRefNumber());
		}
		
		// check for partial update
		List<PendingCharge> revertToOldChargeNumList = new ArrayList<PendingCharge>();
		for (int i = 0; i < numOfTestPayment / 2; i++) {
			chargeList.get(i).setPaymentRefNumber(fakeCCChargeToken);
			revertToOldChargeNumList.add(chargeList.get(i));
		}
		batchProcessingDao.batchUpdatePaymentInfo(revertToOldChargeNumList);
		updatedMapOfCharges = 
			batchProcessingDao.getPendingCharge(fakeCCChargeToken, new Date(0), new Date(curTime));
		assertNotNull(updatedMapOfCharges);
		assertEquals(1, updatedMapOfCharges.entrySet().size());
		assertEquals(numOfTestPayment / 2, 
				updatedMapOfCharges.entrySet().iterator().next().getValue().size());
		
	}
}
