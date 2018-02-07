package cc.ibooker.zdropdownmenulib;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * 自定义下拉菜单2
 *
 * @author 邹峰立
 */
public class ZDropDownMenu extends LinearLayout {
    // 顶部菜单布局
    private LinearLayout tabMenuView;
    // 底部容器，包含popupMenuViews，maskView
    private FrameLayout containerView;
    // 内容区
    private View contentView;
    // 弹出菜单父布局
    private FrameLayout popupMenuViews;
    // 遮罩半透明View，点击可关闭DropDownMenu
    private View maskView;
    // 水平下划线颜色
    private int underlineColor = 0xffcccccc;
    // 分割线颜色
    private int dividerColor = 0xffcccccc;
    // tab文本选中颜色
    private int textSelectedColor = 0xff890c85;
    // tab文本未选中颜色
    private int textUnselectedColor = 0xff111111;
    // menu背景
    private int menuBackgroundColor = 0xffffffff;
    // 遮罩颜色
    private int maskColor = 0x77777777;
    // tab文本字体大小
    private int menuTextSize = 14;
    // menuTab选中Icon图片
    private int menuSelectedIcon;
    // menuTab未选中Icon图片
    private int menuUnselectedIcon;
    // tabMenuView里面选中的tab位置，-1表示未选中
    private int currentTabPosition = -1;

    public ZDropDownMenu(Context context) {
        super(context, null);
    }

