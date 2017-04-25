package com.nebula.wheel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.nebula.utils.DensityUtil;

/**
 * Created by pan on 2017/4/18.
 */

public class FormCell extends AbsFormCell {
    private String mContent;

    private Paint mPaint;

    public FormCell(Context context) {
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


    @Override
    public float calculateCellWidth() {
        return mPaint.measureText(mContent) + 2 * mPadding;
    }

    @Override
    public float calculateCellHeight() {
        return mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().top + 2 * mPadding;
    }
}
