package org.weyoung.xianbicycle.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.weyoung.xianbicycle.R;
import org.weyoung.xianbicycle.data.BicycleData;

import java.util.List;

public class DataAdapter extends ArrayAdapter<BicycleData> {

    private final Context context;
    private List<String> bookmarkList;

    public DataAdapter(Context context, List<BicycleData> list, List<String> bookmarkList) {
        super(context, 0, list);
        this.context = context;
        this.bookmarkList = bookmarkList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BicycleData data = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
            convertView.setTag(new ItemHolder(convertView));
        }
        ItemHolder itemHolder = (ItemHolder) convertView.getTag();
        itemHolder.populate(data, bookmarkList);
        return convertView;
    }
}