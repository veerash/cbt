package com.android.citybustickets.customviews.radiobuttons;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.TextView;



@SuppressWarnings("nls")
public class LatoRegularRadioButton extends RadioButton {

	public LatoRegularRadioButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public LatoRegularRadioButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public LatoRegularRadioButton(Context context) {
		super(context);
		init();
	}

	private void init() {
			Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
					"fonts/Lato-Regular_0.ttf");
			setTypeface(tf);

	}

}