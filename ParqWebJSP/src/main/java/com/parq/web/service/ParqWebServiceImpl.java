package com.parq.web.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.parq.server.dao.AdminDao;
import com.parq.server.dao.AdminReportDao;
import com.parq.server.dao.ClientDao;
import com.parq.server.dao.ParkingLocationDao;
import com.parq.server.dao.ParkingStatusDao;
import com.parq.server.dao.PaymentAccountDao;
import com.parq.server.dao.UserDao;
import com.parq.server.dao.exception.DuplicateEmailException;
import com.parq.server.dao.model.object.Admin;
import com.parq.server.dao.model.object.GeoPoint;
import com.parq.server.dao.model.object.ParkingInstance;
import com.parq.server.dao.model.object.ParkingLocation;
import com.parq.server.dao.model.object.ParkingLocationUsageReport;
import com.parq.server.dao.model.object.ParkingSpace;
import com.parq.server.dao.model.object.PaymentAccount;
import com.parq.server.dao.model.object.PaymentMethod;
import com.parq.server.dao.model.object.User;
import com.parq.server.dao.model.object.UserPaymentReport;
import com.parq.server.dao.model.object.UserPaymentReport.UserPaymentEntry;
import com.parq.web.PasswordChangeRequest;
import com.parq.web.ReportDateRangeFilter;
import com.parq.web.model.MapLocation;
import com.parq.web.model.ParkingHistory;
import com.parq.web.model.ParkingReport;
import com.parq.web.model.ParkingSpaceStatus;
import com.parq.web.model.UserRegistration;
import com.parq.web.model.WebClient;
import com.parq.web.model.WebGeoPoint;
import com.parq.web.model.WebParkingLocation;
import com.parq.web.model.WebPaymentAccount;
import com.parq.web.model.WebUser;

public class ParqWebServiceImpl implements ParqWebService{
	private static SimpleDateFormat dateFormatter;
	private static SimpleDateFormat hourFormatter;
	public ParqWebServiceImpl() {
		
		if (dateFormatter == null) {
			dateFormatter =
					new SimpleDateFormat("MMM dd yyyy");
		}
		if (hourFormatter == null) {
			hourFormatter =
					new SimpleDateFormat("hh:mm:ss a");
		}
	}
	
	
	@Override
	public WebUser validateUser(WebUser userToValidate) {
		
		boolean isValidUser = false;
		
		UserDao userDao = new UserDao();
		User user = userDao.getUserByEmail(userToValidate.getUsername());
		if (user != null && userToValidate.getPassword().equals(user.getPassword())) {
			isValidUser = true;
			userToValidate.setAuthenticated(true);
			userToValidate.setAdminUser(false);
			
			// set the id for the user
			userToValidate.setId(user.getUserID());
		}
		
		if (!isValidUser) {
			AdminDao adminDao = new AdminDao();
			Admin admin = adminDao.getAdminByEmail(userToValidate.getUsername());
			if (admin != null && admin.getPassword().equals(userToValidate.getPassword())) {
				isValidUser = true;
				userToValidate.setAuthenticated(true);
				userToValidate.setAdminUser(true);
				
				// set the id for the admin
				userToValidate.setId(admin.getAdminId());
			}
		}
		
		if (!isValidUser) {
			userToValidate.setAuthenticated(false);
			userToValidate.setAdminUser(false);
			
			// let the webpage know that the user login has failed
			userToValidate.setLoginFailed(true);
		}
		
		return userToValidate;
	}
	
	@Override
	public List<WebPaymentAccount> getPaymentAccountForUser(WebUser user) {
		List<WebPaymentAccount> accs = new ArrayList<WebPaymentAccount>();
		
		// validate the user to make sure it is a user, not a admin
		if (!user.isAuthenticated() || user.isAdminUser()) {
			user.setAuthenticated(false);
			user.setAdminUser(false);
			return null;
		}
		
		PaymentAccountDao dao = new PaymentAccountDao();
		List<PaymentAccount> accounts = dao.getAllPaymentMethodForUser(user.getId());
		
		if (accounts != null && !accounts.isEmpty()) {
			for (PaymentAccount ac : accounts) {
				WebPaymentAccount wpa = new WebPaymentAccount();
				wpa.setCreditCardNumber(ac.getCcStub());
				wpa.setDefaultPayment(ac.isDefaultPaymentMethod());
				if (ac.getCardType() != null) {
					wpa.setType(ac.getCardType().name());
				}
				else {
					wpa.setType("Unknown");
				}
				
				accs.add(wpa);
			}
		}
		
		return accs;
	}
	
