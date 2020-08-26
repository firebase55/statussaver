package com.AQif.Statusdanwloder.fragment;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.AQif.Statusdanwloder.R;
import com.AQif.Statusdanwloder.utils.AdUtils;
import com.AQif.Statusdanwloder.utils.LayManager;
import com.AQif.Statusdanwloder.utils.PrefManager;
import com.AQif.Statusdanwloder.utils.Utils;

import java.util.ArrayList;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class UrlDownloadFragment extends Fragment {
    TextView imageButton;
    EditText editText;
    FBHomeFragment homeFragment;
    private PrefManager pref;
    private static final String ARG_POSITION = "position";

    ImageView help1, help2, help3;
    public static UrlDownloadFragment newInstance(int position) {
        UrlDownloadFragment f = new UrlDownloadFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);

        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.urldownload, container, false);
        ((TextView) rootView.findViewById(R.id.txt)).setTypeface(LayManager.getTypeface(getActivity()));
        homeFragment = new FBHomeFragment();
        imageButton = rootView.findViewById(R.id.download);
        editText = (EditText) rootView.findViewById(R.id.editText);
        editText.setTypeface(LayManager.getTypeface(getActivity()));
        pref = new PrefManager(getContext());
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = editText.getText().toString();
                if (url.equals("")) {
                    Toast.makeText(getActivity(), "Please Paste or Enter The Link", Toast.LENGTH_SHORT).show();
                } else if (!url.contains("fbcdn.net")) {
                    Toast.makeText(getActivity(), "Invalid URL!", Toast.LENGTH_SHORT).show();
                } else {
                    downloadVideo(editText.getText().toString());
                }
            }
        });

        help1 = rootView.findViewById(R.id.help1);
        help2 = rootView.findViewById(R.id.help2);
        help3 = rootView.findViewById(R.id.help3);

        Glide.with(getActivity())
                .load(R.drawable.facebook_step1)
                .into(help1);

        Glide.with(getActivity())
                .load(R.drawable.facebook_step2)
                .into(help2);

        Glide.with(getActivity())
                .load(R.drawable.facebook_step3)
                .into(help3);

        LinearLayout adaptiveAdContainer = rootView.findViewById(R.id.banner_adaptive_container);
        if (!AdUtils.isloadFbAd) {
            //admob
            AdUtils.initAd(getActivity());
            AdUtils.adptiveBannerAd(getActivity(), adaptiveAdContainer);
        } else {
            //Fb banner Ads
            AdUtils.fbAdaptiveBannerAd(getActivity(), adaptiveAdContainer);
        }
        return rootView;
    }

    private void downloadVideo(String pathvideo) {
        Log.e("pre path", pathvideo);
        if (pathvideo.contains("story")) {
            homeFragment.getUrlfromUrlDownload(pathvideo);
        } else {
            editText.setText("");
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(pathvideo));
            request.allowScanningByMediaScanner();
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            int Number = pref.getFileName();
            request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS, Utils.fbDirPath+"Video-"+Number+".mp4");
            DownloadManager dm = (DownloadManager) getActivity().getSystemService(getActivity().DOWNLOAD_SERVICE);
            ArrayList<String> urldownloadFragmentList = (new FBHomeFragment()).getList();
            if (urldownloadFragmentList.contains(pathvideo)) {
                Toast.makeText(getActivity().getApplicationContext(), "The Video is Already Downloading", Toast.LENGTH_LONG).show();
            } else {
                urldownloadFragmentList.add(pathvideo);
                dm.enqueue(request);
                Toast.makeText(getActivity().getApplicationContext(), "Downloading Video-" + Number + ".mp4", Toast.LENGTH_LONG).show();
                Number++;
                pref.setFileName(Number);
            }
        }
    }

}