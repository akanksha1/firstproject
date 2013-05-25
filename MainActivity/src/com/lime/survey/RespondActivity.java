package com.lime.survey;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class RespondActivity extends Activity {

	
	private String selectSurItem;
	private ArrayList<String> selectedSurvyList;
	private TextView urlview;
	private String finalUrl;
	private TextView re_id;
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.respond);
		getAllViews();
		DBAdapter dbAdapter=new DBAdapter(getApplicationContext());
		selectSurItem=	getIntent().getStringExtra("selectSurItem");
		
		
		if(!selectSurItem.equals("Select Survey")){
		selectedSurvyList=	dbAdapter.retriveSelectedRecord(selectSurItem);
		
		if (selectedSurvyList != null && selectedSurvyList.isEmpty() == false) {
		finalUrl=	generateURL(selectedSurvyList.get(1),selectedSurvyList.get(3),"lat","long",selectedSurvyList.get(6));

		}
	setTextOnViews();
		}
		
	}
	
	private void setTextOnViews() {
		re_id.setText(selectedSurvyList.get(6));
urlview.setText(finalUrl);		
	}

	private void getAllViews() {
		urlview=(TextView)findViewById(R.id.urlview);		
		re_id=(TextView)findViewById(R.id.re_id);
	}

	private String generateURL(String surveyID, String imei, String latitude,
			String longitude, String respondent_id) {

		
		http://connect-survey.com/audiencenet/index.php? IMEI={value}& GPS ={value}&SID={value}&SName={value}&RID={value}
			 

		  url = "http://connect-survey.com/survey_v2/index.php?"
				
				+ "IMEI="
				+ imei
				+ "&GPS="
				+ latitude
				+ "&"
				+ longitude
				+"&SID="
				+ surveyID
				+"&SName="
				+selectSurItem
				+ "&RID="
				+ respondent_id;
				
		return url;

	}

}
