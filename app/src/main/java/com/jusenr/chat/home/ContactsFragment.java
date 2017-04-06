package com.jusenr.chat.home;

import android.os.Bundle;
import android.widget.TextView;

import com.jusenr.chat.R;
import com.jusenr.chatlibrary.controller.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by T5-Jusenr on 2017/4/4.
 */

public class ContactsFragment extends BaseFragment {

    @BindView(R.id.tv_test)
    TextView mTvTest;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contacts;
    }

    @Override
    public void onViewCreatedFinish(Bundle savedInstanceState) {
        mTvTest.setText("测试bugly崩溃收集");
    }

    @Override
    protected String[] getRequestUrls() {
        return new String[0];
    }

    @OnClick(R.id.tv_test)
    public void onViewClicked() {
        mTvTest.setText(0);
    }
}
