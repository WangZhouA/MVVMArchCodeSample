package me.goldze.mvvmhabit.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import me.goldze.mvvmhabit.base.BaseViewModel.ParameterField;
import me.goldze.mvvmhabit.bus.Messenger;


/**
 * 一个拥有DataBinding框架的基Activity
 * 这里根据项目业务可以换成你自己熟悉的BaseActivity, 但是需要继承RxAppCompatActivity,方便LifecycleProvider管理生命周期
 */
public abstract class BaseActivity<V extends ViewDataBinding, VM extends BaseViewModel> extends AppCompatActivity implements IView {
    protected V mBinding;
    protected VM mViewModel;
    private int mViewModelId;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //页面接受的参数方法
        initParam();
        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding(savedInstanceState);
        //私有的ViewModel与View的契约事件回调逻辑
        registerUIChangeLiveDataCallBack();
        //页面数据初始化方法
        initData();
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable();
        //注册RxBus
        mViewModel.registerRxBus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getLifecycle().removeObserver(mViewModel);
        //解除Messenger注册
        Messenger.getDefault().unregister(mViewModel);
        if (mViewModel != null) {
            mViewModel.removeRxBus();
        }
        if(mBinding != null){
            mBinding.unbind();
        }
    }

    /**
     * 注入绑定
     */
    private void initViewDataBinding(Bundle savedInstanceState) {
        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包
        mBinding = DataBindingUtil.setContentView(this, initContentView());
        mViewModelId = initVariableId();
        mViewModel = initViewModel(this);
        //关联ViewModel
        mBinding.setVariable(mViewModelId, mViewModel);
        //支持LiveData绑定xml，数据改变，UI自动会更新
        mBinding.setLifecycleOwner(this);
        //让ViewModel拥有View的生命周期感应
        getLifecycle().addObserver(mViewModel);
    }

    //刷新布局
    public void refreshLayout() {
        if (mViewModel != null) {
            mBinding.setVariable(mViewModelId, mViewModel);
        }
    }

    /**
     * =====================================================================
     **/
    //注册ViewModel与View的契约UI回调事件
    protected void registerUIChangeLiveDataCallBack() {
        //加载对话框显示
        mViewModel.getUC().getShowDialogEvent().observe(this, this::showDialog);
        //加载对话框消失
        mViewModel.getUC().getDismissDialogEvent().observe(this, v -> dismissDialog());
        //跳入新页面
        mViewModel.getUC().getStartActivityEvent().observe(this, params -> {
            Class<?> clz = (Class<?>) params.get(ParameterField.CLASS);
            Bundle bundle = (Bundle) params.get(ParameterField.BUNDLE);
            startActivity(clz, bundle);
        });
        //跳入ContainerActivity
        mViewModel.getUC().getStartContainerActivityEvent().observe(this, params -> {
            String canonicalName = (String) params.get(ParameterField.CANONICAL_NAME);
            Bundle bundle = (Bundle) params.get(ParameterField.BUNDLE);
            startContainerActivity(canonicalName, bundle);
        });
        //关闭界面
        mViewModel.getUC().getFinishEvent().observe(this, v -> finish());
        //关闭上一层
        mViewModel.getUC().getOnBackPressedEvent().observe(this, v -> onBackPressed());
    }

    public void showDialog(String title) {
        if (mDialog == null) {
            mDialog = new ProgressDialog(this);
        }
        mDialog.setTitle(title);
        mDialog.show();
    }

    public void dismissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    public void startActivity(Class<?> clz) {
        startActivity(new Intent(this, clz));
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 跳转容器页面
     *
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     */
    public void startContainerActivity(String canonicalName) {
        startContainerActivity(canonicalName, null);
    }

    /**
     * 跳转容器页面
     *
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     * @param bundle        跳转所携带的信息
     */
    public void startContainerActivity(String canonicalName, Bundle bundle) {
        Intent intent = new Intent(this, ContainerActivity.class);
        intent.putExtra(ContainerActivity.FRAGMENT, canonicalName);
        if (bundle != null) {
            intent.putExtra(ContainerActivity.BUNDLE, bundle);
        }
        startActivity(intent);
    }
}
