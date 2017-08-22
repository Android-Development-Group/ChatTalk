package com.jusenr.chat;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jusenr.chat.home.ContactsFragment;
import com.jusenr.chat.home.ConversationFragment;
import com.jusenr.chat.home.DiscoverFragment;
import com.jusenr.chat.home.MineFragment;
import com.jusenr.chat.qrcodescan.CaptureActivity;
import com.jusenr.chat.scanner.ScanQrCodeActivity;
import com.jusenr.chat.utils.PermissionsUtils;
import com.jusenr.library.controller.BaseActivity;
import com.jusenr.library.utils.ToastUtils;
import com.jusenr.library.view.DragPointView;
import com.yanzhenjie.permission.AndPermission;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements
        ViewPager.OnPageChangeListener,
        DragPointView.OnDragListencer {


    @BindView(R.id.ac_iv_search)
    ImageView mAcIvSearch;
    @BindView(R.id.seal_more)
    ImageView mSealMore;
    @BindView(R.id.title)
    RelativeLayout mTitle;
    @BindView(R.id.main_viewpager)
    ViewPager mMainViewpager;
    @BindView(R.id.tab_img_chats)
    ImageView mTabImgChats;
    @BindView(R.id.tv_chat)
    TextView mTvChat;
    @BindView(R.id.tab_text_chats)
    TextView mTabTextChats;
    @BindView(R.id.ll)
    RelativeLayout mLl;
    @BindView(R.id.seal_num)
    DragPointView mSealNum;
    @BindView(R.id.seal_chat)
    FrameLayout mSealChat;
    @BindView(R.id.tab_img_contact)
    ImageView mTabImgContact;
    @BindView(R.id.tab_text_contact)
    TextView mTabTextContact;
    @BindView(R.id.seal_contact_list)
    RelativeLayout mSealContactList;
    @BindView(R.id.tab_img_find)
    ImageView mTabImgFind;
    @BindView(R.id.tab_text_find)
    TextView mTabTextFind;
    @BindView(R.id.seal_find)
    RelativeLayout mSealFind;
    @BindView(R.id.tab_img_me)
    ImageView mTabImgMe;
    @BindView(R.id.tab_text_me)
    TextView mTabTextMe;
    @BindView(R.id.mine_red)
    ImageView mMineRed;
    @BindView(R.id.seal_me)
    FrameLayout mSealMe;
    @BindView(R.id.main_bottom)
    LinearLayout mMainBottom;
    @BindView(R.id.main_show)
    LinearLayout mMainShow;

    private boolean tag = true;
    private List<Fragment> mFragment = new ArrayList<>();

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onViewCreatedFinish(Bundle saveInstanceState) {
        String str = "123456";
//        NativeContent nativeContent = new NativeContent();
//        String s = nativeContent.stringFromJNI();
//        String s0 = nativeContent.decode(str.getBytes(), 100, 100, true, 80, 150, 100, 100);

        changeTextViewColor();
        changeSelectedTabState(0);
        initMainViewPager();

    }

    private void initMainViewPager() {
        mFragment.add(new ContactsFragment());
        mFragment.add(new ConversationFragment());
        mFragment.add(new DiscoverFragment());
        mFragment.add(new MineFragment());
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }
        };
        mMainViewpager.setAdapter(fragmentPagerAdapter);
        mMainViewpager.setOffscreenPageLimit(4);
        mMainViewpager.setOnPageChangeListener(this);
    }

    private void changeTextViewColor() {
        mTabImgChats.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_chat));
        mTabImgContact.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_contacts));
        mTabImgFind.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_found));
        mTabImgMe.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_me));
        mTabTextChats.setTextColor(Color.parseColor("#abadbb"));
        mTabTextContact.setTextColor(Color.parseColor("#abadbb"));
        mTabTextFind.setTextColor(Color.parseColor("#abadbb"));
        mTabTextMe.setTextColor(Color.parseColor("#abadbb"));
    }

    private void changeSelectedTabState(int position) {
        switch (position) {
            case 0:
                mTabTextChats.setTextColor(Color.parseColor("#0099ff"));
                mTabImgChats.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_chat_hover));
                break;
            case 1:
                mTabTextContact.setTextColor(Color.parseColor("#0099ff"));
                mTabImgContact.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_contacts_hover));
                break;
            case 2:
                mTabTextFind.setTextColor(Color.parseColor("#0099ff"));
                mTabImgFind.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_found_hover));
                break;
            case 3:
                mTabTextMe.setTextColor(Color.parseColor("#0099ff"));
                mTabImgMe.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_me_hover));
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        changeTextViewColor();
        changeSelectedTabState(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PermissionsUtils.REQUEST_CODE_SETTING:
                if (AndPermission.hasPermission(this, Manifest.permission.CAMERA)) {
                    startScan();
                } else {
                    ToastUtils.showToastShort(this, getString(R.string.permission_tips_open_camera));
                }
                break;
        }
    }

    long firstClick = 0;
    long secondClick = 0;

    @OnClick({R.id.tv_chat, R.id.seal_chat, R.id.seal_contact_list, R.id.seal_find, R.id.seal_me, R.id.seal_more, R.id.ac_iv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_chat:
                startActivity(TestActivity.class);
                break;
            case R.id.seal_chat:
                if (mMainViewpager.getCurrentItem() == 0) {
                    if (firstClick == 0) {
                        firstClick = System.currentTimeMillis();
                    } else {
                        secondClick = System.currentTimeMillis();
                    }
                    Log.i("MainActivity", "time = " + (secondClick - firstClick));
                    if (secondClick - firstClick > 0 && secondClick - firstClick <= 800) {
//                        mConversationListFragment.focusUnreadItem();
                        firstClick = 0;
                        secondClick = 0;
                    } else if (firstClick != 0 && secondClick != 0) {
                        firstClick = 0;
                        secondClick = 0;
                    }
                }
                mMainViewpager.setCurrentItem(0, false);
                break;
            case R.id.seal_contact_list:
                mMainViewpager.setCurrentItem(1, false);
                break;
            case R.id.seal_find:
                mMainViewpager.setCurrentItem(2, false);
                break;
            case R.id.seal_me:
                mMainViewpager.setCurrentItem(3, false);
                mMineRed.setVisibility(View.GONE);
                break;
            case R.id.seal_more:
                tag = true;
                apply();
                break;
            case R.id.ac_iv_search:
                tag = false;
                apply();
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra("systemconversation", false)) {
            mMainViewpager.setCurrentItem(0, false);
        }
    }

    @Override
    public void onDragOut() {
        mSealNum.setVisibility(View.GONE);
        ToastUtils.showToastShort(mContext, "清除成功");

    }

    public void apply() {
        PermissionsUtils requestPermissions = PermissionsUtils.requestPermissions(this, new PermissionsUtils.PermissionsListener() {
            @Override
            public void onRequestPermissionSuccessful(int requestCode, List<String> permissions) {
                if (AndPermission.hasPermission(mContext, Manifest.permission.CAMERA)) {
                    startScan();
                }
            }

            @Override
            public void onRequestPermissionFailure(int requestCode, List<String> deniedPermissions) {
                if (!AndPermission.hasPermission(mContext, Manifest.permission.CAMERA)) {
                    ToastUtils.showToastShort(mContext, getString(R.string.permission_tips_open_camera));
                    PermissionsUtils.defineSettingDialog(MainActivity.this, deniedPermissions);
                }
            }
        });
        requestPermissions.requestCamera(true);
    }

    private void startScan() {
        if (tag) {
            startActivity(CaptureActivity.class);
        } else {
            startActivity(ScanQrCodeActivity.class);
        }
    }
}
