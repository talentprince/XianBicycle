package org.weyoung.xianbicycle.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.weyoung.xianbicycle.R;
import org.weyoung.xianbicycle.data.BicycleResult;
import org.weyoung.xianbicycle.utils.BookmarkUtil;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final BookmarkUtil bookmarkUtil;
    private ItemClickListener listener;
    private List<BicycleResult> data = new ArrayList<>();

    public interface ItemClickListener {
        void onBicycleClick(BicycleResult bicycleResult);
    }

    public RecyclerAdapter(BookmarkUtil bookmarkUtil, ItemClickListener listener) {
        this.bookmarkUtil = bookmarkUtil;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return  new ItemHolder(itemView, bookmarkUtil);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final BicycleResult data = this.data.get(position);
        ((ItemHolder)holder).populate(data);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBicycleClick(data);
            }
        });
    }

    public void setData(List<BicycleResult> data) {
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

}