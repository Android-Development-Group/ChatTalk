package com.jusenr.chat.qrcode.camera;

/**
 * Created by riven_chris on 2016/12/14.
 */

public interface CameraQRCallback {

    void onQRStart();

    void onQRStop();

    void onQRSuccess(String result);

    void onQRFailed();
}
