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
import android.content.Context;
import android.content.Intent;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.model.NaviLatLng;

import org.weyoung.xianbicycle.RouteActivity;
import org.weyoung.xianbicycle.data.BicycleLocation;

public class NavigationUtil {
    public static BicycleLocation lastKnown;

    public static void updateLastKnown(AMapLocation lastKnown) {
        NavigationUtil.lastKnown = new BicycleLocation(lastKnown.getLatitude(), lastKnown.getLongitude(), lastKnown.getStreet());
    }

    public static BicycleLocation getLastKnown() {
        return lastKnown;
    }

    public static void launchNavigator(final Activity activity, BicycleLocation endLocation) {
        if (lastKnown == null) {
            return;
        }
        NaviLatLng start = new NaviLatLng(lastKnown.getLat(), lastKnown.getLon());
        NaviLatLng end = locationTransform(activity, endLocation.getLat(), endLocation.getLon());
        Intent intent = new Intent(activity, RouteActivity.class);
        intent.putExtra("start", start);
        intent.putExtra("end", end);
        activity.startActivity(intent);
    }

    private static NaviLatLng locationTransform(Context context, double lan, double lon) {
        CoordinateConverter coordinateConverter = new CoordinateConverter(context);
        try {
            DPoint point = coordinateConverter.from(CoordinateConverter.CoordType.BAIDU).coord(new DPoint(lan, lon)).convert();
            return new NaviLatLng(point.getLatitude(), point.getLongitude());
        } catch (Exception e) {}
        double x = lon - 0.0065, y = lan - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * Math.PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * Math.PI);
        return new NaviLatLng(z * Math.sin(theta), z * Math.cos(theta));
    }

}
