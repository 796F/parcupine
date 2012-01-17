package com.parq.server.dao;

import java.util.Date;

import junit.framework.TestCase;

import com.parq.server.dao.model.object.ParkingLocationUsageReport;
import com.parq.server.dao.model.object.ParkingSpace;
import com.parq.server.dao.model.object.ParkingLocationUsageReport.ParkingLocationUsageEntry;
import com.parq.server.dao.model.object.Payment.PaymentType;
import com.parq.server.dao.model.object.UserPaymentReport;
import com.parq.server.dao.model.object.UserPaymentReport.UserPaymentEntry;
import com.parq.server.dao.support.SupportScriptForDaoTesting;

public class TestAdminReportDao extends TestCase {

	private AdminReportDao adminReportDao;
	private Date reportStartDate;
	private Date reportEndDate;

	@Override
	protected void setUp() throws Exception {
		adminReportDao = new AdminReportDao();
		
		if (reportStartDate == null || reportEndDate == null) {
			reportStartDate = new Date(0); // set the report start data to be
											// UTC start time in the 1970
			reportEndDate = new Date(System.currentTimeMillis()
					+ (72 * 60 * 60 * 1000)); // set the report end date to the
												// 24 hour in the future
		}
		SupportScriptForDaoTesting.insertMainTestDataSet();
		SupportScriptForDaoTesting.createUserTestData();
		SupportScriptForDaoTesting.createUserPaymentAccount();
		SupportScriptForDaoTesting.setupParkingPaymentData();
	}

	public void testGetParkingLocationUsageReport(){
		
		// park the user 100 time using 2 minute increments
		long fiveMinuteIncrement = 5 * 60 * 1000;
		ParkingStatusDao parkingStatusDao = new ParkingStatusDao();
		long curTime = System.currentTimeMillis();
		
		int testSize = 100;
		
		for (int i = 0; i < testSize; i++) {	
			SupportScriptForDaoTesting.testParkingInstance.setParkingBeganTime(
					new Date(curTime + (i * fiveMinuteIncrement) + 2000));
			SupportScriptForDaoTesting.testParkingInstance.setParkingEndTime(
					new Date(curTime + (i * fiveMinuteIncrement) + 62000));
			SupportScriptForDaoTesting.testParkingInstance.getPaymentInfo().setPaymentDateTime(
					new Date(curTime + (i * fiveMinuteIncrement)));
			SupportScriptForDaoTesting.testParkingInstance.getPaymentInfo()
				.setPaymentRefNumber("Payment_Ref_Num: " + i);
			SupportScriptForDaoTesting.testParkingInstance.getPaymentInfo()
				.setAccountId(SupportScriptForDaoTesting.testPaymentAccount.getAccountId());
			parkingStatusDao.addNewParkingAndPayment(SupportScriptForDaoTesting.testParkingInstance);
		}
		
		long locationId = SupportScriptForDaoTesting.testParkingLocationList
			.get(0).getLocationId();
		ParkingLocationUsageReport report = adminReportDao
			.getParkingLocationUsageReport(locationId, reportStartDate, reportEndDate);
		assertNotNull(report);
		assertFalse(report.getUsageReportEntries().isEmpty());
		assertEquals(testSize, report.getUsageReportEntries().size());
		
		for (int i = 0; i < testSize; i++) {
			ParkingLocationUsageEntry usageEntry = report.getUsageReportEntries().get(i);
			assertNotNull(usageEntry);
			assertEquals(locationId, usageEntry.getLocationId());
			assertEquals(SupportScriptForDaoTesting.testParkingLocationList
					.get(0).getLocationIdentifier(), usageEntry.getLocationIdentifier());
			assertNotNull(usageEntry.getLocationName());
			assertFalse(usageEntry.getLocationName().isEmpty());
			assertTrue(usageEntry.getParkingBeganTime().compareTo(reportStartDate) > 0);
			assertTrue(usageEntry.getParkingEndTime().compareTo(reportEndDate) < 0);
			assertTrue(usageEntry.getParkingEndTime().compareTo(usageEntry.getParkingBeganTime()) > 0);
			assertNotNull(usageEntry.getParkingRefNumber());
			assertFalse(usageEntry.getParkingRefNumber().isEmpty());
			assertTrue(usageEntry.getParkingInstId() > 0);
			assertTrue(usageEntry.getSpaceId() > 0);
			assertTrue(usageEntry.getUserId() == SupportScriptForDaoTesting.testUser.getUserID());
			assertEquals(SupportScriptForDaoTesting.testUser.getEmail(), usageEntry.getUserEmail());
			assertNotNull(usageEntry.getSpaceIdentifier());
			assertTrue(usageEntry.getSpaceIdentifier().length() > 0);
		}
	}
	
