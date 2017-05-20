package com.jusenr.chatlibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.jusenr.chatlibrary.R;

/**
 * Created by Penglingxiao on 2017/4/5.
 */

public class QRScanView extends View {

    /**
     * 背景颜色
     */
    private int mBackgoundColor;
    /**
     * 背景画笔
     */
    private Paint mBgPaint;
    /**
     * 前景画笔
     */
    private Paint mViewPaint;
    /**
     * 圆角矩形圆角半径
     */
    private int mRadius = 30;
    /**
     * 是否使用图形交集模式
     */
    private boolean isPorterduffMode = true;

    private int mScreenWidth;
    private int mScreenHeight;
    private int mViewWidth;
    private int mViewHeight;
    private Bitmap mForegroundBitmap, mBackgroundBitmap;

    public QRScanView(Context context) {
        super(context);
        init(context);
    }

    public QRScanView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        init(context);
    }

    public QRScanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {

        if (mScreenWidth == 0) {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            mScreenWidth = displayMetrics.widthPixels;
            mScreenHeight = displayMetrics.heightPixels;
        }

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QRScanView);
        isPorterduffMode = typedArray.getBoolean(R.styleable.QRScanView_isPorterDuffMode, false);
        mViewWidth = (int) typedArray.getDimension(R.styleable.QRScanView_inWidth, 0f);
        mViewHeight = (int) typedArray.getDimension(R.styleable.QRScanView_inHeight, 0f);
        mRadius = (int) typedArray.getDimension(R.styleable.QRScanView_inRadius, 20f);
        mBackgoundColor = typedArray.getColor(R.styleable.QRScanView_qrBackgroundColor, Color.BLACK);
    }

    private void init(Context context) {
        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setColor(mBackgoundColor);

        mViewPaint = new Paint();
        mViewPaint.setAntiAlias(true);
        mViewPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isPorterduffMode) {
            mForegroundBitmap = makeForegroundRect();
            mBackgroundBitmap = makeMaskRect();

            Paint paint = new Paint();
            paint.setFilterBitmap(false);
            canvas.saveLayer(0, 0, mScreenWidth, mScreenHeight, null, Canvas.MATRIX_SAVE_FLAG |
                    Canvas.CLIP_SAVE_FLAG |
                    Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                    Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                    Canvas.CLIP_TO_LAYER_SAVE_FLAG);

            canvas.drawBitmap(mForegroundBitmap, 0, 0, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
            paint.setAlpha(130);
            canvas.drawBitmap(mBackgroundBitmap, 0, 0, paint);
            paint.setXfermode(null);

        } else {
            super.onDraw(canvas);
        }
    }

    /**
     * 创建前景镂空层
     *
     * @return
     */
    private Bitmap makeForegroundRect() {
        Bitmap bitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);

        mViewWidth = Math.min(mViewWidth, mViewHeight);
        if (mViewWidth >= mScreenWidth || mViewWidth == 0) {
            mViewWidth = mScreenWidth - mScreenWidth * 2 / 5;
        }
        int left = mScreenWidth / 2 - mViewWidth / 2;
        int top = mScreenHeight / 2 - mViewWidth / 2 - 100;
        int right = mScreenWidth / 2 + mViewWidth / 2;
        int bottom = mScreenHeight / 2 + mViewWidth / 2 - 100;

        canvas.drawRoundRect(new RectF(left, top, right, bottom), mRadius, mRadius, mViewPaint);
        return bitmap;
    }

    /**
     * 创建蒙板层
     *
     * @return
     */
    private Bitmap makeMaskRect() {
        Bitmap bitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ALPHA_8);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0, 0, mScreenWidth, mScreenHeight, mBgPaint);
        return bitmap;
    }
}