	@Override
	public List<ParkingHistory> getParkingHistoryForUser(WebUser user, ReportDateRangeFilter dateFilter) {
		
		List<ParkingHistory> pHistories = new ArrayList<ParkingHistory>();
		
		if (dateFilter.getDateRangeFilter() == null 
				|| ReportDateRangeFilter.DateRange.NONE.toString()
						.equalsIgnoreCase(dateFilter.getDateRangeFilter())) {
			return pHistories;
		}
		
		AdminReportDao reportDao = new AdminReportDao();
		
		// the below value is set to daterange.this_month
		Calendar reportStartDate = Calendar.getInstance();
		Calendar reportEndDate = Calendar.getInstance();
		int month = reportStartDate.get(Calendar.MONTH);
		reportStartDate.set(Calendar.DATE, 1);
		reportStartDate.set(Calendar.HOUR, 0);
		reportStartDate.set(Calendar.AM_PM, Calendar.AM);
		reportStartDate.set(Calendar.MINUTE, 0);
		reportStartDate.set(Calendar.SECOND, 0);
		
		
		if (ReportDateRangeFilter.DateRange.LAST_MONTH.toString().equals(dateFilter.getDateRangeFilter())) {
			reportEndDate.setTime(reportStartDate.getTime());
			reportStartDate.add(Calendar.MONTH, -1);
		}
		else if (ReportDateRangeFilter.DateRange.LAST_THREE_MONTH.toString().equals(dateFilter.getDateRangeFilter())) {
			reportEndDate.setTime(reportStartDate.getTime());			
			reportStartDate.set(Calendar.MONTH, month - 3);
		}
		
		UserPaymentReport report = reportDao.getConsolidatedUserParkingReport(
				user.getId(), reportStartDate.getTime(), reportEndDate.getTime());
		
        if (report != null) {
			for (UserPaymentEntry payEntry : report.getPaymentEntries()) {
				ParkingHistory pHistory = new ParkingHistory();
				pHistory.setCost(payEntry.getAmountPaidCents() / 100.00);
				
				pHistory.setDate(dateFormatter.format(payEntry.getParkingBeganTime()));
				pHistory.setStartTime(hourFormatter.format(payEntry.getParkingBeganTime()));
				pHistory.setEndTime(hourFormatter.format(payEntry.getParkingEndTime()));
				
				pHistory.setLocationName(payEntry.getLocationName());
				pHistory.setParkingRefNumber(payEntry.getParkingRefNumber());
	
				pHistories.add(pHistory);
			}
			
			Collections.sort(pHistories, new Comparator<ParkingHistory>() {
				@Override
				public int compare(ParkingHistory o1, ParkingHistory o2) {
					return o1.getDate().compareTo(o2.getDate());
				}
			});
		}
		return pHistories;
	}
	
	@Override
	public boolean changePasswordForUser(WebUser user, PasswordChangeRequest passwordChange) {

		boolean isValidPassword = true;
		
		// validate the user to make sure it is a user, not a admin
		if (!user.isAuthenticated() || user.isAdminUser()) {
			user.setAuthenticated(false);
			user.setAdminUser(false);
			return false;
		}
		
		User dbUser = null;
		UserDao userDao = null;
		
		if (!validatePassword(passwordChange.getNewPassword1())) {
			passwordChange.setInvalidPassword(true);
			isValidPassword = false;
		} else if(!passwordChange.getNewPassword1().equals(passwordChange.getNewPassword2())) {
			passwordChange.setNewPasswordNotMatch(true);
			isValidPassword = false;
		} else {
			userDao = new UserDao();
			dbUser = userDao.getUserByEmail(user.getUsername());
			
			if (!dbUser.getPassword().equals(passwordChange.getOldPassword())) {
				passwordChange.setWrongOldPassword(true);
				isValidPassword = false;
			}
		}
		
		// once the new password checks out, we can then call 
		// the DB to change the user's password
		if (isValidPassword) {
			dbUser.setPassword(passwordChange.getNewPassword1());
			isValidPassword = userDao.updateUser(dbUser);
		}
		return isValidPassword;
	}
	
