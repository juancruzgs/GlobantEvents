package com.globant.eventscorelib.utils;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by juan.soler on 6/18/2015.
 */
public class TintInformation{
    private ImageView mImageView;
    private Drawable mDrawable;
    private ErrorLabelLayout mErrorLabelLayout;

    public TintInformation(ImageView imageView, Drawable drawable, ErrorLabelLayout errorLabelLayout) {
        mImageView = imageView;
        mDrawable = drawable;
        mErrorLabelLayout = errorLabelLayout;
    }

    public ImageView getImageView() {
        return mImageView;
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public ErrorLabelLayout getErrorLabelLayout() {
        return mErrorLabelLayout;
    }
}
