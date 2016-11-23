package com.android.citybustickets.customviews.textviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;



@SuppressWarnings("nls")
public class Lato_Reg_TextView extends TextView {

    public Lato_Reg_TextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Lato_Reg_TextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Lato_Reg_TextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/Lato-Regular_0.ttf");
        setTypeface(tf);

    }

}