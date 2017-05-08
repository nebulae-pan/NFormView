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
        mLinePaint.setStrokeWidth(1);
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
        float lineWidth = mLinePaint.getStrokeWidth();
        float cellStartX = lineWidth;
        float cellStartY = lineWidth;
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                mCells[i][j].setParams(cellStartX, cellStartY, mRowHeight[i], mColumnWidth[j]);
                mCells[i][j].setPosition(i, j);
                cellStartX += mColumnWidth[j] + lineWidth;
            }
            cellStartX = lineWidth;
            cellStartY += mRowHeight[i] + lineWidth;
        }
        AbsFormCell cell = mCells[rowCount - 1][colCount - 1];
        mFormHeight = cell.mStartY + cell.mHeight + lineWidth;
        mFormWidth = cell.mStartX + cell.mWidth + lineWidth;
    }

    void stateChange(float pressX, float pressY, float offsetX, float offsetY, int stateCode) {
        AbsFormCell cell = getCellByCoordinate(pressX, pressY, offsetX, offsetY);
        cell.stateChangeTo(stateCode);
    }

    void invokeClick(float pressX, float pressY, float offsetX, float offsetY) {
        AbsFormCell cell = getCellByCoordinate(pressX, pressY, offsetX, offsetY);
        cell.preformClick(cell);
        cell.stateChangeTo(AbsFormCell.STATE_NORMAL);
    }

    private AbsFormCell getCellByCoordinate(float pressX, float pressY, float offsetX, float offsetY) {
        float lineWidth = mLinePaint.getStrokeWidth();
        float sum = lineWidth;
        int i = -1, j = -1;
        if (pressY < mRowHeight[0] + 2 * mLinePaint.getStrokeWidth()) {
            i = 0;
        } else {
            while(sum < pressY + offsetY) {
                sum += mRowHeight[++i] + lineWidth;
            }
        }
        if (pressX< mColumnWidth[0] + 2 * mLinePaint.getStrokeWidth()) {
            j = 0;
        } else {
            sum = lineWidth;
            while (sum < pressX + offsetX) {
                sum += mColumnWidth[++j] + lineWidth;
            }
        }
        Log.e("qwe", i + ":" + j);
        return getCellByPosition(i, j);
    }


    AbsFormCell getCellByPosition(int rowNumber, int columnNumber) {
//        checkInit();
        return mCells[rowNumber][columnNumber];
    }

    float getCellFrameLineWidth() {
        return mLinePaint.getStrokeWidth();
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
        }
        for (int i = 0; i < mAdapter.getColumnCount(); i++) {
            mColumnWidth[i] = calculateColumnCellMaxWidth(i);
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
