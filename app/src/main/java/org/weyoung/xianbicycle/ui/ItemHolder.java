package org.weyoung.xianbicycle.ui;

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
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.status)
    TextView status;
    @Bind(R.id.location)
    TextView location;
    @Bind(R.id.bookmark)
    ImageView bookmark;

    public ItemHolder(View view) {
        ButterKnife.bind(this, view);
    }

    public void populate(final BicycleData data, List<String> bookmarkList) {
        name.setText(data.getSitename());
        status.setText(String.format(Locale.US, "(%s/%s)", data.getEmptynum(), data.getLocknum()));
        location.setText(data.getLocation());
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
}
