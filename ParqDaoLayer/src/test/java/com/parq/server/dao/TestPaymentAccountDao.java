package com.parq.server.dao;

import java.util.List;

import junit.framework.TestCase;

import com.parq.server.dao.model.object.PaymentAccount;
import com.parq.server.dao.support.SupportScriptForDaoTesting;

/**
 * @author GZ
 *
 */
public class TestPaymentAccountDao extends TestCase {

	private PaymentAccountDao payAccDao;
	
	@Override
	protected void setUp() throws Exception {
		payAccDao = new PaymentAccountDao();

		SupportScriptForDaoTesting.createUserTestData();

	}

	/**
	 * Test basic Dao initialization 
	 */
	public void testDaoInit() {
		SupportScriptForDaoTesting.insertMainTestDataSet();
		payAccDao.getAllPaymentMethodForUser(1);
	}

	/**
	 * Test a simple createNewPaymentMethod call.
	 */
	public void testCreateNewPaymentMethod(){
		SupportScriptForDaoTesting.insertMainTestDataSet();
		
		PaymentAccount newPA = new PaymentAccount();
		newPA.setCcStub(SupportScriptForDaoTesting.testCCStub);
		newPA.setCustomerId(SupportScriptForDaoTesting.testCustomerId);
		newPA.setDefaultPaymentMethod(true);
		newPA.setPaymentMethodId(SupportScriptForDaoTesting.testPaymentMethodId);
		newPA.setUserId(SupportScriptForDaoTesting.testUser.getUserID());
		newPA.setCardType(SupportScriptForDaoTesting.ccCardType);
		
		boolean paCreationSuccessful = payAccDao.createNewPaymentMethod(newPA);
		assertTrue(paCreationSuccessful);
		
		List<PaymentAccount> userNewPaList = payAccDao.getAllPaymentMethodForUser(SupportScriptForDaoTesting.testUser.getUserID()); 
		assertNotNull(userNewPaList);
		assertTrue(userNewPaList.size() == 1);
		
		// test to make sure the new PaymentAccount object is created correctly
		PaymentAccount userNewPa = userNewPaList.get(0);
		assertEquals(SupportScriptForDaoTesting.testCCStub, userNewPa.getCcStub());
		assertEquals(SupportScriptForDaoTesting.testCustomerId, userNewPa.getCustomerId());
		assertEquals(true, userNewPa.isDefaultPaymentMethod());
		assertEquals(SupportScriptForDaoTesting.testPaymentMethodId, userNewPa.getPaymentMethodId());
		assertEquals(SupportScriptForDaoTesting.testUser.getUserID(), userNewPa.getUserId());
		assertEquals(SupportScriptForDaoTesting.ccCardType, userNewPa.getCardType());
		assertTrue(userNewPa.getAccountId() > 0);
		
	}
	
	/**
	 * Test that when a new default payment account is added, the existing
	 * payment methods default status will be set to false 
	 */
	public void testOnlyOneDefaultPaymentMethod(){
		SupportScriptForDaoTesting.insertMainTestDataSet();
		
		int numOfPaymentToTest = 10;
		
		// create 10 dummy PA all with default payment set to true
		for (int i = 0; i < numOfPaymentToTest; i++) {
			PaymentAccount newPA = new PaymentAccount();
			newPA.setCcStub(SupportScriptForDaoTesting.testCCStub);
			newPA.setCustomerId(SupportScriptForDaoTesting.testCustomerId);
			newPA.setDefaultPaymentMethod(true);
			newPA.setPaymentMethodId(SupportScriptForDaoTesting.testPaymentMethodId);
			newPA.setUserId(SupportScriptForDaoTesting.testUser.getUserID());
			newPA.setCardType(SupportScriptForDaoTesting.ccCardType);
			
			boolean paCreationSuccessful = payAccDao.createNewPaymentMethod(newPA);
			assertTrue(paCreationSuccessful);
		}
		
		List<PaymentAccount> userNewPaList = payAccDao.getAllPaymentMethodForUser(SupportScriptForDaoTesting.testUser.getUserID()); 
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
		SupportScriptForDaoTesting.insertMainTestDataSet();
		
		int numOfPaymentToTest = 4;
		// create 4 dummy PA
		for (int i = 0; i < numOfPaymentToTest; i++) {
			PaymentAccount newPA = new PaymentAccount();
			newPA.setCcStub(SupportScriptForDaoTesting.testCCStub);
			newPA.setCustomerId(SupportScriptForDaoTesting.testCustomerId);
			newPA.setDefaultPaymentMethod(true);
			newPA.setPaymentMethodId(SupportScriptForDaoTesting.testPaymentMethodId);
			newPA.setUserId(SupportScriptForDaoTesting.testUser.getUserID());
			newPA.setCardType(SupportScriptForDaoTesting.ccCardType);
			
			boolean paCreationSuccessful = payAccDao.createNewPaymentMethod(newPA);
			assertTrue(paCreationSuccessful);
		}
		
		List<PaymentAccount> userNewPaList = payAccDao.getAllPaymentMethodForUser(SupportScriptForDaoTesting.testUser.getUserID()); 
		assertNotNull(userNewPaList);
		assertEquals(numOfPaymentToTest, userNewPaList.size());
	}
	
