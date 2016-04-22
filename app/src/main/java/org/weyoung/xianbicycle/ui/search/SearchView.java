package org.weyoung.xianbicycle.ui.search;

import com.baidu.location.BDLocation;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.weyoung.xianbicycle.data.BicycleResult;

import java.util.List;


interface SearchView extends MvpView {
    void showMessage(int resId);
    void showTip(int resId);
    void setLocation(BDLocation location);
    void showLoading();
    void showContent();
    void setData(List<BicycleResult> data);
}
