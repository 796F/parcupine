package com.parq.server.dao;

import com.parq.server.dao.model.object.User;

import junit.framework.TestCase;

public class TestUserDao extends TestCase {

	private UserDao userDao;

	private static final String password = "Password123";
	private static final String eMail = "TestUser12@testCorp.com";
	private static final String phoneNumber = "123-555-4567";

	@Override
	protected void setUp() throws Exception {
		userDao = new UserDao();
	}

	public void testCreateUser() {
		User newUser = new User();
		newUser.setPassword(password);
		newUser.setEmail(eMail);
		newUser.setPhoneNumber(phoneNumber);

		boolean userCreationSuccessful = userDao.createNewUser(newUser);
		assertTrue(userCreationSuccessful);
	}

	public void testGetUserById() {
		User userTemp = userDao.getUserByEmail(eMail);
		User user = userDao.getUserById(userTemp.getUserID());

		assertNotNull(user);
		assertEquals(user.getPassword(), password);
		assertEquals(user.getEmail(), eMail);
		assertEquals(user.getPhoneNumber(), phoneNumber);
		assertTrue(user.getUserID() > 0);
	}
	
	public void testGetUserByEmail() {
		User user = userDao.getUserByEmail(eMail);

		assertNotNull(user);
		assertEquals(user.getPassword(), password);
		assertEquals(user.getEmail(), eMail);
		assertEquals(user.getPhoneNumber(), phoneNumber);
		assertTrue(user.getUserID() > 0);
	}

	public void testCaching() {
		
		User user = userDao.getUserByEmail(eMail);
		User user1 = userDao.getUserByEmail(eMail);
		User user2 = userDao.getUserByEmail(eMail);
		User user3 = userDao.getUserByEmail(eMail);
		assertSame(user, user1);
		assertSame(user, user2);
		assertSame(user, user3);
		
		// for some reason the above method return user as null during
		// maven surefire test, but not during regular eclipse unit testing.
		// Cause of the issue is still under investigation, current work around
		// is to use a if block to by past the below test during maven surefire testing
		// phase.
		//	if (user == null) {
		//		System.out.println(
		//			"By passing test cache of getUserById portion, " +
		//			"because getUserByUserName is returning: " + user);
		//	}
		User userId = userDao.getUserById(user.getUserID());
		User userId1 = userDao.getUserById(user.getUserID());
		User userId2 = userDao.getUserById(user.getUserID());
		User userId3 = userDao.getUserById(user.getUserID());
		assertSame(userId, userId1);
		assertSame(userId, userId2);
		assertSame(userId, userId3);
	
		
		User userEmail = userDao.getUserByEmail(eMail);
		User userEmail1 = userDao.getUserByEmail(eMail);
		User userEmail2 = userDao.getUserByEmail(eMail);
		User userEmail3 = userDao.getUserByEmail(eMail);
		assertSame(userEmail, userEmail1);
		assertSame(userEmail, userEmail2);
		assertSame(userEmail, userEmail3);
	}

	public void testDeleteUser() {
		User user = userDao.getUserByEmail(eMail);
		boolean deleteSuccessful = userDao.deleteUserById(user.getUserID());
		assertTrue(deleteSuccessful);
	}
}
