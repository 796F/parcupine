package com.test;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.objects.Global;
import com.objects.SavedInfo;
import com.objects.ThrowDialog;

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

	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		b = intent.getExtras();
//		mainView = (View) b.get("mainView");
//		vf = (ViewFlipper) mainView.findViewById(R.id.flipper);
		final SharedPreferences check = getSharedPreferences(SAVED_INFO,0);
		final SharedPreferences.Editor editor = check.edit();
		//grab current time, and declare end time
		Date currentTime = new Date();
		Date endTime = new Date();
		Date warningTime = new Date();
		//add park minutes to current time's mins and set as end time.
		
		//warningTime.setMinutes(currentTime.getMinutes()+b.getInt("time")-5);
		warningTime.setSeconds(currentTime.getSeconds()+b.getInt("time")-5*60);

		//endTime.setMinutes(currentTime.getMinutes() + b.getInt("time"));
		endTime.setSeconds(currentTime.getSeconds() + b.getInt("time"));
		
		//create formatter and get endtime string
		String endTimeString = Global.sdf.format(endTime);
		editor.putString("endTime", endTimeString);
		editor.commit();
		//ON REFILL, stop this service, RECALCULATE ENDTIME by adding refill mins, AND RESTART service with new time.
		
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
				//unparq user from server, stop the service, Vibrate
				
				//stopService(new Intent(Background.this, Background.class));
				 String qrcode = check.getString("code", "badcode");
				 String email = check.getString("email", "bademail");
				 if(ServerCalls.unPark(qrcode, email)==1){
					 //TODO:  resume app, and give dialog/warning
					((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(2000);
					stopService(new Intent(Background.this, Background.class));
					//MainActivity.vf.showPrevious();
					SavedInfo.togglePark(Background.this);
				 }else{
					//ThrowDialog.show(Background.this, ThrowDialog.UNPARK_ERROR);
				 }
				
				
				editor.putBoolean("parkState", false);
				editor.commit();
			}
		},endTime);
		
		//schedule(TimerTask task, Date time) executes task at the time specified.  
		//so create a date object for 5 minutes before time runs out.
		return START_STICKY;
	}
	@Override
	public void onDestroy(){
		super.onDestroy();
		x.cancel();
	}

}
