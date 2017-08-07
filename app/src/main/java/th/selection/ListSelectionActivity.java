package th.selection;

import android.content.Intent;
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


public class ListSelectionActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_selection);
        findViewById(R.id.bt_meituan).setOnClickListener(this);
        findViewById(R.id.bt_wchat).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.bt_meituan:
                intent.setClass(this, ImitateMeiTuanActivity.class);
                break;
            case R.id.bt_wchat:
                intent.setClass(this, ImitateWeChatActivity.class);
                break;
            default:
                break;
        }
        if(intent.getComponent() != null)
            startActivity(intent);
    }
}
