package com.jusenr.chat.qrcode;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.alibaba.fastjson.JSONObject;
import com.jusenr.chat.R;
import com.jusenr.chat.base.TitleActivity;
import com.jusenr.chat.qrcode.camera.CameraQRCallback;
import com.jusenr.chat.qrcode.camera.CameraQRCoder;
import com.jusenr.chat.qrcode.camera.LocalQRCallback;
import com.jusenr.chat.qrcode.camera.LocalQRCoder;
import com.jusenr.chat.utils.PermissionsUtils;
import com.jusenr.chat.utils.ScanUrlParseUtils;
import com.jusenr.chatlibrary.utils.ImageUtils;
import com.jusenr.chatlibrary.utils.Logger;
import com.jusenr.chatlibrary.utils.NetworkUtils;
import com.jusenr.chatlibrary.utils.StringUtils;
import com.jusenr.chatlibrary.utils.ToastUtils;
import com.yanzhenjie.permission.AndPermission;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 扫码页面
 * Created by riven_chris on 2016/12/15.
 */
public class QRActivity extends TitleActivity {
    private static final int REQUEST_QR_FROM_ALBUM = 10002;//相册选择

    @BindView(R.id.capture_preview)
    FrameLayout scanPreview;
    @BindView(R.id.capture_crop_container)
    FrameLayout scanCropView;

    private CameraQRCoder cameraQRCoder;
    private LocalQRCoder localQRCoder;
    private Context mContext;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_qr);
    }

    @Override
    protected void onViewCreatedFinish(Bundle saveInstanceState) {
        super.onViewCreatedFinish(saveInstanceState);
        mContext = this;
        mNavigation_bar.setMainTitleColor(Color.WHITE);
        cameraQRCoder = new CameraQRCoder(this, scanPreview, scanCropView);
        cameraQRCoder.setOnQRCallback(cameraQrCallback);

    }


    @OnClick({R.id.tv_question_1})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_question_1:
                break;
        }
    }

    @Override
    public void onRightAction() {
        PermissionsUtils requestPermissions = PermissionsUtils.requestPermissions(this, new PermissionsUtils.PermissionsListener() {

            @Override
            public void onRequestPermissionSuccessful(int requestCode, List<String> grantPermissions) {
                if (AndPermission.hasPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQUEST_QR_FROM_ALBUM);
                }
            }

            @Override
            public void onRequestPermissionFailure(int requestCode, List<String> deniedPermissions) {
                if (!AndPermission.hasPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ToastUtils.showToastShort(mContext, getString(R.string.permission_tips_write_sdcard));
                    PermissionsUtils.defineSettingDialog(QRActivity.this, deniedPermissions);
                }
            }
        });
        requestPermissions.requestSDCard(true);

    }

    @Override
    public void onResume() {
        super.onResume();
        cameraQRCoder.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        cameraQRCoder.stop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_QR_FROM_ALBUM:
                if (data != null) {
                    Uri selectedImage = data.getData();
                    String picturePath = ImageUtils.getImageAbsolutePath(QRActivity.this, selectedImage);
                    if (localQRCoder == null) {
                        localQRCoder = new LocalQRCoder();
                        localQRCoder.setOnQRCallback(localQrCallback);
                    }
                    localQRCoder.startWithFile(picturePath);
                }
                break;
        }
    }

    private CameraQRCallback cameraQrCallback = new CameraQRCallback() {
        @Override
        public void onQRSuccess(String result) {

            Logger.d("#####", "cameraQrCallback-scan_result:" + result);
            processor(result);
        }

        @Override
        public void onQRFailed() {
            ToastUtils.showToastShort(QRActivity.this, "错误");
            cameraQRCoder.start();
        }

        @Override
        public void onQRStart() {
        }

        @Override
        public void onQRStop() {

        }
    };

    private LocalQRCallback localQrCallback = new LocalQRCallback() {
        @Override
        public void onQRSuccess(String result) {
            Logger.d("#####", "localQrCallback-scan_result:" + result);
            processor(result);
        }

        @Override
        public void onQRFailed() {
            ToastUtils.showToastShort(QRActivity.this, "错误");
            cameraQRCoder.start();
        }

        @Override
        public void onQRStart() {

        }

        @Override
        public void onQRStop() {
            cameraQRCoder.stop();
        }
    };

    /**
     * 处理结果
     *
     * @param scanResult 扫描结果
     */
    private void processor(final String scanResult) {
        if (scanResult == null) {
            ToastUtils.showToastShort(this, getString(R.string.qrcode_has_error));
            cameraQRCoder.start();
            return;
        }
        if (!NetworkUtils.isNetworkReachable(this)) {
            ToastUtils.showToastShort(this, getString(R.string.no_network_tips));
            cameraQRCoder.start();
            return;
        }

        String scheme = getResultScheme(scanResult);
        if (scheme == null) {
            ToastUtils.showToastShort(this, getString(R.string.qrcode_please_scan_product));
            cameraQRCoder.start();
            return;
        }

        Logger.d("#####", "scheme:" + scheme);
        switch (scheme) {
            case ScanUrlParseUtils.Scheme.PUTAO_LOGIN://扫描登录
//                mWebsitePresenter.processor(scanResult);//不使用
                showErrorInfo();
                break;
            case ScanUrlParseUtils.Scheme.HTTPS:
            case ScanUrlParseUtils.Scheme.HTTP:
                String type = ScanUrlParseUtils.getSingleParams(scanResult, "type");
                if ("chat".equalsIgnoreCase(type)) {
                    scanResult(scanResult);
                } else if ("child".equalsIgnoreCase(type)) {
                    scanResult(scanResult);
                }
                return;
        }
        showErrorInfo();
    }

    private String getResultScheme(String result) {
        String scheme = null;
        //json格式
        try {
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (jsonObject.containsKey("type")) {
                String typeValue = jsonObject.getString("type");
                if (!StringUtils.isEmpty(typeValue) && (ScanUrlParseUtils.Scheme.PICO_ACCOUNT_SERVER.equals(typeValue)
                        || ScanUrlParseUtils.Scheme.PT_BROWSER_LOGIN.equals(typeValue))) {
                    scheme = typeValue;
                    Log.e("#####", "getResultScheme: " + scheme);
                }
            }
            if (StringUtils.isEmpty(scheme)) {
                showErrorInfo();
            }
        } catch (Exception e) {
            scheme = null;
        }
        //链接格式
        if (scheme == null) {
            try {
                scheme = ScanUrlParseUtils.getScheme(result);
                if (StringUtils.isEmpty(scheme)) {
                    showErrorInfo();
                }
            } catch (Exception e) {
                e.printStackTrace();
                showErrorInfo();
                return null;
            }
        }
        return scheme;
    }

    public void scanResult(String result) {
        Intent intent = new Intent(this, QRResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("result", result);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    public void showFailed(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            ToastUtils.showToastLong(this, msg);
        } else {
            ToastUtils.showToastShort(this, getString(R.string.no_network_tips));
        }
        cameraQRCoder.start();
    }

    public void showErrorInfo() {
        ToastUtils.showToastShort(this, getString(R.string.qrcode_not_find_please_scan_product));
        cameraQRCoder.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraQRCoder != null)
            cameraQRCoder.release();
        if (localQRCoder != null)
            localQRCoder.release();
    }
}
