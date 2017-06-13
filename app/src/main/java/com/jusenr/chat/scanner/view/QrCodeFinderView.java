/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.jusenr.chat.scanner.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.jusenr.chat.R;
import com.jusenr.chat.scanner.utils.ScreenUtils;

import java.lang.reflect.Field;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder rectangle and partial transparency outside
 * it, as well as the laser scanner animation and result points.
 */
public final class QrCodeFinderView extends RelativeLayout {
    public static final String TAG = QrCodeFinderView.class.getSimpleName();

    private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
    private static final long ANIMATION_DELAY = 100L;//动画延迟
    private static final int OPAQUE = 0xFF;

    private Context mContext;
    private int mScreenWidth;
    private int mScreenHeight;
    private int mViewWidth;
    private int mViewHeight;
    private int mRadius;//圆角矩形圆角半径

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

    public QrCodeFinderView(Context context) {
        this(context, null);
    }

    public QrCodeFinderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QrCodeFinderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        mContext = context;
        mPaint = new Paint();

        mFocusThick = 1;
        mAngleThick = 8;
        mAngleLength = 40;
        mScannerAlpha = 0;

        int statusBarHeight = getStatusBarHeight(context);
//        if (mScreenWidth == 0 || mScreenHeight == 0) {
//            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//            mScreenWidth = displayMetrics.widthPixels;
//            mScreenHeight = displayMetrics.heightPixels;
////            mScreenHeight = displayMetrics.heightPixels - statusBarHeight;
//        }

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QrCodeFinderView);
        mViewWidth = (int) typedArray.getDimension(R.styleable.QrCodeFinderView_innerWidth, 720f);
        mViewHeight = (int) typedArray.getDimension(R.styleable.QrCodeFinderView_innerHeight, 720f);
        mRadius = (int) typedArray.getDimension(R.styleable.QrCodeFinderView_innerRadius, 20f);
        mMaskColor = typedArray.getColor(R.styleable.QrCodeFinderView_maskColor, Color.parseColor("#80000000"));
        mFrameColor = typedArray.getColor(R.styleable.QrCodeFinderView_frameColor, Color.TRANSPARENT);
        mLaserColor = typedArray.getColor(R.styleable.QrCodeFinderView_laserColor, Color.parseColor("#37C222"));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int myWidth = -1;
        int myHeight = -1;

        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode != MeasureSpec.UNSPECIFIED) {
            myWidth = widthSize;
        }

        if (heightMode != MeasureSpec.UNSPECIFIED) {
            myHeight = heightSize;
        }

        if (widthMode == MeasureSpec.EXACTLY) {
            mScreenWidth = myWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mScreenHeight = myHeight;
        }

        if (isInEditMode()) {
            return;
        }
        // 需要调用下面的方法才会执行onDraw方法
        setWillNotDraw(false);
        mFrameRect = new Rect();
        mFrameRect.left = (mScreenWidth - mViewWidth) / 2;
        mFrameRect.top = (mScreenHeight - mViewHeight) / 2;
        mFrameRect.right = mFrameRect.left + mViewWidth;
        mFrameRect.bottom = mFrameRect.top + mViewHeight;
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
        drawBGRect(canvas, frame);
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

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    private int getStatusBarHeight(Context context) {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getViewWidth() {
        return mViewWidth;
    }

    public int getViewHeight() {
        return mViewHeight;
    }
}
