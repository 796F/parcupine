package com.test;

import com.test.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class WebRegister extends Activity {
    WebView mWebView;
    private Button goAppButton;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.webframe);

            mWebView = (WebView) findViewById(R.id.webview);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.loadUrl("http://www.parqme.com/register/register.mobile.php");
            //TODO:  add back button graphic
            goAppButton = (Button) findViewById(R.id.goApp);
            goAppButton.setOnClickListener(new View.OnClickListener() {
                @Override
				public void onClick(View v) {
                	  finish();
                    
                }});
    }

}
