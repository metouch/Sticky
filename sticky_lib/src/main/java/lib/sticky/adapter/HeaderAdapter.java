package lib.sticky.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import lib.sticky.analysier.DataContainer;
import lib.sticky.bean.FullEntity;
import lib.sticky.spell.IWord2Spell;


/**
 * Created by me_touch on 2017/6/14.
 *
 */

public abstract class HeaderAdapter<T extends IWord2Spell> extends RecyclerView.Adapter {

    private LayoutInflater mInflater;
    private DataContainer<T> container;
    protected List<FullEntity<T>> mSet;
    private SparseArray<View> headers = new SparseArray<>();

    private int headerStart = Integer.MIN_VALUE;


    public HeaderAdapter(Context context, DataContainer<T> container){
        this.mInflater = LayoutInflater.from(context);
        mSet = container.getDecorationSet();
        this.container = container;
    }

    private void addHeaderView(View view){
        headers.put(headerStart, view);
        headerStart++;
    }

    public void addHeaderView(View view, String word, String index){
        addHeaderView(view);
        container.addHeaderData(word, index);
    }

    @Override
    public int getItemCount() {
        return mSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position < headers.size()){
            return Integer.MIN_VALUE + position;
        }
        return obtainViewType(position);
    }

    public abstract int obtainViewType(int position);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType < Integer.MIN_VALUE + headers.size()){
            int position = viewType - Integer.MIN_VALUE;
            View view = headers.valueAt(position);
            return new ViewHolder(view);
        }
        return foundViewHolder(parent, viewType);
    }

    public abstract RecyclerView.ViewHolder foundViewHolder(ViewGroup parent, int viewType);

    private class ViewHolder extends RecyclerView.ViewHolder{
        private ViewHolder(View view){
            super(view);
        }
    }

}
