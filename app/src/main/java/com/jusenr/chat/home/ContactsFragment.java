package com.jusenr.chat.home;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jusenr.chat.MobClient;
import com.jusenr.chat.R;
import com.jusenr.chat.TotalActivity;
import com.jusenr.chat.qrcode.QRActivity;
import com.jusenr.library.controller.BaseFragment;
import com.putao.ptlog.PTLog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by T5-Jusenr on 2017/4/4.
 */

public class ContactsFragment extends BaseFragment {

    @BindView(R.id.tv_test)
    TextView mTvTest;
    @BindView(R.id.btn_1)
    Button mBtn1;
    @BindView(R.id.btn_2)
    Button mBtn2;
    @BindView(R.id.btn_3)
    Button mBtn3;
    @BindView(R.id.btn_4)
    Button mBtn4;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contacts;
    }

    @Override
    public void onViewCreatedFinish(Bundle savedInstanceState) {
        MobClient.onEvent(mActivity, getClass().getSimpleName());
        PTLog.i(getClass().getSimpleName());
        mTvTest.setText("测试bugly崩溃收集");
    }

    @Override
    protected String[] getRequestUrls() {
        return new String[0];
    }

    @OnClick({R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4, R.id.tv_test})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                startActivity(QRActivity.class);
                break;
            case R.id.btn_2:
                startActivity(TotalActivity.class);
                break;
            case R.id.btn_3:
                break;
            case R.id.btn_4:
                break;
            case R.id.tv_test:
                mTvTest.setText(0);
                break;
        }
    }
}
