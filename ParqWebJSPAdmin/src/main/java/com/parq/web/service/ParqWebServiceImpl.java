package com.parq.web.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.parq.server.dao.ParkingLocationDao;
import com.parq.server.dao.ParkingStatusDao;
import com.parq.server.dao.model.object.ParkingInstance;
import com.parq.server.dao.model.object.ParkingLocation;
import com.parq.server.dao.model.object.ParkingSpace;
import com.parq.server.dao.model.object.Payment;
import com.parq.server.dao.model.object.Payment.PaymentType;
import com.parq.server.grid.GridManagementService;
import com.parq.server.grid.model.object.ParkingLocationWithFillRate;
import com.parq.web.model.WebParkingSpot;

public class ParqWebServiceImpl implements ParqWebService{
	
	@Override
	public List<WebParkingSpot> findParkingLocations() {
		
		List<WebParkingSpot> parkingLocations = new ArrayList<WebParkingSpot>();
		
		ParkingLocationDao geoDao = new ParkingLocationDao();
		
		// for the demo load all spots
		List<ParkingLocation> parkingStreet = geoDao.findCloseByParkingLocation(-180, 180, -180, 180);
		
		GridManagementService gms = GridManagementService.getInstance();
		ParkingStatusDao statusDao = new ParkingStatusDao();
		
		if (parkingStreet != null) {
			for (ParkingLocation street: parkingStreet) {
				List<Long> streetIds = new ArrayList<Long>();
				streetIds.add(street.getLocationId());
				ParkingLocationWithFillRate streetWithSpaces = 
					gms.getParkingLocationStatus(streetIds).get(0);
				for (ParkingSpace space : streetWithSpaces.getSpaces()) {
					// get the parking status for the parking space
					List<ParkingInstance> parkingStatus = 
							statusDao.getParkingStatusBySpaceIds(new long[] {space.getSpaceId()});
					// fill in the parking space information
					WebParkingSpot parkSpace = new WebParkingSpot();
					parkSpace.setSpotId(space.getSpaceId());
					parkSpace.setLatitude(space.getLatitude());
					parkSpace.setLongitude(space.getLongitude());
					parkSpace.setSpotName(space.getSpaceIdentifier());
					if (parkingStatus == null || parkingStatus.isEmpty()) {
						parkSpace.setAvailable(true);
					} else {
						parkSpace.setAvailable(
								parkingStatus.get(0).getParkingEndTime().getTime() < System.currentTimeMillis());
					}
					parkingLocations.add(parkSpace);
				}
			}
		}
		return parkingLocations;
	}

	@Override
	public boolean updateParkingStatus(long userId,
			List<WebParkingSpot> spotsWithStatus) {
		
		ParkingStatusDao statusDao = new ParkingStatusDao();
		
		//nowtime is after previous end time.  user was not parked.  
		Date end = new Date(); //end is iterations of increment + old time.  
		long msec = 1000*60*60; // park for 60 minute
		Date start = new Date();
		end.setTime(start.getTime()+msec);
		
		// setup a dummy payment during the MIT pilot
		Payment pilotPayment = new Payment();
		pilotPayment.setPaymentType(PaymentType.PrePaid);
		pilotPayment.setPaymentRefNumber("MIT_PILOT");
		pilotPayment.setPaymentDateTime(new Date(System.currentTimeMillis()));
		pilotPayment.setAmountPaidCents(0);
		pilotPayment.setAccountId(0L);

		for (WebParkingSpot spot: spotsWithStatus) {
			if (!spot.isAvailable()) {
				ParkingInstance newPark = new ParkingInstance();
				newPark.setPaidParking(true);
				newPark.setParkingBeganTime(start);
				newPark.setParkingEndTime(end);
				newPark.setUserId(userId);
				newPark.setSpaceId(spot.getSpotId());
				newPark.setPaymentInfo(pilotPayment);
				statusDao.addNewParkingAndPayment(newPark);
			} else if (spot.isAvailable()) {
				List<ParkingInstance> pi = statusDao.getParkingStatusBySpaceIds(new long[] {spot.getSpotId()});
				if (pi != null && !pi.isEmpty()) {
					statusDao.unparkBySpaceIdAndParkingRefNum(spot.getSpotId(), pi.get(0).getParkingRefNumber(), start);
				}
			}
		}
		return true;
	}
	
	
	
}
