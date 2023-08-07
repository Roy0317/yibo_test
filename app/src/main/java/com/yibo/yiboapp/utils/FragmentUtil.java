package com.yibo.yiboapp.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import com.yibo.yiboapp.R;

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
 * Date  :08/08/2019
 * Desc  :Fragment管理工具类，方便添加、移除、恢复Fragment
 */
public class FragmentUtil {
    private static FragmentManager fragmentManager;

    /**
     * 使用之前要先调用init方法初始化FragmentManager
     *
     * @param activity
     */

    public static void init(AppCompatActivity activity) {
        fragmentManager = activity.getSupportFragmentManager();
    }

    /**
     * 如果在Fragment中管理其中的Fragment需要调用这个初始化方法
     *
     * @param fragment
     */
    public static void init(Fragment fragment) {
        fragmentManager = fragment.getChildFragmentManager();
    }

    public static void addFragment(Fragment fragment, boolean isAddBackStack, int containerId) {
        if (!fragment.isAdded()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit);
            transaction.add(containerId, fragment);
            if (isAddBackStack) {
                transaction.addToBackStack(null);
            }
            transaction.commit();
        }
    }

    public static void addAndHide(Fragment fragment, int containerId) {
        if (!fragment.isAdded()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit);
            transaction.add(containerId, fragment);
            transaction.hide(fragment);
            transaction.commit();
        }
    }

    public static void addAndShow(Fragment fragment, int containerId) {
        if (!fragment.isAdded()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit);
            transaction.add(containerId, fragment);
            transaction.commit();
        }
    }

    public static void addShowAndHide(Fragment addShow, Fragment hideFragment, int containerId) {
        if (!addShow.isAdded()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit);
            transaction.add(containerId, addShow);
            transaction.hide(hideFragment);
            transaction.commit();
        }
    }

    public static void hideFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(fragment);
        transaction.commit();
    }

    public static void showFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.show(fragment);
        transaction.commit();
    }

    public static void hideAndShow(Fragment hideFragment, Fragment showFragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit);
        transaction.hide(hideFragment);
        transaction.show(showFragment);
        transaction.commit();
    }

    public static void removeAndShow(Fragment fragment, Fragment showFragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit);
        transaction.remove(fragment);
        transaction.show(showFragment);
        transaction.commit();
    }

    /**
     * 添加fragment
     *
     * @param fragment       要被添加的fragment
     * @param isAddBackStack 是否需要添加到回退栈
     * @param containerId    fragment的容器ID
     */
    public static void replaceFragment(Fragment fragment, boolean isAddBackStack, int containerId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit);
        transaction.replace(containerId, fragment);
        if (isAddBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    /**
     * 将栈顶的fragment出栈
     */
    public static void popFragment() {
        fragmentManager.popBackStack();
    }

    /**
     * fragment出栈，直至指定tag的fragment
     *
     * @param tag
     */
    public static void popFragment(String tag) {
        fragmentManager.popBackStackImmediate(tag, 0);
    }
}
