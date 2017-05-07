package com.nebula.wheel;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by pan on 2017/3/28.
 * View can draw Form
 */
public class FormView extends View implements NestedScrollingChild{
    /**
     * Adapter
     */
    private BaseAdapter mAdapter;

    /**
     * about scroll
     */
    private float mPressX;
    private float mPressY;
    private float mOffsetX;
    private float mOffsetY;

    private FormParam mFormParam;

    public FormView(Context context) {
        this(context, null);
    }

    public FormView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FormView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLongClickable(true);
        mFormParam = new FormParam();
    }

    public void setAdapter(BaseAdapter adapter) {
        this.mAdapter = adapter;
        requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.e("tag", "on layout called");
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e("tag", "on measure called");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureCells();
    }

    @SuppressWarnings("unchecked")
    private void measureCells() {
        if (mAdapter == null) {
            return;
        }
        mFormParam.initCells(mAdapter);
        int rowCount = mAdapter.getRowCount();
        int colCount = mAdapter.getColumnCount();
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                AbsFormCell cell = mFormParam.getCellByPosition(i, j);
                mAdapter.bindCell(cell, i, j);
            }
        }
        mFormParam.initParams();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mAdapter == null) {
            return;
        }
        drawBeginColumn(canvas);
        drawFormTitle(canvas);
        drawContent(canvas);
    }


    private void drawBeginColumn(Canvas canvas) {
        float beginColumnWidth = mFormParam.mColumnWidth[0];
        float beginRowHeight = mFormParam.mRowHeight[0];
        float lineWidth = mFormParam.getCellFrameLineWidth();
        AbsFormCell cell = mFormParam.getCellByPosition(0, 0);
        cell.drawCell(canvas);

        canvas.save();
        canvas.clipRect(0, beginRowHeight + 2 * lineWidth,
                beginColumnWidth + 2 * lineWidth, mFormParam.getFormHeight());
        canvas.translate(0, mOffsetY);
        for (int i = 1; i < mAdapter.getRowCount(); i++) {
            cell = mFormParam.getCellByPosition(i, 0);
            cell.drawCell(canvas);
        }
        canvas.restore();
    }

    private void drawFormTitle(Canvas canvas) {
        float lineWidth = mFormParam.getCellFrameLineWidth();
        canvas.save();
        canvas.clipRect(mFormParam.mColumnWidth[0] + 2 * lineWidth, 0,
                mFormParam.getFormWidth(), mFormParam.mRowHeight[0] + 2 * lineWidth);
        canvas.translate(mOffsetX, 0);

        for (int i = 1; i < mAdapter.getColumnCount(); i++) {
            AbsFormCell cell = mFormParam.getCellByPosition(0, i);
            cell.drawCell(canvas);
        }
        canvas.restore();
    }

    private void drawContent(Canvas canvas) {
        float lineWidth = mFormParam.getCellFrameLineWidth();
        canvas.save();
        canvas.clipRect(mFormParam.mColumnWidth[0] + 2 * lineWidth, mFormParam.mRowHeight[0] + 2 * lineWidth,
                mFormParam.getFormWidth(), mFormParam.getFormHeight());
        canvas.translate(mOffsetX, mOffsetY);

        for (int i = 1; i < mAdapter.getRowCount(); i++) {
            for (int j = 1; j < mAdapter.getColumnCount(); j++) {
                AbsFormCell cell = mFormParam.getCellByPosition(i, j);
                cell.drawCell(canvas);
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPressX = event.getX() - mOffsetX;
                mPressY = event.getY() - mOffsetY;
                mFormParam.stateChange(mPressX, mPressY, mOffsetX, mOffsetY, AbsFormCell.STATE_PRESS);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                mOffsetX = event.getX() - mPressX;
                mOffsetY = event.getY() - mPressY;
                if (mOffsetX != 0 || mOffsetY != 0) {
                    mFormParam.stateChange(mPressX, mPressY, mOffsetX, mOffsetY, AbsFormCell.STATE_NORMAL);
                }
                mOffsetX = mOffsetX > 0 ? 0 : mOffsetX;
                mOffsetY = mOffsetY > 0 ? 0 : mOffsetY;
                float limitX = -mFormParam.getFormWidth() + getWidth();
                float limitY = -mFormParam.getFormHeight() + getHeight();
                mOffsetX = mOffsetX < limitX ? limitX : mOffsetX;
                mOffsetY = mOffsetY < limitY ? limitY : mOffsetY;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                Log.e("action up", event.getX() + ":" + mPressX + "|" + event.getY() + ":" + mPressY);
                if (event.getX() == mPressX + mOffsetX && event.getY() == mPressY + mOffsetY) {
                    mFormParam.invokeClick(mPressX, mPressY, mOffsetX, mOffsetY);
                    invalidate();
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}
