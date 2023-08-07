package com.yibo.yiboapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.fragment.CaigouMallFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class CaigouMallActivity extends BaseActivity {

    public static void createIntent(Context context){
        Intent intent = new Intent(context, CaigouMallActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caigou_mall);
        initView();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText(R.string.goucai_hall);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.relativeContainer, new CaigouMallFragment())
                .commit();
    }
}