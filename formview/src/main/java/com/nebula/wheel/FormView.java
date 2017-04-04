package com.nebula.wheel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.nebula.utils.DensityUtil;

/**
 * Created by pan on 2017/3/28.
 */

public class FormView extends View {
    private Paint mPaint;

    private BaseAdapter mAdapter;

    private float[] mColumnWidth;
    private float[] mRowHeight;

    private float pressX;
    private float pressY;
    private float offsetX;
    private float offsetY;

    private float padding;

    public FormView(Context context) {
        this(context, null);
    }

    public FormView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FormView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        setLongClickable(true);
        mPaint.setTextSize(80);
        padding = DensityUtil.dip2Px(context, 5);
    }

    public void setAdapter(BaseAdapter adapter) {
        this.mAdapter = adapter;
        mRowHeight = new float[mAdapter.getRowCount() + 1];
        mColumnWidth = new float[mAdapter.getColumnCount()];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mAdapter == null) {
            return;
        }
        float formHeight = 0;
        float formWidth = 0;
        //calculate form size
        for (int i = 0; i <= mAdapter.getRowCount(); i++) {
            mRowHeight[i] = calculateRowCellMaxHeight(i) + 2 * padding;
            formHeight += mRowHeight[i];
        }
        for (int i = 0; i < mAdapter.getColumnCount(); i++) {
            mColumnWidth[i] = calculateColumnCellMaxWidth(i) + 2 * padding;
            formWidth += mColumnWidth[i];
        }
        drawBeginColumn(canvas, formHeight);
        canvas.save();
        canvas.clipRect(mColumnWidth[0], 0, formWidth, mRowHeight[0]);
        canvas.translate(offsetX, 0);
        drawFormTitle(canvas, formWidth);
        canvas.restore();
        canvas.save();
        canvas.clipRect(mColumnWidth[0], mRowHeight[0], formWidth, formHeight);
        canvas.translate(offsetX, offsetY);
        drawContent(canvas, formHeight, formWidth);
    }

    private void drawBeginColumn(Canvas canvas, float formHeight) {
        float beginColumnWidth = mColumnWidth[0];
        float paddingTop = padding;
        float paddingLeft = padding;
        float contentX = paddingLeft;
        float contentY = mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().descent - mPaint.getFontMetrics().top + paddingTop;
        float lenY = 0;
        canvas.drawLine(0, 0, 0, mRowHeight[0], mPaint);
        canvas.drawLine(0, lenY, beginColumnWidth, lenY, mPaint);
        canvas.drawText(mAdapter.mTitle[0], contentX, contentY, mPaint);

        canvas.save();
        canvas.clipRect(0, mRowHeight[0], beginColumnWidth, formHeight);
        canvas.translate(0, offsetY);

        canvas.drawLine(0, mRowHeight[0], 0, formHeight, mPaint);
        canvas.drawLine(0, mRowHeight[0], mColumnWidth[0], mRowHeight[0], mPaint);
        lenY += mRowHeight[0];
        contentY += mRowHeight[0];
        for (int i = 0; i < mAdapter.getRowCount(); i++) {
            canvas.drawText(mAdapter.getCellContent(i, 0), contentX, contentY, mPaint);
            lenY += mRowHeight[0];
            canvas.drawLine(0, lenY, beginColumnWidth, lenY, mPaint);
            contentY += mRowHeight[0];
        }
        canvas.restore();
    }

    private void drawFormTitle(Canvas canvas, float formWidth) {
        float lenX = mColumnWidth[0];
        float paddingTop = padding;
        float paddingLeft = padding;
        float contentX = mColumnWidth[0] + paddingLeft;
        float contentY = mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().top - mPaint.getFontMetrics().descent + paddingTop;

        canvas.drawLine(lenX, 0, formWidth, 0, mPaint);
        canvas.drawLine(lenX, 0, lenX, mRowHeight[0], mPaint);
        for (int i = 1; i < mAdapter.getColumnCount(); i++) {
            canvas.drawText(mAdapter.mTitle[i], contentX, contentY, mPaint);
            contentX += mColumnWidth[i];
            lenX += mColumnWidth[i];
            canvas.drawLine(lenX, 0, lenX, mRowHeight[0], mPaint);
        }
    }

    private void drawContent(Canvas canvas, float formHeight, float formWidth) {
        float lenX = mColumnWidth[0];
        float lenY = mRowHeight[0];
        float paddingTop = padding;
        float paddingLeft = padding;
        float contentX = mColumnWidth[0] + paddingLeft;
        float contentY = mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().top - mPaint.getFontMetrics().descent + paddingTop;

        contentY += mRowHeight[0];
        canvas.drawLine(lenX, lenY, formWidth, lenY, mPaint);
        canvas.drawLine(lenX, lenY, lenX, formHeight, mPaint);

        for (int i = 0; i < mAdapter.getRowCount(); i++) {
            for (int j = 1; j < mAdapter.getColumnCount(); j++) {
                canvas.drawText(mAdapter.getCellContent(i, j), contentX, contentY, mPaint);
                lenX += mColumnWidth[j];
                canvas.drawLine(lenX, lenY, lenX, lenY + mRowHeight[i + 1], mPaint);
                contentX += mColumnWidth[j];
            }
            lenX = mColumnWidth[0];
            lenY += mRowHeight[i + 1];
            contentX = mColumnWidth[0] + paddingLeft;
            contentY += mRowHeight[i + 1];
            canvas.drawLine(mColumnWidth[0], lenY, formWidth, lenY, mPaint);
        }
    }

    private float calculateColumnCellMaxWidth(int columnNumber) {
        float maxWidth = 0;
        for (int j = 0; j < mAdapter.getRowCount(); j++) {
            float cellWidth = mPaint.measureText(mAdapter.getCellContent(j, columnNumber));
            if (cellWidth > maxWidth) {
                maxWidth = cellWidth;
            }
        }
        return maxWidth;
    }

    private float calculateRowCellMaxHeight(int rowNumber) {
        float maxHeight = 0;
        for (int i = 0; i < mAdapter.getColumnCount(); i++) {
            float cellHeight = (mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().top);
            if (cellHeight > maxHeight) {
                maxHeight = cellHeight;
            }
        }
        return maxHeight;
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
                offsetX = offsetX > 0 ? offsetX = 0 : offsetX;
                offsetY = offsetY > 0 ? offsetY = 0 : offsetY;
                invalidate();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}
