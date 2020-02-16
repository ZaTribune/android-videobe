package com.shadow.videobe.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raid_2209ee on 14/02/2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment>fragmentList=new ArrayList<>();
    private final List<String>fragmentTitlesList=new ArrayList<>();


    public ViewPagerAdapter(FragmentManager manager){
        super(manager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);//todo:learn behaviors of this adapter from docs
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitlesList.get(position);
    }

    public  void addFragment(Fragment fragment, String title){
        fragmentList.add(fragment);
        fragmentTitlesList.add(title);

    }

}
