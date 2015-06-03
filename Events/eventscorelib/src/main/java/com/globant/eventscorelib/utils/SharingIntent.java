package com.globant.eventscorelib.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.globant.eventscorelib.R;


public class SharingIntent {

   private void shareViaTwitter(Context context, String title, String description) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, (String) "Check out this event!\n");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, title + "\n" + description);
        sharingIntent.setPackage("com.whatsapp");
        context.startActivity(sharingIntent);
    }

    private void shareViaFacebook(Fragment fragment, String title, String description, Uri urlImage) {
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

    public static void showList(Context context) {
new MaterialDialog.Builder(context)
        .titleColorRes(R.color.globant_green_dark)
        .dividerColorRes(R.color.grey_light)
        .title(R.string.dialog_share_via)
        .items(R.array.social_networks)
        .itemsCallback(new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
            }
                })
                .show();
    }
}
