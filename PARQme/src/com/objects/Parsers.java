package com.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

public class Parsers {
	// User parameters
	private static final String PARAM_UID = "uid";
	private static final String PARAM_PARKSTATE = "parkState";

	// Response code parameter
	private static final String PARAM_RESP_CODE = "resp";
	private static final String RESP_CODE_OK = "OK";

	// Park rate parameters
	private static final String PARAM_LAT = "lat";
	private static final String PARAM_LON = "lon";
	private static final String PARAM_SPOT_ID = "spotId";
	private static final String PARAM_MIN_TIME = "minTime";
	private static final String PARAM_MAX_TIME = "maxTime";
	private static final String PARAM_DEFAULT_RATE = "defaultRate";
	private static final String PARAM_MIN_INCREMENT = "minIncrement";
	private static final String PARAM_DESCRIPTION = "location";

	private static final String PARAM_PARK_REFERENCE = "parkingReferenceNumber";
	private static final String PARAM_END_TIME = "endTime";
	
	private static final String PARAM_PARK_SYNC = "sync";
	private static final String PARAM_RATE_OBJECT = "rateObject";

	private static final String PARAM_LOCATION_LIST = "locationList";
	private static final String PARAM_SPOT_NAME = "spot";
	
	private static final String PARAM_CC_STUB= "creditCardStub";
	private static final String PARAM_SPOT_NUMBER = "spotNumber";
	// {"fname":"xia@umd.edu","lname":"Mikey","phone":"1337"}
	public static UserObject parseUser(JsonParser jp) throws IOException {
		long uid = -1;
		ParkSync sync = null;
		boolean parkState = false;
		String creditCardStub = "";
		JsonToken t = jp.nextToken();
		String curr;
		while (t != null && t != JsonToken.END_OBJECT) {
			if (t == JsonToken.VALUE_NUMBER_INT) {
				curr = jp.getCurrentName();
				if (PARAM_UID.equals(curr)) {
					uid = jp.getIntValue();
				} else if (PARAM_PARKSTATE.equals(curr)) {
					parkState = jp.getIntValue() == 1;
				}
			}
			if(t==JsonToken.VALUE_STRING){
				curr = jp.getCurrentName();
				if(PARAM_CC_STUB.equals(curr)){
					creditCardStub = jp.getText();
				}
			}
			if(t==JsonToken.START_OBJECT){
				curr = jp.getCurrentName();
				if(PARAM_PARK_SYNC.equals(curr)){

					long endTime = 0; //how much to set timer
					int minTime=0;			//must park 1 hour
					int maxTime=0;			//max park 3 hours
					int defaultRate=0; 		//rate is x/increment
					int minIncrement=0;		//min increase 30mins

					//THE FOLLOWING ARE FOR REFILLING/UNPARKING
					String parkingReferenceNumber=""; 
					double lat=0;
					double lon=0;
					long spotId=0;
					String description ="";
					String spotNumber ="";
					
					JsonToken tt = jp.nextToken();
					String current;

					while (tt != null && tt != JsonToken.END_OBJECT) {
						current = jp.getCurrentName();
						switch (tt) {
						case VALUE_NUMBER_INT: {
							if (PARAM_SPOT_ID.equals(current)) {
								spotId = jp.getLongValue();
							} else if (PARAM_MIN_TIME.equals(current)) {
								minTime = jp.getIntValue();
							} else if (PARAM_MAX_TIME.equals(current)) {
								maxTime = jp.getIntValue();
							} else if (PARAM_DEFAULT_RATE.equals(current)) {
								defaultRate = jp.getIntValue();
							} else if (PARAM_MIN_INCREMENT.equals(current)) {
								minIncrement = jp.getIntValue();
							} else if (PARAM_END_TIME.equals(current)){
								endTime = jp.getLongValue();
							}
						}
						case VALUE_NUMBER_FLOAT: {
							if (PARAM_LAT.equals(current)) {
								lat = jp.getDoubleValue();
							} else if (PARAM_LON.equals(current)) {
								lon = jp.getDoubleValue();
							}
						}
						case VALUE_STRING: {
							if (PARAM_DESCRIPTION.equals(current)) {
								description = jp.getText();
							} else if (PARAM_PARK_REFERENCE.equals(current)){
								parkingReferenceNumber = jp.getText();
							} else if (PARAM_SPOT_NUMBER.equals(current)){
								spotNumber = jp.getText();
							}
						}
						}
						tt = jp.nextToken();
					}

					sync = new ParkSync(endTime, minTime, maxTime, defaultRate, minIncrement, parkingReferenceNumber, lat, lon, spotId, description, spotNumber);

				}
			}
			t = jp.nextToken();
		}
		return new UserObject(uid, parkState, sync, creditCardStub);
	}

