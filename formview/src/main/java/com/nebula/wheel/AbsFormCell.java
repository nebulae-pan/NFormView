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

    private Paint mLinePaint;

    private int mRow;
    private int mCol;

    protected Context mContext;

    private OnCellClickListener mOnCellClickListener;

    public AbsFormCell(Context context) {
        this.mContext = context;
        mPadding = DensityUtil.dip2Px(context, 5);
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

    void setPosition(int row, int col) {
        this.mRow = row;
        this.mCol = col;
    }

    void drawCell(Canvas canvas) {
        drawCellFrame(canvas);
        draw(canvas);
    }

    void preformClick(AbsFormCell cell) {
        mOnCellClickListener.onCellClick(cell);
    }

    private void drawCellFrame(Canvas canvas) {
        if (mRow == 0) {
            canvas.drawLine(mStartX, mStartY, mStartX + mWidth, mStartY, mLinePaint);
        }
        if (mCol == 0) {
            canvas.drawLine(mStartX, mStartY, mStartX, mStartY + mHeight, mLinePaint);
        }
        float lineWidth = mLinePaint.getStrokeWidth()/2;
        canvas.drawLine(mStartX + mWidth - lineWidth, mStartY, mStartX + mWidth - lineWidth, mStartY + mHeight, mLinePaint);
        canvas.drawLine(mStartX, mStartY + mHeight - lineWidth, mStartX + mWidth, mStartY + mHeight - lineWidth, mLinePaint);
    }

    void setLinePaint(Paint paint) {
        this.mLinePaint = paint;
    }

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

    public interface OnCellClickListener{
        void onCellClick(AbsFormCell formCell);
    }
}
