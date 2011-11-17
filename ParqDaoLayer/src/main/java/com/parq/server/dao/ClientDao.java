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

public class ClientDao extends AbstractParqDaoParent {

	/**
	 * Name of the local cache use by this dao
	 */
	private static final String cacheName = "ClientCache";
	private static Cache myCache;

	private static final String sqlGetClientByClientName = "SELECT client_id, name, address, client_desc FROM client WHERE name = ? AND Is_Deleted IS NOT TRUE";
	private static final String sqlGetClientByClientId = "SELECT client_id, name, address, client_desc FROM client WHERE client_id = ? AND Is_Deleted IS NOT TRUE";
	private static final String sqlGetAllSpacesByClientId = 
		"SELECT B.Client_ID, B.Location_Identifier, B.Location_ID, S.Space_ID, S.Space_Identifier, S.Parking_Level " + 
		" FROM ParkingLocation AS B, ParkingSpace AS S " +
		" WHERE B.Location_ID = S.Location_ID " +
		" AND B.Client_ID = ?" +
		" AND B.Is_Deleted IS NOT TRUE " +
		" AND S.Is_Deleted IS NOT TRUE " +
		" ORDER BY B.Location_ID";

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
		if (myCache.get(cacheKey) != null) {
			client = (Client) myCache.get(cacheKey).getValue();
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

	public Client getClientById(int id) {

		// the cache key for this method call;
		String cacheKey = clientIdCache + id;

		Client client = null;
		if (myCache.get(cacheKey) != null) {
			client = (Client) myCache.get(cacheKey).getValue();
			return client;
		}

		// query the DB for the user object
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetClientByClientId);
			pstmt.setInt(1, id);
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
	public List<ParkingLocation> getParkingLocationsAndSpacesByClientId(int clientId) {

		// the cache key for this method call;
		String cacheKey = getParkingLocationByClientIdCache + clientId;

		List<ParkingLocation> buildings = null;
		if (myCache.get(cacheKey) != null) {
			buildings = (List<ParkingLocation>) myCache.get(cacheKey).getValue();
			return buildings;
		}

		// query the DB for the user object
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetAllSpacesByClientId);
			pstmt.setInt(1, clientId);
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
		List<ParkingLocation> buildings = new ArrayList<ParkingLocation>();
		ParkingLocation curLocation = new ParkingLocation();

		while (rs.next()) {
			int buildingId = rs.getInt("Location_ID");

			// create new building whenever new row encountered.
			if (buildingId != curLocation.getLocationId()) {
				curLocation = new ParkingLocation();
				curLocation.setLocationId(buildingId);
				curLocation.setLocationIdentifier(rs.getString("Location_Identifier"));
				curLocation.setClientId(rs.getInt("Client_ID"));
				buildings.add(curLocation);
			}

			ParkingSpace curSpace = new ParkingSpace();
			curSpace.setLocationId(buildingId);
			curSpace.setSpaceId(rs.getInt("Space_ID"));
			curSpace.setParkingLevel(rs.getString("Parking_Level"));
			curSpace.setSpaceIdentifier(rs.getString("Space_Identifier"));

			curLocation.getSpaces().add(curSpace);
		}
		// sort the list, so all the building are group together.
		Collections.sort(buildings, new Comparator<ParkingLocation>() {
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
		return buildings;
	}

	private Client createClientObject(ResultSet rs) throws SQLException {
		if (rs == null || !rs.isBeforeFirst()) {
			return null;
		}
		Client client = new Client();
		rs.first();
		client.setId(rs.getInt("client_id"));
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
