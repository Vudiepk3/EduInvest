package com.example.eduinvest.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.eduinvest.R;
import com.example.eduinvest.activity.OnboardingActivity;
import com.example.eduinvest.adapters.EditProfileAdapter;
import com.example.eduinvest.databinding.FragmentUserBinding;
import com.example.eduinvest.firebase.FireBaseClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;


public class UserFragment extends Fragment {

    private FragmentUserBinding binding;
    private FirebaseAuth auth;

    public UserFragment() {

    }

    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        loadUserInfo();
        setUpClick();
    }

    private void loadUserInfo() {
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            boolean isGoogleUser = false;

            for (UserInfo profile : currentUser.getProviderData()) {
                if ("google.com".equals(profile.getProviderId())) {
                    isGoogleUser = true;
                    break;
                }
            }

            if (isGoogleUser) {
                // Lấy thông tin từ FirebaseAuth (đăng nhập Gmail)
                String name = currentUser.getDisplayName();
                String email = currentUser.getEmail();
                Uri photoUrl = currentUser.getPhotoUrl();

                binding.tvUserName.setText(name != null ? name : "N/A");
                binding.tvUserEmail.setText(email != null ? email : "N/A");

                Glide.with(this)
                        .load(photoUrl)
                        .placeholder(R.drawable.img_user)
                        .error(R.drawable.img_user)
                        .into(binding.iconImage);
            } else {
                // Nếu không phải Gmail -> lấy từ FireBaseClass
                FireBaseClass.getUserInfo(userInfo -> {
                    if (userInfo != null) {
                        binding.tvUserName.setText(userInfo.getName());
                        binding.tvUserEmail.setText(currentUser.getEmail());

                        Glide.with(this)
                                .load(userInfo.getImage())
                                .placeholder(R.drawable.img_user)
                                .error(R.drawable.img_user)
                                .into(binding.iconImage);
                    } else {
                        binding.tvUserName.setText("N/A");
                        binding.tvUserEmail.setText("N/A");
                    }
                });
            }
        }
    }

    private void setUpClick() {
        binding.cardImage.setOnClickListener(v -> {
            FirebaseUser currentUser = auth.getCurrentUser();
            boolean isGoogleUser = false;

            if (currentUser != null) {
                for (UserInfo profile : currentUser.getProviderData()) {
                    if ("google.com".equals(profile.getProviderId())) {
                        isGoogleUser = true;
                        break;
                    }
                }
            }

            if (isGoogleUser) {
                // Nếu là người dùng Gmail → không cho sửa hồ sơ
                Toast.makeText(requireContext(), "Tài khoản Google không thể chỉnh sửa thông tin", Toast.LENGTH_SHORT).show();
            } else {
                // Nếu là người dùng thường → cho phép chỉnh sửa
                EditProfileAdapter bottomSheetFragment = new EditProfileAdapter(requireContext());
                bottomSheetFragment.show(requireActivity().getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });

        binding.cvSignOut.setOnClickListener(v -> {
            FirebaseUser currentUser = auth.getCurrentUser();
            if (currentUser != null) {
                auth.signOut();
                Intent intent = new Intent(requireActivity(), OnboardingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                requireActivity().finish();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        loadUserInfo();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
