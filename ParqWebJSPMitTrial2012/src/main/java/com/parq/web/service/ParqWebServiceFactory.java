package com.parq.web.service;

public final class ParqWebServiceFactory {
	
	// disable constructor
	private ParqWebServiceFactory(){}

	public static ParqWebService getParqWebServiceInstance() {
		return new MockParqWebService();
	}
}
