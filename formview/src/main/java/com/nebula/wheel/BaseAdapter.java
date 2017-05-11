package com.nebula.wheel;

/**
 * Created by pan on 2017/3/28.
 */

abstract public class BaseAdapter{

    abstract public int getRowCount();

    abstract public int getColumnCount();

    abstract public FormCell createCell(int rowNumber, int colNumber);

    abstract public void bindCell(FormCell cell, int rowNumber, int colNumber);
}
