package com.jusenr.chat.widgets.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.jusenr.chat.R;
import com.jusenr.chatlibrary.view.CleanableEditText;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ZSW on 17/3/29.
 */

public class SingleInputDialog extends BasicDialog {

    @BindView(R.id.et_nickname)
    CleanableEditText etNickname;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_save)
    Button btnSave;
    private Context mContext;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_single_input;
    }

    public SingleInputDialog(Context context) {
        super(context);
        mContext = context;
    }

    @OnClick({R.id.btn_cancel, R.id.btn_save})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                onCancelClick(v);
                break;
            case R.id.btn_save:
                onSaveClick(v);
                break;
        }
    }

    public String getInputText() {
        return etNickname.getText().toString();
    }

    public void setInputText(String text) {
        if (!TextUtils.isEmpty(text)) {
            etNickname.setText(text);
            Editable etext = etNickname.getText();
            Selection.setSelection(etext, etext.length());
        }
    }

    public void onCancelClick(View v) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etNickname.getWindowToken(), 0);
    }

    public void onSaveClick(View v) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etNickname.getWindowToken(), 0);
    }

}
