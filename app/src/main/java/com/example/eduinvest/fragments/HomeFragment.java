package com.example.eduinvest.fragments;

import android.content.ActivityNotFoundException;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.eduinvest.R;
import com.example.eduinvest.activity.UserProfileActivity;
import com.example.eduinvest.databinding.FragmentHomeBinding;
import com.example.eduinvest.loanactivities.ManageLoanActivities;
import com.example.eduinvest.models.BannerModel;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private static final String TAG = "HomeFragment";

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
    }

    private void loadImageSlider() {
        if (!isAdded()) return;

        ImageSlider imageSlider = binding.ImageSlide;
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        List<String> linkWebsites = new ArrayList<>();
        // Sử dụng ExecutorService để thực hiện tải dữ liệu trên background thread
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            databaseReference = FirebaseDatabase.getInstance().getReference("Banner");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!isAdded()) return;

                    slideModels.clear();
                    linkWebsites.clear();

                    RequestOptions requestOptions = new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop()
                            .placeholder(R.drawable.image_eduinvest)
                            .error(R.drawable.image_eduinvest);

                    for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                        BannerModel imageModel = itemSnapshot.getValue(BannerModel.class);
                        if (imageModel != null && imageModel.getUrlImage() != null &&
                                (imageModel.getNoteImage().equals("BANNER1") || imageModel.getNoteImage().equals("All"))) {
                            // Preload ảnh vào cache
                            Glide.with(requireContext())
                                    .load(imageModel.getUrlImage())
                                    .apply(requestOptions)
                                    .preload();

                            slideModels.add(new SlideModel(imageModel.getUrlImage(), ScaleTypes.FIT));
                            linkWebsites.add(imageModel.getLinkWeb());
                        }
                    }

                    // Cập nhật giao diện trên luồng chính
                    requireActivity().runOnUiThread(() -> {
                        imageSlider.setImageList(slideModels, ScaleTypes.FIT);
                        imageSlider.setItemClickListener(i -> {
                            if (i >= 0 && i < linkWebsites.size()) {
                                String linkWebsite = linkWebsites.get(i);
                                if (linkWebsite != null && !linkWebsite.isEmpty() &&
                                        !linkWebsite.equals("No Link Website")) {
                                    openWebsite(linkWebsite);
                                } else {
                                    showToast("Không có đường dẫn web.");
                                }
                            }
                        });
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Failed to load banner images: " + error.getMessage());
                }
            });
        });
        executorService.shutdown();
    }

    // Mở đường dẫn website
    private void openWebsite(String url) {
        if (!isAdded()) return;
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            showToast("Không thể mở đường dẫn web.");
        }
    }

    private void initializeActivities() {
        auth = FirebaseAuth.getInstance();

        try {
            binding.iconImage.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), UserProfileActivity.class);
                startActivity(intent);
            });
        } catch (Exception e) {
            Log.e("Error", Objects.requireNonNull(e.getMessage()));
        }

        // Các sự kiện click khác
        setupClickListener(binding.iconNotifition, this::showFeatureNotAvailableToast);
        setupClickListener(binding.loanCard,
                () -> startActivity(new Intent(getActivity(), ManageLoanActivities.class)));
        setupClickListener(binding.payCard, this::showFeatureNotAvailableToast);
        setupClickListener(binding.row4, this::showFeatureNotAvailableToast);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
