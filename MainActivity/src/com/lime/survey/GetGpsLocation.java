package com.lime.survey;

import java.util.Timer;
import java.util.TimerTask;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GetGpsLocation extends Activity {

	private static final String TAG = "MainActivity";
	private Handler uiHandler;
	private TextView location_tv;
	private Button getLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gps);
		// Create a uiHandler, to manage UI Thread
		location_tv = (TextView) findViewById(R.id.textView);
		getLocation = (Button) findViewById(R.id.getLocation);
		uiHandler = new Handler();
		getLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getLocationFix();

			}
		});

	}

	/**
	 * Function to show settings alert dialog
	 * */
	public void showSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				GetGpsLocation.this);

		// Setting Dialog Title
		alertDialog.setTitle("GPS is settings");

		// Setting Dialog Message
		alertDialog
				.setMessage("GPS is not enabled. Do you want to go to settings menu?");

		// Setting Icon to Dialog
		// alertDialog.setIcon(R.drawable.delete);

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivity(intent);
					}
				});

		// on pressing cancel button
		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}

	private void getLocationFix() {
		try {

			final MyLocation loc = new MyLocation(getApplicationContext());
			final ProgressDialog dialog;
			if (loc.isProviderEnabled()) {

				final String message = "May take some time to get location fix.\nPlease be patient...";

				dialog = new ProgressDialog(GetGpsLocation.this);
				dialog.setIcon(android.R.drawable.ic_dialog_info);
				dialog.setTitle("Getting a location fix");
				dialog.setMessage(message);
				dialog.setProgressStyle(1);
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();

				final Timer timer = new Timer(true);

				final TimerTask task = new TimerTask() {
					@Override
					public void run() {
						uiHandler.post(new Runnable() {

							public void run() {
								dialog.setMessage(message
										+ "\nCurrent Accuracy : "
										+ loc.getCurrentAccuracy());
								dialog.setOnCancelListener(new OnCancelListener() {

									public void onCancel(DialogInterface dialog) {
										loc.stopListening();
										timer.cancel();
										CustomToast
												.makeCenterToast(
														getApplicationContext(),
														"Process cancelled, Not saving ...",
														Toast.LENGTH_SHORT)
												.show();
									}
								});
								if ((int) loc.getCurrentAccuracy() <= 1000) {
									dialog.setProgress(20);
									if ((int) loc.getCurrentAccuracy() <= 500) {
										dialog.setProgress(30);
										if ((int) loc.getCurrentAccuracy() <= 100) {
											dialog.setProgress(40);
											if ((int) loc.getCurrentAccuracy() <= 50) {
												dialog.setProgress(50);
												if ((int) loc
														.getCurrentAccuracy() <= 40) {
													dialog.setProgress(70);
													if ((int) loc
															.getCurrentAccuracy() <= 30) {
														dialog.setProgress(100);
														loc.stopListening();
														dialog.dismiss();
														timer.cancel();
														Location currentLoc = loc
																.getCurrentLocation();
														setLocation(currentLoc);
													}
												}
											}
										}
									}
								}

							}

							private void setLocation(Location currentLoc) {
								double latitude = currentLoc.getLatitude();
								double longitude = currentLoc.getLongitude();
								location_tv.setText("Latitude = "
										+ Double.toString(latitude)
										+ ", Longitude  = "
										+ Double.toString(longitude));

							}
						});
					}
				};
				timer.schedule(task, 0, 5000);

			} else {
				uiHandler.post(new Runnable() {

					public void run() {

						showSettingsAlert();

					}
				});
			}
		} catch (Exception e) {
			Log.i(TAG + ">>  getLocationFix()", e.toString());
		}
	}
}
