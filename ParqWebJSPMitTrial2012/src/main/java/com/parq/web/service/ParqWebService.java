package com.parq.web.service;

import java.util.List;

import com.parq.web.PasswordChangeRequest;
import com.parq.web.model.MapLocation;
import com.parq.web.model.ParkingHistory;
import com.parq.web.model.ParkingSpaceStatus;
import com.parq.web.model.ReportingHistory;
import com.parq.web.model.UserRegistration;
import com.parq.web.model.WebParkingLocation;
import com.parq.web.model.WebUser;

public interface ParqWebService {

	WebUser validateUser(WebUser userToValidate);
	
	List<ParkingHistory> getParkingHistoryForUser(WebUser user);
	
	boolean changePasswordForUser(WebUser user, PasswordChangeRequest passwordChange);
	
	List<ParkingSpaceStatus> getParkingStatusByClient(long clientId, String locationIdentifier);
		
	boolean registerNewUser(UserRegistration registration);
	
	List<WebParkingLocation> findParkingLocations(MapLocation centerOfMap);
	
	List<ReportingHistory> getReportingHistoryForUser(WebUser user);
}
