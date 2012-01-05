package com.objects;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.provider.Settings;


public class ThrowDialog {
	public final static int MUST_LOGIN=0;
	public final static int COULD_NOT_AUTH=1;
	public final static int IS_PARKED=2;
	public final static int ZERO_MINUTES=3;
	public final static int UNPARK_ERROR=4;
	public final static int RESULT_ERROR=5;
	public final static int TIME_OUT=6;
	public final static int PARK_WARNING=7;
	public final static int BAD_QRCODE=8;
	public final static int REFILL_DONE=9;
	public final static int MAX_TIME=10;
	public final static int NO_REFILL=11;
	public final static int NO_NET=12;
	public final static int NO_LOCATION=13;
	public final static int NOT_PARKED= 14;
	public final static int NO_SPOTS= 15;
	public static void show(final Context c, int dialog){
		AlertDialog.Builder alert = new AlertDialog.Builder(c);
		AlertDialog a;
		switch(dialog){
		case MUST_LOGIN:
			alert.setMessage("You Must Login to use ParqMe");
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			a = alert.create();
			a.show();
			break;
		case COULD_NOT_AUTH:
			alert.setMessage("Could not Login\nCheck your fields");
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			a = alert.create();
			a.show();
			break;
		case IS_PARKED:
			alert.setMessage("You are currently parked");
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			a = alert.create();
			a.show();
			break;
		case ZERO_MINUTES:
			alert.setMessage("Select Parking Time");
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			a = alert.create();
			a.show();
			break;
		case UNPARK_ERROR:
			alert.setMessage("Error Occurred Unparking\nTry Again.");
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			a = alert.create();
			a.show();
			break;
		case RESULT_ERROR:
			alert.setMessage("Error Occurred Parking\nTry Again.");
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			a = alert.create();
			a.show();
			break;
		case TIME_OUT:
			alert.setMessage("You have run out of time.");
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			a = alert.create();
			a.show();
			break;
		case PARK_WARNING:
			alert.setMessage("You are almost out of time!");
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			a = alert.create();
			a.show();
			break;
		case BAD_QRCODE:
			alert.setMessage("QR Code did not Scan.");
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			a = alert.create();
			a.show();
			break;
			
		case REFILL_DONE:
			alert.setMessage("Refill Complete.");
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			a = alert.create();
			a.show();
			break;
		case MAX_TIME:
			alert.setMessage("You have reached the Max\nAllowed Park Time");
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			a = alert.create();
			a.show();
			break;
		//unused, MAX_TIME used instead.
		case NO_REFILL:
			alert.setMessage("You have reached the Max\nAllowed Park Time");
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			a = alert.create();
			a.show();
			break;
		case NO_NET:
			alert.setMessage("Could not connect to\nthe Internet");
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			a = alert.create();
			a.show();
			break;
		case NO_LOCATION:
			alert.setMessage("You need to enable Location Services in Settings to park by spot number");
			alert.setPositiveButton("Settings", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					c.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
						.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
				}
			});
			alert.setNegativeButton("Cancel", null);
			//alert.show();
			break;
		case NOT_PARKED:
			alert.setMessage("You are not currently parked");
			alert.setNegativeButton("Ok", null);
			alert.show();
			break;
		case NO_SPOTS:
			alert.setMessage("There are no open spots nearby");
			alert.setNegativeButton("Ok", null);
			alert.show();
			break;
		}
	}

}
