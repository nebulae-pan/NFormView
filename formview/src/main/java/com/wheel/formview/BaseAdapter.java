package com.wheel.formview;

/**
 * Created by pan on 2017/3/28.
 */

abstract public class BaseAdapter {
    abstract int getRowCount();

    abstract int getColumnCount();

    abstract String getCellContent(int rowNumber, int colNumber);



}
