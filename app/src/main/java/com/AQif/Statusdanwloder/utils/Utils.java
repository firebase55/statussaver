package com.AQif.Statusdanwloder.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.AQif.Statusdanwloder.R;

import java.io.File;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.os.Environment.DIRECTORY_DOWNLOADS;


public class Utils {
    private static Context context;
    public static File downloadWhatsAppDir = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Whatsapp");
    public static File downloadWABusiDir = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/WABusiness");

    public static String instaDirPath = "StatusSaver/Instagram/";
    public static File downloadInstaDir = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Instagram");

    public static String twitterDirPath = "StatusSaver/Twitter/";
    public static File downloadTwitterDir = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Twitter");

    public static File downloadLikeeDir = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Likee");

    public static String tiktokDirPath = "StatusSaver/Tiktok/";
    public static File downloadTiktokDir = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Tiktok");

    public static String fbDirPath = "StatusSaver/Facebook/";
    public static File downloadFBDir = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Facebook");


    public Utils(Context mContext) {
        context = mContext;
    }

    private static boolean doesPackageExist(String targetPackage, Context context) {
        try {
            context.getPackageManager().getPackageInfo(targetPackage, PackageManager.GET_META_DATA);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void mPlayer(String filepath, Activity activity) {
        File file = new File(filepath);
        Uri videoURI = FileProvider.getUriForFile(activity.getApplicationContext(), activity.getApplicationContext()
                .getPackageName() + ".provider",file);
        Intent intent;
        if (!Utils.getBack(filepath, "((\\.mp4|\\.webm|\\.ogg|\\.mpK|\\.avi|\\.mkv|\\.flv|\\.mpg|\\.wmv|\\.vob|\\.ogv|\\.mov|\\.qt|\\.rm|\\.rmvb\\.|\\.asf|\\.m4p|\\.m4v|\\.mp2|\\.mpeg|\\.mpe|\\.mpv|\\.m2v|\\.3gp|\\.f4p|\\.f4a|\\.f4b|\\.f4v)$)").isEmpty()) {
            try {
                intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(videoURI, "video/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                if (doesPackageExist("com.google.android.gallery3d", activity)) {
                    intent.setClassName("com.google.android.gallery3d", "com.android.gallery3d.app.MovieActivity");
                } else if (doesPackageExist("com.android.gallery3d", activity)) {
                    intent.setClassName("com.android.gallery3d", "com.android.gallery3d.app.MovieActivity");
                } else if (doesPackageExist("com.cooliris.media", activity)) {
                    intent.setClassName("com.cooliris.media", "com.cooliris.media.MovieView");
                }
                activity.startActivity(intent);
            } catch (Exception e) {
                try {
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(videoURI, "video/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    activity.startActivity(intent);
                } catch (Exception e2) {
                    Toast.makeText(activity, "Sorry, Play video not working properly , try again!", Toast.LENGTH_LONG).show();
                }
            }
        } else if (!Utils.getBack(filepath, "((\\.3ga|\\.aac|\\.aif|\\.aifc|\\.aiff|\\.amr|\\.au|\\.aup|\\.caf|\\.flac|\\.gsm|\\.kar|\\.m4a|\\.m4p|\\.m4r|\\.mid|\\.midi|\\.mmf|\\.mp2|\\.mp3|\\.mpga|\\.ogg|\\.oma|\\.opus|\\.qcp|\\.ra|\\.ram|\\.wav|\\.wma|\\.xspf)$)").isEmpty()) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(videoURI, "audio/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            activity.startActivity(intent);
        } else if (!Utils.getBack(filepath, "((\\.jpg|\\.png|\\.gif|\\.jpeg|\\.bmp)$)").isEmpty()) {
            try {
                intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(videoURI, "image/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                activity.startActivity(intent);
            } catch (Exception e3) {
                Toast.makeText(activity, "Sorry. We can't Display Images. try again", Toast.LENGTH_LONG).show();
            }
        }
    }

    public static String getBack(String paramString1, String paramString2) {
        Matcher localMatcher = Pattern.compile(paramString2).matcher(paramString1);
        if (localMatcher.find()) {
            return localMatcher.group(1);
        }
        return "";
    }

    public static void mShare(String filepath, Activity activity) {
        File fileToShare = new File(filepath);
        if (isImageFile(filepath)) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            share.setType("image/*");
            Uri photoURI = FileProvider.getUriForFile(
                    activity.getApplicationContext(), activity.getApplicationContext()
                            .getPackageName() + ".provider", fileToShare);
            share.putExtra(Intent.EXTRA_STREAM,
                    photoURI);
            activity.startActivity(Intent.createChooser(share, "Share via"));

        } else if (isVideoFile(filepath)) {

            Uri videoURI = FileProvider.getUriForFile(activity.getApplicationContext(), activity.getApplicationContext()
                    .getPackageName() + ".provider",fileToShare);
            Intent videoshare = new Intent(Intent.ACTION_SEND);
            videoshare.setType("*/*");
            videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            videoshare.putExtra(Intent.EXTRA_STREAM, videoURI);

            activity.startActivity(videoshare);
        }

    }

    public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }

    public static boolean isVideoFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("video");
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isNullOrEmpty(String s) {
        return (s == null) || (s.length() == 0) || (s.equalsIgnoreCase("null"))||(s.equalsIgnoreCase("0"));
    }

    static Dialog customDialog;
    public static void showProgressDialog(Activity activity) {
        if (customDialog != null) {
            customDialog.dismiss();
            customDialog = null;
        }
        customDialog = new Dialog(activity);
        LayoutInflater inflater = LayoutInflater.from(activity);
        View mView = inflater.inflate(R.layout.progress_dialog, null);
        customDialog.setCancelable(false);
        customDialog.setContentView(mView);
        if (!customDialog.isShowing() && !activity.isFinishing()) {
            customDialog.show();
        }
    }

    public static void hideProgressDialog() {
        if (customDialog != null && customDialog.isShowing()) {
            customDialog.dismiss();
        }
    }

    public static void startDownload(String downloadPath, String destinationPath, Context context, String FileName) {
        Toast.makeText(context, context.getResources().getString(R.string.dl_started), Toast.LENGTH_SHORT).show();
        Uri uri = Uri.parse(downloadPath);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle(FileName+"");
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS,destinationPath+FileName);
        ((DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request);

    }
}
