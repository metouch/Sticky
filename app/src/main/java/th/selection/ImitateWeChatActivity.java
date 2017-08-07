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


public class ImitateWeChatActivity extends AppCompatActivity implements IndexBar.TouchEventListener,
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
    boolean ignored = true; //忽略因为界面重绘导致的tvCharacter显示

    private List<CityBean> mSet = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_index_bar);
        container = new DataContainer<CityBean>(mSet);
        initView();
        lazyLoadBodyData();
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
        decoration.setBgColor(0xffd4d4d4);
        decoration.setPaddingLeft(10);
        decoration.setMListener(this);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.addItemDecoration(dividerDecoration);
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mManager);
        indexBar = (IndexBar)findViewById(R.id.test_index);
        indexBar.setTouchEventListener(this);
        indexBar.setMData(container.getIndexSet());
        loadBodyData();
    }


    private void lazyLoadMiddleData(){
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadMiddleData();
            }
        }, 6000);
    }

    private void loadMiddleData2(){
        List<CityBean> beans = new ArrayList<>();
        beans.add(new CityBean(0, "知乎"));
        beans.add(new CityBean(0, "沪指"));
        beans.add(new CityBean(0, "联系"));
        container.addMiddleData(beans, "乱写", "↑", false);
    }

    private void loadMiddleData(){
        List<CityBean> beans = new ArrayList<>();
        beans.add(new CityBean(0, "北京"));
        beans.add(new CityBean(0, "上海"));
        beans.add(new CityBean(0, "广州"));
        beans.add(new CityBean(0, "深圳"));
        beans.add(new CityBean(0, "杭州"));
        container.addMiddleData(beans, "热门", "☆", true);
    }

    private void lazyLoadBodyData(){
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                //loadBodyData();
                loadMiddleData();
                loadMiddleData2();
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
        ignored = true;
    }

    @Override
    public void onDown(IndexBean bean) {
        Log.e(TAG, "mRecyclerView.getHeight() = " + mRecyclerView.getHeight());
        Log.w(TAG, "s = " + bean.toString());
        mManager.scrollToPositionWithOffset(bean.position + container.getIndexOffset(), 0);
        //tvCharacter.setVisibility(View.VISIBLE);
        //tvCharacter.setText(bean.firstSpell);
    }

    @Override
    public void onChanged(IndexBean last, IndexBean current) {
        Log.w(TAG, "last = " + last.toString() + ", current = " + current.toString());
        mManager.scrollToPositionWithOffset(current.position + container.getIndexOffset(), 0);
        //tvCharacter.setText(current.firstSpell);
    }

    @Override
    public void onUp(IndexBean last, IndexBean current) {
        if(last == null) return;
        Log.w(TAG, "up, last = " + last.toString());
        //tvCharacter.setVisibility(View.GONE);
    }

    @Override
    public void updateTitle(String title) {
        if(!TextUtils.isEmpty(title)){
            indexBar.setCurrentItem(title);
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
