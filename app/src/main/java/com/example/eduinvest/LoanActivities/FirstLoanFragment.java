package com.example.eduinvest.LoanActivities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Handler;

import com.example.eduinvest.R;
import com.example.eduinvest.adapters.LoanAdapter;
import com.example.eduinvest.models.LoanModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FirstLoanFragment extends Fragment {
    private LoanAdapter adapter;
    private ArrayList<LoanModel> dataList;
    private DatabaseReference databaseReference;
    private ExecutorService executorService;

    public FirstLoanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Khởi tạo ExecutorService
        executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first_loan, container, false);
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        SearchView searchView = view.findViewById(R.id.search);
        searchView.clearFocus();

        // Set up LayoutManager for RecyclerView
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Show progress dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataList = new ArrayList<>();
        adapter = new LoanAdapter(getContext(), dataList);
        recyclerView.setAdapter(adapter);

        // Tải dữ liệu từ Firebase bằng ExecutorService
        loadDataFromFirebase(dialog);

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), UploadLoanRequestActivity.class);
            startActivity(intent);
        });

        // Tìm kiếm
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
    }
    private void loadDataFromFirebase(AlertDialog dialog) {
        executorService.execute(() -> {
            databaseReference = FirebaseDatabase.getInstance().getReference("Loan");
            Query query = databaseReference.orderByChild("timestamp");

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<LoanModel> tempList = new ArrayList<>();
                    for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                        LoanModel dataClass = itemSnapshot.getValue(LoanModel.class);
                        if (dataClass != null && "VAYUUDAI".equals(dataClass.getTypeBank())) {
                            dataClass.setKey(itemSnapshot.getKey());
                            tempList.add(dataClass);
                        }
                    }

                    // Đảo danh sách và cập nhật giao diện trên MainThread
                    Collections.reverse(tempList);
                    new Handler(Looper.getMainLooper()).post(() -> {
                        dataList.clear();
                        dataList.addAll(tempList);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    new Handler(Looper.getMainLooper()).post(dialog::dismiss);
                }
            });
        });
    }

    public void searchList(String text) {
        ArrayList<LoanModel> searchList = new ArrayList<>();
        for (LoanModel dataClass : dataList) {
            if (dataClass.getTitleBank().toLowerCase().contains(text.toLowerCase()) ||
                    dataClass.getNameBank().toLowerCase().contains(text.toLowerCase()) ||
                    dataClass.getRateBank().toLowerCase().contains(text.toLowerCase()) ||
                    dataClass.getLoanPeriodBank().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(dataClass);
            }
        }
        adapter.searchDataList(searchList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown(); // Dừng ExecutorService để tránh rò rỉ tài nguyên
        }
    }
}
