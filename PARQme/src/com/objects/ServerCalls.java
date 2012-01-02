package com.objects;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;

import android.content.SharedPreferences;
import android.os.AsyncTask;

/**
 * This object is used to store user preferences and such, passed between activities inside a bundle.  
 * Remember to generate getters and setters
 * */
public class ServerCalls {

	public static final JsonFactory JSON_FACTORY = new JsonFactory();
	private static final String SERVER_HOSTNAME = "http://75.101.132.219:8080";

	/*returns if authentication passed.*/
	public static void authUser(String email, String hash, AuthUserCallback callback){
	    new AuthUserTask(email, hash, callback).execute((Void) null);
	}

	public interface AuthUserCallback {
	    void onAuthUserComplete(UserObject user);
	}

	private static class AuthUserTask extends AsyncTask<Void, Void, UserObject> {
	    private final String mEmail;
	    private final String mHash;
	    private final AuthUserCallback mCallback;
	    AuthUserTask(String email, String hash, AuthUserCallback callback) {
	        mEmail = email;
	        mHash = hash;
	        mCallback = callback;
	    }

        @Override
        protected UserObject doInBackground(Void... params) {
            try {
                //open connection
                final HttpURLConnection conn =
                        (HttpURLConnection)
                        new URL(SERVER_HOSTNAME + "/parkservice.auth")
                .openConnection();
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                //write data
                final JsonGenerator jg = JSON_FACTORY.createJsonGenerator(conn.getOutputStream());
                jg.writeStartObject();
                jg.writeFieldName("email");
                jg.writeString(mEmail);
                jg.writeFieldName("password");
                jg.writeString(mHash);
                jg.writeEndObject();
                jg.flush();
                jg.close();
                if (conn.getResponseCode() == 200) {
                    final JsonParser jp = JSON_FACTORY.createJsonParser(conn
                            .getInputStream());
                    final UserObject user = Parsers.parseUser(jp);
                    jp.close();
                    return user;
                }
            } catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(UserObject result) {
            mCallback.onAuthUserComplete(result);
        }
	}

