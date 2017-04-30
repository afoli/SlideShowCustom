package ir.thebigbang.myslideshowlibrary.viewcustom;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CastomViewPager extends ViewPager {

    private boolean swipeable = false;

    public CastomViewPager(Context context) {
        super(context);
    }

    public CastomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // Call this method in your motion events when you want to disable or enable
    // It should work as desired.
    public void setSwipeable(boolean swipeable) {
        this.swipeable = swipeable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return (this.swipeable) && super.onInterceptTouchEvent(arg0);
    }
}
