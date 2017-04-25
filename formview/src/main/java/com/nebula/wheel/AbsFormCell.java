package com.nebula.wheel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.nebula.utils.DensityUtil;

/**
 * Created by pan on 2017/4/17.
 */

abstract public class AbsFormCell {
    protected float mStartX;
    protected float mStartY;
    protected float mHeight;
    protected float mWidth;

    protected float mPadding;

    private int mRow;
    private int mCol;

    protected Context mContext;

    private Paint mLinePaint;

    public AbsFormCell(Context context) {
        this.mContext = context;
        mPadding = DensityUtil.dip2Px(context, 5);
        mLinePaint = new Paint();
//        mLinePaint.setStrokeWidth(DensityUtil.dip2Px(context, 1));
    }


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

    void setPosition(int row, int col) {
        this.mRow = row;
        this.mCol = col;
    }

    void drawCell(Canvas canvas) {
        drawCellFrame(canvas);
        draw(canvas);
    }

    float getLineWidth() {
        return mLinePaint.getStrokeWidth();
    }

    private void drawCellFrame(Canvas canvas) {
        if (mRow == 0) {
            canvas.drawLine(mStartX, mStartY, mStartX + mWidth, mStartY, mLinePaint);
        }
        if (mCol == 0) {
            canvas.drawLine(mStartX, mStartY, mStartX, mStartY + mHeight, mLinePaint);
        }
        canvas.drawLine(mStartX + mWidth, mStartY, mStartX + mWidth, mStartY + mHeight, mLinePaint);
        canvas.drawLine(mStartX, mStartY + mHeight, mStartX + mWidth, mStartY + mHeight, mLinePaint);
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

    public float getCellWidth() {
        return mWidth;
    }
}
