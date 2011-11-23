package com.objects;

import java.io.IOException;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

public class Parsers {
	public static final JsonFactory JSON_FACTORY = new JsonFactory();

	private static final String PARAM_FNAME = "fname";
	private static final String PARAM_LNAME = "lname";
	private static final String PARAM_EMAIL = "email";
	private static final String PARAM_PHONE = "phone";

	// {"fname":"xia@umd.edu","lname":"Mikey","phone":"1337"}
	public static UserObject parseUser(JsonParser jp) throws IOException {
		String fname = null;
		String lname = null;
		String email = null;
		String phone = null;

		JsonToken t = jp.nextToken();
		String curr;
		while (t != null && t != JsonToken.END_OBJECT) {
			if (t == JsonToken.VALUE_STRING) {
				curr = jp.getCurrentName();
				if (PARAM_FNAME.equals(curr)) {
					fname = jp.getText();
				} else if (PARAM_LNAME.equals(curr)) {
					lname = jp.getText();
				} else if (PARAM_EMAIL.equals(curr)) {
					email = jp.getText();
				} else if (PARAM_PHONE.equals(curr)) {
					phone = jp.getText();
				}
			}
			t = jp.nextToken();
		}
		return new UserObject(fname, lname, email, phone);
	}
}
