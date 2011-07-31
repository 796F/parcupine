package com.objects;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;


public class ThrowDialog {
	public final static int MUST_LOGIN=0;
	public final static int COULD_NOT_AUTH=1;
	public final static int IS_PARKED=2;
	public final static int ZERO_MINUTES=3;
	public final static int UNPARK_ERROR=4;
	public final static int RESULT_ERROR=5;

	public static void show(Context c, int dialog){
		AlertDialog.Builder alert = new AlertDialog.Builder(c);
		AlertDialog a;
		switch(dialog){
		case MUST_LOGIN:
			alert.setMessage("You Must Login to use ParqMe");
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			a = alert.create();
			a.show();
			break;
		}
	}

}
