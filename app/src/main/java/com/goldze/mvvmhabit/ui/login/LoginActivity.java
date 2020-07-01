package com.goldze.mvvmhabit.ui.login;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.goldze.mvvmhabit.BR;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.app.AppViewModelFactory;
import com.goldze.mvvmhabit.databinding.ActivityLoginBinding;

import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * 一个MVVM模式的登陆界面
 */
public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> {
    //ActivityLoginBinding类是databinding框架自定生成的,对应activity_login.xml
    @Override
    public int initContentView() {
        return R.layout.activity_login;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public LoginViewModel initViewModel(ViewModelStoreOwner owner) {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return new ViewModelProvider(this, factory).get(LoginViewModel.class);
    }

    @Override
    public void initViewObservable() {
        //监听ViewModel中pSwitchObservable的变化, 当ViewModel中执行【uc.pSwitchObservable.set(!uc.pSwitchObservable.get());】时会回调该方法
        mViewModel.uc.pSwitchEvent.observe(this, aBoolean -> {
            //pSwitchObservable是boolean类型的观察者,所以可以直接使用它的值改变密码开关的图标
            if (mViewModel.uc.pSwitchEvent.getValue()) {
                //密码可见
                //在xml中定义id后,使用binding可以直接拿到这个view的引用,不再需要findViewById去找控件了
                mBinding.ivSwichPasswrod.setImageResource(R.mipmap.show_psw);
                mBinding.etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                //密码不可见
                mBinding.ivSwichPasswrod.setImageResource(R.mipmap.show_psw_press);
                mBinding.etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        mViewModel.stockLiveData.observe(this, s -> {
            Log.i("LoginActivity", "initViewObservable: "+s);
        });
    }
}
