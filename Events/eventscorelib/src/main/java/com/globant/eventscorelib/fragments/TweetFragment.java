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

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseComponents.BaseApplication;
import com.globant.eventscorelib.baseComponents.BaseFragment;
import com.globant.eventscorelib.utils.Logger;
import com.squareup.picasso.Picasso;

import twitter4j.User;


public class TweetFragment extends BaseFragment implements View.OnClickListener {

    private ImageView mUserPicture;
    private TextView mUsername;
    private TextView mUserFullName;
    private EditText mTweetText;
    private Button mTweetButton;
    private Button mLoginTwitterButton;
    private AsyncTask<String, Void, Boolean> mTweetPost;
    private AsyncTask<Void, Void, User> mLoadTweetUser;
    private AsyncTask<Void, Void, Boolean> mTwitterLoader;

    public TweetFragment() {
        // Required empty public constructor
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(com.globant.eventscorelib.R.layout.fragment_tweet, container, false);
        hideUtilsAndShowContentOverlay();
        wireUpViews(rootView);
        return rootView;
    }

    private void wireUpViews(View rootView) {
        mLoginTwitterButton = (Button) rootView.findViewById(R.id.button_login_twitter);
        mUserPicture = (ImageView) rootView.findViewById(R.id.imageView_user);
        mUsername = (TextView) rootView.findViewById(R.id.textView_username);
        mUserFullName = (TextView) rootView.findViewById(R.id.textView_user_full_name);
        mTweetText = (EditText) rootView.findViewById(R.id.textView_tweet);
        mTweetButton = (Button) rootView.findViewById(R.id.button_tweet);
        mTweetButton.setOnClickListener(this);
        mLoginTwitterButton.setOnClickListener(this);
    }

    @Override
    public String getFragmentTitle() {
        return "Tweet";
    }

    @Override
    public void onResume() {
        changeUserInformation();
        super.onResume();
    }

    private void changeUserInformation() {
        try {
            if (BaseApplication.getInstance().getSharedPreferencesManager()
                    .isAlreadyTwitterLogged()) {
               mLoginTwitterButton.setVisibility(View.GONE);
               mLoadTweetUser = new LoadTweetUser().execute();
            } else {
                mTweetButton.setEnabled(false);
                mTweetText.setEnabled(false);
                hideUtilsAndShowContentOverlay();
            }
        } catch (Exception e) {
            Logger.e("LOADING TWITTER", e);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_tweet) {
            String tweet = mTweetText.getText().toString();
            if (!tweet.equals("")) {
                InputMethodManager imm = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mTweetText.getWindowToken(), 0);
               mTweetPost = new TweetPost().execute(tweet);
            }
        } else if (v.getId() == R.id.button_login_twitter) {
            showProgressOverlay();
           mTwitterLoader = new TwitterLoader().execute();
        }
    }

    private class TweetPost extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            return BaseApplication.getInstance().getTwitterManager().publishPost(params[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                mTweetText.setText(getActivity().getString(R.string.general_hashtag));
                // TODO: get Event hashtag and change the strings
                Toast.makeText(getActivity(), getActivity().getString(R.string.tweet_success),Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), getActivity().getString(R.string.tweet_failure), Toast.LENGTH_SHORT).show();
            }
        }

    }

    public class LoadTweetUser extends AsyncTask<Void, Void, User> {

        @Override
        protected User doInBackground(Void... params) {
            User user = BaseApplication.getInstance().getCacheObjectsManager().user;
            try {
                if (user == null) {
                    user = BaseApplication.getInstance().getTwitterManager().getUser();
                    BaseApplication.getInstance().getCacheObjectsManager().user = user;
                }
            } catch (Exception e) {
                Logger.e("LOADING TWITTER", e);
            }
            if (user != null) {
                return user;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            if (user != null) {
                mUsername.setText(String.format(getString(R.string.twitter_username), user.getScreenName()));
                mUserFullName.setText(user.getName());
                if (user.getProfileImageURL() != null) {
                    Picasso.with(getActivity()).load(user.getOriginalProfileImageURL()).into(mUserPicture);
                }
                mTweetButton.setEnabled(true);
                mTweetText.setEnabled(true);
                mLoginTwitterButton.setVisibility(View.GONE);
                hideUtilsAndShowContentOverlay();
            }
        }
    }

    private class TwitterLoader extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            return BaseApplication.getInstance().getTwitterManager().loginToTwitter(getActivity(), null);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
               mLoadTweetUser = new LoadTweetUser().execute();
            }
        }
    }

/*    @Override
    public void onStop() {
        if (mTweetPost != null && mTweetPost.getStatus() == AsyncTask.Status.RUNNING) {
            mTweetPost.cancel(false);
        }
        else {
            if (mTwitterLoader != null && mTwitterLoader.getStatus() == AsyncTask.Status.RUNNING) {
                mTwitterLoader.cancel(false);
            }
            else {
                if (mLoadTweetUser != null && mLoadTweetUser.getStatus() == AsyncTask.Status.RUNNING) {
                    mLoadTweetUser.cancel(false);
                }
            }
        }
        super.onStop();
    }*/
}