	public static boolean parseResponseCode(JsonParser jp) throws IOException {
		JsonToken t = jp.nextToken();
		String curr;
		while (t != null && t != JsonToken.END_OBJECT) {
			if (t == JsonToken.VALUE_STRING) {
				curr = jp.getCurrentName();
				if (PARAM_RESP_CODE.equals(curr)) {
					String test = jp.getText();
					String jj = RESP_CODE_OK;
					return RESP_CODE_OK.equals(jp.getText());
				}
			}
			t = jp.nextToken();
		}
		return false;
	}
	

	public static ParkInstanceObject parseParkInstance(JsonParser jp) throws IOException {
		long endTime = 0;
		String parkingReferenceNumber = "";
		ParkSync sync = null;
		JsonToken t = jp.nextToken();
		String curr;
		String resp = "";
		while (t != null && t != JsonToken.END_OBJECT) {
			curr = jp.getCurrentName();
			if (t == JsonToken.VALUE_NUMBER_INT) {
				if (PARAM_END_TIME.equals(curr)) {
					endTime = jp.getLongValue();
				}
			}
			if(t == JsonToken.VALUE_STRING){
				if (PARAM_PARK_REFERENCE.equals(curr)) {
					parkingReferenceNumber = jp.getText();
				}else if(PARAM_RESP_CODE.equals(curr)){
					resp = jp.getText();
				}
			}
			if(t==JsonToken.START_OBJECT){
				curr = jp.getCurrentName();
				if(PARAM_PARK_SYNC.equals(curr)){

					long syncEndTime = 0; //how much to set timer
					int minTime=0;			//must park 1 hour
					int maxTime=0;			//max park 3 hours
					int defaultRate=0; 		//rate is x/increment
					int minIncrement=0;		//min increase 30mins

					//THE FOLLOWING ARE FOR REFILLING/UNPARKING
					String syncParkingReferenceNumber=""; 
					double lat=0;
					double lon=0;
					long spotId=0;
					String description ="";
					String spotNumber = "";

					JsonToken tt = jp.nextToken();
					String current;

					while (tt != null && tt != JsonToken.END_OBJECT) {
						current = jp.getCurrentName();
						switch (tt) {
						case VALUE_NUMBER_INT: {
							if (PARAM_SPOT_ID.equals(current)) {
								spotId = jp.getLongValue();
							} else if (PARAM_MIN_TIME.equals(current)) {
								minTime = jp.getIntValue();
							} else if (PARAM_MAX_TIME.equals(current)) {
								maxTime = jp.getIntValue();
							} else if (PARAM_DEFAULT_RATE.equals(current)) {
								defaultRate = jp.getIntValue();
							} else if (PARAM_MIN_INCREMENT.equals(current)) {
								minIncrement = jp.getIntValue();
							} else if (PARAM_END_TIME.equals(current)){
								syncEndTime = jp.getLongValue();
							}
						}
						case VALUE_NUMBER_FLOAT: {
							if (PARAM_LAT.equals(current)) {
								lat = jp.getDoubleValue();
							} else if (PARAM_LON.equals(current)) {
								lon = jp.getDoubleValue();
							}
						}
						case VALUE_STRING: {
							if (PARAM_DESCRIPTION.equals(current)) {
								description = jp.getText();
							} else if (PARAM_PARK_REFERENCE.equals(current)){
								syncParkingReferenceNumber = jp.getText();
							}else if (PARAM_SPOT_NUMBER.equals(current)){
								spotNumber = jp.getText();
							}
						}
						}
						tt = jp.nextToken();
					}

					sync = new ParkSync(syncEndTime, minTime, maxTime, defaultRate, minIncrement, syncParkingReferenceNumber, lat, lon, spotId, description,spotNumber);
					return new ParkInstanceObject(-1, null, sync);
				}
			}
			t = jp.nextToken();
		}
		return new ParkInstanceObject(endTime, parkingReferenceNumber, sync);
	}
	
