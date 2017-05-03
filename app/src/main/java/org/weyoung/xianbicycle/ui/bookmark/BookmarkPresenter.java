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

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

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
