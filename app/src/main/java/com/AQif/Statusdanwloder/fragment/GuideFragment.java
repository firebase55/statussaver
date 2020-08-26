package com.AQif.Statusdanwloder.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.AQif.Statusdanwloder.R;
import com.AQif.Statusdanwloder.utils.AdUtils;
import com.AQif.Statusdanwloder.utils.LayManager;


public class GuideFragment extends Fragment {
    ImageView help1, help2, help3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View localView = inflater.inflate(R.layout.frag_wapp_guide, container, false);

        ((TextView) localView.findViewById(R.id.help)).setTypeface(LayManager.getTypeface(getActivity()));
        ((TextView) localView.findViewById(R.id.textView1)).setTypeface(LayManager.getTypeface(getActivity()));
        ((TextView) localView.findViewById(R.id.textView2)).setTypeface(LayManager.getSubTypeface(getActivity()));
        ((TextView) localView.findViewById(R.id.textView3)).setTypeface(LayManager.getTypeface(getActivity()));
        ((TextView) localView.findViewById(R.id.textView4)).setTypeface(LayManager.getSubTypeface(getActivity()));
        ((TextView) localView.findViewById(R.id.textView5)).setTypeface(LayManager.getTypeface(getActivity()));
        ((TextView) localView.findViewById(R.id.textView6)).setTypeface(LayManager.getSubTypeface(getActivity()));

        help1 = localView.findViewById(R.id.help1);
        help2 = localView.findViewById(R.id.help2);
        help3 = localView.findViewById(R.id.help3);

        Glide.with(getActivity())
                .load(R.drawable.help_1)
                .into(help1);

        Glide.with(getActivity())
                .load(R.drawable.help_2)
                .into(help2);

        Glide.with(getActivity())
                .load(R.drawable.help_3)
                .into(help3);

        LinearLayout adaptiveAdContainer = localView.findViewById(R.id.banner_adaptive_container);
        if (!AdUtils.isloadFbAd) {
            //admob
            AdUtils.initAd(getActivity());
            AdUtils.adptiveBannerAd(getActivity(), adaptiveAdContainer);
        } else {
            //Fb banner Ads
            AdUtils.fbAdaptiveBannerAd(getActivity(), adaptiveAdContainer);
        }
        return localView;
    }
}
