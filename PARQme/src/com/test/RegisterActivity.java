package com.test;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.objects.SavedInfo;
import com.objects.ServerCalls;
import com.objects.ThrowDialog;
import com.objects.UserObject;

public class RegisterActivity extends Activity {
	EditText emailBox;
	EditText passwordBox;
	EditText confirmBox;
	EditText nameBox;
	EditText ccBox;
	EditText cscBox;
	EditText streetBox;
	EditText zipBox;
	Spinner expMonthSpinner;
	Spinner expYearSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		emailBox = (EditText)findViewById(R.id.emailBox);
		passwordBox = (EditText)findViewById(R.id.passwordBox);
		confirmBox = (EditText)findViewById(R.id.confirmBox);
		nameBox = (EditText)findViewById(R.id.nameBox);
		ccBox = (EditText)findViewById(R.id.ccBox);
		cscBox = (EditText)findViewById(R.id.cscBox);
		streetBox = (EditText)findViewById(R.id.streetBox);
		zipBox = (EditText)findViewById(R.id.zipBox);

		expMonthSpinner = (Spinner) findViewById(R.id.expmonth_spinner);
	    final ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(
	            this, R.array.exp_months_array, android.R.layout.simple_spinner_item);
	    monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    expMonthSpinner.setAdapter(monthAdapter);

		expYearSpinner = (Spinner) findViewById(R.id.expyear_spinner);
	    final ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(
	            this, R.array.exp_years_array, android.R.layout.simple_spinner_item);
	    yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    expYearSpinner.setAdapter(yearAdapter);

		final Button cancelButton = (Button)findViewById(R.id.cancel);
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		final Button submitButton = (Button)findViewById(R.id.submit);
		submitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (validates()) {
					final String email = emailBox.getText().toString();
					final String password = passwordBox.getText().toString();
					final String name = nameBox.getText().toString();
					final String ccNumber = ccBox.getText().toString();
					final String cscNumber = cscBox.getText().toString();
					final int expMonth = expMonthSpinner.getSelectedItemPosition() + 1;
					final int expYear = Integer.valueOf(expYearSpinner.getSelectedItem().toString());
					final String street = streetBox.getText().toString();
					final String zip = zipBox.getText().toString();
					if (ServerCalls.registerNewUser(email, password, name, ccNumber, cscNumber, expMonth, expYear, street, zip)) {
						UserObject user = ServerCalls.getUser(email, password);
						if(user!=null){
							if (user.getUid() != -1) {
								SavedInfo.logIn(RegisterActivity.this, false,
										email, user.getUid(), password);
								startActivity(new Intent(RegisterActivity.this,
										TabsActivity.class));
								finish();
							} else {
								ThrowDialog.show(RegisterActivity.this, ThrowDialog.COULD_NOT_AUTH);
							}
						}else{
							ThrowDialog.show(RegisterActivity.this, ThrowDialog.NO_NET);
						}
					} else {
						Toast.makeText(RegisterActivity.this, "An error occurred during registration", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
    }

	private boolean validates() {
		boolean valid = true;
		/*
		final String[] email = emailBox.getText().toString().split("@");
		valid &= email.length == 2;
		valid &= email[0].length() > 0;
		final String[] host = email[1].split(".");
		valid &= host.length == 2;
		valid &= host[0].length() > 0;
		valid &= host[1].length() > 1;
		final CharSequence password = passwordBox.getText();
		valid &= password.length() > 5;
		valid &= password.equals(confirmBox.getText());
		valid &= nameBox.getText().length() > 0;
		final CharSequence ccNum = ccBox.getText();
		valid &= ccNum.length() == 16;
		valid &= cscBox.getText().length() == 3;
		valid &= streetBox.getText().length() > 0;
		valid &= zipBox.getText().length() == 5;
		 */
		return valid;
	}
}
