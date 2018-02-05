package cc.ibooker.zdropdownmenulib;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * 自定义下拉菜单
 *
 * @author 邹峰立
 */
public class DropDownMenu extends LinearLayout {
	// 顶部菜单布局
	private LinearLayout tabMenuView;
	// 底部容器，包含popupMenuViews，maskView
	private FrameLayout containerView;
	// 弹出菜单父布局
	private FrameLayout popupMenuViews;
	// 遮罩半透明View，点击可关闭DropDownMenu
	private View maskView;
	// tabMenuView里面选中的tab位置，-1表示未选中
	private int current_tab_position = -1;
	// 分割线颜色
	private int dividerColor = 0xffcccccc;
	// tab选中颜色
	private int textSelectedColor = 0xff890c85;
	// tab未选中颜色
	private int textUnselectedColor = 0xff111111;
	// 遮罩颜色
	private int maskColor = 0x77777777;
	// tab字体大小
	private int menuTextSize = 14;

	public DropDownMenu(Context context) {
		super(context, null);
	}

	public DropDownMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DropDownMenu(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		setOrientation(VERTICAL);
		// 为DropDownMenu添加自定义属性
		int menuBackgroundColor = 0xffffffff;
		int underlineColor = 0xffcccccc;
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DropDownMenu);
		underlineColor = a.getColor(R.styleable.DropDownMenu_ddunderlineColor, underlineColor);
		dividerColor = a.getColor(R.styleable.DropDownMenu_dddividerColor, dividerColor);
		textSelectedColor = a.getColor(R.styleable.DropDownMenu_ddtextSelectedColor, textSelectedColor);
		textUnselectedColor = a.getColor(R.styleable.DropDownMenu_ddtextUnselectedColor, textUnselectedColor);
		menuBackgroundColor = a.getColor(R.styleable.DropDownMenu_ddmenuBackgroundColor, menuBackgroundColor);
		maskColor = a.getColor(R.styleable.DropDownMenu_ddmaskColor, maskColor);
		menuTextSize = a.getDimensionPixelSize(R.styleable.DropDownMenu_ddmenuTextSize, menuTextSize);
		a.recycle();

