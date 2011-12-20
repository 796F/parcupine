package com.parq.server.dao;

import junit.framework.TestCase;


/**
 * @author GZ
 *
 */
public class TestAbstractParqDaoParent extends TestCase {

	MockDao testDao;

	@Override
	protected void setUp() throws Exception {
		testDao = new MockDao();
	}

	public void testInitialize() {
		testDao.initialize();
		// if initialization call fails, it will never reach this assert
		// statement
		assertTrue(true);
	}

	public void testConnectAndClose() {
		testDao.initialize();
		testDao.getConnection();
		testDao.closeConnection();

		// if connect or close call fails, it will never reach this assert
		// statement
		assertTrue(true);
	}
}

class MockDao extends AbstractParqDaoParent {

	public MockDao() {
		propertyFile = "./ParqDao.properties";
	}
}
