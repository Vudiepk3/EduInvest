package com.example.eduinvest.fragments;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.eduinvest.R;
import com.example.eduinvest.adapters.NewsAdapter;
import com.example.eduinvest.models.BannerModel;
import com.example.eduinvest.models.NewsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {
    private ValueEventListener eventListener;

    private DatabaseReference databaseReference;
    private List<NewsModel> dataList;
    private NewsAdapter adapter;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        loadImageSlide(view);
        loadUniversityData(view);
        return view;
    }

    // Load images from Firebase for ImageSlider
    public void loadImageSlide(View view) {
        ImageSlider imageSlider2 = view.findViewById(R.id.ImageSlide2);
        ArrayList<SlideModel> slideModels2 = new ArrayList<>();
        List<String> linkWebsites = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Banner");
        Query query = databaseReference.orderByKey().limitToFirst(5);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                slideModels2.clear();
                linkWebsites.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    BannerModel imageModel = itemSnapshot.getValue(BannerModel.class);
                    if (imageModel != null && imageModel.getUrlImage() != null ) {
                        slideModels2.add(new SlideModel(imageModel.getUrlImage(), ScaleTypes.FIT));
                        linkWebsites.add(imageModel.getLinkWeb());
                    }
                }

                imageSlider2.setImageList(slideModels2, ScaleTypes.FIT);
                imageSlider2.setItemClickListener(i -> {
                    if (i >= 0 && i < linkWebsites.size()) {
                        String linkWebsite = linkWebsites.get(i);
                        if (linkWebsite != null && !linkWebsite.isEmpty() && !linkWebsite.equals("No Link Website")) {
                            try {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkWebsite));
                                startActivity(intent);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(getActivity(), "Không thể mở đường dẫn web.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Không có đường dẫn web.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Tải Dữ Liệu Không Thành Công.", Toast.LENGTH_SHORT).show();
            }
        });
    }



    // Load university data from Firebase
    public void loadUniversityData(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        dataList = new ArrayList<>();
        adapter = new NewsAdapter(getActivity(), dataList);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("News");
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    NewsModel dataClass = itemSnapshot.getValue(NewsModel.class);
                    if (dataClass != null) {
                        dataClass.setKey(itemSnapshot.getKey());
                        dataList.add(dataClass);
                    }
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Đăng ký lại eventListener khi Activity trở lại hoạt động
        if (databaseReference != null && eventListener != null) {
            databaseReference.addValueEventListener(eventListener);
            // Hoặc addListenerForSingleValueEvent() nếu chỉ cần tải dữ liệu một lần
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Hủy đăng ký eventListener khi Activity bị hủy để tránh rò rỉ bộ nhớ
        if (databaseReference != null && eventListener != null) {
            databaseReference.removeEventListener(eventListener);
        }
    }
}