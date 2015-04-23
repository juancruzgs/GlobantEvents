package com.globant.eventscorelib.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
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

import com.globant.eventscorelib.CropCircleTransformation;
import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseComponents.BaseApplication;
import com.globant.eventscorelib.baseComponents.BaseFragment;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.utils.Logger;
import com.squareup.picasso.Picasso;

import twitter4j.User;


public class TweetFragment extends BaseFragment implements View.OnClickListener, BaseService.ActionListener {

    private ImageView mUserPicture;
    private TextView mUsername;
    private TextView mUserFullName;
    private EditText mTweetText;
    private Button mTweetButton;
    private Button mLoginTwitterButton;
    CropCircleTransformation mCircleTransformation;

    public TweetFragment() {
        // Required empty public constructor
    }

    @Override
    public Activity getBindingActivity() {
        return getActivity();
    }

    @Override
    public Object getBindingKey() {
        return null;
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
                BaseApplication.getInstance().getCacheObjectsManager().user = user;
                if (user != null) {
                    changeUserInformation(user);
                }
                break;
            case TWITTER_LOADER:
                if ((Boolean) result) {
                    mService.executeAction(BaseService.ACTIONS.GET_TWITTER_USER, null);
                }
                break;
            case TWEET_POST:
                if ((Boolean) result) {
                    mTweetText.setText(getActivity().getString(R.string.general_hashtag));
                    // TODO: get Event hashtag and change the strings
                    Toast.makeText(getActivity(), getActivity().getString(R.string.tweet_success), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.tweet_failure), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        hideUtilsAndShowContentOverlay();
    }

    @Override
    public void onFailAction(BaseService.ACTIONS theAction, Exception e) {
        showErrorOverlay();
    }

    @Override
    public BaseService.ActionListener getActionListener() {
        return TweetFragment.this;
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(com.globant.eventscorelib.R.layout.fragment_tweet, container, false);
        mCircleTransformation = new CropCircleTransformation(getActivity());
        hideUtilsAndShowContentOverlay();
        wireUpViews(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        User user = BaseApplication.getInstance().getCacheObjectsManager().user;
        if (user != null) {
          setUserInformation(user);
        }

    }

    private void wireUpViews(View rootView) {
        mLoginTwitterButton = (Button) rootView.findViewById(R.id.button_login_twitter);
        mUserPicture = (ImageView) rootView.findViewById(R.id.imageView_user);
        Picasso.with(getActivity()).load(R.mipmap.placeholder).transform(mCircleTransformation).into(mUserPicture);
        mUsername = (TextView) rootView.findViewById(R.id.textView_username);
        mUserFullName = (TextView) rootView.findViewById(R.id.textView_user_full_name);
        mTweetText = (EditText) rootView.findViewById(R.id.textView_tweet);
        mTweetButton = (Button) rootView.findViewById(R.id.button_tweet);
        mTweetButton.setOnClickListener(this);
        mLoginTwitterButton.setOnClickListener(this);
    }

    @Override
    public String getTitle() {
        return "Tweet";
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_tweet) {
            String tweet = mTweetText.getText().toString();
            if (!tweet.equals("")) {
                InputMethodManager imm = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mTweetText.getWindowToken(), 0);
                mService.executeAction(BaseService.ACTIONS.TWEET_POST, tweet);
            }
        } else if (v.getId() == R.id.button_login_twitter) {
            mService.executeAction(BaseService.ACTIONS.TWITTER_LOADER, null);
        }
    }


    public void changeUserInformation(User user) {
        try {
            if (BaseApplication.getInstance().getSharedPreferencesManager()
                    .isAlreadyTwitterLogged()) {
                mLoginTwitterButton.setVisibility(View.GONE);
                setUserInformation(user);
            } else {
                mTweetButton.setEnabled(false);
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
        mTweetButton.setEnabled(true);
        mTweetText.setEnabled(true);
        mLoginTwitterButton.setVisibility(View.GONE);
    }
}





//    private class TweetPost extends AsyncTask<String, Void, Boolean> {
//
//        @Override
//        protected Boolean doInBackground(String... params) {
//            return BaseApplication.getInstance().getTwitterManager().publishPost(params[0]);
//        }
//
//        @Override
//        protected void onPostExecute(Boolean result) {
//            super.onPostExecute(result);
//            if (result) {
//                mTweetText.setText(getActivity().getString(R.string.general_hashtag));
//                // TODO: get Event hashtag and change the strings
//                Toast.makeText(getActivity(), getActivity().getString(R.string.tweet_success), Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(getActivity(), getActivity().getString(R.string.tweet_failure), Toast.LENGTH_SHORT).show();
//            }
//        }
//
//    }
