package com.example.eduinvest.repository;

import android.util.Log;

import androidx.annotation.NonNull;

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

public class NewsRepository {
    private static NewsRepository instance;
    private final List<NewsModel> newsList = new ArrayList<>();
    private boolean isDataLoaded = false;

    private NewsRepository() {
        loadData();
    }

    public static synchronized NewsRepository getInstance() {
        if (instance == null) {
            instance = new NewsRepository();
        }
        return instance;
    }

    private void loadData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("News");
        Query query = databaseReference.orderByChild("timestamp");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newsList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    NewsModel news = itemSnapshot.getValue(NewsModel.class);
                    if (news != null) {
                        news.setKey(itemSnapshot.getKey());
                        newsList.add(news);
                    }
                }
                Collections.reverse(newsList);
                isDataLoaded = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("NewsRepository", "Failed to load news: " + error.getMessage());
            }
        });
    }

    public boolean isDataLoaded() {
        return isDataLoaded;
    }

    public List<NewsModel> getNewsList() {
        return newsList;
    }

    // Lọc danh sách theo typeNews
    public List<NewsModel> getNewsByType(String typeNews) {
        List<NewsModel> filteredList = new ArrayList<>();
        for (NewsModel news : newsList) {
            if (news.getTypeNews().equals(typeNews)) {
                filteredList.add(news);
            }
        }
        return filteredList;
    }
}
