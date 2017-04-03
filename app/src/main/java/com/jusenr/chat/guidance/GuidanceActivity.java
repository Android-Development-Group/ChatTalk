//package com.jusenr.chat.guidance;
//
//import android.animation.ObjectAnimator;
//import android.os.Bundle;
//import android.os.PersistableBundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.WindowManager;
//import android.view.animation.Animation;
//import android.view.animation.BounceInterpolator;
//import android.view.animation.TranslateAnimation;
//import android.widget.Button;
//
//import com.myself.library.controller.ActivityManager;
//import com.myself.library.utils.DensityUtils;
//import com.myself.library.view.viewpager.banner.ConvenientBanner;
//import com.myself.library.view.viewpager.banner.holder.CBViewHolderCreator;
//import com.myself.newsapp.R;
//import com.myself.newsapp.guidance.view.GuidancePictureHolderView;
//import com.myself.newsapp.user.LoginActivity;
//import com.myself.newsapp.user.RegisterActivity;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//
///**
// * 引导页
// * Created by Jusenr on 2017/03/25.
// */
//public class GuidanceActivity extends AppCompatActivity implements View.OnClickListener {
//    private ObjectAnimator waveShiftAnim;//波浪动画
//
////    private Subscription upgradeSubscription;
//
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_guidance);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
////        upgradeSubscription = UpgradeHelper.checkUpgradeInfo(this);
//        initAnimation();
//        initGuidanceBanner();
//        ActivityManager.getInstance().popOtherActivity(GuidanceActivity.class);
//
//    }
//
//    private void initGuidanceBanner() {
//        List<GuidancePicture> banners = new ArrayList<GuidancePicture>();
//        banners.add(new GuidancePicture(R.drawable.img_wt_bg_01,
//                getString(R.string.guidance_title_1),
//                getString(R.string.guidance_label_1)));
//        banners.add(new GuidancePicture(R.drawable.img_wt_bg_02,
//                getString(R.string.guidance_title_2),
//                getString(R.string.guidance_label_2)));
//        banners.add(new GuidancePicture(R.drawable.img_wt_bg_03,
//                getString(R.string.guidance_title_3),
//                getString(R.string.guidance_label_3)));
//
//        cb_guidanceBanner.setPageIndicatorMarginBottom(DensityUtils.dp2px(this, 120));
//        cb_guidanceBanner.setPages(new CBViewHolderCreator() {
//            @Override
//            public Object createHolder() {
//                return new GuidancePictureHolderView();
//            }
//        }, banners);
//    }
//
//    /**
//     * 初始化动画
//     */
//    private void initAnimation() {
////        waveShiftAnim = ObjectAnimator.ofFloat(wv_wave, "waveShiftRatio", 1f, 0f);
////        waveShiftAnim.setRepeatCount(ValueAnimator.INFINITE);
////        waveShiftAnim.setDuration(3500);
////        waveShiftAnim.setInterpolator(new LinearInterpolator());
//
//        TranslateAnimation btn_loginAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
//                Animation.RELATIVE_TO_SELF, 0.0f,
//                Animation.RELATIVE_TO_PARENT, 1.0f,
//                Animation.RELATIVE_TO_SELF, -0.3f);
//        btn_loginAnim.setInterpolator(new BounceInterpolator());
//        btn_loginAnim.setDuration(1000);
//        btn_loginAnim.setStartOffset(1000);
//        btn_login.setAnimation(btn_loginAnim);
//
//        TranslateAnimation btn_regAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
//                Animation.RELATIVE_TO_SELF, 0.0f,
//                Animation.RELATIVE_TO_PARENT, 1.0f,
//                Animation.RELATIVE_TO_SELF, -0.3f);
//        btn_regAnim.setInterpolator(new BounceInterpolator());
//        btn_regAnim.setDuration(1000);
//        btn_register.setAnimation(btn_regAnim);
//    }
//
//
//    @OnClick({R.id.btn_login, R.id.btn_register})
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_login:
//                startActivity(LoginActivity.class);
//                break;
//            case R.id.btn_register:
//                startActivity(RegisterActivity.class);
//                break;
//        }
//    }
//
//
//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//            return exit();
//        }
//        return super.dispatchKeyEvent(event);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        cb_guidanceBanner.startTurning(3000);
////        wv_wave.setShowWave(true);
//        if (waveShiftAnim != null) {
//            waveShiftAnim.start();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        cb_guidanceBanner.stopTurning();
//        if (waveShiftAnim != null) {
//            waveShiftAnim.end();
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
////        if (upgradeSubscription != null)
////            upgradeSubscription.unsubscribe();
//    }
//}