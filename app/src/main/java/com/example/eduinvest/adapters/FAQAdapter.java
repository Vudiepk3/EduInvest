package com.example.eduinvest.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eduinvest.databinding.ItemFaqBinding;
import com.example.eduinvest.models.FAQModel;

import java.util.ArrayList;
import java.util.List;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.FAQViewHolder> {
    private List<FAQModel> faqList;

    public FAQAdapter(List<FAQModel> faqList) {
        this.faqList = faqList;
    }

    @NonNull
    @Override
    public FAQViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFaqBinding binding = ItemFaqBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FAQViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FAQViewHolder holder, int position) {
        FAQModel faq = faqList.get(position);
        holder.binding.faqQuestion.setText(faq.getQuestion());
        holder.binding.faqAnswer.setText(faq.getAnswer());

        // Đặt trạng thái ban đầu của câu trả lời là ẩn
        holder.binding.faqAnswer.setVisibility(View.GONE);

        // Xử lý khi click vào câu hỏi
        holder.binding.faqQuestion.setOnClickListener(v -> {
            if (holder.binding.faqAnswer.getVisibility() == View.VISIBLE) {
                holder.binding.faqAnswer.setVisibility(View.GONE);
            } else {
                holder.binding.faqAnswer.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return faqList.size();
    }

    public static class FAQViewHolder extends RecyclerView.ViewHolder {
        private final ItemFaqBinding binding;

        public FAQViewHolder(@NonNull ItemFaqBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void searchDataList(ArrayList<FAQModel> searchList) {
        faqList = searchList;
        notifyDataSetChanged();
    }
}
