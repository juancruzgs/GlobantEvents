package com.globant.eventscorelib;


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

import com.globant.eventscorelib.baseComponents.BaseApplication;
import com.globant.eventscorelib.baseComponents.BaseFragment;
import com.globant.eventscorelib.utils.Logger;
import com.squareup.picasso.Picasso;

import twitter4j.User;


public class BaseTweetFragment extends BaseFragment implements View.OnClickListener {

    private ImageView picture;
    private TextView usernameTextView;
    private TextView nameTextView;
    private EditText tweetTextField;
    private Button tweetBtn;
    private Button loginTwitterBtn;
    private AsyncTask<String, Void, Boolean> mTweetPost;
    private AsyncTask<Void, Void, User> mLoadTweetUser;
    private AsyncTask<Void, Void, Boolean> mTwitterLoader;

    public BaseTweetFragment() {
        // Required empty public constructor
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tweet, container, false);
        hideUtilsAndShowContentOverlay();
        loginTwitterBtn = (Button) rootView.findViewById(R.id.loginTwitterBtn);
        picture = (ImageView) rootView.findViewById(R.id.picture);
        usernameTextView = (TextView) rootView.findViewById(R.id.usernameTextView);
        nameTextView = (TextView) rootView.findViewById(R.id.nameTextView);
        tweetTextField = (EditText) rootView.findViewById(R.id.tweetTextField);
        tweetBtn = (Button) rootView.findViewById(R.id.tweetBtn);
        tweetBtn.setOnClickListener(this);
        loginTwitterBtn.setOnClickListener(this);
        return rootView;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void onResume() {
        try {
            if (BaseApplication.getInstance().getSharedPreferencesManager()
                    .isAlreadyTwitterLogged()) {
                loginTwitterBtn.setVisibility(View.GONE);
               mLoadTweetUser = new LoadTweetUser().execute();
            } else {
                tweetBtn.setEnabled(false);
                tweetTextField.setEnabled(false);
                hideUtilsAndShowContentOverlay();
            }
        } catch (Exception e) {
            Logger.e("LOADING TWITTER", e);
        }
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tweetBtn) {
            String tweet = tweetTextField.getText().toString();
            if (!tweet.equals("")) {
                InputMethodManager imm = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(tweetTextField.getWindowToken(), 0);
               mTweetPost = new TweetPost().execute(tweet);
            }
        } else if (v.getId() == R.id.loginTwitterBtn) {
           mTwitterLoader = new TwitterLoader().execute();
        }
    }

    private class TweetPost extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            return BaseApplication.getInstance().getTwitterManager()
                    .publishPost(params[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                tweetTextField.setText(getActivity().getString(
                        R.string.basic_tweet)); // TODO: get Event hashtag and change the strings
                Toast.makeText(getActivity(),
                        getActivity().getString(R.string.tweet_success),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(),
                        getActivity().getString(R.string.tweet_failure),
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    private class LoadTweetUser extends AsyncTask<Void, Void, User> {

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
                usernameTextView.setText(user.getScreenName());
                nameTextView.setText(user.getName());
                if (user.getProfileImageURL() != null) {
                    Picasso.with(getActivity()).load(user.getOriginalProfileImageURL()).into(picture);
                }
                tweetBtn.setEnabled(true);
                tweetTextField.setEnabled(true);
                loginTwitterBtn.setVisibility(View.GONE);
                hideUtilsAndShowContentOverlay();
            } else {
                showErrorOverlay();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressOverlay();
        }

    }

    private class TwitterLoader extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            return BaseApplication.getInstance().getTwitterManager()
                    .loginToTwitter(getActivity(), null);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            hideUtilsAndShowContentOverlay();
            if (result) {

                new LoadTweetUser().execute();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressOverlay();
        }
    }


    @Override
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
    }
}



