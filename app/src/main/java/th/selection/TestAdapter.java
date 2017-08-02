package th.selection;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import lib.sticky.adapter.HeaderAdapter;
import lib.sticky.analysier.DataContainer;

/**
 * Created by me_touch on 2017/8/2.
 *
 */

public class TestAdapter extends HeaderAdapter{

    private LayoutInflater mInflater;

    public TestAdapter(Context context, DataContainer container){
        super(context, container);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int obtainViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder foundViewHolder(ViewGroup parent, int viewType) {
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
}
