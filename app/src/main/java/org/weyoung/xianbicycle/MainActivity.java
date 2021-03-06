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

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.tencent.stat.StatConfig;
import com.tencent.stat.StatService;

import org.weyoung.xianbicycle.ui.AboutFragment;
import org.weyoung.xianbicycle.ui.bookmark.BookmarkFragment;
import org.weyoung.xianbicycle.ui.search.SearchFragment;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity {

    private Fragment aboutFragment;
    private Fragment searchFragment;
    private Fragment bookmarkFragment;

    @BindView(R.id.version)
    TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        ButterKnife.bind(this);

        String format = String.format(Locale.US, getString(R.string.version), BuildConfig.VERSION_NAME);
        version.setText(format);

        initAllFragment();

        StatConfig.setDebugEnable(true);
        StatService.trackCustomEvent(this, "welcome", String.format(Locale.US, "%s %s %s", Build.BRAND, Build.DEVICE, Build.VERSION.CODENAME));
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    protected void goToNavDrawerItem(int item) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (item) {
            case NAVDRAWER_ITEM_ABOUT:
                if (!aboutFragment.isAdded()) {
                    fragmentTransaction.add(R.id.main_fragment, aboutFragment, AboutFragment.TAG);
                }
                fragmentTransaction
                        .hide(searchFragment).hide(bookmarkFragment)
                        .show(aboutFragment)
                        .commit();
                break;
            case NAVDRAWER_ITEM_SEARCH:
                if (!searchFragment.isAdded()) {
                    fragmentTransaction.add(R.id.main_fragment, searchFragment, SearchFragment.TAG);
                }
                fragmentTransaction
                        .hide(aboutFragment).hide(bookmarkFragment)
                        .show(searchFragment)
                        .commit();
                break;
            case NAVDRAWER_ITEM_BOOKMARK:
                if (!bookmarkFragment.isAdded()) {
                    fragmentTransaction.add(R.id.main_fragment, bookmarkFragment, BookmarkFragment.TAG);
                }
                fragmentTransaction
                        .hide(searchFragment).hide(aboutFragment)
                        .show(bookmarkFragment)
                        .commit();
                break;
        }
        getSupportFragmentManager().executePendingTransactions();
    }

    private void initAllFragment() {
        aboutFragment = getFragment(AboutFragment.TAG);
        searchFragment = getFragment(SearchFragment.TAG);
        bookmarkFragment = getFragment(BookmarkFragment.TAG);
    }

    private Fragment getFragment(String tag) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            if (tag.equals(AboutFragment.TAG)) {
                fragment = new AboutFragment();
            } else if (tag.equals(SearchFragment.TAG)) {
                fragment = new SearchFragment();
            } else if (tag.equals(BookmarkFragment.TAG)) {
                fragment = new BookmarkFragment();
            }
        }
        return fragment;
    }
}
