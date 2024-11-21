package com.example.eduinvest.LoanActivities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Handler;

import com.example.eduinvest.R;
import com.example.eduinvest.adapters.LoanRequestAdapter;
import com.example.eduinvest.models.LoanRequestModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class SecondLoanFragment extends Fragment {
    private LoanRequestAdapter adapter;
    private ArrayList<LoanRequestModel> dataList;
    private DatabaseReference databaseReference;
    private ValueEventListener eventListener;

    public SecondLoanFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        Query query = databaseReference.orderByChild("timestamp");

        eventListener = query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    LoanRequestModel dataClass = itemSnapshot.getValue(LoanRequestModel.class);
                    if (dataClass != null && dataClass.getIdUser().equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())) {
                        dataClass.setKey(itemSnapshot.getKey());
                        dataList.add(dataClass);
                    }
                }

                Collections.reverse(dataList); // Đảo ngược danh sách để hiển thị yêu cầu mới nhất trước tiên
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });
        // Tìm kiếm thông  tin
        SearchView searchView = view.findViewById(R.id.search);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

        return view;
    }

    // tìm kiếm thông tin đối tượng
    public void searchList(String text) {
        ArrayList<LoanRequestModel> searchList = new ArrayList<>();
        for (LoanRequestModel dataClass : dataList) {
            if (dataClass.getRateBank().toLowerCase().contains(text.toLowerCase()) ||
                    dataClass.getNamePerson().toLowerCase().contains(text.toLowerCase())||
                    dataClass.getLimitBank().toLowerCase().contains(text.toLowerCase())||
                    dataClass.getLoanPeriodBank().toLowerCase().contains(text.toLowerCase())||
                    dataClass.getGender().toLowerCase().contains(text.toLowerCase()) ||
                    dataClass.getBirthDate().toLowerCase().contains(text.toLowerCase()) ||
                    dataClass.getPhoneNumber().toLowerCase().contains(text.toLowerCase()) ||
                    dataClass.getEmail().toLowerCase().contains(text.toLowerCase()) ||
                    dataClass.getTitleBank().toLowerCase().contains(text.toLowerCase()) ||
                    dataClass.getNameBank().toLowerCase().contains(text.toLowerCase()) ||
                    dataClass.getLoanPeriodBank().toLowerCase().contains(text.toLowerCase()) ||
                    dataClass.getLimitBank().toLowerCase().contains(text.toLowerCase()) ||
                    dataClass.getStatus().toLowerCase().contains(text.toLowerCase())
            ) {

                searchList.add(dataClass);
            }
        }
        adapter.searchDataList(searchList);
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
