package com.goldze.mvvmhabit.ui.form;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.Observable;
import androidx.lifecycle.Observer;

import com.goldze.mvvmhabit.BR;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.databinding.FragmentFormBinding;
import com.goldze.mvvmhabit.entity.FormEntity;

import java.util.Calendar;

import me.goldze.mvvmhabit.base.BaseFragment;

/**
 * Created by goldze on 2017/7/17.
 * 表单提交/编辑界面
 */

public class FormFragment extends BaseFragment<FragmentFormBinding, FormViewModel> {

    private FormEntity entity = new FormEntity();

    @Override
    public void initParam() {
        //获取列表传入的实体
        Bundle mBundle = getArguments();
        if (mBundle != null) {
            entity = mBundle.getParcelable("entity");
        }
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_form;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        //通过binding拿到toolbar控件, 设置给Activity
        ((AppCompatActivity) getActivity()).setSupportActionBar(mBinding.include.toolbar);
        //View层传参到ViewModel层
        mViewModel.setFormEntity(entity);
        //初始化标题
        mViewModel.initToolbar();
    }

    @Override
    public void initViewObservable() {
        //监听日期选择
        mViewModel.uc.showDateDialogObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mViewModel.setBir(year, month, dayOfMonth);
                    }
                }, year, month, day);
                datePickerDialog.setMessage("生日选择");
                datePickerDialog.show();
            }
        });
        mViewModel.entityJsonLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String submitJson) {
                new AlertDialog.Builder(getActivity()).setMessage("提交的json实体数据：\r\n" + submitJson).create().show();
            }
        });
    }
}
