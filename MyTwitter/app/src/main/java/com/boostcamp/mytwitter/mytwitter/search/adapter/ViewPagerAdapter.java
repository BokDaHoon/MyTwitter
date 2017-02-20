package com.boostcamp.mytwitter.mytwitter.search.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.boostcamp.mytwitter.mytwitter.R;
import com.boostcamp.mytwitter.mytwitter.search.DaumSearchFragment;
import com.boostcamp.mytwitter.mytwitter.search.NaverSearchFragment;
import com.boostcamp.mytwitter.mytwitter.search.RetweetSearchFragment;

/**
 * Created by DaHoon on 2017-02-17.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;
    private Fragment[] fragmentsList = {    new RetweetSearchFragment(),
                                            new NaverSearchFragment(),
                                            new DaumSearchFragment()    };

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    public Fragment getItem(int num) {
        return fragmentsList[num];
    }

    @Override
    public int getCount() {
        return fragmentsList.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        String tabMenu = "";

        switch (position) {
            case 0 :
                tabMenu = mContext.getResources().getString(R.string.tab_menu1);
                break;
            case 1 :
                tabMenu = mContext.getResources().getString(R.string.tab_menu2);
                break;
            case 2 :
                tabMenu = mContext.getResources().getString(R.string.tab_menu3);
                break;
            default :

        }
        return tabMenu;
    }

}
