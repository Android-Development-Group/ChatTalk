package com.jusenr.chat.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Button;

import com.jusenr.chat.R;
import com.jusenr.chat.widgets.dialog.SelectDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import com.yanzhenjie.permission.SettingService;

import java.util.List;

/**
 * Description: 6.0+权限请求
 * Copyright  : Copyright (c) 2017
 * Email      : jusenr@163.com
 * Author     : Jusenr
 * Date       : 2017/04/21
 * Time       : 13:49.
 */

public class PermissionsUtils implements PermissionListener {
    public static final int REQUEST_CODE = 0x001;
    public static final int REQUEST_CODE_SETTING = 0x00100;//手机设置
    public static final int READ_EXTERNAL_STORAGE_CODE = 0x00101;//读SDCard
    public static final int WRITE_EXTERNAL_STORAGE_CODE = 0x00101;//写SDCard
    public static final int CAMERA_CODE = 0x00102;//打开相机

    public static void requestPermissions(Activity activity, int requestCode, String... permissions) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    private Context mContext;
    private Activity mActivity;
    private PermissionsListener mListener;
    private static SelectDialog mSelectDialog;

    private PermissionsUtils(Context context, PermissionsListener listener) {
        this.mContext = context;
        this.mListener = listener;
        if (context instanceof Activity) {
            this.mActivity = (Activity) context;
        }

    }

    public static PermissionsUtils requestPermissions(Context context, PermissionsListener listener) {
        return new PermissionsUtils(context, listener);
    }

    /**
     * 申请SDCard权限
     */
    public void requestSDCard() {
        AndPermission.with(mActivity)
                .requestCode(REQUEST_CODE)
                .permission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .callback(this)
                .start();
    }

    /**
     * 申请SDCard权限
     */
    public void requestSDCard(boolean isRationale) {
        AndPermission.with(mActivity)
                .requestCode(REQUEST_CODE)
                .permission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .callback(this)
                .rationale(rationaleListener)
                .start();
    }

    /**
     * 申请打开相机权限
     */
    public void requestCamera() {
        AndPermission.with(mActivity)
                .requestCode(REQUEST_CODE)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .callback(this)
                .start();
    }

    /**
     * 申请打开相机权限
     */
    public void requestCamera(boolean isRationale) {
        AndPermission.with(mActivity)
                .requestCode(REQUEST_CODE)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .callback(this)
                .rationale(rationaleListener)
                .start();
    }

    public static void requestVideo(final Context context, PermissionListener permissionListener) {
        AndPermission.with(context)
                .requestCode(REQUEST_CODE)
                .permission(Manifest.permission.MODIFY_AUDIO_SETTINGS, Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO, Manifest.permission.INTERNET)
                .callback(permissionListener)
                .callback(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
                        // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                        SelectDialog dialog = new SelectDialog.Builder(context)
                                .setMessage("权限提示")
                                .setTitle("是否打开相机和录音权限")
                                .setNegativeButton(context.getString(R.string.permission_button_item_refuse), new SelectDialog.OnNegativeClickListener() {
                                    @Override
                                    public void onClick(SelectDialog dialog, Button button) {
                                        dialog.dismiss();
                                        rationale.cancel();
                                    }
                                })
                                .setPositiveButton(context.getString(R.string.permission_button_item_agree), new SelectDialog.OnPositiveClickListener() {
                                    @Override
                                    public void onClick(SelectDialog dialog, Button button) {
                                        dialog.dismiss();
                                        rationale.resume();
                                    }
                                }).build();
                        dialog.show();
                    }
                }).start();
    }

//    @PermissionYes(REQUEST_CODE)
//    public void resultYes(List<String> permissions) {
//        this.mListener.onRequestPermissionSuccessful(REQUEST_CODE, permissions);
//    }

//    @PermissionNo(REQUEST_CODE)
//    public void resultNo(List<String> deniedPermissions) {
//        this.mListener.onRequestPermissionFailure(REQUEST_CODE, deniedPermissions);
//    }

    @Override
    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
        this.mListener.onRequestPermissionSuccessful(requestCode, grantPermissions);
    }

    @Override
    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
        this.mListener.onRequestPermissionFailure(requestCode, deniedPermissions);
    }

    public interface PermissionsListener {

        void onRequestPermissionSuccessful(int requestCode, List<String> grantPermissions);

        void onRequestPermissionFailure(int requestCode, List<String> deniedPermissions);
    }

    /**
     * Rationale支持，这里自定义对话框。
     */
    private RationaleListener rationaleListener = new RationaleListener() {
        @Override
        public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
            // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
            mSelectDialog = new SelectDialog.Builder(mContext)
                    .setMessage("友情提示")
                    .setTitle("你已拒绝过定位权限，沒有定位定位权限无法为你推荐附近的妹子，你看着办！")
                    .setNegativeButton(mContext.getString(R.string.permission_button_item_refuse), new SelectDialog.OnNegativeClickListener() {
                        @Override
                        public void onClick(SelectDialog dialog, Button button) {
                            dialog.dismiss();
                            rationale.cancel();
                        }
                    })
                    .setPositiveButton(mContext.getString(R.string.permission_button_item_agree), new SelectDialog.OnPositiveClickListener() {
                        @Override
                        public void onClick(SelectDialog dialog, Button button) {
                            dialog.dismiss();
                            rationale.resume();
                        }
                    })
                    .build();

            mSelectDialog.show();
        }
    };

    public static void defineSettingDialog(Activity activity, List<String> deniedPermissions) {
        if (AndPermission.hasAlwaysDeniedPermission(activity, deniedPermissions)) {
            final SettingService settingService = AndPermission.defineSettingDialog(activity, REQUEST_CODE_SETTING);
            mSelectDialog = new SelectDialog.Builder(activity)
                    .setMessage("提示")
                    .setTitle("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
                    .setNegativeButton("拒绝", new SelectDialog.OnNegativeClickListener() {
                        @Override
                        public void onClick(SelectDialog dialog, Button button) {
                            dialog.dismiss();
                            settingService.cancel();
                        }
                    })
                    .setPositiveButton("好，去设置", new SelectDialog.OnPositiveClickListener() {
                        @Override
                        public void onClick(SelectDialog dialog, Button button) {
                            dialog.dismiss();
                            settingService.execute();
                        }
                    })
                    .build();

            mSelectDialog.show();
        }
    }
}
