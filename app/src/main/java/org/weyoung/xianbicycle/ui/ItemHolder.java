package org.weyoung.xianbicycle.ui;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.weyoung.xianbicycle.R;
import org.weyoung.xianbicycle.data.BicycleData;
import org.weyoung.xianbicycle.utils.BookmarkUtil;

import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ItemHolder {
    private Context context;

    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.status)
    TextView status;
    @Bind(R.id.location)
    TextView location;
    @Bind(R.id.bookmark)
    ImageView bookmark;
    @Bind(R.id.distance)
    TextView distance;

    public ItemHolder(View view) {
        context = view.getContext();
        ButterKnife.bind(this, view);
    }

    public void populate(final BicycleData data, List<String> bookmarkList) {
        name.setText(data.getSitename());
        status.setText(String.format(Locale.US, context.getString(R.string.result), getAvailableBike(data.getEmptynum(), data.getLocknum()), data.getEmptynum()));
        location.setText(data.getLocation());
        if (data.getDistance() != null) {
            distance.setVisibility(View.VISIBLE);
            distance.setText(String.format(Locale.US, "%dm", data.getDistance().intValue()));
        } else {
            distance.setVisibility(View.INVISIBLE);
        }
        boolean selected = bookmarkList.contains(data.getSiteid());
        setBookmark(selected);
        bookmark.setSelected(selected);
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                setBookmark(v.isSelected());
                if (v.isSelected()) {
                    BookmarkUtil.save(v.getContext(), data.getSiteid());
                } else {
                    BookmarkUtil.unsave(v.getContext(), data.getSiteid());
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
