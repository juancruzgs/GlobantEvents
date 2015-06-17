package com.globant.eventscorelib.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseAdapters.SocialNetworksAdapter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;


public class SharingIntent {

    private static void shareViaWhatsapp(Context context, String title, String description) {
        Intent sharingIntent = createSharingIntent(title, description, context);
        sharingIntent.setPackage(CoreConstants.WHATSAPP_PACKAGE);
        context.startActivity(sharingIntent);
    }

    private static void shareViaTwitter(Context context, String description) {
        Intent sharingIntent = createSharingIntent("", description, context);
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(sharingIntent, 0);
        for (final ResolveInfo app : activityList) {
            if (CoreConstants.TWITTER_PACKAGE.equals(app.activityInfo.name)) {
                final ActivityInfo activity = app.activityInfo;
                final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                sharingIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                sharingIntent.setComponent(name);
                context.startActivity(sharingIntent);
                break;
            }
            else {
                String tweetUrl =String.format(CoreConstants.TWITTER_URL,
                                urlEncode(context.getString(R.string.check_out_event) + "\n" + description));
                Intent sharingIntentViaWeb  = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));
                context.startActivity(sharingIntentViaWeb);
            }
        }
    }

    private static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, CoreConstants.CODIFICATION);
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(CoreConstants.CODIFICATION_EXCEPTION + s);
        }
    }

    private static void shareViaGmail(Context context, String title, String description) {
        Intent sharingIntent = createSharingIntent(title, description, context);
        sharingIntent.setClassName(CoreConstants.GMAIL_PACKAGE, CoreConstants.GMAIL_CLASS);
        context.startActivity(sharingIntent);
    }

    private static Intent createSharingIntent(String title, String description, Context context) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType(CoreConstants.INTENT_TYPE);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.check_out_event));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, title + "\n" + description);
        return sharingIntent;
    }

    private static void shareViaFacebook(Context context, String title, String description) {
        ShareDialog shareDialog = new ShareDialog((Activity) context);

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(title)
                    .setContentDescription(description)
                    .setContentUrl(Uri.parse(CoreConstants.APP_LINK_FACEBOOK))
                    .build();

            shareDialog.show(linkContent);
        }
    }

    public static void showList(final Context context, final String title, final String description) {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .titleColorRes(R.color.globant_green_dark)
                .title(R.string.dialog_share_via)
                .autoDismiss(true)
                .adapter(new SocialNetworksAdapter(context, context.getResources().getStringArray(R.array.social_networks)),
                        new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                switch (which) {
                                    case 0:
                                        shareViaFacebook(context, title, description);
                                        break;
                                    case 1:
                                        shareViaWhatsapp(context, title, description);
                                        break;
                                    case 2:
                                        shareViaTwitter(context, description);
                                        break;
                                    case 3:
                                        shareViaGmail(context, title, description);
                                        break;
                                }
                                dialog.cancel();
                            }
                        })
                .show();
    }
}
