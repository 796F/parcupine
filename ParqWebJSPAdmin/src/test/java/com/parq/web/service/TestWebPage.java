package com.parq.web.service;

import java.util.ArrayList;
import java.util.List;

import com.parq.server.dao.ParkingStatusDao;
import com.parq.server.dao.model.object.ParkingInstance;
import com.parq.web.model.WebParkingSpot;

import junit.framework.TestCase;

public class TestWebPage extends TestCase {
	
	public void testDoNothing() {}

	public void _testUpdateParkingStatus() {
		ParqWebService service = new ParqWebServiceImpl();
		
		long spot_id = 111;
		
		List<WebParkingSpot> spots = new ArrayList<WebParkingSpot>();
		WebParkingSpot spot = new WebParkingSpot();
		spot.setAvailable(false);
		spot.setSpotId(spot_id);
		spots.add(spot);
		
		service.updateParkingStatus(1L, spots);
		
		ParkingStatusDao statusDao = new ParkingStatusDao();
		List<ParkingInstance> pi = statusDao.getParkingStatusBySpaceIds(new long[]{spot_id});
		assertNotNull(pi);
		assertFalse(pi.isEmpty());
		assertEquals(spot_id, pi.get(0).getSpaceId());
		assertTrue(pi.get(0).getParkingEndTime().getTime() > System.currentTimeMillis() + 1000*59*60);
		assertTrue(pi.get(0).getParkingEndTime().getTime() < System.currentTimeMillis() + 1000*61*60);
		
		spot.setAvailable(true);
		service.updateParkingStatus(1L, spots);
		
		pi = statusDao.getParkingStatusBySpaceIds(new long[]{spot_id});
		assertNotNull(pi);
		assertFalse(pi.isEmpty());
		assertEquals(spot_id, pi.get(0).getSpaceId());
		assertTrue(pi.get(0).getParkingEndTime().getTime() <= System.currentTimeMillis());
	}
	
}
