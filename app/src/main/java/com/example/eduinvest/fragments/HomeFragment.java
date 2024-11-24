package com.example.eduinvest.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.eduinvest.LoanActivities.ManageLoanActivities;
import com.example.eduinvest.R;
import com.example.eduinvest.UserProfileActivity;
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
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private ValueEventListener eventListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    //Gắn kết và xử lý logic
    
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseApp.initializeApp(requireContext()); // Khởi tạo Firebase
        initializeActivities(view);
        ImageView iconImage = view.findViewById(R.id.iconImage);

        // Lấy thông tin user hiện tại
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            Uri photoUrl = currentUser.getPhotoUrl();
            if (photoUrl != null) {
                // Sử dụng Glide để tải ảnh vào iconImage
                Glide.with(this)
                        .load(photoUrl)
                        .placeholder(R.drawable.image_eduinvest)
                        .error(R.drawable.image_eduinvest)
                        .into(iconImage);
            } else {
                iconImage.setImageResource(R.drawable.image_eduinvest);
            }
        }

        loadImageSlider(view); // Tải ảnh slider
    }

    private void loadImageSlider(View view) {
        if (!isAdded()) return; // Kiểm tra Fragment đã được gắn vào Activity

        ImageSlider imageSlider = view.findViewById(R.id.ImageSlide);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        List<String> linkWebsites = new ArrayList<>();

        // Tạo ExecutorService để chạy tác vụ nền
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(() -> {
            databaseReference = FirebaseDatabase.getInstance().getReference("Banner");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!isAdded()) return;

                    slideModels.clear();
                    linkWebsites.clear();

                    for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                        BannerModel imageModel = itemSnapshot.getValue(BannerModel.class);
                        if (imageModel != null && imageModel.getUrlImage() != null &&
                                (imageModel.getNoteImage().equals("BANNER1") || imageModel.getNoteImage().equals("All"))) {
                            // Sử dụng Glide để caching ảnh
                            Glide.with(requireContext())
                                    .load(imageModel.getUrlImage())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
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
                                if (linkWebsite != null && !linkWebsite.isEmpty() && !linkWebsite.equals("No Link Website")) {
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
                    requireActivity().runOnUiThread(() -> showToast("Failed to load data."));
                }
            });
        });

        executorService.shutdown();
    }
    // mở đường dẫn kèm theo
    private void openWebsite(String url) {
        if (!isAdded()) return;
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            showToast("Không thể mở đường dẫn web.");
        }
    }

    private void initializeActivities(View view) {
        auth = FirebaseAuth.getInstance();
        ImageView iconImage = view.findViewById(R.id.iconImage);

        try {
            iconImage.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), UserProfileActivity.class);
                startActivity(intent);
            });
        } catch (Exception e) {
            Log.e("Error", Objects.requireNonNull(e.getMessage()));
        }

        setupClickListener(view.findViewById(R.id.iconNotifition), this::showFeatureNotAvailableToast);
        setupClickListener(view.findViewById(R.id.loanCard), () -> startActivity(new Intent(getActivity(), ManageLoanActivities.class)));
        setupClickListener(view.findViewById(R.id.payCard), this::showFeatureNotAvailableToast);
        setupClickListener(view.findViewById(R.id.savingCard), this::showFeatureNotAvailableToast);
        setupClickListener(view.findViewById(R.id.row3), this::showFeatureNotAvailableToast);
        setupClickListener(view.findViewById(R.id.row4), this::showFeatureNotAvailableToast);
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
   // chức năng thông báo
    private void showToast(String message) {
        if (isAdded()) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (databaseReference != null && eventListener != null) {
            databaseReference.addValueEventListener(eventListener);
        }
    }
}
