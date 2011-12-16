package com.parq.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import com.parq.server.dao.model.object.ParkingLocation;
import com.parq.server.dao.model.object.Client;
import com.parq.server.dao.model.object.ParkingSpace;

/**
 * @author GZ
 *
 */
public class ClientDao extends AbstractParqDaoParent {

	/**
	 * Name of the local cache use by this dao
	 */
	private static final String cacheName = "ClientCache";
	private static Cache myCache;

	private static final String sqlGetClientByClientName = "SELECT client_id, name, address, client_desc FROM client WHERE name = ? AND is_deleted IS NOT TRUE";
	private static final String sqlGetClientByClientId = "SELECT client_id, name, address, client_desc FROM client WHERE client_id = ? AND is_deleted IS NOT TRUE";
	private static final String sqlGetAllSpacesByClientId = 
		"SELECT b.client_id, b.location_identifier, b.location_id, s.space_id, s.space_identifier, s.parking_level " + 
		" FROM parkinglocation AS b, parkingspace AS s " +
		" WHERE b.location_id = s.location_id " +
		" AND b.client_id = ?" +
		" AND b.is_deleted IS NOT TRUE " +
		" AND s.is_deleted IS NOT TRUE " +
		" ORDER BY b.location_id";

	private static final String clientNameCache = "getClientByClientName:";
	private static final String clientIdCache = "getClientByClientId:";
	private static final String getParkingLocationByClientIdCache = "getParkingLocationByClientId:";

	public ClientDao() {
		super();
		if (myCache == null) {
			// create the cache.
			myCache = setupCache(cacheName);
		}
	}

	public Client getClientByName(String name) {

		// the cache key for this method call;
		String cacheKey = clientNameCache + name;

		Client client = null;
		Element cacheEntry = myCache.get(cacheKey); 
		if (cacheEntry  != null) {
			client = (Client) cacheEntry.getValue();
			return client;
		}

		// query the DB for the user object
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetClientByClientName);
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();

			client = createClientObject(rs);

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}

		// put result into cache
		myCache.put(new Element(cacheKey, client));

		return client;
	}

	public Client getClientById(long id) {

		// the cache key for this method call;
		String cacheKey = clientIdCache + id;

		Client client = null;
		Element cacheEntry = myCache.get(cacheKey); 
		if (cacheEntry  != null) {
			client = (Client) cacheEntry.getValue();
			return client;
		}

		// query the DB for the user object
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetClientByClientId);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();

			client = createClientObject(rs);

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}

		// put result into cache
		myCache.put(new Element(cacheKey, client));

		return client;
	}

	@SuppressWarnings("unchecked")
	public List<ParkingLocation> getParkingLocationsAndSpacesByClientId(long clientId) {

		// the cache key for this method call;
		String cacheKey = getParkingLocationByClientIdCache + clientId;

		List<ParkingLocation> buildings = null;
		Element cacheEntry = myCache.get(cacheKey); 
		if (cacheEntry  != null) {
			buildings = (List<ParkingLocation>) cacheEntry.getValue();
			return buildings;
		}

		// query the DB for the user object
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetAllSpacesByClientId);
			pstmt.setLong(1, clientId);
			ResultSet rs = pstmt.executeQuery();

			buildings = createLocationsObject(rs);

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}

		// put result into cache
		myCache.put(new Element(cacheKey, buildings));

		return buildings;
	}

	private List<ParkingLocation> createLocationsObject(ResultSet rs)
			throws SQLException {
		if (rs == null || !rs.isBeforeFirst()) {
			return null;
		}
		List<ParkingLocation> locations = new ArrayList<ParkingLocation>();
		ParkingLocation curLocation = new ParkingLocation();

		while (rs.next()) {
			long buildingId = rs.getLong("location_id");

			// create new building whenever new row encountered.
			if (buildingId != curLocation.getLocationId()) {
				curLocation = new ParkingLocation();
				curLocation.setLocationId(buildingId);
				curLocation.setLocationIdentifier(rs.getString("location_identifier"));
				curLocation.setClientId(rs.getLong("client_id"));
				locations.add(curLocation);
			}

			ParkingSpace curSpace = new ParkingSpace();
			curSpace.setLocationId(buildingId);
			curSpace.setSpaceId(rs.getLong("space_id"));
			curSpace.setParkingLevel(rs.getString("parking_level"));
			curSpace.setSpaceIdentifier(rs.getString("space_identifier"));

			curLocation.getSpaces().add(curSpace);
		}
		// sort the list, so all the building are group together.
		Collections.sort(locations, new Comparator<ParkingLocation>() {
			@Override
			public int compare(ParkingLocation b1, ParkingLocation b2) {
				if (b1.getLocationIdentifier() == null && b2.getLocationIdentifier() == null) {
					return 0;
				} else if (b1.getLocationIdentifier() != null	&& b2.getLocationIdentifier() == null) {
					return 1;
				} else if (b1.getLocationIdentifier() == null	&& b2.getLocationIdentifier() != null) {
					return -1;
				} else {
					return b1.getLocationIdentifier().compareTo(b2.getLocationIdentifier());
				}
			}
		});
		return locations;
	}

	private Client createClientObject(ResultSet rs) throws SQLException {
		if (rs == null || !rs.isBeforeFirst()) {
			return null;
		}
		Client client = new Client();
		rs.first();
		client.setId(rs.getLong("client_id"));
		client.setName(rs.getString("name"));
		client.setAddress(rs.getString("address"));
		client.setClientDescription(rs.getString("client_desc"));
		return client;
	}
	
	/**
	 * manually clear out the cache
	 * @return
	 */
	public boolean clearUserCache() {
		myCache.removeAll();
		return true;
	}
}
