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

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.weyoung.xianbicycle.R;
import org.weyoung.xianbicycle.data.BicycleResult;
import org.weyoung.xianbicycle.data.SearchQuery;
import org.weyoung.xianbicycle.net.BicycleLoader;

import java.util.List;

import javax.inject.Inject;

public class SearchPresenter extends MvpBasePresenter<SearchView>{
    @Inject
    LocationClient locationClient;

    @Inject
    public SearchPresenter() {
    }

    public void initLocationClient() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("gcj02");
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        option.setNeedDeviceDirect(true);

        locationClient.setLocOption(option);
        locationClient.start();

        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                locationClient.stop();
                getView().setLocation(location);
            }
        });
    }

    public void query(SearchQuery searchQuery) {
        new BicycleLoader(new BicycleLoader.Callback() {
            @Override
            public void onLoaderStarted() {
                if (isViewAttached()) {
                    getView().showLoading();
                }
            }
            @Override
            public void onLoaderFinished(List<BicycleResult> data) {
                if (isViewAttached()) {
                    getView().showContent();
                    if (data.size() == 0) {
                        if (isViewAttached()) {
                            getView().showMessage(R.string.no_result);
                        }
                    } else {
                        getView().showTip(R.string.tips);
                    }
                    getView().setData(data);
                }
            }

            @Override
            public void onLoaderFailed() {
                if (isViewAttached()) {
                    getView().showContent();
                    getView().showMessage(R.string.error);
                }
            }
        }).load(searchQuery);
    }

    public void startLocationClient() {
        if (locationClient != null) {
            locationClient.start();
        }
    }

    public void stopLocationClient() {
        if (locationClient != null) {
            locationClient.stop();
        }
    }
}
