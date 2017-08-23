package com.jusenr.chat.guidance;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jusenr.chat.MainActivity;
import com.jusenr.chat.R;
import com.jusenr.chat.account.AccountHelper;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

//import com.jusenr.chat.guidance.GuidanceActivity;

/**
 * 闪屏页面
 * Created by Jusenr on 2017/3/25.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Observable.timer(3000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long time) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        if (!AccountHelper.isLogin()) {
//                    startActivity(new Intent(SplashActivity.this, GuidanceActivity.class));
                        } else {
                            AccountHelper.login();
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        }
                        finish();
                    }
                });
    }
}
