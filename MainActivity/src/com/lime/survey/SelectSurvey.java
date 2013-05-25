package com.lime.survey;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lime.survey.bean.GeneralDTO;

public class SelectSurvey extends Activity implements OnClickListener,
		OnItemSelectedListener {
	private Spinner surveybtn;
	private TextView imei_tv, latitude_tv, longitude_tv, respondent_id;
	private ArrayAdapter surverAdapter;
	private ArrayList<GeneralDTO> surveyList;
	private String imei = null;
	private Button submit, getCoordinates;
	private Handler uiHandler;
	boolean fromSettingActivity;
	private String resIdValue;
	private String IdValue;
	private String urlValue;
	private String nameValue;
	private DBAdapter dba;
	private String lat;
	private String logitd;
	private String respondentId;
	private String latVal;
	private String logitdVal;
	private String respondentIdVal;
	private String imeiVal;
	private ArrayList<Object> tempList;
	private ArrayList<Object> udatedList;
	private String selectedItem;
	private ArrayList<String> selectedItmList;
	private String selectSurItem;

	private static final String TAG = "SelectSurvey";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_survey);
		fromSettingActivity = getIntent().getBooleanExtra(
				"fromSettingActivity", false);
		dba = new DBAdapter(getApplicationContext());
		generateService();
		getAllViews();
		updateList();
		setAllVeriables();
		setSpinners();
		settingListiner();

	}

	private void getLocationFix() {
		try {

			final MyLocation loc = new MyLocation(getApplicationContext());
			final ProgressDialog dialog;
			if (loc.isProviderEnabled()) {

				final String message = "May take some time to get location fix.\nPlease be patient...";

				dialog = new ProgressDialog(SelectSurvey.this);
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
								// location_tv.setText("Latitude = "
								// + Double.toString(latitude)
								// + ", Longitude  = "
								// + Double.toString(longitude));

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

	private void updateList() {

		tempList = dba.retriveSurveyName();
		udatedList = new ArrayList<Object>();
		udatedList.add(0, "Select Survey");
		for (Object temp : tempList) {
			udatedList.add(temp);
		}
		tempList.clear();

	}

	private void setAllVeriables() {
		try {
			TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			imei = telephonyManager.getDeviceId();
		} catch (Exception e) {

			Log.e("SelectSurvey" + ">>setAllVeriables()", e.toString());
		}
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		imei = telephonyManager.getDeviceId();
		imei_tv.setText(imei);

		surverAdapter = new ArrayAdapter(getApplicationContext(),
				R.layout.spinner, udatedList);
		surverAdapter.setDropDownViewResource(R.layout.spinner_dropdown);

	}

	private void setSpinners() {
		surveybtn.setAdapter(surverAdapter);

	}

	private void settingListiner() {

		submit.setOnClickListener(this);
		getCoordinates.setOnClickListener(this);
		surveybtn.setOnItemSelectedListener(this);
	}

	private void getAllViews() {
		// Spinner
		surveybtn = (Spinner) findViewById(R.id.ss_surveybtn);
		// TextView
		latitude_tv = (TextView) findViewById(R.id.ss_latitude);
		longitude_tv = (TextView) findViewById(R.id.ss_longitude);
		respondent_id = (TextView) findViewById(R.id.ss_respondent_id);
		imei_tv = (TextView) findViewById(R.id.ss_imei);
		// Button
		submit = (Button) findViewById(R.id.ss_submit);
		getCoordinates = (Button) findViewById(R.id.ss_getCoordinates);

	}

	private void generateService() {
		GeneralDTO gdto;
		surveyList = new ArrayList<GeneralDTO>();
		gdto = new GeneralDTO();
		gdto.setCode("001");
		gdto.setName("Survey 1");
		surveyList.add(gdto);

		gdto = new GeneralDTO();
		gdto.setCode("002");
		gdto.setName("Survey 2");
		surveyList.add(gdto);

		gdto = new GeneralDTO();
		gdto.setCode("003");
		gdto.setName("Survey 3");
		surveyList.add(gdto);

		gdto = new GeneralDTO();
		gdto.setCode("004");
		gdto.setName("Survey 4");
		surveyList.add(gdto);

	}

	/**
	 * Function to show settings alert dialog
	 * */
	public void showSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				SelectSurvey.this);

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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ss_submit:
			getTextOfAllViews();
			if (fromSettingActivity) {
				nameValue = getIntent().getStringExtra("nameValue");
				IdValue = getIntent().getStringExtra("IdValue");
				urlValue = getIntent().getStringExtra("urlValue");
				resIdValue = getIntent().getStringExtra("resIdValue");
				// dba.insertInfo(IdValue, nameValue, urlValue, latVal,
				// logitdVal,
				// imeiVal, resIdValue);
			}
		String currentItem = surveybtn.getSelectedItem().toString().trim();
			if (currentItem.equals("Select Survey")) {
				CustomAlert.cstmAlert(SelectSurvey.this, "Select Survey To Sumbit");

			}else{
				Intent survey_in = new Intent(getApplicationContext(),
						RespondActivity.class);
				selectSurItem = surveybtn.getSelectedItem().toString().trim();
				survey_in.putExtra("selectSurItem", selectSurItem);
				startActivity(survey_in);
			}

			break;
		case R.id.ss_getCoordinates:

			break;

		}
	}

	private void getTextOfAllViews() {
		latVal = latitude_tv.getText().toString().trim();
		logitdVal = longitude_tv.getText().toString().trim();
		respondentIdVal = respondent_id.getText().toString().trim();
		imeiVal = imei_tv.getText().toString().trim();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int pos, long item) {

		selectedItem = parent.getSelectedItem().toString().trim();
		if (selectedItem.equals("Select Survey")) {

		} else {
			selectedItmList = dba.retriveSelectedRecord(selectedItem);
			if (selectedItmList != null && selectedItmList.isEmpty() == false) {
				respondent_id.setText(selectedItmList.get(6));
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}
