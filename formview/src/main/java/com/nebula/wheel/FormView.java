/*
 * Copyright (c) 2017. pan All rights reserved.
 */

package com.nebula.wheel;

import android.content.Context;
import android.database.Observable;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by pan on 2017/3/28.
 * View can draw Form
 */
public class FormView extends View implements NestedScrollingChild {
    /**
     * Adapter
     */
    private BaseAdapter mAdapter;

    /**
     * about scroll
     */
    private float mPressX;
    private float mPressY;

    private int mContentScrollX;
    private int mContentScrollY;

    private FormParam mFormParam;
    final private FormViewDataObserver mObserver = new FormViewDataObserver();

    /**
     * nested scroll about
     */
    private NestedScrollingChildHelper mScrollChildHelper = new NestedScrollingChildHelper(this);
    private final int[] mScrollOffset = new int[2];
    private final int[] mScrollConsumed = new int[2];

    public FormView(Context context) {
        this(context, null);
    }

    public FormView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FormView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLongClickable(true);
        mFormParam = new FormParam(this);
        setNestedScrollingEnabled(true);
    }

    public void setAdapter(BaseAdapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterAdapterDataObserver(mObserver);
        }
        this.mAdapter = adapter;
        mAdapter.registerAdapterDataObserver(mObserver);

        requestLayout();
    }

    public void setCellFrameLineWidth(float width) {
        mFormParam.setCellFrameLineWidth(width);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        Log.e("tag", "on layout called");
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e("tag", "on measure called");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureCells();
    }

    @SuppressWarnings("unchecked")
    private void measureCells() {
        if (mAdapter == null) {
            return;
        }
        mFormParam.initCells(mAdapter);
        int rowCount = mAdapter.getRowCount();
        int colCount = mAdapter.getColumnCount();
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                FormCell cell = mFormParam.getCellByPosition(i, j);
                mAdapter.bindCell(cell, i, j);
            }
        }
        mFormParam.initParams(getHeight(), getWidth());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mAdapter == null) {
            return;
        }
        drawBeginColumn(canvas);
        drawFormTitle(canvas);
        drawContent(canvas);
    }


    private void drawBeginColumn(Canvas canvas) {
        float beginColumnWidth = mFormParam.mColumnWidth[0];
        float beginRowHeight = mFormParam.mRowHeight[0];
        float lineWidth = mFormParam.getCellFrameLineWidth();
        FormCell cell = mFormParam.getCellByPosition(0, 0);
        cell.drawCell(canvas);

        canvas.save();
        canvas.clipRect(0, beginRowHeight + 2 * lineWidth,
                beginColumnWidth + 2 * lineWidth, mFormParam.getFormHeight());
        canvas.translate(0, -mContentScrollY);
        for (int i = 1; i < mAdapter.getRowCount(); i++) {
            cell = mFormParam.getCellByPosition(i, 0);
            cell.drawCell(canvas);
        }
        canvas.restore();
    }

    private void drawFormTitle(Canvas canvas) {
        float lineWidth = mFormParam.getCellFrameLineWidth();
        canvas.save();
        canvas.clipRect(mFormParam.mColumnWidth[0] + 2 * lineWidth, 0,
                mFormParam.getFormWidth(), mFormParam.mRowHeight[0] + 2 * lineWidth);
        canvas.translate(-mContentScrollX, 0);

        for (int i = 1; i < mAdapter.getColumnCount(); i++) {
            FormCell cell = mFormParam.getCellByPosition(0, i);
            cell.drawCell(canvas);
        }
        canvas.restore();
    }

    private void drawContent(Canvas canvas) {
        float lineWidth = mFormParam.getCellFrameLineWidth();
        canvas.save();
        canvas.clipRect(mFormParam.mColumnWidth[0] + 2 * lineWidth, mFormParam.mRowHeight[0] + 2 * lineWidth,
                mFormParam.getFormWidth(), mFormParam.getFormHeight());
        canvas.translate(-mContentScrollX, -mContentScrollY);

        for (int i = 1; i < mAdapter.getRowCount(); i++) {
            for (int j = 1; j < mAdapter.getColumnCount(); j++) {
                FormCell cell = mFormParam.getCellByPosition(i, j);
                cell.drawCell(canvas);
            }
        }
    }

    private void scrollContentBy(int dx, int dy) {
        mContentScrollX += dx;
        mContentScrollY += dy;
    }

    private void scrollContentTo(int newX, int newY) {
        mContentScrollX = newX;
        mContentScrollY = newY;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPressX = event.getX();
                mPressY = event.getY();
                mFormParam.stateChange(mPressX, mPressY, mContentScrollX, mContentScrollY, FormCell.STATE_PRESS);
                invalidate();
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = (int) (mPressX - event.getX());
                int offsetY = (int) (mPressY - event.getY());


                if (dispatchNestedPreScroll(0, offsetY, mScrollConsumed, mScrollOffset)) {
                    offsetY -= mScrollConsumed[1];
                }
                mPressY = (int) (event.getY() - mScrollOffset[1]);
                mPressX = event.getX();

                int oldY = mContentScrollY;
                int newScrollX = mContentScrollX + offsetX;
                int newScrollY = mContentScrollY + offsetY;
                newScrollX = newScrollX < 0 ? 0 : newScrollX;
                newScrollY = newScrollY < 0 ? 0 : newScrollY;
                int limitX = mFormParam.getFormWidth() > getWidth() ?
                        (int) (mFormParam.getFormWidth() - getWidth()) : 0;
                int limitY = mFormParam.getFormHeight() > getHeight() ?
                        (int) (mFormParam.getFormHeight() - getHeight()) : 0;
                newScrollX = newScrollX > limitX ? limitX : newScrollX;
                newScrollY = newScrollY > limitY ? limitY : newScrollY;

                scrollContentTo(newScrollX, newScrollY);

                int scrollDeltaY = newScrollY - oldY;
                int unconsumedY = (offsetY - scrollDeltaY);
                if (dispatchNestedScroll(0, scrollDeltaY, 0, unconsumedY, mScrollOffset)) {
                    mPressY -= mScrollOffset[1];
                }
                invalidate();
                if (offsetX != 0 || offsetY != 0) {
                    mFormParam.checkRemove(mPressX, mPressY, mContentScrollX, mContentScrollY);
                }
                break;
            case MotionEvent.ACTION_UP:
//                Log.e("action up", event.getX() + ":" + mPressX + "|" + event.getY() + ":" + mPressY);
                if (event.getX() == mPressX && event.getY() == mPressY) {
                    mFormParam.invokeClick(mPressX, mPressY, mContentScrollX, mContentScrollY);
                    invalidate();
                }
                stopNestedScroll();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mScrollChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mScrollChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mScrollChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mScrollChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mScrollChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable @Size(value = 2) int[] offsetInWindow) {
        return mScrollChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable @Size(value = 2) int[] consumed, @Nullable @Size(value = 2) int[] offsetInWindow) {
        return mScrollChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mScrollChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mScrollChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }



    abstract static public class BaseAdapter<T extends FormCell> {
        private AdapterDataObservable mObservable = new AdapterDataObservable();

        abstract public int getRowCount();

        abstract public int getColumnCount();

        abstract public T createCell(int rowNumber, int colNumber);

        abstract public void bindCell(@NonNull T cell, int rowNumber, int colNumber);

        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            mObservable.registerObserver(observer);
        }

        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            mObservable.unregisterObserver(observer);
        }

        public void notifyDataSetChanged() {
            mObservable.notifyChanged();
        }
    }

    private static class AdapterDataObservable extends Observable<AdapterDataObserver> {
        public void notifyChanged() {
            for(int i = mObservers.size() - 1; i >= 0;i--) {
                mObservers.get(i).onChanged();
            }
        }
    }


    private class FormViewDataObserver extends AdapterDataObserver {
        @Override
        public void onChanged() {
            requestLayout();
        }
    }

    public static abstract class AdapterDataObserver{
        public void onChanged() {

        }
    }
}
