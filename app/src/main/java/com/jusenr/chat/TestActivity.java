package com.jusenr.chat;

import android.os.Bundle;

import com.jusenr.chat.base.TitleActivity;

public class TestActivity extends TitleActivity {

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_test);
    }

    @Override
    protected void onViewCreatedFinish(Bundle saveInstanceState) {
        super.onViewCreatedFinish(saveInstanceState);

    }
}
