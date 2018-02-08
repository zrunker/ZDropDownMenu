package cc.ibooker.zdropdownmenulib;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import java.util.List;

/**
 * 自定义下拉菜单内容区布局
 * <p>
 * Created by 邹峰立 on 2018/2/5 0005.
 */
public class DropDownMenu extends FrameLayout {
    // 弹出菜单父布局
    private FrameLayout popupMenuViews;
    // 遮罩半透明View，点击可关闭DropDownMenu
    private View maskView;
    // tabMenuView里面选中的tab位置，-1表示未选中
    private int currentTabPosition = -1;

    public DropDownMenu(Context context) {
        this(context, null);
    }

    public DropDownMenu(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropDownMenu(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    /**
     * 设置下拉菜单
     *
     * @param popupViews 待展示菜单内容集
     */
    public void setDropDownMenu(@NonNull List<View> popupViews) {
        maskView = new View(getContext());
        maskView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        maskView.setBackgroundColor(0x77777777);
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

        this.addView(maskView, 0);
        this.addView(popupMenuViews, 1);
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (currentTabPosition != -1) {
            // 设置菜单关闭动画
            popupMenuViews.setVisibility(View.GONE);
            popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_out));
            maskView.setVisibility(GONE);
            maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_out));
            // 标记关闭菜单
            currentTabPosition = -1;
            // 关闭菜单事件监听
            if (closeMenuListener != null) {
                closeMenuListener.onCloseMenuListener();
            }
        }
    }

    /**
     * 切换菜单
     *
     * @param target 目标textView
     */
    public void switchMenu(View target) {
        for (int i = 0; i < popupMenuViews.getChildCount(); i++) {
            if (target == popupMenuViews.getChildAt(i)) {
                if (currentTabPosition == i) {
                    closeMenu();
                } else {// 弹窗菜单
                    if (currentTabPosition == -1) {// 初始化状态
                        popupMenuViews.setVisibility(View.VISIBLE);
                        popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_in));
                        maskView.setVisibility(VISIBLE);
                        maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_in));
                        popupMenuViews.getChildAt(i).setVisibility(View.VISIBLE);
                    } else {
                        popupMenuViews.getChildAt(i).setVisibility(View.VISIBLE);
                    }
                    currentTabPosition = i;
                }
            } else {
                popupMenuViews.getChildAt(i).setVisibility(View.GONE);
            }
        }
    }

    // 获取菜单是否正在展示状态
    public boolean isShowing() {
        return currentTabPosition != -1;
    }

    /**
     * 定义接口，对关闭事件监听
     */
    public interface CloseMenuListener {
        void onCloseMenuListener();
    }

    private CloseMenuListener closeMenuListener;

    public void setCloseMenuListener(CloseMenuListener closeMenuListener) {
        this.closeMenuListener = closeMenuListener;
    }

}
