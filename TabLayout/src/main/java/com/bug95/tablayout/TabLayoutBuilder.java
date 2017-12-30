package com.bug95.tablayout;

import android.content.Context;
import android.support.annotation.IdRes;
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
    private OnTabSelectedListener listener;
    private ArrayList<View> mItemViewsList = new ArrayList<>();
    private ArrayList<ItemStatus> mItemStatusList = new ArrayList<>();
    private int mBottomMargin;
    private float mTextSize;
    private TabLayoutBuilder.ItemStatus mCenterTabItemStatus;

    {
        listener = new OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
                int position = tab.getPosition();
                ItemStatus status = mItemStatusList.get(position);
                TextView view = getTextView(position);
                ImageView img = getImageView(position);
                if (view != null) {
                    view.setTextColor(mItemStatusList.get(position).getSelectedTitleColor());
                }
                if (status.getSelectedResId() != 0 && img != null) {
                    img.setImageResource(status.getSelectedResId());
                }
            }

            @Override
            public void onTabUnselected(Tab tab) {
                int position = tab.getPosition();
                ItemStatus status = mItemStatusList.get(position);
                TextView view = getTextView(position);
                ImageView img = getImageView(position);
                if (view != null) {
                    view.setTextColor(status.getNormalTitleColor());
                }
                if (status.getNormalResId() != 0 && img != null) {
                    img.setImageResource(status.getNormalResId());
                }
            }

            @Override
            public void onTabReselected(Tab tab) {
            }
        };
    }


    public TabLayoutBuilder(Context context) {
        super(context);
    }

    public TabLayoutBuilder(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TabLayoutBuilder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 预添加Item元素，根据Item的属性封装类（ItemStatus）的属性填充成View对象
     * 并保存属性封装类（ItemStatus）和填充后的View对象到数组中等待build方法调用时使用
     * <p>Add Item where you want to show</p>
     * <p>If you want to set the title textSize,please call {@link #setTextSize(float)}</p>
     * <p>Else if you want to set the bottomMargin,please call {@link #setBottomMargin(int)}</p>
     *
     * @param status Inner class {@link ItemStatus}
     */
    public void addTab(ItemStatus status) {
        View inflate = getTabView(status);
        mItemStatusList.add(status);
        mItemViewsList.add(inflate);
    }

    /**
     * <p>Set the view of bottom margin</p>
     * <p>Must before {@link #addTab(ItemStatus)}</p>
     *
     * @param dp bottomMargin
     */
    public void setBottomMargin(int dp) {
        mBottomMargin = dp;
    }

    /**
     * <p>Set the bottom title size</p>
     * <p>Must before {@link #addTab(ItemStatus)}</p>
     *
     * @param sp int sp
     */
    public void setTextSize(float sp) {
        mTextSize = sp;
    }

    /**
     * 把已经填充完成的View设置到TabLayout的每个Tab上
     * XTabLayout#newTab()方法中会为Tab填充默认布局，这里再次填充真正布局
     * 考虑优化Tab布局填充方式，设置自定义布局后，避免默认纯文字布局先行填充
     * <p>Must call {@link #addTab(ItemStatus)} before</p>
     *
     * @throws RuntimeException
     */
    public void build() {
        if (mItemViewsList.isEmpty()) {
            throw new RuntimeException("Must call addTab(ItemStatus status) before!");
        }

        XTabLayout.Tab tab = null;
        for (int i = 0; i < getTabCount() / 2; i++) {
            tab = this.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(mItemViewsList.get(i));
            }
        }
        setCenterTabCustomView(mCenterTabItemStatus);
        for (int i = getTabCount() / 2 + 1; i < getTabCount(); i++) {
            tab = this.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(mItemViewsList.get(i - 1));
            }
        }

        // addOnTabSelectedListener(listener);/*inner listener*/
    }

    private void setCenterTabCustomView(TabLayoutBuilder.ItemStatus status) {
        Tab tab = this.getTabAt(getTabCount() / 2);
        if (tab != null && status != null) {
            View view = getTabView(status);
            tab.setCustomView(view);
        }
    }

    public void setCenterTabItemStatus(TabLayoutBuilder.ItemStatus centerTabItemStatus) {
        mCenterTabItemStatus = centerTabItemStatus;
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


    /**
     * 根据ItemStatus的属性填充成Item的View对象
     * Inflate the Item View
     *
     * @param status ItemStatus
     * @return View
     */
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
