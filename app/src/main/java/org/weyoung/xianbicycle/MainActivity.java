package org.weyoung.xianbicycle;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import org.weyoung.xianbicycle.ui.AboutFragment;
import org.weyoung.xianbicycle.ui.SearchFragment;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends BaseActivity {
    @InjectView(R.id.version)
    TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        ButterKnife.inject(this);

        String format = String.format(Locale.US, getString(R.string.version), BuildConfig.VERSION_NAME);
        version.setText(format);
    }

    @Override
    protected void goToNavDrawerItem(int item) {
        switch (item) {
            case NAVDRAWER_ITEM_ABOUT:
                Fragment about = getFragment(AboutFragment.TAG);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment, about, AboutFragment.TAG)
                        .commit();
                break;
            case NAVDRAWER_ITEM_SEARCH:
                Fragment search = getFragment(SearchFragment.TAG);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment, search, SearchFragment.TAG)
                        .commit();
                break;
            case NAVDRAWER_ITEM_BOOKMARK:
                break;
        }
    }

    private Fragment getFragment(String tag) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("");
        if (fragment == null) {
            if (tag.equals(AboutFragment.TAG)) {
                fragment = new AboutFragment();
            } else if (tag.equals(SearchFragment.TAG)) {
                fragment = new SearchFragment();
            }
        }
        return fragment;
    }
}
