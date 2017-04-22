package com.nebula.wheel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
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
    private Paint mLinePaint;

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
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);

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
//        drawFormTitle(canvas);
//        drawContent(canvas);
    }


    private void drawBeginColumn(Canvas canvas) {
        float beginColumnWidth = mFormParam.mColumnWidth[0];
        float beginRowHeight = mFormParam.mRowHeight[0];
        float lenY = 0;
        canvas.drawLine(0, 0, 0, mFormParam.mRowHeight[0], mLinePaint);
        canvas.drawLine(0, lenY, beginColumnWidth, lenY, mLinePaint);
        AbsFormCell cell = mFormParam.getCellByPosition(0, 0);
        cell.draw(canvas);

        canvas.save();
        canvas.clipRect(0, beginRowHeight, beginColumnWidth, mFormParam.getFormHeight());
        canvas.translate(0, offsetY);

        canvas.drawLine(0, beginRowHeight, 0, mFormParam.getFormHeight(), mLinePaint);
        canvas.drawLine(0, beginRowHeight, beginColumnWidth, beginRowHeight, mLinePaint);
        lenY += beginRowHeight;
        for (int i = 1; i < mAdapter.getRowCount(); i++) {
            cell = mFormParam.getCellByPosition(i, 0);
            cell.draw(canvas);
            lenY += beginRowHeight;
            canvas.drawLine(0, lenY, beginColumnWidth, lenY, mLinePaint);
        }
        canvas.restore();
    }

//    private void drawFormTitle(Canvas canvas, float formWidth) {
//        canvas.save();
//        canvas.clipRect(mColumnWidth[0], 0, mFormWidth, mRowHeight[0]);
//        canvas.translate(offsetX, 0);
//        float lenX = mColumnWidth[0];
//        float paddingTop = padding;
//        float paddingLeft = padding;
//        float contentX = mColumnWidth[0] + paddingLeft;
//        float contentY = mLinePaint.getFontMetrics().bottom - mLinePaint.getFontMetrics().top - mLinePaint.getFontMetrics().descent + paddingTop;
//
//        canvas.drawLine(lenX, 0, formWidth, 0, mLinePaint);
//        canvas.drawLine(lenX, 0, lenX, mRowHeight[0], mLinePaint);
//        for (int i = 1; i < mAdapter.getColumnCount(); i++) {
//            canvas.drawText(mAdapter.mTitle[i], contentX, contentY, mLinePaint);
//            contentX += mColumnWidth[i];
//            lenX += mColumnWidth[i];
//            canvas.drawLine(lenX, 0, lenX, mRowHeight[0], mLinePaint);
//        }
//
//        canvas.restore();
//    }
//
//    private void drawContent(Canvas canvas, float formHeight, float formWidth) {
//        canvas.save();
//        canvas.clipRect(mColumnWidth[0], mRowHeight[0], mFormWidth, mFormHeight);
//        canvas.translate(offsetX, offsetY);
//        float lenX = mColumnWidth[0];
//        float lenY = mRowHeight[0];
//        float paddingTop = padding;
//        float paddingLeft = padding;
//        float contentX = mColumnWidth[0] + paddingLeft;
//        float contentY = mLinePaint.getFontMetrics().bottom - mLinePaint.getFontMetrics().top - mLinePaint.getFontMetrics().descent + paddingTop;
//
//        contentY += mRowHeight[0];
//        canvas.drawLine(lenX, lenY, formWidth, lenY, mLinePaint);
//        canvas.drawLine(lenX, lenY, lenX, formHeight, mLinePaint);
//
//        for (int i = 0; i < mAdapter.getRowCount(); i++) {
//            for (int j = 1; j < mAdapter.getColumnCount(); j++) {
//                canvas.drawText(mAdapter.getCellContent(i, j), contentX, contentY, mLinePaint);
//                lenX += mColumnWidth[j];
//                canvas.drawLine(lenX, lenY, lenX, lenY + mRowHeight[i + 1], mLinePaint);
//                contentX += mColumnWidth[j];
//            }
//            lenX = mColumnWidth[0];
//            lenY += mRowHeight[i + 1];
//            contentX = mColumnWidth[0] + paddingLeft;
//            contentY += mRowHeight[i + 1];
//            canvas.drawLine(mColumnWidth[0], lenY, formWidth, lenY, mLinePaint);
//        }
//    }


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
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}
