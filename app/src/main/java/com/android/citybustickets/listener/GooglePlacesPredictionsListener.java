package com.android.citybustickets.listener;


import com.android.citybustickets.places.PlaceAutoComplete;

import java.util.ArrayList;

public interface GooglePlacesPredictionsListener {
	void onSuccess(ArrayList<PlaceAutoComplete> result);

	void onFailure(Exception exception);

}
