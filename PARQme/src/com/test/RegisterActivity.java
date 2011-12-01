package com.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.objects.ServerCalls;

public class RegisterActivity extends Activity {
	EditText emailBox;
	EditText passwordBox;
	EditText confirmBox;
	EditText nameBox;
	EditText ccBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		emailBox = (EditText)findViewById(R.id.emailBox);
		passwordBox = (EditText)findViewById(R.id.passwordBox);
		confirmBox = (EditText)findViewById(R.id.confirmBox);
		nameBox = (EditText)findViewById(R.id.nameBox);
		ccBox = (EditText)findViewById(R.id.ccBox);

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
					if (ServerCalls.registerNewUser(email, password, name, ccNumber)) {
						Toast.makeText(RegisterActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
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
    	valid &= TextUtils.isDigitsOnly(ccNum);
    	*/
		return valid;
    }
}
