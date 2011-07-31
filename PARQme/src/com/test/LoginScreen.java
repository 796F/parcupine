package com.test;


import com.objects.SavedInfo;
import com.objects.ThrowDialog;
import com.objects.UserObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
	private ViewFlipper vf;
	private CheckBox box;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reg_flip);
		vf = (ViewFlipper) findViewById(R.id.reg_flip);
		box = (CheckBox) findViewById(R.id.checkBox1);
		
		emailForm=(EditText) findViewById(R.id.emailForm);
		passwordForm = (EditText) findViewById(R.id.passwordForm);
		final SharedPreferences check = getSharedPreferences(SAVED_INFO,0);

		if(check.getBoolean("loginState", false)){
			vf.showNext();
		}
		
		loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String email = emailForm.getText().toString();
				String pass = passwordForm.getText().toString();
				UserObject user = ServerCalls.getUser(email, pass);
				if(user!=null){
					SharedPreferences.Editor editor = check.edit();
					vf.showNext();
					editor.putString("fname", user.getFname());
					TextView fname = (TextView) findViewById(R.id.accinfolabel);
					fname.setText(user.getFname()+" "+user.getLname());
					
					TextView emailField = (TextView) findViewById(R.id.accemailfield);
					emailField.setText("Email: " + user.getEmail());
					
					TextView phone = (TextView) findViewById(R.id.creditcardfield);
					phone.setText(user.getPhone());
					
					
					editor.putBoolean("loginState", true);
					editor.putString("email", email);
					//SharedPreferences login = getSharedPreferences(SAVED_INFO, 0);
					if(box.isChecked()){
						editor.putBoolean("remember", true);
					}else{
						editor.putBoolean("remember", false);
					}
					editor.commit();
				}else{
					ThrowDialog.show(LoginScreen.this, ThrowDialog.COULD_NOT_AUTH);
				}
				// CURRENTLY SENDING CLEAR TEXT.  ENCRYPT LATER.
//				if (ServerCalls.getAuth(email, pass)==1){
//					SharedPreferences.Editor editor = check.edit();
//					vf.showNext();
//					SavedInfo.toggleLogin(LoginScreen.this);
//					if(box.isChecked()){
//						editor.putBoolean("remember", true);
//					}else{
//						editor.putBoolean("remember", false);
//					}
//					editor.commit();
//				}else{
//					ThrowDialog.show(LoginScreen.this, ThrowDialog.COULD_NOT_AUTH);
//				}
			}
		});
		register = (Button) findViewById(R.id.registerbutton);
		
		register.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent myintent = new Intent(LoginScreen.this, WebRegister.class);
				startActivity(myintent);
			}});
		
		
		logout = (Button) findViewById(R.id.logoutbutton);
		
		logout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//if not currently parked.
				if(!check.getBoolean("parkState", false)){
					vf.showPrevious();
					SavedInfo.toggleLogin(LoginScreen.this);
				}else{
					ThrowDialog.show(LoginScreen.this, ThrowDialog.IS_PARKED);
				}
			}});
		
		
	}
}


