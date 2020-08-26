package com.AQif.Statusdanwloder;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.AQif.Statusdanwloder.callapi.CommonClassForAPI;
import com.AQif.Statusdanwloder.model.TwitterResponse;
import com.AQif.Statusdanwloder.utils.AdUtils;
import com.AQif.Statusdanwloder.utils.LayManager;
import com.AQif.Statusdanwloder.utils.Utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import io.reactivex.observers.DisposableObserver;

public class TwetActivity extends AppCompatActivity {

    ImageView tweatBtn, backBtn;

    EditText linkEdt;
    TextView downloadBtn;
    CommonClassForAPI commonClassForAPI;

    ImageView help1, help2, help3, help4, help5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twet);

        commonClassForAPI = CommonClassForAPI.getInstance(TwetActivity.this);

        ((TextView) findViewById(R.id.txt)).setTypeface(LayManager.getTypeface(TwetActivity.this));
        ((TextView) findViewById(R.id.textView1)).setTypeface(LayManager.getTypeface(TwetActivity.this));
        ((TextView) findViewById(R.id.textView2)).setTypeface(LayManager.getSubTypeface(TwetActivity.this));
        ((TextView) findViewById(R.id.textView3)).setTypeface(LayManager.getTypeface(TwetActivity.this));
        ((TextView) findViewById(R.id.textView4)).setTypeface(LayManager.getSubTypeface(TwetActivity.this));
        ((TextView) findViewById(R.id.textView5)).setTypeface(LayManager.getTypeface(TwetActivity.this));
        ((TextView) findViewById(R.id.textView6)).setTypeface(LayManager.getSubTypeface(TwetActivity.this));
        ((TextView) findViewById(R.id.textView7)).setTypeface(LayManager.getTypeface(TwetActivity.this));
        ((TextView) findViewById(R.id.textView8)).setTypeface(LayManager.getSubTypeface(TwetActivity.this));
        ((TextView) findViewById(R.id.textView9)).setTypeface(LayManager.getSubTypeface(TwetActivity.this));
        ((TextView) findViewById(R.id.textView10)).setTypeface(LayManager.getSubTypeface(TwetActivity.this));

        help1 = findViewById(R.id.help1);
        help2 = findViewById(R.id.help2);
        help3 = findViewById(R.id.help3);
        help4 = findViewById(R.id.help4);
        help5 = findViewById(R.id.help5);

        Glide.with(TwetActivity.this)
                .load(R.drawable.twet_1)
                .into(help1);

        Glide.with(TwetActivity.this)
                .load(R.drawable.twet_2)
                .into(help2);

        Glide.with(TwetActivity.this)
                .load(R.drawable.twet_3)
                .into(help3);

        Glide.with(TwetActivity.this)
                .load(R.drawable.twet_4)
                .into(help4);

        Glide.with(TwetActivity.this)
                .load(R.drawable.intro04)
                .into(help5);


        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> onBackPressed());

        tweatBtn = findViewById(R.id.tweatBtn);
        tweatBtn.setOnClickListener(v -> openTwitter());

        linkEdt = findViewById(R.id.linkEdt);
        downloadBtn = findViewById(R.id.downloadBtn);
        downloadBtn.setTypeface(LayManager.getTypeface(TwetActivity.this));
        downloadBtn.setOnClickListener(v -> {
            String url = linkEdt.getText().toString();
            if (url.equals("")) {
                Toast.makeText(TwetActivity.this, "Please paste url and download!!!!", Toast.LENGTH_SHORT).show();
            } else if (!Patterns.WEB_URL.matcher(url).matches()) {
                Toast.makeText(TwetActivity.this, "Invalid URL!", Toast.LENGTH_SHORT).show();
            } else {
                Utils.showProgressDialog(TwetActivity.this);
                getTwitterData(url);
            }
        });

        LinearLayout adContainer = findViewById(R.id.banner_container);
        LinearLayout adaptiveAdContainer = findViewById(R.id.banner_adaptive_container);
        if (!AdUtils.isloadFbAd) {
            //admob
            AdUtils.initAd(TwetActivity.this);
            AdUtils.loadBannerAd(TwetActivity.this, adContainer);
            AdUtils.adptiveBannerAd(TwetActivity.this, adaptiveAdContainer);
        } else {
            //Fb banner Ads
            AdUtils.fbBannerAd(TwetActivity.this, adContainer);
            AdUtils.fbAdaptiveBannerAd(TwetActivity.this, adaptiveAdContainer);
        }
    }


    private void getTwitterData(String tUrl) {
        try {
            URL url = new URL(tUrl);
            String host = url.getHost();
            if (host.contains("twitter.com")) {
                Long id = getTweetId(tUrl);
                if (id != null) {
                    callGetTwitterData(String.valueOf(id));
                }
            } else {
                Toast.makeText(TwetActivity.this, "Please paste url and download!!!!", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Long getTweetId(String s) {
        try {
            String[] split = s.split("\\/");
            String id = split[5].split("\\?")[0];
            return Long.parseLong(id);
        } catch (Exception e) {
            Log.d("TAG", "getTweetId: " + e.getLocalizedMessage());
            return null;
        }
    }

    private String videoUrl;
    private void callGetTwitterData(String id) {
        String url = "https://twittervideodownloaderpro.com/twittervideodownloadv2/index.php";
        try {
            Utils utils = new Utils(TwetActivity.this);
            if (utils.isNetworkAvailable()) {
                if (commonClassForAPI != null) {
                    Utils.showProgressDialog(TwetActivity.this);
                    commonClassForAPI.callTwitterApi(observer,url,id);
                }
            } else {
                Toast.makeText(TwetActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private DisposableObserver<TwitterResponse> observer = new DisposableObserver<TwitterResponse>() {
        @Override
        public void onNext(TwitterResponse twitterResponse) {
            Utils.hideProgressDialog();
            try {
                videoUrl = twitterResponse.getVideos().get(0).getUrl();
                if (twitterResponse.getVideos().get(0).getType().equals("image")){
                    Utils.startDownload(videoUrl, Utils.twitterDirPath, TwetActivity.this, getFilenameFromURL(videoUrl,"image"));
                    linkEdt.setText("");
                }else {
                    videoUrl = twitterResponse.getVideos().get(twitterResponse.getVideos().size()-1).getUrl();
                    Utils.startDownload(videoUrl, Utils.twitterDirPath, TwetActivity.this, getFilenameFromURL(videoUrl,"mp4"));
                    linkEdt.setText("");
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(TwetActivity.this, "No Media on Tweet or Invalid Link", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(Throwable e) {
            Utils.hideProgressDialog();
            e.printStackTrace();

        }

        @Override
        public void onComplete() {
            Utils.hideProgressDialog();
        }
    };

    public String getFilenameFromURL(String url, String type) {
        if (type.equals("image")){
            try {
                return new File(new URL(url).getPath()).getName() + "";
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return System.currentTimeMillis() + ".jpg";
            }
        }else {
            try {
                return new File(new URL(url).getPath()).getName() + "";
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return System.currentTimeMillis() + ".mp4";
            }
        }
    }

    @Override
    protected void onDestroy() {
        AdUtils.destroyFbAd();
        super.onDestroy();
    }

    private void openTwitter() {
        try {
            Intent i = this.getPackageManager().getLaunchIntentForPackage("com.twitter.android");
            this.startActivity(i);
        } catch (Exception var4) {
            Toast.makeText(getApplicationContext(), R.string.twitter_not_found, Toast.LENGTH_SHORT).show();
            this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + "com.twitter.android")));
        }
    }
}
