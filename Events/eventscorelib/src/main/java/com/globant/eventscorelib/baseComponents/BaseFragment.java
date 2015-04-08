package com.globant.eventscorelib.baseComponents;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

    public final View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.fragment_base, container, false);
        wireUpViews(rootView);
        View content=this.onCreateEventView(inflater, null, savedInstanceState);
        mContentLayout.addView(content);
        return rootView;
    }

    abstract protected View onCreateEventView(LayoutInflater inflater, ViewGroup container,
                                         Bundle savedInstanceState);

    private void wireUpViews(View rootView) {
        mUtilsLayout = (LinearLayout)rootView.findViewById(R.id.utilsPanel);
        mContentLayout=(FrameLayout)rootView.findViewById(R.id.contentPanel);
        mTextViewUtilsMessage=(TextView)rootView.findViewById(R.id.textView_utils);
        mImageViewUtils=(ImageView)rootView.findViewById(R.id.imageView_utils);
    }

    protected void showProgressOverlay(){
        mContentLayout.setVisibility(View.GONE);
        mTextViewUtilsMessage.setText(getResources().getString(R.string.loading));
        mImageViewUtils.setImageDrawable(getResources().getDrawable(R.drawable.wifi_xxl));
        mUtilsLayout.setVisibility(View.VISIBLE);
    }

    protected void showProgressOverlay(String messageProgress){
        mContentLayout.setVisibility(View.GONE);
        mTextViewUtilsMessage.setText(messageProgress);
        mUtilsLayout.setVisibility(View.VISIBLE);
    }

    protected void showErrorOverlay(){
        mContentLayout.setVisibility(View.GONE);
        mTextViewUtilsMessage.setText(getResources().getString(R.string.error_message));
        mImageViewUtils.setImageDrawable(getResources().getDrawable(R.drawable.error_xxl));
        mUtilsLayout.setVisibility(View.VISIBLE);
    }

    protected void showErrorOverlay(String messageError){
        mContentLayout.setVisibility(View.GONE);
        mTextViewUtilsMessage.setText(messageError);
        mUtilsLayout.setVisibility(View.VISIBLE);
    }


}
