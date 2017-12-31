package com.bug95.tablayout;

import android.content.Context;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class TabLayoutBuilder extends XTabLayout {

    /**
     * Item的属性封装在ItemStatus中，Layout每个Item的属性构成mItemStatusListList数组
     * 每个根据ItemStatus属性填充而成的View对象，构成mItemViewsListList数组
     * 等待build方法调用时，把mItemViewsListList每个View全部添加到TabLayout中
     */
    private ArrayList<View> mItemViewsList = new ArrayList<>();
    private int mBottomMargin;
    private float mTextSize;
    private int[] mTabResId;
    private String[] mTabTitle;
    private int mCenterResId;
    private String mCenterTitle;
    private int[] mTextColor;

    private int mTabCount;

    public TabLayoutBuilder(Context context) {
        super(context);
    }

    public TabLayoutBuilder(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TabLayoutBuilder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBottomMargin(int dp) {
        mBottomMargin = dp;
    }

    public void setTextSize(float sp) {
        mTextSize = sp;
    }

    public void setTabIcon(int[] resId) {
        mTabResId = resId;
    }

    public void setTabTitle(String[] tabTitle) {
        mTabTitle = tabTitle;
    }

    public void setCenterIcon(int resId) {
        mCenterResId = resId;
    }

    public void setCenterTitle(String title) {
        mCenterTitle = title;
    }

    public void setTextColor(int[] textColor) {
        mTextColor = textColor;
    }

    /**
     * 根据传入的icon、title等属性填充成View，并设置到TabLayout的每个Tab上
     * XTabLayout#newTab()方法中会为Tab填充默认布局，这里再次填充真正布局
     * 考虑优化Tab布局填充方式，设置自定义布局后，避免默认纯文字布局先行填充
     */
    public void build() {
        mTabCount = Math.min(mTabResId.length, mTabTitle.length) + (mHasCenterTab ? 1 : 0);

        XTabLayout.Tab tab;
        TabLayoutBuilder.ItemStatus itemStatus;
        for (int i = 0; i < mTabCount; i++) {
            if (mHasCenterTab && i == mTabCount / 2) {
                itemStatus = new TabLayoutBuilder.ItemStatus(mCenterTitle, mCenterResId, mTextColor[0], mTextColor[1]);
            } else if (mHasCenterTab && i > mTabCount / 2) {
                itemStatus = new TabLayoutBuilder.ItemStatus(mTabTitle[i - 1], mTabResId[i - 1], mTextColor[0], mTextColor[1]);
            } else {
                itemStatus = new TabLayoutBuilder.ItemStatus(mTabTitle[i], mTabResId[i], mTextColor[0], mTextColor[1]);
            }

            mItemViewsList.add(getTabView(itemStatus));

            tab = this.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(mItemViewsList.get(i));
            }
        }
    }

    private void setCenterTabCustomView(TabLayoutBuilder.ItemStatus status) {
        Tab tab = this.getTabAt(getTabCount() / 2);
        if (tab != null && status != null) {
            View view = getTabView(status);
            tab.setCustomView(view);
        }
    }

    /**
     * Get CustomView child TextView
     *
     * @param position index
     * @return TextView
     */
    private TextView getTextView(int position) {
        if (mItemViewsList != null) {
            ViewGroup parent = (ViewGroup) mItemViewsList.get(position);
            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                if (parent.getChildAt(i) instanceof TextView) {
                    return (TextView) parent.getChildAt(i);
                }
            }
        }
        return null;
    }

    /**
     * Get CustomView child ImageView
     *
     * @param position index
     * @return ImageView
     */
    private ImageView getImageView(int position) {
        if (mItemViewsList != null) {
            ViewGroup parent = (ViewGroup) mItemViewsList.get(position);
            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                if (parent.getChildAt(i) instanceof ImageView) {
                    return (ImageView) parent.getChildAt(i);
                }
            }
        }
        return null;
    }

    // 每个Tab的属性封装类ItemStatus
    public static final class ItemStatus {
        private CharSequence title;
        private int drawableSelectorId;
        private int normalResId;
        private int selectedResId;
        private int normalTitleColor;
        private int selectedTitleColor;

        public ItemStatus(CharSequence title, @IdRes int normalResId, @IdRes int selectedResId, int normalTitleColor, int selectedTitleColor) {
            this.title = title;
            this.normalResId = normalResId;
            this.selectedResId = selectedResId;
            this.normalTitleColor = normalTitleColor;
            this.selectedTitleColor = selectedTitleColor;
        }

        public ItemStatus(CharSequence title, @IdRes int drawableSelectorId, int normalTitleColor, int selectedTitleColor) {
            this.title = title;
            this.drawableSelectorId = drawableSelectorId;
            this.normalTitleColor = normalTitleColor;
            this.selectedTitleColor = selectedTitleColor;
        }

        private CharSequence getTitle() {
            return title;
        }

        private int getNormalResId() {
            return normalResId;
        }

        private int getSelectedResId() {
            return selectedResId;
        }

        private int getNormalTitleColor() {
            return normalTitleColor;
        }

        private int getSelectedTitleColor() {
            return selectedTitleColor;
        }

        private int getDrawableSelectorId() {
            return drawableSelectorId;
        }
    }

    // 根据ItemStatus的属性填充成Item的View对象
    private View getTabView(ItemStatus status) {

        LinearLayout linearLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(params);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflate = inflater.inflate(R.layout.design_layout_tab_item, linearLayout, false);
        ImageView img = (ImageView) inflate.findViewById(R.id.layout_tab_img);
        TextView text = (TextView) inflate.findViewById(R.id.layout_tab_text);

        if (status.getNormalResId() == 0) {
            img.setImageResource(status.getDrawableSelectorId());
        } else {
            img.setImageResource(status.getNormalResId());
        }

        text.setText(status.getTitle());
        //COMPLEX_UNIT_SP=2
        text.setTextSize(2, mTextSize);
        text.setTextColor(status.getNormalTitleColor());

        params = new LinearLayout.LayoutParams(inflate.getLayoutParams());
        params.gravity = Gravity.BOTTOM;
        params.bottomMargin = dpToPx(mBottomMargin);
        inflate.setLayoutParams(params);

        return inflate;
    }

}
