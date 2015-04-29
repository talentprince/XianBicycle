package org.weyoung.xianbicycle.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import org.weyoung.xianbicycle.R;
import org.weyoung.xianbicycle.data.BicycleData;

import java.util.List;

public class DataAdapter extends ArrayAdapter<BicycleData> {

    private final Context context;

    public DataAdapter(Context context, List<BicycleData> list) {
        super(context, 0, list);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BicycleData data = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
            convertView.setTag(new ItemHolder(convertView));
        }
        ItemHolder itemHolder = (ItemHolder) convertView.getTag();
        itemHolder.populate(data);
        return convertView;
    }
}