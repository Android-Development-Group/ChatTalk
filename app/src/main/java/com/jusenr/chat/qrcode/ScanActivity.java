package com.jusenr.chat.qrcode;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.jusenr.chat.R;
import com.jusenr.chat.base.TitleActivity;
import com.jusenr.chat.qrcodescan.CaptureActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class ScanActivity extends TitleActivity {

    public static final int SCAN_CODE = 1;

    @BindView(R.id.scan_button)
    Button mScanButton;
    @BindView(R.id.scan_descrpition)
    TextView mScanDescrpition;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_scan);
    }

    @Override
    protected void onViewCreatedFinish(Bundle saveInstanceState) {
        super.onViewCreatedFinish(saveInstanceState);
    }

    @Override
    public void onRightAction() {
        super.onRightAction();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SCAN_CODE:
                if (resultCode == RESULT_OK) {
                    String result = data.getStringExtra("scan_result");
                    mScanDescrpition.setText(result);
                } else if (resultCode == RESULT_CANCELED) {
                    mScanDescrpition.setText("没有扫描出结果");
                }
                break;
        }
    }

    @OnClick(R.id.scan_button)
    public void onClick() {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, SCAN_CODE);
    }
}
