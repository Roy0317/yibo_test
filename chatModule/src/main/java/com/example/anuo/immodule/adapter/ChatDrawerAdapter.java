package com.example.anuo.immodule.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.bean.ChatUserListBean;
import com.example.anuo.immodule.bean.GroupBean;
import com.example.anuo.immodule.constant.EventCons;
import com.example.anuo.immodule.event.CommonEvent;
import com.example.anuo.immodule.utils.CommonUtils;
import com.example.anuo.immodule.utils.GlideUtils;
import com.example.anuo.immodule.view.CircleImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


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
 * Date  :05/12/2019
 * Desc  :com.example.anuo.immodule.adapter
 */
public class ChatDrawerAdapter extends BaseExpandableListAdapter {
    private Context context;
    private boolean isFrontManager;//是不是前台管理员
    private boolean hasPrivatePermission;

    public List<GroupBean> getGroupBeans() {
        return groupBeans;
    }

    private List<GroupBean> groupBeans;
    public static final int CHILD_NORMAL = 0;
    public static final int CHILD_FOOT = 1;

    public ChatDrawerAdapter(Context context, List<GroupBean> groupBeans, boolean isFrontManager, boolean hasPrivatePermission) {
        this.context = context;
        this.groupBeans = groupBeans;
        this.isFrontManager = isFrontManager;
        this.hasPrivatePermission = hasPrivatePermission;
    }

    @Override
    public int getGroupCount() {
        return groupBeans == null ? 0 : groupBeans.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groupBeans.get(groupPosition).getChildBeans() == null ? 0 : groupBeans.get(groupPosition).getChildBeans().size() + 1;
    }


    @Override
    public Object getGroup(int groupPosition) {
        return groupBeans.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groupBeans.get(groupPosition).getChildBeans().get(childPosition);
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        if (childPosition == groupBeans.get(groupPosition).getChildBeans().size())
            return CHILD_FOOT;
        return CHILD_NORMAL;
    }

