package com.AQif.Statusdanwloder;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.AQif.Statusdanwloder.adapter.WAPagerAdapter;
import com.AQif.Statusdanwloder.fragment.WAStatusFragment;
import com.AQif.Statusdanwloder.utils.AdUtils;
import com.AQif.Statusdanwloder.utils.LayManager;
import com.google.android.gms.ads.AdView;


public class WABusiActivity extends AppCompatActivity implements View.OnClickListener {

    WAPagerAdapter adapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    AdView mAdView;
    ImageView waBusiBtn;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wawhats);

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);
        adapter = new WAPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(getTabView(i));
        }

        View v = LayoutInflater.from(WABusiActivity.this).inflate(R.layout.custom_tab, null);
        TextView img = v.findViewById(R.id.imgView);
        img.setBackgroundResource(imagePress[0]);
        LinearLayout.LayoutParams param;
        param = LayManager.setLinParams(WABusiActivity.this, 384, 114);
        img.setText(R.string.recent_status);
        img.setTextColor(getResources().getColor(R.color.tab_press_text_color));
        img.setTypeface(LayManager.getTypeface(WABusiActivity.this));
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
                    ((WAStatusFragment) WABusiActivity.this.getSupportFragmentManager().findFragmentByTag("android:switcher:" + viewPager.getId() + ":" + tab.getPosition())).loadMedia();
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

        waBusiBtn = findViewById(R.id.waBusiBtn);
        waBusiBtn.setOnClickListener(this);

        LinearLayout adContainer = findViewById(R.id.banner_container);
            if (!AdUtils.isloadFbAd) {
                //admob
                AdUtils.initAd(WABusiActivity.this);
                AdUtils.loadBannerAd(WABusiActivity.this, adContainer);
            } else {
                //Fb banner Ads
                AdUtils.fbBannerAd(WABusiActivity.this, adContainer);
            }
    }

    @Override
    protected void onDestroy() {
        AdUtils.destroyFbAd();
        super.onDestroy();
    }

    private int[] imagePress = {R.drawable.tab_btn_press, R.drawable.tab_btn_press};
    private int[] imageUnPress = {R.drawable.tab_btn_unpress, R.drawable.tab_btn_unpress};

    public View getTabView(int position) {
        View v = LayoutInflater.from(WABusiActivity.this).inflate(R.layout.custom_tab, null);
        TextView img = v.findViewById(R.id.imgView);
        img.setBackgroundResource(imageUnPress[position]);
        LinearLayout.LayoutParams param;
        param = LayManager.setLinParams(WABusiActivity.this, 384, 114);

        if (position == 0) {
            img.setText(R.string.recent_status);
            img.setTextColor(getResources().getColor(R.color.tab_unpress_text_color));
        } else {
            img.setText(R.string.help);
            img.setTextColor(getResources().getColor(R.color.tab_unpress_text_color));
        }
        img.setTypeface(LayManager.getTypeface(WABusiActivity.this));
        img.setLayoutParams(param);
        return v;
    }

    public View getTabViewUn(int position) {
        View v = LayoutInflater.from(WABusiActivity.this).inflate(R.layout.custom_tab, null);
        TextView img = v.findViewById(R.id.imgView);
        img.setBackgroundResource(imagePress[position]);

        LinearLayout.LayoutParams param;
        param = LayManager.setLinParams(WABusiActivity.this, 384, 114);

        if (position == 0) {
            img.setText(R.string.recent_status);
            img.setTextColor(getResources().getColor(R.color.tab_press_text_color));
        } else {
            img.setText(R.string.help);
            img.setTextColor(getResources().getColor(R.color.tab_press_text_color));
        }
        img.setTypeface(LayManager.getTypeface(WABusiActivity.this));
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
            case R.id.waBusiBtn:
                Intent localIntent = getPackageManager().getLaunchIntentForPackage("com.whatsapp.w4b");
                boolean installed = appInstalledOrNot("com.whatsapp.w4b");
                if (installed) {
                    try {
                        startActivity(localIntent);
                    } catch (ActivityNotFoundException localActivityNotFoundException) {
                        Toast.makeText(this, "Please Install WA Business First For Use This App.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp.w4b")));
                    }
                } else {
                    Toast.makeText(this, "Please Install WA Business First For Use This App.", Toast.LENGTH_SHORT).show();
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
