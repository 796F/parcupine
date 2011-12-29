package com.test;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Debug;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.objects.SavedInfo;
import com.objects.ServerCalls;
import com.objects.ThrowDialog;

public class AccountActivity extends Activity {
	public static final String SAVED_INFO = "ParqMeInfo";
	private Button logout;

	private CheckBox vibrateBox;
	private CheckBox ringBox;
	private CheckBox refillBox;

	private Button editName;
	private Button editEmail;
	private Button editCity;
	private Button editCard;

	private EditText editNameBox;
	private EditText editEmailBox;
	private EditText editCityBox;
	private EditText editCardBox;
	
	//these allow us to circumvent data from custom dialogs into this class. 
	int saveType = -1;
    private View myPrivateView = null;
    private View layout = null;
    
	private TextView emailDisplay;
	private TextView nameDisplay;
	private TextView cityDisplay;
	private TextView cardDisplay;
	
	public CheckBox getBox(){
		return (CheckBox) findViewById(R.id.vibrateEnable);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account);
		emailDisplay = (TextView)findViewById(R.id.profile_email_small);
		nameDisplay = (TextView)findViewById(R.id.profile_name);
		final SharedPreferences check = getSharedPreferences(SAVED_INFO,0);
	    final DialogInterface.OnClickListener saveEditListener = new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				SharedPreferences.Editor edit = check.edit();
				if(saveType==0){
					EditText test = (EditText) layout.findViewById(R.id.editText1);
					String name = test.getText().toString();
					edit.putString("fullName", name);
					nameDisplay.setText(name);
				}else if (saveType==1){
					EditText test = (EditText) layout.findViewById(R.id.current_email_box);
					String oldEmail = test.getText().toString();
					if(oldEmail.equals(check.getString("email", ""))){
						test = (EditText) layout.findViewById(R.id.new_email_box);
						String newEmail = test.getText().toString();
						edit.putString("email", newEmail);
						emailDisplay.setText(newEmail);
					}else{
						ThrowDialog.show(AccountActivity.this, ThrowDialog.COULD_NOT_AUTH);
					}
				}else if (saveType==2){
					EditText test = (EditText) layout.findViewById(R.id.editText1);
					String name = test.getText().toString();
					edit.putString("city", name);
					cityDisplay.setText(name);
				}else if(saveType==3){
					//on server side, delete old account, register new account, send back the new uid and stuff to sync app.  
				}
				edit.commit();
				dialog.cancel();
			}
			
		};
		final DialogInterface.OnClickListener cancelEditListener = new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
			
		};

		
		final Button editName = (Button) findViewById(R.id.profile_name_button);
		final Button editEmail= (Button) findViewById(R.id.profile_email_button);
		final Button editCity= (Button) findViewById(R.id.profile_city_button);
		final Button editCard= (Button) findViewById(R.id.profile_card_button);

		
		View.OnClickListener showEditText = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(editName.isPressed()){
					showEditDialog(0, saveEditListener,cancelEditListener);
				}else if(editEmail.isPressed()){
					showEditDialog(1, saveEditListener,cancelEditListener);
				}else if(editCity.isPressed()){
					showEditDialog(2, saveEditListener,cancelEditListener);
				}else if (editCard.isPressed()){
					showEditDialog(3, saveEditListener,cancelEditListener);
				}
			}
		};	

	
		
		editName.setOnClickListener(showEditText);
		editEmail.setOnClickListener(showEditText);
		editCity.setOnClickListener(showEditText);
		editCard.setOnClickListener(showEditText);


		//		ListView lv = (ListView) findViewById(R.id.listView1);
		//		 final String[] COUNTRIES = new String[] {
		//			    "8/25, Nebraska Ave", "8/26, Nebraska Ave","8/27, 5th & G","9/22, Nebraska Ave","9/27, 5th & G"
		//			  };
		//		lv.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, COUNTRIES));
		//
		//		lv.setVerticalFadingEdgeEnabled(false);
		//		lv.setTextFilterEnabled(true);
		//		lv.setOnItemClickListener(new OnItemClickListener() {
		//		    @Override
		//			public void onItemClick(AdapterView<?> parent, View view,
		//		        int position, long id) {
		//		      // When clicked, show a toast with the TextView text
		//		      Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
		//		          Toast.LENGTH_SHORT).show();
		//		    }
		//		  });


		vibrateBox = (CheckBox) findViewById(R.id.vibrate_checkbox);
		vibrateBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(vibrateBox.isChecked()){
					SavedInfo.setGeneric(AccountActivity.this, "vibrateEnable", true);
					//set true in file
				}else{
					//set false in file
					SavedInfo.setGeneric(AccountActivity.this, "vibrateEnable", false);
				}

			}
		});
		ringBox = (CheckBox) findViewById(R.id.alarm_checkbox);
		ringBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(ringBox.isChecked()){
					SavedInfo.setGeneric(AccountActivity.this, "ringEnable", true);
					//set true in file
				}else{
					//set false in file
					SavedInfo.setGeneric(AccountActivity.this, "ringEnable", false);
				}
			}
		});

		refillBox = (CheckBox) findViewById(R.id.refill_checkbox);
		refillBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(refillBox.isChecked()){
					SavedInfo.setGeneric(AccountActivity.this, "autoRefill", true);
					//set true in file
				}else{
					//set false in file
					SavedInfo.setGeneric(AccountActivity.this, "autoRefill", false);
				}
			}
		});

		logout = (Button) findViewById(R.id.logoutbutton);

		logout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// if not currently parked.
				if (SavedInfo.isParked(check)) {
					new AlertDialog.Builder(AccountActivity.this)
					.setMessage("Are you sure you want to log out?\n You will be unparked.")
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							final SharedPreferences check = getSharedPreferences(
									SAVED_INFO, 0);
							final String parkId = SavedInfo.getParkId(check);
							if (ServerCalls.unPark(111, parkId, check)) {
							} else {
								ThrowDialog.show(AccountActivity.this,
										ThrowDialog.UNPARK_ERROR);
							}

							SavedInfo.logOut(AccountActivity.this);
							startActivity(new Intent(AccountActivity.this,
									LoginActivity.class));
							finish();
						}
					}).setNegativeButton("No", null).create().show();
				} else {
					SavedInfo.logOut(AccountActivity.this);
					startActivity(new Intent(AccountActivity.this, LoginActivity.class));
					finish();
				}
			}});

	}
	
	public void showEditDialog(int i, DialogInterface.OnClickListener a,DialogInterface.OnClickListener b){
		AlertDialog.Builder alert = new AlertDialog.Builder(AccountActivity.this);     
	    LayoutInflater  factory = LayoutInflater.from(AccountActivity.this);
	    String g = null;
	    switch(i){
	    	case 0:
	    		layout =factory.inflate(R.layout.edit_text_dialog ,null);
	    		g = "Name";
	    		break;
	    	case 1:
	    		layout = factory.inflate(R.layout.edit_email_layout, null);
	    		g = "Email";
	    		break;
	    	case 2:
	    		layout = factory.inflate(R.layout.edit_text_dialog, null);
	    		g = "City";
	    		break;
	    	case 3:
	    		layout = factory.inflate(R.layout.edit_card_layout, null);
	    		g = "Card";
	    		break;
	    }
	    saveType = i;
	    alert.setView(layout); 
	    alert.setPositiveButton("Save", a);
	    alert.setNegativeButton("Cancel", b);
	    alert.setTitle("Edit " + g);
		AlertDialog gg = alert.create();
		gg.show();
	}
	
}


