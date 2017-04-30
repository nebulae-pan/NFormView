package com.nebula.wheel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.nebula.utils.DensityUtil;

/**
 * Created by pan on 2017/4/17.
 */

abstract public class AbsFormCell {
    static final int STATE_NORMAL = 1;
    static final int STATE_PRESS = 2;
    protected float mStartX;
    protected float mStartY;
    protected float mHeight;
    protected float mWidth;

    protected float mPadding;

    private int mState;

    private Paint mLinePaint;

    private int mRow;
    private int mCol;

    protected Context mContext;

    private OnCellClickListener mOnCellClickListener;

    public AbsFormCell(Context context) {
        this.mContext = context;
        mState = STATE_NORMAL;
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
        drawBackground(canvas);
        draw(canvas);
    }

    void preformClick(AbsFormCell cell) {
        if (mOnCellClickListener != null) {
            mOnCellClickListener.onCellClick(cell);
        }
    }

    private void drawCellFrame(Canvas canvas) {
        float lineWidth = mLinePaint.getStrokeWidth() / 2;
        float startX = mStartX - lineWidth;
        float startY = mStartY - lineWidth;
        float width = mWidth + 2 * lineWidth;
        float height = mHeight + 2 * lineWidth;
        if (mRow == 0) {
            canvas.drawLine(startX - lineWidth, startY, startX + width + lineWidth, startY, mLinePaint);
        }
        if (mCol == 0) {
            canvas.drawLine(startX, startY, startX, startY + height + lineWidth, mLinePaint);
        }
        canvas.drawLine(startX + width, startY, startX + width, startY + height, mLinePaint);
        canvas.drawLine(startX, startY + height, startX + width + lineWidth, startY + height, mLinePaint);
    }

    private void drawBackground(Canvas canvas) {
        canvas.save();
        switch (mState) {
            case STATE_NORMAL:
                canvas.clipRect(mStartX, mStartY, mStartX + mWidth, mStartY + mHeight);
                canvas.drawColor(0xffffffff);
                break;
            case STATE_PRESS:
                canvas.clipRect(mStartX, mStartY, mStartX + mWidth, mStartY + mHeight);
                canvas.drawColor(0x66cccccc);
                break;
            default:
                break;
        }
        canvas.restore();
    }

    void stateChangeTo(int stateCode) {
        this.mState = stateCode;
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

    public interface OnCellClickListener {
        void onCellClick(AbsFormCell formCell);
    }
}
