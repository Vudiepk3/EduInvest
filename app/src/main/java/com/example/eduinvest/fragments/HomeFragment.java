package com.example.eduinvest.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;


import com.example.eduinvest.BankActivities.ManageLoanActivities;
import com.example.eduinvest.R;
import com.example.eduinvest.models.BannerModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate giao diện cho fragment này
       View view =  inflater.inflate(R.layout.fragment_home, container, false);
       getActivities(view);
       loadImageSlide(view);
       return view;
    }
    // Phương thức để tải và hiển thị slide ảnh từ Firebase
    private void loadImageSlide(View view) {

        ImageSlider imageSlider = view.findViewById(R.id.ImageSlide);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        List<String> linkWebsites = new ArrayList<>(); // Danh sách các link website

        // Lấy tham chiếu đến SlideImage trong Firebase
        // Tham chiếu cơ sở dữ liệu Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Banner");
        // Xóa danh sách hình ảnh cũ
        // Xóa danh sách link website cũ
        // Duyệt qua từng mục trong dữ liệu Firebase
        // Thêm hình ảnh vào danh sách slide
        // Thêm link website vào danh sách
        // Cập nhật danh sách hình ảnh lên ImageSlider
        // Đặt sự kiện click cho các hình ảnh trong slider
        // Listener để nhận dữ liệu từ Firebase
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                slideModels.clear(); // Xóa danh sách hình ảnh cũ
                linkWebsites.clear(); // Xóa danh sách link website cũ

                // Duyệt qua từng mục trong dữ liệu Firebase
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    BannerModel imageModel = itemSnapshot.getValue(BannerModel.class);
                    if (imageModel != null
                            && imageModel.getUrlImage() != null
                            && (imageModel.getNoteImage().equals("BANNER1") || imageModel.getNoteImage().equals("All"))) {

                        slideModels.add(new SlideModel(imageModel.getUrlImage(), ScaleTypes.FIT)); // Thêm hình ảnh vào danh sách slide
                        linkWebsites.add(imageModel.getLinkWeb()); // Thêm link website vào danh sách
                    }

                }

                // Cập nhật danh sách hình ảnh lên ImageSlider
                imageSlider.setImageList(slideModels, ScaleTypes.FIT);

                // Đặt sự kiện click cho các hình ảnh trong slider
                imageSlider.setItemClickListener(i -> {
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
                Toast.makeText(getActivity(), "Failed to load data.", Toast.LENGTH_SHORT).show();
            }
        };
        databaseReference.addValueEventListener(eventListener);
    }

    private void getActivities(View view){
        ImageView iconImage = view.findViewById(R.id.iconImage);
        iconImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast();
            }
        });
        ImageView iconNotifition = view.findViewById(R.id.iconNotifition);
        iconNotifition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast();
            }
        });
        CardView loanCard = view.findViewById(R.id.loanCard);
        try{
            loanCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), ManageLoanActivities.class);
                    startActivity(intent);
                }
            });
        }catch (Exception e){
            Log.e("Error", Objects.requireNonNull(e.getMessage()));
        }


    }
    private void Toast(){
        Toast.makeText(getActivity(), "Chức năng sẽ được cập nhật sớm nhất đến với bạn", Toast.LENGTH_SHORT).show();
    }
}
