package org.weyoung.xianbicycle.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import org.weyoung.xianbicycle.R;
import org.weyoung.xianbicycle.data.BicycleData;
import org.weyoung.xianbicycle.data.Place;
import org.weyoung.xianbicycle.net.Loader;
import org.weyoung.xianbicycle.net.Search;
import org.weyoung.xianbicycle.utils.BookmarkUtil;
import org.weyoung.xianbicycle.utils.NavigationUtil;

import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;


public class SearchFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String TAG = SearchFragment.class.getSimpleName();

    @InjectView(R.id.query)
    EditText query;
    @InjectView(R.id.result)
    ListView result;
    @InjectView(R.id.progress)
    ProgressBar progressBar;
    @InjectView(R.id.location)
    TextView locationHeader;

    private LocationClient locationClient;
    private LocationClientOption option;
    private boolean isLocationSearch;
    private DataAdapter dataAdapter;
    private BDLocation lastKnownLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        ButterKnife.inject(this, view);
        PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);
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
                    Toast.makeText(getActivity(), R.string.no_result, Toast.LENGTH_SHORT).show();
                } else {
                    dataAdapter = new DataAdapter(getActivity(), data, BookmarkUtil.getAll(getActivity()));
                    result.setAdapter(dataAdapter);
                }
                hideProgress();
            }

            @Override
            public void onLoaderFailed() {
                Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                hideProgress();
            }
        }).load(search);
    }

    @OnItemClick(R.id.result)
    void onResultItemClick(int index) {
        BicycleData bicycleData = dataAdapter.getItem(index);
        if (lastKnownLocation == null) {
            Toast.makeText(getActivity(), R.string.get_ur_location, Toast.LENGTH_SHORT).show();
            locationClient.start();
            return;
        }
        NavigationUtil.launchNavigator(getActivity(),
                new Place(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), lastKnownLocation.getAddrStr()),
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
        option.setCoorType("bd09ll");
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
                if (location == null)
                    return;
                lastKnownLocation = location;
                String addrStr = location.getAddrStr();
                if (TextUtils.isEmpty(addrStr)) {
                    locationHeader.setText(R.string.location_failed);
                } else {
                    String format = String.format(Locale.US, getString(R.string.ur_location), addrStr);
                    locationHeader.setText(format);
                }

                if (isLocationSearch) {
                    query(new Search(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude())));
                    isLocationSearch = false;
                }
            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (dataAdapter != null) {
            dataAdapter.setBookmarkList(BookmarkUtil.getAll(getActivity()));
            dataAdapter.notifyDataSetChanged();
        }
    }
}
