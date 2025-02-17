package com.example.eduinvest.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eduinvest.databinding.ItemChatBinding;
import com.example.eduinvest.models.MessageModel;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    private final List<MessageModel> messageModelList;

    public MessageAdapter(List<MessageModel> messageModelList) {
        this.messageModelList = messageModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChatBinding binding = ItemChatBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MessageModel messageModel = messageModelList.get(position);
        if (messageModel.getSentBy().equals(MessageModel.SENT_BY_ME)) {
            holder.binding.leftChatView.setVisibility(View.GONE);
            holder.binding.rightChatView.setVisibility(View.VISIBLE);
            holder.binding.rightChatTextView.setText(messageModel.getMessage());
        } else {
            holder.binding.rightChatView.setVisibility(View.GONE);
            holder.binding.leftChatView.setVisibility(View.VISIBLE);
            holder.binding.leftChatTextView.setText(messageModel.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        final ItemChatBinding binding;

        public MyViewHolder(@NonNull ItemChatBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
