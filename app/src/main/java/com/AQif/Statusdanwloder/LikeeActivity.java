package com.AQif.Statusdanwloder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.AQif.Statusdanwloder.utils.AdUtils;
import com.AQif.Statusdanwloder.utils.LayManager;
import com.AQif.Statusdanwloder.utils.Utils;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LikeeActivity extends Activity {

    ImageView likeeBtn, backBtn;
    EditText linkEdt;
    TextView downloadBtn;
    Pattern pattern = Pattern.compile("window\\.data \\s*=\\s*(\\{.+?\\});");
    private String VideoUrl;
    ProgressDialog progressDialog;
    ImageView help1, help2, help3, help4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likee);

        ((TextView) findViewById(R.id.txt)).setTypeface(LayManager.getTypeface(LikeeActivity.this));
        ((TextView) findViewById(R.id.textView1)).setTypeface(LayManager.getTypeface(LikeeActivity.this));
        ((TextView) findViewById(R.id.textView2)).setTypeface(LayManager.getSubTypeface(LikeeActivity.this));
        ((TextView) findViewById(R.id.textView3)).setTypeface(LayManager.getTypeface(LikeeActivity.this));
        ((TextView) findViewById(R.id.textView4)).setTypeface(LayManager.getSubTypeface(LikeeActivity.this));
        ((TextView) findViewById(R.id.textView5)).setTypeface(LayManager.getTypeface(LikeeActivity.this));
        ((TextView) findViewById(R.id.textView6)).setTypeface(LayManager.getSubTypeface(LikeeActivity.this));
        ((TextView) findViewById(R.id.textView7)).setTypeface(LayManager.getTypeface(LikeeActivity.this));
        ((TextView) findViewById(R.id.textView8)).setTypeface(LayManager.getSubTypeface(LikeeActivity.this));

        help1 = findViewById(R.id.help1);
        help2 = findViewById(R.id.help2);
        help3 = findViewById(R.id.help3);
        help4 = findViewById(R.id.help4);

        Glide.with(LikeeActivity.this)
                .load(R.drawable.lik_help1)
                .into(help1);

        Glide.with(LikeeActivity.this)
                .load(R.drawable.lik_help2)
                .into(help2);

        Glide.with(LikeeActivity.this)
                .load(R.drawable.lik_help3)
                .into(help3);

        Glide.with(LikeeActivity.this)
                .load(R.drawable.intro04)
                .into(help4);

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> onBackPressed());

        likeeBtn = findViewById(R.id.likeeBtn);
        likeeBtn.setOnClickListener(view -> launchLikee());

        linkEdt = findViewById(R.id.linkEdt);
        downloadBtn = findViewById(R.id.downloadBtn);
        downloadBtn.setTypeface(LayManager.getTypeface(LikeeActivity.this));
        downloadBtn.setOnClickListener(view -> {
            final String url = linkEdt.getText().toString();

            if (url.equals("")) {
                Toast.makeText(LikeeActivity.this, "Please paste url and download!!!!", Toast.LENGTH_SHORT).show();
            } else if (!Patterns.WEB_URL.matcher(url).matches()) {
                Toast.makeText(LikeeActivity.this, "Invalid URL!", Toast.LENGTH_SHORT).show();
            } else {
                GetLikeeData();
                linkEdt.getText().clear();
            }
        });

        progressDialog = new ProgressDialog(LikeeActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("Downloading....");
        progressDialog.setCancelable(false);

        LinearLayout adContainer = findViewById(R.id.banner_container);
        LinearLayout adaptiveAdContainer = findViewById(R.id.banner_adaptive_container);
        if (!AdUtils.isloadFbAd) {
            //admob
            AdUtils.initAd(LikeeActivity.this);
            AdUtils.loadBannerAd(LikeeActivity.this, adContainer);
            AdUtils.adptiveBannerAd(LikeeActivity.this, adaptiveAdContainer);
        } else {
            //Fb banner Ads
            AdUtils.fbBannerAd(LikeeActivity.this, adContainer);
            AdUtils.fbAdaptiveBannerAd(LikeeActivity.this, adaptiveAdContainer);
        }
    }

    private void GetLikeeData() {
        try {
            String url = linkEdt.getText().toString();
            if (url.contains("likee")) {
                Utils.showProgressDialog(LikeeActivity.this);
                new callGetLikeeData().execute(url);
            } else {
                Toast.makeText(LikeeActivity.this, "Invalid URL!", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class callGetLikeeData extends AsyncTask<String, Void, Document> {
        Document likeeDoc;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Document doInBackground(String... urls) {
            try {
                likeeDoc = Jsoup.connect(urls[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return likeeDoc;
        }
        @Override
        protected void onPostExecute(Document result) {
            Utils.hideProgressDialog();
            try {
                String JSONData="";
                Matcher matcher = pattern.matcher(result.toString());
                while (matcher.find()) {
                    JSONData =  matcher.group().replaceFirst("window.data = ","").replace(";","");
                }
                JSONObject jsonObject = new JSONObject(JSONData);
                VideoUrl = jsonObject.getString("video_url").replace("_4","");
                if (!VideoUrl.equals("")) {
                    try {
                        progressDialog.show();
                        new DownloadFileFromURL().execute(VideoUrl);
                        VideoUrl = "";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                if (!Utils.downloadLikeeDir.isDirectory()){
                    Utils.downloadLikeeDir.mkdirs();
                }
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                int lenghtOfFile = conection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                OutputStream output = new FileOutputStream(
                        Utils.downloadLikeeDir+"/"+getFilenameFromURL(VideoUrl));
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress(""+(int)((total*100)/lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }
        @Override
        protected void onProgressUpdate(String... progress) {
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }
        @Override
        protected void onPostExecute(String file_url) {
            progressDialog.dismiss();
            progressDialog.setProgress(0);

            Toast.makeText(LikeeActivity.this, "Download Successfully!", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progressDialog.setProgress(0);
        }
    }

    public String getFilenameFromURL(String url) {
        try {
            return new File(new URL(url).getPath()).getName()+"";
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".mp4";
        }
    }

    public void launchLikee() {
        String likeeApp = "video.like";
        try {
            Intent intent = getPackageManager().getLaunchIntentForPackage(likeeApp);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.likee_not_found, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+likeeApp)));
        }
    }
}