	public static RateResponse parseRateResponse(JsonParser jp) throws IOException {
		String resp = "";
		RateObject rate = null;
		ParkSync sync = null;
		JsonToken t = jp.nextToken();
		String curr;
		while (t != null && t != JsonToken.END_OBJECT) {
			curr = jp.getCurrentName();
				if(t==JsonToken.VALUE_STRING) {
					if (PARAM_RESP_CODE.equals(curr)) {
						resp = jp.getText();
					}
				}
				/* if embedded object is detected and param matches rateObject, parse the object.  */
				if(t==JsonToken.START_OBJECT){
					curr = jp.getCurrentName();
					if(PARAM_RATE_OBJECT.equals(curr)){
						double lat = 0;
						double lon = 0;
						long spot = 0;
						int minTime = 1;
						int maxTime = 3;
						int defaultRate = 1;
						int minIncrement = 30;
						String description = "";

						JsonToken token = jp.nextToken();
						String current;
						while (token != null && token != JsonToken.END_OBJECT) {
							current = jp.getCurrentName();
							switch (token) {
								case VALUE_NUMBER_INT: {
									if (PARAM_SPOT_ID.equals(current)) {
										spot = jp.getLongValue();
									} else if (PARAM_MIN_TIME.equals(current)) {
										minTime = jp.getIntValue();
									} else if (PARAM_MAX_TIME.equals(current)) {
										maxTime = jp.getIntValue();
									} else if (PARAM_DEFAULT_RATE.equals(current)) {
										defaultRate = jp.getIntValue();
									} else if (PARAM_MIN_INCREMENT.equals(current)) {
										minIncrement = jp.getIntValue();
									}
								}
								case VALUE_NUMBER_FLOAT: {
									if (PARAM_LAT.equals(current)) {
										lat = jp.getDoubleValue();
									} else if (PARAM_LON.equals(current)) {
										lon = jp.getDoubleValue();
									}
								}
								case VALUE_STRING: {
									if (PARAM_DESCRIPTION.equals(current)) {
										description = jp.getText();
									}
								}
							}
							token = jp.nextToken();
						}
						rate = new RateObject(lat, lon, spot, minTime, maxTime,defaultRate, minIncrement, description);
					}
					if(PARAM_PARK_SYNC.equals(curr)){

						long syncEndTime = 0; //how much to set timer
						int minTime=0;			//must park 1 hour
						int maxTime=0;			//max park 3 hours
						int defaultRate=0; 		//rate is x/increment
						int minIncrement=0;		//min increase 30mins

						//THE FOLLOWING ARE FOR REFILLING/UNPARKING
						String syncParkingReferenceNumber=""; 
						double lat=0;
						double lon=0;
						long spotId=0;
						String description ="";
						String spotNumber = "";

						JsonToken tt = jp.nextToken();
						String current;

						while (tt != null && tt != JsonToken.END_OBJECT) {
							current = jp.getCurrentName();
							switch (tt) {
							case VALUE_NUMBER_INT: {
								if (PARAM_SPOT_ID.equals(current)) {
									spotId = jp.getLongValue();
								} else if (PARAM_MIN_TIME.equals(current)) {
									minTime = jp.getIntValue();
								} else if (PARAM_MAX_TIME.equals(current)) {
									maxTime = jp.getIntValue();
								} else if (PARAM_DEFAULT_RATE.equals(current)) {
									defaultRate = jp.getIntValue();
								} else if (PARAM_MIN_INCREMENT.equals(current)) {
									minIncrement = jp.getIntValue();
								} else if (PARAM_END_TIME.equals(current)){
									syncEndTime = jp.getLongValue();
								}
							}
							case VALUE_NUMBER_FLOAT: {
								if (PARAM_LAT.equals(current)) {
									lat = jp.getDoubleValue();
								} else if (PARAM_LON.equals(current)) {
									lon = jp.getDoubleValue();
								}
							}
							case VALUE_STRING: {
								if (PARAM_DESCRIPTION.equals(current)) {
									description = jp.getText();
								} else if (PARAM_PARK_REFERENCE.equals(current)){
									syncParkingReferenceNumber = jp.getText();
								}else if (PARAM_SPOT_NUMBER.equals(current)){
									spotNumber = jp.getText();
								}
							}
							}
							tt = jp.nextToken();
						}

						sync = new ParkSync(syncEndTime, minTime, maxTime, defaultRate, minIncrement, syncParkingReferenceNumber, lat, lon, spotId, description,spotNumber);
						return new RateResponse(null, null, sync);
					}
				}
			
			t = jp.nextToken();
		}
		return new RateResponse(resp, rate, null);
	}

