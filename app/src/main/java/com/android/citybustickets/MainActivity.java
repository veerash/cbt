package com.android.citybustickets;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.citybustickets.services.GetAddressFromLocation;
import com.android.citybustickets.shareprefferences.SharedPreferencesData;
import com.android.citybustickets.utils.GpsConnectivity;
import com.android.citybustickets.utils.InternetConnectivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener {
    private MapFragment mapFragment;
    private GoogleMap googleMap;
    private TextView pickupPointAddressTextView, droppingPointAddressTextView;
    private Handler fetchAddressHandler;
    private Runnable notification;
    private LatLngBounds.Builder bounds;
    private LinearLayout searchlayout;
    private RelativeLayout mMapCpntainer, markerLoadingTextView;
    private SharedPreferencesData sharedPreferencesData;
    private String lat, lng, mSearhLocationText;
    private ImageView[] strips;
    private ImageView mMapPin;
    public static String TAG = "Maps Fragment";
    private boolean isMenuAvailable = false;
    private Animation bottomUp, bottomDown;
    private Handler mHandler = new Handler();
    private ImageView myAnimation, myLocationIV;
    private boolean mGetLocationDetailsFromApi = true;
    private GoogleApiClient mGoogleApiClient;
    private Dialog mFullScreenDialog;
    private Button mIntroTextView, mFinishButton;
    private int noOfTimesRequested = 0;
    public final int GETPICKUPPOINT = 1, GETDROPPINGPOINT = 2;
    private LatLng mPickupPoint, mDroppingPoint;
    private Button mNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        sharedPreferencesData = new SharedPreferencesData();
        initializeView();
        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)

                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//        showMap();
    }

    private void initializeView() {
        fetchAddressHandler = new Handler();
        strips = new ImageView[3];

        mMapCpntainer = (RelativeLayout) findViewById(R.id.rel);
        markerLoadingTextView = (RelativeLayout) findViewById(R.id.marker_loading_textview);
        pickupPointAddressTextView = (TextView) findViewById(R.id.pickup_address_textview);
        droppingPointAddressTextView = (TextView) findViewById(R.id.dest_address_textview);
        mNextButton = (Button) findViewById(R.id.next_button);
        mMapPin = (ImageView) findViewById(R.id.map_pin);

        myAnimation = (ImageView) findViewById(R.id.my_image);
        myLocationIV = (ImageView) findViewById(R.id.my_location_button);

        setOnClickListeners();
        showLoading();
        if (getIntent().hasExtra("search_address") && getIntent().getExtras().getString("search_address") != null) {
            mSearhLocationText = "" + getIntent().getExtras().getString("search_address");
            mGetLocationDetailsFromApi = false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GETPICKUPPOINT:
                if (data != null && data.hasExtra("search_address") && data.getExtras().getString("search_address") != null) {
                    mSearhLocationText = "" + data.getExtras().getString("search_address");
                    mGetLocationDetailsFromApi = false;
                    showMap();
                }

                break;
            case GETDROPPINGPOINT:
                if (data != null && data.hasExtra("search_address") && data.getExtras().getString("search_address") != null) {
                    droppingPointAddressTextView.setText("" + data.getExtras().getString("search_address"));
                    mNextButton.setVisibility(View.VISIBLE);
                    mDroppingPoint = new LatLng(data.getExtras().getDouble("latitude"), data.getExtras().getDouble("longitude"));
                    Intent mapActivityIntent = new Intent(MainActivity.this, MapsActivity.class);
                    mapActivityIntent.putExtra("src_latitude", mPickupPoint.latitude);
                    mapActivityIntent.putExtra("src_longitude", mPickupPoint.longitude);
                    mapActivityIntent.putExtra("dest_latitude", mDroppingPoint.latitude);
                    mapActivityIntent.putExtra("dest_longitude", mDroppingPoint.longitude);
                    startActivity(mapActivityIntent);

                }
                break;
            default:
                break;

        }
    }

    private void setOnClickListeners() {
        pickupPointAddressTextView.setOnClickListener(this);
        droppingPointAddressTextView.setOnClickListener(this);
        myLocationIV.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
    }

    public void showLoading() {
        final AnimationDrawable myAnimationDrawable = (AnimationDrawable) myAnimation
                .getDrawable();

        myAnimation.post(new Runnable() {
            @Override
            public void run() {
                myAnimationDrawable.start();
            }
        });
    }

    private void showMap() {

//        googleMap=mapFragment.get
//        googleMap = ((MapFragment) getFragmentManager()
//                .findFragmentById(R.id.map)).getMapAsync(this);

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng arg0) {

            }
        });

        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        try {
            LatLng currentLatng = new LatLng(
                    Double.parseDouble(sharedPreferencesData
                            .retreiveValueFromSharedPreference(
                                    getApplicationContext(),
                                    sharedPreferencesData.CURRENT_LATITUDE)),
                    Double.parseDouble(sharedPreferencesData
                            .retreiveValueFromSharedPreference(
                                    getApplicationContext(),
                                    sharedPreferencesData.CURRENT_LONGITUDE)));
            mPickupPoint = currentLatng;
            googleMap
                    .moveCamera(CameraUpdateFactory.newLatLng(currentLatng));
            googleMap.addMarker(new MarkerOptions()
                    .position(currentLatng)
                    .title("Current location"));


        } catch (NullPointerException ne) {
            ne.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();

        }
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(final CameraPosition position) {
                googleMap.clear();
                showMyLocationButton();
                fetchAddressHandler.removeCallbacks(notification);
                if (isInternetAvailable()) {
                    notification = new Runnable() {
                        public void run() {
                            if (mGetLocationDetailsFromApi) {
                                pickupPointAddressTextView.setText(new GetAddressFromLocation()
                                        .getLocationAddressFromLatLng(MainActivity.this,
                                                position.target));
                            } else {
                                pickupPointAddressTextView.setText("" + mSearhLocationText);
                                mGetLocationDetailsFromApi = true;
                            }

                            boolean within_range = false;
                            sharedPreferencesData.storeValueIntoSharedPreference(getApplicationContext(),
                                    sharedPreferencesData.CURRENT_LATITUDE, "" + position.target.latitude);
                            sharedPreferencesData.storeValueIntoSharedPreference(getApplicationContext(),
                                    sharedPreferencesData.CURRENT_LONGITUDE, ""
                                            + position.target.longitude);
                            //TODO: fetch data based on that
                        }
                    };
                    markerLoadingTextView.setVisibility(View.GONE);
                    fetchAddressHandler.postDelayed(notification, 500);
                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        showMap();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()
                && !mRequestingLocationUpdates) {
            createLocationRequest();
            startLocationUpdates();
        }
    }

    private void showMyLocationButton() {
        GpsConnectivity gpsConnectivity = new GpsConnectivity(MainActivity.this);
        if (gpsConnectivity.isGPSEnabled()) {
            myLocationIV.setVisibility(View.VISIBLE);
        } else {
            myLocationIV.setVisibility(View.GONE);
        }
    }

    public boolean isInternetAvailable() {
        if (new InternetConnectivity(MainActivity.this).isNetworkAvailable()) {
            return true;
        } else {
            Toast.makeText(
                    MainActivity.this,
                    "Please check your internet connection",
                    Toast.LENGTH_LONG).show();
            return false;
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == droppingPointAddressTextView) {
            Intent dropIntent = new Intent(MainActivity.this, SearchActivity.class);
            dropIntent.putExtra("pick_location", true);
            startActivityForResult(dropIntent, GETDROPPINGPOINT);
        } else if (v == pickupPointAddressTextView) {
            Intent dropIntent = new Intent(MainActivity.this, SearchActivity.class);
            startActivityForResult(dropIntent, GETPICKUPPOINT);
        } else if (v == mNextButton) {
            Intent mapActivityIntent = new Intent(MainActivity.this, MapsActivity.class);
            mapActivityIntent.putExtra("src_latitude", mPickupPoint.latitude);
            mapActivityIntent.putExtra("src_longitude", mPickupPoint.longitude);
            mapActivityIntent.putExtra("dest_latitude", mDroppingPoint.latitude);
            mapActivityIntent.putExtra("dest_longitude", mDroppingPoint.longitude);
            startActivity(mapActivityIntent);
        } else if (v == myLocationIV) {
            try {
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(
                        googleMap.getMyLocation().getLatitude(), googleMap
                        .getMyLocation().getLongitude())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_bookings) {
            // Handle the camera action
        } else if (id == R.id.nav_offers) {

        } else if (id == R.id.nav_contact) {

        } else if (id == R.id.nav_feedback) {

        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
    public void onConnectionFailed(ConnectionResult result) {

    }

    Location mLastLocation;
    LocationRequest mLocationRequest = new LocationRequest();
    boolean mRequestingLocationUpdates;

    protected void createLocationRequest() {

        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(5000);
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

    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    public void startLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private void storeLocation(Location location) {
        sharedPreferencesData.storeValueIntoSharedPreference(
                MainActivity.this,
                sharedPreferencesData.CURRENT_LATITUDE,
                "" + location.getLatitude());
        sharedPreferencesData.storeValueIntoSharedPreference(
                MainActivity.this,
                sharedPreferencesData.CURRENT_LONGITUDE,
                "" + location.getLongitude());
        sharedPreferencesData.storeValueIntoSharedPreference(
                MainActivity.this, sharedPreferencesData.APP_LATITUDE, ""
                        + location.getLatitude());
        sharedPreferencesData.storeValueIntoSharedPreference(
                MainActivity.this, sharedPreferencesData.APP_LONGITUDE,
                "" + location.getLongitude());

    }


    @Override
    public void onPause() {
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
