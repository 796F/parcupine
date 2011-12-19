package com.parq.server.dao;

import com.parq.server.dao.model.object.User;
import com.parq.server.dao.support.ParqUnitTestParent;

/**
 * @author GZ
 *
 */
public class TestUserDao extends ParqUnitTestParent {


	@Override
	protected void setUp() throws Exception {
		userDao = new UserDao();
	}

	public void testCreateUser() {
		User newUser = new User();
		newUser.setPassword(testUserPW);
		newUser.setEmail(testUserEmail);
		newUser.setPhoneNumber(testUserPhone);

		boolean userCreationSuccessful = userDao.createNewUser(newUser);
		assertTrue(userCreationSuccessful);
	}

	public void testGetUserById() {
		User userTemp = userDao.getUserByEmail(testUserEmail);
		User user = userDao.getUserById(userTemp.getUserID());

		assertNotNull(user);
		assertEquals(user.getPassword(), testUserPW);
		assertEquals(user.getEmail(), testUserEmail);
		assertEquals(user.getPhoneNumber(), testUserPhone);
		assertTrue(user.getUserID() > 0);
	}
	
	public void testGetUserByEmail() {
		User user = userDao.getUserByEmail(testUserEmail);

		assertNotNull(user);
		assertEquals(user.getPassword(), testUserPW);
		assertEquals(user.getEmail(), testUserEmail);
		assertEquals(user.getPhoneNumber(), testUserPhone);
		assertTrue(user.getUserID() > 0);
	}

	public void testCaching() {
		
		User user = userDao.getUserByEmail(testUserEmail);
		User user1 = userDao.getUserByEmail(testUserEmail);
		User user2 = userDao.getUserByEmail(testUserEmail);
		User user3 = userDao.getUserByEmail(testUserEmail);
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
	
		
		User userEmail = userDao.getUserByEmail(testUserEmail);
		User userEmail1 = userDao.getUserByEmail(testUserEmail);
		User userEmail2 = userDao.getUserByEmail(testUserEmail);
		User userEmail3 = userDao.getUserByEmail(testUserEmail);
		assertSame(userEmail, userEmail1);
		assertSame(userEmail, userEmail2);
		assertSame(userEmail, userEmail3);
	}
	
	public void testUserCleanUp() {
		deleteUserTestData();
	}
}
