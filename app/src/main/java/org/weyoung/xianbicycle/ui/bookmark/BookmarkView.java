package org.weyoung.xianbicycle.ui.bookmark;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.weyoung.xianbicycle.data.BicycleResult;

import java.util.List;

interface BookmarkView extends MvpView {
    void showMessage(int resId);
    void showLoading();
    void showContent();
    void setData(List<BicycleResult> data);
}
