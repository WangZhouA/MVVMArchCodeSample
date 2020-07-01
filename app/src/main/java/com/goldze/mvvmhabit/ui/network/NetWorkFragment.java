package com.goldze.mvvmhabit.ui.network;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.goldze.mvvmhabit.BR;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.app.AppViewModelFactory;
import com.goldze.mvvmhabit.databinding.FragmentNetworkBinding;

import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;

/**
 * Created by goldze on 2017/7/17.
 * 网络请求列表界面
 */

public class NetWorkFragment extends BaseFragment<FragmentNetworkBinding, NetWorkViewModel> {
    @Override
    public void initParam() {
        super.initParam();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_network;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public NetWorkViewModel initViewModel(ViewModelStoreOwner owner) {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用NetWorkViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(NetWorkViewModel.class);
    }

    @Override
    public void initData() {
        //给RecyclerView添加Adpter，请使用自定义的Adapter继承BindingRecyclerViewAdapter，重写onBindBinding方法，里面有你要的Item对应的binding对象。
        // Adapter属于View层的东西, 不建议定义到ViewModel中绑定，以免内存泄漏
        mBinding.setAdapter(new BindingRecyclerViewAdapter());
        //请求网络数据
        mViewModel.requestNetWork();
    }

    @Override
    public void initViewObservable() {
        //监听下拉刷新完成
        mViewModel.uc.finishRefreshing.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                //结束刷新
                mBinding.twinklingRefreshLayout.finishRefreshing();
            }
        });
        //监听上拉加载完成
        mViewModel.uc.finishLoadmore.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                //结束刷新
                mBinding.twinklingRefreshLayout.finishLoadmore();
            }
        });
        //监听删除条目
        mViewModel.deleteItemLiveData.observe(this, new Observer<NetWorkItemViewModel>() {
            @Override
            public void onChanged(@Nullable final NetWorkItemViewModel netWorkItemViewModel) {
                int index = mViewModel.getItemPosition(netWorkItemViewModel);
                //删除选择对话框
                new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage("是否删除【" + netWorkItemViewModel.entity.get().getName() + "】？ position：" + index)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ToastUtils.showShort("取消");
                            }
                        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mViewModel.deleteItem(netWorkItemViewModel);
                    }
                }).show();
            }
        });
    }
}
