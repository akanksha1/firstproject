package com.lime.survey;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	Button select_survey;
	Button settingbtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getAllViews();
		setListiner();

	}

	private void setListiner() {

		select_survey.setOnClickListener(this);
		settingbtn.setOnClickListener(this);
	}

	private void getAllViews() {
		select_survey = (Button) findViewById(R.id.select_survey);
		settingbtn = (Button) findViewById(R.id.settingbtn);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.select_survey:
			Intent selct_in = new Intent(getApplicationContext(),
					SelectSurvey.class);
			startActivity(selct_in);
			break;
		case R.id.settingbtn:
			Intent setting_in = new Intent(getApplicationContext(),
					SettingActivity.class);
			startActivity(setting_in);
			break;
		default:
			break;
		}
	}

}
