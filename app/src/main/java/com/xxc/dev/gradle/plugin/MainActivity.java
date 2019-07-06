package com.xxc.dev.gradle.plugin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import com.xxc.dev.gradle.plugin.annotation.Cast;
import com.xxc.dev.gradle.plugin.annotation.SmoothClick;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private TextView mHello;

    @Cast
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(LayoutInflater.from(this), new DataBinderViewFactory() {
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                AppCompatDelegate delegate = getDelegate();
                View view = delegate.createView(parent, name, context, attrs);
                view.setTag(R.id.view_host_tag, new ActivityShadow(MainActivity.this));
                return view;
            }
        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHello = findViewById(R.id.main_hello);
        mHello.setOnClickListener(this);
        startActivity(new Intent(this, MainActivity.class));
    }

    @SmoothClick
    @Override
    public void onClick(View v) {
        Toast.makeText(this, "点击HelloWorld", Toast.LENGTH_SHORT).show();
    }

}
