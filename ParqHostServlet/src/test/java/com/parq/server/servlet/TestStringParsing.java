package com.parq.server.servlet;

import junit.framework.TestCase;

public class TestStringParsing extends TestCase {

	String incomingUrl = "http://localhost:8080/p/c01/p00123";
	
	public void testStringParsing()
	{
		String[] stringParts = incomingUrl.split("/");
		if (! stringParts[stringParts.length - 3].equalsIgnoreCase("p"))
		{
			throw new IllegalStateException("Incoming URL is malformed");
		}
		
		String clientId = stringParts[stringParts.length - 2];
		String parkingSpaceId = stringParts[stringParts.length - 2];
		
		
		System.out.println("Client Id: " + clientId);
		System.out.println("Parking Space Id: " + parkingSpaceId);
	}
}
