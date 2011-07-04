package com.test;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ParqActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.relative_parq);
	    //load correct layout which has the time selector and camera view.  
	    /*TextView textview = new TextView(this);
        textview.setText("This is the PARQ tab, main tab.  Will take picture of QR code, and load subsequent activities");
        setContentView(textview);*/
	}

}
