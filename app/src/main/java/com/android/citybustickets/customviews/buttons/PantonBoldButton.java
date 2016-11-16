package com.android.citybustickets.customviews.buttons;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;



@SuppressWarnings("nls")
public class PantonBoldButton extends Button {

	public PantonBoldButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public PantonBoldButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PantonBoldButton(Context context) {
		super(context);
		init();
	}

	private void init() {
			Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
					"fonts/Panton-ExtraBold.otf");
			setTypeface(tf);

	}

}