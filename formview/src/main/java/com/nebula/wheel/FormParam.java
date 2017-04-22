package com.nebula.wheel;

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

    FormParam() {
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
        calculateFormSize();
        float cellStartX = 0;
        float cellStartY = 0;
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                mCells[i][j].setParams(cellStartX, cellStartY);
                cellStartX += mColumnWidth[j];
            }
            cellStartX = 0;
            cellStartY += mRowHeight[i];
        }
    }

    AbsFormCell getCellByPosition(int rowNumber, int columnNumber) {
//        checkInit();
        return mCells[rowNumber][columnNumber];
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
