/*
 * Copyright (c) 2017. pan All rights reserved.
 */

package com.nebula.wheel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.nebula.utils.DensityUtil;

/**
 * Created by pan on 2017/4/18.
 */

public class StringFormCell extends FormCell {
    private String mContent;

    private Paint mPaint;

    public StringFormCell(Context context) {
        super(context);
        this.mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(DensityUtil.dip2Px(context, 25));
    }

    public void setContent(String content) {
        this.mContent = content;
    }

    @Override
    public void draw(Canvas canvas) {
        float contentX = mStartX + mPadding;
        float contentY = mStartY + mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().top - mPaint.getFontMetrics().descent + mPadding;
        canvas.drawText(mContent, contentX, contentY, mPaint);
    }

    public String getContent() {
        return mContent;
    }

    @Override
    public float calculateCellWidth(int remainColCount, int remainViewWidth) {
        return mPaint.measureText(mContent) + 2 * mPadding;
    }

    @Override
    public float calculateCellHeight(int remainRowCount, int remainViewHeight) {
        return mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().top + 2 * mPadding;
    }
}
