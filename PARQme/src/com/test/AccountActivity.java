package com.test;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.objects.SavedInfo;
import com.objects.ThrowDialog;

public class AccountActivity extends Activity {
	public static final String SAVED_INFO = "ParqMeInfo";
	private Button logout;
	private ViewFlipper LoginVf;
	public static CheckBox vibrateBox;
	public static CheckBox warningBox;
	private CheckBox ringBox;
	private CheckBox refillBox;
	private Button settings;
	private Button back;
	
	public CheckBox getBox(){
		return (CheckBox) findViewById(R.id.vibrateEnable);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reg_flip);
		
		ListView lv = (ListView) findViewById(R.id.listView1);
		 final String[] COUNTRIES = new String[] {
			    "8/25, Nebraska Ave", "8/26, Nebraska Ave","8/27, 5th & G","9/22, Nebraska Ave","9/27, 5th & G"
			  };
		lv.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, COUNTRIES));

		lv.setVerticalFadingEdgeEnabled(false);
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new OnItemClickListener() {
		    @Override
			public void onItemClick(AdapterView<?> parent, View view,
		        int position, long id) {
		      // When clicked, show a toast with the TextView text
		      Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
		          Toast.LENGTH_SHORT).show();
		    }
		  });
		
		LoginVf = (ViewFlipper) findViewById(R.id.reg_flip);
		final SharedPreferences check = getSharedPreferences(SAVED_INFO,0);
		vibrateBox = (CheckBox) findViewById(R.id.vibrateEnable);
		vibrateBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(vibrateBox.isChecked()){
					SavedInfo.setGeneric(AccountActivity.this, "vibrateEnable", true);
					//set true in file
				}else{
					//set false in file
					SavedInfo.setGeneric(AccountActivity.this, "vibrateEnable", false);
				}
				
			}
		});
		warningBox = (CheckBox) findViewById(R.id.warnEnable);
		warningBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(warningBox.isChecked()){
				SavedInfo.setGeneric(AccountActivity.this, "warningEnable", true);
				//set true in file
			}else{
				//set false in file
				SavedInfo.setGeneric(AccountActivity.this, "warningEnable", false);
			}
			}
		});
		ringBox = (CheckBox) findViewById(R.id.ringEnable);
		ringBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(ringBox.isChecked()){
					SavedInfo.setGeneric(AccountActivity.this, "ringEnable", true);
					//set true in file
				}else{
					//set false in file
					SavedInfo.setGeneric(AccountActivity.this, "ringEnable", false);
				}
			}
		});
		
		refillBox = (CheckBox) findViewById(R.id.refillEnable);
		refillBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(refillBox.isChecked()){
					SavedInfo.setGeneric(AccountActivity.this, "autoRefill", true);
					//set true in file
				}else{
					//set false in file
					SavedInfo.setGeneric(AccountActivity.this, "autoRefill", false);
				}
			}
		});

		logout = (Button) findViewById(R.id.logoutbutton);
		
		logout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//if not currently parked.
				if(!check.getBoolean("parkState", false)){
					TabsActivity.topright.setText("Not Logged In");
					SavedInfo.toggleLogin(AccountActivity.this);
					startActivity(new Intent(AccountActivity.this, LoginActivity.class));
					finish();
				}else{
					ThrowDialog.show(AccountActivity.this, ThrowDialog.IS_PARKED);
				}
			}});
		
		settings = (Button) findViewById(R.id.setting);
		settings.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				LoginVf.showNext();
			}
		});
		back = (Button) findViewById(R.id.settingback);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				LoginVf.showPrevious();
			}
		});
	}
}


