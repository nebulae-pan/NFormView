package com.nebula.wheel;

import android.graphics.Paint;
import android.util.Log;

/**
 * Created by pan on 2017/4/17.
 */

class FormParam {

    private AbsFormCell[][] mCells;

    /**
     * line's width and height
     */
    float[] mColumnWidth;
    float[] mRowHeight;
    /**
     * formSize
     */
    private float mFormHeight;
    private float mFormWidth;

    private BaseAdapter mAdapter;

    private boolean isInitialized;

    private Paint mLinePaint;

    FormParam() {
        mLinePaint = new Paint();
        mLinePaint.setStrokeWidth(2);
    }

    void initCells(BaseAdapter adapter) {
        this.mAdapter = adapter;
        int rowCount = mAdapter.getRowCount();
        int colCount = mAdapter.getColumnCount();
        mCells = new AbsFormCell[rowCount][colCount];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                if (mCells[i][j] == null) {
                    mCells[i][j] = mAdapter.createCell(i, j);
                    mCells[i][j].setLinePaint(mLinePaint);
                }
            }
        }
    }

    void initParams() {
        isInitialized = true;
        int rowCount = mAdapter.getRowCount();
        int colCount = mAdapter.getColumnCount();
        mRowHeight = new float[rowCount];
        mColumnWidth = new float[colCount];
        mFormHeight = 0;
        mFormWidth = 0;
        calculateFormSize();
        float cellStartX = 0;
        float cellStartY = 0;
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                mCells[i][j].setParams(cellStartX, cellStartY, mRowHeight[i], mColumnWidth[j]);
                mCells[i][j].setPosition(i, j);
                cellStartX += mColumnWidth[j];
            }
            cellStartX = 0;
            cellStartY += mRowHeight[i];
        }
    }

    void invokeClick(float pressX, float pressY) {
        float sum = 0;
        int i = -1, j = -1;
        while (sum < pressY) {
            sum += mRowHeight[++i];
        }
        sum = 0;
        while (sum < pressX) {
            sum += mColumnWidth[++j];
        }
        Log.e("qwe", i + ":" + j);
        AbsFormCell cell = getCellByPosition(i, j);
        cell.preformClick(cell);
    }

    AbsFormCell getCellByPosition(int rowNumber, int columnNumber) {
//        checkInit();
        return mCells[rowNumber][columnNumber];
    }

    void setCellFrameLineWidth(float width) {
        mLinePaint.setStrokeWidth(width);
    }

    float getFormHeight() {
        return mFormHeight;
    }

    float getFormWidth() {
        return mFormWidth;
    }

    private void checkInit() {
        if (!isInitialized) {
            throw new RuntimeException("access parameters failed, did you forget call the \'initParams?\'");
        }
    }

    private void calculateFormSize() {
        //calculate form size
        for (int i = 0; i < mAdapter.getRowCount(); i++) {
            mRowHeight[i] = calculateRowCellMaxHeight(i);
            mFormHeight += mRowHeight[i];
        }
        for (int i = 0; i < mAdapter.getColumnCount(); i++) {
            mColumnWidth[i] = calculateColumnCellMaxWidth(i);
            mFormWidth += mColumnWidth[i];
        }
    }

    private float calculateColumnCellMaxWidth(int columnNumber) {
        float maxWidth = 0;
        for (int i = 0; i < mAdapter.getRowCount(); i++) {
            float cellWidth = mCells[i][columnNumber].calculateCellWidth();
            if (cellWidth > maxWidth) {
                maxWidth = cellWidth;
            }
        }
        return maxWidth;
    }

    private float calculateRowCellMaxHeight(int rowNumber) {
        float maxHeight = 0;
        for (int j = 0; j < mAdapter.getColumnCount(); j++) {
            float cellHeight = mCells[rowNumber][j].calculateCellHeight();
            maxHeight = cellHeight > maxHeight ? cellHeight : maxHeight;
        }
        return maxHeight;
    }
}
