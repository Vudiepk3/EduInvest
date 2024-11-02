package com.example.eduinvest.fragments;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import java.util.Collections;
import java.util.List;

public class NewsFragment extends Fragment {
    private DatabaseReference databaseReference;
    private ValueEventListener eventListener;
    private List<NewsModel> dataList;
    private NewsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        loadImageSlide(view);
        loadUniversityData(view);
        return view;
    }

    // Load images from Firebase for ImageSlider
    public void loadImageSlide(View view) {
        ImageSlider imageSlider = view.findViewById(R.id.ImageSlide2);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        List<String> linkWebsites = new ArrayList<>();

        DatabaseReference bannerRef = FirebaseDatabase.getInstance().getReference("Banner");
        Query query = bannerRef.orderByKey().limitToFirst(5);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                slideModels.clear();
                linkWebsites.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    BannerModel banner = itemSnapshot.getValue(BannerModel.class);
                    if (banner != null && "BANNER2".equals(banner.getNoteImage()) || "All".equals(banner.getNoteImage())) {
                        slideModels.add(new SlideModel(banner.getUrlImage(), ScaleTypes.FIT));
                        linkWebsites.add(banner.getLinkWeb());
                    }
                }
                imageSlider.setImageList(slideModels, ScaleTypes.FIT);
                imageSlider.setItemClickListener(i -> {
                    if (i >= 0 && i < linkWebsites.size()) {
                        String link = linkWebsites.get(i);
                        if (link != null && !link.isEmpty() && !"No Link Website".equals(link)) {
                            try {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                                startActivity(intent);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(requireContext(), "Không thể mở đường dẫn web.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(requireContext(), "Không có đường dẫn web.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Tải Dữ Liệu Không Thành Công.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Load university data from Firebase
    public void loadUniversityData(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 1));

        dataList = new ArrayList<>();
        adapter = new NewsAdapter(requireContext(), dataList);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("News");
        Query query = databaseReference.orderByChild("timestamp");

        eventListener = query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    NewsModel news = itemSnapshot.getValue(NewsModel.class);
                    if (news != null) {
                        news.setKey(itemSnapshot.getKey());
                        dataList.add(news);
                    }
                }
                Collections.reverse(dataList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Tải Dữ Liệu Không Thành Công.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (databaseReference != null && eventListener != null) {
            databaseReference.addValueEventListener(eventListener);
        }
    }

}
