package com.AQif.Statusdanwloder;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.AQif.Statusdanwloder.adapter.MainPagerAdapter;
import com.AQif.Statusdanwloder.fragment.WAppStatusFrag;
import com.AQif.Statusdanwloder.utils.AdUtils;
import com.AQif.Statusdanwloder.utils.LayManager;
import com.google.android.gms.ads.AdView;

public class WAppActivity extends AppCompatActivity implements View.OnClickListener {

    MainPagerAdapter adapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    AdView mAdView;
    ImageView wappBtn;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wapp);

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);
        adapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);


        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(getTabView(i));
        }

        View v = LayoutInflater.from(WAppActivity.this).inflate(R.layout.custom_tab, null);
        TextView img = v.findViewById(R.id.imgView);
        img.setBackgroundResource(imageUnPress[0]);
        LinearLayout.LayoutParams param;
        param = LayManager.setLinParams(WAppActivity.this, 384, 114);
        img.setText(R.string.recent_status);
        img.setTextColor(getResources().getColor(R.color.tab_press_text_color));
        img.setTypeface(LayManager.getTypeface(WAppActivity.this));
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


                if (tab.getPosition() == 0) {
                    ((WAppStatusFrag) WAppActivity.this.getSupportFragmentManager().findFragmentByTag("android:switcher:" + viewPager.getId() + ":" + tab.getPosition())).loadMedia();
                }
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


        wappBtn = findViewById(R.id.wappBtn);
        wappBtn.setOnClickListener(this);



        LinearLayout adContainer = findViewById(R.id.banner_container);
            if (!AdUtils.isloadFbAd) {
                //admob
                AdUtils.initAd(WAppActivity.this);
                AdUtils.loadBannerAd(WAppActivity.this, adContainer);
            } else {
                //Fb banner Ads
                AdUtils.fbBannerAd(WAppActivity.this, adContainer);
            }

    }

    @Override
    protected void onDestroy() {
        AdUtils.destroyFbAd();
        super.onDestroy();
    }

    private int[] imageUnPress = {R.drawable.tab_btn_press, R.drawable.tab_btn_press};
    private int[] imagePress = {R.drawable.tab_btn_unpress, R.drawable.tab_btn_unpress};

    public View getTabView(int position) {
        View v = LayoutInflater.from(WAppActivity.this).inflate(R.layout.custom_tab, null);
        TextView img =  v.findViewById(R.id.imgView);
        img.setBackgroundResource(imagePress[position]);
        LinearLayout.LayoutParams param;
        param = LayManager.setLinParams(WAppActivity.this, 384, 114);

        if (position == 0) {
            img.setText(R.string.recent_status);
            img.setTextColor(getResources().getColor(R.color.tab_unpress_text_color));
        } else {
            img.setText(R.string.help);
            img.setTextColor(getResources().getColor(R.color.tab_unpress_text_color));
        }
        img.setTypeface(LayManager.getTypeface(WAppActivity.this));
        img.setLayoutParams(param);


        return v;
    }


    public View getTabViewUn(int position) {
        View v = LayoutInflater.from(WAppActivity.this).inflate(R.layout.custom_tab, null);
        TextView img = v.findViewById(R.id.imgView);
        img.setBackgroundResource(imageUnPress[position]);
        LinearLayout.LayoutParams param;
        param = LayManager.setLinParams(WAppActivity.this, 384, 114);

        if (position == 0) {
            img.setText(R.string.recent_status);
            img.setTextColor(getResources().getColor(R.color.tab_press_text_color));
        } else {
            img.setText(R.string.help);
            img.setTextColor(getResources().getColor(R.color.tab_press_text_color));
        }
        img.setTypeface(LayManager.getTypeface(WAppActivity.this));
        img.setLayoutParams(param);


        return v;
    }


    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }




    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wappBtn:
                Intent localIntent = getPackageManager().getLaunchIntentForPackage("com.whatsapp");
                boolean installed = appInstalledOrNot("com.whatsapp");
                if (installed) {
                    try {
                        startActivity(localIntent);
                    } catch (ActivityNotFoundException localActivityNotFoundException) {
                        Toast.makeText(this, "Please Install Whatsapp First For Use This App.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp")));
                    }
                } else {
                    Toast.makeText(this, "Please Install Whatsapp First For Use This App.", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.backBtn:
                onBackPressed();
                break;

            default:
                break;
        }
    }
}
