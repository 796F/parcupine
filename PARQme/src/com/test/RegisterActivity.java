package com.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.objects.SavedInfo;
import com.objects.ServerCalls;
import com.objects.ThrowDialog;
import com.objects.UserObject;

public class RegisterActivity extends Activity {
	ViewFlipper flipper;
	EditText emailBox;
	EditText passwordBox;
	EditText confirmBox;
	EditText nameBox;
	EditText ccBox;
	EditText cscBox;
	EditText zipBox;
	EditText streetBox;
	Spinner expMonthSpinner;
	Spinner expYearSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		flipper = (ViewFlipper)findViewById(R.id.flipper);
		emailBox = (EditText)findViewById(R.id.emailBox);
		passwordBox = (EditText)findViewById(R.id.passwordBox);
		confirmBox = (EditText)findViewById(R.id.confirmBox);
		nameBox = (EditText)findViewById(R.id.nameBox);
		ccBox = (EditText)findViewById(R.id.ccBox);
		cscBox = (EditText)findViewById(R.id.cscBox);
		zipBox = (EditText)findViewById(R.id.zipBox);
		streetBox = (EditText)findViewById(R.id.streetBox);
		
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

	    final Button backButton = (Button) findViewById(R.id.back);
	    backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				flipper.showPrevious();
			}
		});
	    final Button nextButton = (Button) findViewById(R.id.next);
	    nextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final InvalidField field = validateScreen1();
				if (field == InvalidField.ALL_OK) {
					flipper.showNext();
				} else {
					Toast.makeText(RegisterActivity.this, field.getErrorMsg(), Toast.LENGTH_LONG).show();
				}
			}
		});
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
				final InvalidField field = validateScreen2();
				if (field == InvalidField.ALL_OK) {
					final String email = emailBox.getText().toString();
					final String password = passwordBox.getText().toString();
					final String name = nameBox.getText().toString();
					final String ccNumber = ccBox.getText().toString();
					final String cscNumber = cscBox.getText().toString();
					final int expMonth = expMonthSpinner.getSelectedItemPosition() + 1;
					final int expYear = Integer.valueOf(expYearSpinner.getSelectedItem().toString());
					final String zip = zipBox.getText().toString();
					final String addr = streetBox.getText().toString();
					if (ServerCalls.registerNewUser(email, password, name, ccNumber, cscNumber, expMonth, expYear, zip, addr)) {
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
				} else {
					Toast.makeText(RegisterActivity.this, field.getErrorMsg(), Toast.LENGTH_LONG).show();
				}
			}
		});
    }

    private enum InvalidField {
        ALL_OK      (null),
        EMAIL       ("Please provide a valid email address"),
        PASSWORD    ("Your password must be at least 6 characters long"),
        PW_CONFIRM  ("Your passwords did not match"),
        NAME        ("Please enter your full name as it appears on your credit card"),
        CCNUM       ("Please provide a valid credit card number"),
        CSC         ("Please provide your 3-digit credit card security code"),
        STREET      ("Please provide your street address"),
        ZIP         ("Please provide your 5-digit ZIP code");

        private final String errorMsg;

        InvalidField(String errorMsg) {
            this.errorMsg = errorMsg;
        }

        public String getErrorMsg() {
            return errorMsg;
        }
    }

    private InvalidField validateScreen1() {
        final String[] email = emailBox.getText().toString().split("@");
        if (email.length != 2 || email[0].length() <= 0) {
            return InvalidField.EMAIL;
        }
        final String[] host = email[1].split("\\.");
        if (host.length != 2 || host[0].length() <= 0 || host[1].length() <= 1) {
            return InvalidField.EMAIL;
        }
        final CharSequence password = passwordBox.getText();
        if (password.length() <= 5) {
            return InvalidField.PASSWORD;
        }
        if (!password.toString().equals(confirmBox.getText().toString())) {
            return InvalidField.PW_CONFIRM;
        }
        return InvalidField.ALL_OK;
    }

    private InvalidField validateScreen2() {
        if (nameBox.getText().length() <= 1) {
            return InvalidField.NAME;
        }
        final int ccLen = ccBox.getText().length();
        if (ccLen < 8 || ccLen > 19) {
            return InvalidField.CCNUM;
        }
        if (cscBox.getText().length() != 3) {
            return InvalidField.CSC;
        }
        if (streetBox.getText().length() <= 0) {
            return InvalidField.STREET;
        }
        if (zipBox.getText().length() != 5) {
            return InvalidField.ZIP;
        }
        return InvalidField.ALL_OK;
    }
}
