/* 
 * MainActivity represents the first tab, where users can scan a qr code,
 * select the amount of time they want to park, and park it.  Also while
 * parked, they are able to refill time and unpark themselves.  
 * 
 * */

package com.test;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.objects.ParkInstanceObject;
import com.objects.RateObject;
import com.objects.RateResponse;
import com.objects.SavedInfo;
import com.objects.ServerCalls;
import com.objects.ThrowDialog;
import com.quietlycoding.android.picker.NumberPickerDialog;

/**
 * connection ready display, ping server.  
 * robust communication model.  
 * 
 * new timer, crashes on timer end.  Also, two vars in saved pref holding endtime (endTime and TIMER).  use one.  
 * erase unnecessary instance variables such as remainSeconds.  
 * 
 * by saving all info, you can resume properly.  on create, use string starttime to find out how much time is left, calculate it
 * and then use it to initiate timer.  service should still be running, to resume app and auto-unpark and stuff.  have service conduct autorefill,
 * not the timer.  do so by editing a saved string for time, a service can edit it.  
 * 
 * edittext with tags, layout similar to citrix meeting app.  you can edit the layout of the actual edittext widget.	
 * 
 * save all info to be displayed, such as email etc, and load to prevent app from looking bad if resume doesn't work.  true "re-load"
 * refill should edit usertable and history table, and then edit app.  unparking then parking is lazy programming.  
 * edit add_history.php to check if users are currently parked, if true then update else insert.
 * sqlite db for parking history
 * internet detection
 * different user  
 * pop-up tutorial
 * write find car w/ pop-up for location, find parking (nearby? in general? how to do?)
 * create managerial app
 * time zone? how is it delt with on phone?  ON PARK, SAVE PRIVATE STARTTIME DATE OBJ.
 * 
 * compatibility!!!! works on emulators but not on phone.
 * app doesn't work on 2.3, the app always quits and services dont stop.
 * OR it isn't quitting, but the resume is just making a new instance of same app each time.  possible?
 * 
 * expense handling, create database of company charge tokens, for specific user email.  
 *    if the user's expense handlingn is flagged, check db for how much company has paid.  
 *    
 * refill complete dialog cancels after a while
 * settings should have option for warning time, save info on back, the time is read and passed when starting service
 * 
 * on park, grab lat/lon and compare with what we get from server for spot, combine and store for later.
 * park only if detect internet  on the parq button, check if internet is active.  
 * 
 * DO NOT RELY ON GOOD CONNECTION.  model should be - ping server, respond okay, make change on app, confirm change on server.
 * also must consider broken connections, so app must re-send refill requests that did not go through
 * 
 * perfect information display (sizing, borders, etc)
 * INSIDE NUMBER PICKER DISPLAY should show hrs:minutes ???
 * 
 * "Share Parq" option, pulls up qr code to scan.  
 * 
 * flash light when dark
 * 
 * BOOT LOAD SERVICE RESUME
 * 
 * TimeLeftDisplay = analog timer, digital countdown (setting gives choice) 
 * Add server calls for rates.
 * 
 * INCORPORATING NUMBERPICKER:  import class, start activity for view, setContentView of dialog
 * 
 * SEcurity in authenticating with server? encrypt, salt, and ssl all communication.
 * 
 * look into city's expenses, number of parks, gauge the server costs, lay out finance to potential
 *    partners
 * */

public class MainActivity extends ActivityGroup implements LocationListener {

	/*Textual Display objects declared here*/
	private TextView rate;
	private EditText hours;
	private EditText minutes;
	private TextView remain_hours;
	private TextView remain_mins;
	private TextView colon;
	private TextView price;
	private TextView increment;
	private TextView lotDesc;
	private TextView spot;
	private TextView timeHeader;
	private TextView priceHeader;

	/*Buttons declared here*/
	private EditText spotNum;
	private Button submitButton;
	private Button scanButton;
	private Button leftButton;
	private Button rightButton;
	private Button plusButton;
	private Button minusButton;

	/*ints used by calculations*/
	private int totalTimeParked = 0; //in minutes

