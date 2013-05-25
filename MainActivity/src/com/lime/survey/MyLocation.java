package com.lime.survey;

import java.util.Timer;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class MyLocation implements LocationListener {
	String TAG = "FTMS";
	SharedPreferences prefs;
	LocationManager lm;
	boolean gps_enabled = false;
	boolean network_enabled = false;

	String lat;
	String lon;
	Location currentLocation = null;
	float currentAccuracy = 10000;
	Timer timer;

	MyLocation(Context context) {
		if (lm == null)
			lm = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);
	}

	public float getCurrentAccuracy() {
		if (currentAccuracy < 10000)
			return currentAccuracy;
		else {
			startListening();
			return currentAccuracy;
		}
	}

	public Location getCurrentLocation() {
		return currentLocation;
	}

	public boolean isProviderEnabled() {
		return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	
	public void onLocationChanged(Location location) {
		currentLocation = location;
		currentAccuracy = currentLocation.getAccuracy();
		// String message ="Location Accuracy :"+currentAccuracy+
		// "\nLat :"+String.valueOf(currentLocation.getLatitude())+
		// "\nLong :"+String.valueOf(currentLocation.getLongitude());
		// Log.d(TAG,message);
		if (location.getAccuracy() < 10.0) {
			stopListening();
		}
	}

	
	public void onProviderDisabled(String provider) {
		Log.d(TAG, "GPS Disabled");

	}

	
	public void onProviderEnabled(String provider) {
		Log.d(TAG, "GPS Enabled");
	}

	
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	public int startListening() {
		// Check status of providers
		gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// network_enabled=lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		// Starting the GPS Listener
		if (gps_enabled) {
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
			return 1;
		}
		// Starting the Network Listener
		// else if(network_enabled){
		// lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
		// this);
		// return 2;
		// }
		// don't start listeners if no provider is enabled
		else
			return 0;

	}

	public void stopListening() {
		lm.removeUpdates(this);
		Log.d(TAG, "Stopped Listening");
	}

}
