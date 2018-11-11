package com.gloiot.hygoSupply.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.List;

public class MyPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> list;
    FragmentManager fm;
    boolean[] fragmentsUpdateFlag;

    public MyPagerAdapter(FragmentManager fm, List<Fragment> list, boolean[] fragmentsUpdateFlag) {
        super(fm);
        this.list = list;
        this.fm = fm;
        this.fragmentsUpdateFlag = fragmentsUpdateFlag;

    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        String fragmentTag = fragment.getTag();
        if (fragmentsUpdateFlag[position % fragmentsUpdateFlag.length]) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.remove(fragment);
            fragment = list.get(position % list.size());
            ft.add(container.getId(), fragment, fragmentTag);
            ft.attach(fragment);
            ft.commit();
            fragmentsUpdateFlag[position % fragmentsUpdateFlag.length] = false;
        }
        return fragment;
    }

}
