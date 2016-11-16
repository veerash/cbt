package com.android.citybustickets.shareprefferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 
 * @author Vinil.S
 * 
 */
public class SharedPreferencesData {
	// used to create shared preference manager data under this reference
	private String app_reference = "com.freshspoon.spoonly.credetentials";

	// storing the device id into shared preferences with this key
	public String DEVICEIDKEY = "device_id_key";

	// storing the password into shared preferences with this key
	public String AUTHORIZATIONTOKENKEY = "authorization_token_key";

	// storing the access toUSER_DEVICE_IDken preference into shared preferences
	// with this key
	public String ACCESS_TOKEN_KEY = "access_token_key";

	// storing the session cookie into shared preferences with this key
	public String USER_EMAIL_KEY = "user_email_key";

	public String NAMEKEY = "name_key";
	// storing the current latitude into shared preferences with this key
	public String CURRENT_LATITUDE = "current_latitude";
	// storing the current longitude into shared preferences with this key
	public String CURRENT_LONGITUDE = "current_longitude";
	// storing the current latitude into shared preferences with this key
	public String APP_LATITUDE = "app_latitude";
	// storing the current longitude into shared preferences with this key
	public String APP_LONGITUDE = "app_longitude";

	// string to store notification value
	public String USER_ID = "user_id";

	// string to store notification value
	public String USER_ALREADY_LOGIN_KEY = "user_already_login_key";

	// string to app version code
	public String APP_CURRENT_VERSION_CODE = "app_current_version_code";

	/**
	 * 
	 * @param context
	 * @param Key
	 * @param Value
	 */
	public void storeValueIntoSharedPreference(Context context, String Key,
											   String Value) {
		Editor editor = context.getSharedPreferences(app_reference,
				Context.MODE_PRIVATE).edit();
		editor.putString(Key, Value);
		editor.commit();
	}

	/**
	 * 
	 * @param context
	 * @param Key
	 * @return String
	 */
	public String retreiveValueFromSharedPreference(Context context, String Key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				app_reference, Context.MODE_PRIVATE);

		return sharedPreferences.getString(Key, null);

	}

	/**
	 * 
	 * @param context
	 * @param Key
	 * @param Value
	 */
	public void storeIntValueIntoSharedPreference(Context context, String Key,
												  int Value) {
		Editor editor = context.getSharedPreferences(app_reference,
				Context.MODE_PRIVATE).edit();
		editor.putInt(Key, Value);
		editor.commit();
	}

	/**
	 * 
	 * @param context
	 * @param Key
	 * @return String
	 */
	public int retreiveIntValueFromSharedPreference(Context context, String Key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				app_reference, Context.MODE_PRIVATE);

		return sharedPreferences.getInt(Key, 0);

	}

	/**
	 * 
	 * @param context
	 * @param Key
	 * @param Value
	 */
	public void storeBooleanValueIntoSharedPreference(Context context,
													  String Key, boolean Value) {
		Editor editor = context.getSharedPreferences(app_reference,
				Context.MODE_PRIVATE).edit();
		editor.putBoolean(Key, Value);
		editor.commit();
	}

	/**
	 * 
	 * @param context
	 * @param Key
	 * @return String
	 */
	public boolean retreiveBooleanValueFromSharedPreference(Context context,
			String Key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				app_reference, Context.MODE_PRIVATE);

		return sharedPreferences.getBoolean(Key, false);

	}
}
