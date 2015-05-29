package com.globant.eventscorelib.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;


public class SharingIntent {

    public static void shareViaTwitter(Context context, String title, String description) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, (String) "Check out this event!\n");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, title + "\n" + description);
        sharingIntent.setPackage("com.whatsapp");
        context.startActivity(sharingIntent);
    }

    public static void shareViaFacebook(Fragment fragment, String title, String description, Uri urlImage) {
        ShareDialog shareDialog = new ShareDialog(fragment);

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(title)
                    .setImageUrl(urlImage)
                    .setContentDescription(description)
                    .setContentUrl(Uri.parse("https://fb.me/383624728506392"))
                    .build();

            shareDialog.show(linkContent);
        }
    }
}