    public ZDropDownMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZDropDownMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);

        // 为DropDownMenu添加自定义属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DropDownMenu);
        underlineColor = a.getColor(R.styleable.DropDownMenu_underlineColor, underlineColor);
        dividerColor = a.getColor(R.styleable.DropDownMenu_dividerColor, dividerColor);
        textSelectedColor = a.getColor(R.styleable.DropDownMenu_textSelectedColor, textSelectedColor);
        textUnselectedColor = a.getColor(R.styleable.DropDownMenu_textUnselectedColor, textUnselectedColor);
        menuBackgroundColor = a.getColor(R.styleable.DropDownMenu_menuBackgroundColor, menuBackgroundColor);
        maskColor = a.getColor(R.styleable.DropDownMenu_maskColor, maskColor);
        menuTextSize = a.getDimensionPixelSize(R.styleable.DropDownMenu_menuTextSize, menuTextSize);
        menuSelectedIcon = a.getResourceId(R.styleable.DropDownMenu_menuSelectedIcon, menuSelectedIcon);
        menuUnselectedIcon = a.getResourceId(R.styleable.DropDownMenu_menuUnselectedIcon, menuUnselectedIcon);
        a.recycle();

        // 初始化控件
        initViews(context);
    }

    /**
     * 初始化View
     *
     * @param context 上下文对象
     */
    private void initViews(Context context) {
        // 初始化tabMenuView并添加到tabMenuView
        tabMenuView = new LinearLayout(context);
        tabMenuView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        tabMenuView.setBackgroundColor(menuBackgroundColor);
        tabMenuView.setOrientation(VERTICAL);
        addView(tabMenuView, 0);

        // 创建下划线
        View underlineView = new View(context);
        underlineView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, dpToPx(1)));
        underlineView.setBackgroundColor(underlineColor);
        addView(underlineView, 1);

        // 初始化containerView并将其添加到DropDownMenu
        containerView = new FrameLayout(context);
        containerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        addView(containerView, 2);
    }

    /**
     * 初始化DropDownMenu
     *
     * @param tabTexts    头部标题
     * @param popupViews  弹框View内容
     * @param contentView 内容区View
     */
    public void setDropDownMenu(@NonNull List<String> tabTexts, @NonNull List<View> popupViews, @NonNull View contentView) {
        if (tabTexts.size() != popupViews.size()) {
            throw new IllegalArgumentException("params not match, tabTexts.size() should be equal popupViews.size()");
        }

        for (int i = 0; i < tabTexts.size(); i++) {
            initTab(i, tabTexts);
        }

        maskView = new View(getContext());
        maskView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        maskView.setBackgroundColor(maskColor);
        maskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 关闭下拉菜单
                closeMenu();
            }
        });
        maskView.setVisibility(GONE);

        popupMenuViews = new FrameLayout(getContext());
        popupMenuViews.setVisibility(GONE);
        for (int i = 0; i < popupViews.size(); i++) {
            popupViews.get(i).setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            popupMenuViews.addView(popupViews.get(i), i);
        }

        this.contentView = contentView;
        containerView.addView(contentView, 0);
        containerView.addView(maskView, 1);
        containerView.addView(popupMenuViews, 2);
    }

    /**
     * 初始化菜单tab
     *
     * @param i        tabTexts列表下标
     * @param tabTexts 菜单tab显示文本集
     */
    private void initTab(final int i, List<String> tabTexts) {
        final TextView textView = new TextView(getContext());
        textView.setSingleLine();
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, menuTextSize);
        textView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        textView.setTextColor(textUnselectedColor);
        textView.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(menuUnselectedIcon), null);
        textView.setText(tabTexts.get(i));
        textView.setPadding(dpToPx(5), dpToPx(10), dpToPx(5), dpToPx(10));
        // 添加点击事件
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMenu(textView);
                // 执行点击菜单事件
                if (clickMenuListener != null)
                    clickMenuListener.onClickMenu(textView, i);
            }
        });
        tabMenuView.addView(textView);
        // 添加分割线
        if (i < tabTexts.size() - 1) {
            View view = new View(getContext());
            view.setLayoutParams(new LayoutParams(dpToPx(0.5f), LayoutParams.MATCH_PARENT));
            view.setBackgroundColor(dividerColor);
            addView(view);
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (currentTabPosition != -1) {
            ((TextView) tabMenuView.getChildAt(currentTabPosition)).setTextColor(textUnselectedColor);
            ((TextView) tabMenuView.getChildAt(currentTabPosition)).setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(menuUnselectedIcon), null);
            popupMenuViews.setVisibility(View.GONE);
            popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_out));
            maskView.setVisibility(GONE);
            maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_out));
            currentTabPosition = -1;
            // 执行关闭菜单事件监听
            if (closeMenuListener != null) {
                closeMenuListener.onCloseMenu();
            }
        }
    }

    /**
     * 改变tab文字
     */
    public void setTabText(String text) {
        if (currentTabPosition != -1) {
            ((TextView) tabMenuView.getChildAt(currentTabPosition)).setText(text);
        }
    }

    /**
     * DropDownMenu是否处于可见状态
     */
    public boolean isShowing() {
        return currentTabPosition != -1;
    }

    /**
     * 切换菜单
     *
     * @param target 目标textView
     */
    public void switchMenu(TextView target) {
        for (int i = 0; i < tabMenuView.getChildCount(); i = i + 2) {
            if (target == tabMenuView.getChildAt(i)) {
                if (currentTabPosition == i) {
                    closeMenu();
                } else {// 弹窗菜单
                    if (currentTabPosition == -1) {// 初始化状态
                        popupMenuViews.setVisibility(View.VISIBLE);
                        popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_in));
                        maskView.setVisibility(VISIBLE);
                        maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_in));
                        popupMenuViews.getChildAt(i / 2).setVisibility(View.VISIBLE);
                    } else {
                        popupMenuViews.getChildAt(i / 2).setVisibility(View.VISIBLE);
                    }
                    currentTabPosition = i;
                    ((TextView) tabMenuView.getChildAt(i)).setTextColor(textSelectedColor);
                    ((TextView) tabMenuView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(menuSelectedIcon), null);
                    // 执行菜单切换事件
                    if (switchMenuListener != null)
                        switchMenuListener.onSwitchMenu();
                }
            } else {
                ((TextView) tabMenuView.getChildAt(i)).setTextColor(textUnselectedColor);
                ((TextView) tabMenuView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(menuUnselectedIcon), null);
                popupMenuViews.getChildAt(i / 2).setVisibility(View.GONE);
            }
        }
    }

    /**
     * dp转换成px
     *
     * @param value 待转换值
     */
    public int dpToPx(float value) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm);
    }

    /**
     * 定义接口，对菜单切换事件监听
     */
    public interface SwitchMenuListener {
        void onSwitchMenu();
    }

    private SwitchMenuListener switchMenuListener;

    public void setSwitchMenuListener(SwitchMenuListener switchMenuListener) {
        this.switchMenuListener = switchMenuListener;
    }

    /**
     * 定义接口，对菜单tab点击事件监听
     */
    public interface ClickMenuListener {
        void onClickMenu(View v, int num);
    }

    private ClickMenuListener clickMenuListener;

    public void setClickMenuListener(ClickMenuListener clickMenuListener) {
        this.clickMenuListener = clickMenuListener;
    }

    /**
     * 定义接口，对菜单关闭关闭事件监听
     */
    public interface CloseMenuListener {
        void onCloseMenu();
    }

    private CloseMenuListener closeMenuListener;

    public void setCloseMenuListener(CloseMenuListener closeMenuListener) {
        this.closeMenuListener = closeMenuListener;
    }
}