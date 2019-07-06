package com.xxc.dev.gradle.plugin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHello = findViewById(R.id.main_hello);
        mHello.setOnClickListener(this);
    }

    @SmoothClick
    @Override
    public void onClick(View v) {
        Toast.makeText(this, "点击HelloWorld", Toast.LENGTH_SHORT).show();
    }

}
