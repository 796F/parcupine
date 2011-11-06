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
	public static double clientRate = 1.15;
	public static double parkingLocationRate = 2.26;
	public static double spaceRate = 3.37;
	
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
	
	private static String insertParkingLocationMain = "INSERT INTO ParkingLocation (client_id, Location_name) " + 
			"VALUE ((SELECT client_id FROM client WHERE name='" + clientNameMain + "'), '" + parkingLocationNameMain + "' )" ;
	private static String insertParkingLocationFake1 = "INSERT INTO ParkingLocation (client_id, Location_name) " + 
			"VALUE ((SELECT client_id FROM client WHERE name='" + fakeClient1 + "'), '" + fakeParkingLocation1 + "' )" ;
	private static String insertParkingLocaationFake2 = "INSERT INTO ParkingLocation (client_id, Location_name) " + 
			"VALUE ((SELECT client_id FROM client WHERE name='" + fakeClient1 + "'), '" + fakeParkingLocation2 + "' )" ;

	private static String insertGeolocationMain = "INSERT INTO Geolocation (location_id, Latitude, Longitude) " + 
	"VALUE ((SELECT location_id FROM ParkingLocation WHERE location_name='" + parkingLocationNameMain + "'), " + parkingLocationMainLatitude + "," + parkingLocationMainLongtitude + " )" ;
	private static String insertGeolocationFake1 = "INSERT INTO Geolocation (location_id, Latitude, Longitude) " + 
	"VALUE ((SELECT location_id FROM ParkingLocation WHERE location_name='" + fakeParkingLocation1 + "'), " + fakeParkingLocation1Latitude + "," + fakeParkingLocation1Longtitude + " )" ;
	private static String insertGeolocationFake2 = "INSERT INTO Geolocation (location_id, Latitude, Longitude) " + 
	"VALUE ((SELECT location_id FROM ParkingLocation WHERE location_name='" + fakeParkingLocation2 + "'), " + fakeParkingLocation2Latitude + "," + fakeParkingLocation2Longtitude + " )" ;
	
	
	private static String insertSpaceMain1 = "INSERT INTO ParkingSpace (Location_Id, Space_Name, Parking_Level) " + 
			"VALUE ((SELECT Location_id FROM ParkingLocation WHERE Location_name='" + parkingLocationNameMain +"'), "+ spaceNameMain +", 1)";
	private static String insertSpaceMain2 = "INSERT INTO ParkingSpace (Location_Id, Space_Name, Parking_Level) " + 
			"VALUE ((SELECT Location_id FROM ParkingLocation WHERE Location_name='" + parkingLocationNameMain +"'), "+ spaceNameMain2 +", 2)";
	private static String insertSpaceMain3 = "INSERT INTO ParkingSpace (Location_Id, Space_Name, Parking_Level) " + 
			"VALUE ((SELECT Location_id FROM ParkingLocation WHERE Location_name='" + parkingLocationNameMain +"'), "+ spaceNameMain3 +", 3)";
	private static String insertSpaceFake1 = "INSERT INTO ParkingSpace (Location_Id, Space_Name, Parking_Level) " + 
			"VALUE ((SELECT Location_id FROM ParkingLocation WHERE Location_name='" + fakeParkingLocation1 +"'), "+ "'Test_Fake_2'" +", 2)";
	private static String insertSpaceFake2 = "INSERT INTO ParkingSpace (Location_Id, Space_Name, Parking_Level) " + 
			"VALUE ((SELECT Location_id FROM ParkingLocation WHERE Location_name='" + fakeParkingLocation1 +"'), "+ "'Test_Fake_3'" +", 2)";
	private static String insertSpaceFake3 = "INSERT INTO ParkingSpace (Location_Id, Space_Name, Parking_Level) " + 
			"VALUE ((SELECT Location_id FROM ParkingLocation WHERE Location_name='" + fakeParkingLocation1 +"'), "+ "'Test_Fake_4'" +", 2)";
	private static String insertSpaceFake4 = "INSERT INTO ParkingSpace (Location_Id, Space_Name, Parking_Level) " + 
			"VALUE ((SELECT Location_id FROM ParkingLocation WHERE Location_name='" + fakeParkingLocation2 +"'), "+ "'Test_Fake_5'" +", 2)";
	private static String insertSpaceFake5 = "INSERT INTO ParkingSpace (Location_Id, Space_Name, Parking_Level) " + 
			"VALUE ((SELECT Location_id FROM ParkingLocation WHERE Location_name='" + fakeParkingLocation2 +"'), "+ "'Test_Fake_6'" +", 2)";
	private static String insertSpaceFake6 = "INSERT INTO ParkingSpace (Location_Id, Space_Name, Parking_Level) " + 
			"VALUE ((SELECT Location_id FROM ParkingLocation WHERE Location_name='" + fakeParkingLocation2 +"'), "+ "'Test_Fake_7'" +", 2)";
	
	private static String insertClientParkingRate = "INSERT INTO ClientDefaultRate (client_id, default_parking_rate, priority) " +
			"VALUE ((SELECT client_id FROM client WHERE name='" + clientNameMain + "'), " + clientRate + ", " +  -3 + ")"; 
	private static String insertLocationParkingRate = "INSERT INTO ClientDefaultRate (client_id, default_parking_rate, priority, Location_id) " +
			"VALUE ((SELECT client_id FROM client WHERE name='" + clientNameMain + "'), " + parkingLocationRate + ", " +  -20 + ", " + 
				"(SELECT Location_id FROM ParkingLocation WHERE Location_name='" + parkingLocationNameMain +"')" + ")";
	private static String insertSpaceParkingRate = "INSERT INTO ClientDefaultRate (client_id, default_parking_rate, priority, Location_id, space_id) " +
			"VALUE ((SELECT client_id FROM client WHERE name='" + clientNameMain + "'), " + spaceRate + ", " +  -100 + ", " + 
				"(SELECT Location_id FROM ParkingLocation WHERE Location_name='" + parkingLocationNameMain + "'), " +  
				"(SELECT space_id FROM ParkingSpace WHERE space_name='" + spaceNameMain +"')"+ ")";
	
	 
	private static String deleteSpaces = "DELETE FROM ParkingSpace WHERE Location_Id IN (" +
			"SELECT Location_id FROM ParkingLocation WHERE Location_name " +
				"IN ('" + parkingLocationNameMain + "', '" + fakeParkingLocation1 + "', '" + fakeParkingLocation2 + "')" +
			")";
	private static String deleteLocations = "DELETE FROM ParkingLocation WHERE Location_name IN ('" + parkingLocationNameMain + "', '" + fakeParkingLocation1 + "', '" + fakeParkingLocation2 + "')";
	private static String deleteClients = "DELETE FROM client WHERE name IN ('" + clientNameMain + "', '" + fakeClient1 + "', '" + fakeClient2 + "')";
	private static String deleteParkingRates = "DELETE FROM ClientDefaultRate WHERE priority < 0";
	private static String deleteAllPayment = "DELETE FROM Payment";
	private static String deleteAllParkingInstance = "DELETE FROM ParkingInstance";
	private static String deleteAllGeoLocations = "DELETE FROM Geolocation"; 
	
	public static String[] sqlInsertStmtList = new String[] { insertClientMain,
			insertClientFake1, insertClientFake2, insertParkingLocationMain,
			insertParkingLocationFake1, insertParkingLocaationFake2, insertSpaceMain1,
			insertSpaceMain2, insertSpaceMain3, insertSpaceFake1,
			insertSpaceFake2, insertSpaceFake3, insertSpaceFake4,
			insertSpaceFake5, insertSpaceFake6, insertClientParkingRate, 
			insertLocationParkingRate, insertSpaceParkingRate, 
			insertGeolocationMain, insertGeolocationFake1, insertGeolocationFake2}; 
	
	public static String[] sqlDeleteStmtList = new String[] {
		deleteAllGeoLocations, deleteAllPayment, deleteAllParkingInstance, deleteParkingRates, 
		deleteSpaces, deleteLocations, deleteClients
	};
}
