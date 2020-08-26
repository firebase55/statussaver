package com.AQif.Statusdanwloder;


import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;


import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.AQif.Statusdanwloder.utils.AdUtils;
import com.AQif.Statusdanwloder.utils.LayManager;
import com.AQif.Statusdanwloder.utils.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class TikActivity extends Activity {
    ImageView tikBtn, backBtn;


    EditText linkEdt;
    TextView downloadBtn;
    ArrayList<String> trueLink;

    ImageView help1, help2, help3, help4;

    String playURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tik);


        ((TextView) findViewById(R.id.txt)).setTypeface(LayManager.getTypeface(TikActivity.this));
        ((TextView) findViewById(R.id.textView1)).setTypeface(LayManager.getTypeface(TikActivity.this));
        ((TextView) findViewById(R.id.textView2)).setTypeface(LayManager.getSubTypeface(TikActivity.this));
        ((TextView) findViewById(R.id.textView3)).setTypeface(LayManager.getTypeface(TikActivity.this));
        ((TextView) findViewById(R.id.textView4)).setTypeface(LayManager.getSubTypeface(TikActivity.this));
        ((TextView) findViewById(R.id.textView5)).setTypeface(LayManager.getTypeface(TikActivity.this));
        ((TextView) findViewById(R.id.textView6)).setTypeface(LayManager.getSubTypeface(TikActivity.this));
        ((TextView) findViewById(R.id.textView7)).setTypeface(LayManager.getTypeface(TikActivity.this));
        ((TextView) findViewById(R.id.textView8)).setTypeface(LayManager.getSubTypeface(TikActivity.this));

        help1 = findViewById(R.id.help1);
        help2 = findViewById(R.id.help2);
        help3 = findViewById(R.id.help3);
        help4 = findViewById(R.id.help4);

        Glide.with(TikActivity.this)
                .load(R.drawable.tiktok_step01)
                .into(help1);

        Glide.with(TikActivity.this)
                .load(R.drawable.tiktok_step02)
                .into(help2);

        Glide.with(TikActivity.this)
                .load(R.drawable.tiktok_step03)
                .into(help3);

        Glide.with(TikActivity.this)
                .load(R.drawable.intro04)
                .into(help4);

        linkEdt = findViewById(R.id.linkEdt);
        downloadBtn = findViewById(R.id.downloadBtn);
        downloadBtn.setTypeface(LayManager.getSubTypeface(TikActivity.this));
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils utils = new Utils(TikActivity.this);
                if (utils.isNetworkAvailable()) {
                    if (linkEdt.getText().toString().trim().length() == 0) {
                        Toast.makeText(TikActivity.this, "Please paste url and download!!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        final String url = linkEdt.getText().toString();
                        if (url.contains("tiktok.com/")) {
                            Log.e("tiktok", url);
                            trueLink = getURLS(url);
                            saveVideo(trueLink.get(0));
                            linkEdt.getText().clear();
                        } else {
                            Toast.makeText(TikActivity.this, "Url not exists!!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    Toast.makeText(TikActivity.this, "Internet Connection not available!!!!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        tikBtn = findViewById(R.id.tweatBtn);
        tikBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openTok();


            }
        });

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        LinearLayout adContainer = findViewById(R.id.banner_container);
        LinearLayout adaptiveAdContainer = findViewById(R.id.banner_adaptive_container);
        if (!AdUtils.isloadFbAd) {
            //admob
            AdUtils.initAd(TikActivity.this);
            AdUtils.loadBannerAd(TikActivity.this, adContainer);
            AdUtils.adptiveBannerAd(TikActivity.this, adaptiveAdContainer);
        } else {
            //Fb banner Ads
            AdUtils.fbBannerAd(TikActivity.this, adContainer);
            AdUtils.fbAdaptiveBannerAd(TikActivity.this, adaptiveAdContainer);
        }

    }

    @Override
    protected void onDestroy() {
        AdUtils.destroyFbAd();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public String withoutWatermark(String url) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            httpURLConnection.getResponseCode();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    stringBuffer.append(readLine);
                    if (stringBuffer.toString().contains("vid:")) {
                        try {
                            if (stringBuffer.substring(stringBuffer.indexOf("vid:")).substring(0, 4).equals("vid:")) {
                                String substring = stringBuffer.substring(stringBuffer.indexOf("vid:"));
                                String trim = substring.substring(4, substring.indexOf("%")).replaceAll("[^A-Za-z0-9]", "").trim();
                                return "http://api2.musical.ly/aweme/v1/playwm/?video_id=" + trim;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            return "";
        }
    }


    public final ArrayList<String> getURLS(String value) {
        ArrayList<String> urls = new ArrayList<>();
        String feggy = "\\(?\\b(https?://|www[.]|ftp://)[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
        Pattern patty = Pattern.compile(feggy);
        Matcher matty = patty.matcher((CharSequence) value);

        while (matty.find()) {
            String urlStrings = matty.group();

            if (urlStrings.startsWith("(") && urlStrings.endsWith(")")) {
                urlStrings = urlStrings.substring(1, urlStrings.length() - 1);
            }

            urls.add(urlStrings);
        }

        return urls;
    }

    public void saveVideo(final String muse) {


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {


                    Document docs = Jsoup.connect(muse)
                            .userAgent("Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30")
                            .get();

                    Elements elem = docs.select("script");

                    for (Element elements : elem) {

                        String dope = elements.data();
                        if (dope.contains("videoData")) {

                            String initial = dope.substring(dope.lastIndexOf("urls"));
                            String finals = initial.substring(initial.indexOf("[") + 1);
                            finals = finals.substring(0, finals.indexOf("]"));
                            finals = finals.substring(1, finals.length() - 1);

                            playURL = finals;
                        }

                    }


                    downloader(playURL);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    public void downloader(String downloadURL) {

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "" + getString(R.string.dl_started), Toast.LENGTH_LONG).show();
            }
        });

        String desc = getString(R.string.downloading);
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String file = "tiktok_" + "_" + timeStamp;
        String ext = "mp4";
        String name = file + "." + ext;

        Uri Download_Uri = Uri.parse(withoutWatermark(downloadURL));
        DownloadManager dm = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setAllowedOverRoaming(true);
        request.setTitle(getString(R.string.app_name));
        request.setVisibleInDownloadsUi(true);
        request.setDescription(desc);
        request.setVisibleInDownloadsUi(true);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        request.setDestinationInExternalFilesDir(TikActivity.this,"/All tiktok saver", name);
        request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS,Utils.tiktokDirPath+name);
        dm.enqueue(request);
    }


    private void openTok() {
        try {
            Intent i = this.getPackageManager().getLaunchIntentForPackage("com.zhiliaoapp.musically");
            this.startActivity(i);
        } catch (Exception var4) {
            Toast.makeText(getApplicationContext(), R.string.tiktok_not_found, Toast.LENGTH_SHORT).show();
            this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + "com.zhiliaoapp.musically")));
        }

    }


}
