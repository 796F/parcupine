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

	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		b = intent.getExtras();
		((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(100);
		Timer x = new Timer();

		x.schedule(new TimerTask(){

			@Override
			public void run() {
				((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(100);
//				Intent myIntent = new Intent(Background.this, TabsActivity.class);
//				myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				startActivity(myIntent);
			}

		}, b.getInt("time")*150);
		
		//schedule(TimerTask task, Date time) executes task at the time specified.  
		//so create a date object for 5 minutes before time runs out.
		return START_STICKY;
	}
	public void onDestroy(){
		super.onDestroy();
		((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(100);
	}

}
