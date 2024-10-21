package com.example.eduinvest.SupportActivities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.eduinvest.R;
import com.example.eduinvest.adapters.FAQAdapter;
import com.example.eduinvest.models.FAQModel;

import java.util.ArrayList;
import java.util.List;

public class FAQFragment extends Fragment {

    private FAQAdapter faqAdapter;
    private List<FAQModel> faqList; // Thêm danh sách câu hỏi

    public FAQFragment() {
        // Required empty public constructor
    }

    public static FAQFragment newInstance(String param1, String param2) {
        FAQFragment fragment = new FAQFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        faqList = new ArrayList<>(); // Khởi tạo danh sách ở đây
        // Thêm dữ liệu mẫu
        faqList.add(new FAQModel("Câu hỏi 1", "Câu trả lời cho câu hỏi 1."));
        faqList.add(new FAQModel("Câu hỏi 2", "Câu trả lời cho câu hỏi 2."));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_f_a_q, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.faqRecyclerView); // Đảm bảo đúng ID
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Tạo adapter với dữ liệu
        faqAdapter = new FAQAdapter(faqList);
        recyclerView.setAdapter(faqAdapter);

        return view;
    }

    // Search functionality
    public void searchList(String text) {
        ArrayList<FAQModel> searchList = new ArrayList<>();
        for (FAQModel dataClass : faqList) {
            if (dataClass.getQuestion().toLowerCase().contains(text.toLowerCase())
                    || dataClass.getAnswer().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(dataClass);
            }
        }
        faqAdapter.searchDataList(searchList);
    }
}
