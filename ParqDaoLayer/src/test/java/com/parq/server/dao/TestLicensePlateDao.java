package com.parq.server.dao;

import java.util.List;

import com.parq.server.dao.model.object.LicensePlate;
import com.parq.server.dao.model.object.User;
import com.parq.server.dao.support.SupportScriptForDaoTesting;

import junit.framework.TestCase;

public class TestLicensePlateDao extends TestCase {

	private static final String TEST_PLATE_NUMBER = "123ABC";
	
	private LicensePlateDao licensePlateDao;
	private static long userId;

	@Override
	protected void setUp() throws Exception {
		licensePlateDao = new LicensePlateDao();
		
		if (userId <= 0) {
			SupportScriptForDaoTesting.insertMainTestDataSet();
			SupportScriptForDaoTesting.createUserTestData();
			userId = SupportScriptForDaoTesting.testUser.getUserID();
			assertTrue(userId > 0);
		}
	}
	
	public void testInvalidGetLicensePlateByUserIdInput() {
		List<LicensePlate> plates =  licensePlateDao.getLicensePlateByUserId(Integer.MAX_VALUE);
		assertNull(plates);
	}
	
	

	public void testInvalidGetUserByPlateNumber() {
		List<User> plateOwners = licensePlateDao.getUserByPlateNumber("-kjjh8897");
		assertNull(plateOwners);
	}
	
	public void testAddLicensePlateForUser() {
		LicensePlate newPlateInfo = new LicensePlate();
		newPlateInfo.setUserID(userId);
		newPlateInfo.setPlateNum(TEST_PLATE_NUMBER);
		newPlateInfo.setDefault(true);
		
		boolean success = licensePlateDao.addLicensePlateForUser(newPlateInfo);
		assertTrue(success);
		
		List<LicensePlate> plateList = licensePlateDao.getLicensePlateByUserId(userId);
		assertNotNull(plateList);
		assertTrue(plateList.size() == 1);
		assertTrue(plateList.get(0).getPlateNum().equals(TEST_PLATE_NUMBER));
		assertTrue(plateList.get(0).getUserID() == userId);
		
		List<User> users = licensePlateDao.getUserByPlateNumber(TEST_PLATE_NUMBER);
		assertNotNull(users);
		assertFalse(users.isEmpty());
		assertEquals(userId, users.get(0).getUserID());
	}
	
	public void testGetLicensePlateByUserId() {
		List<LicensePlate> plateList = licensePlateDao.getLicensePlateByUserId(userId);
		assertNotNull(plateList);
		assertTrue(plateList.size() == 1);
		assertTrue(plateList.get(0).getPlateNum().equals(TEST_PLATE_NUMBER));
		assertTrue(plateList.get(0).getUserID() == userId);
	}
	
	public void testGetUserByPlateNumber() {
		List<User> users = licensePlateDao.getUserByPlateNumber(TEST_PLATE_NUMBER);
		assertNotNull(users);
		assertFalse(users.isEmpty());
		assertEquals(userId, users.get(0).getUserID());
	}
	
	public void testDeletePlateNumber() {
		List<LicensePlate> plateList = licensePlateDao.getLicensePlateByUserId(userId);
		assertNotNull(plateList);
		
		for (LicensePlate lp : plateList) {
			boolean delSuccessful = licensePlateDao.deleteLicensePlateInfo(lp);
			assertTrue(delSuccessful);
		}
		plateList = licensePlateDao.getLicensePlateByUserId(userId);
		assertNull(plateList);
	}
}
