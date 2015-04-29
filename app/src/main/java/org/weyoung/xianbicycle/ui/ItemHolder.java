package org.weyoung.xianbicycle.ui;

import android.view.View;
import android.widget.TextView;

import org.weyoung.xianbicycle.R;
import org.weyoung.xianbicycle.data.BicycleData;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ItemHolder {
    @InjectView(R.id.name)
    TextView name;
    @InjectView(R.id.status)
    TextView status;
    @InjectView(R.id.location)
    TextView location;

    public ItemHolder(View view) {
        ButterKnife.inject(this, view);
    }

    public void populate(BicycleData data) {
        name.setText(data.getSitename());
        status.setText(String.format(Locale.US, "(%s/%s)", data.getEmptynum(), data.getLocknum()));
        location.setText(data.getLocation());
    }
}
