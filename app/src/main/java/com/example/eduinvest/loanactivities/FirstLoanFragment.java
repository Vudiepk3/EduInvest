package com.example.eduinvest.loanactivities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.eduinvest.R;
import com.example.eduinvest.adapters.LoanAdapter;
import com.example.eduinvest.databinding.FragmentFirstLoanBinding;
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
    private FragmentFirstLoanBinding binding;

    public FirstLoanFragment() {
        // Yêu cầu có constructor public không tham số
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Khởi tạo ExecutorService
        executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Khởi tạo binding cho fragment_first_loan.xml
        binding = FragmentFirstLoanBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    // Khởi tạo và cấu hình các thành phần giao diện người dùng sau khi View đã được tạo ra
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Thiết lập LayoutManager cho RecyclerView
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));

        // Hiển thị dialog tiến trình
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Khởi tạo danh sách dữ liệu và adapter
        dataList = new ArrayList<>();
        adapter = new LoanAdapter(getContext(), dataList);
        binding.recyclerView.setAdapter(adapter);

        // Tải dữ liệu từ Firebase sử dụng ExecutorService
        loadDataFromFirebase(dialog);

        // Xử lý sự kiện khi nhấn FloatingActionButton
        binding.fab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), UploadLoanRequestActivity.class);
            startActivity(intent);
        });

        // Xử lý tìm kiếm thông tin
        binding.search.clearFocus();
        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Không xử lý submit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Tìm kiếm theo từ khóa mới nhập
                searchList(newText);
                return true;
            }
        });
    }

    // Phương thức tải dữ liệu từ Firebase
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
                        if (dataClass != null) {
                            dataClass.setKey(itemSnapshot.getKey());
                            tempList.add(dataClass);
                        }
                    }
                    // Đảo ngược danh sách và cập nhật giao diện trên MainThread
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

    // Phương thức tìm kiếm dữ liệu theo từ khóa
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

    // Giải phóng các tài nguyên để tránh rò rỉ bộ nhớ
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown(); // Dừng ExecutorService để tránh rò rỉ tài nguyên
        }
        binding = null;
    }
}
