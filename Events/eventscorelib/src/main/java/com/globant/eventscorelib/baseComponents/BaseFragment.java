package com.globant.eventscorelib.baseComponents;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

    public final View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.fragment_base, container, false);
        wireUpLayouts(rootView);
        wireUpViews(rootView);
        View content=this.onCreateEventView(inflater, null, savedInstanceState);
        mContentLayout.addView(content);
        return rootView;
    }

    private void wireUpLayouts(View rootView) {
        mUtilsLayout = (LinearLayout)rootView.findViewById(R.id.utilsPanel);
        mContentLayout=(FrameLayout)rootView.findViewById(R.id.contentPanel);
    }

    abstract protected View onCreateEventView(LayoutInflater inflater, ViewGroup container,
                                         Bundle savedInstanceState);

    private void wireUpViews(View rootView) {
        mTextViewUtilsMessage=(TextView)rootView.findViewById(R.id.textView_utils);
        mImageViewUtils=(ImageView)rootView.findViewById(R.id.imageView_utils);
    }

    protected void showProgressOverlay(){
        mTextViewUtilsMessage.setText(getResources().getString(R.string.loading));
        mImageViewUtils.setImageDrawable(getResources().getDrawable(R.drawable.wifi_xxl));
        mContentLayout.setVisibility(View.GONE);
        mUtilsLayout.setVisibility(View.VISIBLE);
    }

    protected void showProgressOverlay(String messageProgress){
        mTextViewUtilsMessage.setText(messageProgress);
        mContentLayout.setVisibility(View.GONE);
        mUtilsLayout.setVisibility(View.VISIBLE);
    }

    protected void showErrorOverlay(){
        mTextViewUtilsMessage.setText(getResources().getString(R.string.error_message));
        mImageViewUtils.setImageDrawable(getResources().getDrawable(R.drawable.error_xxl));
        mContentLayout.setVisibility(View.GONE);
        mUtilsLayout.setVisibility(View.VISIBLE);
    }

    protected void showErrorOverlay(String messageError){
        mTextViewUtilsMessage.setText(messageError);
        mContentLayout.setVisibility(View.GONE);
        mUtilsLayout.setVisibility(View.VISIBLE);
    }


}
