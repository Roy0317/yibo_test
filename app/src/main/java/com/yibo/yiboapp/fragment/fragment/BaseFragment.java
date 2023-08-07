package com.yibo.yiboapp.fragment.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.Toast;

import com.yibo.yiboapp.data.ListResultListener;
import com.yibo.yiboapp.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class BaseFragment extends Fragment {

    protected String TAG = this.getClass().getSimpleName();
    DefaultListResultHandler defaultListResultHandler;//列表数据处理基础类
    private Context context;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Utils.logAll(TAG, "onViewCreated()");
        context = view.getContext();
    }

    //统一的列表结果处理接口，子类可用
    public <T> List<T> handleListResult(List<T> originals, List<T> result, String url, boolean clearFirst) {
        if (defaultListResultHandler == null) {
            defaultListResultHandler = new DefaultListResultHandler();
        }
        if (clearFirst) {
            defaultListResultHandler.clear();
        }
        return defaultListResultHandler.sortList(originals, result, url);
    }

    public final class DefaultListResultHandler<T> implements ListResultListener<T> {

        Map<String, List<T>> map = null;

        public DefaultListResultHandler() {
            map = new HashMap<String, List<T>>();
        }

        @Override
        public List<T> sortList(List<T> originals, List<T> results, String url) {
            if (results == null || results.isEmpty()) {
                return originals;
            }
            if (map == null) {
                return originals;
            }
            map.put(url, results);
            originals.clear();//clear originals first for sake that we will loop all the results
            //and fill in it.
            /** sort each page results in sort map according to the url key. the only sort keyword
             *  can be compare is pageno column in url.
             */

            SortedMap<String, List<T>> n = new TreeMap<String, List<T>>(map);
            for (Map.Entry<String, List<T>> entry : n.entrySet()) {
                //zhang modify  20161103 to avoid last load results add to new result.
                //when switch list filter considence.
                //if (url.equals(entry.getKey())){
                originals.addAll(entry.getValue());
                //}
                //end modify
            }
            n.clear();
            return originals;
        }

        public void clear() {
            if (map != null) {
                map.clear();
            }
        }

    }

    protected void showToast(String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
}
