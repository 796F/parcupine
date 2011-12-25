package com.test;

/* TEST COMMIT.  HIIII*/


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.objects.SavedInfo;
import com.objects.ServerCalls;
import com.objects.ThrowDialog;
import com.objects.UserObject;

public class LoginActivity extends Activity {
	public static final String SAVED_INFO = "ParqMeInfo";
	private Button loginButton;
	private EditText emailForm;
	private EditText passwordForm;
	private Button register;
	private CheckBox rememberBox;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		rememberBox = (CheckBox) findViewById(R.id.rememberbox);
		rememberBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				SavedInfo.toggleRemember(LoginActivity.this);
				
			}
		});
		
		emailForm=(EditText) findViewById(R.id.emailForm);
		passwordForm = (EditText) findViewById(R.id.passwordForm);

		if(SavedInfo.isLoggedIn(this)){
			Intent x = new Intent(this, TabsActivity.class);
			x.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(x);
			finish();
		}
		
		loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String email = emailForm.getText().toString();
				String pass = passwordForm.getText().toString();
				UserObject user = ServerCalls.getUser(email, pass);
				if(user!=null){
					if (user.getUid() != -1) {
						SavedInfo.logIn(LoginActivity.this, user.getParkState(), email, user.getUid(), pass);
						startActivity(new Intent(LoginActivity.this,
								TabsActivity.class));
						finish();
					} else {
						ThrowDialog.show(LoginActivity.this, ThrowDialog.COULD_NOT_AUTH);
					}
				} else {
					ThrowDialog.show(LoginActivity.this, ThrowDialog.NO_NET);
				}
				
			}
		});
		register = (Button) findViewById(R.id.registerbutton);
		
		register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myintent = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(myintent);
			}});
	}
}


