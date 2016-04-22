package org.weyoung.xianbicycle.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManager;

import org.weyoung.xianbicycle.NavigatorActivity;
import org.weyoung.xianbicycle.data.BicycleLocation;

import java.util.ArrayList;
import java.util.List;

public class NavigationUtil {
    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    public static BicycleLocation lastKnown;

    public static void updateLastKnown(BDLocation lastKnown) {
        NavigationUtil.lastKnown = new BicycleLocation(lastKnown.getLatitude(), lastKnown.getLongitude(), lastKnown.getAddrStr());
    }

    public static BicycleLocation getLastKnown() {
        return lastKnown;
    }

    public static void launchNavigator(final Activity activity, BicycleLocation end) {
        if (lastKnown == null) {
            return;
        }
        final BNRoutePlanNode startPoint = new BNRoutePlanNode(lastKnown.getLon(), lastKnown.getLat(),
                lastKnown.getName(), null, BNRoutePlanNode.CoordinateType.GCJ02);
        BNRoutePlanNode endPoint = new BNRoutePlanNode(end.getLon(), end.getLat(),
                end.getName(), null, BNRoutePlanNode.CoordinateType.BD09LL);
        List<BNRoutePlanNode> list = new ArrayList<>();
        list.add(startPoint);
        list.add(endPoint);
        BaiduNaviManager.getInstance().launchNavigator(activity,
                list,
                1,
                true,
                new BaiduNaviManager.RoutePlanListener() {
                    @Override
                    public void onJumpToNavigator() {
                        Intent intent = new Intent(activity, NavigatorActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(ROUTE_PLAN_NODE, startPoint);
                        intent.putExtras(bundle);
                        activity.startActivity(intent);
                    }

                    @Override
                    public void onRoutePlanFailed() {
                        Toast.makeText(activity, "算路失败", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

}
