package com.objects;

import android.content.Context;
import android.content.SharedPreferences;

public class SavedInfo{
	/* Constantly making the same check and editor and changing info clutters code, dififcult to see logic.  */
	/* parkState
	 * loginState
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
	//initializes all necessary states to initial if its first time app is run.  
	public static void reset(Context activity){
		SharedPreferences check = activity.getSharedPreferences(SAVED_INFO,0);
		SharedPreferences.Editor editor = check.edit();
		editor.putString("endTime", ""); 
		editor.putString("code", "");
		editor.putBoolean("parkState", false); 
		editor.putBoolean("remember", false);
		editor.putBoolean("vibrateEnable", false); 
		editor.putBoolean("warningEnable", false); 
		editor.putBoolean("ringEnable", false);
		editor.putBoolean("autoRefill", false);
		editor.putBoolean("firstTimeFlag", false);
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
		return check.getBoolean("loginState", false);
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
	//set's end time to a string
	public static void setEndTime(Context activity, String endTime){
		SharedPreferences check = activity.getSharedPreferences(SAVED_INFO, 0);
		SharedPreferences.Editor editor = check.edit();
		editor.putString("endTime", endTime);
		editor.commit();
		
	}
	public static String getEndTime(Context activity){
		SharedPreferences check = activity.getSharedPreferences(SAVED_INFO, 0);
		return check.getString("endTime", null);
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
	//toggles parkState
	public static void togglePark(Context activity){
		SharedPreferences check = activity.getSharedPreferences(SAVED_INFO, 0);
		SharedPreferences.Editor editor = check.edit();
		if(check.getBoolean("parkState", false)){
			//if parked, set as not parked
			editor.putBoolean("parkState", false);
		}else{
			//if not parked, set as parked.
			editor.putBoolean("parkState", true);
		}
		editor.commit();
	}
	//toggles loginState
	public static void toggleLogin(Context activity){
		SharedPreferences check = activity.getSharedPreferences(SAVED_INFO, 0);
		SharedPreferences.Editor editor = check.edit();
		if(check.getBoolean("loginState", false)){
			//if parked, set as not parked
			editor.putBoolean("loginState", false);
		}else{
			//if not parked, set as parked.
			editor.putBoolean("loginState", true);
		}
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
