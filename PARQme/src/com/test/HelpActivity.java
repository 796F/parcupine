package com.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.objects.SavedInfo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HelpActivity extends Activity {
	private Button testphp;
	public static final String SAVED_INFO = "ParqMeInfo";
	private TextView phpre;
	public String time;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        setContentView(R.layout.myhelp);
        testphp = (Button) findViewById(R.id.testbutton);
        phpre = (TextView) findViewById(R.id.phpresponse);
        
        testphp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences check = getSharedPreferences(SAVED_INFO,0);
				phpre.setText(check.getAll().toString());
				//MainActivity.vf.showPrevious();
				//SavedInfo.togglePark(HelpActivity.this);
				//SavedInfo.reset(HelpActivity.this);
				
			}
		});
        
        
       
	}
	public static String formatMe(int seconds){
		SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss");
		try {
			Date x = sdf.parse("00:00:00");
			x.setSeconds(seconds);
			return (sdf.format(x));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "BADBADBAD";
	}
	public static long minToMil(int minutes){
    	return minutes*60000;
    }
	

}
