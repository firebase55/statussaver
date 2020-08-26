package com.AQif.Statusdanwloder;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.AQif.Statusdanwloder.utils.LayManager;

public class PrivacyActivity extends Activity {
    ImageView backBtn;
    TextView headerTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        WebView mWebView ;
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("file:///android_asset/privacy_policy.html");

        headerTxt = findViewById(R.id.headerTxt);
        headerTxt.setTypeface(LayManager.getTypeface(PrivacyActivity.this));

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> onBackPressed());
    }
}