	public void testDeletePaymentMethod(){
		// System.out.println("TestPaymentAccountDao Delete UnitTest");
		
		SupportScriptForDaoTesting.insertMainTestDataSet();
		
		int numOfPaymentToTest = 6;
		// create 6 dummy PA
		for (int i = 0; i < numOfPaymentToTest; i++) {
			PaymentAccount newPA = new PaymentAccount();
			newPA.setCcStub(SupportScriptForDaoTesting.testCCStub);
			newPA.setCustomerId(SupportScriptForDaoTesting.testCustomerId);
			newPA.setDefaultPaymentMethod(true);
			newPA.setPaymentMethodId(SupportScriptForDaoTesting.testPaymentMethodId);
			newPA.setUserId(SupportScriptForDaoTesting.testUser.getUserID());
			newPA.setCardType(SupportScriptForDaoTesting.ccCardType);
			
			boolean paCreationSuccessful = payAccDao.createNewPaymentMethod(newPA);
			assertTrue(paCreationSuccessful);
		}
		
		List<PaymentAccount> userNewPaList = payAccDao.getAllPaymentMethodForUser(SupportScriptForDaoTesting.testUser.getUserID()); 
		assertNotNull(userNewPaList);
		assertEquals(numOfPaymentToTest, userNewPaList.size());
		
		// delete the payment methods
		for (PaymentAccount pa : userNewPaList) {
			payAccDao.deletePaymentMethod(pa.getAccountId());
		}
		
		// test that after the delete no payment account remain for this user
		List<PaymentAccount> paListAfterDelete = payAccDao.getAllPaymentMethodForUser(SupportScriptForDaoTesting.testUser.getUserID());
		assertNotNull(paListAfterDelete);
		assertEquals(0, paListAfterDelete.size());
	}
	

	
	public void testCacheForGetAllPaymentMethodForUser(){
		// System.out.println("TestPaymentAccountDao Cache UnitTest");
		
		int numOfPaymentToTest = 4;
		// create 4 dummy PA
		for (int i = 0; i < numOfPaymentToTest; i++) {
			PaymentAccount newPA = new PaymentAccount();
			newPA.setCcStub(SupportScriptForDaoTesting.testCCStub);
			newPA.setCustomerId(SupportScriptForDaoTesting.testCustomerId);
			newPA.setDefaultPaymentMethod(true);
			newPA.setPaymentMethodId(SupportScriptForDaoTesting.testPaymentMethodId);
			newPA.setUserId(SupportScriptForDaoTesting.testUser.getUserID());
			newPA.setCardType(SupportScriptForDaoTesting.ccCardType);
			
			boolean paCreationSuccessful = payAccDao.createNewPaymentMethod(newPA);
			assertTrue(paCreationSuccessful);
		}
		
		long curSysTimeBeforeCacheCall = System.currentTimeMillis();
		for (int i = 0; i < 1000; i++) {
			List<PaymentAccount> userNewPaList = payAccDao.getAllPaymentMethodForUser(SupportScriptForDaoTesting.testUser.getUserID()); 
			assertNotNull(userNewPaList);
			assertEquals(numOfPaymentToTest, userNewPaList.size());
		}
		long curSysTimeAfterCacheCall = System.currentTimeMillis();
		
		// check to see that the a call to the get method 1000 time result in runtime of less then 1 second
		assertTrue(curSysTimeAfterCacheCall - curSysTimeBeforeCacheCall < 1000);
		
		// test the cache result is accurate
		List<PaymentAccount> paList1 = payAccDao.getAllPaymentMethodForUser(SupportScriptForDaoTesting.testUser.getUserID());
		List<PaymentAccount> paList2 = payAccDao.getAllPaymentMethodForUser(SupportScriptForDaoTesting.testUser.getUserID());
		
		for (int i = 0; i < paList1.size(); i++) {
			PaymentAccount pa1 = paList1.get(i);
			PaymentAccount pa2 = paList2.get(i);
			
			assertSame(pa1, pa2);
			assertEquals(SupportScriptForDaoTesting.testCCStub, pa1.getCcStub());
			assertEquals(SupportScriptForDaoTesting.testCustomerId, pa1.getCustomerId());
			assertEquals(pa2.isDefaultPaymentMethod(), pa1.isDefaultPaymentMethod());
			assertEquals(SupportScriptForDaoTesting.testPaymentMethodId, pa1.getPaymentMethodId());
			assertEquals(SupportScriptForDaoTesting.testUser.getUserID(), pa1.getUserId());
			assertEquals(SupportScriptForDaoTesting.ccCardType, pa1.getCardType());
			assertTrue(pa1.getAccountId() > 0);
		}
	}
}
