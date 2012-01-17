package com.parq.web.service;

import junit.framework.TestCase;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.parq.web.MapLocation;
import com.parq.web.ReportDateRangeFilter;
import com.parq.web.WebUser;

public class TestAccount extends TestCase {

	public void testNothing() {
		// newest version of mvn surefire plugin will not accept a unit that that have no assertions
		// so we have to create a dummy test method that does nothing
		assert (true);
	}
	
	public void _testAdminAccount() {
		ParqWebServiceImpl service = new ParqWebServiceImpl();
		WebUser user = new WebUser();
		user.setUsername("TestAdmin12@testCorp.com");
		user.setPassword("password");
		boolean loginSuccessful = service.validateUser(user).isAuthenticated();
		assertTrue(loginSuccessful);
	}

	public void _testGetPaymentHistory() {
		ParqWebServiceImpl service = new ParqWebServiceImpl();
		WebUser user = new WebUser();
		user.setUsername("TestAdmin12@testCorp.com");
		user.setPassword("Password123");
		boolean loginSuccessful = service.validateUser(user).isAuthenticated();
		assertTrue(loginSuccessful);

		ReportDateRangeFilter newFilter = new ReportDateRangeFilter();
		newFilter.setDateRangeFilter(ReportDateRangeFilter.DateRange.THIS_MONTH
				.name());

		service.getParkingHistoryForUser(user, newFilter);
	}

	public void _testUrlRetrival() {
		BufferedReader in = null;
		try {
			URL google = new URL(
					"https://maps.googleapis.com/maps/api/geocode/xml?address=washington+dc+usa&sensor=false");
			URLConnection yc = google.openConnection();
			in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				System.out.println(inputLine);

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	public void _testGoogleMapService() {
		String testInput = "Paris, France";
		GoogleMapService.getMapLocationForLocationName(testInput);
	}
	
	public void _testParqWebService() {
		ParqWebServiceImpl service = new ParqWebServiceImpl();
		service.findParkingLocations(new MapLocation());
	}
}
