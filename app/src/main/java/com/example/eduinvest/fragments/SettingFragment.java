package com.example.eduinvest.fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dino.rate.RatingDialog;
import com.example.eduinvest.R;
import com.example.eduinvest.activity.UserProfileActivity;
import com.example.eduinvest.databinding.FragmentSettingBinding;
import com.example.eduinvest.utils.Common;

public class SettingFragment extends Fragment {
    private static final String PLAY_STORE_LINK = "https://play.google.com/store/apps/details?id=";
    private static final String TAG = "MoreFragment";

    private FragmentSettingBinding binding;

    public SettingFragment() {

    }

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        setupListeners();

        return view;
    }

    private void setupListeners() {
        //Câu hỏi thường gặp
        binding.btnAsk.setOnClickListener(v -> showFeatureComingSoonToast());
        // Huong dan su dung
        binding.btnInstructions.setOnClickListener(v -> showFeatureComingSoonToast());
        //Điều khoản sử dụng
        binding.btnTerms.setOnClickListener(v -> showFeatureComingSoonToast());
        //Đánh giá app
        binding.btnFeedback.setOnClickListener(v -> sendEmailFeedBack());
        //Mời bạn bè
        binding.btnInvite.setOnClickListener(v -> shareAppLink());
        //Đánh giá app
        binding.btnEvaluate.setOnClickListener(v -> Common.showRate(requireActivity()));
        //Theo dôỗi app trên tiktok
        binding.btnTiktok.setOnClickListener(v -> showFeatureComingSoonToast());
        //Theo dõi app trên facebook
        binding.btnFacebook.setOnClickListener(v -> showFeatureComingSoonToast());
        //Thay đổi ngôn ngữ
        binding.btnLanguage.setOnClickListener(v -> showFeatureComingSoonToast());
        //Âm thanh thông báo
        binding.btnSound.setOnClickListener(v -> showFeatureComingSoonToast());
        //Quản lý thông báo
        binding.btnNotification.setOnClickListener(v -> showFeatureComingSoonToast());

    }

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

    private void sendEmailFeedBack() {
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

    private void openUserProfile() {
        if (!isAdded()) return;
        try {
            Intent intent = new Intent(getActivity(), UserProfileActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Error opening UserProfileActivity: " + e.getMessage(), e);
        }
    }

    private void showFeatureComingSoonToast() {
        if (!isAdded()) return;
        Toast.makeText(requireContext(), "Chức năng sẽ được cập nhật sớm nhất đến với bạn", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
