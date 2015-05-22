package org.weyoung.xianbicycle.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.navisdk.BNaviPoint;
import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.comapi.routeplan.RoutePlanParams;

import org.weyoung.xianbicycle.NavigatorActivity;
import org.weyoung.xianbicycle.data.Place;

public class NavigationUtil {
    public static Place lastKnown;

    public static void updateLastKnown(BDLocation lastKnown) {
        NavigationUtil.lastKnown = new Place(lastKnown.getLatitude(), lastKnown.getLongitude(), lastKnown.getAddrStr());
    }

    public static Place getLastKnown() {
        return lastKnown;
    }

    public static void launchNavigator(final Activity activity, Place end) {
        if (lastKnown == null) {
            return;
        }
        BNaviPoint startPoint = new BNaviPoint(lastKnown.getLon(), lastKnown.getLat(),
                lastKnown.getName(), BNaviPoint.CoordinateType.BD09_MC);
        BNaviPoint endPoint = new BNaviPoint(end.getLon(), end.getLat(),
                end.getName(), BNaviPoint.CoordinateType.BD09_MC);
        BaiduNaviManager.getInstance().launchNavigator(activity,
                startPoint,
                endPoint,
                RoutePlanParams.NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_TIME,
                true,
                BaiduNaviManager.STRATEGY_FORCE_ONLINE_PRIORITY,
                new BaiduNaviManager.OnStartNavigationListener() {

                    @Override
                    public void onJumpToNavigator(Bundle configParams) {
                        Intent intent = new Intent(activity, NavigatorActivity.class);
                        intent.putExtras(configParams);
                        activity.startActivity(intent);
                    }

                    @Override
                    public void onJumpToDownloader() {
                    }
                });
    }

}