	public void testGetUserPaymentReport() {

		
		// park the user 100 time using 2 minute increments
		long fiveMinuteIncrement = 5 * 60 * 1000;
		ParkingStatusDao parkingStatusDao = new ParkingStatusDao();
		long curTime = System.currentTimeMillis();
		
		int testSize = 100;
		
		for (int i = 0; i < testSize; i++) {	
			SupportScriptForDaoTesting.testParkingInstance.setParkingBeganTime(
					new Date(curTime + (i * fiveMinuteIncrement) + 2000));
			SupportScriptForDaoTesting.testParkingInstance.setParkingEndTime(
					new Date(curTime + (i * fiveMinuteIncrement) + 62000));
			SupportScriptForDaoTesting.testParkingInstance.getPaymentInfo().setPaymentDateTime(
					new Date(curTime + (i * fiveMinuteIncrement)));
			SupportScriptForDaoTesting.testParkingInstance.getPaymentInfo()
				.setPaymentRefNumber("Payment_Ref_Num: " + i);
			SupportScriptForDaoTesting.testParkingInstance.getPaymentInfo()
				.setAccountId(SupportScriptForDaoTesting.testPaymentAccount.getAccountId());
			parkingStatusDao.addNewParkingAndPayment(SupportScriptForDaoTesting.testParkingInstance);
		}
		
		UserPaymentReport report = adminReportDao
			.getUserPaymentReport(SupportScriptForDaoTesting.testUser
					.getUserID(), reportStartDate, reportEndDate);
		assertNotNull(report);
		assertFalse(report.getPaymentEntries().isEmpty());
		assertEquals(testSize, report.getPaymentEntries().size());
		
		for (int i = 0; i < testSize; i++) {	
			UserPaymentEntry paymentEntry = report.getPaymentEntries().get(i);
			assertNotNull(paymentEntry);
			assertEquals(SupportScriptForDaoTesting.testParkingInstance
					.getPaymentInfo().getAmountPaidCents(), paymentEntry.getAmountPaidCents());
			assertTrue(paymentEntry.getPaymentRefNumber() != null);
			assertFalse(paymentEntry.getPaymentRefNumber().isEmpty());			
			assertTrue(paymentEntry.getPaymentDateTime().compareTo(reportStartDate) > 0);
			assertTrue(paymentEntry.getPaymentDateTime().compareTo(reportEndDate) < 0);
			assertEquals(paymentEntry.getPaymentType(), PaymentType.CreditCard);
			assertTrue(paymentEntry.getLocationId() > 0);
			assertTrue(paymentEntry.getLocationName() != null);
			assertFalse(paymentEntry.getLocationName().isEmpty());
			assertTrue(paymentEntry.getLocationIdentifier() != null);
			assertFalse(paymentEntry.getLocationIdentifier().isEmpty());
			assertNotNull(paymentEntry.getParkingRefNumber());
			assertNotNull(paymentEntry.getParkingBeganTime());
			assertNotNull(paymentEntry.getParkingEndTime());
		}
	}
	
