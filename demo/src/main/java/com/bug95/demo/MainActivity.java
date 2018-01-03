package com.bug95.demo;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.bug95.tablayout.OnCenterTabSelectedListener;
import com.bug95.tablayout.TabLayoutBuilder;
import com.bug95.tablayout.XPagerAdapter;
import com.bug95.tablayout.XTabLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        Resources resources = getResources();
        //inflate TabLayoutBuilder
        TabLayoutBuilder tabLayout = (TabLayoutBuilder) findViewById(R.id.tab_layout);
        int[] resId = {
                R.drawable.heart_selector,
                R.drawable.ufo_selector,
                R.drawable.diamond_selector,
                R.drawable.mine_selector
        };
        String[] tabTitle = {
                resources.getString(R.string.log),
                resources.getString(R.string.file),
                resources.getString(R.string.tool),
                resources.getString(R.string.setting),
        };

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager(), tabTitle));

        tabLayout.setupWithXViewPager(viewPager); //在这里会完成Tab的实例化，接下来只需要设置Tab的布局View
        tabLayout.setTabIcon(resId);
        tabLayout.setTabTitle(tabTitle);
        tabLayout.setCenterIcon(R.drawable.center_tab_selector);
        tabLayout.setCenterTitle("中心按钮");
        tabLayout.setTextColorId(R.drawable.text_color_selector);
        tabLayout.setBottomMargin(2);//set the bottomMargin --unit:dp
        tabLayout.setTextSize(12);//set title size --unit:sp
        tabLayout.setOnCenterTabSelectedListener(new OnCenterTabSelectedListener() {
            @Override
            public void onCenterTabSelected(XTabLayout.Tab tab) {
                Toast.makeText(MainActivity.this, "选中", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCenterTabUnselected(XTabLayout.Tab tab) {
                Toast.makeText(MainActivity.this, "未选中", Toast.LENGTH_SHORT).show();
            }
        });

        tabLayout.build();
    }


    private class MyAdapter extends XPagerAdapter {

        private String[] titles;

        MyAdapter(FragmentManager fm) {
            super(fm);
        }

        MyAdapter(FragmentManager fm, String[] titles) {
            super(fm);
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            BlankFragment fragment = new BlankFragment();
            if (titles != null && position < titles.length) {
                Bundle args = new Bundle();
                args.putString("title", titles[position]);
                fragment.setArguments(args);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
