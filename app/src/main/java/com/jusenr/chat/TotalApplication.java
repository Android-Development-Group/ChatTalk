package com.jusenr.chat;


import android.content.pm.ApplicationInfo;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.jusenr.library.controller.ActivityManager;
import com.jusenr.library.controller.BaseApplication;
import com.jusenr.library.utils.AppUtils;
import com.jusenr.library.utils.Logger;
import com.jusenr.library.utils.SDCardUtils;
import com.jusenr.library.view.fresco.ImagePipelineFactory;
import com.putao.ptlog.PTLog;
import com.squareup.leakcanary.LeakCanary;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

import okhttp3.OkHttpClient;

/**
 * TotalApplication
 * Created by Jusenr on 2017/3/25.
 */

public class TotalApplication extends BaseApplication {
    private static final String TAG = "ChatApp_Log";


    @Override
    public void onCreate() {
        PTLog.init(getApplicationContext(), PTLog.DEBUG);
        super.onCreate();

        //LeakCanary初始化
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        //创建储存文件夹
        File appDir = new File(getSdCardPath());
        if (!appDir.exists()) {
            boolean isSuccess = appDir.mkdirs();
            System.out.println("Create-" + getSdCardPath() + ":===================>" + isSuccess);
        }
        //初始化Fresco
        Fresco.initialize(getApplicationContext(),
                ImagePipelineFactory.imagePipelineConfig(getApplicationContext(),
                        new OkHttpClient()));
        //初始化UMeng
        ApplicationInfo info = AppUtils.getApplicationInfo(getApplicationContext());
        String umeng_appkey = info.metaData.getString("UMENG_APPKEY");
        String umeng_channel = info.metaData.getString("UMENG_CHANNEL");
        MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(getApplicationContext(), umeng_appkey, umeng_channel);
        MobclickAgent.startWithConfigure(config);
        MobclickAgent.setDebugMode(BuildConfig.DEBUG);
        MobclickAgent.setCatchUncaughtExceptions(true);
        MobclickAgent.openActivityDurationTrack(false);

        PTLog.e("umeng_channel=%s", umeng_channel);
    }

    @Override
    protected void initEnvironment() {
        //初始化Service Api
        BaseApi.init();
    }

    @Override
    public String appDeviceId() {
        return AppUtils.getRealDeviceId(getApplicationContext());
    }

    @Override
    protected boolean isDebug() {
        return BuildConfig.IS_TEST;
    }

    @Override
    protected String getBuglyKey() {
        return "8cb683e78e";
    }

    @Override
    public String getPackageName() {
        return "com.jusenr.chat";
    }

    @Override
    protected String getLogTag() {
        return BuildConfig.LOG_TAG;
    }

    @Override
    protected String getSdCardPath() {
        return SDCardUtils.getSDCardPath() + File.separator + getLogTag();
    }

    @Override
    protected String getNetworkCacheDirectoryPath() {
        return sdCardPath + File.separator + "http_cache";
    }

    @Override
    protected int getNetworkCacheSize() {
        return 20 * 1024 * 1024;
    }

    @Override
    protected int getNetworkCacheMaxAgeTime() {
        return 0;
    }

    /**
     * 捕捉到异常就退出App
     *
     * @param ex 异常信息
     */
    @Override
    protected void onCrash(Throwable ex) {
        Logger.e("APP崩溃了,错误信息是" + ex.getMessage());
        ex.printStackTrace();
        MobclickAgent.reportError(getApplicationContext(), ex);
        MobclickAgent.onKillProcess(getApplicationContext());
        ActivityManager.getInstance().finishAllActivity();
        ActivityManager.getInstance().killProcess(getApplicationContext());
    }
}
