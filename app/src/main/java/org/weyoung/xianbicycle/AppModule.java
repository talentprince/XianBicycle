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
package org.weyoung.xianbicycle;


import android.content.Context;

import com.amap.api.location.AMapLocationClient;

import org.weyoung.xianbicycle.utils.BookmarkUtil;

import javax.inject.Singleton;

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
    public AMapLocationClient providesLocationClient(Context context) {
        return new AMapLocationClient(context);
    }

}
