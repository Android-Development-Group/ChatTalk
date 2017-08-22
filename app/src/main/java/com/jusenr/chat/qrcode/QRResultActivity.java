package com.jusenr.chat.qrcode;

import android.os.Bundle;
import android.widget.TextView;

import com.jusenr.chat.R;
import com.jusenr.chat.base.TitleActivity;
import com.putao.ptlog.PTLog;

import butterknife.BindView;

public class QRResultActivity extends TitleActivity {

    @BindView(R.id.tv_result)
    TextView mTvResult;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_qrresult);
    }

    @Override
    protected void onViewCreatedFinish(Bundle saveInstanceState) {
        super.onViewCreatedFinish(saveInstanceState);
        PTLog.i(getLocalClassName());
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String result = extras.getString("result");
            mTvResult.setText(result);
        }

    }
}
