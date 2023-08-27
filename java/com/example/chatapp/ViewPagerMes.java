package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerMes extends FragmentStateAdapter {
    private String[] title=new String[]{"CHATS","USER","PROFILE"};
    public ViewPagerMes(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new chatFragment();
            case 1:
                return new userFragment();

        }
        return new profileFragment();
    }

    @Override
    public int getItemCount() {
        return title.length;
    }
}
