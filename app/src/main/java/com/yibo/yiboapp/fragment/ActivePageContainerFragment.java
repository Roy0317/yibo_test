package com.yibo.yiboapp.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.Event.ActiveDataEvent;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.activity.ActivePageActivity;
import com.yibo.yiboapp.data.CacheRepository;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.entify.ActivesResultWraper;
import com.yibo.yiboapp.fragment.fragment.BaseFragment;
import com.yibo.yiboapp.network.HttpCallBack;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.network.NetworkResult;
import com.yibo.yiboapp.utils.Utils;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ActivePageContainerFragment extends BaseFragment {

    private MagicIndicator magicIndicator;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private FragmentContainerHelper mFragmentContainerHelper = new FragmentContainerHelper();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.layout_active_page, container, false);
        magicIndicator = root.findViewById(R.id.magic_indicator3);

        ActivePageFragment fragment = ActivePageFragment.newInstance();
        FragmentManager supportFragmentManager = getFragmentManager();
        supportFragmentManager.beginTransaction()
                .add(R.id.fragment_content, fragment).show(fragment)
                .commit();
        updateAcquireActivesViewFromBackup();
        getRecords();

        return root;
    }

    private void getRecords() {
        HttpUtil.get(getActivity(), Urls.ACQUIRE_ACTIVES_URL_V2, null, true, new HttpCallBack() {
            @Override
            public void receive(NetworkResult result) {
                if (result.isSuccess()) {
                    List<ActivesResultWraper.ContentBean> list = new Gson().fromJson(result.getContent(),
                            new TypeToken<List<ActivesResultWraper.ContentBean>>() {
                            }.getType());
                    updateAcquireActivesView(list);
                } else {
                    EventBus.getDefault().postSticky(new ActiveDataEvent("initFailed", result.getContent(), 0));
                }
            }
        });
    }

    private void initMagicIndicator3(List<ActivesResultWraper.ContentBean.TypeListBean> typeList, String content) {
        magicIndicator.setBackgroundColor(Color.WHITE);
        int color = Color.parseColor("#bdac6e");
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return typeList == null ? 0 : typeList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(Color.GRAY);
                simplePagerTitleView.setSelectedColor(color);
                simplePagerTitleView.setText(typeList.get(index).getTypeName());
                simplePagerTitleView.setOnClickListener(v -> {
                    mFragmentContainerHelper.handlePageSelected(index);
                    EventBus.getDefault().post(new ActiveDataEvent("updateIndex", content, index));
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                linePagerIndicator.setColors(color);
                return linePagerIndicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        mFragmentContainerHelper.attachMagicIndicator(magicIndicator);
        magicIndicator.setNavigator(commonNavigator);
        LinearLayout titleContainer = commonNavigator.getTitleContainer();
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContainer.setDividerDrawable(new ColorDrawable() {
            @Override
            public int getIntrinsicWidth() {
                return UIUtil.dip2px(getActivity(), 20);
            }
        });
    }

    private void updateAcquireActivesView(List<ActivesResultWraper.ContentBean> list) {
        String contentString = new Gson().toJson(list, new TypeToken<List<ActivesResultWraper.ContentBean>>() {
        }.getType());
        if (list != null && list.size() > 0) {
            List<ActivesResultWraper.ContentBean.TypeListBean> typeList = list.get(0).getTypeList();
            if (typeList != null && typeList.size() != 0) {
                ActivesResultWraper.ContentBean.TypeListBean type = new ActivesResultWraper.ContentBean.TypeListBean();
                type.setId(0);
                type.setStationId(0);
                type.setTypeName("全部优惠");
                if (!typeList.contains(type)) {
                    typeList.add(0, type);
                }
                initMagicIndicator3(list.get(0).getTypeList(), contentString);
                mFragmentContainerHelper.handlePageSelected(0, false);
                magicIndicator.setVisibility(View.VISIBLE);
            } else {
                magicIndicator.setVisibility(View.GONE);
            }
            EventBus.getDefault().postSticky(new ActiveDataEvent("initData", contentString, 0));
        } else {
            EventBus.getDefault().postSticky(new ActiveDataEvent("initFailed", contentString, 0));
        }
    }


    private void updateAcquireActivesViewFromBackup() {
        CacheRepository.getInstance().loadAcquireActivesData(getContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<ActivesResultWraper.ContentBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {compositeDisposable.add(d);}

                    @Override
                    public void onSuccess(List<ActivesResultWraper.ContentBean> data) {
                        if (data != null && !data.isEmpty()) {
                            updateAcquireActivesView(data);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
