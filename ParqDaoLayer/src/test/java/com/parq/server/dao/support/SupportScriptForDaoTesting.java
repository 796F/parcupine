package com.parq.server.dao.support;


public final class SupportScriptForDaoTesting {

	private static boolean fakeDataInserted = false;

	public static void insertFakeData() {
		if (!fakeDataInserted){
			DaoForTestingPurposes testDao = new DaoForTestingPurposes();
			for (String s : SupportScriptForDaoTesting.sqlInsertStmtList) {
				testDao.executeSqlStatement(s);
			}
			fakeDataInserted = true;
		}
	}
	
	public static void deleteFakeData() {
		DaoForTestingPurposes testDao = new DaoForTestingPurposes();
		for (String s : SupportScriptForDaoTesting.sqlDeleteStmtList) {
			testDao.executeSqlStatement(s);
		}
		fakeDataInserted = false;
	}
	
	public static String clientNameMain = "AU";
	public static String buildingNameMain = "main_lot";
	public static String spaceNameMain = "1412";
	public static double clientRate = 1.15;
	public static double buildingRate = 2.26;
	public static double spaceRate = 3.37;
	
	// Scripts to create the new parking rate for testing 
	private static String fakeClient1 = "fakeClient1";
	private static String fakeClient2 = "fakeClient2";
	
	private static String fakeBuilding1 = "fakeBuilding1";
	private static String fakeBuilding2 = "fakeBuilding2";
	
	public static String spaceNameMain2 = "2210";
	public static String spaceNameMain3 = "3315";
	
	private static String insertClientMain = "INSERT INTO client (name, address, client_desc) " +
			"VALUES ('" + clientNameMain + "', 'test_address', 'testclient')";
	private static String insertClientFake1 = "INSERT INTO client (name, address, client_desc) " +
			"VALUES ('" + fakeClient1 + "', 'test_address', 'testclient')";
	private static String insertClientFake2 = "INSERT INTO client (name, address, client_desc) " +
			"VALUES ('" + fakeClient2 + "', 'test_address', 'testclient')";
	
	private static String insertBuildingMain = "INSERT INTO ParkingBuilding (client_id, building_name) " + 
			"VALUE ((SELECT client_id FROM client WHERE name='" + clientNameMain + "'), '" + buildingNameMain + "' )" ;
	private static String insertBuildingFake1 = "INSERT INTO ParkingBuilding (client_id, building_name) " + 
			"VALUE ((SELECT client_id FROM client WHERE name='" + fakeClient1 + "'), '" + fakeBuilding1 + "' )" ;
	private static String insertBuildingFake2 = "INSERT INTO ParkingBuilding (client_id, building_name) " + 
			"VALUE ((SELECT client_id FROM client WHERE name='" + fakeClient1 + "'), '" + fakeBuilding2 + "' )" ;

	private static String insertSpaceMain1 = "INSERT INTO ParkingSpace (Building_Id, Space_Name, Parking_Level) " + 
			"VALUE ((SELECT building_id FROM ParkingBuilding WHERE building_name='" + buildingNameMain +"'), "+ spaceNameMain +", 1)";
	private static String insertSpaceMain2 = "INSERT INTO ParkingSpace (Building_Id, Space_Name, Parking_Level) " + 
			"VALUE ((SELECT building_id FROM ParkingBuilding WHERE building_name='" + buildingNameMain +"'), "+ spaceNameMain2 +", 2)";
	private static String insertSpaceMain3 = "INSERT INTO ParkingSpace (Building_Id, Space_Name, Parking_Level) " + 
			"VALUE ((SELECT building_id FROM ParkingBuilding WHERE building_name='" + buildingNameMain +"'), "+ spaceNameMain3 +", 3)";
	private static String insertSpaceFake1 = "INSERT INTO ParkingSpace (Building_Id, Space_Name, Parking_Level) " + 
			"VALUE ((SELECT building_id FROM ParkingBuilding WHERE building_name='" + fakeBuilding1 +"'), "+ "'Test_Fake_2'" +", 2)";
	private static String insertSpaceFake2 = "INSERT INTO ParkingSpace (Building_Id, Space_Name, Parking_Level) " + 
			"VALUE ((SELECT building_id FROM ParkingBuilding WHERE building_name='" + fakeBuilding1 +"'), "+ "'Test_Fake_3'" +", 2)";
	private static String insertSpaceFake3 = "INSERT INTO ParkingSpace (Building_Id, Space_Name, Parking_Level) " + 
			"VALUE ((SELECT building_id FROM ParkingBuilding WHERE building_name='" + fakeBuilding1 +"'), "+ "'Test_Fake_4'" +", 2)";
	private static String insertSpaceFake4 = "INSERT INTO ParkingSpace (Building_Id, Space_Name, Parking_Level) " + 
			"VALUE ((SELECT building_id FROM ParkingBuilding WHERE building_name='" + fakeBuilding2 +"'), "+ "'Test_Fake_5'" +", 2)";
	private static String insertSpaceFake5 = "INSERT INTO ParkingSpace (Building_Id, Space_Name, Parking_Level) " + 
			"VALUE ((SELECT building_id FROM ParkingBuilding WHERE building_name='" + fakeBuilding2 +"'), "+ "'Test_Fake_6'" +", 2)";
	private static String insertSpaceFake6 = "INSERT INTO ParkingSpace (Building_Id, Space_Name, Parking_Level) " + 
			"VALUE ((SELECT building_id FROM ParkingBuilding WHERE building_name='" + fakeBuilding2 +"'), "+ "'Test_Fake_7'" +", 2)";
	
