package com.nebula.wheel;

/**
 * Created by pan on 2017/3/28.
 */

abstract public class BaseAdapter<T extends AbsFormCell> {

    abstract public int getRowCount();

    abstract public int getColumnCount();

    abstract public T createCell(int rowNumber, int colNumber);

    abstract public void bindCell(T cell, int rowNumber, int colNumber);
}
