package com.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SlidingDrawer;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.objects.SavedInfo;
import com.objects.ServerCalls;
import com.objects.ServerCalls.UnparkCallback;
import com.objects.ServerCalls.newCallback;
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
	private AlertDialog autoRefillDialog;
	private static final int DIALOG_UNPARKING = 1;
	private static final int DIALOG_REGISTERING = 3;
	private String ccStub;
	public CheckBox getBox(){
		return (CheckBox) findViewById(R.id.vibrateEnable);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account);



		autoRefillDialog = makeAutoRefillDialog();

		emailDisplay = (TextView)findViewById(R.id.profile_email_small);
		emailDisplay.setText(SavedInfo.getEmail(AccountActivity.this));
		nameDisplay = (TextView)findViewById(R.id.profile_name);
		cardDisplay = (TextView)findViewById(R.id.profile_card_small);
		String displaystub = SavedInfo.getCardStub(AccountActivity.this);
		if(displaystub.equals("XXXX")){
			cardDisplay.setText("No Credit Card");
		}else{
			cardDisplay.setText("XXXX XXXX XXXX "+displaystub);
		}
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
						if(ServerCalls.editUser(check, null, newEmail, null)){
							edit.putString("email", newEmail);
							emailDisplay.setText(newEmail);
							edit.commit();
						}else{
							//throw alert
							ThrowDialog.show(AccountActivity.this, ThrowDialog.COULD_NOT_AUTH);
						}
					}else{
						ThrowDialog.show(AccountActivity.this, ThrowDialog.COULD_NOT_AUTH);
					}
				}else if (saveType==2){
					EditText test = (EditText) layout.findViewById(R.id.editText1);
					String name = test.getText().toString();
					edit.putString("city", name);
					cityDisplay.setText(name);
				}else if(saveType==3){
					EditText newHolderName = (EditText) layout.findViewById(R.id.new_card_name_box);
					EditText newCardNum = (EditText) layout.findViewById(R.id.new_cc_box);
					EditText newCSC = (EditText) layout.findViewById(R.id.csc_box);
					EditText newExpMonth = (EditText) layout.findViewById(R.id.new_exp_mon_box);
					EditText newExpYear = (EditText) layout.findViewById(R.id.new_exp_yr_box);
					EditText newBillingAddress = (EditText) layout.findViewById(R.id.new_bill_addr_box);
					EditText newZipcode = (EditText) layout.findViewById(R.id.new_zip_box);
					int nameSplit = newHolderName.getText().toString().split(" ").length;
					if(newCardNum.getText().toString().length()==16 && newCSC.getText().toString().length()==3 &&
							nameSplit>1){
						showDialog(DIALOG_REGISTERING);
						ccStub = newCardNum.getText().toString().substring(12,16);
						ServerCalls.editCreditCard(check, newHolderName.getText().toString(), newCardNum.getText().toString(), 
								newCSC.getText().toString(), Integer.valueOf(newExpMonth.getText().toString()), 2000+Integer.valueOf(newExpYear.getText().toString()),
								newBillingAddress.getText().toString(), newZipcode.getText().toString(), new newCallback(){

							@Override
							public void onEditCardComplete(boolean success) {
								if(success){
									removeDialog(DIALOG_REGISTERING);
									cardDisplay.setText("XXXX XXXX XXXX " + ccStub);
									SharedPreferences.Editor editz = check.edit();
									editz.putString("ccStub", ccStub);
									editz.commit();
								}else{
									removeDialog(DIALOG_REGISTERING);
									Toast.makeText(AccountActivity.this,
											"Your card could not be validated",
											Toast.LENGTH_SHORT).show();
								}
							}


						});
					}else{
						Toast.makeText(AccountActivity.this,
								"Please check your information.",
								Toast.LENGTH_SHORT).show();
					}

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


		//final Button editName = (Button) findViewById(R.id.profile_name_button);
		final Button editEmail= (Button) findViewById(R.id.profile_email_button);
		//final Button editCity= (Button) findViewById(R.id.profile_city_button);
		final Button editCard= (Button) findViewById(R.id.profile_card_button);
		final SlidingDrawer drawer = (SlidingDrawer) findViewById(R.id.pref_drawer);
		drawer.open();
		View.OnClickListener showEditText = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/*if(editName.isPressed()){
					showEditDialog(0, saveEditListener,cancelEditListener);
				}else */if(editEmail.isPressed()){
					showEditDialog(1, saveEditListener,cancelEditListener);
				}/*else if(editCity.isPressed()){
					showEditDialog(2, saveEditListener,cancelEditListener);
				}*/else if (editCard.isPressed()){
					showEditDialog(3, saveEditListener,cancelEditListener);
				}
			}
		};	



		//editName.setOnClickListener(showEditText);
		editEmail.setOnClickListener(showEditText);
		//editCity.setOnClickListener(showEditText);
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
		if(SavedInfo.vibrateEnable(AccountActivity.this)){
			vibrateBox.setChecked(true);
		}
		vibrateBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
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
		if(SavedInfo.ringEnable(AccountActivity.this)){
			ringBox.setChecked(true);
		}
		ringBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
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
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				if(isChecked){
					//user tries to check box
					autoRefillDialog.show();
				}else{
					refillBox.setChecked(false);
					SavedInfo.setGeneric(AccountActivity.this, "autoRefill", false);
				}
			}

		});

		logout = (Button) findViewById(R.id.logoutbutton);
		logout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// if not currently parked.
				if (SavedInfo.isParked(AccountActivity.this)) {
					new AlertDialog.Builder(AccountActivity.this)
					.setMessage("Are you sure you want to log out?\n You will be unparked.")
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							final SharedPreferences check = getSharedPreferences(
									SAVED_INFO, 0);
							final String parkId = SavedInfo.getParkRefNum(check);
							showDialog(DIALOG_UNPARKING);
							
							
							ServerCalls.unpark(SavedInfo.getSpotId(check), parkId, check, new UnparkCallback() {
								@Override
								public void onUnparkComplete(boolean success) {
									removeDialog(DIALOG_UNPARKING);
									if (success) {
										SavedInfo.logOut(AccountActivity.this);
										startActivity(new Intent(AccountActivity.this,
												LoginActivity.class));
										finish();
									} else {
										ThrowDialog.show(AccountActivity.this,
												ThrowDialog.UNPARK_ERROR);
									}
								}
							});
						}
					}).setNegativeButton("No", null).create().show();
				} else {
					SavedInfo.logOut(AccountActivity.this);
					startActivity(new Intent(AccountActivity.this, LoginActivity.class));
					finish();
				}
			}});

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == DIALOG_UNPARKING) {
			final ProgressDialog dialog = new ProgressDialog(this);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setMessage("Unparking...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			return dialog;
		} else if(id== DIALOG_REGISTERING){
			final ProgressDialog dialog = new ProgressDialog(this);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setMessage("Validating...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			return dialog;
		}else {
			return super.onCreateDialog(id);
		}


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

	private AlertDialog makeAutoRefillDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
		builder.setMessage("Are you sure you want to refill your meter automatically when time runs out?")
		.setCancelable(false)
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				//toggle auto-refill
				SavedInfo.setGeneric(AccountActivity.this, "autoRefill", true);
				refillBox.setChecked(true);
				dialog.cancel();
			}
		})
		.setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				//do nothing.  
				dialog.cancel();
				refillBox.setChecked(false);
			}
		});
		return builder.create();
	}



}


