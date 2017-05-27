package com.jusenr.chat;

import android.os.Bundle;

import com.jusenr.chat.base.TitleActivity;

public class TotalActivity extends TitleActivity {


    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_total);
    }

    @Override
    protected void onViewCreatedFinish(Bundle saveInstanceState) {
        super.onViewCreatedFinish(saveInstanceState);

    }
}
