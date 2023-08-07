package com.yibo.yiboapp.views;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.views.loadmore.ListLoadMoreAction;
import com.yibo.yiboapp.views.loadmore.OnLoadMoreListener;


/**
 * 可刷新加载更多
 */
public class SwipeRefreshLayoutExtend extends SwipeRefreshLayout {

    private ListLoadMoreAction loadMoreAction;

    /**
     * 可滑动的view
     */
    private View mContentView;
    /**
     * 上拉监听器, 到了最底部的上拉加载操作
     */
    private onRefrshListener mRefreshListener;

    /**
     * 是否需要下拉刷新
     */
    private boolean isNeedRefersh = false;
    /**
     * 是否需要上拉加载更多
     */
    private boolean isNeedLoadMore = false;
    /**
     * 是否正在刷新
     */
    private boolean isRefershNow = false;
    /**
     * 是否正在加载更多
     */
    private boolean isLoadMoreNow = false;


    private int pageSize = 20;  //默认自动加载数量

    public SwipeRefreshLayoutExtend(@NonNull Context context) {
        super(context);
        init();
    }

    public SwipeRefreshLayoutExtend(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        loadMoreAction = new ListLoadMoreAction();
        loadMoreAction.setPageSize(pageSize);
    }


    /**
     * 设置每一页请求的数据条数，以便设置自动加载的条件
     *
     * @param pageSize
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        loadMoreAction.setPageSize(pageSize);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        getListView();
        setNeedPullDownRefresh(false);
        if (mContentView == null)
            throw new IllegalStateException("cant get listview or Listview should be extend ");
    }


    /**
     * 获取ListView对象
     */
    private void getListView() {
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            if (childView instanceof ListView
                    || childView instanceof RecyclerView
                    || childView instanceof GridView) {
                mContentView = childView;
                break;
            }
        }
    }


    /**
     * 需要下拉刷新
     *
     * @param needPullDownRefresh
     */
    public void setNeedPullDownRefresh(boolean needPullDownRefresh) {
        isNeedRefersh = needPullDownRefresh;
        setEnabled(isNeedRefersh);
        if (isNeedRefersh) {
            setColorSchemeResources(R.color.color1, R.color.color2, R.color.color3, R.color.color4);
            setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (!isLoadMoreNow) {  //正在加载更多，就不进行刷新
                        isRefershNow = true;
                        if (isNeedLoadMore)
                            loadMoreAction.resetLastItemCount();

                        if (mRefreshListener != null)
                            mRefreshListener.onPullDownRefresh();
                    } else {
                        setRefreshing(false);
                    }
                }
            });
        } else {
            setOnRefreshListener(null);
        }
    }


    /**
     * 是否需要上拉加载更多
     *
     * @param needPullUpLoadMore
     */
    public void setNeedPullUpLoadMore(boolean needPullUpLoadMore) {
        isNeedLoadMore = needPullUpLoadMore;
        if (isNeedLoadMore) {
            loadMoreAction.attachToListFor(mContentView, new OnLoadMoreListener() {
                @Override
                public void loadMore(boolean actionFromClick) {
                    if (isRefershNow) {  //正在刷新不进行加载更多
                        loadMoreAction.onloadMoreComplete();
                    } else {
                        if (!actionFromClick && mRefreshListener != null) {
                            isLoadMoreNow = true;
                            mRefreshListener.onPullupLoadMore();
                        }
                    }
                }
            });
        }
    }


    public void setmRefreshListener(onRefrshListener mRefreshListener) {
        setNeedPullDownRefresh(true);
        setNeedPullUpLoadMore(true);
        this.mRefreshListener = mRefreshListener;
    }


    /**
     * 加载或者刷新完成
     */
    public void onRefreshComplete() {
        if (isNeedRefersh) {
            isRefershNow = false;
            setRefreshing(false);
        }

        if (isNeedLoadMore) {
            isLoadMoreNow = false;
            loadMoreAction.onloadMoreComplete();
        }
    }


    /**
     * 请求数据失败
     */
    public void onErrorHappen() {
        loadMoreAction.onloadErrorHappen();
    }


    /**
     * 加载更多的监听器
     *
     * @author mrsimple
     */
    public interface onRefrshListener {

        /**
         * 下拉刷新
         */
        void onPullDownRefresh();

        /**
         * 上拉自动加载
         */
        void onPullupLoadMore();

    }


}
