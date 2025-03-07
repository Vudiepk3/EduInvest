    package com.example.eduinvest.adapters;


    import android.util.Log;

    import androidx.annotation.NonNull;
    import androidx.fragment.app.Fragment;
    import androidx.fragment.app.FragmentActivity;
    import androidx.fragment.app.FragmentManager;
    import androidx.lifecycle.Lifecycle;
    import androidx.viewpager2.adapter.FragmentStateAdapter;

    import com.example.eduinvest.fragments.HomeFragment;
    import com.example.eduinvest.fragments.MySelfFragment;
    import com.example.eduinvest.fragments.NewsFragment;
    import com.example.eduinvest.fragments.SupportFragment;

    public class ViewPagerFragmentAdapter extends FragmentStateAdapter {
        public ViewPagerFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Log.e("log_viewPager", "Position: " + position);
            if (position == 0) {
                return new HomeFragment(); // Fragment cho home
            } else if (position == 1) {
                return new NewsFragment();// Fragment cho tab Study
            } else if (position == 2) {
                return new SupportFragment(); // Fragment cho tab User
            } else if (position == 3) {
                return new MySelfFragment(); // Fragment cho tab Trò chuyện
            }else {
                return new HomeFragment(); // Fragment mặc định
            }
        }

        @Override
        public int getItemCount() {
            return 4; // Số lượng tab
        }
    }
