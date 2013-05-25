package com.lime.survey;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

	SQLiteDatabase sd;
	Context context;

	private final String DATABASE_NAME = "lime_survey";
	private final int DATABASE_VERSION = 1;

	// Table realted Variables

	private final String TABLE_NAME = "survey";
	private final String SURVEY_CODE = "survey_code";
	private final String SURVEY_NAME = "survey_name";
	private final String[] SURVEY_TBL_ARRAY = { SURVEY_CODE, SURVEY_NAME };

	private final String TBL_SURVEY_INFO = "survey_info";
	private final String LATITUDE = "latitude";
	private final String LONGITUDE = "longitude";
	private final String IMEI = "imei";

	private final String SURVEY_ID = "survey_id";
	private final String SURV_NAME = "surv_name";
	private final String LOCAL_URL = "local_url";
	private final String GLOBAL_URL = "global_url";
	private final String INDEX_NO="index_no";
	private final String RESPOND_ID = "respond_id";
	private final String[] INFO_TBL_ARRAY = { SURVEY_ID, GLOBAL_URL, IMEI,
			SURV_NAME, LOCAL_URL, RESPOND_ID };


	private final String[] INFO_INDX_ARRAY = { INDEX_NO,SURVEY_ID, GLOBAL_URL, IMEI,
			SURV_NAME, LOCAL_URL, RESPOND_ID };
	private final String CREATE_TABLE = "create table " + TABLE_NAME + " ("
			+ SURVEY_CODE + " integer primary key, " + SURVEY_NAME + " text);";
	// private final String
	// CREATE_INFO_TABLE="create table "+TBL_SURVEY_INFO+" ("+SURVEY_ID+"integer primary key, "+SURV_NAME+" text, "+LOCAL_URL+" text, "
	// +LATITUDE+" text, "+LONGITUDE+" text, "+IMEI+" text, "+RESPOND_ID+
	// " text);";
	private final String CREATE_INFO_TABLE = "create table " + TBL_SURVEY_INFO
			+ " (" + INDEX_NO + " integer primary key autoincrement, "+ SURV_NAME + " text, " +SURVEY_ID+" integer, " + GLOBAL_URL
			+ " text, " + IMEI + " text, " + LOCAL_URL
			+ " text, " + RESPOND_ID + " text);";

	public DBAdapter(Context context) {
		this.context = context;
		OpenHelper myHelper = new OpenHelper(context, DATABASE_NAME, null,
				DATABASE_VERSION);
		sd = myHelper.getWritableDatabase();
	}

	public void insertRow(String s_code, String s_name) {
		ContentValues cv = new ContentValues();
		cv.put(SURVEY_CODE, s_code);
		cv.put(SURVEY_NAME, s_name);
		sd.insert(TABLE_NAME, null, cv);
	}

	public void insertInfo(String survey_id, String global_url, String imei,
			String survey_name, String local_url, String res_id) {
		ContentValues cv = new ContentValues();
		cv.put(SURVEY_ID, survey_id);
		cv.put(SURV_NAME, survey_name);
		cv.put(LOCAL_URL, local_url);
		cv.put(GLOBAL_URL, global_url);
		cv.put(IMEI, imei);
		cv.put(RESPOND_ID, res_id);
		sd.insert(TBL_SURVEY_INFO, null, cv);

//		CustomAlert.cstmAlert(context, "values are inserted sucessfully");
		// closeDB();
	}

	public void closeDB() {

		sd.close();
	}

	public ArrayList<Object> retriveSurveyName() {

		Cursor cursor;
		ArrayList<Object> rowData = new ArrayList<Object>();

		try {
			cursor = sd.query(TBL_SURVEY_INFO,INFO_TBL_ARRAY, null, null,
					null, null, null);

			cursor.moveToFirst();

			if (!cursor.isAfterLast()) {
				do {
					

					rowData.add(cursor.getString(3));
					System.out.println("-----" + rowData.get(cursor.getInt(3)));
				} while (cursor.moveToNext());

			}
			cursor.close();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(e);
		}

		return rowData;

	}

public ArrayList<String> retriveSelectedRecord(String selectItem) {
		
		Cursor cursor;
		ArrayList<String> selectedDataList = null;
		try{
			cursor=sd.query(TBL_SURVEY_INFO, INFO_INDX_ARRAY, SURV_NAME+"=?",new String[] {selectItem} , null, null, null);
			cursor.moveToFirst();
			if(!cursor.isAfterLast()){
				do{
					selectedDataList=new ArrayList<String>();
					selectedDataList.add(cursor.getString(0));
					selectedDataList.add(cursor.getString(1));
					selectedDataList.add(cursor.getString(2));
					selectedDataList.add(cursor.getString(3));
					selectedDataList.add(cursor.getString(4));
					selectedDataList.add(cursor.getString(5));
					selectedDataList.add(cursor.getString(6));
				}while(cursor.moveToNext());
			}
			cursor.close();
		}catch (Exception e) {
			Log.d("retriveSelectedRecord", e.toString());
		}
		return selectedDataList;
		// TODO Auto-generated method stub
		
	}
	
	
	public ArrayList<ArrayList<Object>> retriveSurvey() {

		Cursor cursor;
		ArrayList<ArrayList<Object>> tableData = new ArrayList<ArrayList<Object>>();

		try {
			cursor = sd.query(TABLE_NAME, SURVEY_TBL_ARRAY, null, null, null,
					null, null);

			cursor.moveToFirst();

			if (!cursor.isAfterLast()) {
				do {
					ArrayList<Object> rowData = new ArrayList<Object>();

					rowData.add(cursor.getInt(0));
					rowData.add(cursor.getString(1));

					tableData.add(rowData);
				} while (cursor.moveToNext());

			}
			cursor.close();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(e);
		}

		return tableData;

	}

	class OpenHelper extends SQLiteOpenHelper {

		public OpenHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_TABLE);
			db.execSQL(CREATE_INFO_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

		}

	}

	public void updateSelectedInfo(String selectIndx, String global_urlVal, String nameValue, String idValue, String urlValue, String resIdValue) {
		ContentValues cv=new ContentValues();
		cv.put(GLOBAL_URL, global_urlVal);
		cv.put(SURVEY_ID, idValue);
		cv.put(SURV_NAME, nameValue);
		cv.put(LOCAL_URL, urlValue);
		cv.put(RESPOND_ID, resIdValue);
		sd.update(TBL_SURVEY_INFO, cv, INDEX_NO+"="+selectIndx, null);
		
	}

	

}
