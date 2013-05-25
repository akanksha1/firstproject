package com.lime.survey;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.lime.survey.bean.GeneralDTO;

public class SettingActivity extends Activity implements OnClickListener,
		OnItemSelectedListener {
	private static final String TAG = "SettingActivity";
	Spinner selct_srvey;
	TextView imei_tv;
	EditText survey_ID, name, local_url, respondent_id, global_url;
	Button save, add;
	private String imei = null;
	ArrayList<GeneralDTO> surveyList;
	private ArrayAdapter surverAdapter;
	private String nameValue;
	private String IdValue;
	private String urlValue;
	private String resIdValue;
	private String imeiValue;
	DBAdapter dba;
	String selectItem;
	private String global_urlVal;
	private ArrayList<Object> udatedList;
	private ArrayList<Object> tempList;
	private ArrayList<String> selectedItmList;
	private String selectIndx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		dba = new DBAdapter(getApplicationContext());
		getAllViews();
		generateService();
		setAllVeriables();
		updateList();
		settingListner();
		setSpinners();
	}

	private void generateService() {
		// GeneralDTO gdto;
		udatedList = new ArrayList<Object>();
		udatedList.add("Select Survey");
		
		// surveyList = new ArrayList<GeneralDTO>();
		// gdto = new GeneralDTO();
		// gdto.setCode("001");
		// gdto.setName("Select Survey");
		// surveyList.add(gdto);

		// gdto = new GeneralDTO();
		// gdto.setCode("002");
		// gdto.setName("Survey 2");
		// surveyList.add(gdto);
		//
		// gdto = new GeneralDTO();
		// gdto.setCode("003");
		// gdto.setName("Survey 3");
		// surveyList.add(gdto);
		//
		// gdto = new GeneralDTO();
		// gdto.setCode("004");
		// gdto.setName("Survey 4");
		// surveyList.add(gdto);

	}

	private void setAllVeriables() {
		try {
			TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			imei = telephonyManager.getDeviceId();
		} catch (Exception e) {
			Log.e(TAG + ">>setAllVeriables()", e.toString());
		}
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		imei = telephonyManager.getDeviceId();
		imei_tv.setText(imei);

		surverAdapter = new ArrayAdapter(getApplicationContext(),
				R.layout.spinner, udatedList);
		surverAdapter.setDropDownViewResource(R.layout.spinner_dropdown);

	}

	private void setSpinners() {
		selct_srvey.setAdapter(surverAdapter);

	}

	private void settingListner() {
		save.setOnClickListener(this);
		add.setOnClickListener(this);
		selct_srvey.setOnItemSelectedListener(this);
	}

	private void getAllViews() {
		// Spinner
		selct_srvey = (Spinner) findViewById(R.id.st_selct_srvey);
		// TextView

		imei_tv = (TextView) findViewById(R.id.st_imei);
		// EditText
		global_url = (EditText) findViewById(R.id.st_global_url);
		survey_ID = (EditText) findViewById(R.id.st_survey_ID);
		name = (EditText) findViewById(R.id.st_name);
		local_url = (EditText) findViewById(R.id.st_local_url);
		respondent_id = (EditText) findViewById(R.id.st_respondent_id);
		// Button
		save = (Button) findViewById(R.id.st_save);
		add = (Button) findViewById(R.id.st_add);
	}

	private void clearData() {
		global_url.setText("");
		name.setText("");
		survey_ID.setText("");
		local_url.setText("");
		respondent_id.setText("");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.st_add:
			getTextAllViews();
			if (!imeiValue.equals("") && !nameValue.equals("")
					&& !IdValue.equals("") && !urlValue.equals("")
					&& !resIdValue.equals("") && !global_urlVal.equals("")) {

				dba.insertInfo(IdValue, global_urlVal, imeiValue, nameValue,
						urlValue, resIdValue);
				CustomToast.makeCenterToast(SettingActivity.this,
						"values are inserted sucessfully", 5000);
				updateList();
				clearData();
				// Intent survey_in = new Intent(getApplicationContext(),
				// SelectSurvey.class);
				// survey_in.putExtra(nameValue, nameValue);
				// survey_in.putExtra(IdValue, IdValue);
				// survey_in.putExtra(urlValue, urlValue);
				// survey_in.putExtra(resIdValue, resIdValue);
				// survey_in.putExtra("fromSettingActivity", true);
				// startActivity(survey_in);

			} else {
				CustomAlert.cstmAlert(SettingActivity.this,
						"Please Fill All Fields");
			}
			break;

		case R.id.st_save:
			getTextAllViews();
			selectItem=selct_srvey.getSelectedItem().toString().trim();
			if (selectItem.equals("Select Survey")) {
				CustomAlert.cstmAlert(SettingActivity.this, "Select Survey Name From List To Edit it");
				clearData();
			} else {
				dba.updateSelectedInfo(selectIndx, global_urlVal, nameValue,
						IdValue, urlValue, resIdValue);
				updateList();
			}
			
			break;
		}
	}

	private void updateList() {

		tempList = dba.retriveSurveyName();
		if (tempList != null && tempList.isEmpty() == false) {
		udatedList.clear();
		udatedList.add("Select Survey");
		for(Object temp:tempList){
			udatedList.add(temp);
		}
		surverAdapter.notifyDataSetChanged();
		selct_srvey.setAdapter(surverAdapter);
		tempList.clear();

		}
	}

	private void getTextAllViews() {
		global_urlVal = global_url.getText().toString().trim();
		imeiValue = imei_tv.getText().toString().trim();
		nameValue = name.getText().toString().trim();
		IdValue = survey_ID.getText().toString().trim();
		urlValue = local_url.getText().toString().trim();
		resIdValue = respondent_id.getText().toString().trim();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int pos, long item) {
		System.out.println("---getSelectedItem---" + parent.getSelectedItem());
		selectItem = parent.getSelectedItem().toString().trim();
		if (selectItem.equals("Select Survey")) {
			clearData();
		} else {
			selectedItmList = dba.retriveSelectedRecord(selectItem);
			if (selectedItmList != null && selectedItmList.isEmpty() == false) {
				selectIndx = selectedItmList.get(0);
				respondent_id.setText(selectedItmList.get(6));
				global_url.setText(selectedItmList.get(2));
				survey_ID.setText(selectedItmList.get(1));
				local_url.setText(selectedItmList.get(5));
				name.setText(selectedItmList.get(4));

			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
}
