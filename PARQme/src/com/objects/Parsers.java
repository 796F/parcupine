package com.objects;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

public class Parsers {
	// User parameters
	private static final String PARAM_UID = "uid";
	private static final String PARAM_PARKSTATE = "parkstate";

	// Response code parameter
	private static final String PARAM_RESP_CODE = "responsecode";
	private static final String RESP_CODE_OK = "OK";

	// {"fname":"xia@umd.edu","lname":"Mikey","phone":"1337"}
	public static UserObject parseUser(JsonParser jp) throws IOException {
		long uid = -1;
		boolean parkState = false;

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
			t = jp.nextToken();
		}
		return new UserObject(uid, parkState);
	}

	public static boolean parseResponseCode(JsonParser jp) throws IOException {
		JsonToken t = jp.nextToken();
		String curr;
		while (t != null && t != JsonToken.END_OBJECT) {
			if (t == JsonToken.VALUE_STRING) {
				curr = jp.getCurrentName();
				if (PARAM_RESP_CODE.equals(curr)) {
					return RESP_CODE_OK.equals(jp.getText());
				}
			}
		}
		return false;
	}
}
