package com.AQif.Statusdanwloder;

import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.AQif.Statusdanwloder.fragment.FBHomeFragment;
import com.AQif.Statusdanwloder.fragment.UrlDownloadFragment;
import com.AQif.Statusdanwloder.utils.AdUtils;
import com.AQif.Statusdanwloder.utils.LayManager;

import java.util.ArrayList;
import java.util.List;

public class FBActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb);


        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> onBackPressed());
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(getTabView(i));
        }

        View v = LayoutInflater.from(FBActivity.this).inflate(R.layout.tab_layout, null);
        TextView tabTitle = v.findViewById(R.id.tab_title);
        tabTitle.setBackgroundResource(imageUnPress[0]);
        LinearLayout.LayoutParams param;
        param = LayManager.setLinParams(FBActivity.this, 384, 114);
        tabTitle.setText(R.string.my_ac);
        tabTitle.setTextColor(getResources().getColor(R.color.tab_press_text_color));
        tabTitle.setTypeface(LayManager.getTypeface(FBActivity.this));
        tabTitle.setLayoutParams(param);
        TabLayout.Tab tab = tabLayout.getTabAt(0);
        tab.setCustomView(null);
        tab.setCustomView(v);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                TabLayout.Tab tabs = tabLayout.getTabAt(tab.getPosition());
                tabs.setCustomView(null);
                tabs.setCustomView(getTabViewUn(tab.getPosition()));

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TabLayout.Tab tabs = tabLayout.getTabAt(tab.getPosition());
                tabs.setCustomView(null);
                tabs.setCustomView(getTabView(tab.getPosition()));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.setCurrentItem(0);


        LinearLayout adContainer = findViewById(R.id.banner_container);

        if (!AdUtils.isloadFbAd) {
            //admob
            AdUtils.initAd(FBActivity.this);
            AdUtils.loadBannerAd(FBActivity.this, adContainer);
        } else {
            //Fb banner Ads
            AdUtils.fbBannerAd(FBActivity.this, adContainer);
        }

    }

    @Override
    protected void onDestroy() {
        AdUtils.destroyFbAd();
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private int[] imagePress = {R.drawable.tab_btn_unpress, R.drawable.tab_btn_unpress};
    private int[] imageUnPress = {R.drawable.tab_btn_press, R.drawable.tab_btn_press};

    public View getTabView(int position) {
        View v = LayoutInflater.from(FBActivity.this).inflate(R.layout.tab_layout, null);
        TextView tabTitle =  v.findViewById(R.id.tab_title);
        tabTitle.setBackgroundResource(imagePress[position]);
        LinearLayout.LayoutParams param;
        param = LayManager.setLinParams(FBActivity.this, 384, 114);

        if (position == 0) {
            tabTitle.setText(R.string.my_ac);
            tabTitle.setTextColor(getResources().getColor(R.color.tab_unpress_text_color));
        } else {
            tabTitle.setText(R.string.paste_link);
            tabTitle.setTextColor(getResources().getColor(R.color.tab_unpress_text_color));
        }
        tabTitle.setTypeface(LayManager.getTypeface(FBActivity.this));
        tabTitle.setLayoutParams(param);


        return v;
    }


    public View getTabViewUn(int position) {
        View v = LayoutInflater.from(FBActivity.this).inflate(R.layout.tab_layout, null);
        TextView tabTitle = v.findViewById(R.id.tab_title);
        tabTitle.setBackgroundResource(imageUnPress[position]);
        LinearLayout.LayoutParams param;
        param = LayManager.setLinParams(FBActivity.this, 384, 114);
        if (position == 0) {
            tabTitle.setText(R.string.my_ac);
            tabTitle.setTextColor(getResources().getColor(R.color.tab_press_text_color));
        } else {
            tabTitle.setText(R.string.paste_link);
            tabTitle.setTextColor(getResources().getColor(R.color.tab_press_text_color));
        }
        tabTitle.setTypeface(LayManager.getTypeface(FBActivity.this));
        tabTitle.setLayoutParams(param);


        return v;
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FBHomeFragment(), "My Account");
        adapter.addFragment(new UrlDownloadFragment(), "Paste Link");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: {
                    return FBHomeFragment.newInstance(position);
                }
                case 1: {
                    return UrlDownloadFragment.newInstance(position);
                }
            }
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
