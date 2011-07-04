package com.test;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.maps.MapActivity;

public class MapViewActivity extends MapActivity {

	/** Called when the activity is first created. */
	
	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.map);
	}

}
