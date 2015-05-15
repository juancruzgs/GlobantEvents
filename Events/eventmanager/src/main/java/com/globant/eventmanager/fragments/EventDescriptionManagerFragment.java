package com.globant.eventmanager.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.globant.eventmanager.R;
import com.globant.eventmanager.activities.EventsManagerPagerActivity;
import com.globant.eventmanager.utils.QRCodeEncoder;
import com.globant.eventscorelib.baseFragments.BaseEventDescriptionFragment;
import com.google.zxing.WriterException;

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
            EventsManagerPagerActivity.mEvent = mEvent;
            EventsFragment.mEventAction = EventsFragment.ActionType.EDIT_EVENT;
            startActivity(intent);

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
        }
        return super.onOptionsItemSelected(item);

    }

    private void loadPhoto(ImageView imageView) {

        FragmentActivity context = getActivity();

        AlertDialog.Builder imageDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(FragmentActivity.LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.full_image_dialog, (ViewGroup) context.findViewById(R.id.layout_root));
        ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
        TextView textView = (TextView) layout.findViewById(R.id.custom_fullimage_placename);
        textView.setText(mEvent.getTitle());
        image.setImageDrawable(imageView.getDrawable());
        imageDialog.setView(layout);
        imageDialog.setPositiveButton(getResources().getString(R.string.action_done), new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });
        imageDialog.create();
        imageDialog.show();
    }
}