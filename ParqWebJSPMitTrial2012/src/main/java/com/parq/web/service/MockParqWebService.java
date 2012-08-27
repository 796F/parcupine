package com.parq.web.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.parq.web.PasswordChangeRequest;
import com.parq.web.model.MapLocation;
import com.parq.web.model.ParkingHistory;
import com.parq.web.model.ParkingSpaceStatus;
import com.parq.web.model.ReportingHistory;
import com.parq.web.model.UserRegistration;
import com.parq.web.model.WebParkingLocation;
import com.parq.web.model.WebUser;

public class MockParqWebService implements ParqWebService{

	@Override
	public WebUser validateUser(WebUser userToValidate) {
		
		if (userToValidate.getPassword().equalsIgnoreCase("password")) {
			userToValidate.setAuthenticated(true);
			userToValidate.setAdminUser(false);
		}
		else if (userToValidate.getPassword().equalsIgnoreCase("admin")) {
			userToValidate.setAuthenticated(true);
			userToValidate.setAdminUser(true);
		}
		else {
			userToValidate.setAuthenticated(false);
			userToValidate.setAdminUser(false);
			userToValidate.setLoginFailed(true);
		}
		return userToValidate;
	}
	
	@Override
	public List<ParkingHistory> getParkingHistoryForUser(WebUser user) {
		List<ParkingHistory> pHis = new ArrayList<ParkingHistory>();
		
		ParkingHistory his1 = new ParkingHistory();
		his1.setCost(1.25);
		his1.setDate("Nov-13-2011");
		his1.setEndTime("5:00 pm");
		his1.setStartTime("4:15 pm");
		his1.setLocationName("MIT Student Lot");
		his1.setParkingRefNumber("MIT1001-01");
		
		ParkingHistory his2 = new ParkingHistory();
		his2.setCost(4.25);
		his2.setDate("Nov-15-2011");
		his2.setEndTime("5:00 pm");
		his2.setStartTime("2:15 pm");
		his2.setLocationName("MIT Student Lot");
		his2.setParkingRefNumber("MIT1001-02");
		
		ParkingHistory his3 = new ParkingHistory();
		his3.setCost(8.35);
		his3.setDate("Nov-16-2011");
		his3.setEndTime("5:00 pm");
		his3.setStartTime("1:00 pm");
		his3.setLocationName("MIT Student Lot");
		his3.setParkingRefNumber("MIT1001-03");
		
		pHis.add(his1);
		pHis.add(his2);
		pHis.add(his3);
		
		return pHis;
	}
	
	@Override
	public boolean changePasswordForUser(WebUser user, PasswordChangeRequest passwordChange) {
		boolean changeSuccessful = Math.random() > 0.5;
		
		passwordChange.setInvalidPassword(!changeSuccessful);
		passwordChange.setWrongOldPassword(!changeSuccessful);
		passwordChange.setNewPasswordNotMatch(!changeSuccessful);
		
		return changeSuccessful;
	}
	
	@Override
	public List<ParkingSpaceStatus> getParkingStatusByClient(long clientId, String locationIdentifier) {
		List<ParkingSpaceStatus> pStatus = new ArrayList<ParkingSpaceStatus>();
		
		ParkingSpaceStatus status1 = new ParkingSpaceStatus();
		status1.setOccupied(true);
		status1.setParkingLevel("1");
		status1.setSpaceId(2345);
		status1.setSpaceLocation("MIT");
		status1.setSpaceName("MIT-1001");
		
		ParkingSpaceStatus status2 = new ParkingSpaceStatus();
		status2.setOccupied(true);
		status2.setParkingLevel("1");
		status2.setSpaceId(2484);
		status2.setSpaceLocation("MIT");
		status2.setSpaceName("MIT-1002");
		
		ParkingSpaceStatus status3 = new ParkingSpaceStatus();
		status3.setOccupied(false);
		status3.setParkingLevel("1");
		status3.setSpaceId(2115);
		status3.setSpaceLocation("MIT");
		status3.setSpaceName("MIT-1003");
		
		pStatus.add(status1);
		pStatus.add(status2);
		pStatus.add(status3);
		
		return pStatus;
	}
	
	@Override
	public boolean registerNewUser(UserRegistration registration) {
		boolean registrationSuccessful = Math.random() > 0.5;
		
		registration.setInvalidPassword(!registrationSuccessful);
		registration.setPasswordDoesNotMatch(!registrationSuccessful);
		registration.setEmailAlreadyExist(!registrationSuccessful);
		
		return registrationSuccessful;
	}

	@Override
	public List<WebParkingLocation> findParkingLocations(MapLocation centerOfMap) {
		List<WebParkingLocation> webParkingLocations = new ArrayList<WebParkingLocation>();
		
		WebParkingLocation firstLocation = new WebParkingLocation();
		firstLocation.setAvailable(true);
		firstLocation.setLatitude(42.358117);
		firstLocation.setLongitude(-71.094253);
		firstLocation.setLocationName("space 1");
		webParkingLocations.add(firstLocation);
		
		WebParkingLocation secondLocation = new WebParkingLocation();
		secondLocation.setAvailable(false);
		secondLocation.setLatitude(42.358419);
		secondLocation.setLongitude(-71.094851);
		secondLocation.setLocationName("space 2");
		webParkingLocations.add(secondLocation);
		
		return webParkingLocations;
	}

	@Override
	public List<ReportingHistory> getReportingHistoryForUser(WebUser user) {
		List<ReportingHistory> reportHis = new ArrayList<ReportingHistory>();
		ReportingHistory his1 = new ReportingHistory();
		his1.setDate(new Date(System.currentTimeMillis()));
		his1.setPoints(150);
		reportHis.add(his1);
		ReportingHistory his2 = new ReportingHistory();
		his2.setDate(new Date(System.currentTimeMillis() - 10000000));
		his2.setPoints(100);
		reportHis.add(his2);

		return reportHis;
	}
}
