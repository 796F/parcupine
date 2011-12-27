package com.parq.server.dao.support;

import java.util.Date;
import java.util.List;

import com.parq.server.dao.ClientDao;
import com.parq.server.dao.PaymentAccountDao;
import com.parq.server.dao.UserDao;
import com.parq.server.dao.model.object.ParkingInstance;
import com.parq.server.dao.model.object.ParkingLocation;
import com.parq.server.dao.model.object.Payment;
import com.parq.server.dao.model.object.PaymentAccount;
import com.parq.server.dao.model.object.User;
import com.parq.server.dao.model.object.Payment.PaymentType;


/**
 * @author GZ
 *
 */
public final class SupportScriptForDaoTesting {

	public static final String adminName = "Test_Admin_1";
	public static final String adminPassword = "Password123";
	public static final String adminEMail = "TestAdmin12@testCorp.com";
	public static final String adminRoleName = "TestRole";
	public static final String adminClientName = "TestClientName";
	
	public static final String testCCStub = "1234";
	public static final String testCustomerId = "CUS_345";
	public static final String testPaymentMethodId = "PAY_456169";
	
	public static final String userEmail = "TestUser@PaymentAccount.test";
	public static final String userPassWord = "TestPassword";
	public static final String userPhoneNum = "123-555-7890";
	
	public static User testUser;
	public static ParkingInstance testParkingInstance;
	public static ParkingInstance testParkingInstanceRefil;
	public static List<ParkingLocation> testParkingLocationList;
	public static PaymentAccount testPaymentAccount;
	
	private static boolean mainDataSetInserted = false;

	public static void insertMainTestDataSet() {
		if (!mainDataSetInserted){
			deleteMainTestDataSet();
			ParqMockObjectCreationDao testDao = new ParqMockObjectCreationDao();

			// create 3 new test clients
			testDao.createNewClient(clientNameMain, "test_address", "testclient");
			testDao.createNewClient(fakeClient1, "test_address", "testclient");
			testDao.createNewClient(fakeClient2, "test_address", "testclient");

			// insert 3 new parking locations
			testDao.createNewParkingLocation(clientNameMain, parkingLocationNameMain, "TestParkingLocation");
			testDao.createNewParkingLocation(fakeClient1, fakeParkingLocation1, "TestParkingLocation");
			testDao.createNewParkingLocation(fakeClient1, fakeParkingLocation2, "TestParkingLocation");

			// insert Geo-location coordinates for the 3 parking location
			testDao.setGeoCoordinateForParkingLocation(parkingLocationNameMain, parkingLocationMainLatitude, parkingLocationMainLongtitude);
			testDao.setGeoCoordinateForParkingLocation(fakeParkingLocation1, fakeParkingLocation1Latitude, fakeParkingLocation1Longtitude);
			testDao.setGeoCoordinateForParkingLocation(fakeParkingLocation2, fakeParkingLocation2Latitude, fakeParkingLocation2Longtitude);

			// insert 10 test parking spaces
			testDao.insertParkingSpace(parkingLocationNameMain, spaceNameMain, "1", "space name 1");
			testDao.insertParkingSpace(parkingLocationNameMain, spaceNameMain2, "2", "space name 2");
			testDao.insertParkingSpace(parkingLocationNameMain, spaceNameMain3, "3", "space name 3");
			testDao.insertParkingSpace(fakeParkingLocation1, "Test_Fake_1", "3", "space name 4");
			testDao.insertParkingSpace(fakeParkingLocation1, "Test_Fake_2", "2", "space name 5");
			testDao.insertParkingSpace(fakeParkingLocation1, "Test_Fake_3", "2", "space name 6");			
			testDao.insertParkingSpace(fakeParkingLocation1, "Test_Fake_4", "2", "space name 7");
			testDao.insertParkingSpace(fakeParkingLocation2, "Test_Fake_5", "2", "space name 8");
			testDao.insertParkingSpace(fakeParkingLocation2, "Test_Fake_6", "2", "space name 9");
			testDao.insertParkingSpace(fakeParkingLocation2, "Test_Fake_7", "2", "space name 10");

			// insert location based parking rate
			testDao.setParkingLocationRate(parkingLocationRate, parkingLocationParkingRatePriority, 
					parkingLocationNameMain, 5);

			// insert parking space based parking rate
			testDao.setParkingSpaceRate(spaceRate, parkingSpaceParkingRatePriority, 
					parkingLocationNameMain, spaceNameMain, 5);
			
			mainDataSetInserted = true;
		}
	}
	
	protected static void deleteMainTestDataSet() {
		DaoForTestingPurposes testDao = new DaoForTestingPurposes();
		for (String deleteScript : SupportScriptForDaoTesting.sqlDeleteStmtList) {
			testDao.executeSqlStatement(deleteScript);
		}
		mainDataSetInserted = false;
	}
	
