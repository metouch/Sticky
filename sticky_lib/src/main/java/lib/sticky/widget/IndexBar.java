package lib.sticky.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import lib.sticky.R;
import lib.sticky.bean.IndexBean;


/**
 * TODO: document your custom view class.
 */
public class IndexBar extends View {

    private final static String TAG = "IndexBar";
    private final String INIT_DATA[] = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private final int COLOR_BLACK = 0x000000;
    private final int DEFAULT_SIZE = 16;


    private int textColor;
    private int selectedColor = Color.GREEN;
    private int textSize;
    private float textSpacing;
    private int mGravity = Gravity.CENTER_HORIZONTAL;
    private String currentItem;
    private IndexBean last;

    private TouchEventListener mListener;
    private TextPaint mPaint;
    private List<IndexBean> mData;
    private Rect rect;
    private Rect bound;
    private Paint.FontMetricsInt mMetrics;


    public IndexBar(Context context) {
        super(context);
        init(null, 0);
    }

    public IndexBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public IndexBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        mData = new ArrayList<>();
        rect = new Rect();
        bound = new Rect();
        mMetrics = new Paint.FontMetricsInt();
        if(attrs != null) {
            final TypedArray a = getContext().obtainStyledAttributes(
                    attrs, R.styleable.IndexBar, defStyle, 0);
            textSize = a.getDimensionPixelSize(R.styleable.IndexBar_textSize
                    , (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP
                    , DEFAULT_SIZE, getContext().getResources().getDisplayMetrics()));
            textColor = a.getColor(R.styleable.IndexBar_textColor, COLOR_BLACK);
            textSpacing = a.getDimension(R.styleable.IndexBar_textSpacing, 0);
            mGravity = a.getInt(R.styleable.IndexBar_gravity, Gravity.CENTER_HORIZONTAL);
            Log.e(TAG, "mGravity = " + mGravity);
            a.recycle();
        }
        invalidateTextPaintAndMeasurements();
    }

    public void setTouchEventListener(TouchEventListener touchEventListener) {
        this.mListener = touchEventListener;
    }

    public void setMData(List<IndexBean> data) {
        this.mData = data;
        if(currentItem == null && mData.size() > 0)
            currentItem = mData.get(0).firstSpell;
        invalidate();
    }

    public void setCurrentItem(String currentItem) {
        if(currentItem == null || currentItem.equals(this.currentItem)) return;
        this.currentItem = currentItem;
        invalidate();
    }

    private void invalidateTextPaintAndMeasurements() {
        mPaint = new TextPaint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(textColor);
        mPaint.setTextSize(textSize);
        mPaint.getFontMetricsInt(mMetrics);
        if(mGravity == Gravity.RIGHT || mGravity == Gravity.END){
            mPaint.setTextAlign(Paint.Align.RIGHT);
        }else if(mGravity == Gravity.LEFT || mGravity == Gravity.START){
            mPaint.setTextAlign(Paint.Align.LEFT);
        }else {
            mPaint.setTextAlign(Paint.Align.CENTER);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e(TAG, "LLLLLLLLLLLLLLLLLLLLLLLLLLLL");
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e(TAG, "MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
        String index;
        float height = 0;
        canvas.save();
        canvas.clipRect(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingTop() - getPaddingBottom());
        canvas.translate(getPaddingLeft(), getPaddingTop());
        canvas.getClipBounds(bound);
        Log.e(TAG, bound.toString());
        for(int i = 0; i < mData.size(); i ++){
            index = mData.get(i).firstSpell;
            mPaint.getTextBounds(index, 0, index.length(), rect);
            float baseline = - mMetrics.top;
            float x = mGravity == Gravity.CENTER_HORIZONTAL ? (bound.left + bound.right)/ 2
                    : mGravity == Gravity.LEFT ? bound.left : bound.right ;
            float y = height + baseline;
            Log.e(TAG, index + "," + currentItem);
            if(index.equals(currentItem)){
                mPaint.setColor(selectedColor);
            }else {
                mPaint.setColor(textColor);
            }
            canvas.drawText(index, x, y, mPaint);
            height += mMetrics.descent - mMetrics.ascent;
            height += textSpacing;
        }
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int position = calculatePosition(event.getY());
        //Log.e(TAG, "position = " + mSet.get(position));
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(position == - 1)
                    return true;
                if(mListener != null)
                    mListener.onDown(mData.get(position));
                last = mData.get(position);
                break;
            case MotionEvent.ACTION_MOVE:
                if(position == - 1)
                    return true;
                if(last != null && !mData.get(position).equals(last) && mListener != null) {
                    mListener.onChanged(last, mData.get(position));
                }
                last = mData.get(position);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if(mListener != null) {
                    if(position == -1){
                        mListener.onUp(last, null);
                    }else {
                        mListener.onUp(last, mData.get(position));
                    }
                }
            default:
                break;
        }
        return true;
    }

    private int measureWidth(int widthMeasureSpec){
        int result = 0;
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        if(wMode == MeasureSpec.EXACTLY){
            result = wSize;
        } else {
            for(int i = 0; i < mData.size(); i ++){
                int width = (int)mPaint.measureText(mData.get(i).firstSpell);
                result = Math.max(width, result);
            }
            result += (getPaddingLeft() + getPaddingRight());
            if(wMode == MeasureSpec.AT_MOST)
                return Math.min(result, wSize);
        }
        return result;
    }

    private int measureHeight(int heightMeasureSpec){
        int result = 0;
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        Log.e(TAG, "hSize = " + hSize);
        Log.e(TAG, "hMode = " + hMode);
        for(int i = 0; i < mData.size(); i ++){
            if(i == 0) {
                result += mMetrics.bottom - mMetrics.top;
            }else {
                result += mMetrics.descent - mMetrics.ascent;
            }
        }
        result += getPaddingBottom() + getPaddingTop();
        if(hMode == MeasureSpec.EXACTLY){
            if(result < hSize && mData.size() > 1) {
                Log.e(TAG, "hSize = " + hSize);
                Log.e(TAG, "result = " + result);
                textSpacing = (hSize - result) / (mData.size() - 1);
                Log.e(TAG, "textSpacing = " + textSpacing);
            }
            result = hSize;
        }else if(hMode == MeasureSpec.AT_MOST){
            result += mData.size() - 1 > 0 ? (mData.size() - 1) * textSpacing : 0;
            return Math.min(result, hSize);
        }
        return  result;
    }


    public void setTextSize(int textSize) {
        this.textSize = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEFAULT_SIZE,
                getContext().getResources().getDisplayMetrics());
        invalidate();
    }

    /**
     *
     * @param y 触摸点
     * @return position 触摸点对应的文字位置，-1表示位置不可用
     */
    private int calculatePosition(float y){
        int result = -1;
        int position = (int)Math.ceil(((y + mMetrics.top - mMetrics.ascent) / (mMetrics.descent - mMetrics.ascent + textSpacing))) - 1;
        if(position < 0 || position > mData.size() -1)
            return result;
        float baseLine = -mMetrics.top;
        baseLine += position * (mMetrics.descent - mMetrics.ascent + textSpacing);
        String index = mData.get(position).firstSpell;
        mPaint.getTextBounds(index, 0, index.length(), rect);
        float top = baseLine + rect.top ;
        float bottom = baseLine + rect.bottom;
        if(y >= top && y <= bottom){
            return position;
        }
        return result;
    }

    public interface TouchEventListener{

        /**
         * @param bean 被触摸到的字符串
         */
        void onDown(IndexBean bean);
        void onChanged(IndexBean last, IndexBean current);
        void onUp(IndexBean last, IndexBean current);
    }
}
