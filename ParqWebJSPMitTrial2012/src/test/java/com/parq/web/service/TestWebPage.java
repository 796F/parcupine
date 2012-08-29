package com.parq.web.service;

import java.util.List;

import junit.framework.TestCase;

import com.parq.web.model.ParkingSpaceStatus;

public class TestWebPage extends TestCase {
	
	public void testSomething() {
		ParqWebServiceImpl service = new ParqWebServiceImpl();
		List<ParkingSpaceStatus> pssList = service.getParkingStatusByClient(9, "AlGa");
		
		System.out.println(pssList);
	}

}
