package com.AQif.Statusdanwloder.utils;

import android.app.Activity;
import android.content.Intent;


public class Helpers {


    public static void mShareText(String text, Activity activity) {
        Intent myapp = new Intent(Intent.ACTION_SEND);
        myapp.setType("text/plain");
        myapp.putExtra(Intent.EXTRA_TEXT, text);
        activity.startActivity(myapp);
    }


}
