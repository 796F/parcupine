package com.parq.server.dao.support;


public final class SupportScriptForDaoTesting {

	private static boolean fakeDataInserted = false;

	public static void insertFakeData() {
		if (!fakeDataInserted){
			deleteFakeData();
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
			testDao.setGeoLocationForParkingLocation(parkingLocationNameMain, parkingLocationMainLatitude, parkingLocationMainLongtitude);
			testDao.setGeoLocationForParkingLocation(fakeParkingLocation1, fakeParkingLocation1Latitude, fakeParkingLocation1Longtitude);
			testDao.setGeoLocationForParkingLocation(fakeParkingLocation2, fakeParkingLocation2Latitude, fakeParkingLocation2Longtitude);

			// insert 10 test parking spaces
			testDao.insertParkingSpace(parkingLocationNameMain, spaceNameMain, "1");
			testDao.insertParkingSpace(parkingLocationNameMain, spaceNameMain2, "2");
			testDao.insertParkingSpace(parkingLocationNameMain, spaceNameMain3, "3");
			testDao.insertParkingSpace(fakeParkingLocation1, "Test_Fake_1", "3");
			testDao.insertParkingSpace(fakeParkingLocation1, "Test_Fake_2", "2");
			testDao.insertParkingSpace(fakeParkingLocation1, "Test_Fake_3", "2");			
			testDao.insertParkingSpace(fakeParkingLocation1, "Test_Fake_4", "2");
			testDao.insertParkingSpace(fakeParkingLocation2, "Test_Fake_5", "2");
			testDao.insertParkingSpace(fakeParkingLocation2, "Test_Fake_6", "2");
			testDao.insertParkingSpace(fakeParkingLocation2, "Test_Fake_7", "2");

			// insert location based parking rate
			testDao.setParkingLocationRate(parkingLocationRate, -20, parkingLocationNameMain, 5);

			// insert parking space based parking rate
			testDao.setParkingSpaceRate(spaceRate, -100, parkingLocationNameMain, spaceNameMain, 5);
			
			fakeDataInserted = true;
		}
	}
	
	protected static void deleteFakeData() {
		DaoForTestingPurposes testDao = new DaoForTestingPurposes();
		for (String deleteScript : SupportScriptForDaoTesting.sqlDeleteStmtList) {
			testDao.executeSqlStatement(deleteScript);
		}
		fakeDataInserted = false;
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
