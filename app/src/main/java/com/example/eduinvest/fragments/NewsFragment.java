package com.example.eduinvest.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eduinvest.adapters.NewsAdapter;
import com.example.eduinvest.databinding.FragmentNewsBinding;
import com.example.eduinvest.models.NewsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewsFragment extends Fragment {
    private FragmentNewsBinding binding;
    private List<NewsModel> dataList;
    private NewsAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNewsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadNewsData();
    }
    // Hàm load tin tức từ Firebase
    private void loadNewsData() {
        binding.recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 1));

        dataList = new ArrayList<>();
        adapter = new NewsAdapter(requireContext(), dataList);
        binding.recyclerView.setAdapter(adapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("News");
        Query query = databaseReference.orderByChild("timestamp");

        ValueEventListener eventListener = query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    NewsModel news = itemSnapshot.getValue(NewsModel.class);
                    if (news != null) {
                        news.setKey(itemSnapshot.getKey());
                        dataList.add(news);
                    }
                }
                Collections.reverse(dataList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
