package org.weyoung.xianbicycle.ui.bookmark;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.weyoung.xianbicycle.R;
import org.weyoung.xianbicycle.data.BicycleResult;
import org.weyoung.xianbicycle.data.SearchQuery;
import org.weyoung.xianbicycle.net.BicycleLoader;
import org.weyoung.xianbicycle.utils.BookmarkUtil;

import java.util.List;

import javax.inject.Inject;

public class BookmarkPresenter extends MvpBasePresenter<BookmarkView> {
    @Inject
    BookmarkUtil bookmarkUtil;

    @Inject
    public BookmarkPresenter() {
    }

    public void refreshBookmark() {
        if (!isViewAttached()) {
            return;
        }
        List<String> bookmark = bookmarkUtil.queryAllIds();
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
                    if (data.size() == 0) {
                        getView().showMessage(R.string.no_result);
                    }
                    getView().setData(data);
                    getView().showContent();
                }
            }

            @Override
            public void onLoaderFailed() {
                if (isViewAttached()) {
                    getView().showMessage(R.string.error);
                    getView().showContent();
                }
            }
        }).load(new SearchQuery(bookmark));
    }
}
