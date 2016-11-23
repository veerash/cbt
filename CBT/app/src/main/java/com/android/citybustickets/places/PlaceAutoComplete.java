package com.android.citybustickets.places;

/**
 * Holder for Places Geo Data Autocomplete API results.
 */
public class PlaceAutoComplete {

	public CharSequence placeId;
	public CharSequence description;

	public PlaceAutoComplete(CharSequence placeId, CharSequence description) {
		this.placeId = placeId;
		this.description = description;
	}

	@Override
	public String toString() {
		return description.toString();
	}
}