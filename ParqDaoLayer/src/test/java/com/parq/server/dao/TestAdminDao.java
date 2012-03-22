package com.parq.server.dao;

import junit.framework.TestCase;

import com.parq.server.dao.model.object.Admin;
import com.parq.server.dao.model.object.AdminRole;
import com.parq.server.dao.model.object.Client;
import com.parq.server.dao.support.SupportScriptForDaoTesting;

/**
 * @author GZ
 *
 */
public class TestAdminDao extends TestCase {

	private AdminDao adminDao;
	
	private String email = "test4545@asdf.com";
	private String password = "dfasdfdwfe";

	private static boolean testDataCreated = false;

	@Override
	protected void setUp() throws Exception {
		adminDao = new AdminDao();
		createTestData();
	}
	
	private void createTestData() {
		if (!testDataCreated){
			testDataCreated = true;
			SupportScriptForDaoTesting.insertMainTestDataSet();
			SupportScriptForDaoTesting.createAdminUserTestData();
		}
	}
	
	public void testCreateAdmin(){
		ClientDao clientDao = new ClientDao();
		Client client = clientDao.getClientByName(SupportScriptForDaoTesting.clientNameMain);
		
		Admin newAdmin = new Admin();
		newAdmin.setEmail(email);
		newAdmin.setPassword(password);
		newAdmin.setClientId(client.getId());
		newAdmin.setAdminRole(AdminRole.admin);
		
		Boolean createSuccessful = 
			adminDao.createAdmin(newAdmin);
		assertTrue(createSuccessful);
		
		Admin newlyCreatedAdmin = adminDao.getAdminByEmail(email);
		assertEquals(email, newlyCreatedAdmin.getEmail());
		assertEquals(password, newlyCreatedAdmin.getPassword());
		assertNotNull(newlyCreatedAdmin.getAdminRole());
		assertTrue(newlyCreatedAdmin.getClientId() > 0);
		assertTrue(newlyCreatedAdmin.getAdminId() > 0);
	}
	
	public void testUpdateAdmin(){
		String tempEmail = "testUHJ@87864.com";
		String tempPassword = "78615";
		
		// test admin update
		Admin admin = adminDao.getAdminByEmail(email);
		admin.setEmail(tempEmail);
		admin.setPassword(tempPassword);
		boolean updateSuccessful = adminDao.updateAdmin(admin);
		assertTrue(updateSuccessful);
		
		Admin uAdmin = adminDao.getAdminByEmail(tempEmail);
		assertNotNull(uAdmin);
		assertEquals(tempEmail, uAdmin.getEmail());
		assertEquals(tempPassword, uAdmin.getPassword());
		
		// revert the admin back to the old value
		admin.setEmail(email);
		admin.setPassword(password);
		updateSuccessful = adminDao.updateAdmin(admin);
		assertTrue(updateSuccessful);
		
		uAdmin = adminDao.getAdminByEmail(email);
		assertNotNull(uAdmin);
		assertEquals(email, uAdmin.getEmail());
		assertEquals(password, uAdmin.getPassword());
		
	}
	
	public void testDeleteAdmin() {
		Admin admin = adminDao.getAdminByEmail(email);
		boolean deleteSuccessful = adminDao.deleteAdmin(admin.getAdminId());
		assertTrue(deleteSuccessful); 
		
		admin = adminDao.getAdminByEmail(email);
		assertNull(admin);
	}
	
	public void testGetAdminByEmail() {
		Admin admin = adminDao.getAdminByEmail(SupportScriptForDaoTesting.adminEMail);

		assertNotNull(admin);
		assertEquals(admin.getPassword(), SupportScriptForDaoTesting.adminPassword);
		assertEquals(admin.getEmail(), SupportScriptForDaoTesting.adminEMail);
		assertTrue(admin.getAdminId() > 0);
		assertNotNull(admin.getAdminRole());
		assertTrue(admin.getClientId() > 0);
	}
	
	public void testGetAdminById() {
		Admin adminTemp = adminDao.getAdminByEmail(SupportScriptForDaoTesting.adminEMail);
		Admin admin = adminDao.getAdminById(adminTemp.getAdminId());

		assertNotNull(admin);
		assertEquals(admin.getPassword(), SupportScriptForDaoTesting.adminPassword);
		assertEquals(admin.getEmail(), SupportScriptForDaoTesting.adminEMail);
		assertTrue(admin.getAdminId() > 0);
		assertNotNull(admin.getAdminRole());
		assertTrue(admin.getClientId() > 0);
	}

	public void testCaching() {
		
		for (int i = 0; i < 1000; i++)
		{
			Admin admin = adminDao.getAdminByEmail(SupportScriptForDaoTesting.adminEMail);
			Admin admin1 = adminDao.getAdminByEmail(SupportScriptForDaoTesting.adminEMail);
			Admin admin2 = adminDao.getAdminByEmail(SupportScriptForDaoTesting.adminEMail);
			Admin admin3 = adminDao.getAdminByEmail(SupportScriptForDaoTesting.adminEMail);
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
		
			
			Admin adminEmail = adminDao.getAdminByEmail(SupportScriptForDaoTesting.adminEMail);
			Admin adminEmail1 = adminDao.getAdminByEmail(SupportScriptForDaoTesting.adminEMail);
			Admin adminEmail2 = adminDao.getAdminByEmail(SupportScriptForDaoTesting.adminEMail);
			Admin adminEmail3 = adminDao.getAdminByEmail(SupportScriptForDaoTesting.adminEMail);
			assertNotNull(adminEmail);
			assertSame(admin, adminEmail1);
			assertSame(admin, adminEmail2);
			assertSame(admin, adminEmail3);
		}
	}
}
