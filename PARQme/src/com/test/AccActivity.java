package com.test;

import com.test.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AccActivity extends Activity {
    private Button goWeb;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);
        goWeb = (Button) findViewById(R.id.goWebNow);
        
        goWeb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //open website
                Intent myintent = new Intent(AccActivity.this, WebRegister.class);
                startActivity(myintent);
            }});
    }

}
