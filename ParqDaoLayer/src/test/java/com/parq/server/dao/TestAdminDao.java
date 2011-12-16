package com.parq.server.dao;

import com.parq.server.dao.model.object.Admin;
import com.parq.server.dao.support.DaoForTestingPurposes;

import junit.framework.TestCase;

/**
 * @author GZ
 *
 */
public class TestAdminDao extends TestCase {

	private AdminDao adminDao;

	private static final String adminName = "Test_Admin_1";
	private static final String password = "Password123";
	private static final String eMail = "TestAdmin12@testCorp.com";
	private static final String roleName = "TestRole";
	private static final String clientName = "TestClientName";
	
	private static boolean testDataCreated = false;

	@Override
	protected void setUp() throws Exception {
		adminDao = new AdminDao();
		
		createTestData();
	}
	
	private void createTestData() {
		if (!testDataCreated){
			testDataCreated = true;
			
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
	}

	public void testGetAdminByAdminName() {
		Admin admin = adminDao.getAdminByUserName(adminName);

		assertNotNull(admin);
		assertEquals(admin.getUserName(), adminName);
		assertEquals(admin.getPassword(), password);
		assertEquals(admin.getEmail(), eMail);
		assertTrue(admin.getAdminId() > 0);
		assertFalse(admin.getClientRelationships().isEmpty());
		assertTrue(admin.getClientRelationships().get(0).getAdminId() == admin.getAdminId());
		assertTrue(admin.getClientRelationships().get(0).getClientId() > 0);
		assertTrue(admin.getClientRelationships().get(0).getRelationShipId() > 0);
		assertTrue(admin.getClientRelationships().get(0).getRoleId() > 0);
	}
	
	public void testGetAdminById() {
		Admin adminTemp = adminDao.getAdminByUserName(adminName);
		Admin admin = adminDao.getAdminById(adminTemp.getAdminId());

		assertNotNull(admin);
		assertEquals(admin.getUserName(), adminName);
		assertEquals(admin.getPassword(), password);
		assertEquals(admin.getEmail(), eMail);
		assertTrue(admin.getAdminId() > 0);
		assertFalse(admin.getClientRelationships().isEmpty());
		assertTrue(admin.getClientRelationships().get(0).getAdminId() == admin.getAdminId());
		assertTrue(admin.getClientRelationships().get(0).getClientId() > 0);
		assertTrue(admin.getClientRelationships().get(0).getRelationShipId() > 0);
		assertTrue(admin.getClientRelationships().get(0).getRoleId() > 0);
	}
	
	public void testGetAdminByEmail() {
		Admin admin = adminDao.getAdminByEmail(eMail);

		assertNotNull(admin);
		assertEquals(admin.getUserName(), adminName);
		assertEquals(admin.getPassword(), password);
		assertEquals(admin.getEmail(), eMail);
		assertTrue(admin.getAdminId() > 0);
		assertFalse(admin.getClientRelationships().isEmpty());
		assertTrue(admin.getClientRelationships().get(0).getAdminId() == admin.getAdminId());
		assertTrue(admin.getClientRelationships().get(0).getClientId() > 0);
		assertTrue(admin.getClientRelationships().get(0).getRelationShipId() > 0);
		assertTrue(admin.getClientRelationships().get(0).getRoleId() > 0);
	}

	public void testCaching() {
		
		for (int i = 0; i < 1000; i++)
		{
			Admin admin = adminDao.getAdminByUserName(adminName);
			Admin admin1 = adminDao.getAdminByUserName(adminName);
			Admin admin2 = adminDao.getAdminByUserName(adminName);
			Admin admin3 = adminDao.getAdminByUserName(adminName);
			assertNotNull(admin);
			assertSame(admin, admin1);
			assertSame(admin, admin2);
			assertSame(admin, admin3);
			
			Admin adminId = adminDao.getAdminById(admin.getAdminId());
			Admin adminId1 = adminDao.getAdminById(admin.getAdminId());
			Admin adminId2 = adminDao.getAdminById(admin.getAdminId());
			Admin adminId3 = adminDao.getAdminById(admin.getAdminId());
			assertNotNull(adminId);
			assertSame(adminId, adminId1);
			assertSame(adminId, adminId2);
			assertSame(adminId, adminId3);
		
			
			Admin adminEmail = adminDao.getAdminByEmail(eMail);
			Admin adminEmail1 = adminDao.getAdminByEmail(eMail);
			Admin adminEmail2 = adminDao.getAdminByEmail(eMail);
			Admin adminEmail3 = adminDao.getAdminByEmail(eMail);
			assertNotNull(adminEmail);
			assertSame(adminEmail, adminEmail1);
			assertSame(adminEmail, adminEmail2);
			assertSame(adminEmail, adminEmail3);
		}
	}
}
