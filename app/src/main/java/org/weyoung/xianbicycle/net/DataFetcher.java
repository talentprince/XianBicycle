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

import com.facebook.stetho.okhttp3.BuildConfig;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.weyoung.xianbicycle.data.BicycleResult;
import org.weyoung.xianbicycle.data.SearchQuery;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class DataFetcher {

    public static final String URL = "http://xian-pub-bicycle.herokuapp.com/api?query=";

    public SingleTransformer<SearchQuery, List<BicycleResult>> fetchData() {
        return new SingleTransformer<SearchQuery, List<BicycleResult>>() {
            @Override
            public SingleSource<List<BicycleResult>> apply(@NonNull Single<SearchQuery> searchQuery) {
                return searchQuery.observeOn(Schedulers.io()).map(new Function<SearchQuery, List<BicycleResult>>() {
                    @Override
                    public List<BicycleResult> apply(@NonNull SearchQuery searchQuery) throws Exception {
                        try {
                            String url = URL + URLEncoder.encode(new Gson().toJson(searchQuery), "utf-8");
                            Request request = new Request.Builder().get().url(url).build();
                            OkHttpClient okClient = new OkHttpClient();
                            if (BuildConfig.DEBUG) {
                                okClient.interceptors().add(new StethoInterceptor());
                            }
                            Response response = okClient.newCall(request).execute();
                            List<BicycleResult> t = new ArrayList<>();
                            if (response.isSuccessful()) {
                                t = new Gson().fromJson(response.body().string(),
                                        new TypeToken<List<BicycleResult>>() {
                                        }.getType());
                            }
                            return t;
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        };
    }
}