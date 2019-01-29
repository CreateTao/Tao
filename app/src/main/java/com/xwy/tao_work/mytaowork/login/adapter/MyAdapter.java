package com.xwy.tao_work.mytaowork.login.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.xwy.tao_work.mytaowork.login.fragment.EmailRegFragment;
import com.xwy.tao_work.mytaowork.login.fragment.PhoneRegFragment;

public class MyAdapter extends FragmentStatePagerAdapter {
    private String [] tab_text ;

    public MyAdapter(FragmentManager fm , String [] tab_text) {
        super(fm);
        this.tab_text = tab_text;
    }

    @Override
    public Fragment getItem(int i) {
        if(i == 0){
            return new PhoneRegFragment();
        }else{
            return new EmailRegFragment();
        }
    }

    @Override
    public int getCount() {
        return tab_text.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tab_text[position];
    }
}
