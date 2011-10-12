package com.test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.objects.Global;
import com.objects.SavedInfo;
import com.objects.ServerCalls;
import com.objects.ThrowDialog;

import android.app.Activity;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class DebugActivity extends Activity {
	
	private CountDownTimer timer;
	private TextView display;
	private Button debugbutton;
	private LocationManager locationManager;
	private String bestProvider;
	private RadioButton serverping;
	int remainSeconds;
	public static final String SAVED_INFO = "ParqMeInfo";
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.debuglayout);
	    SharedPreferences check = getSharedPreferences(SAVED_INFO,0);
	    
	    
	    
		serverping = (RadioButton) findViewById(R.id.radioButton1);
	    debugbutton = (Button) findViewById(R.id.debugButton1);
	    display = (TextView) findViewById(R.id.debugText1);

	    
	    
	    debugbutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				new Thread(new Runnable() {
				    public void run() {
				    	String host = "http://www.parqme.com";
						try {
							URL url = new URL(host);
							HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
							urlc.setConnectTimeout(1000 * 2); // mTimeout is in seconds
							urlc.connect();
							
							View z = getWindow().getDecorView().findViewById(R.layout.debuglayout);
							if (urlc.getResponseCode() == 200) {
								serverping.post(new Runnable(){

									@Override
									public void run() {
										serverping.setChecked(true);
										display.setText("trueth");
									}
									
									
								});
							}
						} catch (Exception e1) {
							serverping.setChecked(false);
							display.setText("FALSE LOL");
						} 
				    	
				    }
				  }).start();
				
				
				
				
			}
			
		});
	    
	}
}
