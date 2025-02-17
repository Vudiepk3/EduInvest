package com.example.eduinvest.loanactivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eduinvest.R;
import com.example.eduinvest.adapters.LoanRequestAdapter;
import com.example.eduinvest.databinding.FragmentSecondLoanBinding;
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
    private FragmentSecondLoanBinding binding;

    public SecondLoanFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Sử dụng View Binding để inflate layout
        binding = FragmentSecondLoanBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

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

        // Khởi tạo danh sách dữ liệu và adapter cho RecyclerView
        dataList = new ArrayList<>();
        adapter = new LoanRequestAdapter(getContext(), dataList);
        binding.recyclerView.setAdapter(adapter);

        // Lắng nghe thay đổi dữ liệu từ Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("LoanRequest");
        Query query = databaseReference.orderByChild("timestamp");

        // Sử dụng addListenerForSingleValueEvent để chỉ lắng nghe dữ liệu một lần
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    LoanRequestModel dataClass = itemSnapshot.getValue(LoanRequestModel.class);
                    // Kiểm tra nếu đối tượng không null và idUser khớp với người dùng hiện tại
                    if (dataClass != null && dataClass.getIdUser()
                            .equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())) {
                        dataClass.setKey(itemSnapshot.getKey());
                        dataList.add(dataClass);
                    }
                }

                Collections.reverse(dataList);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });

        // Xử lý tìm kiếm thông tin
        binding.search.clearFocus();
        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String queryText) {
                // Không xử lý sự kiện submit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });
    }

    // Phương thức tìm kiếm thông tin đối tượng dựa trên từ khóa
    public void searchList(String text) {
        ArrayList<LoanRequestModel> searchList = new ArrayList<>();
        for (LoanRequestModel dataClass : dataList) {
            if (dataClass.getRateBank().toLowerCase().contains(text.toLowerCase()) ||
                    dataClass.getNamePerson().toLowerCase().contains(text.toLowerCase()) ||
                    dataClass.getLimitBank().toLowerCase().contains(text.toLowerCase()) ||
                    dataClass.getLoanPeriodBank().toLowerCase().contains(text.toLowerCase()) ||
                    dataClass.getGender().toLowerCase().contains(text.toLowerCase()) ||
                    dataClass.getBirthDate().toLowerCase().contains(text.toLowerCase()) ||
                    dataClass.getPhoneNumber().toLowerCase().contains(text.toLowerCase()) ||
                    dataClass.getEmail().toLowerCase().contains(text.toLowerCase()) ||
                    dataClass.getTitleBank().toLowerCase().contains(text.toLowerCase()) ||
                    dataClass.getNameBank().toLowerCase().contains(text.toLowerCase()) ||
                    dataClass.getStatus().toLowerCase().contains(text.toLowerCase())) {

                searchList.add(dataClass);
            }
        }
        adapter.searchDataList(searchList);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
