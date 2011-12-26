package com.test;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;
import android.widget.Toast;
 
public class Preferences extends PreferenceActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                addPreferencesFromResource(R.xml.account_page);
                // Get the custom preference
                getListView().setBackgroundColor(Color.TRANSPARENT);
                getListView().setCacheColorHint(Color.TRANSPARENT);
        }
}