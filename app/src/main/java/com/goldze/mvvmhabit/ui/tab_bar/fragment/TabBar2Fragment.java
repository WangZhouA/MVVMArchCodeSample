package com.goldze.mvvmhabit.ui.tab_bar.fragment;

import com.goldze.mvvmhabit.BR;
import com.goldze.mvvmhabit.R;

import me.goldze.mvvmhabit.base.BaseFragment;

/**
 * Created by goldze on 2018/7/18.
 */

public class TabBar2Fragment extends BaseFragment {
    @Override
    public int initContentView() {
        return R.layout.fragment_tab_bar_2;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

}
