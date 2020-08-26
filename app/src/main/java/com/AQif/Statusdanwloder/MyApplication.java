package com.AQif.Statusdanwloder;

import android.app.Application;

import com.facebook.ads.AudienceNetworkAds;
import com.onesignal.OneSignal;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        // Initialize the Audience Network SDK
        AudienceNetworkAds.initialize(this);
    }

}
