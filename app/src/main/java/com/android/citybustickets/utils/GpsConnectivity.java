package com.android.citybustickets.utils;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;

/**
 * 
 * @author Vinil.S
 * 
 */
public class GpsConnectivity {

	private Context context;

	public GpsConnectivity(Context context) {
		this.context = context;
	}

	@SuppressWarnings("deprecation")
	public void turnGPSOn() {
		Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
		intent.putExtra("enabled", true);
		context.sendBroadcast(intent);

		String provider = Settings.Secure.getString(
				context.getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		if (!provider.contains("gps")) { // if gps is disabled
			final Intent poke = new Intent();
			poke.setClassName("com.android.settings",
					"com.android.settings.widget.SettingsAppWidgetProvider");
			poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			poke.setData(Uri.parse("3"));
			context.sendBroadcast(poke);
		}
	}

	public void turnGPSOff() {
		Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
		intent.putExtra("enabled", false);
		context.sendBroadcast(intent);
	}

	public boolean isGPSEnabled() {
		if(this.context!=null) {
			// Acquire a reference to the system Location Manager
			LocationManager locationManager = (LocationManager) this.context
					.getSystemService(Context.LOCATION_SERVICE);
			if (locationManager
					.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
					|| locationManager
					.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
}
