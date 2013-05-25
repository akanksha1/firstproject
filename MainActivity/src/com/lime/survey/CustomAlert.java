package com.lime.survey;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class CustomAlert {
public static void cstmAlert(Context context,String msg){
	AlertDialog.Builder al=new AlertDialog.Builder(context);
	al.setMessage(msg);
	al.setPositiveButton("Ok", new OnClickListener() {
		
		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}
	});
	AlertDialog alert = al.create();
	alert.show();
}

}