	public static ParkResponse parseParkingResponse(JsonParser jp) throws IOException {
		long endTime =0;
		String resp = "";
		String parkingReferenceNumber= "";
		ParkSync sync = null;
		JsonToken t = jp.nextToken();
		String curr;
		while (t != null && t != JsonToken.END_OBJECT) {
			curr = jp.getCurrentName();
			switch (t) {
				case VALUE_NUMBER_INT: {
					if (PARAM_END_TIME.equals(curr)){
						endTime = jp.getLongValue();
					}
				}
				case VALUE_STRING: {
					if (PARAM_RESP_CODE.equals(curr)) {
						resp = jp.getText();
					} else if (PARAM_PARK_REFERENCE.equals(curr)){
						parkingReferenceNumber = jp.getText();
					}
				}
				case START_OBJECT :{
					curr = jp.getCurrentName();
					if(PARAM_PARK_SYNC.equals(curr)){

						long syncEndTime = 0; //how much to set timer
						int minTime=0;			//must park 1 hour
						int maxTime=0;			//max park 3 hours
						int defaultRate=0; 		//rate is x/increment
						int minIncrement=0;		//min increase 30mins

						//THE FOLLOWING ARE FOR REFILLING/UNPARKING
						String syncParkingReferenceNumber=""; 
						double lat=0;
						double lon=0;
						long spotId=0;
						String description ="";
						String spotNumber = "";

						JsonToken tt = jp.nextToken();
						String current;

						while (tt != null && tt != JsonToken.END_OBJECT) {
							current = jp.getCurrentName();
							switch (tt) {
							case VALUE_NUMBER_INT: {
								if (PARAM_SPOT_ID.equals(current)) {
									spotId = jp.getLongValue();
								} else if (PARAM_MIN_TIME.equals(current)) {
									minTime = jp.getIntValue();
								} else if (PARAM_MAX_TIME.equals(current)) {
									maxTime = jp.getIntValue();
								} else if (PARAM_DEFAULT_RATE.equals(current)) {
									defaultRate = jp.getIntValue();
								} else if (PARAM_MIN_INCREMENT.equals(current)) {
									minIncrement = jp.getIntValue();
								} else if (PARAM_END_TIME.equals(current)){
									syncEndTime = jp.getLongValue();
								}
							}
							case VALUE_NUMBER_FLOAT: {
								if (PARAM_LAT.equals(current)) {
									lat = jp.getDoubleValue();
								} else if (PARAM_LON.equals(current)) {
									lon = jp.getDoubleValue();
								}
							}
							case VALUE_STRING: {
								if (PARAM_DESCRIPTION.equals(current)) {
									description = jp.getText();
								} else if (PARAM_PARK_REFERENCE.equals(current)){
									syncParkingReferenceNumber = jp.getText();
								}else if (PARAM_SPOT_NUMBER.equals(current)){
									spotNumber = jp.getText();
								}
							}
							}
							tt = jp.nextToken();
						}

						sync = new ParkSync(syncEndTime, minTime, maxTime, defaultRate, minIncrement, syncParkingReferenceNumber, lat, lon, spotId, description, spotNumber);
						return new ParkResponse(null, -1, null, sync);
					}
				}
				
			}
			t = jp.nextToken();
		}
		return new ParkResponse(resp, endTime, parkingReferenceNumber, null);
	}

    public static List<Spot> parseSpots(JsonParser jp) throws IOException {
        final List<Spot> spots = new ArrayList<Spot>();

        JsonToken t = jp.nextToken();
        String curr;
        while (t != null && t != JsonToken.END_OBJECT) {
            curr = jp.getCurrentName();
            if (t == JsonToken.START_ARRAY && PARAM_LOCATION_LIST.equals(curr)) {
                JsonToken tt = jp.nextToken();
                while (tt != null && tt != JsonToken.END_ARRAY) {
                    spots.add(parseSpot(jp));
                    tt = jp.nextToken();
                }
            }
            t = jp.nextToken();
        }
        return spots;
    }

    private static Spot parseSpot(JsonParser jp) throws IOException {
        double lat = 0d;
        double lon = 0d;
        String spotName = "";

        JsonToken t = jp.nextToken();
        String curr;
        while (t != null && t != JsonToken.END_OBJECT) {
            if (t == JsonToken.VALUE_STRING) {
                curr = jp.getCurrentName();
                if (PARAM_LAT.equals(curr)) {
                    lat = jp.getDoubleValue();
                } else if (PARAM_LON.equals(curr)) {
                    lon = jp.getDoubleValue();
                } else if (PARAM_SPOT_NAME.equals(curr)) {
                    spotName = jp.getText();
                }
            }
            t = jp.nextToken();
        }
        return new Spot(lat, lon, spotName);
    }
}


