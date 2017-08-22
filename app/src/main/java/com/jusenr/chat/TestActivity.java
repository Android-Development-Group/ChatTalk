package com.jusenr.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jusenr.chat.base.TitleActivity;
import com.jusenr.chat.qrcode.QRActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class TestActivity extends TitleActivity {

    @BindView(R.id.btn_1)
    Button mBtn1;
    @BindView(R.id.btn_2)
    Button mBtn2;
    @BindView(R.id.btn_3)
    Button mBtn3;
    @BindView(R.id.btn_4)
    Button mBtn4;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_test);
    }

    @Override
    protected void onViewCreatedFinish(Bundle saveInstanceState) {
        super.onViewCreatedFinish(saveInstanceState);
        MobClient.onEvent(this, getLocalClassName());
    }

    @OnClick({R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                startActivity(QRActivity.class);
                break;
            case R.id.btn_2:
                break;
            case R.id.btn_3:
                break;
            case R.id.btn_4:
                break;
        }
    }
}
