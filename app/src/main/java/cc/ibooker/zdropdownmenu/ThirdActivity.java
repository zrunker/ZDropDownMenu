package cc.ibooker.zdropdownmenu;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cc.ibooker.zdropdownmenu.adapter.GwAdapter;
import cc.ibooker.zdropdownmenu.adapter.LvAdapter;
import cc.ibooker.zdropdownmenulib.DropDownMenu;

/**
 * 下拉菜单3
 * <p>
 * Created by 邹峰立 on 2018/2/7.
 */
public class ThirdActivity extends AppCompatActivity implements View.OnClickListener {
    // 内容区
    private TextView contentTv;
    // 菜单项
    private LinearLayout menuLayout;
    private LinearLayout classifyLayout, timeLayout, sortLayout, chooseLayout;
    private TextView classifyTv, timeTv, sortTv, chooseTv;
    private ImageView classifyImg, timeImg, sortImg, chooseImg;
    // 菜单内容区
    private DropDownMenu dropDownMenu;
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
        setContentView(R.layout.activity_third);

        initView();
    }

    // 初始化控件
    private void initView() {
        contentTv = findViewById(R.id.tv_content);

        // 菜单项
        menuLayout = findViewById(R.id.include_menu);
        classifyLayout = menuLayout.findViewById(R.id.layout_classify);
        classifyLayout.setOnClickListener(this);
        classifyTv = menuLayout.findViewById(R.id.tv_classify);
        classifyImg = menuLayout.findViewById(R.id.iv_classify);

        timeLayout = menuLayout.findViewById(R.id.layout_time);
        timeLayout.setOnClickListener(this);
        timeTv = menuLayout.findViewById(R.id.tv_time);
        timeImg = menuLayout.findViewById(R.id.iv_time);

        sortLayout = menuLayout.findViewById(R.id.layout_sort);
        sortLayout.setOnClickListener(this);
        sortTv = menuLayout.findViewById(R.id.tv_sort);
        sortImg = menuLayout.findViewById(R.id.iv_sort);

        chooseLayout = menuLayout.findViewById(R.id.layout_choose);
        chooseLayout.setOnClickListener(this);
        chooseTv = menuLayout.findViewById(R.id.tv_choose);
        chooseImg = menuLayout.findViewById(R.id.iv_choose);

        // 设置内容区到顶部距离
        menuLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int menuLayoutHeight = menuLayout.getMeasuredHeight();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) contentTv.getLayoutParams();
        params.setMargins(0, menuLayoutHeight, 0, 0);
        contentTv.setLayoutParams(params);

        // 菜单内容区
        dropDownMenu = findViewById(R.id.dropdownmenu);
        if (popupViews == null)
            popupViews = new ArrayList<>();
        LayoutInflater inflater = LayoutInflater.from(this);
        listView = inflater.inflate(R.layout.layout_listview, dropDownMenu, false).findViewById(R.id.listview);
        popupViews.add(listView);
        gridView = inflater.inflate(R.layout.layout_gridview, dropDownMenu, false).findViewById(R.id.gridview);
        popupViews.add(gridView);
        linearLayout = inflater.inflate(R.layout.layout_linearlayout, dropDownMenu, false);
        popupViews.add(linearLayout);
        scrollView = inflater.inflate(R.layout.layout_scrollview, dropDownMenu, false);
        popupViews.add(scrollView);

        dropDownMenu.setDropDownMenu(popupViews);
        dropDownMenu.setCloseMenuListener(new DropDownMenu.CloseMenuListener() {
            @Override
            public void onCloseMenuListener() {// 菜单关闭事件监听
                clearTabMenu();
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

    // 点击事件监听
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_classify:// 分类
                // 加载数据
                setLvDatas();
                // 刷新界面
                setLvAdapter();
                // 展示
                dropDownMenu.switchMenu(listView);
                updateTab(0);
                break;
            case R.id.layout_time:// 时间
                // 加载数据
                setGwDatas();
                // 刷新界面
                setGwAdapter();
                // 展示
                dropDownMenu.switchMenu(gridView);
                updateTab(1);
                break;
            case R.id.layout_sort:// 排序
                // 展示
                dropDownMenu.switchMenu(linearLayout);
                updateTab(2);
                break;
            case R.id.layout_choose:// 筛选
                // 展示
                dropDownMenu.switchMenu(scrollView);
                updateTab(3);
                break;
        }
    }

    // 清空菜单项所有样式
    private void clearTabMenu() {
        classifyTv.setTextColor(Color.parseColor("#555555"));
        classifyImg.setImageResource(R.mipmap.icon_down);
        timeTv.setTextColor(Color.parseColor("#555555"));
        timeImg.setImageResource(R.mipmap.icon_down);
        sortTv.setTextColor(Color.parseColor("#555555"));
        sortImg.setImageResource(R.mipmap.icon_down);
        chooseTv.setTextColor(Color.parseColor("#555555"));
        chooseImg.setImageResource(R.mipmap.icon_down);
    }

    // 修改选中项样式
    private void updateTab(int index) {
        clearTabMenu();
        if (dropDownMenu.isShowing()) {
            switch (index) {
                case 0:// 分类
                    classifyTv.setTextColor(Color.parseColor("#FE7517"));
                    classifyImg.setImageResource(R.mipmap.icon_up);
                    break;
                case 1:// 时间
                    timeTv.setTextColor(Color.parseColor("#FE7517"));
                    timeImg.setImageResource(R.mipmap.icon_up);
                    break;
                case 2:// 排序
                    sortTv.setTextColor(Color.parseColor("#FE7517"));
                    sortImg.setImageResource(R.mipmap.icon_up);
                    break;
                case 3:// 筛选
                    chooseTv.setTextColor(Color.parseColor("#FE7517"));
                    chooseImg.setImageResource(R.mipmap.icon_up);
                    break;
            }
        }
    }

}
