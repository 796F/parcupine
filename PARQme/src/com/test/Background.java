package com.test;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.objects.Global;
import com.objects.SavedInfo;
import com.objects.ServerCalls;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.view.View;
import android.widget.ViewFlipper;

public class Background extends Service{
	private int parkTime;
	private Bundle b;
	public static final String SAVED_INFO = "ParqMeInfo";
	private Timer x;
	private View mainView;
	private ViewFlipper vf;
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	//TODO:  bug here, may be unparqing despite refill.
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		b = intent.getExtras();
		
		final SharedPreferences check = getSharedPreferences(SAVED_INFO,0);
		final SharedPreferences.Editor editor = check.edit();
		//grab current time, and declare end time
		Date currentTime = new Date();
		Date endTime = new Date();
		Date warningTime = new Date();
		//add park minutes to current time's mins and set as end time.
		
		warningTime.setSeconds(currentTime.getSeconds()+b.getInt("time")-30);
		endTime.setSeconds(currentTime.getSeconds() + b.getInt("time"));
		//TODO: delete me, for testing
//		warningTime.setSeconds(currentTime.getSeconds()+30);
//		endTime.setSeconds(currentTime.getSeconds() + 60);
		
		//create formatter and get endtime string
		String endTimeString = Global.sdf.format(endTime);
		editor.putString("endTime", endTimeString);
		editor.commit();
		
		x = new Timer();

		
		if(SavedInfo.warningEnable(Background.this)){
			x.schedule(new TimerTask(){
				@Override
				public void run() {

					MainActivity.warnMe(Background.this);
					
					Intent myIntent = new Intent(Background.this, TabsActivity.class);
					myIntent.setAction(Intent.ACTION_MAIN);
					myIntent.addCategory(Intent.CATEGORY_LAUNCHER);
					
					myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(myIntent);
					
				}
			}, warningTime);
		}
		
		x.schedule(new TimerTask(){
			@Override
			public void run() {
				
				 String qrcode = check.getString("code", "badcode");
				 String email = check.getString("email", "bademail");
				 if(ServerCalls.unPark(qrcode, email)==1){
					 
					((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(500);
					SavedInfo.togglePark(Background.this);
					Intent myIntent = new Intent(Background.this, TabsActivity.class);
					myIntent.setAction(Intent.ACTION_MAIN);
					myIntent.addCategory(Intent.CATEGORY_LAUNCHER);
					
					myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(myIntent);
					
					stopService(new Intent(Background.this, Background.class));
				 }else{
				 }
				
				
				
			}
		},endTime);
		
		return START_STICKY;
	}
	@Override
	public void onDestroy(){
		super.onDestroy();
		x.cancel();
	}

}
