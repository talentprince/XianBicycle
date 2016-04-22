package org.weyoung.xianbicycle;


import android.content.Context;

import com.baidu.location.LocationClient;

import org.weyoung.xianbicycle.ui.search.SearchPresenter;
import org.weyoung.xianbicycle.utils.BookmarkUtil;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public Context providesApplicationContext() {
        return context;
    }

    @Provides
    @Singleton
    public BookmarkUtil providesBookmarkUtil(Context context) {
        return new BookmarkUtil(context);
    }

    @Provides
    @Singleton
    public LocationClient providesLocationClient(Context context) {
        return new LocationClient(context);
    }

}
