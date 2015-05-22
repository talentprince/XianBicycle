package org.weyoung.xianbicycle.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.weyoung.xianbicycle.R;
import org.weyoung.xianbicycle.data.BicycleData;
import org.weyoung.xianbicycle.net.Loader;
import org.weyoung.xianbicycle.net.Search;
import org.weyoung.xianbicycle.utils.BookmarkUtil;

import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class BookmarkFragment extends Fragment {
    public static final String TAG = BookmarkFragment.class.getSimpleName();
    @InjectView(R.id.summary)
    TextView summaryView;
    @InjectView(R.id.result)
    ListView resultView;
    @InjectView(R.id.progress)
    ProgressBar progressBar;
    private DataAdapter dataAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bookmark_fragment, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshBookmark();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden && isAdded()) {
            refreshBookmark();
        }
    }

    private void showProgress() {
        resultView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        resultView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void refreshBookmark() {
        if (null == getActivity()) {
            return;
        }
        List<String> bookmark = BookmarkUtil.getAll(getActivity());
        summaryView.setText(String.format(Locale.US, getResources().getString(R.string.bookmark_number), bookmark.size()));

        if (!bookmark.isEmpty()) {
            new Loader(new Loader.Callback() {
                @Override
                public void onLoaderStarted() {
                    showProgress();
                }

                @Override
                public void onLoaderFinished(List<BicycleData> data) {
                    if (data.size() == 0) {
                        Toast.makeText(getActivity(), R.string.no_result, Toast.LENGTH_SHORT).show();
                    } else {
                        dataAdapter = new DataAdapter(getActivity(), data, BookmarkUtil.getAll(getActivity()));
                        resultView.setAdapter(dataAdapter);
                    }
                    hideProgress();
                }

                @Override
                public void onLoaderFailed() {
                    Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                    hideProgress();
                }
            }).load(new Search(bookmark));
        } else if (dataAdapter != null){
            dataAdapter.clear();
            dataAdapter.notifyDataSetChanged();
        }
    }

}
