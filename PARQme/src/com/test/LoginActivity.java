package com.test;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.objects.SavedInfo;
import com.objects.ServerCalls;
import com.objects.ThrowDialog;
import com.objects.UserObject;

public class LoginActivity extends Activity {
	private EditText emailForm;
	private EditText passwordForm;
	private CheckBox rememberBox;

	private static final int DIALOG_LOGGING_IN = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

        if (SavedInfo.isLoggedIn(this)) {
            Intent x = new Intent(this, TabsActivity.class);
            x.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(x);
            finish();
        }

		rememberBox = (CheckBox) findViewById(R.id.rememberbox);
		rememberBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				SavedInfo.toggleRemember(LoginActivity.this);
			}
		});

		emailForm=(EditText) findViewById(R.id.emailForm);
		passwordForm = (EditText) findViewById(R.id.passwordForm);
		passwordForm.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onLoginClick(null);
                    return true;
                }
                return false;
            }
        });
	}

    public void onLoginClick(View view) {
        final String email = emailForm.getText().toString();
        final String pass = passwordForm.getText().toString();
        showDialog(DIALOG_LOGGING_IN);
        ServerCalls.authUser(email, pass, new ServerCalls.AuthUserCallback() {
            @Override
            public void onAuthUserComplete(UserObject user) {
                removeDialog(DIALOG_LOGGING_IN);
                if (user == null) {
                    ThrowDialog.show(LoginActivity.this, ThrowDialog.NO_NET);
                    return;
                }
                if (user.getUid() == -1) {
                    ThrowDialog.show(LoginActivity.this, ThrowDialog.COULD_NOT_AUTH);
                    return;
                }
                if (user.getParkState()) {
                    SavedInfo.syncParkingSession(LoginActivity.this, user.getSync());
                }
                SavedInfo.logIn(LoginActivity.this, user.getParkState(), email, user.getUid(), pass, user.getCreditCardStub());
                startActivity(new Intent(LoginActivity.this, TabsActivity.class));
                finish();
            }
        });
    }

	public void onRegisterClick(View view) {
        Intent myintent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(myintent);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_LOGGING_IN) {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Logging in...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            return dialog;
        } else {
            return super.onCreateDialog(id);
        }
    }
   
}


