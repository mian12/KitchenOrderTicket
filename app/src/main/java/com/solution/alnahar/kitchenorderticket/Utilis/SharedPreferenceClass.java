package com.solution.alnahar.kitchenorderticket.Utilis;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceClass {

	public static SharedPreferences sharedPreferences;
	public static SharedPreferences.Editor sharedPrefEditor;


	public SharedPreferenceClass(Context mContext, String sharedPreferenceName, int MODE) {
		sharedPreferences = mContext.getSharedPreferences(sharedPreferenceName, MODE);
		sharedPrefEditor = sharedPreferences.edit();
	}


public static void setSessionFunc(String stage, String value) {
		sharedPrefEditor.putString(stage, value);
		sharedPrefEditor.commit();

	}

	public static String getSessionFunc(String stage) {
		return sharedPreferences.getString(stage, "false");
	}


	public static void setSessionTimeFunc(String stage, String value) {
		sharedPrefEditor.putString(stage, value);
		sharedPrefEditor.commit();

	}

	public static String getSessionTimeFunc(String stage) {
		return sharedPreferences.getString(stage, "false");
	}






	public static void clearAll() {
		sharedPrefEditor.clear();
		sharedPrefEditor.commit();
	}



	public static SharedPreferenceClass getInstance(Context mContext, String sharedPreferenceName, int MODE) {
		return new SharedPreferenceClass(mContext, sharedPreferenceName, MODE);
	}

}
