package com.goldze.mvvmhabit.ui.base.fragment;

import androidx.fragment.app.Fragment;

import com.goldze.mvvmhabit.BR;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.databinding.FragmentBasePagerBinding;
import com.goldze.mvvmhabit.ui.base.adapter.BaseFragmentPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.base.BaseViewModel;

/**
 * Created by goldze on 2017/7/17.
 * 抽取的二级BasePagerFragment
 */

public abstract class BasePagerFragment extends BaseFragment<FragmentBasePagerBinding, BaseViewModel> {

    private List<Fragment> mFragments;
    private List<String> titlePager;

    protected abstract List<Fragment> pagerFragment();

    protected abstract List<String> pagerTitleString();

    @Override
    public int initContentView() {
        return R.layout.fragment_base_pager;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        mFragments = pagerFragment();
        titlePager = pagerTitleString();
        //设置Adapter
        BaseFragmentPagerAdapter pagerAdapter = new BaseFragmentPagerAdapter(getChildFragmentManager(), mFragments, titlePager);
        mBinding.viewPager.setAdapter(pagerAdapter);
        mBinding.tabs.setupWithViewPager(mBinding.viewPager);
        mBinding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mBinding.tabs));
    }

    @Override
    public void initViewObservable() {

    }
}
