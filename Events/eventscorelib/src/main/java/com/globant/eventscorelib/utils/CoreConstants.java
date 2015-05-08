package com.globant.eventscorelib.utils;

/**
 * Created by ignaciopena on 4/1/15.
 */
public class CoreConstants {

    public static int ZERO = 0;

    //Base Activity
    public static final String INTENT_FILTER_CONNECTIVITY = "android.net.conn.CONNECTIVITY_CHANGE";

    //Parse
    public static final String APPLICATION_ID = "XFRKSroVgyKznK5gYTqpQoL8fnNZ1zhSwBVc9tJb";
    public static final String CLIENT_KEY = "D00WzyZtzJ3aeNRT93sSUYflBRAR3empdd6YgcLd";

    public static final String EVENTS_TABLE = "Events";
    public static final String EVENTS_TO_SUBSCRIBERS_TABLE = "EventsToSubscribers";
    public static final String SPEAKERS_TABLE = "Speakers";
    public static final String SUBSCRIBERS_TABLE = "Subscribers";

    public static final String FIELD_TITLE = "title";
    public static final String FIELD_SHORT_DESCRIPTION = "short_description";
    public static final String FIELD_FULL_DESCRIPTION = "full_description";
    public static final String FIELD_ADDITIONAL_INFO = "additional_info";
    public static final String FIELD_ADDRESS = "address";
    public static final String FIELD_CITY = "city";
    public static final String FIELD_COUNTRY = "country";
    public static final String FIELD_CATEGORY = "category";
    public static final String FIELD_SPEAKERS = "speakers";
    public static final String FIELD_START_DATE = "start_date";
    public static final String FIELD_END_DATE = "end_date";
    public static final String FIELD_EVENT_LOGO = "event_logo";
    public static final String FIELD_ICON = "icon";
    public static final String FIELD_MAP_COORDINATES = "map_coordinates";
    public static final String FIELD_PUBLIC = "public";
    public static final String FIELD_QR_CODE = "qr_code";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_BIOGRAPHY = "biography";
    public static final String FIELD_PICTURE = "picture";
    public static final String FIELD_ENGLISH = "english";
    public static final String FIELD_GLOBER = "glober";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_OCCUPATION = "occupation";
    public static final String FIELD_PHONE = "phone";
    public static final String FIELD_TWITTER_USER = "twitter_user";
    public static final String FIELD_LANGUAGE = "language";
    public static final String FIELD_HASHTAG = "hashtag";
    public static final String FIELD_ACCEPTED = "accepted";
    public static final String FIELD_CHECK_IN = "check_in";
    public static final String FIELD_EVENTS = "events";
    public static final String FIELD_SUBSCRIBERS = "subscribers";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_LAST_NAME = "last_name";

    //QR Encoder
    public static final int WHITE = 0xFFFFFFFF;
    public static final int BLACK = 0xFF000000;
    public static final String ENCODING = "UTF-8";

    //QR Scanner
    public static final String INTENT_SCAN = "com.google.zxing.client.android.SCAN";
    public static final String SCAN_RESULT = "SCAN_RESULT";
    public static final int REQUEST_CODE_SCAN = 1;

    //Logger
    public static final String LOG_TAG = "GlobantEventsApp";

    //Map
    public static final int MAP_CAMERA_ZOOM = 17;
    public static final int MAP_CAMERA_ANIMATION_DURATION = 2000;
    public static final int MAX_GEOCODER_RESULTS = 1;
    public static final String MAP_ADDRESS_INTENT = "address";
    public static final String MAP_MARKER_POSITION_INTENT = "markerPosition";
    public static final String MAP_CAMERA_POSITION_INTENT = "cameraPosition";
    public static final String MAP_SEARCH_QUERY_INTENT = "searchQuery";
    public static final int DOUBLE_TAP_TIME_INTERVAL = 2000;

    // Twitter
    public static final String TWITTER_CONSUMER_KEY = "c3nMUttSEcZzQRR4TekRIA3Em";
    public static final String TWITTER_CONSUMER_SECRET = "lLBZ0E6wBF5vf9GasZnqqqOCmPzwZ58ruei14yU579RFhtED6P";
    public static final String TWITTER_ACCESS_TOKEN = "152693997-IEoWMmAkYDX3f1hO5pxWBKhbgqNh0V09wOFPkG8V";
    public static final String TWITTER_ACCESS_TOKEN_SECRET = "r2C4SZl8MCeF5vXYyFF9VumUvYYJ1kkk2FzM6QVW3Cxjb";
    public static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";
    public static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    public static final String TWITTER_IS_LOGGED_IN = "is_logged_in";
    public static final String TWITTER_PREF_KEY_OAUTH_TOKEN = "oauth_token";
    public static final String TWITTER_PREF_KEY_OAUTH_SECRET = "oauth_token_secret";

    // RecyclerView
    public static final String KEY_LAYOUT_MANAGER = "layoutManager";
    public static final String KEY_LAYOUT_PLACEHOLDER = "placeholder666";

    //SubscriberProfile
    public static final String SHARED_PREF_IMG = "img.jpg";
    public static final String IMAGE_CAPTURE = "android.media.action.IMAGE_CAPTURE";
    public static final String IMAGE_CROP = "com.android.camera.action.CROP";
    public static final String URI_NAME = "image/*";
    public static final String EXTRA_CROP = "crop";
    public static final String EXTRA_TRUE = "true";
    public static final String EXTRA_ASPECTX = "aspectX";
    public static final String EXTRA_ASPECTY = "aspectY";
    public static final String EXTRA_OUTPUTX = "outputX";
    public static final String EXTRA_OUTPUTY= "outputY";
    public static final String EXTRA_RETURN_DATA= "return-data";
    public static final String DATA= "data";
    public static final String SHARED_PREF_DIR = "/shared_prefs/";
    public static final String SHARED_PREF_ROOT = "/data/data/";
    public static final String SHARED_PREF_FILE = "_preferences.xml";
    public static final String DONE_CLICKED="done_clicked";
    public static final String PHOTO_ROTATE="photoRotate";
    public static final String SPEAKER_SELECTED ="speaker";
    public static final String PHOTO_TAKEN ="photo_taken";

    //Base Activity
    public static final String ACTIVITY_TITLE_INTENT = "ActivityTitle";

    //Event Descripion
    public static final float MAX_TEXT_SCALE_DELTA = 0.3f;

    //Pager
    public static final String CURRENT_FRAGMENT_INTENT = "currentFragmentPosition";
}
