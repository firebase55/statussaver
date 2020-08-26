package com.AQif.Statusdanwloder.fragment;

import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.AQif.Statusdanwloder.R;
import com.AQif.Statusdanwloder.adapter.WABusStatusAdapter;
import com.AQif.Statusdanwloder.model.DataModel;
import com.AQif.Statusdanwloder.utils.LayManager;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;


public class WAStatusFragment extends Fragment {

    File file;
    String folderName = "";
    ArrayList<DataModel> statusImageList = new ArrayList<>();
    ArrayList<DataModel> statusVideoList = new ArrayList<>();
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView mRecyclerView;
    LinearLayout isEmptyList;
    WABusStatusAdapter mAdapter;
    TextView txt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_wapp_status, container, false);
        folderName = "WhatsApp Business/Media/.Statuses";
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view_0);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(this.mLayoutManager);
        isEmptyList = view.findViewById(R.id.isEmptyList);

        txt = view.findViewById(R.id.txt);
        txt.setTypeface(LayManager.getSubTypeface(getActivity()));
        return view;
    }

    public void loadMedia() {

        file = new File(Environment.getExternalStorageDirectory() + File.separator + folderName + File.separator);
        statusImageList.clear();
        statusVideoList.clear();
        if (!file.isDirectory()) {
            return;
        }

        displayfiles(file, mRecyclerView);

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

    @Override
    public void onResume() {
        super.onResume();
        loadMedia();
    }

    void displayfiles(File file, final RecyclerView mRecyclerView) {
        File[] listfilemedia = dirListByAscendingDate(file);
        if (listfilemedia != null) {
            int i = 0;
            while (i < listfilemedia.length) {
                if (!listfilemedia[i].getAbsolutePath().contains(".nomedia"))
                    statusImageList.add(new DataModel(listfilemedia[i].getAbsolutePath(), listfilemedia[i].getName()));
                i++;
            }
        }
        if (statusImageList != null) {
            if (statusImageList.size() > 0) {
                isEmptyList.setVisibility(View.GONE);
            } else {
                isEmptyList.setVisibility(View.VISIBLE);
            }
        }
        mAdapter = new WABusStatusAdapter(getActivity(), statusImageList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }
}
