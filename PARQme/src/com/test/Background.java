package com.test;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.view.View;
import android.widget.ViewFlipper;

import com.objects.Global;
import com.objects.SavedInfo;
import com.objects.ServerCalls;

public class Background extends Service{
	private int parkTime;
	private Bundle b;
	public static final String SAVED_INFO = "ParqMeInfo";
	private Timer x;
	private View mainView;
	private ViewFlipper vf;
	private static MediaPlayer mediaPlayer;
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	//TODO:  bug here, may be unparqing despite refill.
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		b = intent.getExtras();
		
		mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
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
		//warningTime.setSeconds(currentTime.getSeconds()+30);
		//endTime.setSeconds(currentTime.getSeconds() + 60);
		
		//create formatter and get endtime string
		String endTimeString = Global.sdf.format(endTime);
		editor.putString("endTime", endTimeString);
		editor.commit();
		
		x = new Timer();

		
		if(SavedInfo.warningEnable(Background.this)){
			x.schedule(new TimerTask(){
				@Override
				public void run() {

					warnMe(Background.this);
					Intent myIntent = new Intent(Background.this, LoginActivity.class);
					myIntent.setAction(Intent.ACTION_MAIN);
					myIntent.addCategory(Intent.CATEGORY_LAUNCHER);
					
					//myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
					myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
					
                    final Notification n = new Notification(R.drawable.bluep,
                            "Your parking time is about to expire", System.currentTimeMillis());
					n.setLatestEventInfo(Background.this, Background.this
							.getString(R.string.app_name),
							"Your parking time is about to expire",
							PendingIntent.getActivity(Background.this, 0,
									myIntent, 0));
                    n.flags |= Notification.FLAG_AUTO_CANCEL;
                    ((NotificationManager) Background.this.getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, n);
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
					
					//TODO intent below causes crash.  
//					Intent myIntent = new Intent(Background.this, LoginActivity.class);
//					//the intent should be pointing to login now, no longer tabs.  
//					myIntent.setAction(Intent.ACTION_MAIN);
//					myIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//					
//					//myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//					startActivity(myIntent);
//					
					 
					Intent myIntent = new Intent(Background.this, LoginActivity.class);
					myIntent.setAction(Intent.ACTION_MAIN);
					myIntent.addCategory(Intent.CATEGORY_LAUNCHER);
					
					//myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
					myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
					
                    final Notification n = new Notification(R.drawable.bluep,
                            "Your parking time is about to expire", System.currentTimeMillis());
					n.setLatestEventInfo(Background.this, Background.this
							.getString(R.string.app_name),
							"Your parking time is about to expire",
							PendingIntent.getActivity(Background.this, 0,
									myIntent, 0));
                    n.flags |= Notification.FLAG_AUTO_CANCEL;
                    ((NotificationManager) Background.this.getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, n);
					
					stopService(new Intent(Background.this, Background.class));
				 }else{
					stopService(new Intent(Background.this, Background.class));
				 }
				
				
				
			}
		},endTime);
		
		return START_STICKY;
	}
	@Override
	public void onDestroy(){
		try{
		x.cancel();
		super.onDestroy();
		}catch(Exception e){
			//do nothing.
		}
	}
	
	static void warnMe(Context activity){
		SharedPreferences check = activity.getSharedPreferences(SAVED_INFO,0);
		//if time warning is enabled
		if(check.getBoolean("warningEnable", false)){
			//vibrate if settings say so
			if(check.getBoolean("vibrateEnable", false)){
				((Vibrator)activity.getSystemService(VIBRATOR_SERVICE)).vibrate(1000);
			}
			if(check.getBoolean("ringEnable", false)){
				mediaPlayer.start();
			}
		}
	}

}
