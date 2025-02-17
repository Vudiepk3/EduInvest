package com.example.eduinvest.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.eduinvest.loanactivities.FirstLoanFragment;
import com.example.eduinvest.loanactivities.SecondLoanFragment;

public class ViewPageBankAdapter extends FragmentStateAdapter {

    public ViewPageBankAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new FirstLoanFragment(); // Fragment cho tab FirstLoan
        }
        return new SecondLoanFragment(); // Fragment mặc định
    }

    @Override
    public int getItemCount() {
        return 2; // Số lượng tab
    }
}
