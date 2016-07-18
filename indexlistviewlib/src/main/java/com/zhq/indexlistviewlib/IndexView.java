package com.zhq.indexlistviewlib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 索引视图
 * Created by zhanghq on 2016/5/21.
 */
final class IndexView extends View {

    private Context mContext;

    private double mCharHeight;

    private String[] mIndexerChars;
    private int mVerticalSpace;
    private Paint mTextPaint;

    private int mCurrentIndex = -1;
    private double mStartPosition_Y;

    private float mTextSize;
    private int mTextColorNormal;
    private int mTextColorPressed;
    private IndexListView.IndexLayoutMode mLayoutMode;

    private OnIndexerItemClickListener mOnIndexerItemClickListener;

    public IndexView(Context context) {
        this(context, null);
    }

    public IndexView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        mTextSize = DensityUtil.spToPx(mContext, 14);
        mTextColorNormal = Color.GRAY;
        mTextColorPressed = Color.DKGRAY;
        initPaint();
        calculateFontHeight();
    }

    private void initPaint() {
        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColorNormal);
        mTextPaint.setTextSize(DensityUtil.spToPx(mContext, mTextSize));
        mTextPaint.setFakeBoldText(true);
        mTextPaint.setAntiAlias(true);
    }

    private void calculateFontHeight() {
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        mCharHeight = Math.abs(fm.bottom - fm.top);
    }

    private double getBaselineHeight() {
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        return Math.abs(fm.top);
    }

    public void setTextSize(float textSize) {
        mTextSize = textSize;
        initPaint();
        calculateFontHeight();

        requestLayout();
    }

    public void setTextColorNormal(int textColorNormal) {
        mTextColorNormal = textColorNormal;
    }

    public void setTextColorPressed(int textColorPressed) {
        mTextColorPressed = textColorPressed;
    }

    public void setLayoutMode(IndexListView.IndexLayoutMode layoutMode) {
        mLayoutMode = layoutMode;
    }

    public void setOnItemClickListener(OnIndexerItemClickListener listener) {
        mOnIndexerItemClickListener = listener;
    }

    public void setIndexerChars(String[] indexerChars) throws Exception {

        if (indexerChars == null || indexerChars.length <= 0) {
            setVisibility(GONE);
            throw new Exception("Index collection can not be empty!");
        } else {
            mIndexerChars = indexerChars;
            requestLayout();
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (mIndexerChars == null || mIndexerChars.length == 0)
            return;

        int viewHeight = getHeight();
        int viewWidth = getWidth();
        double surplusHeight = viewHeight - mCharHeight * mIndexerChars.length;
        if (surplusHeight >= 0) {
            if (IndexListView.IndexLayoutMode.LOOSE.equals(mLayoutMode)) {
                mVerticalSpace = (int) Math.ceil(surplusHeight * 1.0 / (mIndexerChars.length + 1));
            } else {
                mVerticalSpace = 0;
            }
//            int otherSpace = (int) Math.ceil((viewHeight - mCharHeight * 29) * 1.0 / (mIndexerChars.length + 1));
//            if (mVerticalSpace > otherSpace) {
//                mVerticalSpace = otherSpace;
//            }

            mStartPosition_Y = (viewHeight - mVerticalSpace * (mIndexerChars.length + 1) - mCharHeight * mIndexerChars.length) / 2 +
                    getBaselineHeight();
        } else {
            Log.e("IndexView", "Indexer height is big than view height, will hide IndexView");
            setVisibility(GONE);
            return;
        }

        for (int i = 0; i < mIndexerChars.length; i++) {
            if (i == mCurrentIndex) {
                mTextPaint.setColor(mTextColorPressed);
            } else {
                mTextPaint.setColor(mTextColorNormal);
            }
            float xPos = (viewWidth - mTextPaint.measureText(mIndexerChars[i])) / 2;
            double fontY = mStartPosition_Y + i * (mVerticalSpace + mCharHeight) + mVerticalSpace;
            canvas.drawText(mIndexerChars[i], xPos, (float) fontY, mTextPaint);
//            RectF[] rectFs = new RectF[mIndexerChars.length];
//            if (rectFs != null && rectFs.length >= mIndexerChars.length) {
//                rectFs[i] = new RectF(xPos, (float)(fontY - (mVerticalSpace + 1) / 2), xPos + paint.measureText(mIndexerChars[i]), (float)(fontY + mCharHeight + (mVerticalSpace + 1) / 2));
//            }
//            mTextPaint.reset();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mIndexerChars != null && mIndexerChars.length > 0) {
            int maxWidth = 0;
            for (String str : mIndexerChars) {
                int width = (int) (mTextPaint.measureText(str) + 0.5);

                maxWidth = Math.max(maxWidth, width);
            }
            setMeasuredDimension(MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.EXACTLY), heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getActionMasked();
        final float y = event.getY();
        int c = (int) ((y - mStartPosition_Y) / (mVerticalSpace + mCharHeight));

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (mCurrentIndex != c) {
                    if (c >= 0 && c < mIndexerChars.length) {
                        performItemPressed(c);
                        mCurrentIndex = c;
                        invalidate();
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (mCurrentIndex != c) {
                    if (c >= 0 && c < mIndexerChars.length) {
                        performItemPressed(c);
                        mCurrentIndex = c;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mCurrentIndex = -1;
                invalidate();
                performItemRelease();
                break;
        }
        return true;
    }

    private void performItemPressed(int item) {
        if (mOnIndexerItemClickListener != null) {
            mOnIndexerItemClickListener.onIndexerItemPress(mIndexerChars[item]);
        }
    }

    private void performItemRelease() {
        if (mOnIndexerItemClickListener != null) {
            mOnIndexerItemClickListener.onIndexerItemRelease();
        }
    }

    interface OnIndexerItemClickListener {
        void onIndexerItemPress(String s);

        void onIndexerItemRelease();
    }
}
