package com.test;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class TabsActivity extends TabActivity {
	public static final String SAVED_INFO = "ParqMeInfo";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Resources res = getResources(); 
        TabHost tabHost = getTabHost(); 
        TabHost.TabSpec spec;  
        Intent intent;  

        intent = new Intent().setClass(this, MainActivity.class);
        spec = tabHost.newTabSpec("parq").setIndicator("PARQ",
        		res.getDrawable(R.drawable.ic_tab_camera))
                      .setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, MapViewActivity.class);
        spec = tabHost.newTabSpec("map").setIndicator("Map",
                          res.getDrawable(R.drawable.ic_tab_map))
                      .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, LoginScreen.class);
        spec = tabHost.newTabSpec("info").setIndicator("Account",
                          res.getDrawable(R.drawable.ic_tab_account))
                      .setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, HelpActivity.class);
        spec = tabHost.newTabSpec("test").setIndicator("Help",
                          res.getDrawable(R.drawable.ic_tab_help))
                      .setContent(intent);
        tabHost.addTab(spec);
        SharedPreferences check = getSharedPreferences(SAVED_INFO,0);

        if(check.getBoolean("loginState", false))
        	tabHost.setCurrentTab(0);
        else
        	tabHost.setCurrentTab(2);
    }
    
}