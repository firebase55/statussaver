package com.AQif.Statusdanwloder;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.AQif.Statusdanwloder.utils.SharePrefs;

public class InstaLoginActivity extends AppCompatActivity {

    InstaLoginActivity activity;
    SwipeRefreshLayout swipeRefreshLayout;
    WebView webView;
    private String cookies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insta_login);
        webView = findViewById(R.id.webView);
        activity = this;

        loadPage();
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> loadPage());
    }

    public void loadPage() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.clearCache(true);
        webView.setWebViewClient(new MyBrowser());
        CookieSyncManager.createInstance(activity);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        webView.loadUrl("https://www.instagram.com/accounts/login/");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    swipeRefreshLayout.setRefreshing(true);
                }
            }
        });
    }

    public String getCookie(String siteName, String CookieName) {
        String CookieValue = null;

        CookieManager cookieManager = CookieManager.getInstance();
        String cookiesStr = cookieManager.getCookie(siteName);
        if (cookiesStr != null && !cookiesStr.isEmpty()) {
            String[] temp = cookiesStr.split(";");
            for (String ar1 : temp) {
                if (ar1.contains(CookieName)) {
                    String[] temp1 = ar1.split("=");
                    CookieValue = temp1[1];
                    break;
                }
            }
        }
        return CookieValue;
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onLoadResource(WebView webView, String str) {
            super.onLoadResource(webView, str);
        }

        @Override
        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);

            cookies = CookieManager.getInstance().getCookie(str);

            try {
                String sessionId = getCookie(str, "sessionid");
                String csrfToken = getCookie(str, "csrftoken");
                String userid = getCookie(str, "ds_user_id");
                if (sessionId != null && csrfToken != null && userid != null) {
                    SharePrefs.getInstance(activity).putString(SharePrefs.COOKIES, cookies);
                    SharePrefs.getInstance(activity).putString(SharePrefs.CSRF, csrfToken);
                    SharePrefs.getInstance(activity).putString(SharePrefs.SESSIONID, sessionId);
                    SharePrefs.getInstance(activity).putString(SharePrefs.USERID, userid);
                    SharePrefs.getInstance(activity).putBoolean(SharePrefs.ISINSTALOGIN, true);

                    webView.destroy();
                    Intent intent= new Intent();
                    intent.putExtra("result","result");
                    setResult(RESULT_OK,intent);
                    finish();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onReceivedError(WebView webView, int i, String str, String str2) {
            super.onReceivedError(webView, i, str, str2);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
            return super.shouldInterceptRequest(webView, webResourceRequest);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
            return super.shouldOverrideUrlLoading(webView, webResourceRequest);
        }
    }
}
