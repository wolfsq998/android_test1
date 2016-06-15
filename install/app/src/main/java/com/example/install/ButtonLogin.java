package com.example.install;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/6/12.
 */
public class ButtonLogin extends LinearLayout {

    public ButtonLogin(final Context context,AttributeSet abs){
        super(context, abs);
        ((Activity)getContext()).getLayoutInflater().inflate(R.layout.login_but, this);

        TextView loginButText = (TextView)this.findViewById(R.id.login_but_text_tv);
        String butText = getResources().getString(R.string.login_but_text);
        SpannableString ss = new SpannableString(butText);
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorWhite)), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new StyleSpan(Typeface.BOLD_ITALIC),0,2,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        loginButText.setText(ss);
    }
}
