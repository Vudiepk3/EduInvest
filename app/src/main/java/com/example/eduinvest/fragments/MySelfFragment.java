package com.example.eduinvest.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.example.eduinvest.R;

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
            // Retrieve passed parameters if needed
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
            // You can use mParam1 and mParam2 as needed
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myself, container, false);
        getActivities(view);
        return view;
    }

    private void getActivities(View view) {
        setupClickListener(view, R.id.scholarshipRelative, this::Toast);
        setupClickListener(view, R.id.internshipRelative, this::Toast);
        setupClickListener(view, R.id.rate, this::openPlayStoreForRating);
        setupClickListener(view, R.id.share, this::shareAppLink);
        setupClickListener(view, R.id.contact, this::sendEmailIntent);
        setupClickListener(view, R.id.donateRelative, this::Toast);
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
    //Phương thức gửi email
    private void sendEmailIntent() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("vulq2k3@gmail.com"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Liên Hệ Hợp Tác");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Body of the Email");

        if (emailIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(Intent.createChooser(emailIntent, "Send email via"));
        } else {
            Log.e(TAG, "No email app found.");
            Toast.makeText(getActivity(), "No email app found.", Toast.LENGTH_SHORT).show();
        }
    }
    private void Toast(){
        Toast.makeText(getActivity(), "Chức năng sẽ được cập nhật sớm nhất đến với bạn", Toast.LENGTH_SHORT).show();
    }

}
