package com.jusenr.chat.base;

import android.os.Bundle;

import com.jusenr.chat.R;
import com.jusenr.chatlibrary.controller.BaseActivity;
import com.jusenr.chatlibrary.view.NavigationBar;

import butterknife.BindView;


public abstract class TitleActivity extends BaseActivity implements NavigationBar.ActionsListener {
    @BindView(R.id.navigation_bar)
    public NavigationBar mNavigation_bar;

    @Override
    protected void onViewCreatedFinish(Bundle saveInstanceState) {
        mNavigation_bar.setActionListener(this);
    }

    @Override
    public void onLeftAction() {
        finish();
    }

    @Override
    public void onRightAction() {

    }

    @Override
    public void onMainAction() {

    }
}
