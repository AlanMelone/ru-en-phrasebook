package com.r_mobile.phasebook;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TabHost;

/**
 * Created by Admin on 28.10.2015.
 */
public class MyTabFactory implements TabHost.TabContentFactory {

    private final Context mContext;

    public MyTabFactory(Context context) {
        mContext = context;
    }

    public View createTabContent(String tag) {
        View v = new View(mContext);
        v.setMinimumWidth(0);
        v.setMinimumHeight(0);
        return v;
    }
}