	public void testGetConsolidatedUserParkingReport() {
		
		long fiveMinuteIncrement = 5 * 60 * 1000;
		ParkingStatusDao parkingStatusDao = new ParkingStatusDao();
		long curTime = System.currentTimeMillis();
		
		// set up the first parking payment
		SupportScriptForDaoTesting.testParkingInstance.setParkingBeganTime(
				new Date(curTime));
		SupportScriptForDaoTesting.testParkingInstance.setParkingEndTime(
				new Date(curTime + fiveMinuteIncrement));
		SupportScriptForDaoTesting.testParkingInstance.getPaymentInfo().setPaymentDateTime(
				new Date(curTime));
		SupportScriptForDaoTesting.testParkingInstance.getPaymentInfo()
			.setPaymentRefNumber("Payment_Ref_Num: " + curTime);
		SupportScriptForDaoTesting.testParkingInstance.getPaymentInfo()
			.setAccountId(SupportScriptForDaoTesting.testPaymentAccount.getAccountId());
		parkingStatusDao.addNewParkingAndPayment(SupportScriptForDaoTesting.testParkingInstance);
		
		// refill the above parking payment once
		Date newParkingEndTime = new Date(curTime + (fiveMinuteIncrement * 2));
		parkingStatusDao.refillParkingForParkingSpace(
				SupportScriptForDaoTesting.testParkingInstance.getSpaceId(), 
				newParkingEndTime , SupportScriptForDaoTesting.testParkingInstance.getPaymentInfo());
		// refill the above parking payment again
		newParkingEndTime = new Date(curTime + (fiveMinuteIncrement * 3));
		parkingStatusDao.refillParkingForParkingSpace(
				SupportScriptForDaoTesting.testParkingInstance.getSpaceId(), 
				newParkingEndTime , SupportScriptForDaoTesting.testParkingInstance.getPaymentInfo());
		
		// create a new parking entry
		ParkingSpace pTemp = new ParkingSpaceDao()
				.getParkingSpaceBySpaceIdentifier(
						SupportScriptForDaoTesting.parkingLocationNameMain, SupportScriptForDaoTesting.spaceNameMain3);
		long newParkingStartTime = curTime + 1;
		SupportScriptForDaoTesting.testParkingInstance.setSpaceId(pTemp.getSpaceId());
		SupportScriptForDaoTesting.testParkingInstance.setParkingBeganTime(
				new Date(newParkingStartTime));
		SupportScriptForDaoTesting.testParkingInstance.setParkingEndTime(
				new Date(newParkingStartTime + fiveMinuteIncrement));
		SupportScriptForDaoTesting.testParkingInstance.getPaymentInfo().setPaymentDateTime(
				new Date(newParkingStartTime));
		SupportScriptForDaoTesting.testParkingInstance.getPaymentInfo()
			.setPaymentRefNumber("Payment_Ref_Num: " + newParkingStartTime);
		SupportScriptForDaoTesting.testParkingInstance.getPaymentInfo()
			.setAccountId(SupportScriptForDaoTesting.testPaymentAccount.getAccountId());
		parkingStatusDao.addNewParkingAndPayment(SupportScriptForDaoTesting.testParkingInstance);
		// refill the above new parking instance once
		Date newParkingEndTime2 = new Date(newParkingStartTime + (fiveMinuteIncrement * 2));
		parkingStatusDao.refillParkingForParkingSpace(
				SupportScriptForDaoTesting.testParkingInstance.getSpaceId(), 
				newParkingEndTime2 , SupportScriptForDaoTesting.testParkingInstance.getPaymentInfo());
		
		// get the consolidated report based on the above entry
		UserPaymentReport consolidatedReport = adminReportDao
				.getConsolidatedUserParkingReport(SupportScriptForDaoTesting.testUser
						.getUserID(), reportStartDate, reportEndDate);
		
		assertEquals(2, consolidatedReport.getPaymentEntries().size());
		
		UserPaymentEntry parkingEntry1 = consolidatedReport.getPaymentEntries().get(0);
		assertEquals(curTime / 1000, parkingEntry1.getParkingBeganTime().getTime() / 1000);
		assertEquals(newParkingEndTime.getTime() / 1000, parkingEntry1.getParkingEndTime().getTime() / 1000);
		assertEquals(SupportScriptForDaoTesting.testParkingInstance.getPaymentInfo().getAmountPaidCents() * 3, 
				parkingEntry1.getAmountPaidCents());
		

		UserPaymentEntry parkingEntry2 = consolidatedReport.getPaymentEntries().get(1);
		assertEquals(newParkingStartTime / 1000, parkingEntry2.getParkingBeganTime().getTime() / 1000);
		assertEquals(newParkingEndTime2.getTime() / 1000, parkingEntry2.getParkingEndTime().getTime() / 1000);
		assertEquals(SupportScriptForDaoTesting.testParkingInstance.getPaymentInfo().getAmountPaidCents() * 2, 
				parkingEntry2.getAmountPaidCents());
	}
}
