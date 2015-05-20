package org.weyoung.xianbicycle.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.weyoung.xianbicycle.BuildConfig;
import org.weyoung.xianbicycle.R;
import org.weyoung.xianbicycle.utils.FileUtil;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AboutFragment extends Fragment{
    public static final String TAG = AboutFragment.class.getSimpleName();

    @InjectView(R.id.about)
    TextView about;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_fragment, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Spanned log = Html.fromHtml(FileUtil.readRawTextFile(getActivity(), R.raw.log));
        about.setText(log);
    }

}
