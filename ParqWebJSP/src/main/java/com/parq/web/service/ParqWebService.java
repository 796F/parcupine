package com.parq.web.service;

import java.util.List;

import com.parq.web.ParkingHistory;
import com.parq.web.ParkingReport;
import com.parq.web.ParkingSpaceStatus;
import com.parq.web.PasswordChangeRequest;
import com.parq.web.ReportDateRangeFilter;
import com.parq.web.WebPaymentAccount;
import com.parq.web.WebUser;
import com.parq.web.UserRegistration;

public interface ParqWebService {

	WebUser validateUser(WebUser userToValidate);
	
	List<WebPaymentAccount> getPaymentAccountForUser(WebUser user);
	
	List<ParkingHistory> getParkingHistoryForUser(WebUser user, ReportDateRangeFilter dateFilter);
	
	boolean changePasswordForUser(WebUser user, PasswordChangeRequest passwordChange);
	
	List<ParkingSpaceStatus> getParkingStatusByClientId(long clientId);
	
	List<ParkingReport> getParkingReportByClientId(long clientId, ReportDateRangeFilter dateFilter);
	
	boolean registerNewUser(UserRegistration registration);
}
