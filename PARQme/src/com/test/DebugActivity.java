package com.test;

import java.util.List;

import com.objects.ServerCalls;

import android.app.Activity;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DebugActivity extends Activity {

	private TextView display;
	private Button debugbutton;
	private LocationManager locationManager;
	private String bestProvider;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.debuglayout);
	    
	    debugbutton = (Button) findViewById(R.id.debugButton1);
	    display = (TextView) findViewById(R.id.debugText1);
	    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	    List<String> providers = locationManager.getAllProviders();	
	    for (String provider : providers) {
			printProvider(provider);
			Location location = locationManager.getLastKnownLocation(provider);
			printLocation(location);
		}
	    
	    
	    Criteria criteria = new Criteria();
		bestProvider = locationManager.getBestProvider(criteria, false);
		//display.append("\n\nBEST Provider:\n");
		//printProvider(bestProvider);
		
		Location location = locationManager.getLastKnownLocation(bestProvider);
		//printLocation(location);

	    debugbutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Toast.makeText(DebugActivity.this, "HELLO WORLD", Toast.LENGTH_SHORT);
				SharedPreferences check = getSharedPreferences("ParqMeInfo",0);
				display.setText(ServerCalls.test("bha", "email", "endtime"));
				//SavedInfo.reset(DebugActivity.this);
				//finish();
			}
		});
	    
	}
	private void printProvider(String provider) {
		LocationProvider info = locationManager.getProvider(provider);
		display.append(info.toString() + "\n");
	}

	private void printLocation(Location location) {
		if (location == null)
			display.append("\nLocation[unknown]\n\n");
		else
			display.append("\n\n" + location.toString());
	
	}
}
