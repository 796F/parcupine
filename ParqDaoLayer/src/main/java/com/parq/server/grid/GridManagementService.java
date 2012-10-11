package com.parq.server.grid;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.parq.server.dao.GridDao;
import com.parq.server.dao.model.object.GeoPoint;
import com.parq.server.dao.model.object.Grid;
import com.parq.server.dao.model.object.ParkingLocation;
import com.parq.server.dao.model.object.ParkingSpace;
import com.parq.server.dao.model.object.SimpleGrid;
import com.parq.server.grid.model.object.GridWithFillRate;
import com.parq.server.grid.model.object.ParkingLocationWithFillRate;
import com.parq.server.grid.model.object.SpaceExpirationEntry;

public class GridManagementService implements Runnable{
	
	private static GridManagementService gridManagementService;
	// keep track of the grid fill rate information
	private Map<Long, GridWithFillRate> gridInfoMap ;
	// keep track of the parking location fill rate information
	private Map<Long, ParkingLocationWithFillRate> locationInfoMap;
	// keep a spaceId to gridId reference for grid look up by spaceId
	private Map<Long, Long> spaceIdToGridIdMap;
	// keep a spaceId to locationId reference for location look up by spaceId
	private Map<Long, Long> spaceIdToParkingLocationIdMap;
	// the complete list of grids with parking location w/ rates inside.
	private List<GridWithFillRate> fullGridList;
	
	// this map keep track of spaceId to the ExpirationEntry
	private Map<Long, SpaceExpirationEntry> spaceIdToExpirationEntryMap;
	// priority queue based on expiration time
	private PriorityQueue<SpaceExpirationEntry> expireTimeQueue;
	
	private static ScheduledThreadPoolExecutor refreshTimer;
	// how often the timer refresh parking status in seconds
	private static final int parkingStatusUpdateRate = 30;
	
	// set how much to enlarge the search radius, when searching for streets within a grid
	// this value should be the size of at least 1 grid length or width
	// TODO set the value to the correct grid size value
	private static final double gridSearchEnlargement = 0.3;
	
	private static final boolean DEBUG_ENABLED = false;

	
	//singleton instance
	private GridManagementService() {
		loadGridData();
		loadLocationData();
		loadSpaceIdMapping();
		
		// initialized the spaceId to expiration entry map
		spaceIdToExpirationEntryMap = new HashMap<Long, SpaceExpirationEntry>();
		// initialized the parking space expiration queue
		expireTimeQueue = new PriorityQueue<SpaceExpirationEntry>(1000, 
				new SpaceExpirationEntry.SpaceExpirationTimeComparator());
	}
	
	private void loadSpaceIdMapping() {
		spaceIdToGridIdMap = new HashMap<Long, Long>();
		spaceIdToParkingLocationIdMap = new HashMap<Long, Long>();
		
		for (GridWithFillRate grid : gridInfoMap.values()) {
			for (ParkingLocation pl : grid.getParkingLocations()) {
				for (ParkingSpace space : pl.getSpaces()) {
					spaceIdToGridIdMap.put(space.getSpaceId(), grid.getGridId());
					spaceIdToParkingLocationIdMap.put(space.getSpaceId(), pl.getLocationId());
				}
			}
		}
	}

	public static GridManagementService getInstance(){
		if (gridManagementService == null) {
			gridManagementService = new GridManagementService();
			// creating the expiration check timer
			refreshTimer = new ScheduledThreadPoolExecutor(1);
			refreshTimer.scheduleWithFixedDelay(gridManagementService, parkingStatusUpdateRate, 
					parkingStatusUpdateRate, TimeUnit.SECONDS);
		}
		return gridManagementService;
	}
	
	private void loadGridData() {
		GridDao gridDao = new GridDao();
		List<Grid> gridList = gridDao.getAllCompleteGridHiarchy();
		this.gridInfoMap = new HashMap<Long, GridWithFillRate>();
		
		fullGridList = new ArrayList<GridWithFillRate>();
		
		// iterate each of the grid object, create the fillRate info
		// and put it into the hashmap.
		for (Grid grid : gridList) {
			GridWithFillRate gInfo = new GridWithFillRate(grid);
			// the number of space for the grid is the sum of all the
			// number of spaces for the parking locations in the grid
			int numSpaces = 0;
			for (ParkingLocation location : grid.getParkingLocations()) {
				numSpaces += location.getSpaces().size();
			}
			gInfo.setNumberOfSpaces(numSpaces);
			
			gridInfoMap.put(gInfo.getGridId(), gInfo);
			fullGridList.add(gInfo);
		}
	}
	
