package org.weyoung.xianbicycle.net;

import com.facebook.stetho.okhttp3.BuildConfig;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.weyoung.xianbicycle.data.BicycleData;
import org.weyoung.xianbicycle.data.Search;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static rx.Observable.just;

public class Fetcher {

    public static final String URL = "http://xian-pub-bicycle.herokuapp.com/api?query=";

    public Observable<List<BicycleData>> getData(final Search search) {
        return just(search).flatMap(new Func1<Search, Observable<List<BicycleData>>>() {
            @Override
            public Observable<List<BicycleData>> call(final Search s) {

                return Observable.create(new Observable.OnSubscribe<List<BicycleData>>() {
                    @Override
                    public void call(Subscriber<? super List<BicycleData>> subscriber) {
                        try {
                            String url = URL + URLEncoder.encode(new Gson().toJson(s), "utf-8");
                            Request request = new Request.Builder().get().url(url).build();
                            OkHttpClient okClient = new OkHttpClient();
                            if (BuildConfig.DEBUG) {
                                okClient.interceptors().add(new StethoInterceptor());
                            }
                            Response response = okClient.newCall(request).execute();
                            List<BicycleData> t = new ArrayList<>();
                            if (response.isSuccessful()) {
                                t = new Gson().fromJson(response.body().string(),
                                        new TypeToken<List<BicycleData>>() {
                                        }.getType());
                            }
                            subscriber.onNext(t);
                        } catch (Exception e) {
                            subscriber.onError(e);
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        });
    }
}