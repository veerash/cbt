package com.android.citybustickets;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.citybustickets.R;
import com.android.citybustickets.adapter.SelectedPlaceAdapter;
import com.android.citybustickets.dao.selected_place_details;
import com.android.citybustickets.listener.GooglePlacesPredictionsListener;
import com.android.citybustickets.places.PlaceAutoComplete;
import com.android.citybustickets.places.PlacesApiTask;
import com.android.citybustickets.shareprefferences.SharedPreferencesData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class SearchActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener {

    private TextView myLocationAddressTextView;
    private LinearLayout myLocationLayout, mPreviousLocationsLayout, mSearchLocationsLayout;
    private SharedPreferencesData sharedPreferencesData;

    protected GoogleApiClient mGoogleApiClient;
    ArrayList<PlaceAutoComplete> results;

    // private PlaceAutocompleteAdapter mAdapter;

    private AutoCompleteTextView mAutocompleteView;
    private SelectedPlaceAdapter mSelectedPlaceAdapter;
    private ListView mSeletcedPlaceListView, mSearchPlaceListView;
    private ArrayList<selected_place_details> mSelectedPlacesArrayList;

    private static String TAG = "SearchActivity";
    private Dialog mSelectedPlaceLoadingBar;
    private LatLng mAppLatLng = null;
    private ProgressBar mSearchingPlaceProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(SearchActivity.this, 51, this)
                .addApi(LocationServices.API).addApi(Places.GEO_DATA_API).build();
        setContentView(R.layout.activity_search);
        mSearchPlaceListView = (ListView) findViewById(R.id.places_search_list);
        mSeletcedPlaceListView = (ListView) findViewById(R.id.selected_address_list);
        myLocationLayout = (LinearLayout) findViewById(R.id.my_location_layout);
        mSearchLocationsLayout = (LinearLayout) findViewById(R.id.search_locations_layout);
        mPreviousLocationsLayout = (LinearLayout) findViewById(R.id.previous_locations_layout);
        mSearchingPlaceProgressBar = (ProgressBar) findViewById(R.id.selected_location_progressbar);
        myLocationAddressTextView = (TextView) findViewById(R.id.search_intro);
        // mSelectedPlaceLoadingBar = (ProgressBar)
        // findViewById(R.id.selected_location_progressbar);
        mSelectedPlacesArrayList = new ArrayList<>();
        sharedPreferencesData = new SharedPreferencesData();
//        mSelectedPlacesArrayList = SpoonlyApplication.getInstance().setUpDb().fetchAllSelectedPlacesDetails();
//        SpoonlyApplication.setCurrentActvityContext(this);
        setActionBar();
        setLocationAlertDialog(SearchActivity.this);
        mSelectedPlaceAdapter = new SelectedPlaceAdapter(SearchActivity.this, mSelectedPlacesArrayList);
        mSeletcedPlaceListView.setAdapter(mSelectedPlaceAdapter);
        mSeletcedPlaceListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hideKeyBoard();
                storeLocationDetails(mSelectedPlacesArrayList.get(position).getPlace_name(),mSelectedPlacesArrayList.get(position).getPlace_latitude(),
                        mSelectedPlacesArrayList.get(position).getPlace_longitude());
            }

        });
