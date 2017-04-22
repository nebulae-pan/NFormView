package com.nebula.wheel;

import android.content.Context;
import android.graphics.Canvas;

import com.nebula.utils.DensityUtil;

/**
 * Created by pan on 2017/4/17.
 */

abstract public class AbsFormCell {
    protected float mStartX;
    protected float mStartY;
    protected float mHeight;
    protected float mWidth;

    protected float padding;

    protected Context mContext;

    public AbsFormCell(Context context) {
        this.mContext = context;
        padding = DensityUtil.dip2Px(context, 2);
    }

    public void reset() {
        mStartX = 0;
        mStartY = 0;
        mHeight = 0;
        mWidth = 0;
    }

    public void setParams(float startX, float startY) {
        this.mStartX = startX;
        this.mStartY = startY;
    }

    void drawCell(Canvas canvas) {
        drawCellFrame(canvas);
        draw(canvas);
    }

    private void drawCellFrame(Canvas canvas) {

    }

    abstract public void draw(Canvas canvas);

    public float calculateCellHeight() {
        return 0;
    }

    public float calculateCellWidth() {
        return 0;
    }

    public float getCellHeight() {
        return mHeight;
    }

    public float getCellWidth(){
        return mWidth;
    }
}
