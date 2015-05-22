package org.weyoung.xianbicycle;

import android.app.Application;

import com.tencent.stat.StatService;


public class BicycleApplication extends Application implements Thread.UncaughtExceptionHandler {

    Thread.UncaughtExceptionHandler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }


    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        StatService.reportError(this, exception.getMessage());
        if (handler != null) {
            handler.uncaughtException(thread, exception);
        } else {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
