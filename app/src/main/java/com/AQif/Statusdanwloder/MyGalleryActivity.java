package com.AQif.Statusdanwloder;

import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.AQif.Statusdanwloder.adapter.GalleryPagerAdapter;
import com.AQif.Statusdanwloder.utils.AdUtils;
import com.AQif.Statusdanwloder.utils.LayManager;

public class MyGalleryActivity extends AppCompatActivity {

    ImageView backBtn;
    TextView headerTxt;

    ViewPager viewPager;
    TabLayout tabLayout;
    GalleryPagerAdapter adapter;

    private String[] imagePress = { "Whatsapp", "WA Business", "Instagram", "Tiktok", "Likee", "Facebook", "Twitter"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_gallery);

        headerTxt = findViewById(R.id.headerTxt);
        headerTxt.setTypeface(LayManager.getTypeface(MyGalleryActivity.this));

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> onBackPressed());


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(7);
        adapter = new GalleryPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);


        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(getTabView(i));
        }

        View v = LayoutInflater.from(MyGalleryActivity.this).inflate(R.layout.custom_tab, null);
        TextView img = v.findViewById(R.id.imgView);
        img.setBackgroundResource(R.drawable.tab_btn_press);
        img.setText(R.string.whatsapp);
        img.setTypeface(LayManager.getTypeface(MyGalleryActivity.this));
        img.setTextColor(getResources().getColor(R.color.tab_press_text_color));
        LinearLayout.LayoutParams param;
        param = LayManager.setLinParams(MyGalleryActivity.this, 250, 90);
        img.setLayoutParams(param);
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
            AdUtils.initAd(MyGalleryActivity.this);
            AdUtils.loadBannerAd(MyGalleryActivity.this, adContainer);
            AdUtils.loadInterAd(MyGalleryActivity.this);
        } else {
            //Fb banner Ads
            AdUtils.fbBannerAd(MyGalleryActivity.this, adContainer);
            AdUtils.loadFbInterAd(MyGalleryActivity.this);
        }

    }

    @Override
    protected void onDestroy() {
        AdUtils.destroyFbAd();
        super.onDestroy();
    }


    public View getTabView(int position) {
        View v = LayoutInflater.from(MyGalleryActivity.this).inflate(R.layout.custom_tab, null);
        TextView img =  v.findViewById(R.id.imgView);
        img.setText(imagePress[position]);
        img.setBackgroundResource(R.drawable.tab_btn_unpress);
        img.setTextColor(getResources().getColor(R.color.tab_unpress_text_color));
        img.setTypeface(LayManager.getTypeface(MyGalleryActivity.this));
        LinearLayout.LayoutParams param;
        param = LayManager.setLinParams(MyGalleryActivity.this, 250, 90);
        img.setLayoutParams(param);
        return v;
    }


    public View getTabViewUn(int position) {
        View v = LayoutInflater.from(MyGalleryActivity.this).inflate(R.layout.custom_tab, null);
        TextView img =  v.findViewById(R.id.imgView);
        img.setBackgroundResource(R.drawable.tab_btn_press);
        img.setText(imagePress[position]);
        img.setTextColor(getResources().getColor(R.color.tab_press_text_color));
        img.setTypeface(LayManager.getTypeface(MyGalleryActivity.this));
        LinearLayout.LayoutParams param;
        param = LayManager.setLinParams(MyGalleryActivity.this, 250, 90);
        img.setLayoutParams(param);
        return v;
    }

}
