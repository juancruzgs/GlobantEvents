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

package com.globant.eventscorelib;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BaseEventsListAdapter extends RecyclerView.Adapter<BaseEventsListViewHolder> {

    private String[] mDataSet;

    public BaseEventsListAdapter(String[] dataSet) {
        mDataSet = dataSet;
    }

    @Override
    public BaseEventsListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.event_card_row_item, viewGroup, false);
        return new BaseEventsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseEventsListViewHolder holder, int position) {
        holder.getTextView().setText(mDataSet[position]);
    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }

}
