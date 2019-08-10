package com.dohman.captionsscanner.controller;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

// Custom ViewPager class to disable swiping when needed
public class LockableViewPager extends ViewPager {

    private boolean enabled;

    public LockableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return enabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return enabled && super.onInterceptTouchEvent(event);
    }

    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isPagingEnabled() {
        return enabled;
    }
}
