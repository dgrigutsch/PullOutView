package de.bitninja.net.pulloutview.lib;

import android.content.Context;
import android.content.res.TypedArray;
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

    private int windowH = 0, windowW = 0;

    private boolean isDragging = false;

    private WindowManager windowsManager;

    private static final int INVALID_POINTER = -1;
    protected int mActivePointerId = INVALID_POINTER;

    private float mLastX = -1;
    private float mLastY = -1;

    private float mInitialX = -1;
    private float mInitialY = -1;

    private int mScrollX = 0;
    private int mScrollY = 0;

    private int mLastTopMargin = 0;

    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    private int mTouchSlop;
    private Display mScreen;
    private boolean mIsOpen = false;
    private int mMinHeight;
    private int mOffsetTop;

    private AttributeSet mAttrs;

    public TestView(Context context) {
        this(context,null);
    }

    public TestView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TestView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Style
        final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PullOutAttrs);
        // How much of the view sticks out when closed
        mMinHeight = ta.getDimensionPixelOffset(R.styleable.PullOutAttrs_pov_minHeight, 0);
        mOffsetTop = ta.getDimensionPixelOffset(R.styleable.PullOutAttrs_pov_offsetTop, 0);
        ta.recycle();

        mAttrs = attrs;

        init(context);
    }

    void init(Context context){

        windowsManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mScreen = windowsManager.getDefaultDisplay();
        mScroller = new Scroller(context);

        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        final float density = context.getResources().getDisplayMetrics().density;

        LayoutParams params = (LayoutParams)getLayoutParams();
        if(params == null)
            params = generateLayoutParams(mAttrs);
        params.height = mMinHeight;
        params.setMargins(0,mOffsetTop,0,0);
        setLayoutParams(params);
        invalidate();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
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

                if(xDiff > 5 || yDiff > 5)
                    isDragging = true;

                Log.e(TestView.class.getSimpleName(), String.format("xDiff: %d ", (int) xDiff));

                if(y<mLastY){
                    mLastTopMargin-=yDiff;
                }else {
                    mLastTopMargin+=yDiff;
                }

                MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
                params.setMargins(0,mLastTopMargin,0,0);
                setLayoutParams(params);

                break;

            case MotionEvent.ACTION_UP:

                if(isDragging){
                    isDragging = false;
                } else if (mIsOpen) {
                    closeLayer();
                } else if (!mIsOpen) {
                    openLayer();
                }
                mActivePointerId = INVALID_POINTER;

                break;

            case MotionEvent.ACTION_CANCEL:

                break;

            case MotionEventCompat.ACTION_POINTER_DOWN:

                mActivePointerId = event.getAction()
                        & (Build.VERSION.SDK_INT >= 8 ? MotionEvent.ACTION_POINTER_INDEX_MASK
                        : MotionEventCompat.ACTION_POINTER_INDEX_MASK);
                mLastX = mInitialX = MotionEventCompat.getX(event, mActivePointerId);
                mLastY = mInitialY = MotionEventCompat.getY(event, mActivePointerId);

                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }

                break;

            case MotionEventCompat.ACTION_POINTER_UP:

                break;
        }
        return true;
    }

    private void openLayer(){

        MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
        params.height = LayoutParams.MATCH_PARENT;
        params.setMargins(0,mOffsetTop,0,0);
        setLayoutParams(params);

        mIsOpen = true;
    }
    private void closeLayer(){

        MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();

        if(params == null)
            params = generateLayoutParams(mAttrs);

        int dist = 0;

//        ViewGroup parentView = (ViewGroup)getParent();

//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
//            Point size = new Point();
//            mScreen.getSize(size);
//            dist = size.y;
//        }else {
//            dist = mScreen.getHeight();
//        }

//        dist = parentView.getHeight();

        Log.e(TestView.class.getSimpleName(), String.format("DIST: %d ", dist));

//        params.setMargins(0, 600 - mMinHeight, 0, 0);
//        params.height = 10;
        params.height = mMinHeight;
        setLayoutParams(params);
        mIsOpen = false;
    }



    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mScrollX = mScroller.getCurrX();
            scrollTo(mScrollX, 0);
            postInvalidate();
        }
    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        windowW = getMeasuredWidth();
        windowH = getMeasuredHeight();
    }
}
