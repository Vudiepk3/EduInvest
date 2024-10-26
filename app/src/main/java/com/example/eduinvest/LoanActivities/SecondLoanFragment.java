package com.example.eduinvest.LoanActivities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Handler;

import com.example.eduinvest.R;
import com.example.eduinvest.adapters.LoanRequestAdapter;
import com.example.eduinvest.models.LoanRequestModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SecondLoanFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView txtNumberImage;
    private LoanRequestAdapter adapter;
    private ArrayList<LoanRequestModel> dataList;
    private DatabaseReference databaseReference;
    private ValueEventListener eventListener;

    public SecondLoanFragment() {
        // Required empty public constructor
    }

    public static FirstLoanFragment newInstance(String param1, String param2) {
        FirstLoanFragment fragment = new FirstLoanFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second_loan, container, false);

        // Initialize and bind the RecyclerView and other UI elements
        // Declare your views and references
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);


        // Set up LayoutManager for RecyclerView
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Show progress dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Dismiss the dialog after 5 seconds
        new Handler().postDelayed(dialog::dismiss, 5000);

        // Initialize the data list and RecyclerView Adapter
        dataList = new ArrayList<>();
        adapter = new LoanRequestAdapter(getContext(), dataList);
        recyclerView.setAdapter(adapter);

        // Listen for data changes in Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("LoanRequest");
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    LoanRequestModel dataClass = itemSnapshot.getValue(LoanRequestModel.class);
                    if (dataClass != null) {
                        dataClass.setKey(itemSnapshot.getKey());
                        dataList.add(dataClass);
                    }
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });




        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        // Re-register eventListener when the fragment becomes active
        if (databaseReference != null && eventListener != null) {
            databaseReference.addValueEventListener(eventListener);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Unregister eventListener when the fragment is destroyed to avoid memory leaks
        if (databaseReference != null && eventListener != null) {
            databaseReference.removeEventListener(eventListener);
        }
    }
}
