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

    private OnTabSelectedListener listener;
    private ArrayList<View> mItemViews = new ArrayList<>();
    private ArrayList<ItemStatus> mItemStatus = new ArrayList<>();
    private int mBottomMargin;
    private float mTextSize;
    private OnClickListener mCenterTabOnClickListener;

    {
        listener = new OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
                int position = tab.getPosition();
                ItemStatus status = mItemStatus.get(position);
                TextView view = getTextView(position);
                ImageView img = getImageView(position);
                if (view != null) {
                    view.setTextColor(mItemStatus.get(position).getSelectedTitleColor());
                }
                if (status.getSelectedResId() != 0 && img != null) {
                    img.setImageResource(status.getSelectedResId());
                }
            }

            @Override
            public void onTabUnselected(Tab tab) {
                int position = tab.getPosition();
                ItemStatus status = mItemStatus.get(position);
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
     * <p>Add Item where you want to show</p>
     * <p>If you want to set the title textSize,please call {@link #setTextSize(float)}</p>
     * <p>Else if you want to set the bottomMargin,please call {@link #setBottomMargin(int)}</p>
     *
     * @param status Inner class {@link ItemStatus}
     */
    public void addTab(ItemStatus status) {
        View inflate = getTabView(status);
        mItemStatus.add(status);
        mItemViews.add(inflate);
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

    public void setOnCenterTabClickListener(View.OnClickListener onClickListener) {
        mCenterTabOnClickListener = onClickListener;
    }

    /**
     * <p>Must call {@link #addTab(ItemStatus)} before</p>
     *
     * @throws RuntimeException
     */
    public void build() {
        if (mItemViews.isEmpty()) {
            throw new RuntimeException("Must call addTab(ItemStatus status) before!");
        }
        for (int i = 0; i < getTabCount(); i++) {
            XTabLayout.Tab tab = this.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(mItemViews.get(i));
                if (i == 0) {
                    tab.select();
                    TextView view = getTextView(i);
                    if (view != null) {
                        view.setTextColor(mItemStatus.get(i).getSelectedTitleColor());
                    }
                }
            }
        }
        prepareForCenterTab(mCenterTabOnClickListener);
        addOnTabSelectedListener(listener);/*inner listener*/
    }

    /**
     * Get CustomView child TextView
     *
     * @param position index
     * @return TextView
     */
    private TextView getTextView(int position) {
        if (mItemViews != null) {
            ViewGroup parent = (ViewGroup) mItemViews.get(position);
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
        if (mItemViews != null) {
            ViewGroup parent = (ViewGroup) mItemViews.get(position);
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
