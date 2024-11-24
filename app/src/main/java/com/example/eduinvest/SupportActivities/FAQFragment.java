package com.example.eduinvest.SupportActivities;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        faqList = new ArrayList<>(); // Khởi tạo danh sách ở đây
        // Thêm dữ liệu mẫu
        faqList.add(new FAQModel("Giới thiệu về EduInvest", "EduInvest là một nền tảng Fintech kết nối sinh viên và nhà đầu tư. " +
                                         "Chúng tôi giúp sinh viên tiếp cận nguồn vốn học phí với lãi suất ưu đãi, đồng thời tạo ra cơ hội đầu tư an toàn và có ý nghĩa xã hội cho nhà đầu tư."));
        faqList.add(new FAQModel("Giải pháp của EduInvest mang đến cho khách hàng.", "Chúng tôi cung cấp nền tảng cho vay ngang hàng (P2P lending) " +
                                         "dành riêng cho sinh viên. Bằng việc sử dụng công nghệ đánh giá tín dụng dựa trên kỹ năng và tiềm năng của sinh viên, chúng tôi giúp sinh viên tiếp cận vốn với quy trình minh bạch và lãi suất hợp lý."));
        faqList.add(new FAQModel("Giá trị cốt lõi ","Chúng tôi cung cấp các tính năng độc đáo như mentorship giữa nhà đầu tư và sinh viên, chương trình học bổng cộng đồng, " +
                                         "hệ thống tài trợ kỹ năng, và cơ chế bảo trợ từ các cá nhân, doanh nghiệp lớn."));
        faqList.add(new FAQModel("Điểm khác biệt của EduInvest","EduInvest không chỉ đơn thuần hướng đến lợi nhuận mà còn đặt giá trị xã hội làm trọng tâm trong mọi hoạt động." +
                                         " Chúng tôi cam kết xây dựng các dự án mang lại tác động tích cực và bền vững, góp phần cải thiện chất lượng cuộc sống và nâng cao cơ hội giáo dục cho cộng đồng. Bằng cách cân bằng giữa phát triển kinh tế và trách nhiệm xã hội, EduInvest đặt mục tiêu tạo ra những giá trị lâu dài, góp phần xây dựng một tương lai thịnh vượng và tốt đẹp hơn cho tất cả mọi người."));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_f_a_q, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.faqRecyclerView); // Đảm bảo đúng ID
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Tạo adapter với dữ liệu
        faqAdapter = new FAQAdapter(faqList);
        recyclerView.setAdapter(faqAdapter);
        //tim kiem
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