	private void loadLocationData() {
		locationInfoMap = new HashMap<Long, ParkingLocationWithFillRate>();
		for (GridWithFillRate grid : gridInfoMap.values()) {
			for (ParkingLocation pl : grid.getParkingLocations()) {
				ParkingLocationWithFillRate plInfo = new ParkingLocationWithFillRate(pl);
				plInfo.setNumberOfSpaces(pl.getSpaces().size());

				locationInfoMap.put(plInfo.getLocationId(), plInfo);
			}
		}
	}

	public void park(long spaceId, Date parkingEndTime) {
		
		long lastUpdatedDateTimeInSeconds = System.currentTimeMillis() / 1000;
		
		long gridId = spaceIdToGridIdMap.get(spaceId);
		GridWithFillRate grid = gridInfoMap.get(gridId);
		grid.park(spaceId);
		grid.setLastUpdatedDateTime(lastUpdatedDateTimeInSeconds);
		
		long locationId = spaceIdToParkingLocationIdMap.get(spaceId);
		ParkingLocationWithFillRate pl = locationInfoMap.get(locationId);
		pl.park(spaceId);
		pl.setLastUpdatedDateTime(lastUpdatedDateTimeInSeconds);
	
		// add the parking end time to the timer
		SpaceExpirationEntry expirationEntry = new SpaceExpirationEntry(spaceId, parkingEndTime);
		spaceIdToExpirationEntryMap.put(spaceId, expirationEntry);
		expireTimeQueue.add(expirationEntry);
		
	}
	
	public void unpark(long spaceId) {
		
		long lastUpdatedDateTimeInSeconds = System.currentTimeMillis() / 1000;
		
		long gridId = spaceIdToGridIdMap.get(spaceId);
		GridWithFillRate grid = gridInfoMap.get(gridId);
		grid.unPark(spaceId);
		grid.setLastUpdatedDateTime(lastUpdatedDateTimeInSeconds);
		
		long locationId = spaceIdToParkingLocationIdMap.get(spaceId);
		ParkingLocationWithFillRate pl = locationInfoMap.get(locationId);
		pl.unPark(spaceId);
		pl.setLastUpdatedDateTime(lastUpdatedDateTimeInSeconds);
		
		// remove the spaceId from the expiration map, and priority queue
		SpaceExpirationEntry see = 
			spaceIdToExpirationEntryMap.remove(spaceId);
		if (see != null) {
			expireTimeQueue.remove(see);
		}
	}
	
	public void refillParking(long spaceId, Date newParkingEndTime) {
		// Change the expiration time for the parking space by
		// 1) get the ExpirationEntry from the spaceId to expiration map
		// 2) update the priority queue
		SpaceExpirationEntry see = spaceIdToExpirationEntryMap.get(spaceId);
		if (see != null) {
			expireTimeQueue.remove(see);
			see.setParkingExiprationTime(newParkingEndTime);
			expireTimeQueue.add(see);
		}
	}
	
	public List<GridWithFillRate> getGridStatus(List<Long> gridIds) {
		
		List<GridWithFillRate> gridInfos
				= new ArrayList<GridWithFillRate>();
		for (long id : gridIds) {
			GridWithFillRate gridFillRate = gridInfoMap.get(id);
			gridInfos.add(gridFillRate);
		}
		return gridInfos;
	}
	
	public List<ParkingLocationWithFillRate> getParkingLocationStatus(List<Long> blockIds) {
		
		List<ParkingLocationWithFillRate> plInfos 
				= new ArrayList<ParkingLocationWithFillRate>();
		for (long id : blockIds) {
			ParkingLocationWithFillRate locationFillRate = locationInfoMap.get(id);
			plInfos.add(locationFillRate);
		}
		return plInfos;
	}
	
