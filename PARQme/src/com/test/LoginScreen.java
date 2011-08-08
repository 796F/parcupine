package com.test;


import com.objects.SavedInfo;
import com.objects.ThrowDialog;
import com.objects.UserObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class LoginScreen extends Activity {
	public static final String SAVED_INFO = "ParqMeInfo";
	private Button loginButton;
	private EditText emailForm;
	private EditText passwordForm;
	private Button register;
	private Button logout;
	private ViewFlipper LoginVf;
	private CheckBox rememberBox;
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
		LoginVf = (ViewFlipper) findViewById(R.id.reg_flip);
		
		rememberBox = (CheckBox) findViewById(R.id.rememberbox);
		rememberBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				SavedInfo.toggleRemember(LoginScreen.this);
				
			}
		});
		vibrateBox = (CheckBox) findViewById(R.id.vibrateEnable);
		vibrateBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				SavedInfo.toggleVibrate(LoginScreen.this);
				
			}
		});
		warningBox = (CheckBox) findViewById(R.id.warnEnable);
		warningBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				SavedInfo.toggleWarn(LoginScreen.this);
				
			}
		});
		ringBox = (CheckBox) findViewById(R.id.ringEnable);
		ringBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				SavedInfo.toggleRing(LoginScreen.this);
				
			}
		});
		
		refillBox = (CheckBox) findViewById(R.id.refillEnable);
		refillBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				SavedInfo.toggleRefill(LoginScreen.this);
				
			}
		});
		
		emailForm=(EditText) findViewById(R.id.emailForm);
		passwordForm = (EditText) findViewById(R.id.passwordForm);
		final SharedPreferences check = getSharedPreferences(SAVED_INFO,0);

		if(check.getBoolean("loginState", false)){
			LoginVf.showNext();
		}
		
		loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String email = emailForm.getText().toString();
				String pass = passwordForm.getText().toString();
				UserObject user = ServerCalls.getUser(email, pass);
				if(user!=null){
					SharedPreferences.Editor editor = check.edit();
					LoginVf.showNext();
					
					TabsActivity.topright.setText(user.getFname());
					
					editor.putString("fname", user.getFname());
					TextView fname = (TextView) findViewById(R.id.accinfolabel);
					fname.setText(user.getFname()+" "+user.getLname());
					
					TextView emailField = (TextView) findViewById(R.id.accemailfield);
					emailField.setText("Email: " + user.getEmail());
					
					TextView phone = (TextView) findViewById(R.id.creditcardfield);
					phone.setText(user.getPhone());
					
					editor.putBoolean("loginState", true);
					editor.putString("email", email);
					editor.commit();
				}else{
					ThrowDialog.show(LoginScreen.this, ThrowDialog.COULD_NOT_AUTH);
				}
				
			}
		});
		register = (Button) findViewById(R.id.registerbutton);
		
		register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myintent = new Intent(LoginScreen.this, WebRegister.class);
				startActivity(myintent);
			}});
		
		
		logout = (Button) findViewById(R.id.logoutbutton);
		
		logout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//if not currently parked.
				if(!check.getBoolean("parkState", false)){
					LoginVf.showPrevious();
					TabsActivity.topright.setText("Not Logged In");
					SavedInfo.toggleLogin(LoginScreen.this);
				}else{
					ThrowDialog.show(LoginScreen.this, ThrowDialog.IS_PARKED);
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
	public void onBackPressed(){
		Log.d("CDA", "OnBackPressed Called");
		Intent setIntent = new Intent(Intent.ACTION_MAIN);
		setIntent.addCategory(Intent.CATEGORY_HOME);
		setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(setIntent);
    	return;
    }
}


