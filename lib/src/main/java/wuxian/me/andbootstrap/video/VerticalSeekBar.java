package wuxian.me.andbootstrap.video;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ProgressBar;

import wuxian.me.andbootstrap.R;


/**
 * Created by wuxian on 2015/4/29.
 */
public class VerticalSeekBar extends ProgressBar {
    private Drawable mThumb;
    private int mThumbOffset;

    public VerticalSeekBar(Context context) {
        super(context);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.seekBarStyle);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MySeekBar, defStyle, 0);
        Drawable thumb = a.getDrawable(R.styleable.MySeekBar_thumb);
        setThumb(thumb);
        int thumbOffset = a.getDimensionPixelOffset(
                R.styleable.MySeekBar_thumbOffset, getThumbOffset());
        setThumbOffset(thumbOffset);
        a.recycle();

        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public int getThumbOffset() {
        return mThumbOffset;
    }

    public void setThumbOffset(int thumbOffset) {
        mThumbOffset = thumbOffset;
        invalidate();
    }

    public void setThumb(Drawable thumb) {
        boolean needUpdate;
        // This way, calling setThumb again with the same bitmap will result in
        // it recalcuating mThumbOffset (if for example it the bounds of the
        // drawable changed)
        if (mThumb != null && thumb != mThumb) {
            mThumb.setCallback(null);
            needUpdate = true;
        } else {
            needUpdate = false;
        }
        if (thumb != null) {
            thumb.setCallback(this);

            // Assuming the thumb drawable is symmetric, set the thumb offset
            // such that the thumb will hang halfway off either edge of the
            // progress bar.
            mThumbOffset = thumb.getIntrinsicWidth() / 2;//圆的中心

            // If we're updating get the new states
            if (needUpdate &&
                    (thumb.getIntrinsicWidth() != mThumb.getIntrinsicWidth()
                            || thumb.getIntrinsicHeight() != mThumb.getIntrinsicHeight())) {
                requestLayout();
            }
        }
        mThumb = thumb;
        invalidate();
        if (needUpdate) {
            updateThumbPos(getWidth(), getHeight());
            if (thumb.isStateful()) {
                // Note that if the states are different this won't work.
                // For now, let's consider that an app bug.
                int[] state = getDrawableState();
                thumb.setState(state);
            }
        }
    }

    private void updateThumbPos(int w, int h) {
        Drawable d = getProgressDrawable();
        Drawable thumb = mThumb;
        int thumbWidth = thumb == null ? 0 : thumb.getIntrinsicWidth();
        int trackWidth = w - getPaddingRight() - getPaddingLeft();

        int max = getMax();
        float scale = max > 0 ? (float) getProgress() / (float) max : 0;
        if (d != null) {
            int minWid = 6;
            d.setBounds((w - minWid) / 2, 0 + mThumbOffset + getPaddingTop(), (w - minWid) / 2 + minWid, h - getPaddingBottom()
                    - getPaddingTop() - mThumbOffset);
        }

        if (thumbWidth > trackWidth) {
            if (thumb != null) {
                setThumbPos(h, thumb, scale, 0);
            }
        } else {
            int gap = (trackWidth - thumbWidth) / 2;
            if (thumb != null) {
                setThumbPos(h, thumb, scale, gap);
            }
        }
    }

    private void setThumbPos(int h, Drawable thumb, float scale, int gap) {
        int available = h - getPaddingTop() - getPaddingBottom();
        int thumbWidth = thumb.getIntrinsicWidth();
        int thumbHeight = thumb.getIntrinsicHeight();
        available -= thumbHeight;

        //  available += mThumbOffset * 2;

        int thumbPos = (int) (scale * available);
        thumbPos = available - thumbPos;
        int leftBound, rightBound;
        if (gap == Integer.MIN_VALUE) {
            Rect oldBounds = thumb.getBounds();
            leftBound = oldBounds.left;
            rightBound = oldBounds.right;
        } else {
            leftBound = gap;
            rightBound = gap + thumbWidth;
        }
        thumb.setBounds(leftBound, thumbPos + mThumbOffset + getPaddingTop(), rightBound, thumbPos + thumbHeight + mThumbOffset);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable d = getProgressDrawable();
        int thumbWidth = mThumb == null ? 0 : mThumb.getIntrinsicWidth();
        int dw = 0;
        int dh = 0;
        if (d != null) {
            dw = Math.max(thumbWidth, d.getIntrinsicWidth());
            dh = Math.max(0, d.getIntrinsicHeight());
        }
        dw += getPaddingLeft() + getPaddingRight();
        dh += getPaddingBottom() + getPaddingTop();
        int width = resolveSize(dw, widthMeasureSpec);
        int height = resolveSize(dh, heightMeasureSpec);
        setMeasuredDimension(dw, height);
    }

    protected synchronized void onDraw(Canvas canvas) {
        updateThumbPos(getWidth(), getHeight());

        super.onDraw(canvas);
        if (mThumb != null) {
            canvas.save();
            canvas.translate(getPaddingLeft(), getPaddingTop() - mThumbOffset);
            mThumb.draw(canvas);
            canvas.restore();
        }
    }

    public void setProgress(int progress) {
        super.setProgress(progress);
        invalidate();
    }

    private boolean mIsUserSeekable = true;
    private boolean mIsDragging;
    private float mTouchDownX;
    private int mScaledTouchSlop;
    float mTouchProgressOffset = 0;

    public boolean isInScrollingContainer() {
     /*   ViewParent p = getParent();
        while (p != null && p instanceof ViewGroup) {
            if (((ViewGroup) p).shouldDelayChildPressedState()) { //fuck require api 14
                return true;
            }
            p = p.getParent();
        }
     */
        return false;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!mIsUserSeekable || !isEnabled()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isInScrollingContainer()) {
                    mTouchDownX = event.getX();
                } else {
                    setPressed(true);
                    //   if (mThumb != null) {
                    //       invalidate(mThumb.getBounds()); // This may be within the padding region
                    //   }

                    onStartTrackingTouch();
                    trackTouchEvent(event);
                    attemptClaimDrag();
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (mIsDragging) {
                    trackTouchEvent(event);
                } else {
                    final float x = event.getX();
                    if (Math.abs(x - mTouchDownX) > mScaledTouchSlop) {
                        setPressed(true);
                        //    if (mThumb != null) {
                        //        invalidate(mThumb.getBounds()); // This may be within the padding region
                        //    }
                        onStartTrackingTouch();
                        trackTouchEvent(event);
                        attemptClaimDrag();

                    }
                }
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                if (mIsDragging) {
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                    setPressed(false);
                } else {
                    // Touch up when we never crossed the touch slop threshold should
                    // be interpreted as a tap-seek to that location.
                    onStartTrackingTouch();
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                }
                // ProgressBar doesn't know to repaint the thumb drawable
                // in its inactive state when the touch stops (because the
                // value has not apparently changed)
                invalidate();
                break;

            case MotionEvent.ACTION_CANCEL:
                if (mIsDragging) {
                    onStopTrackingTouch();
                    setPressed(false);
                }
                invalidate(); // see above explanation
                break;
        }
        return true;
    }

    private void trackTouchEvent(MotionEvent event) {
        final int height = getHeight();
        final int available = height - getPaddingTop() - getPaddingBottom();
        int y = (int) event.getY();
        float scale;
        float progress = 0;
        if (y < getPaddingTop()) {
            scale = 1.0f;
        } else if (y > height - getPaddingBottom()) {
            scale = 0.0f;
        } else {
            scale = (float) (height - y) / (float) available;
            progress = mTouchProgressOffset;
        }

        final int max = getMax();
        progress += scale * max;
        setProgress((int) progress);
    }

    void onStartTrackingTouch() {
        mIsDragging = true;
        if (mOnSeekBarChangeListener != null) {
            mOnSeekBarChangeListener.onStartTrackingTouch(this);
        }
    }

    void onStopTrackingTouch() {
        mIsDragging = false;
        if (mOnSeekBarChangeListener != null) {
            mOnSeekBarChangeListener.onStopTrackingTouch(this);
        }
    }

    private void attemptClaimDrag() {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
    }

    public interface OnSeekBarChangeListener {
        void onStartTrackingTouch(VerticalSeekBar seekBar);

        void onStopTrackingTouch(VerticalSeekBar seekBar);
    }

    private OnSeekBarChangeListener mOnSeekBarChangeListener;

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
        mOnSeekBarChangeListener = l;
    }

}
