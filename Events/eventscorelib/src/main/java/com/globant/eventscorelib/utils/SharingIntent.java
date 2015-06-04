package com.globant.eventscorelib.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseAdapters.SocialNetworksAdapter;


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

    public static void showList(final Context context) {
        new MaterialDialog.Builder(context)
                .titleColorRes(R.color.globant_green_dark)
                .title(R.string.dialog_share_via)
                .adapter(new SocialNetworksAdapter(context, context.getResources().getStringArray(R.array.social_networks)),
                        new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                Toast.makeText(context, "Clicked item " + which, Toast.LENGTH_SHORT).show();
                            }
                        })
                .show();
    }
}
