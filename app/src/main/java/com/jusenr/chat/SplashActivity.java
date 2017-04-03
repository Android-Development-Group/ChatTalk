package com.jusenr.chat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                if (!AccountHelper.isLogin()) {
//                    startActivity(new Intent(SplashActivity.this, GuidanceActivity.class));
//                } else {
//                    AccountHelper.login();
//                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                }
                finish();
            }
        }, 2000);
    }
}
