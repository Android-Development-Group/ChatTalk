package com.jusenr.chat.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.jusenr.chat.R;
import com.jusenr.chat.scanner.utils.ScreenUtils;

/**
 * Description:
 * Copyright  : Copyright (c) 2017
 * Email      : jusenr@163.com
 * Author     : Jusenr
 * Date       : 2017/05/27
 * Time       : 11:06.
 */

public class ScanBoxView extends RelativeLayout {

    private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
    private static final long ANIMATION_DELAY = 100L;//动画延迟
    private static final int OPAQUE = 0xFF;

    private Context mContext;
    private Paint mPaint;
    private int mScannerAlpha;
    private int mMaskColor;
    private int mFrameColor;
    private int mLaserColor;
    private int mTextColor;
    private Rect mFrameRect;
    private int mFocusThick;
    private int mAngleThick;
    private int mAngleLength;

    public ScanBoxView(Context context) {
        this(context, null);
    }

    public ScanBoxView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScanBoxView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mPaint = new Paint();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScanBoxView);
        mMaskColor = typedArray.getColor(R.styleable.ScanBoxView_mask_color, Color.parseColor("#80000000"));
        mFrameColor = typedArray.getColor(R.styleable.ScanBoxView_frame_color, Color.parseColor("#FFFFFFFF"));
        mLaserColor = typedArray.getColor(R.styleable.ScanBoxView_laser_color, Color.parseColor("#37C221"));

        Resources resources = getResources();
        mTextColor = resources.getColor(R.color.qr_code_white);

        mFocusThick = 1;
        mAngleThick = 8;
        mAngleLength = 40;
        mScannerAlpha = 0;
        init(context);
    }

    private void init(Context context) {
        if (isInEditMode()) {
            return;
        }
        // 需要调用下面的方法才会执行onDraw方法
        setWillNotDraw(false);
        LayoutInflater inflater = LayoutInflater.from(context);
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.layout_qr_code_scanner, this);
        FrameLayout frameLayout = (FrameLayout) relativeLayout.findViewById(R.id.qr_code_fl_scanner);
        mFrameRect = new Rect();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) frameLayout.getLayoutParams();
        mFrameRect.left = (ScreenUtils.getScreenWidth(context) - layoutParams.width) / 2;
        mFrameRect.top = layoutParams.topMargin;
        mFrameRect.right = mFrameRect.left + layoutParams.width;
        mFrameRect.bottom = mFrameRect.top + layoutParams.height;

//        mFrameRect.left = 0;
//        mFrameRect.top = 0;
//        int width = this.getWidth();
//        int height = this.getHeight();
//        mFrameRect.right = width;
//        mFrameRect.bottom = height;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (isInEditMode()) {
            return;
        }
        Rect frame = mFrameRect;
        if (frame == null) {
            return;
        }
//        drawBGRect(canvas, frame);
        drawFocusRect(canvas, frame);
        drawAngle(canvas, frame);
//        drawText(canvas, frame);
//        drawLaser(canvas, frame);

        // Request another update at the animation interval, but only repaint the laser line,
        // not the entire viewfinder mask.
        postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top, frame.right, frame.bottom);
    }

    /**
     * 绘制焦点框外边的暗色背景
     *
     * @param canvas
     * @param rect
     */
    private void drawBGRect(Canvas canvas, Rect rect) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        // 画笔颜色
        mPaint.setColor(mMaskColor);
        canvas.drawRect(0, 0, width, rect.top, mPaint);
        canvas.drawRect(0, rect.top, rect.left, rect.bottom + 1, mPaint);
        canvas.drawRect(rect.right + 1, rect.top, width, rect.bottom + 1, mPaint);
        canvas.drawRect(0, rect.bottom + 1, width, height, mPaint);
    }

    /**
     * 绘制聚焦框，白色的
     *
     * @param canvas
     * @param rect
     */
    private void drawFocusRect(Canvas canvas, Rect rect) {
        // 画笔颜色
        mPaint.setColor(mFrameColor);
        // 上
        canvas.drawRect(rect.left + mAngleLength, rect.top, rect.right - mAngleLength, rect.top + mFocusThick, mPaint);
        // 左
        canvas.drawRect(rect.left, rect.top + mAngleLength, rect.left + mFocusThick, rect.bottom - mAngleLength,
                mPaint);
        // 右
        canvas.drawRect(rect.right - mFocusThick, rect.top + mAngleLength, rect.right, rect.bottom - mAngleLength,
                mPaint);
        // 下
        canvas.drawRect(rect.left + mAngleLength, rect.bottom - mFocusThick, rect.right - mAngleLength, rect.bottom,
                mPaint);
    }

    /**
     * 画带色的四个角
     *
     * @param canvas
     * @param rect
     */
    private void drawAngle(Canvas canvas, Rect rect) {
        // 画笔颜色
        mPaint.setColor(mLaserColor);
        mPaint.setAlpha(OPAQUE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(mAngleThick);
        int left = rect.left;
        int top = rect.top;
        int right = rect.right;
        int bottom = rect.bottom;
        // 左上角
        canvas.drawRect(left, top, left + mAngleLength, top + mAngleThick, mPaint);
        canvas.drawRect(left, top, left + mAngleThick, top + mAngleLength, mPaint);
        // 右上角
        canvas.drawRect(right - mAngleLength, top, right, top + mAngleThick, mPaint);
        canvas.drawRect(right - mAngleThick, top, right, top + mAngleLength, mPaint);
        // 左下角
        canvas.drawRect(left, bottom - mAngleLength, left + mAngleThick, bottom, mPaint);
        canvas.drawRect(left, bottom - mAngleThick, left + mAngleLength, bottom, mPaint);
        // 右下角
        canvas.drawRect(right - mAngleLength, bottom - mAngleThick, right, bottom, mPaint);
        canvas.drawRect(right - mAngleThick, bottom - mAngleLength, right, bottom, mPaint);
    }

    /**
     * 绘制文字
     *
     * @param canvas
     * @param rect
     */
    private void drawText(Canvas canvas, Rect rect) {
        int margin = 40;
        // 画笔颜色
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(getResources().getDimension(R.dimen.text_size_14sp));
        String text = getResources().getString(R.string.qr_code_auto_scan_notification);
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
        float offY = fontTotalHeight / 2 - fontMetrics.bottom;
        float newY = rect.bottom + margin + offY;
        float left = (ScreenUtils.getScreenWidth(mContext) - mPaint.getTextSize() * text.length()) / 2;
        canvas.drawText(text, left, newY, mPaint);
    }

    /**
     * 绘制焦点框内固定的一条扫描线
     *
     * @param canvas
     * @param rect
     */
    private void drawLaser(Canvas canvas, Rect rect) {
        // 画笔颜色
        mPaint.setColor(mLaserColor);
        mPaint.setAlpha(SCANNER_ALPHA[mScannerAlpha]);
        mScannerAlpha = (mScannerAlpha + 1) % SCANNER_ALPHA.length;
        int middle = rect.height() / 2 + rect.top;
        canvas.drawRect(rect.left + 2, middle - 1, rect.right - 1, middle + 2, mPaint);
    }
}
