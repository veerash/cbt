package com.android.citybustickets.services;

import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import com.android.citybustickets.utils.InternetConnectivity;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * @author Vinil.S Fetches address based on location
 */
public class GetAddressFromLocation {
	public String TAG = "GETADDRESSFROMLOCATION";
//
//	/**
//	 * @author Vinil.S
//	 * @param latlng
//	 * @return
//	 */
//	public String getLocationAddressFromLatLng(Context ctx,
//			LatLng latlng) {
//		String address = null;
//		try {
//			Geocoder geocoder;
//			List<Address> addresses;
//			geocoder = new Geocoder(ctx, Locale.getDefault());
//			addresses = geocoder.getFromLocation(latlng.latitude,
//					latlng.longitude, 1);
//			address = addresses.get(0).getAddressLine(0);
//			if (addresses.get(0).getAddressLine(1) != null) {
//				address = address + ", " + addresses.get(0).getAddressLine(1);
//			}
////			if (addresses.get(0).getAddressLine(2) != null) {
////				address = address + ", " + addresses.get(0).getAddressLine(2);
////			}
//		} catch (Exception e) {
//			SpoonlyApplication.getInstance().initiateLogger().error(TAG, "Error while getting locations: " + e.getMessage());
//			e.printStackTrace();
//		}
//		return address;
//	}
// google place api url
public String GOOGLE_PLACES_API_URL = "http://maps.googleapis.com/maps/api/geocode/json?";
	// google maps sensor
	public String GOOGLE_API_SENSOR = "&sensor=false";
	/**
	 * @author Vinil.S
	 * @param latlng
	 * @return
	 */
	String address = null;
	StringTokenizer mStringTokenizer;

	public String getAreaAddressFromLatLng(final Context ctx,
											   LatLng latlng) {
		// previously we were using the google places api to fetch places as it
		// is giving some api error we have been using the geocoder
		final String ss = GOOGLE_PLACES_API_URL + "latlng=" + latlng.latitude
				+ "," + latlng.longitude + GOOGLE_API_SENSOR;

		final ArrayList<String> addresses = new ArrayList<>();
		try {
			if (new InternetConnectivity(ctx).isNetworkAvailable()) {
				try {
					String response = readWebService(ss);
					JSONObject json = null;
					json = new JSONObject(response);
					JSONArray jarr = json
							.getJSONArray("results");
					json = jarr.getJSONObject(0);
					String adress = json
							.getString("formatted_address");
					mStringTokenizer = new StringTokenizer(
							adress, ",");
					while (mStringTokenizer.hasMoreElements()) {
						addresses.add(""
								+ mStringTokenizer
								.nextElement());
					}
					if (address == null) {
						address = addresses.get(1).trim()+"Bus Stop";
//						address = address
//								+ ","
//								+ addresses.get(1).trim();
//						address = address
//								+ ","
//								+ addresses.get(2).trim();
					}
				} catch (JSONException e) {

				}

			}


		} catch (Exception e) {

		} finally {
		}
		return address;
	}
	/**
	 * @author Vinil.S
	 * @param latlng
	 * @return
	 */

	public String getLocationAddressFromLatLng(final Context ctx,
											   LatLng latlng) {
		// previously we were using the google places api to fetch places as it
		// is giving some api error we have been using the geocoder
		final String ss = GOOGLE_PLACES_API_URL + "latlng=" + latlng.latitude
				+ "," + latlng.longitude + GOOGLE_API_SENSOR;

		final ArrayList<String> addresses = new ArrayList<>();
		try {
			if (new InternetConnectivity(ctx).isNetworkAvailable()) {
				try {
					String response = readWebService(ss);
					JSONObject json = null;
					json = new JSONObject(response);
					JSONArray jarr = json
							.getJSONArray("results");
					json = jarr.getJSONObject(0);
					String adress = json
							.getString("formatted_address");
					mStringTokenizer = new StringTokenizer(
							adress, ",");
					while (mStringTokenizer.hasMoreElements()) {
						addresses.add(""
								+ mStringTokenizer
								.nextElement());
					}
					if (address == null) {
						address = addresses.get(0).trim();
						address = address
								+ ","
								+ addresses.get(1).trim();
						address = address
								+ ","
								+ addresses.get(2).trim();
					}
				} catch (JSONException e) {

				}

			}


		} catch (Exception e) {

		} finally {
		}
		return address;
	}
	/**
	 * @author Vinil.S
	 * @param s
	 * @return
	 */

	public String readWebService(String s) {
		try {
			if (Build.VERSION.SDK_INT >= 10) {
				StrictMode.ThreadPolicy tp = StrictMode.ThreadPolicy.LAX;
				StrictMode.setThreadPolicy(tp);
			}
			String str = "";
			URL url = new URL(s);
			Scanner sc = new Scanner(url.openConnection().getInputStream());
			while (sc.hasNext()) {
				str = str + sc.nextLine();
			}

			return str;
		} catch (Exception e) {
			Log.e(TAG, "Error while connecting: " + e.getMessage());
			e.printStackTrace();
			return null;
		}

	}
}
