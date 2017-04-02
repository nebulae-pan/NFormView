package com.wheel.nebula;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by pan on 2017/3/28.
 */

public class FormView extends View {
    private Paint mPaint;

    private BaseAdapter mAdapter;

    private ArrayList<Float> mColumnWidth;

    private ArrayList<Float> mRowHeight;

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
        mPaint.setTextSize(55);
        mColumnWidth = new ArrayList<>();
        mRowHeight = new ArrayList<>();
    }

    public void setAdapter(BaseAdapter adapter) {
        this.mAdapter = adapter;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawGrid(canvas);
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

    private void drawGrid(Canvas canvas) {
        if (mAdapter == null) {
            return;
        }
        float formHeight = 0;
        //calculate form height
        for (int i = 0; i <= mAdapter.getRowCount(); i++) {
            mRowHeight.add(i, calculateRowCellMaxHeight(i));
            formHeight += mRowHeight.get(i);
        }
        float formWidth = drawColumnLineAndGetWidth(canvas, formHeight);
        drawContentAndRowLine(canvas, formWidth);

    }

    private float drawColumnLineAndGetWidth(Canvas canvas, float formHeight) {
        float lineWidth;
        float lineStartX = 0;
        //draw column line
        canvas.drawLine(lineStartX, 0, lineStartX, formHeight, mPaint);
        for (int i = 0; i < mAdapter.getColumnCount(); i++) {
            lineWidth = calculateColumnCellMaxWidth(i);
            mColumnWidth.add(i, lineWidth);
            lineStartX += lineWidth;
            canvas.drawLine(lineStartX, 0, lineStartX, formHeight, mPaint);
        }
        return lineStartX;
    }

    private void drawContentAndRowLine(Canvas canvas, float formWidth) {
        float textX = 0;
        float textY = mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().descent - mPaint.getFontMetrics().top;
        float lenY = 0;

        //draw content and row line
        canvas.drawLine(0, lenY, formWidth, lenY, mPaint);
        for(int i = 0; i < mAdapter.getColumnCount(); i++) {
            canvas.drawText(mAdapter.mTitle[i], textX, textY, mPaint);
            textX += mColumnWidth.get(i);
        }
        textX = 0;
        textY += mRowHeight.get(0);
        lenY += mRowHeight.get(0);
        canvas.drawLine(0, lenY, formWidth, lenY, mPaint);

        for (int i = 0; i < mAdapter.getRowCount(); i++) {
            for (int j = 0; j < mAdapter.getColumnCount(); j++) {
                canvas.drawText(mAdapter.getCellContent(i, j), textX, textY, mPaint);
                textX += mColumnWidth.get(j);
            }
            textX = 0;
            textY += mRowHeight.get(i);
            lenY += mRowHeight.get(i);
            canvas.drawLine(0, lenY, formWidth, lenY, mPaint);
        }
    }
}
