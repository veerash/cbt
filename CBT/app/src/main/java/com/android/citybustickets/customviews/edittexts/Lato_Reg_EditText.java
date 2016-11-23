package com.android.citybustickets.customviews.edittexts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;


@SuppressWarnings("nls")
public class Lato_Reg_EditText extends EditText {

    public Lato_Reg_EditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Lato_Reg_EditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Lato_Reg_EditText(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/Lato-Regular_0.ttf");
        setTypeface(tf);

    }

}