    @Override
    public int getChildTypeCount() {
        return 2;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_drawer_partent_item, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.iv_icon = convertView.findViewById(R.id.group_icon);
            groupViewHolder.tv_name = convertView.findViewById(R.id.group_name);
            groupViewHolder.iv_arrow = convertView.findViewById(R.id.group_arrow);
            groupViewHolder.iv_tips = convertView.findViewById(R.id.iv_red_tips);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        GroupBean groupBean = groupBeans.get(groupPosition);
        //是否显示小红点
        int unReadCount = 0;
        if (groupBean != null && groupBean.getChildBeans() != null) {
            for (ChatUserListBean.ChatUserBean childBean : groupBean.getChildBeans()) {
                unReadCount += childBean.getIsRead();
            }
        }
        EventBus.getDefault().post(new CommonEvent(EventCons.UNREAD_MESSAGE, unReadCount));
        groupViewHolder.iv_tips.setText(unReadCount + "");
        groupViewHolder.iv_tips.setVisibility(unReadCount > 0 ? View.VISIBLE : View.GONE);
        groupViewHolder.iv_icon.setImageResource(groupBean.getIcon());
        groupViewHolder.tv_name.setText(groupBean.getName());
        if (isExpanded) {
            groupViewHolder.iv_arrow.setImageResource(R.drawable.arrow_up);
        } else {
            groupViewHolder.iv_arrow.setImageResource(R.drawable.arrow_down);
        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (isLastChild) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_drawer_child_item_foot, parent, false);
            if (groupBeans.get(groupPosition).getStatus() == 1) {
                ((TextView) convertView.findViewById(R.id.tv_loadMore)).setText("点击加载更多");
            } else {
                ((TextView) convertView.findViewById(R.id.tv_loadMore)).setText("显示所有用户");
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onLoadMoreClickListener != null) {
                        onLoadMoreClickListener.onLoadMoreClick(groupPosition, groupBeans.get(groupPosition).getStatus());
                    }
                }
            });
            if (groupPosition != 1) {
                convertView = new TextView(context);
                convertView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            }
            return convertView;
        }
        final ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_drawer_child_item, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.rl_search = convertView.findViewById(R.id.rl_search);
            childViewHolder.et_search = convertView.findViewById(R.id.et_search);
            childViewHolder.iv_search = convertView.findViewById(R.id.iv_search);
            childViewHolder.iv_child_icon = convertView.findViewById(R.id.child_icon);
            childViewHolder.tv_child_name = convertView.findViewById(R.id.child_name);
            childViewHolder.ll_item = convertView.findViewById(R.id.ll_item);
            childViewHolder.iv_chat = convertView.findViewById(R.id.iv_chat);
            childViewHolder.textRemark = convertView.findViewById(R.id.text_remark);
            childViewHolder.textLastRecord = convertView.findViewById(R.id.textLastRecord);
            childViewHolder.iv_child_red_tips = convertView.findViewById(R.id.iv_child_red_tips);
            convertView.setTag(childViewHolder);

        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        final ChatUserListBean.ChatUserBean chatUserBean = groupBeans.get(groupPosition).getChildBeans().get(childPosition);
        if (chatUserBean.getIsRead() > 0) {
            if (chatUserBean.getIsRead() >= 99) {
                childViewHolder.iv_child_red_tips.setText("99+");
            } else {
                childViewHolder.iv_child_red_tips.setText(String.valueOf(chatUserBean.getIsRead()));
            }
            childViewHolder.iv_child_red_tips.setVisibility(View.VISIBLE);
        } else {
            childViewHolder.iv_child_red_tips.setVisibility(View.GONE);
        }
        boolean isManagerList = groupBeans.get(groupPosition).getLayoutType() == 0;
        boolean isOnlineList = groupBeans.get(groupPosition).getLayoutType() == 1;
        boolean isPrivateList = groupBeans.get(groupPosition).getLayoutType() == 2;
        if (childPosition == 0 && isOnlineList) {
            childViewHolder.rl_search.setVisibility(View.VISIBLE);
            childViewHolder.iv_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = childViewHolder.et_search.getText().toString();
                    if (onSearchListener != null) {
                        onSearchListener.onSearch(s);
                    }
                    CommonUtils.hideSoftInput(context, childViewHolder.et_search);
                }
            });
        } else {
            childViewHolder.rl_search.setVisibility(View.GONE);
        }
        if (isPrivateList) {
            childViewHolder.textRemark.setVisibility(View.VISIBLE);
            childViewHolder.textRemark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onChatClickListener != null) {
                        onChatClickListener.onRemarkClick(chatUserBean);
                    }
                }
            });
            childViewHolder.textLastRecord.setVisibility(View.VISIBLE);
            childViewHolder.textLastRecord.setText(
                    TextUtils.isEmpty(chatUserBean.getLastRecord()) ? "" : chatUserBean.getLastRecord());

            childViewHolder.iv_chat.setVisibility(View.GONE);
            childViewHolder.ll_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onChatClickListener != null) {
                        onChatClickListener.onChatClick(chatUserBean, true);
                    }
                }
            });
        } else if (isManagerList) {
            childViewHolder.textRemark.setVisibility(View.GONE);
            childViewHolder.textLastRecord.setVisibility(View.GONE);
            //管理员列表
            //只可以私聊有权限的管理员
            if (chatUserBean.isPrivatePermission()) {
                childViewHolder.iv_chat.setVisibility(View.VISIBLE);
            } else {
                childViewHolder.iv_chat.setVisibility(View.GONE);
            }
            childViewHolder.ll_item.setOnClickListener(null);
            childViewHolder.iv_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onChatClickListener != null) {
                        onChatClickListener.onChatClick(chatUserBean, false);
                    }
                }
            });
        } else if (isOnlineList) {
            childViewHolder.textRemark.setVisibility(View.GONE);
            childViewHolder.textLastRecord.setVisibility(View.GONE);
            //在线用户列表
            //双方有任一方为管理员的话，有管理身份者可以私聊才能私聊
            //双方皆为普通会员的话，任一方有私聊权限即可私聊
            if(chatUserBean.getUserType() == 4){
                if(chatUserBean.isPrivatePermission()){
                    childViewHolder.iv_chat.setVisibility(View.VISIBLE);
                }else {
                    childViewHolder.iv_chat.setVisibility(View.GONE);
                }
            }else if(isFrontManager){
                if(hasPrivatePermission){
                    childViewHolder.iv_chat.setVisibility(View.VISIBLE);
                }else {
                    childViewHolder.iv_chat.setVisibility(View.GONE);
                }
            }else {
                if(hasPrivatePermission || chatUserBean.isPrivatePermission()){
                    childViewHolder.iv_chat.setVisibility(View.VISIBLE);
                }else {
                    childViewHolder.iv_chat.setVisibility(View.GONE);
                }
            }
            childViewHolder.ll_item.setOnClickListener(null);
            childViewHolder.iv_chat.setOnClickListener(v -> {
                if (onChatClickListener != null) {
                    onChatClickListener.onChatClick(chatUserBean, false);
                }
            });
        }

        String avatar;
        String nickName;
        String account;
        //私聊列表
        if (isPrivateList && chatUserBean.getFromUser() != null) {
            avatar = chatUserBean.getFromUser().getAvatar();
            if(!TextUtils.isEmpty(chatUserBean.getRemarks())){
                nickName = chatUserBean.getRemarks();
            }else {
                nickName = chatUserBean.getFromUser().getNickName();
            }
            account = chatUserBean.getFromUser().getAccount();
        } else {
            avatar = chatUserBean.getAvatar();
            nickName = chatUserBean.getNickName();
            account = chatUserBean.getAccount();
        }

        GlideUtils.loadHeaderPic(context, avatar, childViewHolder.iv_child_icon);
        childViewHolder.tv_child_name.setText(TextUtils.isEmpty(nickName) ? account : nickName);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupViewHolder {
        private ImageView iv_icon;
        private TextView tv_name;
        private ImageView iv_arrow;
        private TextView iv_tips;
    }

    class ChildViewHolder {
        private LinearLayout ll_item;
        private CircleImageView iv_child_icon;
        private TextView tv_child_name;
        private ImageView iv_chat;
        private TextView iv_child_red_tips;
        private EditText et_search;
        private RelativeLayout rl_search;
        private ImageView iv_search;
        private TextView textRemark;
        private TextView textLastRecord;
    }

    private OnChatClickListener onChatClickListener;

    /**
     * 私聊的点击监听
     *
     * @param onChatClickListener
     */
    public void setOnChatClickListener(OnChatClickListener onChatClickListener) {
        this.onChatClickListener = onChatClickListener;
    }

    public interface OnChatClickListener {
        void onChatClick(ChatUserListBean.ChatUserBean chatUserBean, boolean isPrivate);
        void onRemarkClick(ChatUserListBean.ChatUserBean chatUserBean);
    }

    private OnLoadMoreClickListener onLoadMoreClickListener;

    public void setOnLoadMoreClickListener(OnLoadMoreClickListener onLoadMoreClickListener) {
        this.onLoadMoreClickListener = onLoadMoreClickListener;
    }

    public interface OnLoadMoreClickListener {
        void onLoadMoreClick(int group, int status);
    }

    private OnSearchListener onSearchListener;

    public void setOnSearchListener(OnSearchListener onSearchListener) {
        this.onSearchListener = onSearchListener;
    }

    public interface OnSearchListener {
        void onSearch(String content);
    }
}
