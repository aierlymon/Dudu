package com.linglong.huodai.widget.jingewenku.abrahamcaijin.loopviewpagers.adapter;


import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 主要功能:
 *
 * @Prject: LoopViewPager
 * @Package: com.example.huodai.widget.jingewenku.abrahamcaijin.loopviewpagers.adapter
 * @author: Abraham
 * @date: 2019年04月20日 14:05
 * @Copyright: 个人版权所有
 * @Company:
 * @version: 1.0.0
 */

public class LoopFragmentPagerAdapter extends FragmentPagerAdapter {
    public List<Fragment> listFragment;

    public LoopFragmentPagerAdapter(FragmentManager fm, List<Fragment> listData) {
        super(fm);
        this.listFragment=listData;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        position = position % listFragment.size();
        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getItem(int i) {
        return this.listFragment.get(i);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

}
