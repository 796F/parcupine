package com.test;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;

public class Background extends Service{
	private int parkTime;
	private Bundle b;

	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	//	public void onCreate(){
	//		super.onCreate();
	//		/*SERVICE CANNOT USE A COUNTDOWN, MUST PERFORM A TIME CHECK (PHONE OFF??)*/
	//		//parkTime = b.getInt("time", 0);
	//		((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(100);
	//		//start timer,
	//		Timer x = new Timer();
	//
	//		x.schedule(new TimerTask(){
	//
	//			@Override
	//			public void run() {
	////				((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(300);
	////				Intent myIntent = new Intent(Background.this, TabsActivity.class);
	////				myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	////				startActivity(myIntent);
	//			}
	//
	//		}, 1100);
	//		
	//	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		b = intent.getExtras();
		((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(100);
		Timer x = new Timer();

		x.schedule(new TimerTask(){

			@Override
			public void run() {
				((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(300);
				Intent myIntent = new Intent(Background.this, TabsActivity.class);
				myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(myIntent);
			}

		}, b.getInt("time")*150);
		return START_STICKY;
	}
	public void onDestroy(){
		super.onDestroy();
		((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(1000);
	}

}
