package com.example.eduinvest.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.eduinvest.adapters.ScholarshipAdapter;
import com.example.eduinvest.databinding.FragmentHomeBinding;
import com.example.eduinvest.loanactivities.ManageLoanActivities;
import com.example.eduinvest.models.NewsModel;
import com.example.eduinvest.repository.BannerRepository;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private FirebaseAuth auth;
    private static final String TAG = "HomeFragment";

    private List<NewsModel> dataList;
    private ScholarshipAdapter adapter;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Khởi tạo Firebase
        FirebaseApp.initializeApp(requireContext());
        initializeActivities();

        // Xử lý ảnh icon người dùng
        FirebaseUser currentUser = auth.getCurrentUser();
//        if (currentUser != null) {
//            Uri photoUrl = currentUser.getPhotoUrl();
//            if (photoUrl != null) {
//                Glide.with(this)
//                        .load(photoUrl)
//                        .apply(new RequestOptions()
//                                .placeholder(R.drawable.image_eduinvest)
//                                .error(R.drawable.image_eduinvest)
//                                .centerCrop())
//                        .into(binding.iconImage);
//            } else {
//                binding.iconImage.setImageResource(R.drawable.image_eduinvest);
//            }
//        }
        // Tải ảnh slider
        loadImageSlider();
        loadData();
    }

    private void loadImageSlider() {
        if (!isAdded()) return;

        BannerRepository bannerRepository = BannerRepository.getInstance();

        // Nếu dữ liệu đã tải xong thì hiển thị ngay
        if (bannerRepository.isDataLoaded()) {
            updateSlider();
        } else {
            // Nếu chưa tải xong, chờ dữ liệu xong rồi cập nhật UI
            bannerRepository.setOnDataChangedListener(this::updateSlider);
        }
    }

    private void updateSlider() {
        if (!isAdded()) return;

        BannerRepository bannerRepository = BannerRepository.getInstance();
        List<SlideModel> slideModels = bannerRepository.getSlideModels();
        List<String> linkWebsites = bannerRepository.getLinkWebsites();

        requireActivity().runOnUiThread(() -> {
            binding.ImageSlide.setImageList(slideModels, ScaleTypes.FIT);
            binding.ImageSlide.setItemClickListener(position -> {
                if (position >= 0 && position < linkWebsites.size()) {
                    openWebsite(linkWebsites.get(position));
                }
            });
        });
    }
    private void openWebsite(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
//            showToast("Không thể mở liên kết.");
        }
    }


    private void initializeActivities() {
        auth = FirebaseAuth.getInstance();

        // Các sự kiện click khác
        setupClickListener(binding.iconNotifition, this::showFeatureNotAvailableToast);
        setupClickListener(binding.layoutFunction.loanCard,
                () -> startActivity(new Intent(getActivity(), ManageLoanActivities.class)));
        setupClickListener(binding.layoutFunction.payCard, this::showFeatureNotAvailableToast);
        setupClickListener(binding.layoutFunction.voucherCard, this::showFeatureNotAvailableToast);
        setupClickListener(binding.layoutFunction.personalGrowthCard, this::showFeatureNotAvailableToast);
        setupClickListener(binding.layoutFunction.saveCard, this::showFeatureNotAvailableToast);
        setupClickListener(binding.layoutFunction.mentorCard, this::showFeatureNotAvailableToast);
        setupClickListener(binding.layoutFunction.financialGoalsCard, this::showFeatureNotAvailableToast);

    }

    private void setupClickListener(View view, Runnable onClick) {
        view.setOnClickListener(v -> {
            try {
                onClick.run();
            } catch (Exception e) {
                Log.e("Error", Objects.requireNonNull(e.getMessage()));
            }
        });
    }

    private void showFeatureNotAvailableToast() {
        showToast("Chức năng sẽ được cập nhật sớm nhất đến với bạn");
    }

    private void showToast(String message) {
        if (isAdded()) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }
    private void loadData() {
        binding.recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        dataList = new ArrayList<>();
        adapter = new ScholarshipAdapter(dataList, requireContext());
        binding.recyclerView.setAdapter(adapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("News");
        Query query = databaseReference.orderByChild("timestamp");

        ValueEventListener eventListener = query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    NewsModel news = itemSnapshot.getValue(NewsModel.class);
                    if (news != null && news.getTypeNews().equals("SCHOLARSHIP")) {
                        news.setKey(itemSnapshot.getKey());
                        dataList.add(news);
                    }
                }
                Collections.reverse(dataList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
