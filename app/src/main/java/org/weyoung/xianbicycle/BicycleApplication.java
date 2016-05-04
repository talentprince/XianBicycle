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

import android.support.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;
import com.tencent.stat.StatService;


public class BicycleApplication extends MultiDexApplication implements Thread.UncaughtExceptionHandler {

    Thread.UncaughtExceptionHandler handler;
    AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(getApplicationContext())).build();
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(getApplicationContext());
        }
    }


    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        if (handler != null) {
            handler.uncaughtException(thread, exception);
        } else {
            StatService.reportException(this, exception);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    public AppComponent component() {
        return appComponent;
    }
}
