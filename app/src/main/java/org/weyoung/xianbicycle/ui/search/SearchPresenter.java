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

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.weyoung.xianbicycle.R;
import org.weyoung.xianbicycle.data.BicycleResult;
import org.weyoung.xianbicycle.data.SearchQuery;
import org.weyoung.xianbicycle.net.BicycleLoader;

import java.util.List;

import javax.inject.Inject;

public class SearchPresenter extends MvpBasePresenter<SearchView>{
    @Inject
    AMapLocationClient locationClient;

    @Inject
    public SearchPresenter() {
    }

    public void initLocationClient() {
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        option.setNeedAddress(true);
        option.setOnceLocation(true);

        locationClient.setLocationOption(option);
        locationClient.startLocation();

        locationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                locationClient.stopLocation();
                if (isViewAttached()) {
                    getView().setLocation(aMapLocation);
                }
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
            locationClient.startLocation();
        }
    }

    public void stopLocationClient() {
        if (locationClient != null) {
            locationClient.stopLocation();
        }
    }
}
