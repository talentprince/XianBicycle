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
package org.weyoung.xianbicycle.utils;

import android.app.Activity;

import com.amap.api.location.AMapLocation;

import org.weyoung.xianbicycle.data.BicycleLocation;

public class NavigationUtil {
    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    public static BicycleLocation lastKnown;

    public static void updateLastKnown(AMapLocation lastKnown) {
        NavigationUtil.lastKnown = new BicycleLocation(lastKnown.getLatitude(), lastKnown.getLongitude(), lastKnown.getStreet());
    }

    public static BicycleLocation getLastKnown() {
        return lastKnown;
    }

    public static void launchNavigator(final Activity activity, BicycleLocation end) {
        if (lastKnown == null) {
            return;
        }
//        final BNRoutePlanNode startPoint = new BNRoutePlanNode(lastKnown.getLon(), lastKnown.getLat(),
//                lastKnown.getName(), null, BNRoutePlanNode.CoordinateType.GCJ02);
//        BNRoutePlanNode endPoint = new BNRoutePlanNode(end.getLon(), end.getLat(),
//                end.getName(), null, BNRoutePlanNode.CoordinateType.BD09LL);
//        List<BNRoutePlanNode> list = new ArrayList<>();
//        list.add(startPoint);
//        list.add(endPoint);
//        BaiduNaviManager.getInstance().launchNavigator(activity,
//                list,
//                1,
//                true,
//                new BaiduNaviManager.RoutePlanListener() {
//                    @Override
//                    public void onJumpToNavigator() {
//                        Intent intent = new Intent(activity, NavigatorActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable(ROUTE_PLAN_NODE, startPoint);
//                        intent.putExtras(bundle);
//                        activity.startActivity(intent);
//                    }
//
//                    @Override
//                    public void onRoutePlanFailed() {
//                        Toast.makeText(activity, "算路失败", Toast.LENGTH_SHORT).show();
//                    }
//                }
//        );
    }

}
