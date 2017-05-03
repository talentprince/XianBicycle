/*
 * Copyright (C) 2015 A Weyoung App
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
package org.weyoung.xianbicycle.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.weyoung.xianbicycle.R;
import org.weyoung.xianbicycle.data.BicycleResult;
import org.weyoung.xianbicycle.utils.BookmarkUtil;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemHolder extends RecyclerView.ViewHolder{
    private final BookmarkUtil bookmarkUtil;
    private Context context;

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.location)
    TextView location;
    @BindView(R.id.bookmark)
    ImageView bookmark;
    @BindView(R.id.distance)
    TextView distance;

    public ItemHolder(View view, BookmarkUtil bookmarkUtil) {
        super(view);
        this.bookmarkUtil = bookmarkUtil;
        context = view.getContext();
        ButterKnife.bind(this, view);
    }

    public void populate(final BicycleResult data) {
        name.setText(data.getSitename());
        status.setText(String.format(Locale.US, context.getString(R.string.result), getAvailableBike(data.getEmptynum(), data.getLocknum()), data.getEmptynum()));
        location.setText(data.getLocation());
        if (data.getDistance() != null) {
            distance.setVisibility(View.VISIBLE);
            distance.setText(String.format(Locale.US, "%dm", data.getDistance().intValue()));
        } else {
            distance.setVisibility(View.INVISIBLE);
        }
        boolean selected = bookmarkUtil.contains(data.getSiteid());
        setBookmark(selected);
        bookmark.setSelected(selected);
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                setBookmark(v.isSelected());
                if (v.isSelected()) {
                    bookmarkUtil.addSiteId(data.getSiteid());
                } else {
                    bookmarkUtil.deleteSiteId(data.getSiteid());
                }
            }
        });
    }

    private void setBookmark(boolean selected) {
        bookmark.setColorFilter(selected ?
                bookmark.getContext().getResources().getColor(R.color.real_yellow) :
                bookmark.getContext().getResources().getColor(R.color.real_dark));
    }

    private String getAvailableBike(String empty, String lock) {
        return String.valueOf(Integer.valueOf(lock) - Integer.valueOf(empty));
    }
}
