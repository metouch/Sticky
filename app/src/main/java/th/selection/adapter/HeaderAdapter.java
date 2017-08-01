package th.selection.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import th.selection.R;
import th.selection.analysier.DataContainer;
import th.selection.bean.FullWordEntity;

/**
 * Created by me_touch on 2017/6/14.
 *
 */

public class HeaderAdapter extends RecyclerView.Adapter {

    private LayoutInflater mInflater;
    private DataContainer container;
    private List<FullWordEntity> mSet;
    private SparseArray<View> headers = new SparseArray<>();

    private int headerStart = Integer.MIN_VALUE;


    public HeaderAdapter(Context context, DataContainer container){
        this.mInflater = LayoutInflater.from(context);
        mSet = container.getDecorationSet();
        this.container = container;
    }

    public void addHeaderView(View view){
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
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType < Integer.MIN_VALUE + headers.size()){
            int position = viewType - Integer.MIN_VALUE;
            View view = headers.valueAt(position);
            return new ViewHolder(view);
        }
        View view = mInflater.inflate(R.layout.item_test, parent, false);
        return new TestHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof TestHolder) {
            ((TestHolder) holder).tvItem.setText(mSet.get(position).word);
        }
    }

    private class TestHolder extends RecyclerView.ViewHolder{

        TextView tvItem;

        private TestHolder(View view){
            super(view);
            tvItem = (TextView)view.findViewById(R.id.item_text);
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        private ViewHolder(View view){
            super(view);
        }
    }

}