	public static boolean registerNewUser(String email, String password, String name,
			String ccNumber, String cscNumber, int expMonth, int expYear, String zip, String address) {
		try {	
			final HttpURLConnection conn =
					(HttpURLConnection)
					new URL(SERVER_HOSTNAME + "/parkservice.user/register")
			.openConnection();
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);
			final JsonGenerator jg = JSON_FACTORY.createJsonGenerator(conn.getOutputStream());
			jg.writeStartObject();
			jg.writeFieldName("creditCard");
			jg.writeString(ccNumber);
			jg.writeFieldName("holderName");
			jg.writeString(name);
			jg.writeFieldName("cscNumber");
			jg.writeString(cscNumber);
			jg.writeFieldName("expMonth");
			jg.writeNumber(expMonth);
			jg.writeFieldName("expYear");
			jg.writeNumber(expYear);
			jg.writeFieldName("email");
			jg.writeString(email);
			jg.writeFieldName("password");
			jg.writeString(password);
			jg.writeFieldName("zipcode");
			jg.writeString(zip);
			jg.writeFieldName("address");
			jg.writeString(address);
			jg.writeEndObject();
			jg.flush();
			jg.close();
			if (conn.getResponseCode() == 200) {
				final JsonParser jp = JSON_FACTORY.createJsonParser(conn
						.getInputStream());
				final boolean response = Parsers.parseResponseCode(jp);
				jp.close();
				return response;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void unpark(long spotId, String parkingReferenceNumber, SharedPreferences prefs, UnparkCallback callback) {
	    new UnparkTask(spotId, parkingReferenceNumber, prefs, callback).execute((Void) null);
	}

	public interface UnparkCallback {
	    void onUnparkComplete(boolean success);
	}

	private static class UnparkTask extends AsyncTask<Void, Void, Boolean> {
	    private final long mSpotId;
	    private final String mParkingReferenceNumber;
	    private final SharedPreferences mPrefs;
	    private final UnparkCallback mCallback;
	    UnparkTask(long spotId, String parkingReferenceNumber, SharedPreferences prefs, UnparkCallback callback) {
	        mSpotId = spotId;
	        mParkingReferenceNumber = parkingReferenceNumber;
	        mPrefs = prefs;
	        mCallback = callback;
	    }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                final HttpURLConnection conn =
                        (HttpURLConnection)
                        new URL(SERVER_HOSTNAME + "/parkservice.park/unpark")
                .openConnection();
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                final JsonGenerator jg = JSON_FACTORY.createJsonGenerator(conn.getOutputStream());
                jg.writeStartObject();
                jg.writeFieldName("spotId");
                jg.writeNumber(mSpotId);
                jg.writeFieldName("parkingReferenceNumber");
                jg.writeString(mParkingReferenceNumber);
                writeUserDetails(jg, mPrefs);
                jg.writeEndObject();
                jg.flush();
                jg.close();
                if (conn.getResponseCode() == 200) {
                    final JsonParser jp = JSON_FACTORY.createJsonParser(conn
                            .getInputStream());
                    final boolean response = Parsers.parseResponseCode(jp);
                    jp.close();
                    return response;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mCallback.onUnparkComplete(result);
        }
	}

	public static RateResponse getRateGps(String spotNum, double lat, double lon, SharedPreferences prefs) {
		try {
			final HttpURLConnection conn =
					(HttpURLConnection)
					new URL(SERVER_HOSTNAME + "/parkservice.rate/gps")
			.openConnection();
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);
			final JsonGenerator jg = JSON_FACTORY.createJsonGenerator(conn.getOutputStream());
			jg.writeStartObject();
			jg.writeFieldName("lat");
			jg.writeNumber(lat);
			jg.writeFieldName("lon");
			jg.writeNumber(lon);
			jg.writeFieldName("spot");
			jg.writeString(spotNum);
			writeUserDetails(jg, prefs);
			jg.writeEndObject();
			jg.flush();
			jg.close();
			if (conn.getResponseCode() == 200) {
				final JsonParser jp = JSON_FACTORY.createJsonParser(conn
						.getInputStream());
				final RateResponse rate = Parsers.parseRateResponse(jp);
				jp.close();
				return rate;
			}
		} catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}

	public static RateResponse getRateQr(String qrcode, SharedPreferences prefs){
		///final String[] tokens = qrcode.split("/");
		final String[] tokens = qrcode.split("http://|/");
		//0 is empty, 1 is parqme.com, 2 is main_lot, 3 is 1412
		// qr code is url, http://parqme.com/main_lot/1412
		if (tokens.length != 4) {
			return null;
		}
		try {
			final HttpURLConnection conn =
					(HttpURLConnection)
					new URL(SERVER_HOSTNAME + "/parkservice.rate/qrcode")
			.openConnection();
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);
			final JsonGenerator jg = JSON_FACTORY.createJsonGenerator(conn.getOutputStream());
			jg.writeStartObject();
			jg.writeFieldName("lot");
			jg.writeString(tokens[2]);
			jg.writeFieldName("spot");
			jg.writeString(tokens[3]);
			writeUserDetails(jg, prefs);
			jg.writeEndObject();
			jg.flush();
			jg.close();
			if (conn.getResponseCode() == 200) {
				final JsonParser jp = JSON_FACTORY.createJsonParser(conn
						.getInputStream());
				final RateResponse rate = Parsers.parseRateResponse(jp);
				jp.close();
				return rate;
			}
		} catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}

	public static void park(int duration, RateObject spot, SharedPreferences prefs, ParkCallback callback) {
	    new ParkTask(duration, spot, prefs, callback).execute((Void) null);
	}

	public interface ParkCallback {
	    void onParkComplete(ParkInstanceObject parkInstance);
	}

	private static class ParkTask extends AsyncTask<Void, Void, ParkInstanceObject> {
	    private final int mDuration;
	    private final RateObject mSpot;
	    private final SharedPreferences mPrefs;
	    private final ParkCallback mCallback;
	    ParkTask(int duration, RateObject spot, SharedPreferences prefs, ParkCallback callback) {
	        mDuration = duration;
	        mSpot = spot;
	        mPrefs = prefs;
	        mCallback = callback;
	    }

