package com.android.citybustickets.utils;

//import com.crashlytics.android.Crashlytics;

import android.util.Log;

/**
 * 
 * @author Vinil.S
 * 
 */
public class Logger {

	public boolean isInImplementationMode = false;

	public void info(String tag, String message) {
		if (isInImplementationMode)
			Log.i(tag, message);

	}

	public void debug(String tag, String message) {
		if (isInImplementationMode)
			Log.d(tag, message);

	}

	public void error(String tag, String message) {
		Log.e(tag, ":" + message);
//		try {
//			if (message != null && message.length() > 0) {
//				Crashlytics.logException(new RuntimeException(message));
//			} else {
//				Crashlytics.logException(new RuntimeException());
//			}
//		} catch (Exception e) {
//			Crashlytics.logException(new RuntimeException());
//		}
	}

	public void wtf(String tag, String message) {
		Log.wtf(tag, ":" + message);

	}

}
