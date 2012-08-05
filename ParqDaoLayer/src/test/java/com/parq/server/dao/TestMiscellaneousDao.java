package com.parq.server.dao;

import java.util.List;

import com.parq.server.dao.model.object.ParkingSpace;
import com.parq.server.dao.model.object.User;
import com.parq.server.dao.model.object.UserActionLog;
import com.parq.server.dao.model.object.UserSelfReporting;
import com.parq.server.dao.support.SupportScriptForDaoTesting;

import junit.framework.TestCase;

public class TestMiscellaneousDao extends TestCase {

	private MiscellaneousDao miscDao;

	@Override
	protected void setUp() throws Exception {
		miscDao = new MiscellaneousDao();
	}
	
	public void testGetNextCount() {
		long preValue = -1;
		long curValue = 0;
		for (int i = 0; i < 100; i++) {
			curValue = miscDao.getNextCount();
			assertTrue(curValue > preValue);
			preValue = curValue;
		}
	}
	
	public void testGetUserReportWithNoReport() {
		List<UserSelfReporting> report =  miscDao.getUserSelfReportingHistoryForUser(14);
		assertNotNull(report);
		assertTrue(report.isEmpty());
	}
	
	public void testInsertUserReport() {
		User user = new UserDao()
			.getUserByEmail(SupportScriptForDaoTesting.userEmail);
		ParkingSpace pSpace = new ParkingSpaceDao()
			.getParkingSpaceBySpaceIdentifier(
				SupportScriptForDaoTesting.parkingLocationNameMain, SupportScriptForDaoTesting.spaceNameMain);
		String parkingStatus = "PARKED";
		
		UserSelfReporting userReport = new UserSelfReporting();
		userReport.setUserId(user.getUserID());
		userReport.setSpaceId(pSpace.getSpaceId());
		userReport.setParkingSpaceStatus(parkingStatus);
		userReport.setScore1(1);
		userReport.setScore2(3);
		userReport.setScore3(5);
		userReport.setScore4(7);
		userReport.setScore5(11);
		userReport.setScore6(13);
		
		miscDao.insertUserSelfReporting(userReport);
		
		List<UserSelfReporting> reports = miscDao.getUserSelfReportingHistoryForUser(user.getUserID());
		assertNotNull(reports);
		assertTrue(reports.size() > 0);
		UserSelfReporting userReport2 = reports.get(0);
		
		assertTrue(userReport2.getReportId() > 0);
		assertNotNull(userReport2.getReportDateTime());
		System.out.println(userReport2.getReportDateTime());
		assertEquals(userReport.getUserId(), userReport2.getUserId());
		assertEquals(userReport.getSpaceId(), userReport2.getSpaceId());
		assertEquals(userReport.getParkingSpaceStatus(), userReport2.getParkingSpaceStatus());
		assertEquals(userReport.getScore1(), userReport2.getScore1());
		assertEquals(userReport.getScore2(), userReport2.getScore2());
		assertEquals(userReport.getScore3(), userReport2.getScore3());
		assertEquals(userReport.getScore4(), userReport2.getScore4());
		assertEquals(userReport.getScore5(), userReport2.getScore5());
		assertEquals(userReport.getScore6(), userReport2.getScore6());
	}
	
	public void testGetUserReport() {
		User user = new UserDao()
			.getUserByEmail(SupportScriptForDaoTesting.userEmail);
		ParkingSpace pSpace = new ParkingSpaceDao()
			.getParkingSpaceBySpaceIdentifier(
				SupportScriptForDaoTesting.parkingLocationNameMain, SupportScriptForDaoTesting.spaceNameMain);
		String parkingStatus = "EMPTY";
		
		for (int i = 10; i < 100; i+=7){
			UserSelfReporting userReport = new UserSelfReporting();
			userReport.setUserId(user.getUserID());
			userReport.setSpaceId(pSpace.getSpaceId());
			userReport.setParkingSpaceStatus(parkingStatus);
			userReport.setScore1(i+1);
			userReport.setScore2(i+2);
			userReport.setScore3(i+3);
			userReport.setScore4(i+4);
			userReport.setScore5(i+5);
			userReport.setScore6(i+6);
			miscDao.insertUserSelfReporting(userReport);
		}
		
		for (int i = 10; i < 100; i+=7){ 
			List<UserSelfReporting> reports = miscDao.getUserSelfReportingHistoryForUser(user.getUserID());
			assertNotNull(reports);
			assertTrue(reports.size() > 0);
			UserSelfReporting userReport2 = reports.get(0);
			
			assertTrue(userReport2.getReportId() > 0);
			assertNotNull(userReport2.getReportDateTime());
			System.out.println(userReport2.getReportDateTime());
			assertEquals(user.getUserID(), userReport2.getUserId());
			assertEquals(pSpace.getSpaceId(), userReport2.getSpaceId());
			assertEquals(parkingStatus, userReport2.getParkingSpaceStatus());
			assertTrue(userReport2.getScore6() - userReport2.getScore5() == 1);
			assertTrue(userReport2.getScore5() - userReport2.getScore4() == 1);
			assertTrue(userReport2.getScore4() - userReport2.getScore3() == 1);
			assertTrue(userReport2.getScore3() - userReport2.getScore2() == 1);
			assertTrue(userReport2.getScore2() - userReport2.getScore1() == 1);
		}
	}
	
	public void testInsertUserActionLogging() {
		User user = new UserDao()
			.getUserByEmail(SupportScriptForDaoTesting.userEmail);
		UserActionLog userActionLog = new UserActionLog();
		userActionLog.setLog("TEST LOGs");
		userActionLog.setUserId(user.getUserID());
		assertTrue(miscDao.insertUserActionLogging(userActionLog));
	}
}
