package org.weyoung.xianbicycle.net;

import org.weyoung.xianbicycle.data.BicycleData;
import org.weyoung.xianbicycle.data.Search;

import java.util.List;

import rx.functions.Action1;

public class Loader {
    private Callback callback;

    public Loader(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void onLoaderStarted();

        void onLoaderFinished(List<BicycleData> data);

        void onLoaderFailed();
    }

    public void load(Search search) {
        callback.onLoaderStarted();
        Fetcher fetcher = new Fetcher();
        fetcher.getData(search).subscribe(new Action1<List<BicycleData>>() {
            @Override
            public void call(List<BicycleData> data) {
                callback.onLoaderFinished(data);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                callback.onLoaderFailed();
            }
        });
    }
}
