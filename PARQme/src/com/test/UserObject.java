package com.test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.*;
import java.sql.*;

/**
 * This object is used to store user preferences and such, passed between activities inside a bundle.  
 * Remember to generate getters and setters
 * */
public class UserObject {
	/*returns if authentication passed.*/
	public static boolean getAuth(String email, String hash){
		try {
			String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
			data+="&"+ URLEncoder.encode("passhash", "UTF-8") + "=" + URLEncoder.encode(hash, "UTF-8");
			//prepare data
			URL url = new URL("http://localhost/applogin.php");
			URLConnection conn = url.openConnection();
			//open connection
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();
			//write data
			int bytesRead = 1;
			byte[] buffer = new byte[1];
			//prepare buffers
			BufferedInputStream bufferedInput = new BufferedInputStream(conn.getInputStream());
			bufferedInput.read(buffer);
			//read into buffer
			String x =(new String(buffer, 0,bytesRead));
			return (x.equals("1"));
		}catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}
	/*returns true if parking successful*/
	public static boolean sendCode(String qrcode, String phoneNumber){
		try {
			String data = URLEncoder.encode("code", "UTF-8") + "=" + URLEncoder.encode(qrcode, "UTF-8");
			data+= "&"+URLEncoder.encode("phonenumber", "UTF-8") + "=" + URLEncoder.encode(phoneNumber, "UTF-8");
			// Send data
			URL url = new URL("http://localhost/park.php");
			URLConnection conn = url.openConnection();
			
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();
			//write data
			int bytesRead = 1;
			byte[] buffer = new byte[1];
			//prepare buffers
			BufferedInputStream bufferedInput = new BufferedInputStream(conn.getInputStream());
			bufferedInput.read(buffer);
			//read into buffer
			String x =(new String(buffer, 0,bytesRead));
			return (x.equals("1"));
		}catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
}
