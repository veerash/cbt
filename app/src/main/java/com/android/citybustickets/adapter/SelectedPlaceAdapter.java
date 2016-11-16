package com.android.citybustickets.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.android.citybustickets.dao.selected_place_details;
import com.android.citybustickets.R;

import java.util.ArrayList;

public class SelectedPlaceAdapter extends BaseAdapter {
	public Context context;
	public ArrayList<selected_place_details> mSelectedPlaceDetails;
	public Holder holder;

	public SelectedPlaceAdapter(Context context,
								ArrayList<selected_place_details> fragmentTitles) {
		this.context = context;
		this.mSelectedPlaceDetails = fragmentTitles;
	}

	@Override
	public int getCount() {
		return mSelectedPlaceDetails.size();
	}

	@Override
	public Object getItem(int position) {
		return mSelectedPlaceDetails.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		holder = new Holder();
		LayoutInflater infalInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = infalInflater.inflate(
					R.layout.list_row_selected_place, null);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		holder.mSelectedPlaceAddress = (TextView) convertView
				.findViewById(R.id.selected_place_name);
		holder.mSelectedPlaceAddress.setText(""
				+ mSelectedPlaceDetails.get(position).getPlace_address());

		return convertView;
	}

	public class Holder {
		public TextView mSelectedPlaceAddress;
	}

}
