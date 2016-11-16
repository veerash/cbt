package com.android.citybustickets.customviews.textviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


@SuppressWarnings("nls")
public class PantonBoldTextView extends TextView {

    public PantonBoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public PantonBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PantonBoldTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/Panton-ExtraBold.otf");
        setTypeface(tf);

    }

}