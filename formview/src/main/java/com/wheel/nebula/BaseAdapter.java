package com.wheel.nebula;

/**
 * Created by pan on 2017/3/28.
 */

abstract public class BaseAdapter {
    abstract public int getRowCount();

    abstract public int getColumnCount();

    abstract public String getCellContent(int rowNumber, int colNumber);
}
