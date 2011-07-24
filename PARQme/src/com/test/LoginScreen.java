package com.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginScreen extends Activity {

	private Button loginButton;
	private EditText emailForm;
	private EditText passwordForm;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.login);
	      
	      emailForm=(EditText) findViewById(R.id.emailForm);
	      passwordForm = (EditText) findViewById(R.id.passwordForm);
	      
	      loginButton = (Button) findViewById(R.id.loginButton);
	      loginButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					String email = emailForm.getText().toString();
					String pass = passwordForm.getText().toString();
					if (email.equals(UserObject.email) && pass.equals(UserObject.passHash)){
						Intent myIntent = new Intent(LoginScreen.this, TabsActivity.class);
						startActivity(myIntent);
						finish();
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
	   }
	}


