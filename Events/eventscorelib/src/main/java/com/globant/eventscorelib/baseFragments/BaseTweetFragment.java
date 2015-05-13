package com.globant.eventscorelib.baseFragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.globant.eventscorelib.baseActivities.BaseEventDetailPagerActivity;
import com.globant.eventscorelib.baseActivities.BaseTweetActivity;
import com.globant.eventscorelib.utils.CoreConstants;
import com.globant.eventscorelib.utils.CropCircleTransformation;
import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseComponents.BaseApplication;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.utils.Logger;
import com.squareup.picasso.Picasso;

import java.util.Date;

import twitter4j.User;


public class BaseTweetFragment extends BaseFragment implements BaseService.ActionListener, BaseTweetActivity.NewFragmentIntent {

    private ImageView mUserPicture;
    private TextView mUsername;
    private TextView mUserFullName;
    private EditText mTweetText;
    private Button mTweetButton;
    private CropCircleTransformation mCircleTransformation;

    private String mBindingKey;

    public BaseTweetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onNewIntent(Intent intent) {
        Uri uri = intent.getData();
        if (uri != null) {
            mService.executeAction(BaseService.ACTIONS.TWITTER_LOADER_RESPONSE, getBindingKey(), uri);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBindingKey = this.getClass().getSimpleName();// + new Date().toString();
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(com.globant.eventscorelib.R.layout.fragment_tweet, container, false);
        hideUtilsAndShowContentOverlay();
        mCircleTransformation = new CropCircleTransformation(getActivity());
        wireUpViews(rootView);
        return rootView;
    }

    //    @Override
//    public String getTitle() {
//        return getString(R.string.title_fragment_tweet);
//    }
    @Override
    public String getTitle() {
        return "";
    }

    @Override
    public void setService(BaseService service) {
        super.setService(service);
        mTweetText.clearFocus();
        User user = ((BaseTweetActivity) getActivity()).getTwitterUser();
        if (user == null) {
            mService.executeAction(BaseService.ACTIONS.GET_TWITTER_USER, getBindingKey());
        } else {
            setUserInformation(user);
        }
        hideUtilsAndShowContentOverlay();
    }

    private void wireUpViews(View rootView) {
        mUserPicture = (ImageView) rootView.findViewById(R.id.imageView_user);
        Picasso.with(getActivity()).load(R.mipmap.placeholder).transform(mCircleTransformation).into(mUserPicture);
        mUsername = (TextView) rootView.findViewById(R.id.textView_username);
        mUserFullName = (TextView) rootView.findViewById(R.id.textView_user_full_name);
        mTweetText = (EditText) rootView.findViewById(R.id.textView_tweet);
        //mTweetText.setText(getString(R.string.general_hashtag) + event.hashtag); TODO put the event hashtag when the activity starts
        mTweetButton = (Button) rootView.findViewById(R.id.button_tweet);
        mTweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((Button)v).getText().equals(getString(R.string.button_tweet))) {
                    String tweet = mTweetText.getText().toString();
                    if (!tweet.isEmpty()) {
                        hideKeyboard();
                        mService.executeAction(BaseService.ACTIONS.TWEET_POST, getBindingKey(), tweet);
                    }
                } else {
                    mService.executeAction(BaseService.ACTIONS.TWITTER_LOADER, getBindingKey());
                }
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mTweetText.getWindowToken(), CoreConstants.ZERO);
    }

    public void changeUserInformation(User user) {
        try {
            if (BaseApplication.getInstance().getSharedPreferencesController()
                    .isAlreadyTwitterLogged()) {
                setUserInformation(user);
            } else {
                mTweetText.setEnabled(false);
            }
        } catch (Exception e) {
            Logger.e("LOADING TWITTER", e);
        }
    }

    private void setUserInformation(User user) {
        mUsername.setText(String.format(getString(R.string.twitter_username), user.getScreenName()));
        mUserFullName.setText(user.getName());
        if (user.getProfileImageURL() != null) {
            Picasso.with(getActivity()).load(user.getOriginalProfileImageURL()).transform(mCircleTransformation).into(mUserPicture);
        }
        mTweetText.setEnabled(true);
        mTweetButton.setText(getString(R.string.button_tweet));
    }

    @Override
    public BaseService.ActionListener getActionListener() {
        return this;
    }

    @Override
    public Activity getBindingActivity() {
        return getActivity();
    }

    @Override
    public String getBindingKey() {
        return mBindingKey;
    }

    @Override
    public void onStartAction(BaseService.ACTIONS theAction) {
        showProgressOverlay();
    }

    @Override
    public void onFinishAction(BaseService.ACTIONS theAction, Object result) {
        switch (theAction) {
            case GET_TWITTER_USER:
                User user = (User) result;
                if (user != null) {
                    ((BaseTweetActivity) getActivity()).setTwitterUser(user);
                    changeUserInformation(user);
                }
                hideUtilsAndShowContentOverlay();
                break;
            case TWEET_POST:
                if ((Boolean) result) {
                    mTweetText.setText(getActivity().getString(R.string.general_hashtag));
                    // TODO: get Event hashtag and change the strings
                    Toast.makeText(getActivity(), getActivity().getString(R.string.tweet_success), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.tweet_failure), Toast.LENGTH_SHORT).show();
                }
                hideUtilsAndShowContentOverlay();
                break;
            case TWITTER_LOADER_RESPONSE:
                if ((Boolean) result) {
                    mService.executeAction(BaseService.ACTIONS.GET_TWITTER_USER, getBindingKey());
                }
                break;
        }
    }

    @Override
    public void onFailAction(BaseService.ACTIONS theAction, Exception e) {
        showErrorOverlay();
    }
}