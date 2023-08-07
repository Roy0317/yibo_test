package com.yibo.yiboapp.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.entify.PlayItem;
import com.yibo.yiboapp.entify.SubPlayItem;

import java.util.List;

/**
 * Created by johnson on 2017/12/14.
 */

public class PlayRuleSimpleExpandAdapter extends BaseExpandableListAdapter {

    Context context;
    private List<PlayItem> playItems;
//    private  ExpandableListView playExpandListView;

    public PlayRuleSimpleExpandAdapter(Context context, List<PlayItem> plays /*,ExpandableListView playExpandListView*/) {
        this.context = context;
        this.playItems = plays;
    }

    @Override
    public int getGroupCount() {
        return playItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int size = playItems.get(groupPosition).getRules().size();
        return size <= 1 ? 0 : size;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return playItems.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return playItems.get(groupPosition).getRules().get(childPosition);
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
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_simple_expand_group, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            groupViewHolder.tvIndictor = (ImageView) convertView.findViewById(R.id.tv_indictor);
            groupViewHolder.item = (LinearLayout) convertView.findViewById(R.id.item);
            groupViewHolder.iv_select_state = (ImageView) convertView.findViewById(R.id.iv_select_state);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }


        groupViewHolder.tvTitle.setText(playItems.get(groupPosition).getName());
        groupViewHolder.tvTitle.setActivated(playItems.get(groupPosition).isActivated());

        if (playItems.get(groupPosition).isActivated()) {
            groupViewHolder.iv_select_state.setImageResource(R.drawable.icon_play_rules_checked);
        } else {
            groupViewHolder.iv_select_state.setImageResource(R.drawable.icon_play_rules_uncheck);
        }


        if (playItems.get(groupPosition).getRules().size() <= 1) {
            groupViewHolder.tvIndictor.setVisibility(View.INVISIBLE);
            groupViewHolder.iv_select_state.setVisibility(View.VISIBLE);
//            Drawable drawableLeft = context.getResources().getDrawable(
//                    R.drawable.selecter_item_simple_expand_bg);
//            groupViewHolder.tvTitle.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
//            groupViewHolder.tvTitle.setCompoundDrawablePadding(5);
//            Drawable drawable = context.getResources().getDrawable(R.drawable.selecter_item_simple_expand_bg);
//            drawable.setBounds(0, 0, 25, 25);
//            groupViewHolder.tvTitle.setCompoundDrawables(drawable, null, null, null);
//            groupViewHolder.tvTitle.setCompoundDrawablePadding(5);

        } else {
            groupViewHolder.tvIndictor.setVisibility(View.VISIBLE);
            groupViewHolder.iv_select_state.setVisibility(View.INVISIBLE);
//            groupViewHolder.tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_simple_expand_child, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            childViewHolder.item = (LinearLayout) convertView.findViewById(R.id.item);
            childViewHolder.iv_select_state = (ImageView) convertView.findViewById(R.id.iv_select_state);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        int size = playItems.get(groupPosition).getRules().size();
        if (size <= 0) {
            return null;
        }
//        Drawable drawable = context.getResources().getDrawable(R.drawable.selecter_item_simple_expand_bg);
//        drawable.setBounds(0, 0, 25, 25);
//        childViewHolder.tvTitle.setCompoundDrawables(drawable, null, null, null);
//        childViewHolder.tvTitle.setCompoundDrawablePadding(5);

        SubPlayItem subPlayItem = playItems.get(groupPosition).getRules().get(childPosition);

        if (subPlayItem.isActivated()) {
            childViewHolder.iv_select_state.setImageResource(R.drawable.icon_play_rules_checked);
        } else {
            childViewHolder.iv_select_state.setImageResource(R.drawable.icon_play_rules_uncheck);
        }

        childViewHolder.tvTitle.setActivated(playItems.get(groupPosition).getRules().get(childPosition).isActivated());
        childViewHolder.tvTitle.setText(playItems.get(groupPosition).getRules().get(childPosition).getName());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    static class GroupViewHolder {
        TextView tvTitle;
        ImageView tvIndictor;
        LinearLayout item;
        ImageView iv_select_state;
    }

    static class ChildViewHolder {
        TextView tvTitle;
        LinearLayout item;
        ImageView iv_select_state;
    }



    public List<PlayItem> getData() {
        return playItems;
    }

    public void setData(List<PlayItem> playItems) {
        this.playItems = playItems;
        notifyDataSetChanged();
    }


}
