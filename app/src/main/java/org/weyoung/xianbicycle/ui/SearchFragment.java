package org.weyoung.xianbicycle.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

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
import org.weyoung.xianbicycle.utils.CoachUtil;
import org.weyoung.xianbicycle.utils.NavigationUtil;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;


public class SearchFragment extends Fragment {
    public static final String TAG = SearchFragment.class.getSimpleName();

    @Bind(R.id.query)
    EditText query;
    @Bind(R.id.result)
    ListView result;
    @Bind(R.id.progress)
    ProgressBar progressBar;
    @Bind(R.id.location)
    TextView locationHeader;

    @Inject
    BookmarkUtil bookmarkUtil;

    private LocationClient locationClient;
    private LocationClientOption option;
    private boolean isLocationSearch;
    private DataAdapter dataAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        ButterKnife.bind(this, view);
        ((BicycleApplication)(getActivity().getApplication())).component().inject(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        query.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    query(new Search(query.getText().toString()));
                }
                return false;
            }
        });

        initLocation();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (locationClient != null) {
            locationClient.start();
        }
    }

    private void query(Search search) {
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
                        result.setAdapter(dataAdapter);
                        if (CoachUtil.isFirstLaunch(getContext())) {
                            showMessage(R.string.tips);
                        }
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
        }).load(search);
    }

    @OnItemClick(R.id.result)
    void onResultItemClick(int index) {
        BicycleData bicycleData = dataAdapter.getItem(index);
        if (NavigationUtil.getLastKnown() == null) {
            showMessage(R.string.get_ur_location);
            locationClient.start();
            return;
        }
        NavigationUtil.launchNavigator(getActivity(),
                new Place(Double.valueOf(bicycleData.getLatitude()), Double.valueOf(bicycleData.getLongitude()), bicycleData.getSitename()));
    }

    @OnClick(R.id.location_search)
    void onLocationSearchClick() {
        isLocationSearch = true;
        if (dataAdapter != null)
            dataAdapter.clear();
        locationClient.start();
    }

    private void showProgress() {
        query.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        query.setEnabled(true);
        progressBar.setVisibility(View.GONE);
    }

    private void initLocation() {
        option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("gcj02");
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        option.setNeedDeviceDirect(true);

        locationClient = new LocationClient(getActivity().getApplicationContext());
        locationClient.setLocOption(option);
        locationClient.start();

        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                locationClient.stop();
                if (isAdded() && getActivity() != null) {
                    if (location == null)
                        return;
                    NavigationUtil.updateLastKnown(location);
                    String addrStr = location.getAddrStr();
                    if (TextUtils.isEmpty(addrStr)) {
                        locationHeader.setText(R.string.location_failed);
                    } else {
                        String format = String.format(Locale.US, getString(R.string.ur_location), addrStr);
                        locationHeader.setText(format);
                    }

                    if (isLocationSearch) {
                        query(new Search(location.getLatitude(), location.getLongitude()));
                        isLocationSearch = false;
                    }
                }
            }
        });
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
    public void onBookmarkUpdate(BookmarkFragment.BookmarkUpdate bookmarkUpdate) {
        if (dataAdapter != null) {
            dataAdapter.notifyDataSetChanged();
        }
    }
}
