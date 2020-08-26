package com.AQif.Statusdanwloder.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.LinearLayout;

import com.AQif.Statusdanwloder.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;


public class AdUtils {
    public static int adCounter = 1;

    public static boolean isloadFbAd = false;


    public static void initAd(Context context) {
        MobileAds.initialize(context, context.getString(R.string.admob_app_id));
    }

    public static void loadBannerAd(Context context, LinearLayout adContainer) {
        AdView adView = new AdView(context);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(context.getString(R.string.admob_banner_id));
        adView.loadAd(adRequest);
        adContainer.addView(adView);
    }

    public static void adptiveBannerAd(Context context, LinearLayout adContainer) {
        AdView adView = new AdView(context);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        adView.setAdUnitId(context.getString(R.string.admob_banner_id));
        adView.loadAd(adRequest);
        adContainer.addView(adView);
    }

    static InterstitialAd mInterstitialAd;

    public static void loadInterAd(Context context) {
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(context.getString(R.string.admob_interstitial));
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());
    }

    public static void showInterAd(final Context context, final Intent intent) {
        if (adCounter == 6) {
            adCounter = 1;
            if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());
                        startActivity(context, intent);
                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                    }

                    @Override
                    public void onAdLeftApplication() {
                        super.onAdLeftApplication();
                    }

                    @Override
                    public void onAdOpened() {
                        super.onAdOpened();
                        mInterstitialAd.show();
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                    }

                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                    }
                });
            } else {
                startActivity(context, intent);
            }
        } else {
            startActivity(context, intent);
        }
    }

    static void startActivity(Context context, Intent intent) {
        if (intent != null) {
            context.startActivity(intent);
        }
    }

    static com.facebook.ads.AdView adView;

    public static void fbBannerAd(Context context, LinearLayout adContainer) {
        adView = new com.facebook.ads.AdView(context, context.getString(R.string.fbad_banner_id), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        adContainer.addView(adView);
        adView.loadAd();
    }

    static com.facebook.ads.AdView adaptiveView;
    public static void fbAdaptiveBannerAd(Context context, LinearLayout adContainer) {
        adaptiveView = new com.facebook.ads.AdView(context, context.getString(R.string.fbad_banner_id), com.facebook.ads.AdSize.RECTANGLE_HEIGHT_250);
        adContainer.addView(adaptiveView);
        adaptiveView.loadAd();
    }

    static com.facebook.ads.InterstitialAd fbInterstitialAd;

    public static void loadFbInterAd(Context context) {
        fbInterstitialAd = new com.facebook.ads.InterstitialAd(context, context.getString(R.string.fbad_interstitial_id));
        fbInterstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });

        fbInterstitialAd.loadAd();
    }

    public static void showFbInterAd(final Context context, final Intent intent) {
        if (adCounter == 6) {
            adCounter = 1;
            if (fbInterstitialAd != null && fbInterstitialAd.isAdLoaded()) {
                fbInterstitialAd.show();
            } else {
                startActivity(context, intent);
            }


        } else {
            startActivity(context, intent);
        }
    }

    public static void destroyFbAd() {
        if (adView != null) {
            adView.destroy();
        }
        if (adaptiveView != null) {
            adaptiveView.destroy();
        }

        if (fbInterstitialAd != null) {
            fbInterstitialAd.destroy();
        }
    }
}
