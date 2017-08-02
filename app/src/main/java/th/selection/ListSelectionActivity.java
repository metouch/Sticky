package th.selection;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.promeg.pinyinhelper.Pinyin;
import com.github.promeg.pinyinhelper.PinyinMapDict;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lib.sticky.analysier.DataContainer;
import lib.sticky.bean.IndexBean;
import lib.sticky.widget.DividerDecoration;
import lib.sticky.widget.IndexBar;
import lib.sticky.widget.StickyTitleDecoration;


public class ListSelectionActivity extends AppCompatActivity implements IndexBar.TouchEventListener,
        StickyTitleDecoration.OnUpdateTitleListener{

    String TAG = "TestActivity";

    DataContainer container;
    TextView tvCharacter;
    RecyclerView mRecyclerView;
    LinearLayoutManager mManager;
    TestAdapter mAdapter;
    IndexBar indexBar;
    Runnable showIndex;

    private List<String> mSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_index_bar);
        String[] arrays = getResources().getStringArray(R.array.cities);
        mSet = new ArrayList<>(Arrays.asList(arrays));
        final HashMap<String, String[]> dict = new HashMap<>();
        dict.put("重庆", new String[]{"CHONG", "QING"});
        dict.put("长沙", new String[]{"CHANG", "SHA"});
        dict.put("长春", new String[]{"CHANG", "CHUN"});
        Pinyin.init(Pinyin.newConfig()
                .with(new PinyinMapDict() {
                    @Override
                    public Map<String, String[]> mapping() {
                        return dict;
                    }
                }));
        container = new DataContainer(mSet);
        tvCharacter = (TextView)findViewById(R.id.tv_character);
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mAdapter = new TestAdapter(this, container);
        mRecyclerView.setAdapter(mAdapter);
        StickyTitleDecoration decoration = new StickyTitleDecoration(this, container.getDecorationSet());
        DividerDecoration dividerDecoration = new DividerDecoration.Builder()
                                                    .setColor(0xff000000)
                                                    .setHeight(3)
                                                    .setContainer(container)
                                                    .setOffset(1)
                                                    .build();
        decoration.setTextColor(0xffff0000);
        decoration.setMListener(this);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.addItemDecoration(dividerDecoration);
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mManager);
        indexBar = (IndexBar)findViewById(R.id.test_index);
        indexBar.setTouchEventListener(this);
        indexBar.setMData(container.getIndexSet());
        View view = getWindow().getDecorView();
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<String> list = new ArrayList<String>();
                list.add("北京");
                list.add("成都");
                list.add("武汉");
                list.add("淄博");
//                container.addData(list);
//                addHeaderView();
                container.addMiddleData(list, "重复", "☆", true);
               // container.addData(list);
                mAdapter.notifyDataSetChanged();
                indexBar.requestLayout();
                indexBar.invalidate();
            }
        }, 5000);
//        view.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                List<String> list = new ArrayList<String>();
//                list.add("嘻嘻");
//                list.add("哈哈");
//                list.add("哦哦");
//                list.add("滋滋");
////                container.addData(list);
////                addHeaderView();
//                container.addMiddleData(list, "叠词", "↑", false);
//                // container.addData(list);
//                mAdapter.notifyDataSetChanged();
//                indexBar.requestLayout();
//                indexBar.invalidate();
//            }
//        }, 10000);

        view.postDelayed(new Runnable() {
            @Override
            public void run() {
//                String[] arrays = getResources().getStringArray(R.array.cities);
//                container.addData(Arrays.asList(arrays));
                View view1 = LayoutInflater.from(ListSelectionActivity.this).inflate(R.layout.item_test, mRecyclerView, false);
                View view2 = LayoutInflater.from(ListSelectionActivity.this).inflate(R.layout.item_test, mRecyclerView, false);
                mAdapter.addHeaderView(view1, "导航", "导");
                mAdapter.addHeaderView(view2, null, null);
                mAdapter.notifyDataSetChanged();
                indexBar.requestLayout();
                indexBar.invalidate();
            }
        }, 5000);

    }

//    public void addHeaderView(){
//        View view = LayoutInflater.from(this).inflate(R.layout.item_test, mRecyclerView, false);
//        mAdapter.addHeaderView(view);
//        container.updateIndexOffset(1);
//    }

    @Override
    public void onDown(IndexBean bean) {
        Log.e(TAG, "mRecyclerView.getHeight() = " + mRecyclerView.getHeight());
        Log.w(TAG, "s = " + bean.toString());
        mManager.scrollToPositionWithOffset(bean.position + container.getIndexOffset(), 0);
        tvCharacter.setVisibility(View.VISIBLE);
        tvCharacter.setText(bean.firstSpell);
    }

    @Override
    public void onChanged(IndexBean last, IndexBean current) {
        Log.w(TAG, "last = " + last.toString() + ", current = " + current.toString());
        mManager.scrollToPositionWithOffset(current.position + container.getIndexOffset(), 0);
        tvCharacter.setText(current.firstSpell);
    }

    @Override
    public void onUp(IndexBean last, IndexBean current) {
        if(last == null) return;
        Log.w(TAG, "up, last = " + last.toString());
        tvCharacter.setVisibility(View.GONE);
    }

    @Override
    public void updateTitle(String title) {
        if(!TextUtils.isEmpty(title)){
            indexBar.setCurrentItem(title);
            controlIndex(title);
        }
    }

    private void controlIndex(String title){
        if(showIndex == null){
            showIndex = new Runnable() {
                @Override
                public void run() {
                    tvCharacter.setVisibility(View.GONE);
                }
            };
        }else {
            tvCharacter.removeCallbacks(showIndex);
        }
        tvCharacter.setText(title);
        if(!tvCharacter.isShown())
            tvCharacter.setVisibility(View.VISIBLE);
        tvCharacter.postDelayed(showIndex, 1000);
    }
}
