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
 * View can draw Form
 */
public class FormView extends View {
    private Paint mPaint;

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

    private float padding;

    private FormParam mFormParam;

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
        mPaint.setTextSize(80);

        setLongClickable(true);
        padding = DensityUtil.dip2Px(context, 5);
        mFormParam = new FormParam();
    }

    public void setAdapter(BaseAdapter adapter) {
        this.mAdapter = adapter;
        mFormParam.initCells(adapter);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mAdapter == null) {
            return;
        }

        drawBeginColumn(canvas, mFormHeight);
        canvas.save();
        canvas.clipRect(mColumnWidth[0], 0, mFormWidth, mRowHeight[0]);
        canvas.translate(offsetX, 0);
        drawFormTitle(canvas, mFormWidth);
        canvas.restore();
        canvas.save();
        canvas.clipRect(mColumnWidth[0], mRowHeight[0], mFormWidth, mFormHeight);
        canvas.translate(offsetX, offsetY);
        drawContent(canvas, mFormHeight, mFormWidth);
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
                float limitX = -mFormWidth + getWidth();
                float limitY = -mFormHeight + getHeight();
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
