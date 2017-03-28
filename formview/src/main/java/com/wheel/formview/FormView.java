package com.wheel.formview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by pan on 2017/3/28.
 */

public class FormView extends View {
    private Paint mPaint;

    private BaseAdapter mAdapter;

    public FormView(Context context) {
        this(context, null);
    }

    public FormView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FormView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint.setTextSize(15);
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
        for (int j = 0; j <= mAdapter.getRowCount(); j++) {
            //need row count + 1(form title)
            float cellWidth = mPaint.measureText(mAdapter.getCellContent(j, columnNumber));
            if (cellWidth > maxWidth) {
                maxWidth = cellWidth;
            }
        }
        return maxWidth;
    }

    private void drawGrid(Canvas canvas) {
        float lineWidth;
        for(int i = 0; i < mAdapter.getColumnCount(); i++) {
            lineWidth = calculateColumnCellMaxWidth(i);

        }

    }
}
