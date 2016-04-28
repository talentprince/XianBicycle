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
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;

import org.weyoung.xianbicycle.BuildConfig;
import org.weyoung.xianbicycle.R;
import org.weyoung.xianbicycle.ui.CoachTarget;

public class CoachUtil {

    private static final String VERSION = "version";
    private static final String COACH = "coach";

    public static boolean isFirstLaunch(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        int version = sp.getInt(VERSION, 0);
        if (BuildConfig.VERSION_CODE != version) {
            sp.edit().putInt(VERSION, BuildConfig.VERSION_CODE).apply();
            return true;
        }
        return false;
    }

    public static ShowcaseView showCoachMarkIfNecessary(final Activity activity, View view) {
        if (isCoachMarkAvailable(activity)) {
            final ShowcaseView showcaseView = new ShowcaseView.Builder(activity)
                    .setTarget(new CoachTarget(view))
                    .hideOnTouchOutside()
                    .setContentTitle(activity.getString(R.string.showcase_title))
                    .setStyle(R.style.ShowcaseTheme)
                    .replaceEndButton(R.layout.showcase_button)
                    .setShowcaseEventListener(new OnShowcaseEventListener() {
                        @Override
                        public void onShowcaseViewHide(ShowcaseView showcaseView) {
                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
                            sp.edit().putBoolean(COACH, false).apply();
                        }

                        @Override
                        public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

                        }

                        @Override
                        public void onShowcaseViewShow(ShowcaseView showcaseView) {

                        }

                        @Override
                        public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) {

                        }
                    })
                    .build();

            RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lps.addRule(RelativeLayout.CENTER_HORIZONTAL);
            lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

            int margin = ((Number) (activity.getResources().getDisplayMetrics().density * 12)).intValue();
            lps.setMargins(margin, margin, margin, margin + 300);
            showcaseView.setButtonPosition(lps);
            showcaseView.setTitleTextAlignment(Layout.Alignment.ALIGN_CENTER);


            showcaseView.overrideButtonClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showcaseView.hide();
                }
            });
            return showcaseView;
        }
        return null;
    }

    private static boolean isCoachMarkAvailable(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(COACH, true);
    }
}
