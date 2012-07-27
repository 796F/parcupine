package com.parq.server.dao;

import java.util.List;

import junit.framework.TestCase;

import com.parq.server.dao.model.object.PaymentMethod;
import com.parq.server.dao.model.object.User;
import com.parq.server.dao.model.object.UserScore;
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
		newUser.setAccountType(PaymentMethod.PREFILL);

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
		assertEquals(user.getAccountType(), PaymentMethod.PREFILL);
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
		assertEquals(user.getAccountType(), PaymentMethod.PREFILL);
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

	public void testGetUserScore() {
		User user = userDao
			.getUserByEmail(SupportScriptForDaoTesting.userEmail);
		UserScore score = userDao.getScoreForUser(user.getUserID());
		assertEquals(user.getUserID(), score.getUserId());
		assertTrue(score.getScoreId() > 0);
		assertTrue(score.getScore1() >= 0);
		assertTrue(score.getScore2() >= 0);
		assertTrue(score.getScore3() >= 0);
	}
	
	public void testGetUserScoreHistory() {
		User user = userDao
			.getUserByEmail(SupportScriptForDaoTesting.userEmail);
		
		List<UserScore> scores = userDao.getScoreHistoryForUser(user.getUserID());
		assertNotNull(scores);
		assertTrue(scores.size() > 0);
		
		for (UserScore score : scores) {
			assertEquals(user.getUserID(), score.getUserId());
			assertTrue(score.getScoreId() > 0);
			assertTrue(score.getScore1() >= 0);
			assertTrue(score.getScore2() >= 0);
			assertTrue(score.getScore3() >= 0);
		}
	}
	
	public void testUpdateUserScore() {
		User user = userDao
			.getUserByEmail(SupportScriptForDaoTesting.userEmail);
	
		List<UserScore> scores = userDao.getScoreHistoryForUser(user.getUserID());
		int currentScoreHistorySize = scores.size();
		
		int score1Value = 5;
		int score2Value = 10;
		int score3Value = 15;
		
		UserScore newScore = new UserScore();
		newScore.setUserId(user.getUserID());
		newScore.setScore1(score1Value);
		newScore.setScore2(score2Value);
		newScore.setScore3(score3Value);
		userDao.updateUserScore(newScore);
		
		scores = userDao.getScoreHistoryForUser(user.getUserID());
		assertEquals(currentScoreHistorySize + 1, scores.size());
		
		for (int i = 0; i < scores.size(); i++) {
			UserScore score = scores.get(i);
			if (i == 0) {
				assertEquals(user.getUserID(), score.getUserId());
				assertTrue(score.getScoreId() > 0);
				assertEquals(score1Value, score.getScore1());
				assertEquals(score2Value, score.getScore2());
				assertEquals(score3Value, score.getScore3());
			} else {
				assertEquals(user.getUserID(), score.getUserId());
				assertTrue(score.getScoreId() > 0);
				assertTrue(score.getScore1() >= 0);
				assertTrue(score.getScore2() >= 0);
				assertTrue(score.getScore3() >= 0);
			}
		}
	}
	
	public void testUserCleanUp() {
		// SupportScriptForDaoTesting.deleteUserTestData();
	}
}
