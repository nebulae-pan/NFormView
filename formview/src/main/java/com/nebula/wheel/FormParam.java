package com.nebula.wheel;

/**
 * Created by pan on 2017/4/17.
 */

class FormParam {
    private AbsFormCell[][] mCells;

    FormParam() {

    }

    void initCells(BaseAdapter adapter) {
        mCells = new AbsFormCell[adapter.getRowCount()][adapter.getColumnCount()];
    }

    AbsFormCell getCellByPosition(int rowNumber, int columnNumber) {
        return mCells[rowNumber][columnNumber];
    }
}
