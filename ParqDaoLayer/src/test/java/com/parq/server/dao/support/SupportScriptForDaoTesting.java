package com.parq.server.dao.support;


public final class SupportScriptForDaoTesting {

	private static boolean fakeDataInserted = false;

	public static void insertFakeData() {
		if (!fakeDataInserted){
			deleteFakeData();
			DaoForTestingPurposes testDao = new DaoForTestingPurposes();
			for (String s : SupportScriptForDaoTesting.sqlInsertStmtList) {
				testDao.executeSqlStatement(s);
			}
			fakeDataInserted = true;
		}
	}
	
	protected static void deleteFakeData() {
		DaoForTestingPurposes testDao = new DaoForTestingPurposes();
		for (String s : SupportScriptForDaoTesting.sqlDeleteStmtList) {
			testDao.executeSqlStatement(s);
		}
		fakeDataInserted = false;
	}
	
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
	
	private static String insertClientMain = "INSERT INTO client (name, address, client_desc) " +
			"VALUES ('" + clientNameMain + "', 'test_address', 'testclient')";
	private static String insertClientFake1 = "INSERT INTO client (name, address, client_desc) " +
			"VALUES ('" + fakeClient1 + "', 'test_address', 'testclient')";
	private static String insertClientFake2 = "INSERT INTO client (name, address, client_desc) " +
			"VALUES ('" + fakeClient2 + "', 'test_address', 'testclient')";
	
	private static String insertParkingLocationMain = "INSERT INTO ParkingLocation (client_id, location_identifier) " + 
			"VALUE ((SELECT client_id FROM client WHERE name='" + clientNameMain + "'), '" + parkingLocationNameMain + "' )" ;
	private static String insertParkingLocationFake1 = "INSERT INTO ParkingLocation (client_id, location_identifier) " + 
			"VALUE ((SELECT client_id FROM client WHERE name='" + fakeClient1 + "'), '" + fakeParkingLocation1 + "' )" ;
	private static String insertParkingLocationFake2 = "INSERT INTO ParkingLocation (client_id, location_identifier) " + 
			"VALUE ((SELECT client_id FROM client WHERE name='" + fakeClient1 + "'), '" + fakeParkingLocation2 + "' )" ;

	private static String insertGeolocationMain = "INSERT INTO Geolocation (location_id, Latitude, Longitude) " + 
	"VALUE ((SELECT location_id FROM ParkingLocation WHERE location_identifier='" + parkingLocationNameMain + "'), " + parkingLocationMainLatitude + "," + parkingLocationMainLongtitude + " )" ;
	private static String insertGeolocationFake1 = "INSERT INTO Geolocation (location_id, Latitude, Longitude) " + 
	"VALUE ((SELECT location_id FROM ParkingLocation WHERE location_identifier='" + fakeParkingLocation1 + "'), " + fakeParkingLocation1Latitude + "," + fakeParkingLocation1Longtitude + " )" ;
	private static String insertGeolocationFake2 = "INSERT INTO Geolocation (location_id, Latitude, Longitude) " + 
	"VALUE ((SELECT location_id FROM ParkingLocation WHERE location_identifier='" + fakeParkingLocation2 + "'), " + fakeParkingLocation2Latitude + "," + fakeParkingLocation2Longtitude + " )" ;
	
	
	private static String insertSpaceMain1 = "INSERT INTO ParkingSpace (Location_Id, Space_Identifier, Parking_Level) " + 
			"VALUE ((SELECT Location_id FROM ParkingLocation WHERE location_identifier='" + parkingLocationNameMain +"'), "+ spaceNameMain +", 1)";
	private static String insertSpaceMain2 = "INSERT INTO ParkingSpace (Location_Id, Space_Identifier, Parking_Level) " + 
			"VALUE ((SELECT Location_id FROM ParkingLocation WHERE location_identifier='" + parkingLocationNameMain +"'), "+ spaceNameMain2 +", 2)";
	private static String insertSpaceMain3 = "INSERT INTO ParkingSpace (Location_Id, Space_Identifier, Parking_Level) " + 
			"VALUE ((SELECT Location_id FROM ParkingLocation WHERE location_identifier='" + parkingLocationNameMain +"'), "+ spaceNameMain3 +", 3)";
	private static String insertSpaceFake1 = "INSERT INTO ParkingSpace (Location_Id, Space_Identifier, Parking_Level) " + 
			"VALUE ((SELECT Location_id FROM ParkingLocation WHERE location_identifier='" + fakeParkingLocation1 +"'), "+ "'Test_Fake_2'" +", 2)";
	private static String insertSpaceFake2 = "INSERT INTO ParkingSpace (Location_Id, Space_Identifier, Parking_Level) " + 
			"VALUE ((SELECT Location_id FROM ParkingLocation WHERE location_identifier='" + fakeParkingLocation1 +"'), "+ "'Test_Fake_3'" +", 2)";
	private static String insertSpaceFake3 = "INSERT INTO ParkingSpace (Location_Id, Space_Identifier, Parking_Level) " + 
			"VALUE ((SELECT Location_id FROM ParkingLocation WHERE location_identifier='" + fakeParkingLocation1 +"'), "+ "'Test_Fake_4'" +", 2)";
	private static String insertSpaceFake4 = "INSERT INTO ParkingSpace (Location_Id, Space_Identifier, Parking_Level) " + 
			"VALUE ((SELECT Location_id FROM ParkingLocation WHERE location_identifier='" + fakeParkingLocation2 +"'), "+ "'Test_Fake_5'" +", 2)";
	private static String insertSpaceFake5 = "INSERT INTO ParkingSpace (Location_Id, Space_Identifier, Parking_Level) " + 
			"VALUE ((SELECT Location_id FROM ParkingLocation WHERE location_identifier='" + fakeParkingLocation2 +"'), "+ "'Test_Fake_6'" +", 2)";
	private static String insertSpaceFake6 = "INSERT INTO ParkingSpace (Location_Id, Space_Identifier, Parking_Level) " + 
			"VALUE ((SELECT Location_id FROM ParkingLocation WHERE location_identifier='" + fakeParkingLocation2 +"'), "+ "'Test_Fake_7'" +", 2)";
	
