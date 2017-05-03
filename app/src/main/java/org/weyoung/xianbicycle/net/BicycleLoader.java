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
package org.weyoung.xianbicycle.net;

import org.weyoung.xianbicycle.data.BicycleResult;
import org.weyoung.xianbicycle.data.SearchQuery;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

import static io.reactivex.Single.just;


public class BicycleLoader {
    private Callback callback;

    public BicycleLoader(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void onLoaderStarted();

        void onLoaderFinished(List<BicycleResult> data);

        void onLoaderFailed();
    }

    public void load(SearchQuery searchQuery) {
        callback.onLoaderStarted();
        just(searchQuery).compose(new DataFetcher().fetchData())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<BicycleResult>>() {
                    @Override
                    public void accept(@NonNull List<BicycleResult> bicycleResults) throws Exception {
                        callback.onLoaderFinished(bicycleResults);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        callback.onLoaderFailed();

                    }
                });
    }
}
