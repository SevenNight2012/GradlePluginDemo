package com.xxc.dev.gradle.plugin;

import android.view.View;

public class ViewUtils {

    public static boolean isSmoothClick(View view) {
        Object tag = view.getTag(R.id.smooth_click_tag);
        if (tag instanceof Long) {
            long during = Math.abs(System.currentTimeMillis() - (long) tag);
            System.out.println("时间间隔 >>>  " + during);
            return during > 500;
        } else {
            return true;
        }
    }

}
