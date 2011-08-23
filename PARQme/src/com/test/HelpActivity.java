package com.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Stack;

import com.objects.SavedInfo;
import com.quietlycoding.android.picker.NumberPicker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class HelpActivity extends Activity {
	private Button nextButton;
	private Button prevButton;
	public static final String SAVED_INFO = "ParqMeInfo";
	private TextView phpre;
	public String time;
	public ViewFlipper vf;
	public View mainView;
	private ImageView imv;
	private int mark;
	private int[] test;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        setContentView(R.layout.set_time);
        NumberPicker x = (NumberPicker) findViewById(R.id.parktimepicker);
        
//        nextButton = (Button) findViewById(R.id.tutNextPage);
//        prevButton = (Button) findViewById(R.id.tutPrevPage);
//        //phpre = (TextView) findViewById(R.id.phpresponse);
//        imv = (ImageView) findViewById(R.id.tutImageView);
//        test = new int[5];
//        test[0]= R.drawable.page1;
//        test[1]= R.drawable.page2;
//        test[2]= R.drawable.page3;
//        test[3]= R.drawable.page4;
//        test[4]= R.drawable.page5;
//        
//        mark = 0;
//        nextButton.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if(mark<4){
//				mark++;
//				imv.setImageResource(test[mark]);
//				}
//			}
//		});
//        prevButton.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				//push current image onto fwd, pop off back stack and display
//				if(mark>0){
//				mark--;
//				imv.setImageResource(test[mark]);
//				}
//			}
//		});
        
       
	}
	

}
