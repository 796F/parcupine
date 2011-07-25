package com.test;

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
import android.widget.ViewFlipper;

public class LoginScreen extends Activity {
	public static final String SAVED_INFO = "ParqMeInfo";
	private Button loginButton;
	private EditText emailForm;
	private EditText passwordForm;
	private Button goWeb;
	private Button logout;
	private ViewFlipper vf;
	private CheckBox box;
	/** Called when the activity is first created. */
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
				if (email.equals(UserObject.email) && pass.equals(UserObject.passHash)){
					SharedPreferences.Editor editor = check.edit();
					vf.showNext();
					editor.putBoolean("loginState", true);
					//SharedPreferences login = getSharedPreferences(SAVED_INFO, 0);
					if(box.isChecked()){
						editor.putBoolean("remember", true);
					}else{
						editor.putBoolean("remember", false);
					}
					editor.commit();
				}else{
					AlertDialog.Builder alert = new AlertDialog.Builder(LoginScreen.this);
					alert.setMessage("Could Not Log In");
					alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
					AlertDialog a = alert.create();
					a.show();
				}
			}
		});
		goWeb = (Button) findViewById(R.id.registerbutton);
		goWeb.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//open website
				Intent myintent = new Intent(LoginScreen.this, WebRegister.class);
				startActivity(myintent);
			}});
		logout = (Button) findViewById(R.id.logoutbutton);
		logout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//open website
				vf.showPrevious();
				//SharedPreferences logout = getSharedPreferences(SAVED_INFO,0);
				SharedPreferences.Editor editor = check.edit();
				editor.putBoolean("loginState", false);
				editor.commit();
			}});
		
		
	}
}


