package com.AQif.Statusdanwloder.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.AQif.Statusdanwloder.R;
import com.AQif.Statusdanwloder.adapter.DownloadAdapter;
import com.AQif.Statusdanwloder.model.DataModel;
import com.AQif.Statusdanwloder.utils.LayManager;
import com.AQif.Statusdanwloder.utils.Utils;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class WABusFrag extends Fragment {

    File file;
    ArrayList<DataModel> downloadImageList = new ArrayList<>();
    ArrayList<DataModel> downloadVideoList = new ArrayList<>();
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView mRecyclerView;
    LinearLayout isEmptyList;
    DownloadAdapter mAdapter;
    TextView txt;

    @Override
    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        View view = paramLayoutInflater.inflate(R.layout.fragment_download, paramViewGroup, false);
        mRecyclerView = view.findViewById(R.id.my_recycler_view_1);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getActivity(), 3);
        mRecyclerView.setLayoutManager(this.mLayoutManager);
        isEmptyList = view.findViewById(R.id.isEmptyList);
        txt = view.findViewById(R.id.txt);
        txt.setTypeface(LayManager.getSubTypeface(getActivity()));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMedia();
    }

    public void loadMedia() {
        file = Utils.downloadWABusiDir;

        downloadImageList.clear();
        downloadVideoList.clear();
        if (!file.isDirectory()) {
            return;
        }

        displayfiles(file, mRecyclerView);

    }

    void displayfiles(File file, final RecyclerView mRecyclerView) {
        File[] listfilemedia = dirListByAscendingDate(file);
        if (listfilemedia != null && listfilemedia.length != 0) {
            isEmptyList.setVisibility(View.GONE);
        } else {
            isEmptyList.setVisibility(View.VISIBLE);
        }

        if (listfilemedia != null) {
            int i = 0;
            while (i < listfilemedia.length) {
                downloadImageList.add(new DataModel(listfilemedia[i].getAbsolutePath(), listfilemedia[i].getName()));
                i++;
            }

            if (downloadImageList.size() > 0) {
                isEmptyList.setVisibility(View.GONE);
            } else {
                isEmptyList.setVisibility(View.VISIBLE);
            }
            mAdapter = new DownloadAdapter(getActivity(), downloadImageList);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }

    public static File[] dirListByAscendingDate(File folder) {
        if (!folder.isDirectory()) {
            return new File[0];
        }
        File[] sortedByDate = folder.listFiles();
        if (sortedByDate == null || sortedByDate.length <= 1) {
            return sortedByDate;
        }
        Arrays.sort(sortedByDate, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        return sortedByDate;
    }
}
