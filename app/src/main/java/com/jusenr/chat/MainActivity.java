package com.jusenr.chat;

import android.os.Bundle;
import android.widget.TextView;

import com.jusenr.chat.jninative.NativeContent;
import com.jusenr.chatlibrary.controller.BaseActivity;

public class MainActivity extends BaseActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onViewCreatedFinish(Bundle saveInstanceState) {
        TextView tv = (TextView) findViewById(R.id.sample_text);

        NativeContent nativeContent = new NativeContent();
        String s = nativeContent.stringFromJNI();
        tv.setText(s);
    }

}
