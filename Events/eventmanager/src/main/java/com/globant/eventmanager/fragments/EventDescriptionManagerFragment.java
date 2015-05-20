package com.globant.eventmanager.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.globant.eventmanager.R;
import com.globant.eventmanager.activities.EventsManagerPagerActivity;
import com.globant.eventmanager.utils.QRCodeEncoder;
import android.content.Intent;
import android.view.MenuItem;

import com.globant.eventmanager.activities.PushNotificationActivity;
import com.globant.eventscorelib.baseActivities.BaseEventDetailPagerActivity;
import com.globant.eventscorelib.baseComponents.BaseApplication;
import com.globant.eventscorelib.baseFragments.BaseEventDescriptionFragment;
import com.globant.eventscorelib.utils.CoreConstants;
import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class EventDescriptionManagerFragment extends BaseEventDescriptionFragment {

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_qr_code_event, menu);
    }

    @Override
    protected void initializeViewParameters() {
        super.initializeViewParameters();
        ((com.software.shell.fab.ActionButton)mFab).setImageResource(R.mipmap.ic_fab_edit);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EventsManagerPagerActivity.class);
                intent.putExtra(CoreConstants.FIELD_EVENTS, mEvent);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.events_action_QR_code){
            QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(mEvent.getObjectID(), getActivity());
            Bitmap bitmap;
            try {
                bitmap = qrCodeEncoder.encodeAsBitmap();
                ImageView myImage = new ImageView(getActivity());
                myImage.setImageBitmap(bitmap);
                loadPhoto(myImage);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        } else {
            if (id == com.globant.eventscorelib.R.id.action_notifications){
                Intent intentNotifications = new Intent(getActivity(), PushNotificationActivity.class);
                intentNotifications.putExtra(PushNotificationFragment.SOURCE_TAG,
                        BaseEventDetailPagerActivity.getInstance().getEvent().getObjectID());
                startActivity(intentNotifications);
                return  true;
            }
        }
        return super.onOptionsItemSelected(item);

    }

    private void loadPhoto(ImageView imageView) {

        FragmentActivity context = getActivity();

        AlertDialog.Builder imageDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(FragmentActivity.LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.full_image_dialog, (ViewGroup) context.findViewById(R.id.layout_root));
        final ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
        image.setImageDrawable(imageView.getDrawable());
        imageDialog.setView(layout);
        imageDialog.setIcon(R.mipmap.ic_qrcode);
        imageDialog.setTitle(mEvent.getTitle());
        imageDialog.setNeutralButton(R.string.share_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri bmpUri = getLocalBitmapUri(image);
                if (bmpUri != null) {
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                    shareIntent.setType("image/*");
                    startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.share_button)));
                } else {
                    showErrorOverlay();
                }
            }
        });
        imageDialog.setPositiveButton(getResources().getString(R.string.action_done), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });
        imageDialog.create();
        imageDialog.show();
    }

    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
}