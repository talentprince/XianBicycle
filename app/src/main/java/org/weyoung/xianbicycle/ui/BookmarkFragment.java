package org.weyoung.xianbicycle.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.weyoung.xianbicycle.BicycleApplication;
import org.weyoung.xianbicycle.R;
import org.weyoung.xianbicycle.data.BicycleData;
import org.weyoung.xianbicycle.data.Place;
import org.weyoung.xianbicycle.data.Search;
import org.weyoung.xianbicycle.net.Loader;
import org.weyoung.xianbicycle.utils.BookmarkUtil;
import org.weyoung.xianbicycle.utils.NavigationUtil;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class BookmarkFragment extends Fragment {
    public static final String TAG = BookmarkFragment.class.getSimpleName();
    @Bind(R.id.summary)
    TextView summaryView;
    @Bind(R.id.result)
    ListView resultView;
    @Bind(R.id.progress)
    ProgressBar progressBar;
    private DataAdapter dataAdapter;

    @Inject
    BookmarkUtil bookmarkUtil;

    public static class BookmarkUpdate {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bookmark_fragment, container, false);
        ButterKnife.bind(this, view);
        ((BicycleApplication)(getActivity().getApplication())).component().inject(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshBookmark();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden && isAdded()) {
            refreshBookmark();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @OnItemClick(R.id.result)
    void onResultItemClick(int index) {
        BicycleData bicycleData = dataAdapter.getItem(index);
        if (NavigationUtil.getLastKnown() == null) {
            showMessage(R.string.get_ur_location);
            return;
        }
        NavigationUtil.launchNavigator(getActivity(),
                new Place(Double.valueOf(bicycleData.getLatitude()), Double.valueOf(bicycleData.getLongitude()), bicycleData.getSitename()));
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
        List<String> bookmark;
        try {
            bookmark = bookmarkUtil.queryAllIds();
        } catch (Exception e) {
            showMessage(R.string.error);
            return;
        }
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
                        showMessage(R.string.no_result);
                    } else {
                        try {
                            dataAdapter = new DataAdapter(getActivity(), data, bookmarkUtil);
                            resultView.setAdapter(dataAdapter);
                        } catch (Exception e) {
                            showMessage(R.string.error);
                        }
                    }
                    hideProgress();
                }

                @Override
                public void onLoaderFailed() {
                    showMessage(R.string.error);
                    hideProgress();
                }
            }).load(new Search(bookmark));
        } else if (dataAdapter != null){
            dataAdapter.clear();
            dataAdapter.notifyDataSetChanged();
        }
    }

    private void showMessage(int messageId) {
        if (isAdded()) {
            final Snackbar snackbar =  Snackbar.make(getView(), getString(messageId), Snackbar.LENGTH_LONG);
            snackbar.setAction(getString(R.string.hide), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            }).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBookmarkUpdate(BookmarkUpdate bookmarkUpdate) {
        refreshBookmark();
    }
}