	// helper method to check that the new password user want to use is valid
	private boolean validatePassword(String newPassword) {
		boolean isValidPassword = true;
		
		if (newPassword == null	|| newPassword.isEmpty() || newPassword.length() < 6) {
			isValidPassword = false;
		}
		return isValidPassword;
	}

	@Override
	public List<String> getParkingLocationIdentifierListForClient(long clientId) {
		if (clientId < 0) {
			return Collections.emptyList();
		}
		
		List<ParkingLocation> parkingLocations = getParkingLocationsByClientId(clientId);
		List<String> parkingLocationIdentifiers = new ArrayList<String>();
		for (ParkingLocation pl: parkingLocations) {
			parkingLocationIdentifiers.add(pl.getLocationIdentifier());
		}
		
		return parkingLocationIdentifiers;
	}
	
	@Override
	public WebClient getClientByAdminId(long adminId) {
		AdminDao adminDao = new AdminDao();
		Admin admin = adminDao.getAdminById(adminId);
		WebClient webClient = new WebClient();
		webClient.setClientId(admin.getClientId());
		return webClient;
	}
	
	private List<ParkingLocation> getParkingLocationsByClientId(long clientId) {
		if (clientId < 0) {
			return Collections.emptyList();
		}
		
		ClientDao clientDao = new ClientDao();
		// create a list of parking locations for this client
		List<ParkingLocation> parkingLocations = clientDao.getParkingLocationsAndSpacesByClientId(clientId);
		return parkingLocations;
	}
	
	@Override
	public List<ParkingSpaceStatus> getParkingStatusByClient(long clientId, String locationIdentifier) {
		List<ParkingSpaceStatus> pStatus = new ArrayList<ParkingSpaceStatus>();
		if (clientId < 0) {
			return pStatus;
		}
		
		// create a list of spaceIds for this client
		List<ParkingLocation> parkingLocations = getParkingLocationsByClientId(clientId);
		List<Long> spaceIdList = new ArrayList<Long>();
		
		ParkingStatusDao parkingStatusDao = new ParkingStatusDao();
		
		if (parkingLocations != null) {
			for (ParkingLocation pl : parkingLocations) {
				// filter the parking location to bring back, if the user provide a location identifier
				if (locationIdentifier == null || pl.getLocationIdentifier().equalsIgnoreCase(locationIdentifier)) {
					for (ParkingSpace ps: pl.getSpaces()) {
						spaceIdList.add(ps.getSpaceId());
						
						ParkingSpaceStatus status = new ParkingSpaceStatus();
						status.setParkingLevel(ps.getParkingLevel());
						status.setSpaceId(ps.getSpaceId());
						status.setSpaceLocation(pl.getLocationIdentifier());
						status.setSpaceName(ps.getSpaceName());
						status.setSpaceIdentifier(ps.getSpaceIdentifier());
						pStatus.add(status);
					}
				}
			}
			// is there a better way to convert a list into a primitive array?
			long[] spaceIds = new long[spaceIdList.size()];
			for (int i = 0; i < spaceIdList.size(); i++) {
				spaceIds[i] = spaceIdList.get(i);
			}
			List<ParkingInstance> spaceStats = parkingStatusDao.getParkingStatusBySpaceIds(spaceIds);
			
			if (spaceStats != null && pStatus != null) {
				// sort the parking status list so that the space id is in ascending order
				Collections.sort(pStatus, new Comparator<ParkingSpaceStatus>() {
					@Override
					public int compare(ParkingSpaceStatus o1, ParkingSpaceStatus o2) {
							return (int)(o1.getSpaceId() - o2.getSpaceId());
					}
				});
				
				// sort the parking status list os the space id is in ascending order
				Collections.sort(spaceStats, new Comparator<ParkingInstance>() {
					@Override
					public int compare(ParkingInstance o1, ParkingInstance o2) {
							return (int)(o1.getSpaceId() - o2.getSpaceId());
					}
				});
				
				
				// match the parking space 
				long curTime = System.currentTimeMillis();
				for (int i = 0, j = 0; i < pStatus.size(); i++, j++ ) {
					// if we are at the end of the ParkingInstance list, then
					// all the remaining spaces are unoccupied
					if (j < 0 || j >= spaceStats.size()) {
						break;
					}
					
					// attempt to match the ParkingSpaceStatus with the ParkingInstance
					ParkingSpaceStatus status = pStatus.get(i);
					ParkingInstance pi = spaceStats.get(j);
					if (status.getSpaceId() != pi.getSpaceId()) {
						// there is no status for the current space, so we
						// move on to the next space to match the parking instance info
						j--;
					} else {
						status.setOccupied(curTime <= pi.getParkingEndTime().getTime());
						if (status.isOccupied()) {
							status.setSpaceFreeOn(hourFormatter.format(pi.getParkingEndTime().getTime()));
						}
					}
				}
			}
		}
		
		Collections.sort(pStatus, new Comparator<ParkingSpaceStatus>() {
			@Override
			public int compare(ParkingSpaceStatus o1, ParkingSpaceStatus o2) {
				return (int) (o1.getSpaceId() - o2.getSpaceId());
			}
		});
		
		return pStatus;
	}
	
