package com.objects;

import java.util.Date;

import com.test.LoginActivity;

import android.content.Context;
import android.content.SharedPreferences;

public class SavedInfo{
	/* Constantly making the same check and editor and changing info clutters code, dififcult to see logic.  */
	/* parkState
	 * String email
	 * String endTime
	 * String code
	 * Boolean remember
	 * */
	//TODO:  make sure all needed info is set via reset and stuff.
	public static final String SAVED_INFO = "ParqMeInfo";
	public static boolean isFirstTime(Context activity){
		SharedPreferences check = activity.getSharedPreferences(SAVED_INFO,0);
		return check.getBoolean("firstTimeFlag", true);
	}
	public static boolean ringEnable(Context activity){
		SharedPreferences check = activity.getSharedPreferences(SAVED_INFO,0);
		return check.getBoolean("ringEnable", true);
	}
	public static boolean warningEnable(Context activity){
		SharedPreferences check = activity.getSharedPreferences(SAVED_INFO,0);
		return check.getBoolean("warningEnable", true);
	}
	public static boolean vibrateEnable(Context activity){
		SharedPreferences check = activity.getSharedPreferences(SAVED_INFO,0);
		return check.getBoolean("vibrateEnable", true);
	}
	public static boolean autoRefill(Context activity){
		SharedPreferences check = activity.getSharedPreferences(SAVED_INFO,0);
		return check.getBoolean("autoRefill", true);
	}
	public static void eraseTimer(Context activity){
		SharedPreferences check = activity.getSharedPreferences(SAVED_INFO,0);
		SharedPreferences.Editor editor = check.edit();
		editor.putLong("endTime", 0);
		editor.commit();
	}
	//if parked return true, else false
	public static boolean isParked(Context activity){
		SharedPreferences check = activity.getSharedPreferences(SAVED_INFO,0);
		return check.getBoolean("parkState", false);
	}
	//if logged in returns true, else false
	public static boolean isLoggedIn(Context activity){
		SharedPreferences check = activity.getSharedPreferences(SAVED_INFO,0);
		return check.getLong("uid", -1) != -1;
		//SharedPreferences.Editor editor = check.edit();
	}
	//set's email to a string
	public static void setEmail(Context activity, String email){
		SharedPreferences check = activity.getSharedPreferences(SAVED_INFO, 0);
		SharedPreferences.Editor editor = check.edit();
		editor.putString("email", email);
		editor.commit();
		
	}
	public static void setGeneric(Context activity, String title, boolean input){
		SharedPreferences check = activity.getSharedPreferences(SAVED_INFO, 0);
		SharedPreferences.Editor editor = check.edit();
		editor.putBoolean(title, input);
		editor.commit();
	}
	public static long getEndTime(Context activity, SharedPreferences prefs){
		return prefs.getLong("endTime", 0);
	}
	public static Date getEndTimeDate(Context activity, SharedPreferences prefs) {
		return new Date(getEndTime(activity, prefs));
	}
	public static String getEmail(Context activity){
		SharedPreferences check = activity.getSharedPreferences(SAVED_INFO, 0);
		return check.getString("email", null);
	}
	public static float getLat(Context activity){
		SharedPreferences check = activity.getSharedPreferences(SAVED_INFO, 0);
		return check.getFloat("lat", 0);
	}
	public static float getLon(Context activity){
		SharedPreferences check = activity.getSharedPreferences(SAVED_INFO, 0);
		return check.getFloat("lon", 0);
	}
	//set's code to a string
	public static void setCode(Context activity, String code){
		SharedPreferences check = activity.getSharedPreferences(SAVED_INFO, 0);
		SharedPreferences.Editor editor = check.edit();
		editor.putString("code", code);
		editor.commit();
		
	}
	public static String getParkId(Context activity, SharedPreferences prefs) {
	    return prefs.getString("PARKID", "");
	}
    public static void unpark(Context activity, SharedPreferences.Editor edit) {
        edit.putBoolean("parkState", false);
        edit.putString("PARKID", "");
        edit.putLong("endTime", 0);
    }
    public static void park(Context activity, ParkInstanceObject park) {
        SharedPreferences.Editor edit = activity.getSharedPreferences(SAVED_INFO, 0).edit();
        edit.putBoolean("parkState", true);
        edit.putString("PARKID", park.getParkingReferenceNumber());
        edit.putLong("endTime", park.getEndTime());
        edit.commit();
    }
	public static void logIn(Context activity, boolean parkState, String email, long uid, String password){
		SharedPreferences check = activity.getSharedPreferences(SAVED_INFO, 0);
		SharedPreferences.Editor editor = check.edit();
		editor.putBoolean("parkState", parkState);
		editor.putString("email", email);
		editor.putLong("uid", uid);
		editor.putString("password", password);
		editor.commit();
	}
    public static void logOut(Context activity){
        SharedPreferences check = activity.getSharedPreferences(SAVED_INFO, 0);
        SharedPreferences.Editor editor = check.edit();
        editor.putBoolean("parkState", false);
        editor.putString("email", "");
        editor.putLong("uid", -1);
        editor.putString("password", "");
        editor.commit();
    }
	
