package com.jusenr.chat.home;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jusenr.chat.MobClient;
import com.jusenr.chat.R;
import com.jusenr.library.controller.BaseFragment;
import com.putao.ptlog.PTLog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by T5-Jusenr on 2017/4/4.
 */

public class MineFragment extends BaseFragment {

    @BindView(R.id.mine_header)
    ImageView mMineHeader;
    @BindView(R.id.mine_name)
    TextView mMineName;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void onViewCreatedFinish(Bundle savedInstanceState) {
        MobClient.onEvent(mActivity, getClass().getSimpleName());
        PTLog.i(getClass().getSimpleName());
    }

    @Override
    protected String[] getRequestUrls() {
        return new String[0];
    }

    @OnClick({R.id.mine_header, R.id.mine_name, R.id.mine_about})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mine_header:
                break;
            case R.id.mine_name:
                break;
            case R.id.mine_about:
                PTLog.showViewer();
                break;
        }
    }
}
