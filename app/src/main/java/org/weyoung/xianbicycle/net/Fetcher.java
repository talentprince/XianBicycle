package org.weyoung.xianbicycle.net;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.weyoung.xianbicycle.data.BicycleData;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

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
                            Request request = new Request.Builder().get().url(URL
                                    + URLEncoder.encode(new Gson().toJson(s), "utf-8")).build();
                            OkHttpClient okClient = new OkHttpClient();
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