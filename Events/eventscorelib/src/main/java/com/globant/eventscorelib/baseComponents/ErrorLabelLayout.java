package com.globant.eventscorelib.baseComponents;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by agustin.madina on 21/04/2015.
 */
public class ErrorLabelLayout extends LinearLayout implements ViewGroup.OnHierarchyChangeListener {

    private static final int ERROR_LABEL_TEXT_SIZE = 12;
    private static final int ERROR_LABEL_PADDING = 4;

    private TextView mErrorLabel;
    private Drawable mDrawable;
    private int mErrorColor;

    public ErrorLabelLayout(Context context) {
        super(context);
        initView();
    }

    public ErrorLabelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ErrorLabelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setOnHierarchyChangeListener(this);
        setOrientation(VERTICAL);
        mErrorColor = Color.parseColor("#D32F2F");
        initErrorLabel();
    }

    private void initErrorLabel() {
        mErrorLabel = new TextView(getContext());
        mErrorLabel.setFocusable(true);
        mErrorLabel.setFocusableInTouchMode(true);
        mErrorLabel.setTextSize(ERROR_LABEL_TEXT_SIZE);
        mErrorLabel.setTextColor(mErrorColor);
        mErrorLabel.setPadding(dipsToPix(ERROR_LABEL_PADDING), 0, dipsToPix(ERROR_LABEL_PADDING), 0);
    }

    public void setErrorColor(int color) {
        mErrorColor = color;
        mErrorLabel.setTextColor(mErrorColor);
    }

    public void clearError() {
        mErrorLabel.setVisibility(INVISIBLE);
        mDrawable.clearColorFilter();
    }

    public void setError(String text) {
        mErrorLabel.setVisibility(VISIBLE);
        mErrorLabel.setText(text);
        // tint drawable
        mDrawable.setColorFilter(mErrorColor, PorterDuff.Mode.SRC_ATOP);
        // changing focus from EditText to error label, necessary for Android L only
        // EditText background Drawable is not tinted, until EditText remains focus
        mErrorLabel.requestFocus();
    }

    @Override
    public void onChildViewAdded(View parent, View child) {
        int childCount = getChildCount();
        if (childCount == 1) {
            mDrawable = getChildAt(0).getBackground();
            addView(mErrorLabel);
        }
    }

    @Override
    public void onChildViewRemoved(View parent, View child) {
    }

    private int dipsToPix(float dps) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dps, getResources().getDisplayMetrics());
    }
}