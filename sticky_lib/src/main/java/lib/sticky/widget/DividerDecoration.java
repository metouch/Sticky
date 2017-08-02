package lib.sticky.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import lib.sticky.analysier.DataContainer;
import lib.sticky.bean.FullWordEntity;


/**
 * Created by me_touch on 2017/7/28.
 *
 */

public class DividerDecoration extends RecyclerView.ItemDecoration{
    private int offset;
    private int height;
    private int color;

    private Paint paint;
    private Rect mRect;

    private DataContainer container;

    public DividerDecoration(DataContainer container){

    }

    public DividerDecoration(Builder builder){
        this.color = builder.color;
        this.offset = builder.offset;
        this.height = builder.height;
        this.container = builder.container;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        mRect = new Rect();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int count = parent.getChildCount();
        mRect.left = parent.getPaddingLeft();
        mRect.right = parent.getRight() - parent.getPaddingRight();
        for (int i = 0; i < count; i++) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            if(position < offset) continue;
            if(position < 0 || position >= container.getDecorationSet().size()) return;
            FullWordEntity entity = container.getDecorationSet().get(position);
            if(entity.isDifferentWithLast()) continue;
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams)child.getLayoutParams();
            mRect.bottom = child.getTop();
            drawDivider(lp, c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams)view.getLayoutParams();
        int pos = lp.getViewAdapterPosition();
        if(pos < 0 || pos > container.getDecorationSet().size() - 1) return;
        if(!container.getDecorationSet().get(pos).isDifferentWithLast())
            outRect.set(0, height, 0, 0);

    }

    private void drawDivider(RecyclerView.LayoutParams lp, Canvas c){
        mRect.bottom -= lp.topMargin;
        mRect.top = mRect.bottom - height;
        c.drawRect(mRect, paint);
    }

    public static class Builder{

        private int offset = 0;
        private int height = 1;
        private int color = 0xff999999;
        private DataContainer container;

        public Builder setOffset(int offset){
            this.offset = offset;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setColor(int color) {
            this.color = color;
            return this;
        }

        public Builder setContainer(DataContainer container) {
            this.container = container;
            return this;
        }

        public DividerDecoration build(){
            return new DividerDecoration(this);
        }
    }
}
