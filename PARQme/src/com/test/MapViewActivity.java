package com.test;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.objects.SavedInfo;

public class MapViewActivity extends MapActivity {
	private MapController mc;
	private Button findPark;
	private Button findMe;
	private Button findCar;
	public static double lat;
	public static double lon;
	private LocationManager lm;
	private String locationProvider;
	private TextView latlon;
	/** Called when the activity is first created. */
	
	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.map);
	    locationProvider = LocationManager.NETWORK_PROVIDER;
	    MapView mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(false);
	    findPark = (Button) findViewById(R.id.findPark);
	    /*lat=39.008046 lon=-76.888247 */
	    latlon = (TextView) findViewById(R.id.latlontext);
	    findPark.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//get saved info and animate to location and zoom in.  
				mc.animateTo(
						new GeoPoint(
								(int)38.935899*1000000, 
								(int)-77.087122*1000000
								)
						);
				mc.setZoom(14);
			}
		});
	    findCar = (Button) findViewById(R.id.findCar);
	    findCar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//get saved info and animate to location and zoom in.  
				mc.animateTo(
						new GeoPoint(
								(int)SavedInfo.getLat(MapViewActivity.this)*1000000, 
								(int)SavedInfo.getLon(MapViewActivity.this)*1000000
								)
						);
				mc.setZoom(18);
			}
		});
	    
	    findMe = (Button) findViewById(R.id.findMe);
	    findMe.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//get saved info and animate to location and zoom in.  
				mc.animateTo(
						new GeoPoint(
								(int)lat*1000000, 
								(int)lon*1000000
								)
						);
				mc.setZoom(18);
			}
		});
	    
	    mc = mapView.getController();
	    lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    LocationListener locListener = new LocationListener(){

			@Override
			public void onLocationChanged(Location location) {
				lat = location.getLatitude();	
	        	lon = location.getLongitude();
	        	latlon.setText(""+lat+" "+lon);
	        	mc.animateTo(new GeoPoint((int)lat*1000000, (int)lon*1000000));
			}

			@Override
			public void onProviderDisabled(String arg0) {
				
			}

			@Override
			public void onProviderEnabled(String arg0) {
				
			}

			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
				
			}
	    	
	    };

        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
	}
	public void onBackPressed(){
		Log.d("CDA", "OnBackPressed Called");
		Intent setIntent = new Intent(Intent.ACTION_MAIN);
		setIntent.addCategory(Intent.CATEGORY_HOME);
		setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(setIntent);
    	return;
    }
}
