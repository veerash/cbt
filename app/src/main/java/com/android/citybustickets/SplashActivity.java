package com.android.citybustickets;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.android.citybustickets.shareprefferences.SharedPreferencesData;
import com.android.citybustickets.utils.GpsConnectivity;
import com.android.citybustickets.utils.InternetConnectivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class SplashActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    GoogleApiClient mGoogleApiClient;
    private SharedPreferencesData sharedPreferencesData;
    public final int NO_GPS = 2, NO_INTERNET = 1;
    private Dialog mLocationAlertDialog, mInternetAlertDialog;
    private Button mOpenLocationSettingsButton,
            mOpenInternetSettingsButton;
    private int noOfTimesRequested = 0;
    private ImageView myImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        myImage = (ImageView) findViewById(R.id.splash_icon);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
//        Animation animation = new TranslateAnimation(0, width, 0, 0);
        Animation animation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.splash);
        animation.setDuration(3000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        myImage.setVisibility(View.VISIBLE);
                        setInternetAlertDialog();
                        setLocationAlertDialog();
                        handleInternet();
//                    }
//                }, 2000);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
//        animation.setDuration(5000);
        myImage.startAnimation(animation);
        sharedPreferencesData = new SharedPreferencesData();
        mGoogleApiClient = new GoogleApiClient.Builder(this)

                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        sharedPreferencesData.storeValueIntoSharedPreference(
                getApplicationContext(),
                sharedPreferencesData.APP_LATITUDE, "");
        sharedPreferencesData.storeValueIntoSharedPreference(
                getApplicationContext(),
                sharedPreferencesData.APP_LONGITUDE, "");







    }

    @SuppressWarnings("deprecation")
    private void setLocationAlertDialog() {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        mLocationAlertDialog = new Dialog(SplashActivity.this);
        mLocationAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mLocationAlertDialog.setCanceledOnTouchOutside(false);
        mLocationAlertDialog
                .setContentView(R.layout.dialog_location_unavailable);
        mOpenLocationSettingsButton = (Button) mLocationAlertDialog
                .findViewById(R.id.open_settings_button);
        lp.copyFrom(mLocationAlertDialog.getWindow().getAttributes());
        lp.width = getWindowManager().getDefaultDisplay().getWidth();
        mLocationAlertDialog.getWindow().setAttributes(lp);
        setOnClickListenersToDialogButtons();
    }

    private void setInternetAlertDialog() {
        mInternetAlertDialog = new Dialog(SplashActivity.this);
        mInternetAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mInternetAlertDialog.setCanceledOnTouchOutside(false);
        mInternetAlertDialog
                .setContentView(R.layout.dialog_internet_unavailable);
        mOpenInternetSettingsButton = (Button) mInternetAlertDialog
                .findViewById(R.id.open_settings_button);
        mOpenInternetSettingsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Settings.ACTION_SETTINGS),
                        NO_INTERNET);
            }
        });
    }

    private void setOnClickListenersToDialogButtons() {
        mOpenLocationSettingsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goToLocationSettingsScreen();
            }

        });
    }

    public void goToLocationSettingsScreen() {
        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(myIntent, NO_GPS);
    }


    public void goToHomeScreen() {
        Intent callingHomeIntent = new Intent(this, MainActivity.class);
        startActivity(callingHomeIntent);
        finish();
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {

    }

    Location mLastLocation;
    LocationRequest mLocationRequest = new LocationRequest();
    boolean mRequestingLocationUpdates;

    protected void createLocationRequest() {

        mLocationRequest.setInterval(100);
        mLocationRequest.setFastestInterval(50);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);
        if (mLastLocation != null && (mLastLocation.getAccuracy() <= 60 || noOfTimesRequested > 3)) {
            storeLocation(mLastLocation);
        } else {
            startLocationUpdates();
        }
        noOfTimesRequested++;
    }

    private void storeLocation(Location location) {
        sharedPreferencesData.storeValueIntoSharedPreference(
                getApplicationContext(),
                sharedPreferencesData.CURRENT_LATITUDE,
                "" + location.getLatitude());
        sharedPreferencesData.storeValueIntoSharedPreference(
                getApplicationContext(),
                sharedPreferencesData.CURRENT_LONGITUDE,
                "" + location.getLongitude());
        sharedPreferencesData.storeValueIntoSharedPreference(
                getApplicationContext(), sharedPreferencesData.APP_LATITUDE, ""
                        + location.getLatitude());
        sharedPreferencesData.storeValueIntoSharedPreference(
                getApplicationContext(), sharedPreferencesData.APP_LONGITUDE,
                "" + location.getLongitude());
        GpsConnectivity gpsConnectivity = new GpsConnectivity(this);
        InternetConnectivity intConnectivity = new InternetConnectivity(this);
        if (gpsConnectivity.isGPSEnabled()
                && intConnectivity.isNetworkAvailable()) {
            goToHomeScreen();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()
                && !mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    public void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    /**
     * Check GPS status, And show an alert to enable GPS if necessary.
     */
    void handleLocation() {
        GpsConnectivity gpsConnectivity = new GpsConnectivity(this);
        if (!gpsConnectivity.isGPSEnabled()) {
            /*
             * GPS disabled. Show an alert that can navigate to GPS setting
			 * screen.
			 */
            showLocationDialog();
        } else {
            hideLocationAlertDialog();
            getLocationDetails();
        }
    }

    void handleInternet() {
        InternetConnectivity intConnectivity = new InternetConnectivity(this);
        if (!intConnectivity.isNetworkAvailable()) {
            /*
             * GPS disabled. Show an alert that can navigate to GPS setting
			 * screen.
			 */
            showInternetDialog();
        } else {
            hideInternetAlertDialog();
            handleLocation();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case NO_GPS:
                handleLocation();
                break;
            case NO_INTERNET:
                handleInternet();
                break;
            default:
                break;
        }
    }

    public void getLocationDetails() {
        createLocationRequest();
    }

    public void showLocationDialog() {
        if (mLocationAlertDialog != null) {
            if (!mLocationAlertDialog.isShowing()) {
                mLocationAlertDialog.show();
            } else {
                setLocationAlertDialog();
                mLocationAlertDialog.show();
            }
        } else {
            setLocationAlertDialog();
            mLocationAlertDialog.show();
        }
    }

    private void hideLocationAlertDialog() {
        if (mLocationAlertDialog != null || mLocationAlertDialog.isShowing()) {
            mLocationAlertDialog.dismiss();
        }
    }

    public void showInternetDialog() {
        if (mInternetAlertDialog != null) {
            if (!mInternetAlertDialog.isShowing()) {
                mInternetAlertDialog.show();
            } else {
                setInternetAlertDialog();
                mInternetAlertDialog.show();
            }
        } else {
            setInternetAlertDialog();
            mInternetAlertDialog.show();
        }

    }

    private void hideInternetAlertDialog() {
        if (mInternetAlertDialog != null || mInternetAlertDialog.isShowing()) {
            mInternetAlertDialog.dismiss();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null && (location.getAccuracy() <= 60 || noOfTimesRequested > 3)) {
            storeLocation(location);
        } else {
            startLocationUpdates();
        }
        noOfTimesRequested++;

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }

}