	/*various Objects used declared here*/
	private RateObject rateObj;
	private RateResponse rateResponse;
	private CountDownTimer timer;
	public ViewFlipper vf;
	private AlertDialog alert;
	/*final variables*/
	public static final String SAVED_INFO = "ParqMeInfo";
    private static final int WARN_TIME = 30; //in seconds
    private static final float LOCATION_ACCURACY = 20f;

	private LocationManager locationManager;
	private Location lastLocation;
	private boolean goodLocation = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flipper);

		//Create refill dialog which has special components
		alert = makeRefillDialog();

		//hook elements
		rate = (TextView) findViewById(R.id.rate);
		colon = (TextView) findViewById(R.id.colon);

		remain_hours = (TextView) findViewById(R.id.hours_remaining);
		remain_hours.setInputType(InputType.TYPE_NULL);
		remain_mins  = (TextView) findViewById(R.id.mins_remaining);
		remain_mins.setInputType(InputType.TYPE_NULL);
		
		price = (TextView) findViewById(R.id.total_price);
		increment = (TextView) findViewById(R.id.increment);
		lotDesc = (TextView) findViewById(R.id.lot_description);
		spot = (TextView) findViewById(R.id.spot);
		timeHeader = (TextView) findViewById(R.id.time_header);
		priceHeader = (TextView) findViewById(R.id.price_header);
		vf = (ViewFlipper) findViewById(R.id.flipper);

		final SharedPreferences check = getSharedPreferences(SAVED_INFO,0);

		/*	ON CREATE
		 * 	grab string from TIMER
		 * 	parse it to a date
		 * 	compare with current time
		 * 	
		 *	if time left, 
		 *		showNext() x2, initiate timer
		 *	if else
		 *		set TIMER = "none"
		 * 
		 * */

		final long endTime = SavedInfo.getEndTime(this, check);
		if (endTime != 0) {
		    final long now = System.currentTimeMillis();
			int seconds = (int)(endTime - now)/1000;
			if(seconds>0){
				vf.showNext();
				switchToParkedLayout();
				timer = initiateTimer(endTime, vf);
				timer.start();
			}
		}

		//if parkstate returned from login was true, then go to the refill view.  
		if(check.getBoolean("parkState", false)){
			vf.showNext();
			switchToParkedLayout();
		}

		final OnFocusChangeListener timeListener = new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				updateDisplay(getParkMins());
			}
		};
		
		hours = (EditText) findViewById(R.id.hours);
		hours.setInputType(InputType.TYPE_NULL);
		hours.setOnFocusChangeListener(timeListener);

		minutes = (EditText) findViewById(R.id.mins);
		minutes.setInputType(InputType.TYPE_NULL);
		minutes.setOnFocusChangeListener(timeListener);
		
		//initialize buttons and set actions
		submitButton = (Button) findViewById(R.id.submitButton);
		submitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String contents = spotNum.getText().toString();
				// contents contains string "parqme.com/p/c36/p123456" or w/e...
				SharedPreferences check = getSharedPreferences(SAVED_INFO,0);

				final double lat;
				final double lon;
				if (!goodLocation) {
					// TODO: Show a loading dialog and don't do the rest of this stuff yet
					lat = 0f;
					lon = 0f;
				} else {
					lat = lastLocation.getLatitude();
					lon = lastLocation.getLongitude();
				}
				rateResponse = ServerCalls.getRateGps(contents, lat, lon, check);
				if(rateResponse!=null){
					rateObj = rateResponse.getRateObject();

					// if we get the object successfully
					if (rateResponse.getResp().equals("OK")) {
						vf.showNext();
						// prepare time picker for this spot
						//					parkTimePicker.setRange(mySpot.getMinIncrement(),
						//							mySpot.getMaxTime());
						//					parkTimePicker.setMinInc(mySpot.getMinIncrement());

						// initialize all variables to match spot
						final int minimalIncrement = rateObj.getMinIncrement();

						rate.setText(formatCents(rateObj.getDefaultRate()) + " per " + minimalIncrement + " minutes");
						lotDesc.setText(rateObj.getDescription());
						spot.setText("Spot #" + contents);
						if (rateObj.getMinIncrement() != 0) {
							increment.setText(rateObj.getMinIncrement() + " minute increments");
						}
						// store some used info
						SharedPreferences.Editor editor = check.edit();
						editor.putString("code", contents);
						editor.putFloat("lat", (float) rateObj.getLat());
						editor.putFloat("lon", (float) rateObj.getLon());
						editor.commit();
						updateDisplay(minimalIncrement);
					} else {
						ThrowDialog.show(MainActivity.this,
								ThrowDialog.RESULT_ERROR);
					}
				}	else {
					ThrowDialog.show(MainActivity.this,
							ThrowDialog.RESULT_ERROR);
				}
			}
		});
		spotNum = (EditText) findViewById(R.id.spot_num);
		spotNum.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				submitButton.setEnabled(s.length() > 0);
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
		scanButton = (Button) findViewById(R.id.scanButton);
		scanButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!SavedInfo.isLoggedIn(MainActivity.this)){
					//if not logged in, alert user via dialog
					ThrowDialog.show(MainActivity.this, ThrowDialog.MUST_LOGIN);
				}else{

					//else start scan intent
					IntentIntegrator.initiateScan(MainActivity.this);
				}
			}
		});

		leftButton = (Button) findViewById(R.id.left_button);
		rightButton = (Button) findViewById(R.id.right_button);

		plusButton = (Button) findViewById(R.id.plus);
		plusButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				plusTime();
			}
		});
		minusButton = (Button) findViewById(R.id.minus);
		minusButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				minusTime();
			}
		});

		switchToParkingLayout();

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		startGettingLocation();
	}

	private int getParkMins() {
		int h, m;
		try {
			h = Integer.valueOf(hours.getText().toString());
		} catch (NumberFormatException e) {
			h = 0;
		}
		try {
			m = Integer.valueOf(minutes.getText().toString());
		} catch (NumberFormatException e) {
			m = 0;
		}
		int time = h*60+m;
		time -= time % rateObj.getMinIncrement();
		return time;
	}

	private void plusTime() {
	    final int maxTime = rateObj.getMaxTime();
	    final int minIncrement = rateObj.getMinIncrement();
		if (hours.hasFocus()) {
			if (maxTime > 0) {
			    updateDisplay(Math.min(maxTime, getParkMins()+60));
			} else {
			    updateDisplay(getParkMins()+60);
			}
		} else {
			if (maxTime > 0) {
			    updateDisplay(Math.min(maxTime, getParkMins()+minIncrement));
			} else {
			    updateDisplay(getParkMins()+minIncrement);
			}
		}
	}

	private void minusTime() {
	    final int minIncrement = rateObj.getMinIncrement();
		if (hours.hasFocus()) {
			//update the parking minutes and seconds
			updateDisplay(Math.max(minIncrement, getParkMins()-60));
		} else {
			updateDisplay(Math.max(minIncrement, getParkMins()-minIncrement));
		}
	}

	private void switchToParkingLayout() {
	    remain_hours.setVisibility(View.GONE);
	    remain_mins.setVisibility(View.GONE);
	    hours.setVisibility(View.VISIBLE);
	    minutes.setVisibility(View.VISIBLE);
	    minusButton.setVisibility(View.VISIBLE);
	    plusButton.setVisibility(View.VISIBLE);
	    priceHeader.setVisibility(View.VISIBLE);
	    price.setVisibility(View.VISIBLE);
	    timeHeader.setText("Parking Meter");
	    priceHeader.setText("Total");
	    leftButton.setText("Cancel");
	    leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                rateResponse = null;
                totalTimeParked = 0;
                //return to previous view
                vf.showPrevious();
            }
        });
	    rightButton.setText("PARQ now");
	    rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                park();
            }
        });
	}

	private void switchToParkedLayout() {
	    hours.setVisibility(View.GONE);
	    minutes.setVisibility(View.GONE);
	    minusButton.setVisibility(View.GONE);
	    plusButton.setVisibility(View.GONE);
	    priceHeader.setVisibility(View.INVISIBLE);
	    price.setVisibility(View.INVISIBLE);
	    remain_hours.setVisibility(View.VISIBLE);
	    remain_mins.setVisibility(View.VISIBLE);
		timeHeader.setText("Time Remaining");
		leftButton.setText("Unpark");
        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                unpark();
            }
        });
        rightButton.setText("Refill");
        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final int maxTime = rateObj.getMaxTime();
                if(maxTime <= 0 || totalTimeParked<maxTime){
                    NumberPickerDialog y = new NumberPickerDialog(MainActivity.this, 0,0);
                    y.setRange(rateObj.getMinIncrement(), rateObj.getMaxTime()-totalTimeParked);
                    y.setMinInc(rateObj.getMinIncrement());
                    y.setOnNumberSetListener(mRefillListener);
                    y.show();
                }else{
                    ThrowDialog.show(MainActivity.this, ThrowDialog.MAX_TIME);
                }
            }
        });
	}

	private static boolean isLocationProviderAvailable(LocationManager locationManager, String provider) {
		return locationManager.getProvider(provider) != null && locationManager.isProviderEnabled(provider);
	}

	private void startGettingLocation() {
		if (isLocationProviderAvailable(locationManager, LocationManager.GPS_PROVIDER)) {
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		} else if (isLocationProviderAvailable(locationManager, LocationManager.NETWORK_PROVIDER)) {
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
		} else {
			ThrowDialog.show(this, ThrowDialog.NO_LOCATION);
		}
	}

	private void stopGettingLocation() {
		locationManager.removeUpdates(this);
	}

	private AlertDialog makeRefillDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setMessage("You are almost out of time!")
		.setCancelable(false)
		.setPositiveButton("Refill", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
			    final int maxTime = rateObj.getMaxTime();
				if(totalTimeParked<maxTime)
					refillMe(1);
				//refillMe(minimalIncrement);
				else
					ThrowDialog.show(MainActivity.this, ThrowDialog.MAX_TIME);
				//mediaPlayer.stop();
			}
		})
		.setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				//mediaPlayer.stop();
			}
		});
		return builder.create();
	}

    private void unpark() {
        new AlertDialog.Builder(MainActivity.this).setMessage("Are you sure you want to unpark?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        final SharedPreferences check = getSharedPreferences(SAVED_INFO, 0);
                        String parkId = SavedInfo.getParkId(MainActivity.this, check);
                        if (ServerCalls.unPark(111, parkId, check)) {
                            timer.cancel();
                            // stopService(new Intent(MainActivity.this,
                            // Background.class));

                            // reset ints
                            totalTimeParked = 0;

                            SharedPreferences.Editor editor = check.edit();
                            SavedInfo.unpark(MainActivity.this, editor);
                            editor.commit();

                            switchToParkingLayout();
                        } else {
                            ThrowDialog.show(MainActivity.this, ThrowDialog.UNPARK_ERROR);
                        }
                    }
                }).setNegativeButton("No", null).create().show();
    }

    private void park() {
        final SharedPreferences prefs = getSharedPreferences(SAVED_INFO, 0);
        final int parkingTime = getParkMins();
        ParkInstanceObject parkInstance = ServerCalls.park(parkingTime, rateObj, prefs);
        if(parkInstance != null){
            if (parkInstance.getEndTime() > 0) {
                SavedInfo.park(MainActivity.this, parkInstance);
                totalTimeParked += parkingTime;
                if (totalTimeParked == rateObj.getMaxTime()) {
                    ThrowDialog.show(MainActivity.this, ThrowDialog.MAX_TIME);
                }
                switchToParkedLayout();
                //create and start countdown display
                timer = initiateTimer(parkInstance.getEndTime(), vf);
                timer.start();
                //start timer background service
                //startService(new Intent(MainActivity.this, Background.class).putExtra("time", remainSeconds));
            } else {
                ThrowDialog.show(MainActivity.this, ThrowDialog.IS_PARKED);
            }
        }else{
            ThrowDialog.show(MainActivity.this, ThrowDialog.RESULT_ERROR);
        }
    }

	private void refillMe(int refillMinutes){
	    final int maxTime = rateObj.getMaxTime();
		//if we haven't gone past the total time we're allowed to park
		if(totalTimeParked+refillMinutes<=maxTime){

			//update the total time parked and remaining time.
			totalTimeParked+=refillMinutes;
			updateDisplay(getParkMins()+refillMinutes);

			//stop current timer, start new timer with current time + selectedNumber.
			try{

				SharedPreferences check = getSharedPreferences(SAVED_INFO,0);
				final long oldEnd = SavedInfo.getEndTime(this, check);
				final long newEnd = oldEnd + refillMinutes*60*1000;
				//calculate new endtime and initiate timer from it.  
				timer.cancel();
				timer = initiateTimer(newEnd, vf);
				//stopService(new Intent(MainActivity.this, Background.class));
				timer.start();
				//startService(new Intent(MainActivity.this, Background.class).putExtra("time", remainSeconds));
				if(totalTimeParked==maxTime){
					ThrowDialog.show(MainActivity.this, ThrowDialog.MAX_TIME);
				}else{
					ThrowDialog.show(MainActivity.this, ThrowDialog.REFILL_DONE);
				}
			}catch(Exception e){
				ThrowDialog.show(MainActivity.this, ThrowDialog.UNPARK_ERROR);
			}
		}
	}

	private NumberPickerDialog.OnNumberSetListener mRefillListener =
			new NumberPickerDialog.OnNumberSetListener() {
		@Override
		public void onNumberSet(int selectedNumber) {
			if(selectedNumber>=rateObj.getMinIncrement())
				//TODO testing delete me
				refillMe(1);
			//refillMe(selectedNumber);
		}
	};


	//once we scan the qr code
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		final IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if (scanResult != null) {
			//call server using the qr code, to get a resulting spot's info.
			String contents = scanResult.getContents();
			if (contents != null) {
				SharedPreferences check = getSharedPreferences(SAVED_INFO,0);
				//contents contains string "parqme.com/p/c36/p123456" or w/e...
				rateResponse = ServerCalls.getRateQr(contents, check);
				if(rateResponse!=null){
					rateObj = rateResponse.getRateObject();
					
					//if we get the object successfully
					if(rateResponse.getResp().equals("OK")){
						vf.showNext();
						// prepare time picker for this spot
						//					parkTimePicker.setRange(mySpot.getMinIncrement(),
						//							mySpot.getMaxTime());
						//					parkTimePicker.setMinInc(mySpot.getMinIncrement());

						final int minIncrement = rateObj.getMinIncrement();
						String [] test = contents.split("/");
						rate.setText(formatCents(rateObj.getDefaultRate()) + " per " + minIncrement + " minutes");
						lotDesc.setText(rateObj.getDescription());
						spot.setText("Spot #" + test[2]);
						if (rateObj.getMinIncrement() != 0) {
							increment.setText(rateObj.getMinIncrement() + " minute increments");
						}
						// store some used info
						SharedPreferences.Editor editor = check.edit();
						editor.putString("code", contents);
						editor.putFloat("lat", (float) rateObj.getLat());
						editor.putFloat("lon", (float) rateObj.getLon());
						editor.commit();
						updateDisplay(minIncrement);
					}else{
						ThrowDialog.show(MainActivity.this, ThrowDialog.RESULT_ERROR);
					}
				}else{
					ThrowDialog.show(MainActivity.this, ThrowDialog.RESULT_ERROR);
				}
			} else {
				//do nothing if the user doesn't scan and just cancels.
				//call server using the qr code, to get a resulting spot's info.
				contents = "parqme.com/main_lot/1412";
				SharedPreferences check = getSharedPreferences(SAVED_INFO,0);
				//contents contains string "parqme.com/p/c36/p123456" or w/e...
				rateResponse = ServerCalls.getRateQr(contents, check);
				if(rateResponse!=null){
					rateObj = rateResponse.getRateObject();
					
					//if we get the object successfully
					if(rateResponse.getResp().equals("OK")){
						vf.showNext();
						// prepare time picker for this spot
						//					parkTimePicker.setRange(mySpot.getMinIncrement(),
						//							mySpot.getMaxTime());
						//					parkTimePicker.setMinInc(mySpot.getMinIncrement());

						final int minIncrement = rateObj.getMinIncrement();
						String [] test = contents.split("/");
						rate.setText(formatCents(rateObj.getDefaultRate()) + " per " + minIncrement + " minutes");
						lotDesc.setText(rateObj.getDescription());
						spot.setText("Spot #" + test[2]);
						if (minIncrement != 0) {
							increment.setText(minIncrement + " minute increments");
						}
						// store some used info
						SharedPreferences.Editor editor = check.edit();
						editor.putString("code", contents);
						editor.putFloat("lat", (float) rateObj.getLat());
						editor.putFloat("lon", (float) rateObj.getLon());
						editor.commit();
						updateDisplay(minIncrement);
					}else{
						ThrowDialog.show(MainActivity.this, ThrowDialog.RESULT_ERROR);
					}
				}else{
					ThrowDialog.show(MainActivity.this, ThrowDialog.RESULT_ERROR);
				}
			}
		}
	}

	/* THIS TIMER is only used for in-app visuals.  The actual server updating and such are
	 * done via user button clicks, and the background service we run.  */
	private CountDownTimer initiateTimer(long endTime, final ViewFlipper myvf){
		//creates the countdown timer
		long now = System.currentTimeMillis();
		return new CountDownTimer(endTime - now, 1000){
			//on each 1 second tick, 
			@Override
			public void onTick(long millisUntilFinished) {
				int seconds = (int)millisUntilFinished/1000;
				//if the time is what our warning-time is set to
				if(seconds==WARN_TIME && !SavedInfo.autoRefill(MainActivity.this)){
					//alert the user
					alert.show();
				}
				//update remain seconds and timer.
				remain_hours.setText(String.valueOf(seconds / 360));
				remain_mins.setText(String.valueOf((seconds % 360 + 59) / 60));

				flashColon();
			}
			//on last tick,
			//TODO calling alert.cancel() or throwdialog when app isn't in forefront may cause crash?
			@Override
			public void onFinish() {
				//timeDisplay.setText("0:00:00");
				SharedPreferences check = getSharedPreferences(SAVED_INFO,0);
				//if autorefill is on, refill the user minimalIncrement
				if(check.getBoolean("autoRefill", false)){
					alert.cancel();
					refillMe(1);
				}else{
					SavedInfo.eraseTimer(MainActivity.this);
					//else we cancel the running out of tie dialog
					alert.cancel();
					//and restore view
					switchToParkingLayout();
					ThrowDialog.show(MainActivity.this, ThrowDialog.TIME_OUT);
				}
			}


		};

	}

	//converts cents to "$ x.xx"
	private static String formatCents(int m) {
		final String dollars = String.valueOf(m / 100);
		String cents = String.valueOf(m % 100);
		if (m % 100 < 10) {
			cents = '0' + cents;
		}
		return '$'+dollars+'.'+cents;
	}
	//TODO:  THIN METHOD -- will become robust later.  must support varying units and different parking minutes and all day parking etc.
	private static int getCostInCents(int mins, RateObject rate) {
		//number of minutes parked, divided by the minimum increment (aka unit of time), multiplied by price per unit in cents
		return (mins/(rate.getMinIncrement())) * rate.getDefaultRate();
	}
	private void updateDisplay(int time) {
		hours.setText(String.valueOf(time/60));
		minutes.setText(String.valueOf(time%60));
		price.setText(formatCents(getCostInCents(time, rateObj)));
	}

	private void flashColon() {
	    if (colon.getVisibility() == View.VISIBLE) {
	        colon.setVisibility(View.INVISIBLE);
	    } else {
	        colon.setVisibility(View.VISIBLE);
	    }
	}

	@Override
	public void onLocationChanged(Location location) {
		if (location != null && (lastLocation == null || location.getAccuracy() < lastLocation.getAccuracy())) {
			lastLocation = location;
			if (location.getAccuracy() <= LOCATION_ACCURACY) {
				stopGettingLocation();
				goodLocation = true;
			}
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
}
