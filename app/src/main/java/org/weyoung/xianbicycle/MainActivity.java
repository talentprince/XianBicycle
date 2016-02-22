package org.weyoung.xianbicycle;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;
import android.widget.Toast;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatService;

import org.weyoung.xianbicycle.ui.AboutFragment;
import org.weyoung.xianbicycle.ui.BookmarkFragment;
import org.weyoung.xianbicycle.ui.SearchFragment;
import org.weyoung.xianbicycle.utils.FileUtil;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class MainActivity extends BaseActivity {
    @Bind(R.id.version)
    TextView version;
    private Fragment aboutFragment;
    private Fragment searchFragment;
    private Fragment bookmarkFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        ButterKnife.bind(this);

        String format = String.format(Locale.US, getString(R.string.version), BuildConfig.VERSION_NAME);
        version.setText(format);

        initAllFragment();

        initBaiduNaviEngine();

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

    private void initBaiduNaviEngine() {
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this,
                new String[]{ACCESS_FINE_LOCATION,
                            ACCESS_COARSE_LOCATION}, new PermissionsResultAction() {
                    @Override
                    public void onGranted() {
                        BaiduNaviManager.getInstance().init(MainActivity.this, FileUtil.getSdcardDir(), null, null, null);
                    }
                    @Override
                    public void onDenied(String permission) {
                        Toast.makeText(MainActivity.this,
                                getResources().getString(R.string.permission_request_denied), Toast.LENGTH_LONG).show();
                    }
                });
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }
}
