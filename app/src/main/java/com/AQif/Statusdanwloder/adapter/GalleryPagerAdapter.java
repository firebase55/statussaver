package com.AQif.Statusdanwloder.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.AQif.Statusdanwloder.fragment.TweatFrag;
import com.AQif.Statusdanwloder.fragment.LikeeFrag;
import com.AQif.Statusdanwloder.fragment.WABusFrag;
import com.AQif.Statusdanwloder.fragment.WAppFrag;
import com.AQif.Statusdanwloder.fragment.FBFrag;
import com.AQif.Statusdanwloder.fragment.InsFrag;
import com.AQif.Statusdanwloder.fragment.TikFrag;

public class GalleryPagerAdapter extends FragmentPagerAdapter {


    public GalleryPagerAdapter(FragmentManager fm) {
        super(fm);

    }


    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return new WAppFrag();
        }else if (position == 1) {
            return new WABusFrag();
        } else if (position == 2){
            return new InsFrag();
        }  else if (position == 3){
            return new TikFrag();
        }else if (position == 4){
            return new LikeeFrag();
        }else if (position == 5){
            return new FBFrag();
        }else {
            return new TweatFrag();
        }
    }

    @Override
    public int getCount() {
        return 7;
    }


}
