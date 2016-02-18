package org.weyoung.xianbicycle.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import org.weyoung.xianbicycle.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutFragment extends Fragment{
    public static final String TAG = AboutFragment.class.getSimpleName();

    @Bind(R.id.about)
    WebView about;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        about.getSettings().setDefaultTextEncodingName("utf-8");
        about.loadUrl("file:///android_asset/log");
    }

}
