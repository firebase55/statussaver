package com.AQif.Statusdanwloder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;

import android.util.Patterns;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.AQif.Statusdanwloder.service.InstaFetch;
import com.AQif.Statusdanwloder.utils.AdUtils;
import com.AQif.Statusdanwloder.utils.LayManager;
import com.AQif.Statusdanwloder.utils.SharePrefs;

public class InstaActivity extends Activity {

    ImageView instaBtn, backBtn;
    EditText linkEdt;
    TextView downloadBtn;
    ImageView help1, help2, help3, help4;

    Switch instaLoginSwitch;
    RelativeLayout instaLoginLay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insta);


        ((TextView) findViewById(R.id.txtSwitch)).setTypeface(LayManager.getTypeface(InstaActivity.this));
        ((TextView) findViewById(R.id.txt)).setTypeface(LayManager.getTypeface(InstaActivity.this));
        ((TextView) findViewById(R.id.textView1)).setTypeface(LayManager.getTypeface(InstaActivity.this));
        ((TextView) findViewById(R.id.textView2)).setTypeface(LayManager.getSubTypeface(InstaActivity.this));
        ((TextView) findViewById(R.id.textView3)).setTypeface(LayManager.getTypeface(InstaActivity.this));
        ((TextView) findViewById(R.id.textView4)).setTypeface(LayManager.getSubTypeface(InstaActivity.this));
        ((TextView) findViewById(R.id.textView5)).setTypeface(LayManager.getTypeface(InstaActivity.this));
        ((TextView) findViewById(R.id.textView6)).setTypeface(LayManager.getSubTypeface(InstaActivity.this));
        ((TextView) findViewById(R.id.textView7)).setTypeface(LayManager.getTypeface(InstaActivity.this));
        ((TextView) findViewById(R.id.textView8)).setTypeface(LayManager.getSubTypeface(InstaActivity.this));

        help1 = findViewById(R.id.help1);
        help2 = findViewById(R.id.help2);
        help3 = findViewById(R.id.help3);
        help4 = findViewById(R.id.help4);

        Glide.with(InstaActivity.this)
                .load(R.drawable.intro01)
                .into(help1);

        Glide.with(InstaActivity.this)
                .load(R.drawable.intro02)
                .into(help2);

        Glide.with(InstaActivity.this)
                .load(R.drawable.intro03)
                .into(help3);

        Glide.with(InstaActivity.this)
                .load(R.drawable.intro04)
                .into(help4);

        linkEdt = findViewById(R.id.linkEdt);
        downloadBtn = findViewById(R.id.downloadBtn);
        downloadBtn.setTypeface(LayManager.getTypeface(InstaActivity.this));
        downloadBtn.setOnClickListener(view -> {

            final String url = linkEdt.getText().toString();

            if (url.equals("")) {
                Toast.makeText(InstaActivity.this, "Please paste url and download!!!!", Toast.LENGTH_SHORT).show();
            } else if (!Patterns.WEB_URL.matcher(url).matches()) {
                Toast.makeText(InstaActivity.this, "Invalid URL!", Toast.LENGTH_SHORT).show();
            } else {
                new InstaFetch().GetInstagramData(InstaActivity.this, url);
                linkEdt.getText().clear();
            }
        });


        instaBtn = findViewById(R.id.instaBtn);
        instaBtn.setOnClickListener(v -> launchInstagram());

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> onBackPressed());


        instaLoginSwitch = findViewById(R.id.instaLoginSwitch);
        if (SharePrefs.getInstance(this).getBoolean(SharePrefs.ISINSTALOGIN)) {
            instaLoginSwitch.setChecked(true);
        } else {
            instaLoginSwitch.setChecked(false);
        }

        instaLoginLay = findViewById(R.id.instaLoginLay);
        instaLoginLay.setOnClickListener(v -> {
            if (!SharePrefs.getInstance(InstaActivity.this).getBoolean(SharePrefs.ISINSTALOGIN)) {
                Intent intent = new Intent(InstaActivity.this,
                        InstaLoginActivity.class);
                startActivityForResult(intent, 100);
            } else {
                AlertDialog.Builder ab = new AlertDialog.Builder(InstaActivity.this);
                ab.setPositiveButton("Yes", (dialog, id) -> {
                    SharePrefs.getInstance(InstaActivity.this).putBoolean(SharePrefs.ISINSTALOGIN, false);
                    SharePrefs.getInstance(InstaActivity.this).putString(SharePrefs.COOKIES, "");
                    SharePrefs.getInstance(InstaActivity.this).putString(SharePrefs.CSRF, "");
                    SharePrefs.getInstance(InstaActivity.this).putString(SharePrefs.SESSIONID, "");
                    SharePrefs.getInstance(InstaActivity.this).putString(SharePrefs.USERID, "");

                    if (SharePrefs.getInstance(InstaActivity.this).getBoolean(SharePrefs.ISINSTALOGIN)) {
                        instaLoginSwitch.setChecked(true);
                    } else {
                        instaLoginSwitch.setChecked(false);
                    }
                    dialog.cancel();

                });
                ab.setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());
                AlertDialog alert = ab.create();
                alert.setTitle("You Don't Want to Download Media from Private Account?");
                alert.show();
            }

        });


        LinearLayout adContainer = findViewById(R.id.banner_container);
        LinearLayout adaptiveAdContainer = findViewById(R.id.banner_adaptive_container);
        if (!AdUtils.isloadFbAd) {
            //admob
            AdUtils.initAd(InstaActivity.this);
            AdUtils.loadBannerAd(InstaActivity.this, adContainer);
            AdUtils.adptiveBannerAd(InstaActivity.this, adaptiveAdContainer);
        } else {
            //Fb banner Ads
            AdUtils.fbBannerAd(InstaActivity.this, adContainer);
            AdUtils.fbAdaptiveBannerAd(InstaActivity.this, adaptiveAdContainer);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 100 && resultCode == RESULT_OK) {
                if (SharePrefs.getInstance(InstaActivity.this).getBoolean(SharePrefs.ISINSTALOGIN)) {
                    instaLoginSwitch.setChecked(true);
                } else {
                    instaLoginSwitch.setChecked(false);
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        InstaFetch instaFetch = new InstaFetch();
        instaFetch.destroy();
        AdUtils.destroyFbAd();
        super.onDestroy();
    }

    public void launchInstagram() {
        String instagramApp = "com.instagram.android";
        try {
            Intent intent = getPackageManager().getLaunchIntentForPackage(instagramApp);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.instagram_not_found, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+instagramApp)));
        }
    }



}
