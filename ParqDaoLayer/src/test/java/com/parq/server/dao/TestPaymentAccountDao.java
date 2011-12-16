package com.parq.server.dao;

import java.util.List;

import com.parq.server.dao.model.object.PaymentAccount;
import com.parq.server.dao.model.object.User;
import com.parq.server.dao.support.SupportScriptForDaoTesting;

import junit.framework.TestCase;

/**
 * @author GZ
 *
 */
public class TestPaymentAccountDao extends TestCase {

	private PaymentAccountDao payAccDao;
	private UserDao userDao;
	private static User testUser;
	
	private static final String testCCStub = "1234";
	private static final String testCustomerId = "CUS_345";
	private static final String testPaymentMethodId = "PAY_456169";
	
	private static final String testUserEmail = "TestUser@PaymentAccount.test";
	private static final String testUserPW = "TestPassword";
	private static final String testUserPhone = "123-555-7890";
	
	

	@Override
	protected void setUp() throws Exception {
		payAccDao = new PaymentAccountDao();
		userDao = new UserDao();
		
		if (testUser == null) {
			// Create the test user to test the app with.
			User newUser = new User();
			newUser.setPassword(testUserPW);
			newUser.setEmail(testUserEmail);
			newUser.setPhoneNumber(testUserPhone);
			boolean userCreationSuccessful = userDao.createNewUser(newUser);
			assertTrue(userCreationSuccessful);
			
			testUser = userDao.getUserByEmail(testUserEmail);
		}
	}
	
	

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		if (testUser != null) {
			boolean deleteUserSuccessful = userDao.deleteUserById(testUser.getUserID());
			assertTrue(deleteUserSuccessful);
			testUser = null;
		}
	}


	/**
	 * Test basic Dao initialization 
	 */
	public void testDaoInit() {
		SupportScriptForDaoTesting.insertFakeData();
		payAccDao.getAllPaymentMethodForUser(1);
	}

	/**
	 * Test a simple createNewPaymentMethod call.
	 */
	public void testCreateNewPaymentMethod(){
		SupportScriptForDaoTesting.insertFakeData();
		
		PaymentAccount newPA = new PaymentAccount();
		newPA.setCcStub(testCCStub);
		newPA.setCustomerId(testCustomerId);
		newPA.setDefaultPaymentMethod(true);
		newPA.setPaymentMethodId(testPaymentMethodId);
		newPA.setUserId(testUser.getUserID());
		
		boolean paCreationSuccessful = payAccDao.createNewPaymentMethod(newPA);
		assertTrue(paCreationSuccessful);
		
		List<PaymentAccount> userNewPaList = payAccDao.getAllPaymentMethodForUser(testUser.getUserID()); 
		assertNotNull(userNewPaList);
		assertTrue(userNewPaList.size() == 1);
		
		// test to make sure the new PaymentAccount object is created correctly
		PaymentAccount userNewPa = userNewPaList.get(0);
		assertEquals(testCCStub, userNewPa.getCcStub());
		assertEquals(testCustomerId, userNewPa.getCustomerId());
		assertEquals(true, userNewPa.isDefaultPaymentMethod());
		assertEquals(testPaymentMethodId, userNewPa.getPaymentMethodId());
		assertEquals(testUser.getUserID(), userNewPa.getUserId());
		assertTrue(userNewPa.getAccountId() > 0);
		
	}
	
	/**
	 * Test that when a new default payment account is added, the existing
	 * payment methods default status will be set to false 
	 */
	public void testOnlyOneDefaultPaymentMethod(){
		SupportScriptForDaoTesting.insertFakeData();
		
		int numOfPaymentToTest = 10;
		
		// create 10 dummy PA all with default payment set to true
		for (int i = 0; i < numOfPaymentToTest; i++) {
			PaymentAccount newPA = new PaymentAccount();
			newPA.setCcStub(testCCStub);
			newPA.setCustomerId(testCustomerId);
			newPA.setDefaultPaymentMethod(true);
			newPA.setPaymentMethodId(testPaymentMethodId);
			newPA.setUserId(testUser.getUserID());
			
			boolean paCreationSuccessful = payAccDao.createNewPaymentMethod(newPA);
			assertTrue(paCreationSuccessful);
		}
		
		List<PaymentAccount> userNewPaList = payAccDao.getAllPaymentMethodForUser(testUser.getUserID()); 
		assertNotNull(userNewPaList);
		assertEquals(numOfPaymentToTest, userNewPaList.size());
		
		// check that only 1 default payment exist in the end.
		int numOfDefaultPayment = 0;
		for (PaymentAccount pa : userNewPaList) {
			if (pa.isDefaultPaymentMethod()){
				numOfDefaultPayment++;
			}
		}
		
		assertEquals(1, numOfDefaultPayment); 
	}
	
	/**
	 * Test the getAllPaymentMethodForUser method of the PaymentAccountDao
	 */
	public void testGetAllPaymentMethodForUser() {
		SupportScriptForDaoTesting.insertFakeData();
		
		int numOfPaymentToTest = 4;
		// create 4 dummy PA
		for (int i = 0; i < numOfPaymentToTest; i++) {
			PaymentAccount newPA = new PaymentAccount();
			newPA.setCcStub(testCCStub);
			newPA.setCustomerId(testCustomerId);
			newPA.setDefaultPaymentMethod(true);
			newPA.setPaymentMethodId(testPaymentMethodId);
			newPA.setUserId(testUser.getUserID());
			
			boolean paCreationSuccessful = payAccDao.createNewPaymentMethod(newPA);
			assertTrue(paCreationSuccessful);
		}
		
		List<PaymentAccount> userNewPaList = payAccDao.getAllPaymentMethodForUser(testUser.getUserID()); 
		assertNotNull(userNewPaList);
		assertEquals(numOfPaymentToTest, userNewPaList.size());
	}
	
	public void testDeletePaymentMethod(){
		// System.out.println("TestPaymentAccountDao Delete UnitTest");
		
		SupportScriptForDaoTesting.insertFakeData();
		
		int numOfPaymentToTest = 6;
		// create 6 dummy PA
		for (int i = 0; i < numOfPaymentToTest; i++) {
			PaymentAccount newPA = new PaymentAccount();
			newPA.setCcStub(testCCStub);
			newPA.setCustomerId(testCustomerId);
			newPA.setDefaultPaymentMethod(true);
			newPA.setPaymentMethodId(testPaymentMethodId);
			newPA.setUserId(testUser.getUserID());
			
			boolean paCreationSuccessful = payAccDao.createNewPaymentMethod(newPA);
			assertTrue(paCreationSuccessful);
		}
		
		List<PaymentAccount> userNewPaList = payAccDao.getAllPaymentMethodForUser(testUser.getUserID()); 
		assertNotNull(userNewPaList);
		assertEquals(numOfPaymentToTest, userNewPaList.size());
		
		// delete the payment methods
		for (PaymentAccount pa : userNewPaList) {
			payAccDao.deletePaymentMethod(pa.getAccountId());
		}
		
		// test that after the delete no payment account remain for this user
		List<PaymentAccount> paListAfterDelete = payAccDao.getAllPaymentMethodForUser(testUser.getUserID());
		assertNotNull(paListAfterDelete);
		assertEquals(0, paListAfterDelete.size());
	}
	

	
	public void testCacheForGetAllPaymentMethodForUser(){
		// System.out.println("TestPaymentAccountDao Cache UnitTest");
		
		int numOfPaymentToTest = 4;
		// create 4 dummy PA
		for (int i = 0; i < numOfPaymentToTest; i++) {
			PaymentAccount newPA = new PaymentAccount();
			newPA.setCcStub(testCCStub);
			newPA.setCustomerId(testCustomerId);
			newPA.setDefaultPaymentMethod(true);
			newPA.setPaymentMethodId(testPaymentMethodId);
			newPA.setUserId(testUser.getUserID());
			
			boolean paCreationSuccessful = payAccDao.createNewPaymentMethod(newPA);
			assertTrue(paCreationSuccessful);
		}
		
		long curSysTimeBeforeCacheCall = System.currentTimeMillis();
		for (int i = 0; i < 1000; i++) {
			List<PaymentAccount> userNewPaList = payAccDao.getAllPaymentMethodForUser(testUser.getUserID()); 
			assertNotNull(userNewPaList);
			assertEquals(numOfPaymentToTest, userNewPaList.size());
		}
		long curSysTimeAfterCacheCall = System.currentTimeMillis();
		
		// check to see that the a call to the get method 1000 time result in runtime of less then 1 second
		assertTrue(curSysTimeAfterCacheCall - curSysTimeBeforeCacheCall < 1000);
		
		// test the cache result is accurate
		List<PaymentAccount> paList1 = payAccDao.getAllPaymentMethodForUser(testUser.getUserID());
		List<PaymentAccount> paList2 = payAccDao.getAllPaymentMethodForUser(testUser.getUserID());
		
		for (int i = 0; i < paList1.size(); i++) {
			PaymentAccount pa1 = paList1.get(i);
			PaymentAccount pa2 = paList2.get(i);
			
			assertSame(pa1, pa2);
			assertEquals(testCCStub, pa1.getCcStub());
			assertEquals(testCustomerId, pa1.getCustomerId());
			assertEquals(pa2.isDefaultPaymentMethod(), pa1.isDefaultPaymentMethod());
			assertEquals(testPaymentMethodId, pa1.getPaymentMethodId());
			assertEquals(testUser.getUserID(), pa1.getUserId());
			assertTrue(pa1.getAccountId() > 0);
		}
		
	}
}
