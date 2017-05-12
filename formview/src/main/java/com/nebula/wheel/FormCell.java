/*
 * Copyright (c) 2017. pan All rights reserved.
 */

package com.nebula.wheel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;

import com.nebula.utils.DensityUtil;

/**
 * Created by pan on 2017/4/17.
 * Base Form Cell
 */

abstract public class FormCell {
    static final int STATE_NORMAL = 1;
    static final int STATE_PRESS = 2;
    static final int STATE_SELECTED = 3;
    protected float mStartX;
    protected float mStartY;
    protected float mHeight;
    protected float mWidth;

    protected float mPadding;

    private int mState;
    private int mCellNormalBackground;
    private int mCellPressBackground;

    private Paint mLinePaint;

    private boolean mClickable = true;

    private int mRow;
    private int mCol;

    private boolean isDrawVerticalLine = true;
    private boolean isDrawHorizontalLine = true;

    protected Context mContext;

    private OnCellClickListener mOnCellClickListener;

    public FormCell(Context context) {
        this.mContext = context;
        mState = STATE_NORMAL;
        mPadding = DensityUtil.dip2Px(context, 5);
        mCellNormalBackground = ContextCompat.getColor(context, R.color.default_background);
        mCellPressBackground = ContextCompat.getColor(context, R.color.default_press_background);
    }

    abstract public void draw(Canvas canvas);

    public void reset() {
        mStartX = 0;
        mStartY = 0;
        mHeight = 0;
        mWidth = 0;
    }

    public void setParams(float startX, float startY, float height, float width) {
        this.mStartX = startX;
        this.mStartY = startY;
        this.mHeight = height;
        this.mWidth = width;
    }

    public void setOnCellClickListener(OnCellClickListener onCellClickListener) {
        this.mOnCellClickListener = onCellClickListener;
    }

    public void setBackgroundColor(@ColorRes int resId) {
        mCellNormalBackground = ContextCompat.getColor(mContext, resId);
    }

    public void setDrawHorizontalLine(boolean isDraw) {
        this.isDrawHorizontalLine = isDraw;
    }

    public void setDrawVerticalLine(boolean isDraw) {
        this.isDrawVerticalLine = isDraw;
    }

    void setPosition(int row, int col) {
        this.mRow = row;
        this.mCol = col;
    }

    void drawCell(Canvas canvas) {
        drawCellFrame(canvas);
        drawBackground(canvas);
        draw(canvas);
    }

    void preformClick(FormCell cell) {
        if (mOnCellClickListener != null && mClickable) {
            mOnCellClickListener.onCellClick(cell);
        }
    }

    private void drawCellFrame(Canvas canvas) {
        float lineWidth = mLinePaint.getStrokeWidth() / 2;
        float startX = mStartX - lineWidth;
        float startY = mStartY - lineWidth;
        float width = mWidth + 2 * lineWidth;
        float height = mHeight + 2 * lineWidth;
        if (isDrawHorizontalLine) {
            if (mRow == 0) {
                canvas.drawLine(startX - lineWidth, startY, startX + width + lineWidth, startY, mLinePaint);
            }
            canvas.drawLine(startX, startY + height, startX + width + lineWidth, startY + height, mLinePaint);
        }
        if (isDrawVerticalLine) {
            if (mCol == 0) {
                canvas.drawLine(startX, startY, startX, startY + height + lineWidth, mLinePaint);
            }
            canvas.drawLine(startX + width, startY, startX + width, startY + height, mLinePaint);
        }

    }

    private void drawBackground(Canvas canvas) {
        canvas.save();
        canvas.clipRect(mStartX, mStartY, mStartX + mWidth, mStartY + mHeight);
        switch (mState) {
            case STATE_NORMAL:
                canvas.drawColor(mCellNormalBackground);
                break;
            case STATE_PRESS:
                canvas.drawColor(mCellPressBackground);
                break;
            default:
                break;
        }
        canvas.restore();
    }

    void stateChangeTo(int stateCode) {
        if (!mClickable) {
            return;
        }
        this.mState = stateCode;
    }

    void setLinePaint(Paint paint) {
        this.mLinePaint = paint;
    }

    public float calculateCellHeight(int rowCount, int viewHeight) {
        return 0;
    }

    public boolean isClickable() {
        return mClickable;
    }

    public void setClickable(boolean clickable) {
        this.mClickable = clickable;
    }

    public float calculateCellWidth(int colCount, int viewWidth) {
        return 0;
    }

    public float getCellHeight() {
        return mHeight;
    }

    public float getCellWidth() {
        return mWidth;
    }

    public interface OnCellClickListener {
        void onCellClick(FormCell formCell);
    }
}
