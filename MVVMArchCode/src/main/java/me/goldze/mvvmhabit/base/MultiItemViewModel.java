package me.goldze.mvvmhabit.base;


import androidx.annotation.NonNull;

/**
 * Create Author：goldze
 * Create Date：2019/01/25
 * Description：RecycleView多布局ItemViewModel是封装
 */

public class MultiItemViewModel<VM extends BaseViewModel> extends ItemViewModel<VM> {
    private Object multiType;

    public Object getItemType() {
        return multiType;
    }

    public void multiItemType(@NonNull Object multiType) {
        this.multiType = multiType;
    }

    public MultiItemViewModel(@NonNull VM viewModel, int position) {
        super(viewModel, position);
    }

    public MultiItemViewModel(@NonNull VM viewModel) {
        super(viewModel);
    }
}