	@Override
	public List<ParkingReport> getParkingReportByClientId(long clientId, ReportDateRangeFilter dateFilter) {
		
		List<ParkingReport> reports = new ArrayList<ParkingReport>();
		
		if (dateFilter.getDateRangeFilter() == null 
				|| ReportDateRangeFilter.DateRange.NONE.toString()
						.equalsIgnoreCase(dateFilter.getDateRangeFilter())) {
			return reports;
		}
		
		AdminReportDao reportDao = new AdminReportDao();
		ClientDao clientDao = new ClientDao();
		
		// the below value is set to daterange.this_month
		Calendar reportStartDate = Calendar.getInstance();
		Calendar reportEndDate = Calendar.getInstance();
		int month = reportStartDate.get(Calendar.MONTH);
		reportStartDate.set(Calendar.DATE, 1);
		reportStartDate.set(Calendar.HOUR, 0);
		reportStartDate.set(Calendar.AM_PM, Calendar.AM);
		reportStartDate.set(Calendar.MINUTE, 0);
		reportStartDate.set(Calendar.SECOND, 0);
		
		
		if (ReportDateRangeFilter.DateRange.LAST_MONTH.toString().equals(dateFilter.getDateRangeFilter())) {
			reportEndDate.setTime(reportStartDate.getTime());
			reportStartDate.add(Calendar.MONTH, -1);
		}
		else if (ReportDateRangeFilter.DateRange.LAST_THREE_MONTH.toString().equals(dateFilter.getDateRangeFilter())) {
			reportEndDate.setTime(reportStartDate.getTime());			
			reportStartDate.set(Calendar.MONTH, month - 3);
		}
		
		List<ParkingLocation> parkingLocations = clientDao
				.getParkingLocationsAndSpacesByClientId(clientId);
		if (parkingLocations != null && !parkingLocations.isEmpty()) {
			for (int i = 0; i < parkingLocations.size(); i++) {
				
				ParkingLocationUsageReport usageReports = reportDao
						.getParkingLocationUsageReport(parkingLocations.get(i)
								.getLocationId(), reportStartDate.getTime(),
								reportEndDate.getTime());
				if (usageReports != null) {
					for (ParkingLocationUsageReport.ParkingLocationUsageEntry 
							ur : usageReports.getUsageReportEntries()) {
						ParkingReport pr = new ParkingReport();
						pr.setParkingRefNum(ur.getParkingRefNumber());
						pr.setLocationIdentifier(ur.getLocationIdentifier());
						pr.setSpaceIdentifier(ur.getSpaceIdentifier());
						pr.setPaymentDatetime(dateFormatter.format(ur.getParkingBeganTime()));
						pr.setUserEmail(ur.getUserEmail());
						pr.setParkingStartTime(hourFormatter.format(ur.getParkingBeganTime()));
						pr.setParkingEndTime(hourFormatter.format(ur.getParkingEndTime()));
						
						reports.add(pr);
					}
				}
			}
		}
		
		Collections.sort(reports, new Comparator<ParkingReport>() {
			@Override
			public int compare(ParkingReport o1, ParkingReport o2) {
				return o1.getPaymentDatetime().compareTo(o2.getPaymentDatetime());
			}
		});

		return reports;
	}
	