		// 初始化tabMenuView并添加到tabMenuView
		tabMenuView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.navigation_activity_choose_layout, null);
		addView(tabMenuView, 0);

		// 初始化containerView并将其添加到DropDownMenu
		containerView = new FrameLayout(context);
		containerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
		addView(containerView, 1);
	}

	/**
	 * 初始化DropDownMenu
	 *
	 * @param tabTexts
	 *            头部标题
	 * @param tags
	 *            头部标签
	 * @param popupViews
	 * @param contentView
	 */
	public void setDropDownMenu(@NonNull List<String> tabTexts, @NonNull List<String> tags, @NonNull List<View> popupViews) {
		if (tabTexts.size() != popupViews.size() || tabTexts.size() != tags.size()) {
			throw new IllegalArgumentException("params not match, tabTexts.size() should be equal popupViews.size() and tabTexts.size() should be equal tags.size()");
		}

		for (int i = 0; i < tabTexts.size(); i++) {
			initTab(tabTexts, i ,tags.get(i));
		}

		maskView = new View(getContext());
		maskView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
		maskView.setBackgroundColor(maskColor);
		maskView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
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

		containerView.addView(maskView, 0);
		containerView.addView(popupMenuViews, 1);
	}

	private void initTab(List<String> tabTexts, final int i, String tag) {
		final RelativeLayout layout = (RelativeLayout) tabMenuView.getChildAt(i);
		layout.getChildAt(0).setTag(tag);
		// 添加点击事件
		layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (clickMenuListener != null) {
					clickMenuListener.onClickMenuListener(layout, i);
				}
			}
		});
	}

	/**
	 * 改变tab文字
	 *
	 * @param text
	 */
	public void setTabText(String text) {
		if (current_tab_position != -1) {
			((TextView) ((RelativeLayout) tabMenuView.getChildAt(current_tab_position)).getChildAt(0)).setText(text);
		}
	}

	/**
	 * 初始化tab文字
	 */
	public void clearTabTex(int num, String str) {
		((TextView) ((RelativeLayout) tabMenuView.getChildAt(num)).getChildAt(0)).setText(str);
	}

	public void setTabClickable(boolean clickable) {
		for (int i = 0; i < tabMenuView.getChildCount(); i = i + 2) {
			((TextView) ((RelativeLayout) tabMenuView.getChildAt(current_tab_position)).getChildAt(0)).setClickable(clickable);
		}
	}

	/**
	 * 得到tab文字
	 */
	public String getTabText() {
		HashMap<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < tabMenuView.getChildCount(); i++) {
			TextView textView = ((TextView) ((RelativeLayout) tabMenuView.getChildAt(i)).getChildAt(0));
			if (textView != null) {
				String str = textView.getText().toString().trim();
				map.put((String) textView.getTag(), str);
			}
		}
		return new Gson().toJson(map);
	}

	/**
	 * 关闭菜单
	 */
	public void closeMenu() {
		if (current_tab_position != -1) {
			((TextView) ((RelativeLayout) tabMenuView.getChildAt(current_tab_position)).getChildAt(0)).setTextColor(textUnselectedColor);
			((ImageView) ((RelativeLayout) tabMenuView.getChildAt(current_tab_position)).getChildAt(1)).setImageResource(R.drawable.down);
			popupMenuViews.setVisibility(View.GONE);
			popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_out));
			maskView.setVisibility(GONE);
			maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_out));
			current_tab_position = -1;
			// 关闭菜单事件监听
			if (closeMenuListener != null) {
				closeMenuListener.onCloseMenuListener();
			}
		}
	}

	/**
	 * DropDownMenu是否处于可见状态
	 *
	 * @return
	 */
	public boolean isShowing() {
		return current_tab_position != -1;
	}

	/**
	 * 切换菜单
	 *
	 * @param target
	 */
	public void switchMenu(View target) {
		for (int i = 0; i < tabMenuView.getChildCount(); i++) {
			if (target == tabMenuView.getChildAt(i)) {
				if (current_tab_position == i) {
					closeMenu();
				} else {
					if (current_tab_position == -1) {
						popupMenuViews.setVisibility(View.VISIBLE);
						popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_in));
						maskView.setVisibility(VISIBLE);
						maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_in));
						popupMenuViews.getChildAt(i).setVisibility(View.VISIBLE);
					} else {
						popupMenuViews.getChildAt(i).setVisibility(View.VISIBLE);
					}
					current_tab_position = i;
					((TextView) ((RelativeLayout) tabMenuView.getChildAt(i)).getChildAt(0)).setTextColor(textSelectedColor);
					((ImageView) ((RelativeLayout) tabMenuView.getChildAt(i)).getChildAt(1)).setImageResource(R.drawable.up);
				}
			} else {
				((TextView) ((RelativeLayout) tabMenuView.getChildAt(i)).getChildAt(0)).setTextColor(textUnselectedColor);
				((ImageView) ((RelativeLayout) tabMenuView.getChildAt(i)).getChildAt(1)).setImageResource(R.drawable.down);
				popupMenuViews.getChildAt(i).setVisibility(View.GONE);
			}
		}
	}

	public int dpTpPx(float value) {
		DisplayMetrics dm = getResources().getDisplayMetrics();
		return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm) + 0.5);
	}

	/**
	 * 定义接口，对关闭事件监听
	 *
	 * @author Administrator
	 *
	 */
	public interface ClickMenuListener{
		void onClickMenuListener(View v, int num);
	}

	private ClickMenuListener clickMenuListener;

	public void setClickMenuListener(ClickMenuListener clickMenuListener) {
		this.clickMenuListener = clickMenuListener;
	}

	/**
	 * 定义接口，对关闭事件监听
	 *
	 * @author Administrator
	 *
	 */
	public interface CloseMenuListener{
		void onCloseMenuListener();
	}

	private CloseMenuListener closeMenuListener;

	public void setCloseMenuListener(CloseMenuListener closeMenuListener) {
		this.closeMenuListener = closeMenuListener;
	}
}