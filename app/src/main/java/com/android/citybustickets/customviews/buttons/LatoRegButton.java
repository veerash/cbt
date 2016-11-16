package com.android.citybustickets.customviews.buttons;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;



@SuppressWarnings("nls")
public class LatoRegButton extends Button {

	public LatoRegButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public LatoRegButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public LatoRegButton(Context context) {
		super(context);
		init();
	}

	private void init() {
			Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
					"fonts/Lato-Regular_0.ttf");
			setTypeface(tf);

	}

}