	@Override
	public boolean registerNewUser(UserRegistration registration) {

		if (!validatePassword(registration.getPassword1())) {
			registration.setInvalidPassword(true);
			return false;
		} else if ( !registration.getPassword1().equals(registration.getPassword2())) {
			registration.setPasswordDoesNotMatch(true);
			return false;
		}
		
		UserDao userDao = new UserDao();
		boolean userCreationSuccessful = false;
		
		try {
			User user = new User();
			user.setEmail(registration.getEmail());
			user.setPassword(registration.getPassword1());
			user.setPhoneNumber("000-000-0000");
			
			// set user type to be normal for user
			user.setAccountType(PaymentMethod.NORMAL);
			
			userCreationSuccessful = userDao.createNewUser(user);	
		} catch (DuplicateEmailException dee) {
			registration.setEmailAlreadyExist(true);
			userCreationSuccessful = false;
		}
		
		if (userCreationSuccessful) {
			registration.setPasswordDoesNotMatch(false);
			registration.setInvalidPassword(false);
			registration.setEmailAlreadyExist(false);

		}

		return userCreationSuccessful;
	}

	@Override
	public List<WebParkingLocation> findParkingLocations(MapLocation centerOfMap) {
		
		List<WebParkingLocation> parkingLocations = new ArrayList<WebParkingLocation>();
		
		ParkingLocationDao geoDao = new ParkingLocationDao();
		double precision = 0.05; // this is 5.5km search radius
		double centerPointLat = centerOfMap.getLatitude();
		double centerPointLong = centerOfMap.getLongitude();
		
		// round the lat and long to 3 decimal place, to improve the dao layer cache
		// Efficiency
		centerPointLat = ((int) (centerPointLat * 100)) / 100.00;
		centerPointLong = ((int) (centerPointLong * 100)) / 100.00;
		
		
		List<ParkingLocation> geoLocations = geoDao.findCloseByParkingLocation(
				centerPointLat - precision, centerPointLat + precision, 
				centerPointLong - precision, centerPointLong + precision);
		
		if (geoLocations != null) {
			for (ParkingLocation parkingLoc: geoLocations) {
				WebParkingLocation parkLoc = new WebParkingLocation();
				WebGeoPoint webGeoPoint = getCenterGeoLocation(parkingLoc);
				parkLoc.setLatitude(webGeoPoint.getLatitude());
				parkLoc.setLongitude(webGeoPoint.getLongitude());
				parkLoc.setLocationName(parkingLoc.getLocationIdentifier());

				parkingLocations.add(parkLoc);
			}
		}
		return parkingLocations;
	}


	private WebGeoPoint getCenterGeoLocation(ParkingLocation parkingLoc) {
		double avgLat = 0.0;
		double avgLong = 0.0;
		
		for (GeoPoint gp : parkingLoc.getGeoPoints()) {
			avgLat += gp.getLatitude();
			avgLong += gp.getLongitude();
		}
		
		avgLat = avgLat / parkingLoc.getGeoPoints().size();
		avgLong = avgLong / parkingLoc.getGeoPoints().size();
		
		return new WebGeoPoint(avgLat, avgLong);
	}
}
