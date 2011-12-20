package com.parq.server.dao;

import java.util.Date;

import com.parq.server.dao.model.object.ParkingLocationUsageReport;
import com.parq.server.dao.model.object.UserPaymentReport;
import com.parq.server.dao.model.object.ParkingLocationUsageReport.ParkingLocationUsageEntry;
import com.parq.server.dao.model.object.Payment.PaymentType;
import com.parq.server.dao.model.object.UserPaymentReport.UserPaymentEntry;
import com.parq.server.dao.support.ParqUnitTestParent;
import com.parq.server.dao.support.SupportScriptForDaoTesting;

public class TestAdminReportDao extends ParqUnitTestParent {

	private AdminReportDao adminReportDao;
	private Date reportStartDate;
	private Date reportEndDate;

	@Override
	protected void setUp() throws Exception {
		deleteUserTestData();
		adminReportDao = new AdminReportDao();
		
		if (reportStartDate == null || reportEndDate == null) {
			reportStartDate = new Date(0); // set the report start data to be
											// UTC start time in the 1970
			reportEndDate = new Date(System.currentTimeMillis()
					+ (72 * 60 * 60 * 1000)); // set the report end date to the
												// 24 hour in the future
		}
		createUserTestData();
		createUserPaymentAccount();
		setupParkingPaymentData();
	}
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		deleteUserTestData();
	}



	public void testGetParkingLocationUsageReport(){
		SupportScriptForDaoTesting.insertFakeData();
		
		// park the user 100 time using 2 minute increments
		long fiveMinuteIncrement = 5 * 60 * 1000;
		ParkingStatusDao parkingStatusDao = new ParkingStatusDao();
		long curTime = System.currentTimeMillis();
		
		int testSize = 100;
		
		for (int i = 0; i < testSize; i++) {	
			pi.setParkingBeganTime(
					new Date(curTime + (i * fiveMinuteIncrement) + 2000));
			pi.setParkingEndTime(
					new Date(curTime + (i * fiveMinuteIncrement) + 62000));
			pi.getPaymentInfo().setPaymentDateTime(
					new Date(curTime + (i * fiveMinuteIncrement)));
			pi.getPaymentInfo().setPaymentRefNumber("Payment_Ref_Num: " + i);
			pi.getPaymentInfo().setAccountId(testPaymentAccount.getAccountId());
			parkingStatusDao.addNewParkingAndPayment(pi);
		}
		
		long locationId = parkingLocationList.get(0).getLocationId();
		ParkingLocationUsageReport report = adminReportDao
			.getParkingLocationUsageReport(locationId, reportStartDate, reportEndDate);
		assertNotNull(report);
		assertFalse(report.getUsageReportEntries().isEmpty());
		assertEquals(testSize, report.getUsageReportEntries().size());
		
		for (int i = 0; i < testSize; i++) {
			ParkingLocationUsageEntry usageEntry = report.getUsageReportEntries().get(i);
			assertNotNull(usageEntry);
			assertEquals(locationId, usageEntry.getLocationId());
			assertEquals(parkingLocationList.get(0).getLocationIdentifier(), usageEntry.getLocationIdentifier());
			assertNotNull(usageEntry.getLocationName());
			assertFalse(usageEntry.getLocationName().isEmpty());
			assertTrue(usageEntry.getParkingBeganTime().compareTo(reportStartDate) > 0);
			assertTrue(usageEntry.getParkingEndTime().compareTo(reportEndDate) < 0);
			assertTrue(usageEntry.getParkingEndTime().compareTo(usageEntry.getParkingBeganTime()) > 0);
			assertNotNull(usageEntry.getParkingRefNumber());
			assertFalse(usageEntry.getParkingRefNumber().isEmpty());
			assertTrue(usageEntry.getParkingInstId() > 0);
			assertTrue(usageEntry.getSpaceId() > 0);
			assertTrue(usageEntry.getUserId() == testUser.getUserID());
		}
	}
	
	public void testGetUserPaymentReport() {
		SupportScriptForDaoTesting.insertFakeData();
		
		// park the user 100 time using 2 minute increments
		long fiveMinuteIncrement = 5 * 60 * 1000;
		ParkingStatusDao parkingStatusDao = new ParkingStatusDao();
		long curTime = System.currentTimeMillis();
		
		int testSize = 100;
		
		for (int i = 0; i < testSize; i++) {	
			pi.setParkingBeganTime(
					new Date(curTime + (i * fiveMinuteIncrement) + 2000));
			pi.setParkingEndTime(
					new Date(curTime + (i * fiveMinuteIncrement) + 62000));
			pi.getPaymentInfo().setPaymentDateTime(
					new Date(curTime + (i * fiveMinuteIncrement)));
			pi.getPaymentInfo().setPaymentRefNumber("Payment_Ref_Num: " + i);
			pi.getPaymentInfo().setAccountId(testPaymentAccount.getAccountId());
			parkingStatusDao.addNewParkingAndPayment(pi);
		}
		
		UserPaymentReport report = adminReportDao
			.getUserPaymentReport(testUser.getUserID(), reportStartDate, reportEndDate);
		assertNotNull(report);
		assertFalse(report.getPaymentEntries().isEmpty());
		assertEquals(testSize, report.getPaymentEntries().size());
		
		for (int i = 0; i < testSize; i++) {	
			UserPaymentEntry paymentEntry = report.getPaymentEntries().get(i);
			assertNotNull(paymentEntry);
			assertEquals(pi.getPaymentInfo().getAmountPaidCents(), paymentEntry.getAmountPaidCents());
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
		}
		
	 
	}
}
