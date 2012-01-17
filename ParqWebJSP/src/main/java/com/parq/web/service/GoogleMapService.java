package com.parq.web.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.parq.web.MapLocation;

public class GoogleMapService {
	
	private GoogleMapService(){}

	public static MapLocation getMapLocationForLocationName(String userInputValue) {
		
		String parsedUserInput = parseUserInput(userInputValue);
		
		MapLocation mapLocation = new MapLocation();
		BufferedReader in = null;
		try {
			boolean statusOk = false;
			
			URL google = new URL("https://maps.googleapis.com/maps/api/geocode/xml?address=" 
					+ parsedUserInput + "&sensor=false");
			URLConnection yc = google.openConnection();
			in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			String inputLine;
			StringBuffer stringBuff = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				stringBuff.append(inputLine);
				if (!statusOk && inputLine.trim().contains("<status>OK</status>")) {
					statusOk = true;
				}
			}
			
			if (statusOk) {
				processMapResult(stringBuff.toString(), mapLocation);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		
		return mapLocation;
	}

	private static String parseUserInput(String userInputValue) {
		String[] stringSegments = userInputValue.split(" ");
		
		StringBuffer parsedString = new StringBuffer();
		for (int i = 0 ; i < stringSegments.length - 1; i++) {
			parsedString.append(stringSegments[i]);
			parsedString.append("+");
		}
		// fence post problem:
		if (stringSegments.length > 0) {
			parsedString.append(stringSegments[stringSegments.length-1]);
		}
		else {
			parsedString.append(userInputValue);
		}
		return parsedString.toString();
	}

	private static void processMapResult(String xmlString, MapLocation mapLocation) {
		String[] stringSegments = xmlString.split("<lat>");
		
		if (stringSegments.length > 0) {
			String latLongSegment = stringSegments[1];
			String strLat = latLongSegment.split("</lat>")[0];
			String strLong = latLongSegment.split("<lng>")[1].split("</lng>")[0];
			
			// System.out.println("Latitude: " + strLat);
			// System.out.println("Longitude: " + strLong);
			double dLat = new Double(strLat);
			double dLong = new Double(strLong);
			mapLocation.setLatitude(dLat);
			mapLocation.setLongitude(dLong);
		}
		
	}
}
