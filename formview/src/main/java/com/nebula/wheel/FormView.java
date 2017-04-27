package com.nebula.wheel;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by pan on 2017/3/28.
 * View can draw Form
 */
public class FormView extends View {
    /**
     * Adapter
     */
    private BaseAdapter mAdapter;

    /**
     * about scroll
     */
    private float pressX;
    private float pressY;
    private float offsetX;
    private float offsetY;

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
        AbsFormCell cell = mFormParam.getCellByPosition(0, 0);
        cell.drawCell(canvas);

        canvas.save();
        canvas.clipRect(0, beginRowHeight,
                beginColumnWidth, mFormParam.getFormHeight());
        canvas.translate(0, offsetY);
        for (int i = 1; i < mAdapter.getRowCount(); i++) {
            cell = mFormParam.getCellByPosition(i, 0);
            cell.drawCell(canvas);
        }
        canvas.restore();
    }

    private void drawFormTitle(Canvas canvas) {
        canvas.save();
        canvas.clipRect(mFormParam.mColumnWidth[0], 0,
                mFormParam.getFormWidth(), mFormParam.mRowHeight[0]);
        canvas.translate(offsetX, 0);

        for (int i = 1; i < mAdapter.getColumnCount(); i++) {
            AbsFormCell cell = mFormParam.getCellByPosition(0, i);
            cell.drawCell(canvas);
        }
        canvas.restore();
    }

    private void drawContent(Canvas canvas) {
        canvas.save();
        canvas.clipRect(mFormParam.mColumnWidth[0], mFormParam.mRowHeight[0],
                mFormParam.getFormWidth(), mFormParam.getFormHeight());
        canvas.translate(offsetX, offsetY);

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
                pressX = event.getX() - offsetX;
                pressY = event.getY() - offsetY;
                break;
            case MotionEvent.ACTION_MOVE:
                offsetX = event.getX() - pressX;
                offsetY = event.getY() - pressY;
                offsetX = offsetX > 0 ? 0 : offsetX;
                offsetY = offsetY > 0 ? 0 : offsetY;
                float limitX = -mFormParam.getFormWidth() + getWidth();
                float limitY = -mFormParam.getFormHeight() + getHeight();
                offsetX = offsetX < limitX ? limitX : offsetX;
                offsetY = offsetY < limitY ? limitY : offsetY;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                Log.e("action up", event.getX() + ":" + pressX + "|" + event.getY() + ":" + pressY);
                if (event.getX() == pressX + offsetX && event.getY() == pressY + offsetY) {
                    mFormParam.invokeClick(pressX, pressY);
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}
