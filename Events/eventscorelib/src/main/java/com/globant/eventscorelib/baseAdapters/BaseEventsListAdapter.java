/*
* Copyright (C) 2014 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.globant.eventscorelib.baseAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.domainObjects.Speaker;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BaseEventsListAdapter extends RecyclerView.Adapter<BaseEventsListViewHolder> {

    private Context mContext;
    private List<Event> mEventList;

    public BaseEventsListAdapter(List<Event> eventList, Context context) {
        mEventList = eventList;
        mContext = context;
    }

    @Override
    public BaseEventsListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.event_card_row_item, viewGroup, false);
        return new BaseEventsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseEventsListViewHolder holder, int position) {
        byte[] eventLogo = mEventList.get(position).getEventLogo();
        if (eventLogo == null) {
           Picasso.with(mContext).load(R.mipmap.placeholder).into(holder.getImageEvent());
        } else {
            Picasso.with(mContext).load(getImageUri(eventLogo)).into(holder.getImageEvent());
        }
        holder.getEventTitle().setText(mEventList.get(position).getTitle());
        holder.getEventDate().setText(getDate(mEventList.get(position).getStartDate()));
        holder.getLocationEvent().setText(mEventList.get(position).getCity() + ", " + mEventList.get(position).getCountry());
        holder.getShortDescriptionEvent().setText(mEventList.get(position).getShortDescription());
    }

    public String getDate(Date startDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        SimpleDateFormat date = new SimpleDateFormat("MM/dd/yyyy");
        return calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE)
                + " " + date.format(startDate);
    }

    public Uri getImageUri(byte[] eventLogo) {
        Bitmap eventImage;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        eventImage = BitmapFactory.decodeByteArray(eventLogo, 0, eventLogo.length, options);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        eventImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), eventImage, "Title", null);
        return Uri.parse(path);
    }


    @Override
    public int getItemCount() {
        return mEventList.size();
    }

}
