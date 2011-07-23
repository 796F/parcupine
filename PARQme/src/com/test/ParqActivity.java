package com.test;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import com.quietlycoding.android.picker.NumberPickerDialog;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Test ZXing, confirm working.  
 * Add time and charge logic.
 * Add servlet calls for rates.
 * currently crashing on parqButton after user comes back from TimeLeft Activity.  
 * Buttons do not cancel correctly.  
 * */
	
public class ParqActivity extends ActivityGroup {
	
	private TextView priceDisplay;
    private Button parqButton;
    private Button parqButton2;
    private int mHour=0;
    private int mMinute=0;
    private int parkMinutes;
    static final int TIME_DIALOG_ID = 0;
    static final int OKAY_DIALOGUE_ID = 1;
	static final int NUM_PICKER_ID = 2;
    public static ParqActivity group;
    private ArrayList<View> history;
    
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.relative_parq);
	    
	    //load correct layout which has the time selector and camera view.  
	    priceDisplay = (TextView) findViewById(R.id.textView1);
	    parqButton = (Button) findViewById(R.id.firstparq);
	    parqButton2 = (Button) findViewById(R.id.secondparq);
	    
	    
	    parqButton.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
				parqButton.setVisibility(-1);
				parqButton2.setVisibility(1);
				showDialog(2);
			}});
	    
	    parqButton2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent("com.google.zxing.client.android.SCAN");
	    		//Intent intent = new Intent(ParqActivity.this, CaptureActivity.class);
	    		intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
	    		startActivityForResult(intent, 0);
				
				Intent myIntent = new Intent(ParqActivity.this, TimeLeft.class);
				myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				
				Bundle time = new Bundle();
				time.putInt("time",  parkMinutes );  //Bundle parktime with intent.
				myIntent.putExtras(time);
				
				parqButton.setVisibility(1);
				parqButton2.setVisibility(-1);
				
				
				View view = getLocalActivityManager().startActivity("TimeLeft",myIntent).getDecorView();
				//replaceView(view); //THERE IS AN ERROR when calling history.add(view);
				
				setContentView(view);
			}});
	}
	

	
	private void updateDisplay() {
	    priceDisplay.setText("park costs = cents in" +costToText(getCost(parkMinutes)));
	}
	
	
	
	private static boolean pingDB(){
		
		
		return false;
	}
	
	private static double getRates(int LOCATION_CODE){
		/*	Send http request to servlet with location code
		 *  servlet responds with a double indicating rate
		 *  
		 *  URLConnection test = new URLConnection("http://www.parqme.com/SERVLET");
		 * */
		return 25.0/15;
	}
	
	private static String costToText(double cost){
		
		return "" + cost;
	}

	private static double getCost(int mins) {
		return mins*getRates(0);
		/* myLocation = getLocation();
		 * return mins*getRates(myLocation);
		 * */
	}
	
	private NumberPickerDialog.OnNumberSetListener mNumberSetListener = 
		new NumberPickerDialog.OnNumberSetListener() {
			public void onNumberSet(int selectedNumber) {
				parkMinutes = selectedNumber;
				updateDisplay();
				
			}
		};

	    protected Dialog onCreateDialog(int id) {
	        switch (id) {
	        case NUM_PICKER_ID:
	        	NumberPickerDialog x =new NumberPickerDialog(this, 1, 0);
	        	x.setOnNumberSetListener(mNumberSetListener);
	        	return x;
	        }
	        return null;
	    }
	    
	    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
			   if (requestCode == 0) {
			      if (resultCode == RESULT_OK) {
			    	 int contentInt = intent.getIntExtra("SCAN_RESULT", Global.BAD_RESULT_CODE);
			         String contents = intent.getStringExtra("SCAN_RESULT");
			         String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
			         
			         /*scan results will contain an integer number, which represents a park location*/
			         
			         priceDisplay.setText(contents);
			         priceDisplay.setText(UserObject.email);
			         /*send request to server with email + contentInt + time*/
			         contactServer("test", contentInt, parkMinutes);
						

			      } else if (resultCode == RESULT_CANCELED) {
			    	  priceDisplay.setText("RESULT BAD GOT :(");
			         // Handle cancel
			      }
			   }
			}
	    
	    
	    public int contactServer(String email, int contentInt, int time){
	    	try {
	 			URL url = new URL("http://192.268.1.57:8080/UserBounce/UpdateDatabase");
	 			
	 			URLConnection servletConnection = url.openConnection();
	 			
	 			// inform the connection that we will send output and accept input
	         	servletConnection.setDoInput(true);
	         	servletConnection.setDoOutput(true);
	         	
	         	// Don't use a cached version of URL connection.
	         	servletConnection.setUseCaches(false);
	         	servletConnection.setDefaultUseCaches(false);
	         	
	         	// Specify the content type that we will send binary data
	         	servletConnection.setRequestProperty("Content-Type", "application/x-java-serialized-object");
	         	
	         	// get input and output streams on servlet
	         	ObjectOutputStream os = new ObjectOutputStream(servletConnection.getOutputStream());
	         	
	         	// send your data to the servlet
	         	os.writeObject("TESTEMAIL@JA.COM" +","+contentInt+","+parkMinutes);
	         	os.flush();
	         	os.close();
	         	
	         	ObjectInputStream iStream = new ObjectInputStream(servletConnection.getInputStream());
	         	int responseCode = iStream.readInt();
	         	
	         	if(responseCode==0){
	         		//spot taken
	         	}else if(responseCode==1){
	         		//spot okay
	         		return 1;
	         	}else if(responseCode==2){
	         		//jus tin case.  
	         	}else{
	         		//spoof code.  easter egg ascii art
	         	}

	         	/*read response code and interprit*/
	         	
	         }catch (Exception e){
	        	 e.printStackTrace();
	         }
			return 0;
	    }
	    public void back() {  
	        if(history.size() > 0) {  
	            history.remove(history.size()-1);  
	            setContentView(history.get(history.size()-1));  
	        }else {  
	            finish();  
	        }  
	    }  
	    public void replaceView(View v) {  
            // Adds the old one to history  
	    	history.add(v);  
            // Changes this Groups View to the new View.  
	    	setContentView(v);  
	    }
	    @Override  
	    public void onBackPressed() {  
	        ParqActivity.group.back();  
	        return;  
	    }
	    
}
