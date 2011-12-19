package com.parq.server.dao.support;

import java.util.Date;
import java.util.List;

import com.parq.server.dao.ClientDao;
import com.parq.server.dao.ParkingStatusDao;
import com.parq.server.dao.PaymentAccountDao;
import com.parq.server.dao.UserDao;
import com.parq.server.dao.model.object.ParkingInstance;
import com.parq.server.dao.model.object.ParkingLocation;
import com.parq.server.dao.model.object.Payment;
import com.parq.server.dao.model.object.PaymentAccount;
import com.parq.server.dao.model.object.User;
import com.parq.server.dao.model.object.Payment.PaymentType;

import junit.framework.TestCase;

public class ParqUnitTestParent extends TestCase {

	protected static final String adminName = "Test_Admin_1";
	protected static final String password = "Password123";
	protected static final String eMail = "TestAdmin12@testCorp.com";
	protected static final String roleName = "TestRole";
	protected static final String clientName = "TestClientName";
	
	protected static final String testCCStub = "1234";
	protected static final String testCustomerId = "CUS_345";
	protected static final String testPaymentMethodId = "PAY_456169";
	
	protected static final String testUserEmail = "TestUser@PaymentAccount.test";
	protected static final String testUserPW = "TestPassword";
	protected static final String testUserPhone = "123-555-7890";
	
	protected UserDao userDao;
	protected User testUser;
	
	protected ParkingStatusDao statusDao;
	protected ParkingInstance pi;
	protected ParkingInstance piRefil;
	protected List<ParkingLocation> buildingList;
	
	protected PaymentAccountDao payAccDao;
	protected PaymentAccount testPaymentAccount;
	
	protected void createAdminUserTestData() {
		DaoForTestingPurposes testDao = new DaoForTestingPurposes();
		
		testDao.executeSqlStatement("DELETE FROM adminclientrelationship WHERE client_id = (SELECT client_id FROM client WHERE name='" + clientName + "')");
		testDao.executeSqlStatement("DELETE FROM adminrole WHERE role_name = '" + roleName + "'");
		testDao.executeSqlStatement("DELETE FROM admin WHERE username = '" + adminName + "'");
		testDao.executeSqlStatement("DELETE FROM client WHERE name='" + clientName + "'");
		
		String sqlInsertAdmin = "INSERT INTO admin (username, password, email) " +
			"VALUES ('" + adminName + "', '" + password +"', '" + eMail + "')";
		String sqlInsertRole =  "INSERT INTO adminrole (role_name) " +
			"VALUES ('" + roleName + "')";
		String sqlInsertClient = "INSERT INTO client (name) VALUES ('" + clientName + "')";
		String sqlInsertAdminClientRelationship = 
			"INSERT INTO adminclientrelationship (admin_id, client_id, adminrole_id) " +
			"VALUES (" +
			"(SELECT admin_id FROM admin WHERE username = '" + adminName  + "'), " +
			"(SELECT client_id FROM client WHERE name = '" + clientName + "'), " +
			"(SELECT adminrole_id FROM adminrole WHERE role_name = '" + roleName + "'))";
		
		testDao.executeSqlStatement(sqlInsertAdmin);
		testDao.executeSqlStatement(sqlInsertRole);
		testDao.executeSqlStatement(sqlInsertClient);
		testDao.executeSqlStatement(sqlInsertAdminClientRelationship);
	}
	
	protected void createUserTestData() {
		userDao = new UserDao();
		
		// Create the test user to test the app with.
		User newUser = new User();
		newUser.setPassword(testUserPW);
		newUser.setEmail(testUserEmail);
		newUser.setPhoneNumber(testUserPhone);
		boolean userCreationSuccessful = userDao.createNewUser(newUser);
		assertTrue(userCreationSuccessful);
		
		testUser = userDao.getUserByEmail(testUserEmail);
	}
	
	protected void deleteUserTestData() {
		userDao = new UserDao();
		testUser = userDao.getUserByEmail(testUserEmail);
		
		if (testUser != null) {
			boolean deleteUserSuccessful = userDao.deleteUserById(testUser.getUserID());
			assertTrue(deleteUserSuccessful);
			testUser = null;
		}
	}
	
	protected void createUserPaymentAccount() {
		testPaymentAccount = new PaymentAccount();
		testPaymentAccount.setCcStub(testCCStub);
		testPaymentAccount.setCustomerId(testCustomerId);
		testPaymentAccount.setDefaultPaymentMethod(true);
		testPaymentAccount.setPaymentMethodId(testPaymentMethodId);
		testPaymentAccount.setUserId(testUser.getUserID());
		
		payAccDao = new PaymentAccountDao();
		boolean paCreationSuccessful = payAccDao.createNewPaymentMethod(testPaymentAccount);
		testPaymentAccount = payAccDao.getAllPaymentMethodForUser(testUser.getUserID()).get(0);
		assertTrue(paCreationSuccessful);
	}
	
	protected void setupParkingPaymentData(){
		SupportScriptForDaoTesting.insertFakeData();
		
		statusDao = new ParkingStatusDao();
		if (testUser == null) {
			createUserTestData();
		}
		
		ClientDao clientDao = new ClientDao();
		buildingList = clientDao.getParkingLocationsAndSpacesByClientId(
				clientDao.getClientByName(SupportScriptForDaoTesting.clientNameMain).getId());
		
		pi = new ParkingInstance();
		pi.setPaidParking(true);
		pi.setParkingBeganTime(new Date(System.currentTimeMillis()));
		pi.setParkingEndTime(new Date(System.currentTimeMillis() + 3600000));
		pi.setSpaceId(buildingList.get(0).getSpaces().get(0).getSpaceId());
		pi.setUserId(testUser.getUserID());
		
		Payment paymentInfo = new Payment();
		paymentInfo.setAmountPaidCents(1005);
		paymentInfo.setPaymentDateTime(new Date(System.currentTimeMillis()));
		paymentInfo.setPaymentRefNumber("Test_Payment_Ref_Num_1");
		paymentInfo.setPaymentType(PaymentType.CreditCard);
		pi.setPaymentInfo(paymentInfo);
		
		piRefil = new ParkingInstance();
		piRefil.setPaidParking(true);
		piRefil.setParkingBeganTime(new Date(System.currentTimeMillis()));
		piRefil.setParkingEndTime(new Date(System.currentTimeMillis() + 7200000));
		piRefil.setSpaceId(buildingList.get(0).getSpaces().get(0).getSpaceId());
		piRefil.setUserId(testUser.getUserID());
		
		Payment paymentInfo2 = new Payment();
		paymentInfo2.setAmountPaidCents(1250);
		paymentInfo2.setPaymentDateTime(new Date(System.currentTimeMillis()));
		paymentInfo2.setPaymentRefNumber("Test_Payment_Ref_Num_1");
		paymentInfo2.setPaymentType(PaymentType.CreditCard);
		piRefil.setPaymentInfo(paymentInfo2);
	}
}
