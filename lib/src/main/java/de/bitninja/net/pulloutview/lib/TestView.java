package de.bitninja.net.pulloutview.lib;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Scroller;

public class TestView extends FrameLayout {

    private int _xDelta;
    private int _yDelta;

    private int mCurrentScreen;
    private WindowManager windowsManager;

    private static final int INVALID_POINTER = -1;
    protected int mActivePointerId = INVALID_POINTER;

    private float mLastX = -1;
    private float mLastY = -1;

    private float mInitialX = -1;
    private float mInitialY = -1;

    private int mScrollX = 0;
    private int mScrollY = 0;

    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    private static final int SNAP_VELOCITY = 1000;

    private int mTouchSlop;
    private Display mScreen;

    public TestView(Context context) {
        this(context,null);
    }

    public TestView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TestView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    void init(Context context){

        windowsManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mScreen = windowsManager.getDefaultDisplay();
        mScroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        setInitialPosition(0);

    }

    public void setInitialPosition(int initialPosition){
        mCurrentScreen = initialPosition;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();

        final int action = event.getAction();

        Log.e(TestView.class.getSimpleName(), String.format("Action: %d, X: %d, Y: %d", action, X, Y));

        switch (action & MotionEventCompat.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:

                mActivePointerId = event.getAction()
                        & (Build.VERSION.SDK_INT >= 8 ? MotionEvent.ACTION_POINTER_INDEX_MASK
                        : MotionEventCompat.ACTION_POINTER_INDEX_MASK);
                mLastX = mInitialX = MotionEventCompat.getX(event, mActivePointerId);
                mLastY = mInitialY = MotionEventCompat.getY(event, mActivePointerId);

                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }

                break;

            case MotionEvent.ACTION_MOVE:

                final int pointerIndex = MotionEventCompat.findPointerIndex(event, mActivePointerId);
                if (pointerIndex == -1) {
                    mActivePointerId = INVALID_POINTER;
                    break;
                }
                final float x = MotionEventCompat.getX(event, pointerIndex);
                final float xDiff = Math.abs(x - mLastX);
                final float y = MotionEventCompat.getY(event, pointerIndex);
                final float yDiff = Math.abs(y - mLastY);
                if (xDiff > mTouchSlop && xDiff > yDiff) {
//                    mIsDragging = true;
                    mLastX = x;
                    setDrawingCacheEnabled(true);
                } else if (yDiff > mTouchSlop && yDiff > xDiff) {
//                    mIsDragging = true;
                    mLastY = y;
                    setDrawingCacheEnabled(true);
                }

                if (xDiff < 0) {
                    if (mScrollX > 0) {
                        scrollBy(Math.max(-mScrollX, (int) xDiff), 0);
                    }
                }else if (xDiff > 0) {

                    final int availableToScroll = mScrollX - getWidth();

//                    final int availableToScroll = getChildAt(getChildCount() - 1)
//                            .getRight() - mScrollX - getWidth();
                    if (availableToScroll > 0) {
                        scrollBy(Math.min(availableToScroll, (int)xDiff), 0);
                    }
                }

                break;

            case MotionEvent.ACTION_UP:

                mActivePointerId = INVALID_POINTER;

                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000);
                int velocityX = (int) velocityTracker.getXVelocity();
//                if (velocityX > SNAP_VELOCITY && mCurrentScreen > 0) {
//                    // Fling hard enough to move left
//                    snapToScreen(mCurrentScreen - 1);
//                } else if (velocityX < -SNAP_VELOCITY
//                        && mCurrentScreen < getChildCount() - 1) {
//                    // Fling hard enough to move right
//                    snapToScreen(mCurrentScreen + 1);
//                } else {
//                    snapToDestination();
//                }
//                if (mVelocityTracker != null) {
//                    mVelocityTracker.recycle();
//                    mVelocityTracker = null;
//                }


               snapToDestination();

                break;

            case MotionEvent.ACTION_CANCEL:

                mScrollX = this.getScrollX();

                break;

            case MotionEventCompat.ACTION_POINTER_DOWN:

                break;

            case MotionEventCompat.ACTION_POINTER_UP:

                break;
        }
        return true;
    }

    private void snapToDestination() {
        final int screenWidth = getWidth();
        final int whichScreen = (mScrollX + (screenWidth / 2)) / screenWidth;
        snapToScreen(whichScreen);
    }

    public void snapToScreen(int whichScreen) {
        mCurrentScreen = whichScreen;
        final int newX = whichScreen * getWidth();
        final int delta = newX - mScrollX;
        mScroller.startScroll(mScrollX, 0, delta, 0, Math.abs(delta) * 2);
        invalidate();
    }
    public void setToScreen(int whichScreen) {
        mCurrentScreen = whichScreen;
        final int newX = whichScreen * getWidth();
        mScroller.startScroll(newX, 0, 0, 0, 10);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mScrollX = mScroller.getCurrX();
            scrollTo(mScrollX, 0);
            postInvalidate();
        }
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent event) {
//
//        final int X = (int) event.getRawX();
//        final int Y = (int) event.getRawY();
//
//        Log.e(TestView.class.getSimpleName(), String.format("Action: %d, X: %d, Y: %d", event.getAction(), X, Y));
//
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
//        layoutParams.bottomMargin = -250;
//        setLayoutParams(layoutParams);
//        invalidate();
//
//
////        switch (event.getAction() & MotionEvent.ACTION_MASK) {
////            case MotionEvent.ACTION_DOWN:
////                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) getLayoutParams();
////                _xDelta = X - lParams.leftMargin;
////                _yDelta = Y - lParams.topMargin;
////                break;
////            case MotionEvent.ACTION_UP:
////                break;
////            case MotionEvent.ACTION_POINTER_DOWN:
////                break;
////            case MotionEvent.ACTION_POINTER_UP:
////                break;
////            case MotionEvent.ACTION_MOVE:
////                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
////                layoutParams.leftMargin = X - _xDelta;
////                layoutParams.topMargin = Y - _yDelta;
////                layoutParams.rightMargin = -250;
////                layoutParams.bottomMargin = -250;
////                setLayoutParams(layoutParams);
////                windowsManager.updateViewLayout(this,layoutParams);
////                break;
////        }
////        invalidate();
//        return false;
//    }
}
