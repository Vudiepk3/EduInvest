package com.example.eduinvest.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.eduinvest.adapters.FAQAdapter;
import com.example.eduinvest.databinding.ActivityFaqactivityBinding;
import com.example.eduinvest.models.FAQModel;

import java.util.ArrayList;
import java.util.List;

public class FAQActivity extends AppCompatActivity {
    private ActivityFaqactivityBinding binding;
    private FAQAdapter faqAdapter;
    private List<FAQModel> faqList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Khởi tạo view binding
        binding = ActivityFaqactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo danh sách câu hỏi
        initFAQList();

        // Thiết lập RecyclerView
        binding.faqRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        faqAdapter = new FAQAdapter(faqList);
        binding.faqRecyclerView.setAdapter(faqAdapter);

        // Xử lý tìm kiếm
        binding.search.clearFocus();
        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

    private void initFAQList() {
        faqList = new ArrayList<>();
        faqList.add(new FAQModel("Giới thiệu về EduInvest", "EduInvest là một nền tảng Fintech kết nối sinh viên và nhà đầu tư. " +
                "Chúng tôi giúp sinh viên tiếp cận nguồn vốn học phí với lãi suất ưu đãi, đồng thời tạo ra cơ hội đầu tư an toàn và có ý nghĩa xã hội cho nhà đầu tư."));
        faqList.add(new FAQModel("Giải pháp của EduInvest mang đến cho khách hàng.", "Chúng tôi cung cấp nền tảng cho vay ngang hàng (P2P lending) " +
                "dành riêng cho sinh viên. Bằng việc sử dụng công nghệ đánh giá tín dụng dựa trên kỹ năng và tiềm năng của sinh viên, chúng tôi giúp sinh viên tiếp cận vốn với quy trình minh bạch và lãi suất hợp lý."));
        faqList.add(new FAQModel("Giá trị cốt lõi ","Chúng tôi cung cấp các tính năng độc đáo như mentorship giữa nhà đầu tư và sinh viên, chương trình học bổng cộng đồng, " +
                "hệ thống tài trợ kỹ năng, và cơ chế bảo trợ từ các cá nhân, doanh nghiệp lớn."));
        faqList.add(new FAQModel("Điểm khác biệt của EduInvest","EduInvest không chỉ đơn thuần hướng đến lợi nhuận mà còn đặt giá trị xã hội làm trọng tâm trong mọi hoạt động." +
                "Chúng tôi cam kết xây dựng các dự án mang lại tác động tích cực và bền vững, góp phần cải thiện chất lượng cuộc sống và nâng cao cơ hội giáo dục cho cộng đồng. " +
                "   Bằng cách cân bằng giữa phát triển kinh tế và trách nhiệm xã hội, EduInvest đặt mục tiêu tạo ra những giá trị lâu dài, góp phần xây dựng một tương lai thịnh vượng và tốt đẹp hơn cho tất cả mọi người."));
    }

    private void searchList(String text) {
        ArrayList<FAQModel> searchList = new ArrayList<>();
        for (FAQModel data : faqList) {
            if (data.getQuestion().toLowerCase().contains(text.toLowerCase()) ||
                    data.getAnswer().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(data);
            }
        }
        faqAdapter.searchDataList(searchList);
    }
}
