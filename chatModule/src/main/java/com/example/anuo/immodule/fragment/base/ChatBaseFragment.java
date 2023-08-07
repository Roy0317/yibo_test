package com.example.anuo.immodule.fragment.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.View;


/**
 * ┌───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┐
 * │Esc│ │ F1│ F2│ F3│ F4│ │ F5│ F6│ F7│ F8│ │ F9│F10│F11│F12│ │P/S│S L│P/B│ ┌┐    ┌┐    ┌┐
 * └───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┘ └┘    └┘    └┘
 * ┌──┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───────┐┌───┬───┬───┐┌───┬───┬───┬───┐
 * │~`│! 1│@ 2│# 3│$ 4│% 5│^ 6│& 7│* 8│( 9│) 0│_ -│+ =│ BacSp ││Ins│Hom│PUp││N L│ / │ * │ - │
 * ├──┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─────┤├───┼───┼───┤├───┼───┼───┼───┤
 * │Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │{ [│} ]│ | \ ││Del│End│PDn││ 7 │ 8 │ 9 │   │
 * ├────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤└───┴───┴───┘├───┼───┼───┤ + │
 * │Caps │ A │ S │ D │ F │ G │ H │ J │ K │ L │: ;│" '│ Enter  │             │ 4 │ 5 │ 6 │   │
 * ├─────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────────┤    ┌───┐    ├───┼───┼───┼───┤
 * │Shift  │ Z │ X │ C │ V │ B │ N │ M │< ,│> .│? /│  Shift   │    │ ↑ │    │ 1 │ 2 │ 3 │   │
 * ├────┬──┴─┬─┴──┬┴───┴───┴───┴───┴───┴──┬┴───┼───┴┬────┬────┤┌───┼───┼───┐├───┴───┼───┤ E││
 * │Ctrl│Ray │Alt │         Space         │ Alt│code│fuck│Ctrl││ ← │ ↓ │ → ││   0   │ . │←─┘│
 * └────┴────┴────┴───────────────────────┴────┴────┴────┴────┘└───┴───┴───┘└───────┴───┴───┘
 * <p>
 * Author:anuo
 * Date  :02/08/2019
 * Desc  :fragment的基类
 * 解决了fragment的重影问题
 */
public class ChatBaseFragment extends Fragment implements View.OnClickListener {

    private int backCount;

    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";

    protected Activity act;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.act = (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        FragmentTransaction transaction;
//        if (isChildFragment()) {
//            transaction = getChildFragmentManager().beginTransaction();
//        } else {
//            transaction = getFragmentManager().beginTransaction();
//        }
        // 如果savedInstanceState不为空，那么可以获取在onSaveInstanceState方法中保存的值。
//        if (savedInstanceState != null) {
//            boolean isHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);// 获取保存fragment前的可视状态
//
//            if (isHidden) {
//                // 如果原来fragment是隐藏状态，那么就hide
//                transaction.hide(this);
//            } else {
//                // 如果原来fragment是显示状态，那么就show
//                transaction.show(this);
//            }
//        }
//        transaction.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit);
//        if (isAddToBackStack()) {
//            transaction.addToBackStack(this.getTag());
//        }
//        transaction.commit();
    }

    /**
     * 是否为fragment中嵌套的fragment
     *
     * @return 默认不是，如果是嵌套fragment中的需要重写该方法返回true
     */

    protected boolean isChildFragment() {
        return false;
    }

    /**
     * 是否添加fragment到返回栈
     *
     * @return 默认不添加，需要添加的在子类重写该方法
     */
    protected boolean isAddToBackStack() {
        return false;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView(view);
        initListener();
        loadData();
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onClick(View v) {

    }

    /**
     * 初始化控件
     */
    protected void initView(View view) {

    }

    /**
     * 初始化监听事件
     */
    protected void initListener() {

    }


    /**
     * 加载数据方法
     */
    protected void loadData() {

    }

    /**
     * 调用该方法用来保存fragment当前的动态状态。
     * 可以用bundle对象保存一些值，然后可以在onCreate方法中获取保存的值。
     *
     * @param outState Bundle对象
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());// 保存当前fragment的可视状态
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

