package com.android.citybustickets.places;

import android.content.Context;
import android.os.AsyncTask;
import android.text.style.CharacterStyle;

import com.android.citybustickets.listener.GooglePlacesPredictionsListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class PlacesApiTask extends
        AsyncTask<String, Void, ArrayList<PlaceAutoComplete>> {
    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(17.359206, 78.288955), new LatLng(17.531815, 78.414611));
    Context context;
    private GoogleApiClient mGoogleApiClient;
    private GooglePlacesPredictionsListener mAccessTokenListener;
    private String TAG = this.getClass().getSimpleName();

    public PlacesApiTask(Context context, GoogleApiClient mGoogleApiClient,
                         GooglePlacesPredictionsListener listener) {
        this.context = context;
        this.mAccessTokenListener = listener;
        this.mGoogleApiClient = mGoogleApiClient;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected ArrayList<PlaceAutoComplete> doInBackground(String... params) {
        PendingResult<AutocompletePredictionBuffer> results = Places.GeoDataApi
                .getAutocompletePredictions(mGoogleApiClient,
                        params[0].toString(), BOUNDS_GREATER_SYDNEY, null);
        // This method should have been called off the main UI thread.
        // Block and wait for at most 60s
        // for a result from the API.
        AutocompletePredictionBuffer autocompletePredictions = results.await(
                60, TimeUnit.SECONDS);

        // Confirm that the query completed successfully, otherwise
        // return null
        final com.google.android.gms.common.api.Status status = autocompletePredictions
                .getStatus();
        if (!status.isSuccess()) {
            autocompletePredictions.release();
            return null;
        }


        // can't hold onto the buffer.
        // AutocompletePrediction objects encapsulate the API response
        // (place ID and description).

        Iterator<AutocompletePrediction> iterator = autocompletePredictions
                .iterator();
        ArrayList resultList = new ArrayList<>(
                autocompletePredictions.getCount());
        while (iterator.hasNext()) {
            AutocompletePrediction prediction = iterator.next();
            // Get the details of this prediction and copy it into a new
            // PlaceAutocomplete object.
            resultList.add(new PlaceAutoComplete(prediction.getPlaceId(),
                    prediction.getFullText(null)));
        }

        // Release the buffer now that all data has been copied.
        autocompletePredictions.release();
        return resultList;
    }

    @Override
    protected void onPostExecute(ArrayList<PlaceAutoComplete> result) {
        if (result != null)
            mAccessTokenListener.onSuccess(result);
        else {
            mAccessTokenListener.onFailure(new NullPointerException());
        }
        super.onPostExecute(result);
    }
}