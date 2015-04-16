package com.globant.eventscorelib.baseComponents;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.globant.eventscorelib.R;


public abstract class BaseFragment extends Fragment{

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

    abstract protected View onCreateEventView(LayoutInflater inflater, ViewGroup container,
                                              Bundle savedInstanceState);

    public abstract String getTitle();

    protected int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = getActivity().obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    private void wireUpLayouts(View rootView) {
        mUtilsLayout = (LinearLayout)rootView.findViewById(R.id.utilsPanel);
        mContentLayout=(FrameLayout)rootView.findViewById(R.id.contentPanel);
    }

    private void wireUpViews(View rootView) {
        mTextViewUtilsMessage=(TextView)rootView.findViewById(R.id.textView_utils);
        mImageViewUtils=(ImageView)rootView.findViewById(R.id.imageView_utils);
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

    protected void hideUtilsAndShowContentOverlay(){
        mUtilsLayout.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
    }

}
