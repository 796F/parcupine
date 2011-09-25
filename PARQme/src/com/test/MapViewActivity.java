package com.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.objects.MapOverlays;
import com.objects.SavedInfo;

/**
 * user loads map, on find car, we create an overlay item that holds info about where
 * the car is parked (location) such as Nebraska Lot, floor 3, spot 125.  
 * 
 * a little car icon will mark where user is parked.
 * 
 * write code to add and remove overlays easily.
 * 
 * on click, we cannot use dialogs, looks bad, kinda.  what about toast?  custom dialogs?
 * 
 * find parking loads option, use current location vs  
 * address, android uses geoencoder to get geopoint, then animate map
 * to closest parking location.  
 * 
 * launch maps for navigation via app?
 * */
public class MapViewActivity extends MapActivity {
	//private constants used
	public static double lat;
	public static double lon;
	private int zoomLevel = 21;
	private double a;
	private double b;

	//map controlling elements
	private String locationProvider;
	private LocationManager locMan;
	private MapController mapCtrl;
	private ArrayList<OverlayItem> parkLoc;
	private MapOverlays itemizedoverlay;
	private List<Overlay> mapOverlays;
	private Geocoder gc;
	private EditText address;
	//buttons used
	private Button findParking;
	private Button findMe;
	private Button findCar;
	private Button searchButton;
	
	//display elements
	private TextView latlonTempDisplay;
	
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		gc = new Geocoder(this);
//		parkLoc = new ArrayList<OverlayItem>();
//		parkLoc.add(new OverlayItem(new GeoPoint((int)(38.984924*1e6),(int)(-76.935486*1e6)), "Ritchie Parking Lot", "Second Line"));

		//hook elements
		MapView mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		latlonTempDisplay = (TextView) findViewById(R.id.latlontext);
		address = (EditText) findViewById(R.id.addressinput);
		locationProvider = LocationManager.NETWORK_PROVIDER;
		mapCtrl = mapView.getController();
		locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		//get reference to your map's overlays
		mapOverlays = mapView.getOverlays();
		//find a drawable you want, can declare multiple drawables
		
		Drawable drawable = this.getResources().getDrawable(R.drawable.bluep);
		//then create an itemized overlay using said drawable, can make more itemizzedoverlays.
		itemizedoverlay = new MapOverlays(drawable,mapView);
		GeoPoint point = new GeoPoint((int)(38.984924*1e6),(int)(-76.935486*1e6));
		OverlayItem x = new OverlayItem(point, "Ritchie Parking Lot", "Spot: D6");
		itemizedoverlay.addOverlay(x);
		mapOverlays.add(itemizedoverlay);
		
		LocationListener locListener = new LocationListener(){
			@Override
			public void onLocationChanged(Location location) {
				lat = location.getLatitude();	
				lon = location.getLongitude();
				latlonTempDisplay.setText("Lat:" +lat + "   Lon:" + lon);
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
		
		searchButton = (Button) findViewById(R.id.searchbutton);
		searchButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String addressIn = address.getText().toString();
				try {
					List<Address> locations =  gc.getFromLocationName(addressIn, 5);
					for(Address x: locations){
						Toast.makeText(MapViewActivity.this, x.getLatitude()+":"+x.getLongitude(), Toast.LENGTH_LONG);
						a = x.getLatitude();
						b = x.getLongitude();
					}
					mapCtrl.animateTo(new GeoPoint((int)(a*1e6), (int)(b*1e6)));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		//initialize buttons and set listeners
		findParking = (Button) findViewById(R.id.findPark);
		findParking.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//load information from database, change overlay to show where spots are
				mapCtrl.animateTo(new GeoPoint((int)(38.984924*1e6),(int)(-76.935486*1e6)));
				//throw dialog, ask for "current location, address"

				//if current location, zoom there.

				//else zoom to address

			}
		});
		findCar = (Button) findViewById(R.id.findCar);
		findCar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//get saved info about spot and animate to location and zoom in.  
				double testLat = SavedInfo.getLat(MapViewActivity.this);
				double testLon = SavedInfo.getLon(MapViewActivity.this);
				mapCtrl.animateTo(
						new GeoPoint((int)(testLat*1e6), (int)(testLon*1e6))
				);
				mapCtrl.setZoom(21);
				
			}
		});

		findMe = (Button) findViewById(R.id.findMe);
		findMe.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//get phone's current location and animate
				GeoPoint point = new GeoPoint((int)(lat*1e6),(int)(lon*1e6));
				mapCtrl.animateTo(
						point
				);
				mapCtrl.setZoom(zoomLevel);
			}
		});

		locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
		locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);

	}
	@Override
	public void onBackPressed(){
		Log.d("CDA", "OnBackPressed Called");
		Intent setIntent = new Intent(Intent.ACTION_MAIN);
		setIntent.addCategory(Intent.CATEGORY_HOME);
		setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(setIntent);
		return;
	}
}

/* Parking Lot Locations
38.985175,-76.935099
38.985533,-76.934713
38.987885,-76.934316
38.987043,-76.941419
38.985175,-76.938672
38.985166,-76.938264
38.986742,-76.937492
38.987935,-76.934842
38.989177,-76.937224
38.989544,-76.937782
38.989903,-76.937964
38.989711,-76.9357
38.991979,-76.937728
38.993113,-76.937857
38.993147,-76.939219
*/
