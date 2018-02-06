package cc.ibooker.zdropdownmenulib;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * 自定义下拉菜单布局
 * <p>
 * Created by 邹峰立 on 2018/2/5 0005.
 */
public class DropDownMenu extends LinearLayout {
    // 下拉菜单是否已经展示
    private boolean isShow;
    // 底部容器，包含contentView，maskView
    private FrameLayout containerView;
    // 弹出菜单父布局
    private View contentView;
    // 遮罩半透明View，点击可关闭DropDownMenu
    private View maskView;

    public DropDownMenu(Context context) {
        this(context, null);
    }

    public DropDownMenu(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropDownMenu(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);

        // 添加底部内容区域containerView
        containerView = new FrameLayout(context);
        containerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        addView(containerView);
    }

    /**
     * 展示下拉菜单
     *
     * @param view 待展示菜单内容
     */
    public void showDropDownMenu(@NonNull View view) {
        if (isShow)
            closeMenu();
        if (containerView != null) {
            containerView.removeAllViews();
            // 遮罩层
            maskView = new View(getContext());
            maskView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            int maskColor = 0x77777777;
            maskView.setBackgroundColor(maskColor);
            maskView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeMenu();
                }
            });
            maskView.setVisibility(GONE);
            // 菜单内容
            contentView = view;
            // 内容区添加内容
            containerView.addView(maskView, 0);
            containerView.addView(containerView, 1);
            // 设置开启菜单动画
            contentView.setVisibility(View.VISIBLE);
            contentView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_in));
            maskView.setVisibility(VISIBLE);
            maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_in));
            // 标记正在展示菜单
            isShow = true;
        }
    }


    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (isShow) {
            // 设置菜单关闭动画
            contentView.setVisibility(View.GONE);
            contentView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_out));
            maskView.setVisibility(GONE);
            maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_out));
            // 标记关闭菜单
            isShow = false;
            // 关闭菜单事件监听
            if (closeMenuListener != null) {
                closeMenuListener.onCloseMenuListener();
            }
        }
    }

    // 获取菜单是否正在展示状态
    public boolean isShow() {
        return isShow;
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
