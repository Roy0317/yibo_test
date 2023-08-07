package com.yibo.yiboapp.views;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.adapter.NoticeAdapter;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.entify.NoticeBean;
import com.yibo.yiboapp.entify.NoticeResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: soxin
 * @version: ${VERSION}
 * @project: trunk
 * @package: com.yibo.yiboapp.views
 * @description: 首页展示多条公告信息的弹窗
 * @date: 2018/12/28
 * @time: 下午4:31
 */
public class NoticeDialog extends Dialog {


    private Context context;
    private List<NoticeResult> content;
    private String title = "平台公告";

    public NoticeDialog setContent(List<NoticeResult> content) {
        this.content = content;
        return this;
    }

    public NoticeDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public NoticeDialog(@NonNull Context context) {
        super(context);
    }


    public NoticeDialog initView(Context context) {
        this.context = context;
        View view = View.inflate(context, R.layout.dialog_notice_message, null);
        setContentView(view);
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        view.findViewById(R.id.iv_close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ((TextView) view.findViewById(R.id.tv_notice_title)).setText(title);

        ExpandableListView expandableListView = (ExpandableListView) view.findViewById(R.id.expandable_list_view);
        expandableListView.setGroupIndicator(null);
        NoticeAdapter noticeAdapter = new NoticeAdapter();
        noticeAdapter.childCount = content == null ? 0 : content.size();
        List<NoticeBean> list = new ArrayList<>();

        for (NoticeResult noticeResult : content) {
            NoticeBean noticeBean = new NoticeBean();
            noticeBean.setTitle(noticeResult.getTitle());
            List<String> strings = new ArrayList<>();
            strings.add(noticeResult.getContent());
            noticeBean.setList(strings);
            list.add(noticeBean);
        }

        noticeAdapter.list = list;
        expandableListView.setAdapter(noticeAdapter);

        boolean isExpandFirstItem = UsualMethod.getConfigFromJson(context).getNotice_list_dialog_expand_first_notice().equalsIgnoreCase("on");
        if (isExpandFirstItem) {
            expandableListView.expandGroup(0);
        }
        return this;
    }


}
