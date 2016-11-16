package com.android.citybustickets.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 
 * @author Vinil.S
 *
 */
public class InternetConnectivity {
    
    private Context context;
    public InternetConnectivity(Context context) {
        this.context = context;
    }
    
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
              = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
