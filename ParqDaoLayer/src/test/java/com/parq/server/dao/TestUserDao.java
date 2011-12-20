package com.parq.server.dao;

import junit.framework.TestCase;

import com.parq.server.dao.model.object.User;
import com.parq.server.dao.support.SupportScriptForDaoTesting;

/**
 * @author GZ
 * 
 */
public class TestUserDao extends TestCase {

	private UserDao userDao;

	@Override
	protected void setUp() throws Exception {
		userDao = new UserDao();
	}

	public void testCreateUser() {
		
		// delete any existing user data on the DB before before testing
		User delUser = userDao.getUserByEmail(SupportScriptForDaoTesting.userEmail);
		if (delUser != null) {
			boolean deleteUserSuccessful = 
				userDao.deleteUserById(delUser.getUserID());
			assertTrue(deleteUserSuccessful);
		}
		
		User newUser = new User();
		newUser.setPassword(SupportScriptForDaoTesting.userPassWord);
		newUser.setEmail(SupportScriptForDaoTesting.userEmail);
		newUser.setPhoneNumber(SupportScriptForDaoTesting.userPhoneNum);

		boolean userCreationSuccessful = userDao.createNewUser(newUser);
		assertTrue(userCreationSuccessful);
	}

	public void testGetUserById() {
		User userTemp = userDao
				.getUserByEmail(SupportScriptForDaoTesting.userEmail);
		User user = userDao.getUserById(userTemp.getUserID());

		assertNotNull(user);
		assertEquals(user.getPassword(),
				SupportScriptForDaoTesting.userPassWord);
		assertEquals(user.getEmail(), SupportScriptForDaoTesting.userEmail);
		assertEquals(user.getPhoneNumber(),
				SupportScriptForDaoTesting.userPhoneNum);
		assertTrue(user.getUserID() > 0);
	}

	public void testGetUserByEmail() {
		User user = userDao
				.getUserByEmail(SupportScriptForDaoTesting.userEmail);

		assertNotNull(user);
		assertEquals(user.getPassword(),
				SupportScriptForDaoTesting.userPassWord);
		assertEquals(user.getEmail(), SupportScriptForDaoTesting.userEmail);
		assertEquals(user.getPhoneNumber(),
				SupportScriptForDaoTesting.userPhoneNum);
		assertTrue(user.getUserID() > 0);
	}

	public void testCaching() {

		User user = userDao
				.getUserByEmail(SupportScriptForDaoTesting.userEmail);
		User user1 = userDao
				.getUserByEmail(SupportScriptForDaoTesting.userEmail);
		User user2 = userDao
				.getUserByEmail(SupportScriptForDaoTesting.userEmail);
		User user3 = userDao
				.getUserByEmail(SupportScriptForDaoTesting.userEmail);
		assertSame(user, user1);
		assertSame(user, user2);
		assertSame(user, user3);

		// for some reason the above method return user as null during
		// maven surefire test, but not during regular eclipse unit testing.
		// Cause of the issue is still under investigation, current work around
		// is to use a if block to by past the below test during maven surefire
		// testing
		// phase.
		// if (user == null) {
		// System.out.println(
		// "By passing test cache of getUserById portion, " +
		// "because getUserByUserName is returning: " + user);
		// }
		User userById = userDao.getUserById(user.getUserID());
		User userById1 = userDao.getUserById(user.getUserID());
		User userById2 = userDao.getUserById(user.getUserID());
		User userById3 = userDao.getUserById(user.getUserID());
		assertSame(userById, userById1);
		assertSame(userById, userById2);
		assertSame(userById, userById3);

		User userByEmail = userDao
				.getUserByEmail(SupportScriptForDaoTesting.userEmail);
		User userByEmail1 = userDao
				.getUserByEmail(SupportScriptForDaoTesting.userEmail);
		User userByEmail2 = userDao
				.getUserByEmail(SupportScriptForDaoTesting.userEmail);
		User userByEmail3 = userDao
				.getUserByEmail(SupportScriptForDaoTesting.userEmail);
		assertSame(userByEmail, userByEmail1);
		assertSame(userByEmail, userByEmail2);
		assertSame(userByEmail, userByEmail3);
	}

	public void testUserCleanUp() {
		// SupportScriptForDaoTesting.deleteUserTestData();
	}
}
