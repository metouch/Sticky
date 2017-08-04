package th.selection;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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


public class ImitateMeiTuanActivity extends AppCompatActivity implements IndexBar.TouchEventListener,
        StickyTitleDecoration.OnUpdateTitleListener{

    String TAG = "TestActivity";

    DataContainer<CityBean> container;
    TextView tvCharacter;
    RecyclerView mRecyclerView;
    RecyclerView childRecyclerView;
    LinearLayoutManager mManager;
    TestAdapter mAdapter;
    IndexBar indexBar;
    Runnable showIndex;

    private List<CityBean> mSet = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_index_bar);
        container = new DataContainer<CityBean>(mSet);
        initView();
        lazyLoadBodyData();
//        view.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                List<CityBean> list = new ArrayList<CityBean>();
//                list.add(new CityBean(0, "北京"));
//                list.add(new CityBean(0, "日本"));
//                list.add(new CityBean(0, "武汉"));
//                list.add(new CityBean(0, "淄博"));
////                container.addData(list);
////                addHeaderView();
//                container.addMiddleData(list, "重复", "☆", false);
//                // container.addData(list);
//                mAdapter.notifyDataSetChanged();
//                indexBar.requestLayout();
//                indexBar.invalidate();
//            }
//        }, 10000);
//
//        view.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                List<CityBean> list = new ArrayList<CityBean>();
//                list.add(new CityBean(0, "啊啊"));
//                list.add(new CityBean(0, "日本"));
//                list.add(new CityBean(0, "武汉"));
//                list.add(new CityBean(0, "淄博"));
//                container.addData(list);
////                addHeaderView();
////                container.addMiddleData(list, "重复", "☆", true);
//                // container.addData(list);
//                mAdapter.notifyDataSetChanged();
//                indexBar.requestLayout();
//                indexBar.invalidate();
//            }
//        }, 15000);
//
////        view.postDelayed(new Runnable() {
////            @Override
////            public void run() {
////                List<String> list = new ArrayList<String>();
////                list.add("嘻嘻");
////                list.add("哈哈");
////                list.add("哦哦");
////                list.add("滋滋");
//////                container.addData(list);
//////                addHeaderView();
////                container.addMiddleData(list, "叠词", "↑", false);
////                // container.addData(list);
////                mAdapter.notifyDataSetChanged();
////                indexBar.requestLayout();
////                indexBar.invalidate();
////            }
////        }, 10000);
//
//        view.postDelayed(new Runnable() {
//            @Override
//            public void run() {
////                String[] arrays = getResources().getStringArray(R.array.cities);
////                container.addData(Arrays.asList(arrays));
//                View view1 = LayoutInflater.from(ImitateMeiTuanActivity.this).inflate(R.layout.item_test, mRecyclerView, false);
//                View view2 = LayoutInflater.from(ImitateMeiTuanActivity.this).inflate(R.layout.item_test, mRecyclerView, false);
//                mAdapter.addHeaderView(view1, "导航", "导");
//                mAdapter.addHeaderView(view2, "定位", "定");
//                mAdapter.notifyDataSetChanged();
//                indexBar.setCurrentItem(null);
//                indexBar.requestLayout();
//
//                //indexBar.invalidate();
//            }
//        }, 5000);

    }

    private void initView(){
        tvCharacter = (TextView)findViewById(R.id.tv_character);
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mAdapter = new TestAdapter(this, container);
        mRecyclerView.setAdapter(mAdapter);
        StickyTitleDecoration<CityBean> decoration =
                new StickyTitleDecoration<>(this, container.getDecorationSet());
        DividerDecoration<CityBean> dividerDecoration = new DividerDecoration.Builder<CityBean>()
                .setContext(this)
                .setColor(0xff000000)
                .setHeight(1)
                .setContainer(container.getDecorationSet())
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
    }


    private void lazyLoadHeadData(){
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadHeadData();
            }
        }, 6000);
    }

    private void loadHeadData(){
        List<CityBean> beans = new ArrayList<>();
        beans.add(new CityBean(0, "北京"));
        beans.add(new CityBean(0, "上海"));
        beans.add(new CityBean(0, "广州"));
        beans.add(new CityBean(0, "深圳"));
        beans.add(new CityBean(0, "杭州"));
        View headView = LayoutInflater.from(this).inflate(R.layout.item_recyclerview, mRecyclerView, false);
        RecyclerView recyclerView = (RecyclerView) headView.findViewById(R.id.recycler_view);
        GridAdapter adapter = new GridAdapter(this, beans);
        recyclerView.setAdapter(adapter);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(manager);
        mAdapter.addHeaderView(headView, "最近", "热门");
    }

    private void loadHeadData2(){
        View headView2 = LayoutInflater.from(this).inflate(R.layout.item_recyclerview, mRecyclerView, false);
        RecyclerView recyclerView = (RecyclerView) headView2.findViewById(R.id.recycler_view);
        GridAdapter adapter = new GridAdapter(this, new ArrayList<CityBean>());
        recyclerView.setAdapter(adapter);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(manager);
        mAdapter.addHeaderView(headView2, "定位", "定位");
    }
    private void lazyLoadBodyData(){
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadBodyData();
                loadHeadData2();
                loadHeadData();
                invalidate();
            }
        }, 5000);
    }

    private void loadBodyData(){
        String[] arrays = getResources().getStringArray(R.array.cities);
        List<String> cities = Arrays.asList(arrays);
        int length = cities.size();
        List<CityBean> set = new ArrayList<>();
        for(int i = 0; i < length; i ++){
            set.add(new CityBean(i, cities.get(i)));
        }
        container.addData(set);
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
    }

    private void invalidate(){
        mAdapter.notifyDataSetChanged();
        indexBar.requestLayout();
        indexBar.invalidate();
    }

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
