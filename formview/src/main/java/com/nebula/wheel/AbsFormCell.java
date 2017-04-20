package com.nebula.wheel;

/**
 * Created by pan on 2017/4/17.
 */

abstract public class AbsFormCell {
    protected float mStartX;
    protected float mStartY;
    protected float mHeight;
    protected float mWidth;

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
    abstract public void drawCell();

}
