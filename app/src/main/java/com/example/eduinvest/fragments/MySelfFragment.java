package com.example.eduinvest.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.eduinvest.LoanActivities.DetailLoanActivity;
import com.example.eduinvest.R;
import com.example.eduinvest.UserProfileActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MySelfFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String PLAY_STORE_LINK = "https://play.google.com/store/apps/details?id=";
    private static final String TAG = "MoreFragment";

    public MySelfFragment() {
        // Required empty public constructor
    }

    public static MySelfFragment newInstance(String param1, String param2) {
        MySelfFragment fragment = new MySelfFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myself, container, false);
        getActivities(view);
        return view;
    }

    private void getActivities(View view) {
        setupClickListener(view, R.id.scholarshipRelative, this::showFeatureComingSoonToast);
        setupClickListener(view, R.id.internshipRelative, this::showFeatureComingSoonToast);

        setupClickListener(view, R.id.rate, this::openPlayStoreForRating);
        setupClickListener(view, R.id.share, this::shareAppLink);
        setupClickListener(view, R.id.contact, this::sendEmailIntent);
        setupClickListener(view, R.id.donateRelative, this::showFeatureComingSoonToast);
        setupClickListener(view, R.id.UserRelative, this::openUserProfile);
        CardView rewardCard = view.findViewById(R.id.rewardCard);
        if (rewardCard != null) {
            rewardCard.setOnClickListener(v -> showFeatureComingSoonToast());
        } else {
            Log.e(TAG, "View with ID rewardCard not found in layout.");
        }
    }

    private void setupClickListener(View view, int resId, Runnable action) {
        RelativeLayout layout = view.findViewById(resId);
        if (layout != null) {
            layout.setOnClickListener(v -> action.run());
        } else {
            Log.e(TAG, "View with ID " + resId + " not found in layout.");
        }
    }

    // Phương thức đánh giá app
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
            String shareMessage = "This is an awesome app for your Android mobile. Check it out: " + PLAY_STORE_LINK + requireContext().getPackageName();
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
}