	private static String insertClientParkingRate = "INSERT INTO ClientDefaultRate (client_id, default_parking_rate, priority) " +
			"VALUE ((SELECT client_id FROM client WHERE name='" + clientNameMain + "'), " + clientRate + ", " +  -3 + ")"; 
	private static String insertBuildingParkingRate = "INSERT INTO ClientDefaultRate (client_id, default_parking_rate, priority, building_id) " +
			"VALUE ((SELECT client_id FROM client WHERE name='" + clientNameMain + "'), " + buildingRate + ", " +  -20 + ", " + 
				"(SELECT building_id FROM ParkingBuilding WHERE building_name='" + buildingNameMain +"')" + ")";
	private static String insertSpaceParkingRate = "INSERT INTO ClientDefaultRate (client_id, default_parking_rate, priority, building_id, space_id) " +
			"VALUE ((SELECT client_id FROM client WHERE name='" + clientNameMain + "'), " + spaceRate + ", " +  -100 + ", " + 
				"(SELECT building_id FROM ParkingBuilding WHERE building_name='" + buildingNameMain + "'), " +  
				"(SELECT space_id FROM ParkingSpace WHERE space_name='" + spaceNameMain +"')"+ ")";
	
	 
	private static String deleteSpaces = "DELETE FROM ParkingSpace WHERE Building_Id IN (" +
			"SELECT building_id FROM ParkingBuilding WHERE building_name " +
				"IN ('" + buildingNameMain + "', '" + fakeBuilding1 + "', '" + fakeBuilding2 + "')" +
			")";
	private static String deleteBuildings = "DELETE FROM ParkingBuilding WHERE building_name IN ('" + buildingNameMain + "', '" + fakeBuilding1 + "', '" + fakeBuilding2 + "')";
	private static String deleteClients = "DELETE FROM client WHERE name IN ('" + clientNameMain + "', '" + fakeClient1 + "', '" + fakeClient2 + "')";
	private static String deleteParkingRates = "DELETE FROM ClientDefaultRate WHERE priority < 0";
	private static String deleteAllPayment = "DELETE FROM Payment";
	private static String deleteAllParkingInstance = "DELETE FROM ParkingInstance";
	
	public static String[] sqlInsertStmtList = new String[] { insertClientMain,
			insertClientFake1, insertClientFake2, insertBuildingMain,
			insertBuildingFake1, insertBuildingFake2, insertSpaceMain1,
			insertSpaceMain2, insertSpaceMain3, insertSpaceFake1,
			insertSpaceFake2, insertSpaceFake3, insertSpaceFake4,
			insertSpaceFake5, insertSpaceFake6, insertClientParkingRate, 
			insertBuildingParkingRate, insertSpaceParkingRate}; 
	
	public static String[] sqlDeleteStmtList = new String[] {
		deleteAllPayment, deleteAllParkingInstance, deleteParkingRates, 
		deleteSpaces, deleteBuildings, deleteClients
	};
}
