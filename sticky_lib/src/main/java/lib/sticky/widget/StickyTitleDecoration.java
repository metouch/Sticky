package lib.sticky.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import java.util.List;

import lib.sticky.bean.FullWordEntity;


/**
 * Created by me_touch on 2017/6/28.
 *
 */

public class StickyTitleDecoration extends RecyclerView.ItemDecoration{

    private String TAG = "StickyTitleDecoration";
    private int INVALID_POSITION = -1;

    private Context context;
    private Paint mPaint;
    private Paint.FontMetrics metrics;
    private Rect mRect;
    private OnUpdateTitleListener mListener;
    private int textColor = 0xffffffff; //必须是32位
    private int bgColor = 0xff999999;
    private int height = 120;
    private int textSize = 14;
    private List<FullWordEntity> mSet;

    public StickyTitleDecoration(Context context, List<FullWordEntity> mSet){
        this.context = context;
        this.mSet = mSet;
        metrics = new Paint.FontMetrics();
        textSize = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP
                , textSize, context.getResources().getDisplayMetrics());
        initPaint();
        mRect = new Rect();
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public void setTextSize(int textSize){
        this.textSize = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP
                , textSize, context.getResources().getDisplayMetrics());
        updatePaint();
    }

    public void setMListener(OnUpdateTitleListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if(mSet == null || mSet.size() < 1)
            return;
        mRect.left = parent.getPaddingLeft();
        mRect.right = parent.getWidth() - parent.getPaddingRight();
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
        drawStickyDecoration(parent, c, mSet.get(position).text, offset);
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
        c.drawText(text, mRect.left, y, mPaint);
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
        c.drawText(text, mRect.left, y, mPaint);
    }

    public interface OnUpdateTitleListener{
        void updateTitle(String title);
    }
}
