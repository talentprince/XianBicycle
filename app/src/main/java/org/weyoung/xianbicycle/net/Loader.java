package org.weyoung.xianbicycle.net;

import org.weyoung.xianbicycle.data.BicycleResult;
import org.weyoung.xianbicycle.data.SearchQuery;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static rx.Single.just;

public class Loader {
    private Callback callback;

    public Loader(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void onLoaderStarted();

        void onLoaderFinished(List<BicycleResult> data);

        void onLoaderFailed();
    }

    public void load(SearchQuery searchQuery) {
        callback.onLoaderStarted();
        just(searchQuery).compose(new Fetcher().fetchData())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<BicycleResult>>() {
                    @Override
                    public void call(List<BicycleResult> data) {
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
