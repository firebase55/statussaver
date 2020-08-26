package com.AQif.Statusdanwloder;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.AQif.Statusdanwloder.adapter.FullscreenImageAdapter;
import com.AQif.Statusdanwloder.model.DataModel;
import com.AQif.Statusdanwloder.utils.AdUtils;
import com.AQif.Statusdanwloder.utils.LayManager;
import com.AQif.Statusdanwloder.utils.Utils;
import com.snatik.storage.Storage;
import java.io.File;
import java.util.ArrayList;

public class PreviewActivity extends AppCompatActivity {

    ViewPager viewPager;
    ArrayList<DataModel> imageList;
    int position;


    LinearLayout menuSave;
    LinearLayout menuShare;
    LinearLayout menuDelete;
    FullscreenImageAdapter fullscreenImageAdapter;
    String statusDownload;


    ImageView backIV;
    LinearLayout bottomLay;
    TextView headerTxt;
    String folderPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        ((TextView) findViewById(R.id.txt1)).setTypeface(LayManager.getTypeface(PreviewActivity.this));
        ((TextView) findViewById(R.id.txt2)).setTypeface(LayManager.getTypeface(PreviewActivity.this));
        ((TextView) findViewById(R.id.txt3)).setTypeface(LayManager.getTypeface(PreviewActivity.this));

        headerTxt = findViewById(R.id.headerTxt);
        headerTxt.setTypeface(LayManager.getTypeface(PreviewActivity.this));

        backIV = findViewById(R.id.backIV);

        bottomLay = findViewById(R.id.bottomLay);
        LinearLayout.LayoutParams botmparam = LayManager.setLinParams(PreviewActivity.this, 1080, 307);
        bottomLay.setLayoutParams(botmparam);

        viewPager = findViewById(R.id.viewPager);

        menuSave = findViewById(R.id.menu_save);

        menuShare = findViewById(R.id.menu_share);

        menuDelete = findViewById(R.id.menu_delete);

        LinearLayout.LayoutParams btnParam = LayManager.setLinParams(PreviewActivity.this, 300, 100);
        menuSave.setLayoutParams(btnParam);
        menuShare.setLayoutParams(btnParam);
        menuDelete.setLayoutParams(btnParam);

        imageList = getIntent().getParcelableArrayListExtra("images");
        position = getIntent().getIntExtra("position", 0);
        statusDownload = getIntent().getStringExtra("statusdownload");
        folderPath = getIntent().getStringExtra("folderpath");

        if (statusDownload.equals("download")) {

            bottomLay.setWeightSum(2.0f);
            menuSave.setVisibility(View.GONE);
        } else {

            bottomLay.setWeightSum(3.0f);
            menuSave.setVisibility(View.VISIBLE);
        }

        fullscreenImageAdapter = new FullscreenImageAdapter(PreviewActivity.this, imageList);
        viewPager.setAdapter(fullscreenImageAdapter);
        viewPager.setCurrentItem(position);

        menuSave.setOnClickListener(clickListener);
        menuShare.setOnClickListener(clickListener);
        menuDelete.setOnClickListener(clickListener);
        backIV.setOnClickListener(clickListener);

        LinearLayout adContainer = findViewById(R.id.banner_container);
        if (!AdUtils.isloadFbAd) {
            //admob
            AdUtils.initAd(PreviewActivity.this);
            AdUtils.loadBannerAd(PreviewActivity.this, adContainer);
        } else {
            //Fb banner Ads
            AdUtils.fbBannerAd(PreviewActivity.this, adContainer);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        AdUtils.destroyFbAd();
        super.onDestroy();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backIV:
                    onBackPressed();
                    break;
                case R.id.menu_save:
                    if (imageList.size() > 0) {
                        final Storage storage = new Storage(v.getContext());
                        String path;
                        try {

                            path = folderPath;

                            if (!new File(path).exists()) {
                                new File(path).mkdirs();
                            }
                            storage.copy(imageList.get(viewPager.getCurrentItem()).getFilePath(), path + File.separator + new File(imageList.get(viewPager.getCurrentItem()).getFilePath()).getName());
                            Toast.makeText(PreviewActivity.this, "Saved successfully!", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(PreviewActivity.this, "Sorry we can't move file.try with other file.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        finish();
                    }
                    break;

                case R.id.menu_share:
                    if (imageList.size() > 0) {
                        Utils.mShare(imageList.get(viewPager.getCurrentItem()).getFilePath(), PreviewActivity.this);
                    } else {
                        finish();
                    }
                    break;

                case R.id.menu_delete:
                    if (imageList.size() > 0) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PreviewActivity.this);
                        alertDialog.setTitle("Delete");
                        alertDialog.setMessage("Sure to Delete this Image?");
                        alertDialog.setPositiveButton("Yes", (dialog, which) -> {
                            dialog.dismiss();
                            int currentItem = 0;

                            File file = new File(imageList.get(viewPager.getCurrentItem()).getFilePath());
                            if (file.exists()) {
                                file.delete();
                                if (imageList.size() > 0 && viewPager.getCurrentItem() < imageList.size()) {
                                    currentItem = viewPager.getCurrentItem();
                                }
                                imageList.remove(viewPager.getCurrentItem());
                                fullscreenImageAdapter = new FullscreenImageAdapter(PreviewActivity.this, imageList);
                                viewPager.setAdapter(fullscreenImageAdapter);
                                if (imageList.size() > 0) {
                                    viewPager.setCurrentItem(currentItem);
                                } else {
                                    finish();
                                }
                            }
                        });
                        alertDialog.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
                        alertDialog.show();
                    } else {
                        finish();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