	public void updateParkingStatus() {
		if (!expireTimeQueue.isEmpty()) {
			Date curTime = new Date(System.currentTimeMillis());
			boolean parkingExpired = true;
			while (parkingExpired) {
				SpaceExpirationEntry see = expireTimeQueue.peek();
				
				// check to see there are items on the queue
				if (see == null) {
					parkingExpired = false;
				
					// check to see the parking entry if it has expired
				} else if (curTime.compareTo(see.getParkingExiprationTime()) > 0) {
					// parking has expired, unpark this parking space
					unpark(see.getSpaceId());
					expireTimeQueue.poll();
				} else {
					// parking has not expire, break out of the loop. 
					// No further element need to be check, 
					// because the PriorityQueue is a sorted list
					// by the expiration time
					parkingExpired = false;
				}
			}
		}
	}

	@Override
	public void run() {
		if (DEBUG_ENABLED) {
			System.out.println("Parking Status Update Triggered: " + new Date(System.currentTimeMillis()).toString());
		}
		updateParkingStatus();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		// stop the refreshTimer
		refreshTimer.shutdown();
	}

	public List<ParkingLocationWithFillRate> findStreetByGPSCoor(double northLatitude, double eastLongitude,
			double southLatitude, double westLongitude) {
		
		List<ParkingLocationWithFillRate> allLocationsWithinGPSCoor 
				= new ArrayList<ParkingLocationWithFillRate>();
		
		List<GridWithFillRate> grids = 	findGridWithFillrateByGPSCoor(
				northLatitude, eastLongitude, southLatitude, westLongitude);
		
		// iterate through each of the grids to get the parkingLocation list
		// using the parking location list to get the ParkingLocationWithFillRate
		// object
		for (GridWithFillRate grid: grids) {
			for (ParkingLocation pl: grid.getParkingLocations()) {
				ParkingLocationWithFillRate plfr = locationInfoMap.get(pl.getLocationId());
				// check the geo point of the street
				GeoPoint firstStreetCoor = plfr.getGeoPoints().get(0);
				GeoPoint lastStreeetCoor = plfr.getGeoPoints().get(plfr.getGeoPoints().size() - 1);
				// double check the geo points, only add street to the result list
				// if the first street coordinate is within the bounding box
				if (firstStreetCoor.getLatitude() >= southLatitude 
						&& firstStreetCoor.getLongitude() >= westLongitude 
						&& firstStreetCoor.getLatitude() <= northLatitude
						&& firstStreetCoor.getLongitude() <= eastLongitude) {
					allLocationsWithinGPSCoor.add(plfr);
					// or if the last geo point is inside the bounding box
				} else if (lastStreeetCoor.getLatitude() >= southLatitude 
						&& lastStreeetCoor.getLongitude() >= westLongitude
						&& lastStreeetCoor.getLatitude() <= northLatitude
						&& lastStreeetCoor.getLongitude() <= eastLongitude) {
					allLocationsWithinGPSCoor.add(plfr);
				}
			}
		}
		return allLocationsWithinGPSCoor;
	}

	public List<GridWithFillRate> findGridWithFillrateByGPSCoor(double northLatitude, double eastLongitude,
			double southLatitude, double westLongitude) {
		
		// since the grid gps coordinate is designated by the north east corner, we need
		// to do enlargement when the app viewing window is not center exactly inside the grid
		// however the enlargement should only need to apply to the top quadrant and 
		// the left side quadrant, since the bottom quadrant and right quadrant 
		// should be automatically enlarge when the view window pan right, or down
		double northLatitudeWithOffSet = northLatitude + gridSearchEnlargement;
		double eastLongitudeWithOffSet = eastLongitude + gridSearchEnlargement;
		double southLatitudeWithOffSet = southLatitude - gridSearchEnlargement;
		double westLongitudeWithOffSet = westLongitude - gridSearchEnlargement;
		
		GridDao gridDao = new GridDao();
		List<SimpleGrid> grids = gridDao.findSimpleGridNearBy(
				northLatitudeWithOffSet, eastLongitudeWithOffSet, 
				southLatitudeWithOffSet, westLongitudeWithOffSet);
		
		List<GridWithFillRate> allGridsWithinGPSCoor = new ArrayList<GridWithFillRate>();		
		for (SimpleGrid sGrid: grids) {
			GridWithFillRate fullGrid = gridInfoMap.get(sGrid.getGridId());
			allGridsWithinGPSCoor.add(fullGrid);
		}
		return allGridsWithinGPSCoor;
	}
	
}
