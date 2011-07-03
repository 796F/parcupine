package com.test;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MapActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    TextView textview = new TextView(this);
        textview.setText("This is the album tab");
        setContentView(textview);
	    // TODO Auto-generated method stub
	}

}