//        try {
//            if (sharedPreferencesData.retreiveValueFromSharedPreference(getApplicationContext(),
//                    sharedPreferencesData.APP_LATITUDE) != null && !sharedPreferencesData.retreiveValueFromSharedPreference(getApplicationContext(),
//                    sharedPreferencesData.APP_LATITUDE).equals("") && sharedPreferencesData.retreiveValueFromSharedPreference(getApplicationContext(),
//                    sharedPreferencesData.APP_LONGITUDE) != null && !sharedPreferencesData.retreiveValueFromSharedPreference(getApplicationContext(),
//                    sharedPreferencesData.APP_LONGITUDE).equals("")) {
//                myLocationLayout.setVisibility(View.VISIBLE);
//                mAppLatLng = new LatLng(
//                        Double.parseDouble(sharedPreferencesData.retreiveValueFromSharedPreference(getApplicationContext(),
//                                sharedPreferencesData.APP_LATITUDE)),
//                        Double.parseDouble(sharedPreferencesData.retreiveValueFromSharedPreference(getApplicationContext(),
//                                sharedPreferencesData.APP_LONGITUDE)));
//                if (isInternetAvailable()) {
//                    myLocationAddressTextView.setText(""
//                            + new GetAddressFromLocation().getLocationAddressFromLatLng(SearchActivity.this, mAppLatLng));
//                }
//                myLocationAddressTextView.setOnClickListener(new OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        hideKeyBoard();
//                        storeLocationDetails(null,mAppLatLng.latitude, mAppLatLng.longitude);
//
//                    }
//                });
//            }else{
//                SpoonlyApplication.getInstance().initiateLogger().error(TAG,
//                        getString(R.string.unable_to_find_app_location));
//                myLocationLayout.setVisibility(View.GONE);
//            }
//        } catch (Exception e) {
//            SpoonlyApplication.getInstance().initiateLogger().error(TAG,
//                    getString(R.string.unable_to_find_app_location));
//            Crashlytics.logException(e);
            myLocationLayout.setVisibility(View.GONE);
