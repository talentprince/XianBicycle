package org.weyoung.xianbicycle.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.weyoung.xianbicycle.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;

public class ToolbarFragment extends Fragment {

    @Optional
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
        if (mToolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        }
    }

    public void setTitle(String title) {
        ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(title);
        }
    }
}
