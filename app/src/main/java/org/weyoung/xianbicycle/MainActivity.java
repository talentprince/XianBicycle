package org.weyoung.xianbicycle;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import org.weyoung.xianbicycle.ui.AboutFragment;
import org.weyoung.xianbicycle.ui.BookmarkFragment;
import org.weyoung.xianbicycle.ui.SearchFragment;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends BaseActivity {
    @InjectView(R.id.version)
    TextView version;
    private Fragment aboutFragment;
    private Fragment searchFragment;
    private Fragment bookmarkFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        ButterKnife.inject(this);

        String format = String.format(Locale.US, getString(R.string.version), BuildConfig.VERSION_NAME);
        version.setText(format);

        initAllFragment();
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
