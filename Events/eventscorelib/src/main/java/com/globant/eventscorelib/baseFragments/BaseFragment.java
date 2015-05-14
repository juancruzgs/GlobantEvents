package com.globant.eventscorelib.baseFragments;


import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.utils.CoreConstants;


public abstract class BaseFragment extends Fragment{

    private LinearLayout mUtilsLayout;
    private LinearLayout mLoadingLayout;
    private FrameLayout mContentLayout;
    private AppCompatTextView mTextViewUtilsMessage;
    private AppCompatTextView mTextViewLoadingMessage;
    private ImageView mImageViewUtils;
    protected BaseService mService = null;
    private Boolean mIsCheckin;

    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_base, container, false);
        wireUpLayouts(rootView);
        wireUpViews(rootView);
        prepareCheckinTouch();
        prepareContentLayout(inflater, savedInstanceState);
        return rootView;
    }

    private void wireUpLayouts(View rootView) {
        mUtilsLayout = (LinearLayout)rootView.findViewById(R.id.utilsPanel);
        mLoadingLayout = (LinearLayout)rootView.findViewById(R.id.loading_panel);
        mContentLayout=(FrameLayout)rootView.findViewById(R.id.contentPanel);
    }

    private void wireUpViews(View rootView) {
        mTextViewUtilsMessage=(AppCompatTextView)rootView.findViewById(R.id.textView_utils);
        mTextViewLoadingMessage=(AppCompatTextView)rootView.findViewById(R.id.text_view_loading);
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

    private void prepareContentLayout(LayoutInflater inflater, Bundle savedInstanceState) {
        View content = this.onCreateEventView(inflater, null, savedInstanceState);
        mContentLayout.addView(content);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mService.unSubscribeActor(getActionListener());
    }

    /// Return an ActionListener to manage the db actions... or just null
    abstract public BaseService.ActionListener getActionListener();

    abstract protected View onCreateEventView(LayoutInflater inflater, ViewGroup container,
                                              Bundle savedInstanceState);

    protected int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = CoreConstants.ZERO;
        TypedArray a = getActivity().obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    public void showProgressOverlay(){
        mLoadingLayout.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.GONE);
        mUtilsLayout.setVisibility(View.GONE);
    }

    public void showProgressOverlay(String messageProgress){
        mTextViewLoadingMessage.setText(messageProgress);
        mLoadingLayout.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.GONE);
        mUtilsLayout.setVisibility(View.GONE);
    }

    public void showErrorOverlay(){
        mTextViewUtilsMessage.setText(getResources().getString(R.string.error_message));
        mImageViewUtils.setImageDrawable(getResources().getDrawable(R.drawable.error));
        mContentLayout.setVisibility(View.GONE);
        mUtilsLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
    }

    public void showErrorOverlay(String messageError){
        mTextViewUtilsMessage.setText(messageError);
        mImageViewUtils.setImageDrawable(getResources().getDrawable(R.drawable.error));
        mContentLayout.setVisibility(View.GONE);
        mUtilsLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
    }

    public void showCheckinOverlay(){
        mTextViewUtilsMessage.setText(getResources().getString(R.string.checkin_successfully));
        mImageViewUtils.setImageDrawable(getResources().getDrawable(R.mipmap.ic_location));
        mContentLayout.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.GONE);
        mUtilsLayout.setVisibility(View.VISIBLE);
        mIsCheckin = true;
    }

    public void hideUtilsAndShowContentOverlay(){
        mUtilsLayout.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
    }

    public void setService(BaseService service) {
        this.mService = service;
        BaseService.ActionListener listener = getActionListener();
        if (listener != null){
            mService.subscribeActor(listener);
        }
    }

    public abstract String getTitle();
}