//        }
    }

    private void hideKeyBoard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    private void setLocationAlertDialog(Context context) {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        mSelectedPlaceLoadingBar = new Dialog(context);
        mSelectedPlaceLoadingBar.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSelectedPlaceLoadingBar.setCanceledOnTouchOutside(false);
        mSelectedPlaceLoadingBar.setContentView(R.layout.dialog_locting_area);
        lp.copyFrom(mSelectedPlaceLoadingBar.getWindow().getAttributes());
        lp.width = getWindowManager().getDefaultDisplay().getWidth();
        mSelectedPlaceLoadingBar.getWindow().setAttributes(lp);
    }

    @Override
    protected void onStop() {
//		hideKeyBoard();
        super.onStop();
    }

    private void setActionBar() {

        // clearSearchImageview = (ImageView)
        // findViewById(R.id.clear_search_imageview);
        mAutocompleteView = (AutoCompleteTextView) findViewById(R.id.search_edittext);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        // Register a listener that receives callbacks when a suggestion has
        // been selected

        // Set up the adapter that will retrieve suggestions from the Places Geo
        // Data API that cover
        // the entire world.
        // mAdapter = new PlaceAutocompleteAdapter(this,
        // R.layout.list_row_auto_complete_text, mGoogleApiClient,
        // BOUNDS_GREATER_SYDNEY, null);
        // mAutocompleteView.setAdapter(mAdapter);
        mAutocompleteView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if (s.length() > 2) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mSearchLocationsLayout.setVisibility(View.VISIBLE);
                            mPreviousLocationsLayout.setVisibility(View.GONE);
                            mSearchingPlaceProgressBar.setVisibility(View.VISIBLE);
                            new PlacesApiTask(SearchActivity.this, mGoogleApiClient, listener).execute(s.toString());
                        }
                    });

                }
                if (s.length() == 0) {
                    mSearchLocationsLayout.setVisibility(View.GONE);
                    mPreviousLocationsLayout.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    GooglePlacesPredictionsListener listener = new GooglePlacesPredictionsListener() {

        @Override
        public void onSuccess(ArrayList<PlaceAutoComplete> result) {
            results = result;
            mSearchLocationsLayout.setVisibility(View.VISIBLE);
            mSearchingPlaceProgressBar.setVisibility(View.GONE);
            mPreviousLocationsLayout.setVisibility(View.GONE);
            mSearchPlaceListView
                    .setAdapter(new ArrayAdapter<>(SearchActivity.this, R.layout.list_row_auto_complete_text, result));
            mSearchPlaceListView.setOnItemClickListener(mAutocompleteClickListener);
        }

        @Override
        public void onFailure(Exception exception) {
            mSearchLocationsLayout.setVisibility(View.GONE);
            mSearchingPlaceProgressBar.setVisibility(View.GONE);
            mPreviousLocationsLayout.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
//        SpoonlyApplication.setCurrentActvityContext(SearchActivity.this);
    }

    /**
     * Listener that handles selections from suggestions from the
     * AutoCompleteTextView that displays Place suggestions. Gets the place id
     * of the selected item and issues a request to the Places Geo Data API to
     * retrieve more details about the place.
     *
     * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
     * String...)
     */
    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             * Retrieve the place ID of the selected item from the Adapter. The
			 * adapter stores each Place suggestion in a PlaceAutocomplete
			 * object from which we read the place ID.
			 */
            // final PlaceAutocompleteAdapter.PlaceAutocomplete item = mAdapter
            // .getItem(position);
            // mSelectedPlaceLoadingBar.setVisibility(View.VISIBLE);
            hideKeyBoard();
            mSelectedPlaceLoadingBar.show();
            final String placeId = String.valueOf(results.get(position).placeId);
//            SpoonlyApplication.getInstance().initiateLogger().info(TAG,
//                    "Autocomplete item selected: " + results.get(position).description);

			/*
             * Issue a request to the Places Geo Data API to retrieve a Place
			 * object with additional details about the place.
			 */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    /**
     * Callback for results from a Places Geo Data API query that shows the
     * first place result in the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            if (places.getCount() > 0) {
                final Place place = places.get(0);

                // Format details of the place for display and show it in a
                // TextView.
                String placewebsiteuri = null, placeid = null, placename = null, placeaddress = null,
                        placephonenumber = null;
                Double latitude = null, longitude = null;
                try {
                    placeid = place.getId();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    placename = place.getName().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    placeaddress = place.getAddress().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    latitude = place.getLatLng().latitude;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    longitude = place.getLatLng().longitude;
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                SpoonlyApplication.getInstance().setUpDb().updateSelectedDetails(placeid, placename, placeaddress,
//                        placephonenumber, placewebsiteuri, latitude, longitude);
                storeLocationDetails(placename,place.getLatLng().latitude, place.getLatLng().longitude);
//
//                SpoonlyApplication.getInstance().initiateLogger().info(TAG,
//                        "Place details received: " + place.getName());
                // mSelectedPlaceLoadingBar.setVisibility(View.GONE);
                if (mSelectedPlaceLoadingBar != null && mSelectedPlaceLoadingBar.isShowing()) {
                    mSelectedPlaceLoadingBar.dismiss();
                }
            }
            places.release();
        }

    };

    private void storeLocationDetails(String address, Double latitude, Double longitude) {
        if(getIntent().hasExtra("pick_location")){
            Intent intent=new Intent();
            intent.putExtra("latitude",latitude);
            intent.putExtra("longitude",longitude);
            intent.putExtra("search_address",address);
            setResult(RESULT_OK,intent);
            finish();
        }else {
            sharedPreferencesData.storeValueIntoSharedPreference(getApplicationContext(),
                    sharedPreferencesData.CURRENT_LATITUDE, "" + latitude);
            sharedPreferencesData.storeValueIntoSharedPreference(getApplicationContext(),
                    sharedPreferencesData.CURRENT_LONGITUDE, "" + longitude);
            goToHomeScreen(address);
        }
    }

    public void goToHomeScreen(String address) {
        Intent callingHomeIntent = new Intent(this, MainActivity.class);
        callingHomeIntent.putExtra("search_address",address);
        startActivity(callingHomeIntent);
        finish();
    }

    /**
     * Called when the Activity could not connect to Google Play services and
     * the auto manager could resolve the error automatically. In this case the
     * API is not available and notify the user.
     *
     * @param connectionResult can be inspected to determine the cause of the failure
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Toast.makeText(SearchActivity.this,"onConnectionFailed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode(),Toast.LENGTH_SHORT).show();

    }

}
