package com.example.eduinvest.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.eduinvest.models.BannerModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BannerRepository {
    private static BannerRepository instance;
    private final List<SlideModel> slideModels = new ArrayList<>();
    private final List<String> linkWebsites = new ArrayList<>();
    private boolean isDataLoaded = false;
    private final DatabaseReference databaseReference;

    private BannerRepository() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Banner");
    }

    public static synchronized BannerRepository getInstance() {
        if (instance == null) {
            instance = new BannerRepository();
        }
        return instance;
    }

    // Gọi khi mở app để tải dữ liệu
    public void loadData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                slideModels.clear();
                linkWebsites.clear();

                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    BannerModel imageModel = itemSnapshot.getValue(BannerModel.class);
                    if (imageModel != null && imageModel.getUrlImage() != null &&
                            (imageModel.getNoteImage().equals("BANNER1") || imageModel.getNoteImage().equals("All"))) {
                        slideModels.add(new SlideModel(imageModel.getUrlImage(), ScaleTypes.FIT));
                        linkWebsites.add(imageModel.getLinkWeb());
                    }
                }
                isDataLoaded = true;
                notifyDataChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("BannerRepository", "Failed to load banner images: " + error.getMessage());
            }
        });
    }

    public boolean isDataLoaded() {
        return isDataLoaded;
    }

    public List<SlideModel> getSlideModels() {
        return new ArrayList<>(slideModels);
    }

    public List<String> getLinkWebsites() {
        return new ArrayList<>(linkWebsites);
    }

    private OnDataChangedListener onDataChangedListener;
    public void setOnDataChangedListener(OnDataChangedListener listener) {
        this.onDataChangedListener = listener;
    }

    private void notifyDataChanged() {
        if (onDataChangedListener != null) {
            onDataChangedListener.onDataChanged();
        }
    }

    public interface OnDataChangedListener {
        void onDataChanged();
    }
}