	private static String insertLocationParkingRate = "INSERT INTO ParkingRate (parking_rate_cents, priority, Location_id, time_increment_mins) " +
			"VALUE (" + parkingLocationRate + ", " +  -20 + ", " + 
				"(SELECT Location_id FROM ParkingLocation WHERE location_identifier='" + parkingLocationNameMain +"')" + ", 5)";
	private static String insertSpaceParkingRate = "INSERT INTO ParkingRate (parking_rate_cents, priority, Location_id, space_id, time_increment_mins) " +
			"VALUE (" + spaceRate + ", " +  -100 + ", " + 
				"(SELECT Location_id FROM ParkingLocation WHERE location_identifier='" + parkingLocationNameMain + "'), " +  
				"(SELECT space_id FROM ParkingSpace WHERE Space_Identifier='" + spaceNameMain +"')"+ ", 5)";
	
	 
	private static String deleteSpaces = "DELETE FROM ParkingSpace WHERE Location_Id IN (" +
			"SELECT Location_id FROM ParkingLocation WHERE location_identifier " +
				"IN ('" + parkingLocationNameMain + "', '" + fakeParkingLocation1 + "', '" + fakeParkingLocation2 + "')" +
			")";
	private static String deleteLocations = "DELETE FROM ParkingLocation WHERE location_identifier IN ('" + parkingLocationNameMain + "', '" + fakeParkingLocation1 + "', '" + fakeParkingLocation2 + "')";
	private static String deleteClients = "DELETE FROM client WHERE name IN ('" + clientNameMain + "', '" + fakeClient1 + "', '" + fakeClient2 + "')";
	private static String deleteParkingRates = "DELETE FROM ParkingRate WHERE priority < 0";
	private static String deleteAllPayment = "DELETE FROM Payment";
	private static String deleteAllParkingInstance = "DELETE FROM ParkingInstance";
	private static String deleteAllGeoLocations = "DELETE FROM Geolocation"; 
	
	public static String[] sqlInsertStmtList = new String[] { insertClientMain,
			insertClientFake1, insertClientFake2, insertParkingLocationMain,
			insertParkingLocationFake1, insertParkingLocationFake2, insertSpaceMain1,
			insertSpaceMain2, insertSpaceMain3, insertSpaceFake1,
			insertSpaceFake2, insertSpaceFake3, insertSpaceFake4,
			insertSpaceFake5, insertSpaceFake6, 
			insertLocationParkingRate, insertSpaceParkingRate, 
			insertGeolocationMain, insertGeolocationFake1, insertGeolocationFake2}; 
	
	public static String[] sqlDeleteStmtList = new String[] {
		deleteAllGeoLocations, deleteAllPayment, deleteAllParkingInstance, deleteParkingRates, 
		deleteSpaces, deleteLocations, deleteClients
	};
}
