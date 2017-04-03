package com.nebula.wheel;

/**
 * Created by pan on 2017/3/28.
 */

abstract public class BaseAdapter {
    protected String[] mTitle;

    public void setTitle(String[] title) {
        this.mTitle = title;
    }

    abstract public int getRowCount();

    abstract public int getColumnCount();

    abstract public String getCellContent(int rowNumber, int colNumber);
}
