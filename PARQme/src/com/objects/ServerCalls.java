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
	private static final String SERVER_HOSTNAME = "http://75.101.132.219:80";

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

    public static void registerNewUser(String email, String password, String name, String ccNumber,
            String cscNumber, int expMonth, int expYear, String address, String zip,
            RegisterCallback callback) {
        new RegisterTask(email, password, name, ccNumber, cscNumber, expMonth, expYear, address,
                zip, callback).execute((Void) null);
	}

	public interface RegisterCallback {
	    void onRegisterComplete(boolean success);
	}

	private static class RegisterTask extends AsyncTask<Void, Void, Boolean> {
	    private final String mEmail;
	    private final String mPassword;
	    private final String mName;
	    private final String mCcNumber;
	    private final String mCscNumber;
	    private final int mExpMonth;
	    private final int mExpYear;
	    private final String mAddress;
	    private final String mZip;
	    private final RegisterCallback mCallback;
        RegisterTask(String email, String password, String name, String ccNumber, String cscNumber,
                int expMonth, int expYear, String address, String zip, RegisterCallback callback) {
	        mEmail = email;
	        mPassword = password;
	        mName = name;
	        mCcNumber = ccNumber;
	        mCscNumber = cscNumber;
	        mExpMonth = expMonth;
	        mExpYear = expYear;
	        mAddress = address;
	        mZip = zip;
	        mCallback = callback;
	    }

        @Override
        protected Boolean doInBackground(Void... params) {
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
                jg.writeString(mCcNumber);
                jg.writeFieldName("holderName");
                jg.writeString(mName);
                jg.writeFieldName("cscNumber");
                jg.writeString(mCscNumber);
                jg.writeFieldName("expMonth");
                jg.writeNumber(mExpMonth);
                jg.writeFieldName("expYear");
                jg.writeNumber(mExpYear);
                jg.writeFieldName("email");
                jg.writeString(mEmail);
                jg.writeFieldName("password");
                jg.writeString(mPassword);
                jg.writeFieldName("zipcode");
                jg.writeString(mZip);
                jg.writeFieldName("address");
                jg.writeString(mAddress);
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
            mCallback.onRegisterComplete(result);
        }
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

    public interface RateCallback {
        void onGetRateComplete(RateResponse rateResponse);
    }

	public static void getRateGps(String spotNum, double lat, double lon, SharedPreferences prefs, RateCallback callback) {
	    new RateGpsTask(spotNum, lat, lon, prefs, callback).execute((Void) null);
	}

	private static class RateGpsTask extends AsyncTask<Void, Void, RateResponse> {
	    private final String mSpotNum;
	    private final double mLat;
	    private final double mLon;
	    private final SharedPreferences mPrefs;
	    private final RateCallback mCallback;
	    RateGpsTask(String spotNum, double lat, double lon, SharedPreferences prefs, RateCallback callback) {
	        mSpotNum = spotNum;
	        mLat = lat;
	        mLon = lon;
	        mPrefs = prefs;
	        mCallback = callback;
	    }

        @Override
        protected RateResponse doInBackground(Void... params) {
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
                jg.writeNumber(mLat);
                jg.writeFieldName("lon");
                jg.writeNumber(mLon);
                jg.writeFieldName("spot");
                jg.writeString(mSpotNum);
                writeUserDetails(jg, mPrefs);
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

        @Override
        protected void onPostExecute(RateResponse result) {
            mCallback.onGetRateComplete(result);
        }
	}

	public static void getRateQr(String qrCode, SharedPreferences prefs, RateCallback callback) {
	    new RateQrTask(qrCode, prefs, callback).execute((Void) null);
	}

	private static class RateQrTask extends AsyncTask<Void, Void, RateResponse> {
	    private final String mQrCode;
	    private final SharedPreferences mPrefs;
	    private final RateCallback mCallback;
	    RateQrTask(String qrCode, SharedPreferences prefs, RateCallback callback) {
	        mQrCode = qrCode;
	        mPrefs = prefs;
	        mCallback = callback;
	    }

        @Override
        protected RateResponse doInBackground(Void... params) {
            ///final String[] tokens = qrcode.split("/");
            final String[] tokens = mQrCode.split("http://|/");
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
                writeUserDetails(jg, mPrefs);
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

        @Override
        protected void onPostExecute(RateResponse result) {
            mCallback.onGetRateComplete(result);
        }
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
    public static boolean editUser(SharedPreferences prefs, String newPassword, String newEmail, String newPhone){
    	try {
            final HttpURLConnection conn =
                    (HttpURLConnection)
                    new URL(SERVER_HOSTNAME + "/parkservice.user/update")
            .openConnection();
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            final JsonGenerator jg = JSON_FACTORY.createJsonGenerator(conn.getOutputStream());
            jg.writeStartObject();
            jg.writeFieldName("password");
            jg.writeString(newPassword);
            jg.writeFieldName("email");
            jg.writeString(newEmail);
            jg.writeFieldName("phone");
            jg.writeString(newPhone);
            writeUserDetails(jg, prefs);
            jg.writeEndObject();
            jg.flush();
            jg.close();
            if (conn.getResponseCode() == 200) {
                final JsonParser jp = JSON_FACTORY.createJsonParser(conn
                        .getInputStream());
                final boolean result = Parsers.parseResponseCode(jp);
                jp.close();
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
    /* STARTING EDIT CC CODE*/

    public static void editCreditCard(SharedPreferences prefs, String name, String ccNumber,
            String cscNumber, int expMonth, int expYear, String address, String zip,
            newCallback callback) {
        new EditCreditCardTask(prefs, name, ccNumber, cscNumber, expMonth, expYear, address,
                zip, callback).execute((Void) null);
	}

	public interface newCallback {
	    void onEditCardComplete(boolean success);
	}

	private static class EditCreditCardTask extends AsyncTask<Void, Void, Boolean> {
		private final SharedPreferences mPrefs;
	    private final String mName;
	    private final String mCcNumber;
	    private final String mCscNumber;
	    private final int mExpMonth;
	    private final int mExpYear;
	    private final String mAddress;
	    private final String mZip;
	    private final newCallback mCallback;
	    EditCreditCardTask(SharedPreferences prefs, String name, String ccNumber, String cscNumber,
                int expMonth, int expYear, String address, String zip, newCallback callback) {
	        mPrefs = prefs;
	        mName = name;
	        mCcNumber = ccNumber;
	        mCscNumber = cscNumber;
	        mExpMonth = expMonth;
	        mExpYear = expYear;
	        mAddress = address;
	        mZip = zip;
	        mCallback = callback;
	    }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                final HttpURLConnection conn =
                        (HttpURLConnection)
                        new URL(SERVER_HOSTNAME + "/parkservice.user/changeCC")
                .openConnection();
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                final JsonGenerator jg = JSON_FACTORY.createJsonGenerator(conn.getOutputStream());
                jg.writeStartObject();
                jg.writeFieldName("creditCard");
                jg.writeString(mCcNumber);
                jg.writeFieldName("holderName");
                jg.writeString(mName);
                jg.writeFieldName("cscNumber");
                jg.writeString(mCscNumber);
                jg.writeFieldName("expMonth");
                jg.writeNumber(mExpMonth);
                jg.writeFieldName("expYear");
                jg.writeNumber(mExpYear);
                writeUserDetails(jg, mPrefs);
                jg.writeFieldName("zipcode");
                jg.writeString(mZip);
                jg.writeFieldName("address");
                jg.writeString(mAddress);
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
            mCallback.onEditCardComplete(result);
        }
	}

    /* END EDIT CC CODE */
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
