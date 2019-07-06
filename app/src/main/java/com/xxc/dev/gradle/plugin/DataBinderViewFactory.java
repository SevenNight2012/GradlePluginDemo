package com.xxc.dev.gradle.plugin;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

public abstract class DataBinderViewFactory implements LayoutInflater.Factory2 {

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return null;
    }
}
