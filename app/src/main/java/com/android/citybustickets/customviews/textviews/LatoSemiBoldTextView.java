package com.android.citybustickets.customviews.textviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;



@SuppressWarnings("nls")
 public class LatoSemiBoldTextView extends TextView {

	public LatoSemiBoldTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public LatoSemiBoldTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public LatoSemiBoldTextView(Context context) {
		super(context);
		init();
	}

	private void init() {
			Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
					"fonts/Lato-Semibold_0.ttf");
			setTypeface(tf);


	}

}