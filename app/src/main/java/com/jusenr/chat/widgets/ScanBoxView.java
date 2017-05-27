package com.jusenr.chat.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.jusenr.chat.R;

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
        // 需要调用下面的方法才会执行onDraw方法
        setWillNotDraw(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int myWidth = -1;
        int myHeight = -1;

        int width = 0;
        int height = 0;

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
            width = myWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = myHeight;
        }

        mFrameRect = new Rect();
        mFrameRect.left = 0;
        mFrameRect.top = 0;
        mFrameRect.right = mFrameRect.left + width;
        mFrameRect.bottom = mFrameRect.top + height;
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
        drawFocusRect(canvas, frame);//绘制白色的聚焦边框
        drawAngle(canvas, frame);//绘制带色的四个角
    }

    /**
     * 绘制白色的聚焦边框
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
     * 绘制带色的四个角
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
}
