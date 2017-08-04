package th.selection;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by me_touch on 2017/8/4.
 *
 */

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder>{

    private List<CityBean> mSet;
    private LayoutInflater inflater;

    public GridAdapter(Context context, List<CityBean> set){
        this.mSet = set;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return mSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public GridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_recyclerview_child, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GridAdapter.ViewHolder holder, int position) {
        holder.tvName.setText(mSet.get(position).getName());
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvName;

        public ViewHolder(View view){
            super(view);
            tvName = (TextView)view.findViewById(R.id.child_text);
        }
    }
}