	public static void createAdminUserTestData() {
		DaoForTestingPurposes testDao = new DaoForTestingPurposes();
		
		testDao.executeSqlStatement("DELETE FROM adminclientrelationship WHERE client_id = (SELECT client_id FROM client WHERE name='" + adminClientName + "')");
		testDao.executeSqlStatement("DELETE FROM adminrole WHERE role_name = '" + adminRoleName + "'");
		testDao.executeSqlStatement("DELETE FROM admin WHERE username = '" + adminName + "'");
		testDao.executeSqlStatement("DELETE FROM client WHERE name='" + adminClientName + "'");
		
		String sqlInsertAdmin = "INSERT INTO admin (username, password, email) " +
			"VALUES ('" + adminName + "', '" + adminPassword +"', '" + adminEMail + "')";
		String sqlInsertRole =  "INSERT INTO adminrole (role_name) " +
			"VALUES ('" + adminRoleName + "')";
		String sqlInsertClient = "INSERT INTO client (name) VALUES ('" + adminClientName + "')";
		String sqlInsertAdminClientRelationship = 
			"INSERT INTO adminclientrelationship (admin_id, client_id, adminrole_id) " +
			"VALUES (" +
			"(SELECT admin_id FROM admin WHERE username = '" + adminName  + "'), " +
			"(SELECT client_id FROM client WHERE name = '" + adminClientName + "'), " +
			"(SELECT adminrole_id FROM adminrole WHERE role_name = '" + adminRoleName + "'))";
		
		testDao.executeSqlStatement(sqlInsertAdmin);
		testDao.executeSqlStatement(sqlInsertRole);
		testDao.executeSqlStatement(sqlInsertClient);
		testDao.executeSqlStatement(sqlInsertAdminClientRelationship);
	}
	
	public static void createUserTestData() {
		deleteUserTestData();
		UserDao userDao = new UserDao();
		
		// Create the test user to test the app with.
		User newUser = new User();
		newUser.setPassword(userPassWord);
		newUser.setEmail(userEmail);
		newUser.setPhoneNumber(userPhoneNum);
		// boolean userCreationSuccessful = 
		userDao.createNewUser(newUser);
		
		testUser = userDao.getUserByEmail(userEmail);
	}
	
	protected static void deleteUserTestData() {
		UserDao userDao = new UserDao();
		testUser = userDao.getUserByEmail(userEmail);
		
		if (testUser != null) {
			// boolean deleteUserSuccessful = 
			userDao.deleteUserById(testUser.getUserID());
			testUser = null;
		}
	}
	
	public static void createUserPaymentAccount() {
		testPaymentAccount = new PaymentAccount();
		testPaymentAccount.setCcStub(testCCStub);
		testPaymentAccount.setCustomerId(testCustomerId);
		testPaymentAccount.setDefaultPaymentMethod(true);
		testPaymentAccount.setPaymentMethodId(testPaymentMethodId);
		testPaymentAccount.setUserId(testUser.getUserID());
		
		PaymentAccountDao payAccDao = new PaymentAccountDao();
		// boolean paCreationSuccessful = 
		payAccDao.createNewPaymentMethod(testPaymentAccount);
		testPaymentAccount = payAccDao.getAllPaymentMethodForUser(testUser.getUserID()).get(0);
	}
	
	public static void setupParkingPaymentData(){
		insertMainTestDataSet();
		
		if (testUser == null) {
			createUserTestData();
		}
		
		ClientDao clientDao = new ClientDao();
		testParkingLocationList = clientDao.getParkingLocationsAndSpacesByClientId(
				clientDao.getClientByName(SupportScriptForDaoTesting.clientNameMain).getId());
		
		testParkingInstance = new ParkingInstance();
		testParkingInstance.setPaidParking(true);
		testParkingInstance.setParkingBeganTime(new Date(System.currentTimeMillis()));
		testParkingInstance.setParkingEndTime(new Date(System.currentTimeMillis() + 3600000));
		testParkingInstance.setSpaceId(testParkingLocationList.get(0).getSpaces().get(0).getSpaceId());
		testParkingInstance.setUserId(testUser.getUserID());
		
		Payment paymentInfo = new Payment();
		paymentInfo.setAmountPaidCents(1005);
		paymentInfo.setPaymentDateTime(new Date(System.currentTimeMillis()));
		paymentInfo.setPaymentRefNumber("Test_Payment_Ref_Num_1");
		paymentInfo.setPaymentType(PaymentType.CreditCard);
		testParkingInstance.setPaymentInfo(paymentInfo);
		
		testParkingInstanceRefil = new ParkingInstance();
		testParkingInstanceRefil.setPaidParking(true);
		testParkingInstanceRefil.setParkingBeganTime(new Date(System.currentTimeMillis()));
		testParkingInstanceRefil.setParkingEndTime(new Date(System.currentTimeMillis() + 7200000));
		testParkingInstanceRefil.setSpaceId(testParkingLocationList.get(0).getSpaces().get(0).getSpaceId());
		testParkingInstanceRefil.setUserId(testUser.getUserID());
		
		Payment paymentInfo2 = new Payment();
		paymentInfo2.setAmountPaidCents(1250);
		paymentInfo2.setPaymentDateTime(new Date(System.currentTimeMillis()));
		paymentInfo2.setPaymentRefNumber("Test_Payment_Ref_Num_1");
		paymentInfo2.setPaymentType(PaymentType.CreditCard);
		testParkingInstanceRefil.setPaymentInfo(paymentInfo2);
	}
	
	
	// ----------------------------------------------------------------
	// UnitTest create statement values
	// ----------------------------------------------------------------
	public static String clientNameMain = "AU";
	public static String parkingLocationNameMain = "main_lot";
	public static String spaceNameMain = "1412";
	public static int clientRate = 115;
	public static int parkingLocationRate = 226;
	public static int spaceRate = 337;
	
