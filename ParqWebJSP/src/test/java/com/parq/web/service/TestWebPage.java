package com.parq.web.service;

import java.util.List;

import com.parq.server.dao.model.object.ParkingLocation;
import com.parq.web.model.ParkingSpaceStatus;

import junit.framework.TestCase;

public class TestWebPage extends TestCase {
	
	public void testSomething() {
		ParqWebServiceImpl service = new ParqWebServiceImpl();
		List<ParkingSpaceStatus> pssList = service.getParkingStatusByClient(9, "AlGa");
		
		System.out.println(pssList);
	}

}
