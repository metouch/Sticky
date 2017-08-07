package lib.sticky.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import java.util.List;

import lib.sticky.bean.FullEntity;
import lib.sticky.spell.IWord2Spell;


/**
 * Created by me_touch on 2017/6/28.
 *
 */

public class StickyTitleDecoration<T extends IWord2Spell> extends RecyclerView.ItemDecoration{

    private String TAG = "StickyTitleDecoration";
    private int INVALID_POSITION = -1;

    private Context context;
    private DisplayMetrics displayMetrics;
    private Paint mPaint;
    private Paint.FontMetrics metrics;
    private Rect mRect;
    private OnUpdateTitleListener mListener;
    private int textColor = 0xffffffff; //必须是32位
    private int bgColor = 0xff999999;
    private int height = 40;
    private int textSize = 14;
    private int paddingLeft = 0; //unit:dp
    private List<FullEntity<T>> mSet;

    public StickyTitleDecoration(Context context, List<FullEntity<T>> mSet){
        this.context = context;
        this.mSet = mSet;
        displayMetrics = context.getResources().getDisplayMetrics();
        metrics = new Paint.FontMetrics();
        textSize = pxConversion(textSize, TypedValue.COMPLEX_UNIT_SP);
        height = pxConversion(height, TypedValue.COMPLEX_UNIT_DIP);
        initPaint();
        mRect = new Rect();
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        updatePaint();
    }

    public void setHeight(int height) {
        this.height = pxConversion(height, TypedValue.COMPLEX_UNIT_DIP);
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
        updatePaint();
    }

    public void setTextSize(int textSize){
        this.textSize = pxConversion(textSize, TypedValue.COMPLEX_UNIT_SP);
        updatePaint();
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public void setMListener(OnUpdateTitleListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if(mSet == null || mSet.size() < 1)
            return;
        mRect.left = parent.getLeft() + parent.getPaddingLeft();;
        mRect.right = parent.getRight() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        int lastPosition = INVALID_POSITION;
        for(int i = 0; i < childCount; i++){
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            if(position <= lastPosition)
                continue;
            if(position >= mSet.size()){
                return;
            }

            if(mSet.get(position).isDifferentWithLast()) {
                mRect.bottom = child.getTop();
                RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
                drawTitleDecoration(lp, c, mSet.get(position).text);
            }
        }
        super.onDraw(c, parent, state);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        mRect.left = parent.getLeft() + parent.getPaddingLeft();
        mRect.right = parent.getRight() - parent.getPaddingRight();
        View child = parent.getChildAt(0);
        int position = parent.getChildAdapterPosition(child);
        if(position <= INVALID_POSITION)
            return;
        if(position >= mSet.size()){
            return;
        }
        int offset = 0;
        if(child.getBottom() < height){
            if(position + 1 < mSet.size() && mSet.get(position + 1).isDifferentWithLast()){
                offset = height - child.getBottom();
            }
        }
        String text = mSet.get(position).text;
        drawStickyDecoration(parent, c, text, offset);
        if(mListener != null)
            mListener.updateTitle(mSet.get(position).firstSpell);
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams)view.getLayoutParams();
        int position = lp.getViewLayoutPosition();
        if(position <= INVALID_POSITION)
            return;
        if(mSet.get(position).isDifferentWithLast())
            outRect.set(0, height, 0, 0);
    }

    private void initPaint(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(textSize);
    }

    private void updatePaint(){
        mPaint.setTextSize(textSize);
    }

    private void drawTitleDecoration(RecyclerView.LayoutParams lp, Canvas c, String text){
        if(text == null || text.isEmpty()) return;
        mRect.bottom -= lp.topMargin ;
        mRect.top = mRect.bottom - height;
        mPaint.setColor(bgColor);
        c.drawRect(mRect, mPaint);
        mPaint.setColor(textColor);
        mPaint.getFontMetrics(metrics);
        float y = (mRect.top + mRect.bottom - metrics.bottom - metrics.top) / 2;
        c.drawText(text, mRect.left + pxConversion(paddingLeft, TypedValue.COMPLEX_UNIT_DIP), y, mPaint);
    }

    private int pxConversion(int value, int unit){
        return (int)TypedValue.applyDimension(unit, value, displayMetrics);
    }

    private void drawStickyDecoration(RecyclerView parent, Canvas c, String text, int offset){
        if(text == null || text.isEmpty()) return;
        mRect.top = parent.getTop() - offset;
        mRect.bottom = mRect.top + height ;
        mPaint.setColor(bgColor);
        c.drawRect(mRect, mPaint);
        mPaint.setColor(textColor);
        mPaint.getFontMetrics(metrics);
        float y = (mRect.top + mRect.bottom - metrics.bottom - metrics.top) / 2;
        c.drawText(text, mRect.left + pxConversion(paddingLeft, TypedValue.COMPLEX_UNIT_DIP), y, mPaint);
    }

    public interface OnUpdateTitleListener{
        void updateTitle(String title);
    }
}
