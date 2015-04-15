package com.globant.eventscorelib.managers;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import com.globant.eventscorelib.baseComponents.BaseApplication;
import com.globant.eventscorelib.utils.CoreConstants;

import java.util.List;

public class TwitterManager {

    private RequestToken requestToken;

    public TwitterManager() {

    }

    public interface TwitterLoginListener {
        public void onLogin(boolean status);
    }


    public boolean getLoginResponse(Uri uri) {
        if (uri != null && uri.toString().startsWith(CoreConstants.TWITTER_CALLBACK_URL)) {
            String verifier = uri.getQueryParameter(CoreConstants.URL_TWITTER_OAUTH_VERIFIER);
            AccessToken accessToken;
            try {
                Twitter twitter = getTwitter(false);
                accessToken = twitter.getOAuthAccessToken(requestToken,
                        verifier);
                BaseApplication
                        .getInstance()
                        .getSharedPreferencesManager()
                        .setTwitterStatusResponse(accessToken.getToken(),
                                accessToken.getTokenSecret());
                long userID = accessToken.getUserId();
                return true;
            } catch (TwitterException e) {
                   return false;
            }
        } else {
            return false;
        }
    }

    public User getUser() {
        try {
            Twitter twitter = getTwitter();
            long userID = getAccessToken().getUserId();
            return twitter.showUser(userID);
        } catch (TwitterException e) {
            return null;
        }
    }

    public boolean publishPost(String post) {
        try {
            Twitter twitter = getTwitter();
            if (twitter != null) {
                twitter4j.Status status = getTwitter().updateStatus(post);
                return true;
            } else {
                return false;
            }
        } catch (TwitterException e) {
            return false;
        }

    }

    public boolean loginToTwitter(Context ctx, TwitterLoginListener listener) {
        try {
            Twitter twitter = getTwitterNoTokens();
            if (twitter != null) {
                if (!BaseApplication.getInstance().getSharedPreferencesManager()
                        .isAlreadyTwitterLogged()) {
                    requestToken = twitter.getOAuthRequestToken(CoreConstants.TWITTER_CALLBACK_URL);
                    ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                            .parse(requestToken.getAuthenticationURL())));

                }
                if (listener != null) {
                    listener.onLogin(true);
                }
                return true;
            } else {
                return false;
            }
        } catch (TwitterException e) {
            if (listener != null) {
                listener.onLogin(false);
            }
            return false;
        }
    }

    public List<Status> getTweetList() {
        Query query = new Query("#FlipThinking"); // TODO change the hashtag
        query.setCount(50);
        try {
            Twitter twitter = getTwitter(false);
            if (twitter != null) {
                QueryResult qr = twitter.search(query);
                return qr.getTweets();
            } else {
                return null;
            }

        } catch (TwitterException e) {
             return null;
        }
    }

    public Twitter getTwitter() {
        return getTwitter(true);
    }

    public Twitter getTwitterNoTokens() {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(CoreConstants.TWITTER_CONSUMER_KEY);
        builder.setOAuthConsumerSecret(CoreConstants.TWITTER_CONSUMER_SECRET);
        Configuration configuration = builder.build();
        return getTwitter(configuration);
    }

    public Twitter getTwitter(boolean useToken) {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(CoreConstants.TWITTER_CONSUMER_KEY);
        builder.setOAuthConsumerSecret(CoreConstants.TWITTER_CONSUMER_SECRET);
        if (!useToken) {
            builder.setOAuthAccessToken(CoreConstants.TWITTER_ACCESS_TOKEN);
            builder.setOAuthAccessTokenSecret(CoreConstants.TWITTER_ACCESS_TOKEN_SECRET);
        }
        Configuration configuration = builder.build();
        return getTwitter(configuration);
    }

    private Twitter getTwitter(Configuration configuration) {
        TwitterFactory factory = new TwitterFactory(configuration);
        try {
            return factory.getInstance(getAccessToken());
        } catch (Exception e) {
            return factory.getInstance();
        }
    }

    public AccessToken getAccessToken() {
        String access_token = BaseApplication.getInstance()
                .getSharedPreferencesManager().getAccessToken();
        String access_token_secret = BaseApplication.getInstance()
                .getSharedPreferencesManager().getAccessTokenSecret();
        return new AccessToken(access_token, access_token_secret);
    }

    public static String HashtagString(String allString) {
        String[] arrayStrings = allString.split(" ");
        String result = "";
        for (String hashtagString : arrayStrings) {
            result += "#" + hashtagString + " ";
        }
        return result;
    }


}