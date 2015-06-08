package com.globant.eventmanager.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.globant.eventmanager.R;
import com.globant.eventmanager.activities.EventsManagerPagerActivity;
import com.globant.eventmanager.utils.QRCodeEncoder;
import com.globant.eventscorelib.baseActivities.BaseEventDetailPagerActivity;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.baseFragments.BaseEventDescriptionFragment;
import com.globant.eventscorelib.utils.CoreConstants;
import com.globant.eventscorelib.utils.CustomDateFormat;
import com.globant.eventscorelib.utils.PushNotifications;
import com.globant.eventscorelib.utils.SharingIntent;
import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class EventDescriptionManagerFragment extends BaseEventDescriptionFragment {

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_edit_event, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.events_action_update) {
            Intent intent = new Intent(getActivity(), EventsManagerPagerActivity.class);
            intent.putExtra(CoreConstants.FIELD_EVENTS,mEvent);
            EventsManagerPagerActivity.mEventAction = EventsManagerPagerActivity.ActionType.EDIT_EVENT;
            startActivityForResult(intent, CoreConstants.EDIT_EVENT_REQUEST);
            getActivity().overridePendingTransition(R.anim.top_in, R.anim.nothing);
        }
        else if(id == R.id.events_action_QR_code){
            QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(mEvent.getObjectID(), getActivity());
            Bitmap bitmap = null;
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
                new MaterialDialog.Builder(getActivity())
                        .title("Push Notifications")
                        .customView(R.layout.dialog_push_notification, false)
                        .positiveText("Send")
                        .negativeText("Cancel")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                                AppCompatSpinner spinner = (AppCompatSpinner) dialog.getCustomView().findViewById(R.id.spinner_users_filter);
                                AppCompatEditText editText = (AppCompatEditText) dialog.findViewById(R.id.editText_notification_text);
                                PushNotifications.sendNotification(getActivity(), editText.getText().toString(),
                                                                   spinner.getSelectedItem().toString(),
                                                                   mEvent.getObjectID());

                                Toast.makeText(dialog.getContext(), "Message sent to "+spinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                super.onNegative(dialog);
                            }
                        })
                        .show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            if(requestCode == CoreConstants.EDIT_EVENT_REQUEST){
                if (data != null && data.hasExtra(CoreConstants.SAVE_INSTANCE_EVENT_ACTION)){

                    int test = data.getIntExtra(CoreConstants.SAVE_INSTANCE_EVENT_ACTION, 0);
                    BaseService.ACTIONS action = BaseService.ACTIONS.values()[test];

                    switch (action){
                        case EVENT_CREATE:
                            getActivity().finish();
                            break;
                        case EVENT_UPDATE:
                            mEvent = BaseEventDetailPagerActivity.getInstance().getEvent();
                            loadEventDescription();
                            break;
                        case EVENT_DELETE:
                            getActivity().finish();
                            break;
                    }
                }
            }
        }
    }
}