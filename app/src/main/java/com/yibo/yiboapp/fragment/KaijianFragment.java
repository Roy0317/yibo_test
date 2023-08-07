package com.yibo.yiboapp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.adapter.NumbersAdapter;
import com.yibo.yiboapp.data.CacheRepository;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.BallListItemInfo;
import com.yibo.yiboapp.entify.KaijianEntify;
import com.yibo.yiboapp.entify.OpenResultWraper;
import com.yibo.yiboapp.ui.EmptyListView;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;
import com.yibo.yiboapp.ui.XListView;
import com.yibo.yiboapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import crazy_wrapper.Crazy.CrazyResponse;
import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A fragment representing a list of Items.
 * 开奖公告页面
 * <p/>
 *
 * @author johnson
 */
public class KaijianFragment extends Fragment implements SessionResponse.Listener<CrazyResult<OpenResultWraper>> {

    public static final String TAG = KaijianFragment.class.getSimpleName();
    private XListView listView;
    private EmptyListView empty;
    List<KaijianEntify> kaijianList;
    UsualMethod.ChannelListener channelListener;
    public static final int LOAD_OPEN_RESULTS_REQUEST = 0x01;
    ListViewAdapter adapter;
    //索引
    int start = 1;
    //每次加载的页码
    int pageSize = 20;

    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.kaijian_listview, container, false);
        return view;
    }

    private void actionAcquireData(boolean showDialog) {

        if (!Utils.isNetworkAvailable(getActivity())) {
            empty.setVisibility(View.VISIBLE);
            listView.setEmptyView(empty);
            return;
        }

        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.OPEN_RESULTS_URL);

        configUrl.append("?page=").append(start);
        configUrl.append("&rows=").append(pageSize);

        CrazyRequest<CrazyResult<OpenResultWraper>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(LOAD_OPEN_RESULTS_REQUEST)
                .headers(Urls.getHeader(getActivity()))
                .shouldCache(false)
                .listener(this)
                .placeholderText(getActivity().getString(R.string.loading))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<OpenResultWraper>() {
                }.getType()))
                .loadMethod(showDialog ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() :
                        CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(getActivity(), request);
//        if (!showDialog) {
//            ((MainActivity)getActivity()).startProgress();
//        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView(view);
        kaijianList = new ArrayList<>();
        adapter = new ListViewAdapter(getActivity(), kaijianList, R.layout.kaijian_list_item);
        listView.setAdapter(adapter);
        updateOpenResultViewFromBackup();
        actionAcquireData(true);
    }

    @SuppressLint("WrongViewCast")
    private void initView(View view) {
        listView = (XListView) view.findViewById(R.id.xlistview);
        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(true);
        listView.setDivider(getResources().getDrawable(R.color.driver_line_color));
        listView.setXListViewListener(new ListviewListener());
        listView.setDividerHeight(3);
        listView.setVisibility(View.VISIBLE);
        //不要过界的效果
        listView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        empty = (EmptyListView) view.findViewById(R.id.empty);
        empty.setListener(emptyListviewListener);
    }

    private void showToast(String showText) {
        if (Utils.isEmptyString(showText)) {
            return;
        }
        Toast.makeText(getActivity(), showText, Toast.LENGTH_SHORT).show();
    }

    private void showToast(int showText) {
        Toast.makeText(getActivity(), showText, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult<OpenResultWraper>> response) {
        RequestManager.getInstance().afterRequest(response);
//        ((MainActivity)getActivity()).stopProgress();
        if (getActivity() == null || getActivity().isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        if (action == LOAD_OPEN_RESULTS_REQUEST) {
            if (listView.isRefreshing()) {
                listView.stopRefresh();
            } else if (listView.isPullLoading()) {
                listView.stopLoadMore();
            }
            empty.setVisibility(View.VISIBLE);
            listView.setEmptyView(empty);
            CrazyResult<OpenResultWraper> result = response.result;
            if (result == null) {
                showToast(R.string.get_open_results_fail);
                return;
            }
            if (!result.crazySuccess) {
                showToast(R.string.get_open_results_fail);
                return;
            }
            Object regResult = result.result;
            OpenResultWraper reg = (OpenResultWraper) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                        getString(R.string.get_open_results_fail));
                return;
            }
            YiboPreference.instance(getActivity()).setToken(reg.getAccessToken());
            if (reg.getContent() != null) {
                kaijianList.clear();
                kaijianList.addAll(reg.getContent());
                adapter.notifyDataSetChanged();
//                listView.setPullLoadEnable(true);

                if (response.pickType != CrazyResponse.CACHE_REQUEST) {
                    start++;
                }
            }

        }
    }

    /**
     * 列表下拉，上拉监听器
     *
     * @author zhangy
     */
    private final class ListviewListener implements XListView.IXListViewListener {

        public void onRefresh() {
            start = 1;
            actionAcquireData(false);
        }

        public void onLoadMore() {
            actionAcquireData(false);
        }
    }

    EmptyListView.EmptyListviewListener emptyListviewListener = new EmptyListView.EmptyListviewListener() {
        @Override
        public void onEmptyListviewClick() {
            actionAcquireData(true);
        }
    };

    private final class ListViewAdapter extends LAdapter<KaijianEntify> {

        Context context;
        int screenWidth;
        public static final float FIXED_BALL_WIDTH = 25.0f;

        public ListViewAdapter(Context mContext, List<KaijianEntify> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
            context = mContext;
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm); // 获取手机屏幕的大小
            screenWidth = dm.widthPixels;
        }

        @Override
        public void convert(final int position, LViewHolder holder, ViewGroup parent, final KaijianEntify item) {
            RelativeLayout view = holder.getView(R.id.item);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (channelListener != null) {
                        channelListener.onKaiJianItemClick(item.getLotName(), item.getLotCode(), String.valueOf(item.getLotType()));
                    }
                }
            });
            ImageView header = holder.getView(R.id.img);
            if (!Utils.isEmptyString(item.getLotIcon())) {
                UsualMethod.updateLocImageWithUrl(context, header, item.getLotIcon().trim());
            } else {
                UsualMethod.updateLocImage(context, header, item.getLotCode());
            }
            TextView name = holder.getView(R.id.name);
            GridView numbersView = holder.getView(R.id.numbers);
            numbersView.setEnabled(false);
            numbersView.setPressed(false);
            numbersView.setClickable(false);
            TextView emptyNumbers = holder.getView(R.id.empty_numbers);
            TextView qiHao = holder.getView(R.id.qihao);

            if (!Utils.isEmptyString(item.getHaoma())) {
                if (item.getHaoma().contains(",")) {
                    emptyNumbers.setVisibility(View.GONE);
                    numbersView.setVisibility(View.VISIBLE);

                    //Todo 这里改了行数
//                    int column = (screenWidth - Utils.dip2px(context, 80) - 5) /
//                            Utils.dip2px(context, FIXED_BALL_WIDTH);
//                    Utils.LOG(TAG, "the figure out column == " + column);

                    int column = 10;

                    numbersView.setNumColumns(column);
                    numbersView.setAdapter(new NumbersAdapter(context, Utils.splitString(item.getHaoma(), ","),
                            R.layout.number_gridview_item, String.valueOf(item.getLotType()), item.getLotCode()));
                    Utils.setListViewHeightBasedOnChildren(numbersView, column, 10);
                } else {
                    emptyNumbers.setVisibility(View.VISIBLE);
                    numbersView.setVisibility(View.GONE);
                    emptyNumbers.setText(item.getHaoma());
                }
            } else {
                emptyNumbers.setVisibility(View.VISIBLE);
                numbersView.setVisibility(View.GONE);
                emptyNumbers.setText("等待开奖");
            }
            if (!Utils.isEmptyString(item.getQihao())) {
                String qihao = item.getQihao().length() <= 6 ? item.getQihao() : item.getQihao().substring(item.getQihao().length() - 6);
                qiHao.setText(String.format(getString(R.string.di_qihao_format), qihao));
            } else {
                qiHao.setText("暂无期数");
            }
            name.setText(!Utils.isEmptyString(item.getLotName()) ? item.getLotName() : "暂无名称");
        }

        private List<BallListItemInfo> convertNumbers(String numbers) {
            if (Utils.isEmptyString(numbers)) {
                return null;
            }
            List<String> nums = Utils.splitString(numbers, ",");
            List<BallListItemInfo> ballons = new ArrayList<BallListItemInfo>();
            for (String number : nums) {
                BallListItemInfo info = new BallListItemInfo();
                info.setNum(number);
                ballons.add(info);
            }
            return ballons;
        }
    }

    public void setChannelListener(UsualMethod.ChannelListener channelListener) {
        this.channelListener = channelListener;
    }

    /**
     * 检查有无缓存资料
     */
    private void updateOpenResultViewFromBackup() {
        CacheRepository.getInstance().loadOpenResultData(getActivity())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<KaijianEntify>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<KaijianEntify> data) {
                        Utils.LOG(TAG, "data != null && !data.isEmpty() ==> " + (data != null && !data.isEmpty()));
                        if (data != null && !data.isEmpty())
                            kaijianList.clear();
                        kaijianList.addAll(data);
                        adapter.notifyDataSetChanged();
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
        disposable.clear();
    }
}
