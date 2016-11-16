package com.android.citybustickets.customviews.edittexts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;


@SuppressWarnings("nls")
public class LatoBoldEditText extends EditText {

    public LatoBoldEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LatoBoldEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LatoBoldEditText(Context context) {
        super(context);
        init();
    }

    private void init() {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                    "fonts/Lato-Bold.ttf");
            setTypeface(tf);

    }

}