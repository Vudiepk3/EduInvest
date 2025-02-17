package com.example.eduinvest.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.eduinvest.UserProfileActivity;
import com.example.eduinvest.databinding.FragmentMyselfBinding;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class MySelfFragment extends Fragment {
    private static final String PLAY_STORE_LINK = "https://play.google.com/store/apps/details?id=";
    private static final String TAG = "MoreFragment";

    // Biến binding để truy cập các view trong layout fragment_myself.xml
    private FragmentMyselfBinding binding;

    public MySelfFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Sử dụng View Binding để inflate layout và truy cập các view
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyselfBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivities();
    }
    // Thiết lập các sự kiện click cho các view thông qua binding
    private void getActivities() {
        binding.btnScholarship.onClickListener(this::showFeatureComingSoonToast);
        binding.btnInternship.onClickListener(this::showFeatureComingSoonToast);
        binding.btnRate.onClickListener(this::openPlayStoreForRating);
        binding.btnShare.onClickListener(this::shareAppLink);
        binding.btnContact.onClickListener(this::sendEmailIntent);
        binding.btnDonate.onClickListener(this::showFeatureComingSoonToast);
        binding.UserRelative.setOnClickListener(v -> openUserProfile());
        CardView rewardCard = binding.rewardCard;
        rewardCard.setOnClickListener(v -> showFeatureComingSoonToast());
    }

    // Phương thức mở Play Store để đánh giá app
    private void openPlayStoreForRating() {
        if (!isAdded()) return;
        try {
            Intent rateIntent = new Intent(Intent.ACTION_VIEW);
            rateIntent.setData(Uri.parse(PLAY_STORE_LINK + requireContext().getPackageName()));
            rateIntent.setPackage("com.android.vending");
            startActivity(rateIntent);
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, "Play Store not found, opening in browser.", e);
            Intent rateIntent = new Intent(Intent.ACTION_VIEW);
            rateIntent.setData(Uri.parse("market://details?id=" + requireContext().getPackageName()));
            startActivity(rateIntent);
        }
    }

    // Phương thức chia sẻ link app
    private void shareAppLink() {
        if (!isAdded()) return;
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareMessage = "This is an awesome app for your Android mobile. Check it out: "
                    + PLAY_STORE_LINK + requireContext().getPackageName();
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Share Link"));
        } catch (Exception e) {
            Log.e(TAG, "Error sharing: " + e.getMessage(), e);
        }
    }

    // Phương thức gửi email
    private void sendEmailIntent() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"vulq2k3@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Liên Hệ Hợp Tác");
        try {
            startActivity(Intent.createChooser(emailIntent, "Send email via"));
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "No email app found.", Toast.LENGTH_SHORT).show();
        }
    }

    // Phương thức mở trang User Profile
    private void openUserProfile() {
        if (!isAdded()) return;
        try {
            Intent intent = new Intent(getActivity(), UserProfileActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Error opening UserProfileActivity: " + e.getMessage(), e);
        }
    }

    // Hiển thị thông báo "Tính năng sắp ra mắt"
    private void showFeatureComingSoonToast() {
        if (!isAdded()) return;
        Toast.makeText(requireContext(), "Chức năng sẽ được cập nhật sớm nhất đến với bạn", Toast.LENGTH_SHORT).show();
    }

    // Giải phóng binding khi view bị hủy để tránh rò rỉ bộ nhớ
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
