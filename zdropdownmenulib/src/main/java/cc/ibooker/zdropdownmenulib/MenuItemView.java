package cc.ibooker.zdropdownmenulib;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 菜单项 Item
 * <p>
 * Created by 邹峰立 on 2018/2/7 0007.
 */
public class MenuItemView extends LinearLayout {
    private TextView textView;
    private ImageView imageView;
    private boolean isShow;
    private int selectColor = 0xffFE7517;
    private int unSelectColor = 0xff555555;
    private int selectIcon = R.drawable.icon_up;
    private int unSelectIcon = R.drawable.icon_down;

    public MenuItemView(Context context) {
        this(context, null);
    }

    public MenuItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        addViews(context);
    }

    // 添加控件
    private void addViews(Context context) {
        textView = new TextView(context);
        textView.setTextColor(unSelectColor);
        textView.setTextSize(15);
        textView.setPadding(3, 3, 3, 3);
        addView(textView);
        imageView = new ImageView(context);
        imageView.setImageResource(unSelectIcon);
        addView(imageView);
    }

    // 初始化方法
    public MenuItemView init(int selectColor, int unSelectColor, int selectIcon, int unSelectIcon) {
        if (selectColor > 0)
            this.selectColor = selectColor;

        if (unSelectColor > 0) {
            this.unSelectColor = unSelectColor;
            textView.setTextColor(unSelectColor);
        }

        if (selectIcon > 0)
            this.selectIcon = selectIcon;

        if (unSelectIcon > 0) {
            this.unSelectIcon = unSelectIcon;
            imageView.setImageResource(unSelectIcon);
        }
        return this;
    }

    // 设置布局
    public MenuItemView setItemLayoutParams(ViewGroup.LayoutParams params) {
        this.setLayoutParams(params);
        return this;
    }

    // 设置tag信息
    public MenuItemView setItemTag(Object tag) {
        this.setTag(tag);
        return this;
    }

    // 设置文本位置
    public MenuItemView setTvGravity(int gravity) {
        textView.setGravity(gravity);
        return this;
    }

    // 设置文单行显示
    public MenuItemView setTvSingleLine() {
        textView.setSingleLine();
        return this;
    }

    // 设置文本Ellipsize
    public MenuItemView setTvEllipsize(TextUtils.TruncateAt where) {
        textView.setEllipsize(where);
        return this;
    }

    // 设置文本字体
    public MenuItemView setTextSize(int unit, float size) {
        textView.setTextSize(unit, size);
        return this;
    }

    // 设置文本颜色
    public MenuItemView setTextColor(@ColorInt int color) {
        textView.setTextColor(color);
        return this;
    }

    // 设置文本内边距
    public MenuItemView setTvPadding(int left, int top, int right, int bottom) {
        textView.setPadding(left, top, right, bottom);
        return this;
    }

    // 设置文本
    public MenuItemView setText(CharSequence text) {
        textView.setText(text);
        return this;
    }

    // 设置图片src
    public MenuItemView setImageResource(@DrawableRes int resId) {
        imageView.setImageResource(resId);
        return this;
    }

}
