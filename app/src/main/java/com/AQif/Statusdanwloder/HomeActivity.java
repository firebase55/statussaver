package com.AQif.Statusdanwloder;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.AQif.Statusdanwloder.utils.AdUtils;
import com.AQif.Statusdanwloder.utils.Helpers;
import com.AQif.Statusdanwloder.utils.LayManager;

import static android.view.View.*;

public class HomeActivity extends AppCompatActivity implements OnClickListener {

    RelativeLayout wsBtn;
    RelativeLayout waBusiBtn;
    RelativeLayout insBtn;
    RelativeLayout tokBtn;
    RelativeLayout likBtn;
    RelativeLayout fbBtn;
    RelativeLayout tweatBtn;
    RelativeLayout galBtn;
    ImageView moreapp;
    ImageView policy;
    ImageView shareapp;
    ImageView rateapp;

    String[] permissionsList = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

        LinearLayout adContainer = findViewById(R.id.banner_container);

        if (!AdUtils.isloadFbAd) {
            //admob
            AdUtils.initAd(HomeActivity.this);
            AdUtils.loadBannerAd(HomeActivity.this, adContainer);
            AdUtils.loadInterAd(HomeActivity.this);
        } else {
            //Fb banner Ads
            AdUtils.fbBannerAd(HomeActivity.this, adContainer);
            AdUtils.loadFbInterAd(HomeActivity.this);
        }


    }

    void init() {
        wsBtn = findViewById(R.id.wsBtn);
        wsBtn.setOnClickListener(this);
        waBusiBtn = findViewById(R.id.waBusiBtn);
        waBusiBtn.setOnClickListener(this);
        insBtn = findViewById(R.id.insBtn);
        insBtn.setOnClickListener(this);
        tokBtn = findViewById(R.id.tokBtn);
        tokBtn.setOnClickListener(this);
        fbBtn = findViewById(R.id.fbBtn);
        fbBtn.setOnClickListener(this);
        galBtn = findViewById(R.id.galBtn);
        galBtn.setOnClickListener(this);
        tweatBtn = findViewById(R.id.tweatBtn);
        tweatBtn.setOnClickListener(this);
        likBtn = findViewById(R.id.likBtn);
        likBtn.setOnClickListener(this);

        moreapp = findViewById(R.id.moreapp);
        moreapp.setOnClickListener(this);
        policy = findViewById(R.id.policy);
        policy.setOnClickListener(this);
        shareapp = findViewById(R.id.shareapp);
        shareapp.setOnClickListener(this);
        rateapp = findViewById(R.id.rateapp);
        rateapp.setOnClickListener(this);

        ((TextView) findViewById(R.id.txt)).setTypeface(LayManager.getTypeface(HomeActivity.this));
        ((TextView) findViewById(R.id.txt1)).setTypeface(LayManager.getTypeface(HomeActivity.this));
        ((TextView) findViewById(R.id.txt2)).setTypeface(LayManager.getTypeface(HomeActivity.this));
        ((TextView) findViewById(R.id.txt3)).setTypeface(LayManager.getTypeface(HomeActivity.this));
        ((TextView) findViewById(R.id.txt4)).setTypeface(LayManager.getTypeface(HomeActivity.this));
        ((TextView) findViewById(R.id.txt5)).setTypeface(LayManager.getTypeface(HomeActivity.this));
        ((TextView) findViewById(R.id.txt6)).setTypeface(LayManager.getTypeface(HomeActivity.this));
        ((TextView) findViewById(R.id.txt7)).setTypeface(LayManager.getTypeface(HomeActivity.this));
        ((TextView) findViewById(R.id.txt8)).setTypeface(LayManager.getTypeface(HomeActivity.this));

    }

    @Override
    protected void onDestroy() {
        AdUtils.destroyFbAd();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wsBtn:
                if (!checkPermissions(this, permissionsList)) {
                    ActivityCompat.requestPermissions(this, permissionsList, 21);
                } else {
                    startActivityes(new Intent(HomeActivity.this, WAppActivity.class));
                }
                break;

            case R.id.waBusiBtn:
                if (!checkPermissions(this, permissionsList)) {
                    ActivityCompat.requestPermissions(this, permissionsList, 21);
                } else {
                    startActivityes(new Intent(HomeActivity.this, WABusiActivity.class));
                }
                break;

            case R.id.insBtn:
                if (!checkPermissions(this, permissionsList)) {
                    ActivityCompat.requestPermissions(this, permissionsList, 21);
                } else {
                    startActivityes(new Intent(HomeActivity.this, InstaActivity.class));
                }
                break;

            case R.id.likBtn:
                if (!checkPermissions(this, permissionsList)) {
                    ActivityCompat.requestPermissions(this, permissionsList, 21);
                } else {
                    startActivityes(new Intent(HomeActivity.this, LikeeActivity.class));
                }
                break;

            case R.id.tokBtn:
                if (!checkPermissions(this, permissionsList)) {
                    ActivityCompat.requestPermissions(this, permissionsList, 21);
                } else {
                    startActivityes(new Intent(HomeActivity.this, TikActivity.class));
                }
                break;

            case R.id.fbBtn:
                if (!checkPermissions(this, permissionsList)) {
                    ActivityCompat.requestPermissions(this, permissionsList, 21);
                } else {
                    startActivityes(new Intent(HomeActivity.this, FBActivity.class));
                }
                break;

            case R.id.tweatBtn:
                if (!checkPermissions(this, permissionsList)) {
                    ActivityCompat.requestPermissions(this, permissionsList, 21);
                } else {
                    startActivityes(new Intent(HomeActivity.this, TwetActivity.class));
                }
                break;

            case R.id.galBtn:
                if (!checkPermissions(this, permissionsList)) {
                    ActivityCompat.requestPermissions(this, permissionsList, 21);
                } else {
                    startActivityes(new Intent(HomeActivity.this, MyGalleryActivity.class));
                }
                break;

            case R.id.rateapp:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                }
                break;

            case R.id.shareapp:
                Helpers.mShareText("Download this awesome app\n https://play.google.com/store/apps/details?id=" + getPackageName() + " \n", HomeActivity.this);
                break;

            case R.id.policy:
                startActivityes(new Intent(HomeActivity.this, PrivacyActivity.class));
                break;

            case R.id.moreapp:
                startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/dev?id=5700313618786177705&hl=en")));
                break;

                default:
                    break;
        }
    }

    void startActivityes(Intent intent) {
        if (!AdUtils.isloadFbAd) {
            AdUtils.adCounter++;
            AdUtils.showInterAd(HomeActivity.this, intent);
        } else {
            AdUtils.adCounter++;
            AdUtils.showFbInterAd(HomeActivity.this, intent);
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (this.doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000L);
    }


    public static boolean checkPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 21 && !checkPermissions(this, permissionsList)) {
                ActivityCompat.requestPermissions(this, permissionsList, 21);
        }
    }

}
