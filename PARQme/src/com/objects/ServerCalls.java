package com.objects;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;

/**
 * This object is used to store user preferences and such, passed between activities inside a bundle.  
 * Remember to generate getters and setters
 * */
public class ServerCalls {
	
	/*returns if authentication passed.*/
	public static UserObject getUser(String email, String hash){
		try {
			String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
			data+="&"+ URLEncoder.encode("passhash", "UTF-8") + "=" + URLEncoder.encode(hash, "UTF-8");
			//prepare data
			URL url = new URL("http://parqme.com/applogin.php");
			URLConnection conn = url.openConnection();
			//open connection
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();
			//write data
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			//[fname, lname, email, phone, history ] split by pipe char
			//history is [recent & oldeer & ... & oldest]
			return new UserObject(rd.readLine());
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	
	}
	
	public static int unPark(String qrcode, String email){
		try {
			String data = URLEncoder.encode("code", "UTF-8") + "=" + URLEncoder.encode(qrcode, "UTF-8");
			data+= "&"+URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
			// Send data
			URL url = new URL("http://parqme.com/unpark.php");
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
			return Integer.parseInt(x);
		}catch (Exception e){
			e.printStackTrace();
		}
		return -1;
	}
	public static ParkObject getSpotInfo(String qrcode,String email){
		try {
			String data = URLEncoder.encode("code", "UTF-8") + "=" + URLEncoder.encode(qrcode, "UTF-8");
			data+= "&"+URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
			// Send data
			URL url = new URL("http://parqme.com/loc_info.php");
			URLConnection conn = url.openConnection();
			
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();
			//write data
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			//List<String> fields = Arrays.asList(rd.readLine().split("@"));
			// [ location, lat, lon, spot# ]
			return new ParkObject(rd.readLine());
			//return new ParkObject(fields.get(0),Float.parseFloat(fields.get(1)), Float.parseFloat(fields.get(2)), Integer.parseInt(fields.get(3)));
			
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
		
	}
	public static int Park(String qrcode, String email, String endtime){
		try {
			String data = URLEncoder.encode("code", "UTF-8") + "=" + URLEncoder.encode(qrcode, "UTF-8");
			data+= "&"+URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
			data+= "&"+URLEncoder.encode("endtime", "UTF-8") + "=" + URLEncoder.encode(endtime, "UTF-8");
			// Send data
			URL url = new URL("http://parqme.com/park.php");
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
			return Integer.parseInt(x);
		}catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	public static int addHistory(String date, String starttime, String endtime, String location, String email, int cost){
		try {
			String data = URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8");
			data+= "&"+URLEncoder.encode("location", "UTF-8") + "=" + URLEncoder.encode(location, "UTF-8");
			data+= "&"+URLEncoder.encode("starttime", "UTF-8") + "=" + URLEncoder.encode(starttime, "UTF-8");
			data+= "&"+URLEncoder.encode("endtime", "UTF-8") + "=" + URLEncoder.encode(endtime, "UTF-8");
			data+= "&"+URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
			data+= "&"+URLEncoder.encode("cost", "UTF-8") + "=" + URLEncoder.encode(cost+"", "UTF-8");
			// Send data
			URL url = new URL("http://parqme.com/add_history.php");
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
			return Integer.parseInt(x);
		}catch (Exception e){
			e.printStackTrace();
		}
		return -1;
	}
	public static String test(String qrcode, String email, String endtime){
		try {
			String data = URLEncoder.encode("code", "UTF-8") + "=" + URLEncoder.encode(qrcode, "UTF-8");
			data+= "&"+URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
			data+= "&"+URLEncoder.encode("endtime", "UTF-8") + "=" + URLEncoder.encode(endtime, "UTF-8");
			// Send data
			URL url = new URL("http://localhost:8080/UserBounce/UpdateDatabase");
			URLConnection conn = url.openConnection();
			
			conn.setRequestProperty("fromApp", "yes");
			
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();
			//write data
			int bytesRead = 1024;
			byte[] buffer = new byte[1];
			//prepare buffers
			BufferedInputStream bufferedInput = new BufferedInputStream(conn.getInputStream());
			bufferedInput.read(buffer);
			//read into buffer
			String x =(new String(buffer, 0,bytesRead));
			return x;
		}catch (Exception e){
			e.printStackTrace();
		}
		return "blah";
	}
}
