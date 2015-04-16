package com.globant.eventscorelib.baseComponents;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.globant.eventscorelib.R;

/**
 * Created by ignaciopena on 4/1/15.
 */
public abstract class BaseFragment  extends Fragment{

    private LinearLayout mUtilsLayout;
    private FrameLayout mContentLayout;
    private TextView mTextViewUtilsMessage;
    private ImageView mImageViewUtils;
    private Boolean mIsCheckin;

    public final View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.fragment_base, container, false);
        wireUpLayouts(rootView);
        wireUpViews(rootView);
        prepareCheckinTouch();
        View content=this.onCreateEventView(inflater, null, savedInstanceState);
        mContentLayout.addView(content);
        return rootView;
    }

    abstract protected View onCreateEventView(LayoutInflater inflater, ViewGroup container,
                                              Bundle savedInstanceState);

    private void wireUpLayouts(View rootView) {
        mUtilsLayout = (LinearLayout)rootView.findViewById(R.id.utilsPanel);
        mContentLayout=(FrameLayout)rootView.findViewById(R.id.contentPanel);
    }

    private void wireUpViews(View rootView) {
        mTextViewUtilsMessage=(TextView)rootView.findViewById(R.id.textView_utils);
        mImageViewUtils=(ImageView)rootView.findViewById(R.id.imageView_utils);
    }

    private void prepareCheckinTouch() {
        mIsCheckin = false;
        mImageViewUtils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsCheckin){
                    hideUtilsAndShowContentOverlay();
                    mIsCheckin = false;
                }
            }
        });
    }

    protected void showProgressOverlay(){
        mTextViewUtilsMessage.setText(getResources().getString(R.string.loading));
        mImageViewUtils.setImageDrawable(getResources().getDrawable(R.drawable.loading));
        mContentLayout.setVisibility(View.GONE);
        mUtilsLayout.setVisibility(View.VISIBLE);
    }

    protected void showProgressOverlay(String messageProgress){
        mTextViewUtilsMessage.setText(messageProgress);
        mImageViewUtils.setImageDrawable(getResources().getDrawable(R.drawable.loading));
        mContentLayout.setVisibility(View.GONE);
        mUtilsLayout.setVisibility(View.VISIBLE);
    }

    protected void showErrorOverlay(){
        mTextViewUtilsMessage.setText(getResources().getString(R.string.error_message));
        mImageViewUtils.setImageDrawable(getResources().getDrawable(R.drawable.error));
        mContentLayout.setVisibility(View.GONE);
        mUtilsLayout.setVisibility(View.VISIBLE);
    }

    protected void showErrorOverlay(String messageError){
        mTextViewUtilsMessage.setText(messageError);
        mImageViewUtils.setImageDrawable(getResources().getDrawable(R.drawable.error));
        mContentLayout.setVisibility(View.GONE);
        mUtilsLayout.setVisibility(View.VISIBLE);
    }

    protected void showCheckinOverlay(){
        mTextViewUtilsMessage.setText(getResources().getString(R.string.checkin_successfully));
        mImageViewUtils.setImageDrawable(getResources().getDrawable(R.mipmap.ic_location));
        mContentLayout.setVisibility(View.GONE);
        mUtilsLayout.setVisibility(View.VISIBLE);
        mIsCheckin = true;
    }

    protected void hideUtilsAndShowContentOverlay(){
        mUtilsLayout.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
    }
    public abstract String getTitle();
}
