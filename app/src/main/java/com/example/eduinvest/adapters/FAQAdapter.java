package com.example.eduinvest.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eduinvest.R;
import com.example.eduinvest.models.FAQModel;

import java.util.ArrayList;
import java.util.List;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.FAQViewHolder> {
    private List<FAQModel> faqList;
    private List<FAQModel> faqListFull; // Danh sách đầy đủ để tìm kiếm

    public FAQAdapter(List<FAQModel> faqList) {
        this.faqList = faqList;
    }

    @NonNull
    @Override
    public FAQViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_faq, parent, false);
        return new FAQViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FAQViewHolder holder, int position) {
        FAQModel faq = faqList.get(position);
        holder.questionTextView.setText(faq.getQuestion());
        holder.answerTextView.setText(faq.getAnswer());
        // Đặt trạng thái ban đầu của answerTextView là ẩn
        holder.answerTextView.setVisibility(View.GONE);
        // Thiết lập sự kiện khi người dùng nhấn vào câu hỏi
        holder.itemView.setOnClickListener(v -> {
            // Tùy thuộc vào logic của bạn, bạn có thể ẩn hiện câu trả lời hoặc thực hiện một hành động khác
            if (holder.answerTextView.getVisibility() == View.VISIBLE) {
                holder.answerTextView.setVisibility(View.GONE);
            } else {
                holder.answerTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return faqList.size();
    }

    public static class FAQViewHolder extends RecyclerView.ViewHolder {
        TextView questionTextView;
        TextView answerTextView;

        public FAQViewHolder(@NonNull View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.faqQuestion);
            answerTextView = itemView.findViewById(R.id.faqAnswer);
        }
    }
    public void searchDataList(ArrayList<FAQModel> searchList){
        faqList = searchList;
        notifyDataSetChanged();
    }
}

