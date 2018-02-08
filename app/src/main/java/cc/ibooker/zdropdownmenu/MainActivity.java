package cc.ibooker.zdropdownmenu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cc.ibooker.zdropdownmenu.adapter.GwAdapter;
import cc.ibooker.zdropdownmenu.adapter.LvAdapter;
import cc.ibooker.zdropdownmenulib.ZDropDownMenu;

/**
 * 下拉菜单1
 */
public class MainActivity extends AppCompatActivity {
    private ZDropDownMenu zDropDownMenu;
    private String[] tabTexts = {"分类", "时间", "排序", "筛选"};
    private List<View> popupViews;
    private ListView listView;
    private LvAdapter lvAdapter;
    private ArrayList<String> lvDatas = new ArrayList<>();
    private GridView gridView;
    private GwAdapter gwAdapter;
    private ArrayList<String> gwDatas = new ArrayList<>();
    private View linearLayout;
    private View scrollView;
    // 内容区
    private View contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intent = new Intent(this, ThirdActivity.class);
//        startActivity(intent);

//        Intent intent = new Intent(this, SecondActivity.class);
//        startActivity(intent);

        initView();
    }

    // 初始化控件
    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(this);

        zDropDownMenu = findViewById(R.id.zdropdownmenu);

        if (popupViews == null)
            popupViews = new ArrayList<>();
        listView = inflater.inflate(R.layout.layout_listview, zDropDownMenu, false).findViewById(R.id.listview);
        popupViews.add(listView);
        gridView = inflater.inflate(R.layout.layout_gridview, zDropDownMenu, false).findViewById(R.id.gridview);
        popupViews.add(gridView);
        linearLayout = inflater.inflate(R.layout.layout_linearlayout, zDropDownMenu, false);
        popupViews.add(linearLayout);
        scrollView = inflater.inflate(R.layout.layout_scrollview, zDropDownMenu, false);
        popupViews.add(scrollView);

        // 内容区
        contentView = inflater.inflate(R.layout.layout_content, zDropDownMenu, false);


        zDropDownMenu.setDropDownMenu(Arrays.asList(tabTexts), popupViews, contentView);
        // 点击事件监听
        zDropDownMenu.setClickMenuListener(new ZDropDownMenu.ClickMenuListener() {
            @Override
            public void onClickMenu(String tag) {
                switch (tag) {
                    case "分类":// 分类
                        // 加载数据
                        setLvDatas();
                        // 刷新界面
                        setLvAdapter();
                        break;
                    case "时间":// 时间
                        // 加载数据
                        setGwDatas();
                        // 刷新界面
                        setGwAdapter();
                        break;
                    case "排序":// 排序

                        break;
                    case "筛选":// 筛选

                        break;
                }
            }
        });
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
