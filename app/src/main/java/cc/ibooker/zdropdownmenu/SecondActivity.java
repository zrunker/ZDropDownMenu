package cc.ibooker.zdropdownmenu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cc.ibooker.zdropdownmenu.adapter.GwAdapter;
import cc.ibooker.zdropdownmenu.adapter.LvAdapter;
import cc.ibooker.zdropdownmenulib.MDropDownMenu;

/**
 * 自定义UI下拉菜单2
 * Created by 邹峰立 on 2018/2/7.
 */
public class SecondActivity extends AppCompatActivity {
    // 菜单相关
    private MDropDownMenu mDropDownMenu;
    private String[] tabTexts = {"分类", "时间", "排序", "筛选"};
    private String[] tags = {"classify", "time", "sort", "choose"};
    private List<View> popupViews;
    private ListView listView;
    private LvAdapter lvAdapter;
    private ArrayList<String> lvDatas = new ArrayList<>();
    private GridView gridView;
    private GwAdapter gwAdapter;
    private ArrayList<String> gwDatas = new ArrayList<>();
    private View linearLayout;
    private View scrollView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        initView();
    }

    // 初始化控件
    private void initView() {
        // 菜单相关
        mDropDownMenu = findViewById(R.id.mdropdownmenu);

        if (popupViews == null)
            popupViews = new ArrayList<>();
        LayoutInflater inflater = LayoutInflater.from(this);
        listView = inflater.inflate(R.layout.layout_listview, mDropDownMenu, false).findViewById(R.id.listview);
        popupViews.add(listView);
        gridView = inflater.inflate(R.layout.layout_gridview, mDropDownMenu, false).findViewById(R.id.gridview);
        popupViews.add(gridView);
        linearLayout = inflater.inflate(R.layout.layout_linearlayout, mDropDownMenu, false);
        popupViews.add(linearLayout);
        scrollView = inflater.inflate(R.layout.layout_scrollview, mDropDownMenu, false);
        popupViews.add(scrollView);

        mDropDownMenu.setDropDownMenu(Arrays.asList(tabTexts), Arrays.asList(tags), popupViews);

        // 点击事件监听
        mDropDownMenu.setClickMenuListener(new MDropDownMenu.ClickMenuListener() {
            @Override
            public void onClickMenu(String tag) {
                switch (tag) {
                    case "classify":// 分类
                        // 加载数据
                        setLvDatas();
                        // 刷新界面
                        setLvAdapter();
                        break;
                    case "time":// 时间
                        // 加载数据
                        setGwDatas();
                        // 刷新界面
                        setGwAdapter();
                        break;
                    case "sort":// 排序

                        break;
                    case "choose":// 筛选

                        break;
                }
            }
        });

        // 内容区
        TextView contentTv = findViewById(R.id.tv_content);
        // 设置内容区到顶部距离
        mDropDownMenu.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int menuLayoutHeight = mDropDownMenu.getMeasuredHeight();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) contentTv.getLayoutParams();
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.MATCH_PARENT;
        params.setMargins(0, menuLayoutHeight, 0, 0);
        contentTv.setLayoutParams(params);
    }

    // 设置排序数据
    private void setLvDatas() {
        if (lvDatas == null)
            lvDatas = new ArrayList<>();
        lvDatas.clear();
        for (int i = 0; i < 5; i++) {
            lvDatas.add("CLASSIFY" + "_" + i);
        }
    }

    // 自定义排序Adapter
    public void setLvAdapter() {
        if (lvAdapter == null) {
            lvAdapter = new LvAdapter(this, lvDatas);
            listView.setAdapter(lvAdapter);
        } else {
            lvAdapter.reflashData(lvDatas);
        }
    }

    // 设置分类数据
    public void setGwDatas() {
        if (gwDatas == null)
            gwDatas = new ArrayList<>();
        gwDatas.clear();
        for (int i = 0; i < 18; i++) {
            gwDatas.add("TIME" + "_" + i);
        }
    }

    // 自定义分类
    public void setGwAdapter() {
        if (gwAdapter == null) {
            gwAdapter = new GwAdapter(this, gwDatas);
            gridView.setAdapter(gwAdapter);
        } else {
            gwAdapter.reflashData(gwDatas);
        }
    }
}
