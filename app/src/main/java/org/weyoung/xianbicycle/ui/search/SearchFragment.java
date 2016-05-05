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
package org.weyoung.xianbicycle.ui.search;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.weyoung.xianbicycle.BicycleApplication;
import org.weyoung.xianbicycle.R;
import org.weyoung.xianbicycle.data.BicycleLocation;
import org.weyoung.xianbicycle.data.BicycleResult;
import org.weyoung.xianbicycle.data.SearchQuery;
import org.weyoung.xianbicycle.ui.RecyclerAdapter;
import org.weyoung.xianbicycle.ui.bookmark.BookmarkFragment;
import org.weyoung.xianbicycle.utils.BookmarkUtil;
import org.weyoung.xianbicycle.utils.CoachUtil;
import org.weyoung.xianbicycle.utils.NavigationUtil;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_PHONE_STATE;


public class SearchFragment extends MvpFragment<SearchView, SearchPresenter>
implements SearchView, RecyclerAdapter.ItemClickListener{
    public static final String TAG = SearchFragment.class.getSimpleName();

    @Bind(R.id.query)
    EditText queryView;
    @Bind(R.id.content_view)
    RecyclerView recyclerView;
    @Bind(R.id.loading_view)
    ProgressBar progressBar;
    @Bind(R.id.location)
    TextView locationHeader;

    @Inject
    BookmarkUtil bookmarkUtil;
    @Inject
    SearchPresenter searchPresenter;

    private RecyclerAdapter recyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        ButterKnife.bind(this, view);
        ((BicycleApplication)(getActivity().getApplication())).component().inject(this);
        return view;
    }

    @Override
    public SearchPresenter createPresenter() {
        return searchPresenter;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initLocation();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.location_search)
    void onLocationSearchClick() {
        if (recyclerAdapter != null)
            recyclerAdapter.clear();
        presenter.performLocationSearch();
    }

    private void initView() {
        queryView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    presenter.query(new SearchQuery(queryView.getText().toString()));
                }
                return false;
            }
        });
        recyclerAdapter = new RecyclerAdapter(bookmarkUtil, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void initLocation() {
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this,
                new String[]{ACCESS_FINE_LOCATION,
                        ACCESS_COARSE_LOCATION,
                        READ_PHONE_STATE}, new PermissionsResultAction() {
                    @Override
                    public void onGranted() {
                        presenter.initLocationClient();
                    }
                    @Override
                    public void onDenied(String permission) {
                        Toast.makeText(getActivity(),
                                getResources().getString(R.string.permission_request_denied), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBookmarkUpdate(BookmarkFragment.BookmarkUpdate bookmarkUpdate) {
        if (recyclerAdapter != null) {
            recyclerAdapter.notifyDataSetChanged();
        }
    }

    //================================================================================
    // SearchView
    //================================================================================

    @Override
    public void setData(List<BicycleResult> data) {
        recyclerAdapter.setData(data);
        recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        queryView.setEnabled(false);
    }

    @Override
    public void showContent() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        queryView.setEnabled(true);
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
    public void showTip(int resId) {
        if (CoachUtil.isFirstLaunch(getContext())) {
            showMessage(resId);
        }
    }

    @Override
    public void setLocation(boolean isLocationSearch, AMapLocation location) {
        if (isAdded() && getActivity() != null) {
            if (location == null)
                return;
            NavigationUtil.updateLastKnown(location);
            String addrStr = location.getAddress();
            if (TextUtils.isEmpty(addrStr)) {
                locationHeader.setText(R.string.location_failed);
            } else {
                String format = String.format(Locale.US, getString(R.string.ur_location), addrStr);
                locationHeader.setText(format);
            }

            if (isLocationSearch) {
                presenter.query(new SearchQuery(location.getLatitude(), location.getLongitude()));
            }
        }
    }

    //================================================================================
    // OnItemClickListener
    //================================================================================
    @Override
    public void onBicycleClick(BicycleResult bicycleResult) {
        if (NavigationUtil.getLastKnown() == null) {
            showMessage(R.string.get_ur_location);
            presenter.performLocationSearch();
            return;
        }
        NavigationUtil.launchNavigator(getActivity(),
                new BicycleLocation(Double.valueOf(bicycleResult.getLatitude()),
                        Double.valueOf(bicycleResult.getLongitude()), bicycleResult.getSitename()));
    }
}
