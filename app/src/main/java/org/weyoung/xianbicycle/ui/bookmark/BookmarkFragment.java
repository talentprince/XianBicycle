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
package org.weyoung.xianbicycle.ui.bookmark;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.weyoung.xianbicycle.BicycleApplication;
import org.weyoung.xianbicycle.R;
import org.weyoung.xianbicycle.data.BicycleLocation;
import org.weyoung.xianbicycle.data.BicycleResult;
import org.weyoung.xianbicycle.ui.RecyclerAdapter;
import org.weyoung.xianbicycle.utils.BookmarkUtil;
import org.weyoung.xianbicycle.utils.NavigationUtil;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BookmarkFragment extends MvpFragment<BookmarkView, BookmarkPresenter>
        implements RecyclerAdapter.ItemClickListener, BookmarkView{
    public static final String TAG = BookmarkFragment.class.getSimpleName();
    @Bind(R.id.summary)
    TextView summaryView;
    @Bind(R.id.content_view)
    RecyclerView recyclerView;
    @Bind(R.id.loading_view)
    ProgressBar progressBar;
    private RecyclerAdapter recyclerAdapter;

    @Inject
    BookmarkUtil bookmarkUtil;
    @Inject
    BookmarkPresenter bookmarkPresenter;

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
        initView();
        presenter.refreshBookmark();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden && isAdded() && presenter != null) {
            presenter.refreshBookmark();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBookmarkUpdate(BookmarkUpdate bookmarkUpdate) {
        presenter.refreshBookmark();
    }

    private void initView() {
        recyclerAdapter = new RecyclerAdapter(bookmarkUtil, BookmarkFragment.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerAdapter);
    }
    //================================================================================
    // OnItemClickListener
    //================================================================================
    @Override
    public void onBicycleClick(BicycleResult bicycleResult) {
        if (NavigationUtil.getLastKnown() == null) {
            showMessage(R.string.get_ur_location);
            return;
        }
        NavigationUtil.launchNavigator(getActivity(),
                new BicycleLocation(Double.valueOf(bicycleResult.getLatitude()),
                        Double.valueOf(bicycleResult.getLongitude()), bicycleResult.getSitename()));
    }


    //================================================================================
    // BookmarkPresenter
    //================================================================================

    @Override
    public BookmarkPresenter createPresenter() {
        return bookmarkPresenter;
    }

    @Override
    public void showLoading() {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showContent() {
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showMessage(int messageId) {
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

    @Override
    public void setData(List<BicycleResult> data) {
        summaryView.setText(String.format(Locale.US, getResources().getString(R.string.bookmark_number), data.size()));
        recyclerAdapter.setData(data);
        recyclerAdapter.notifyDataSetChanged();
    }
}
