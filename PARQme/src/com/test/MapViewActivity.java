package com.test;

import java.util.List;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class MapViewActivity extends MapActivity {
	private MapController mc;
	/** Called when the activity is first created. */
	
	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.map);

	    MapView mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    
	    mc = mapView.getController();
	    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    List<String> list = lm.getAllProviders();
	    String provider = lm.getBestProvider(new Criteria(), false);
	    
        Location location = lm.getLastKnownLocation(provider);
        if(location!=null){
        	double lat = location.getLatitude();
        	double lon = location.getLongitude();
        	GeoPoint p = new GeoPoint((int) lat*1000000, (int) lon*1000000);
        	mc.animateTo(p);
        }else{
        
        }
	       
	}
}
