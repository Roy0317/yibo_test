package com.yibo.yiboapp.data;

import java.util.List;

/**
 * Created by zhangy on 2016/10/12.
 * 用于处理业务中列表数据的去重及排序工作
 */
public interface ListResultListener<E> {
    /**
     * do sort and replace in order to supple list result.
     * @param originals originals data list. maybe just empty list, which is a pointer to a memory somewhere
     * @param results this time list result
     * @param url this time request url.
     * @return this sort list which include all list data.
     */
    List<E> sortList(List<E> originals, List<E> results, String url);
}
