package me.goldze.mvvmhabit.base;


import androidx.annotation.NonNull;

/**
 * ItemViewModel
 * Created by goldze on 2018/10/3.
 */

public class ItemViewModel<VM extends BaseViewModel> {
    protected VM mViewModel;
    // 有需要的话，可以设置当前实例对应的 position
    protected Integer mPosition;

    public ItemViewModel(@NonNull VM viewModel, int position) {
        this.mViewModel = viewModel;
        this.mPosition = position;
    }

    public ItemViewModel(@NonNull VM mViewModel) {
        this.mViewModel = mViewModel;
    }
}
