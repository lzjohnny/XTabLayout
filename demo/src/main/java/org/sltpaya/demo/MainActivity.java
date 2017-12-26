package org.sltpaya.demo;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import org.sltpaya.tablayout.TabLayoutBuilder;

public class MainActivity extends AppCompatActivity {

    //Tab count
    private int tabCount;

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
        //Tab count
        tabCount = 4;
        //The textColor
        final int[] textColor = {0xff333333, 0xffffffff};
        //The Image res id
        int[] resId = {
                R.drawable.heart_selector,
                R.drawable.ufo_selector,
                R.drawable.diamond_selector,
                R.drawable.mine_selector
        };
        //The title
        String[] title = {
                resources.getString(R.string.log),
                resources.getString(R.string.file),
                resources.getString(R.string.tool),
                resources.getString(R.string.setting),
        };
        //init viewpager
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager(),title));
        //init TabLayout
        tabLayout.setupWithViewPager(viewPager);//setting up this TabLayout with ViewPager
        tabLayout.setBottomMargin(2);//set the bottomMargin --unit:dp
        tabLayout.setTextSize(12);//set title size --unit:sp
        //add tab to TabLayout
        for (int i = 0; i < tabCount; i++) {
            tabLayout.addTab(new TabLayoutBuilder.ItemStatus(title[i], resId[i], textColor[0], textColor[1]));
        }
        //show tabView to your screen
        tabLayout.build();
    }


    private class MyAdapter extends FragmentPagerAdapter {

        private String[] titles;
        MyAdapter(FragmentManager fm,String[] titles) {
            super(fm);
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            args.putString("title",titles[position]);
            BlankFragment fragment = new BlankFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }
}