	public static double parkingLocationMainLatitude = 0.00;
	public static double parkingLocationMainLongtitude = 0.00;
	
	// Scripts to create the new parking rate for testing 
	private static String fakeClient1 = "fakeClient1";
	private static String fakeClient2 = "fakeClient2";
	
	private static String fakeParkingLocation1 = "fakeLocation1";
	private static String fakeParkingLocation2 = "fakeLocation2";
	
	public static double fakeParkingLocation1Latitude = 3.00;
	public static double fakeParkingLocation1Longtitude = 3.00;
	
	public static double fakeParkingLocation2Latitude = 0.50;
	public static double fakeParkingLocation2Longtitude = 5.00;
	
	public static String spaceNameMain2 = "2210";
	public static String spaceNameMain3 = "3315";
	
	public static int parkingLocationParkingRatePriority = -100;
	public static int parkingSpaceParkingRatePriority = -20;
	

	// ------------------------------------------------------------------------
	// UnitTest delete statement to undo test data created during the unit test
	// ------------------------------------------------------------------------
	private static String deleteSpaces = 
		"DELETE FROM parkingspace WHERE location_id IN (" +
		" SELECT location_id FROM parkinglocation WHERE location_identifier " +
		" IN ('" + parkingLocationNameMain + "', '" + fakeParkingLocation1 + "', '" + fakeParkingLocation2 + "'" + 
		" ))";
	private static String deleteLocations = 
		"DELETE FROM parkinglocation WHERE location_identifier " +
		" IN ('" + parkingLocationNameMain + "', '" + fakeParkingLocation1 + "', '" + fakeParkingLocation2 + "')";
	private static String deleteClients = 
		"DELETE FROM client WHERE name " +
		" IN ('" + clientNameMain + "', '" + fakeClient1 + "', '" + fakeClient2 + "')";
	private static String deleteParkingRates = 
		"DELETE FROM parkingrate WHERE priority < 0";
	private static String deletePayments = 
		"DELETE FROM payment WHERE parkinginst_id " +
		" IN (SELECT parkinginst_id FROM parkinginstance WHERE space_id " +
		" IN (SELECT space_id FROM parkingspace WHERE location_id " +
		" IN (SELECT location_id FROM parkinglocation WHERE location_identifier " +
		" IN ('" + parkingLocationNameMain + "', '" + fakeParkingLocation1 + "', '" + fakeParkingLocation2 + "'" + 
		" ))))";
	private static String deleteParkingInstances = 
		"DELETE FROM parkinginstance WHERE space_id " +
		" IN (SELECT space_id FROM parkingspace WHERE location_id " +
		" IN (SELECT location_id FROM parkinglocation WHERE location_identifier " +
		" IN ('" + parkingLocationNameMain + "', '" + fakeParkingLocation1 + "', '" + fakeParkingLocation2 + "'" + 
		" )))";
	
	private static String deleteGeoLocations = 
		"DELETE FROM geolocation WHERE location_id " +
		" IN (SELECT location_id FROM parkinglocation WHERE location_identifier " +
		" IN ('" + parkingLocationNameMain + "', '" + fakeParkingLocation1 + "', '" + 
			fakeParkingLocation2 + "'))";

	
	public static String[] sqlDeleteStmtList = new String[] {
		deleteGeoLocations, deletePayments, deleteParkingInstances, deleteParkingRates, 
		deleteSpaces, deleteLocations, deleteClients
	};
}
