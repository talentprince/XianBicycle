package org.weyoung.xianbicycle.ui;

import android.graphics.Point;
import android.view.View;

import com.github.amlcurran.showcaseview.targets.Target;


public class CoachTarget implements Target{

    private final View mView;

    public CoachTarget(View view) {
        mView = view;
    }

    @Override
    public Point getPoint() {
        int[] location = new int[2];
        mView.getLocationInWindow(location);
        int x = location[0];
        int y = location[1] + mView.getHeight() / 2;
        return new Point(x, y);
    }

}