	public static void syncParkingSession(Context activity, ParkSync sync){
		SharedPreferences check = activity.getSharedPreferences(SAVED_INFO, 0);
		SharedPreferences.Editor editor = check.edit();
		editor.putInt("defaultRate", sync.getDefaultRate());
		editor.putString("description",sync.getDescription());
		editor.putLong("endTime", sync.getEndTime());
		editor.putString("lat", ""+sync.getLat());
		editor.putString("lon", ""+sync.getLon());
		editor.putInt("maxTime", sync.getMaxTime());
		editor.putInt("minIncrement", sync.getMinIncrement());
		editor.putInt("minTime", sync.getMinTime());
		editor.putString("PARKID", sync.getParkingReferenceNumber());
		editor.putLong("spotId", sync.getSpotId());
		editor.commit();
	}
	public static void toggleVibrate(Context activity){
		SharedPreferences check = activity.getSharedPreferences(SAVED_INFO, 0);
		SharedPreferences.Editor editor = check.edit();
		if(check.getBoolean("vibrateEnable", false)){
			//if parked, set as not parked
			editor.putBoolean("vibrateEnable", false);
		}else{
			//if not parked, set as parked.
			editor.putBoolean("vibrateEnable", true);
		}
		editor.commit();
	}
	public static void toggleWarn(Context activity){
		SharedPreferences check = activity.getSharedPreferences(SAVED_INFO, 0);
		SharedPreferences.Editor editor = check.edit();
		if(check.getBoolean("warningEnable", false)){
			//if parked, set as not parked
			editor.putBoolean("warningEnable", false);
		}else{
			//if not parked, set as parked.
			editor.putBoolean("warningEnable", true);
		}
		editor.commit();
	}
	public static void toggleRing(Context activity){
		SharedPreferences check = activity.getSharedPreferences(SAVED_INFO, 0);
		SharedPreferences.Editor editor = check.edit();
		if(check.getBoolean("ringEnable", false)){
			//if parked, set as not parked
			editor.putBoolean("ringEnable", false);
		}else{
			//if not parked, set as parked.
			editor.putBoolean("ringEnable", true);
		}
		editor.commit();
	}
	public static void toggleRefill(Context activity){
		SharedPreferences check = activity.getSharedPreferences(SAVED_INFO, 0);
		SharedPreferences.Editor editor = check.edit();
		if(check.getBoolean("autoRefill", false)){
			//if parked, set as not parked
			editor.putBoolean("autoRefill", false);
		}else{
			//if not parked, set as parked.
			editor.putBoolean("autoRefill", true);
		}
		editor.commit();
	}
	public static void toggleRemember(Context activity){
		SharedPreferences check = activity.getSharedPreferences(SAVED_INFO, 0);
		SharedPreferences.Editor editor = check.edit();
		if(check.getBoolean("remember", false)){
			//if parked, set as not parked
			editor.putBoolean("remember", false);
		}else{
			//if not parked, set as parked.
			editor.putBoolean("remember", true);
		}
		editor.commit();
	}
}
