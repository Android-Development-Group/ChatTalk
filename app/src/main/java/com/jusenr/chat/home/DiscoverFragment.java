package com.jusenr.chat.home;

import android.os.Bundle;

import com.jusenr.chat.R;
import com.jusenr.library.controller.BaseFragment;
import com.putao.ptlog.PTLog;

/**
 * Created by T5-Jusenr on 2017/4/4.
 */

public class DiscoverFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_discover;
    }

    @Override
    public void onViewCreatedFinish(Bundle savedInstanceState) {
        PTLog.i(getClass().getSimpleName());
    }

    @Override
    protected String[] getRequestUrls() {
        return new String[0];
    }
}
