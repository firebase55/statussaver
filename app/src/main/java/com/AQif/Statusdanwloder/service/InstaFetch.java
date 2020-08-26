package com.AQif.Statusdanwloder.service;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.AQif.Statusdanwloder.callapi.CommonClassForAPI;
import com.AQif.Statusdanwloder.model.Edge;
import com.AQif.Statusdanwloder.model.EdgeSidecarToChildren;
import com.AQif.Statusdanwloder.model.ResponseModel;
import com.AQif.Statusdanwloder.utils.SharePrefs;
import com.AQif.Statusdanwloder.utils.Utils;

import java.io.File;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

public class InstaFetch {
    private Activity activity;

    public void GetInstagramData(Activity activity, String urls) {
        this.activity = activity;
        try {
            URL url = new URL(urls);
            String host = url.getHost();
            if (host.equals("www.instagram.com")) {
                callDownload(urls);
            } else {
                Toast.makeText(activity, "Invalid URL!", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callDownload(String Url) {
        CommonClassForAPI commonClassForAPI = CommonClassForAPI.getInstance(activity);
        String UrlWithoutQP = getUrlWithoutParameters(activity, Url);
        UrlWithoutQP = UrlWithoutQP + "?__a=1";
        try {
            Utils utils = new Utils(activity);
            if (utils.isNetworkAvailable()) {
                if (commonClassForAPI != null) {
                    Utils.showProgressDialog(activity);
                    commonClassForAPI.callResult(instaObserver, UrlWithoutQP,
                            "ds_user_id=" + SharePrefs.getInstance(activity).getString(SharePrefs.USERID)
                                    + "; sessionid=" + SharePrefs.getInstance(activity).getString(SharePrefs.SESSIONID));
                }
            } else {
                Toast.makeText(activity, "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private String getUrlWithoutParameters(Context context, String url) {
        try {
            URI uri = new URI(url);
            return new URI(uri.getScheme(),
                    uri.getAuthority(),
                    uri.getPath(),
                    null,
                    uri.getFragment()).toString();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Invalid URL!", Toast.LENGTH_SHORT).show();
            return "";
        }
    }

    private DisposableObserver<JsonObject> instaObserver = new DisposableObserver<JsonObject>() {
        String PhotoUrl;
        String VideoUrl;
        @Override
        public void onNext(JsonObject versionList) {
            Utils.hideProgressDialog();
            try {
                Type listType = new TypeToken<ResponseModel>() {
                }.getType();
                ResponseModel responseModel = new Gson().fromJson(versionList.toString(), listType);
                EdgeSidecarToChildren edgeSidecarToChildren = responseModel.getGraphql().getShortcode_media().getEdge_sidecar_to_children();
                if (edgeSidecarToChildren != null) {
                    List<Edge> edgeArrayList = edgeSidecarToChildren.getEdges();
                    for (int i = 0; i < edgeArrayList.size(); i++) {
                        if (edgeArrayList.get(i).getNode().isIs_video()) {
                            VideoUrl = edgeArrayList.get(i).getNode().getVideo_url();
                            Utils.startDownload(VideoUrl, Utils.instaDirPath, activity, getVideoFilenameFromURL(VideoUrl));
                            VideoUrl = "";

                        } else {
                            PhotoUrl = edgeArrayList.get(i).getNode().getDisplay_resources().get(edgeArrayList.get(i).getNode().getDisplay_resources().size() - 1).getSrc();
                            Utils.startDownload(PhotoUrl, Utils.instaDirPath, activity, getImageFilenameFromURL(PhotoUrl));
                            PhotoUrl = "";
                        }
                    }
                } else {
                    boolean isVideo = responseModel.getGraphql().getShortcode_media().isIs_video();
                    if (isVideo) {
                        VideoUrl = responseModel.getGraphql().getShortcode_media().getVideo_url();
                        Utils.startDownload(VideoUrl, Utils.instaDirPath, activity, getVideoFilenameFromURL(VideoUrl));
                        VideoUrl = "";
                    } else {
                        PhotoUrl = responseModel.getGraphql().getShortcode_media().getDisplay_resources()
                                .get(responseModel.getGraphql().getShortcode_media().getDisplay_resources().size() - 1).getSrc();

                        Utils.startDownload(PhotoUrl, Utils.instaDirPath, activity, getImageFilenameFromURL(PhotoUrl));
                        PhotoUrl = "";
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
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



    private String getImageFilenameFromURL(String url) {
        try {
            return new File(new URL(url).getPath().toString()).getName();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".png";
        }
    }

    private String getVideoFilenameFromURL(String url) {
        try {
            return new File(new URL(url).getPath().toString()).getName();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".mp4";
        }
    }

    public void destroy(){
        instaObserver.dispose();
    }
}
