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
import com.objects.ServerCalls.UnparkCallback;

public class Background extends Service{
	private Bundle b;
	public static final String SAVED_INFO = "ParqMeInfo";
	private Timer x;
	private static MediaPlayer mediaPlayer;
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		b = intent.getExtras();

		mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
		final SharedPreferences check = getSharedPreferences(SAVED_INFO,0);
		long endTimeLong = SavedInfo.getEndTime(check);
		long warnTimeLong = endTimeLong-(1000*240); //hard code 4 minutes left

		Date endTime = new Date();
		endTime.setTime(endTimeLong);

		Date warnTime = new Date();
		warnTime.setTime(warnTimeLong);


		x = new Timer();
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
						"Your parking time is about to expire!", System.currentTimeMillis());
				n.setLatestEventInfo(Background.this, Background.this
						.getString(R.string.app_name),
						"Your parking time is about to expire!",
						PendingIntent.getActivity(Background.this, 0,
								myIntent, 0));
				n.flags |= Notification.FLAG_AUTO_CANCEL;
				((NotificationManager) Background.this.getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, n);
			}
		}, warnTime);


		x.schedule(new TimerTask(){
			@Override
			public void run() {

				if(SavedInfo.autoRefill(Background.this)){
					//if the user has auto-refill on, do not unpark, simply close this background service.  
					//stopService(new Intent(Background.this, Background.class));
				}else{

					String parkingReferenceNumber = check.getString("PARKID", "");
					long spotId = check.getLong("spot", 111);
					ServerCalls.unpark(spotId, parkingReferenceNumber, check, new UnparkCallback() {
						@Override
						public void onUnparkComplete(boolean success) {
							if (success) {
								((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(500);
								SavedInfo.unpark(Background.this);

								Intent myIntent = new Intent(Background.this, LoginActivity.class);
								myIntent.setAction(Intent.ACTION_MAIN);
								myIntent.addCategory(Intent.CATEGORY_LAUNCHER);

								// myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
								myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
										| Intent.FLAG_ACTIVITY_SINGLE_TOP);

								final Notification n = new Notification(R.drawable.bluep,
										"You have run out of time", System
										.currentTimeMillis());
								n.setLatestEventInfo(Background.this,
										Background.this.getString(R.string.app_name),
										"You have run out of time",
										PendingIntent.getActivity(Background.this, 0, myIntent, 0));
								n.flags |= Notification.FLAG_AUTO_CANCEL;
								((NotificationManager) Background.this
										.getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, n);
							}
							stopService(new Intent(Background.this, Background.class));
						}
					});
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
		//vibrate if settings say so
		if(check.getBoolean("vibrateEnable", false)){
			((Vibrator)activity.getSystemService(VIBRATOR_SERVICE)).vibrate(1000);
		}
		if(check.getBoolean("ringEnable", false)){
			mediaPlayer.start();
		}

	}

}
