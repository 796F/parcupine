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
	public static final String SAVED_INFO = "ParqMeInfo";
	//clears all to empty strings and falses
	public static void reset(Context activity){
		SharedPreferences check = activity.getSharedPreferences(SAVED_INFO,0);
		SharedPreferences.Editor editor = check.edit();
		editor.putString("email", ""); editor.putString("endTime", ""); editor.putString("code", "");
		editor.putBoolean("parkState", false); editor.putBoolean("loginState", false); editor.putBoolean("remember", false);
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
	//set's end time to a string
	public static void setEndTime(Context activity, String endTime){
		SharedPreferences check = activity.getSharedPreferences(SAVED_INFO, 0);
		SharedPreferences.Editor editor = check.edit();
		editor.putString("endTime", endTime);
		editor.commit();
		
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
}