        @Override
        protected ParkInstanceObject doInBackground(Void... params) {
            try {
                final HttpURLConnection conn = (HttpURLConnection) new URL(SERVER_HOSTNAME
                        + "/parkservice.park/park")
                .openConnection();
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                final JsonGenerator jg = JSON_FACTORY.createJsonGenerator(conn.getOutputStream());
                jg.writeStartObject();
                jg.writeFieldName("spotId");
                jg.writeNumber(mSpot.getSpot());
                jg.writeFieldName("durationMinutes");
                jg.writeNumber(mDuration);
                jg.writeFieldName("chargeAmount");
                jg.writeNumber(mDuration / mSpot.getMinIncrement() * mSpot.getDefaultRate());
                jg.writeFieldName("paymentType");
                jg.writeNumber(0);
                writeUserDetails(jg, mPrefs);
                jg.writeEndObject();
                jg.flush();
                jg.close();
                if (conn.getResponseCode() == 200) {
                    final JsonParser jp = JSON_FACTORY.createJsonParser(conn.getInputStream());
                    final ParkInstanceObject response = Parsers.parseParkInstance(jp);
                    jp.close();
                    return response;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ParkInstanceObject result) {
            mCallback.onParkComplete(result);
        }
	}

    public static void refill(int duration, RateObject spot, String parkId, SharedPreferences prefs, RefillCallback callback){
        new RefillTask(duration, spot, parkId, prefs, callback).execute((Void) null);
    }

    public interface RefillCallback {
        void onRefillComplete(ParkInstanceObject parkInstance);
    }

    private static class RefillTask extends AsyncTask<Void, Void, ParkInstanceObject> {
        final int mDuration;
        final RateObject mSpot;
        final String mParkId;
        final SharedPreferences mPrefs;
        final RefillCallback mCallback;
        RefillTask(int duration, RateObject spot, String parkId, SharedPreferences prefs, RefillCallback callback) {
            mDuration = duration;
            mSpot = spot;
            mParkId = parkId;
            mPrefs = prefs;
            mCallback = callback;
        }

        @Override
        protected ParkInstanceObject doInBackground(Void... params) {
            try {
                final HttpURLConnection conn =
                        (HttpURLConnection)
                        new URL(SERVER_HOSTNAME + "/parkservice.park/refill")
                .openConnection();
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                final JsonGenerator jg = JSON_FACTORY.createJsonGenerator(conn.getOutputStream());
                jg.writeStartObject();
                jg.writeFieldName("parkingReferenceNumber");
                jg.writeString(mParkId);
                jg.writeFieldName("spotId");
                jg.writeNumber(mSpot.getSpot());
                jg.writeFieldName("durationMinutes");
                jg.writeNumber(mDuration);
                jg.writeFieldName("chargeAmount");
                jg.writeNumber(mDuration / mSpot.getMinIncrement() * mSpot.getDefaultRate());
                jg.writeFieldName("paymentType");
                jg.writeNumber(0);
                writeUserDetails(jg, mPrefs);
                jg.writeEndObject();
                jg.flush();
                jg.close();
                if (conn.getResponseCode() == 200) {
                    final JsonParser jp = JSON_FACTORY.createJsonParser(conn
                            .getInputStream());
                    final ParkInstanceObject response = Parsers.parseParkInstance(jp);
                    jp.close();
                    return response;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ParkInstanceObject result) {
            mCallback.onRefillComplete(result);
        }
    }

    public static List<Spot> findSpots(double lat, double lon, SharedPreferences prefs){
        try {
            final HttpURLConnection conn =
                    (HttpURLConnection)
                    new URL(SERVER_HOSTNAME + "/parkservice.maps/find")
            .openConnection();
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            final JsonGenerator jg = JSON_FACTORY.createJsonGenerator(conn.getOutputStream());
            jg.writeStartObject();
            jg.writeFieldName("lat");
            jg.writeNumber(lat);
            jg.writeFieldName("lon");
            jg.writeNumber(lon);
            writeUserDetails(jg, prefs);
            jg.writeEndObject();
            jg.flush();
            jg.close();
            if (conn.getResponseCode() == 200) {
                final JsonParser jp = JSON_FACTORY.createJsonParser(conn
                        .getInputStream());
                final List<Spot> spots = Parsers.parseSpots(jp);
                jp.close();
                return spots;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
	private static void writeUserDetails(JsonGenerator jg, SharedPreferences prefs) throws JsonGenerationException, IOException {
		final long uid = prefs.getLong("uid", -1);
		final String email = prefs.getString("email", "");
		final String password = prefs.getString("password", "");
		jg.writeFieldName("uid");
		jg.writeNumber(uid);
		jg.writeFieldName("userInfo");
		jg.writeStartObject();
		jg.writeFieldName("email");
		jg.writeString(email);
		jg.writeFieldName("password");
		jg.writeString(password);
		jg.writeEndObject();
	}
}
