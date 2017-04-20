package com.nebula.wheel;

/**
 * Created by pan on 2017/3/28.
 */

abstract public class BaseAdapter<T extends AbsFormCell> {
    protected String[] mTitle;

    public void setTitle(String[] title) {
        this.mTitle = title;
    }

    public void measure(float[] rowHeight, float[] columnWidth) {

    }

    abstract public int getRowCount();

    abstract public int getColumnCount();

    abstract public void bindCell(T cell, int rowNumber, int colNumber);

    abstract public String getCellContent(int rowNumber, int colNumber);
}
