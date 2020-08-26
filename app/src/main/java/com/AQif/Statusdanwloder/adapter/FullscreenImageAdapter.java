package com.AQif.Statusdanwloder.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.AQif.Statusdanwloder.R;
import com.AQif.Statusdanwloder.model.DataModel;
import com.AQif.Statusdanwloder.utils.Utils;

import java.io.File;
import java.util.ArrayList;

public class FullscreenImageAdapter extends PagerAdapter {
    Activity activity;
    ArrayList<DataModel> imageList;
    private File file;

    public FullscreenImageAdapter(Activity activity, ArrayList<DataModel> imageList) {
        this.activity = activity;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = LayoutInflater.from(activity).inflate(R.layout.preview_list_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        ImageView iconplayer = (ImageView) itemView.findViewById(R.id.iconplayer);

        final DataModel jpast = (DataModel) imageList.get(position);
        this.file = new File(jpast.getFilePath());
        if (!this.file.isDirectory()) {
            if (!Utils.getBack(jpast.getFilePath(), "((\\.mp4|\\.webm|\\.ogg|\\.mpK|\\.avi|\\.mkv|\\.flv|\\.mpg|\\.wmv|\\.vob|\\.ogv|\\.mov|\\.qt|\\.rm|\\.rmvb\\.|\\.asf|\\.m4p|\\.m4v|\\.mp2|\\.mpeg|\\.mpe|\\.mpv|\\.m2v|\\.3gp|\\.f4p|\\.f4a|\\.f4b|\\.f4v)$)").isEmpty()) {
                try {
                    iconplayer.setVisibility(View.VISIBLE);
                    Glide.with(this.activity).load(this.file).apply(new RequestOptions().placeholder(android.R.color.black).error(android.R.color.black).optionalTransform(new RoundedCorners(5))).into(imageView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (!Utils.getBack(jpast.getFilePath(), "((\\.jpg|\\.png|\\.gif|\\.jpeg|\\.bmp)$)").isEmpty()) {
                iconplayer.setVisibility(View.GONE);
                Glide.with(this.activity).load(this.file).apply(new RequestOptions().placeholder(android.R.color.black).error(android.R.color.black).optionalTransform(new RoundedCorners(5))).into(imageView);
            }
        }
        imageView.setOnClickListener(view -> {
            if (!Utils.getBack(jpast.getFilePath(), "((\\.mp4|\\.webm|\\.ogg|\\.mpK|\\.avi|\\.mkv|\\.flv|\\.mpg|\\.wmv|\\.vob|\\.ogv|\\.mov|\\.qt|\\.rm|\\.rmvb\\.|\\.asf|\\.m4p|\\.m4v|\\.mp2|\\.mpeg|\\.mpe|\\.mpv|\\.m2v|\\.3gp|\\.f4p|\\.f4a|\\.f4b|\\.f4v)$)").isEmpty()) {
                Utils.mPlayer(jpast.getFilePath(), activity);
            }
        });
        container.addView(itemView);

        return itemView;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((RelativeLayout) object);
    }
}
