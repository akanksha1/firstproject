package com.lime.survey;

//import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

//@SuppressLint({ "ShowToast", "ShowToast" })
public class CustomToast {
	public static Toast makeCenterToast(Context context, String text, int length) {
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
		return toast;
	}

	public static Toast makeBottomToast(Context context, String text, int length) {
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
		return toast;
	}
}
