package com.parq.web.service;

import java.util.List;

import com.parq.web.model.WebParkingSpot;

public interface ParqWebService {
	
	List<WebParkingSpot> findParkingLocations();
	
	boolean updateParkingStatus(long userId, List<WebParkingSpot> spotsWithStatus);
}
