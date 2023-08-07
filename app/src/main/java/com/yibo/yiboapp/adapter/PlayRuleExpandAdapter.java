package com.yibo.yiboapp.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.nfc.Tag;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
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

public class PlayRuleExpandAdapter extends BaseExpandableListAdapter {

    Context context;
    List<PlayItem> playItems;

    public PlayRuleExpandAdapter(Context context, List<PlayItem> plays) {
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
        return size <= 1 ? 0:size;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_expand_group, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            groupViewHolder.tvIndictor = (ImageView) convertView.findViewById(R.id.tv_indictor);
            groupViewHolder.item = (LinearLayout) convertView.findViewById(R.id.item);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.tvTitle.setText(playItems.get(groupPosition).getName());
        if (playItems.get(groupPosition).getRules().size() <= 1) {
            groupViewHolder.tvIndictor.setVisibility(View.GONE);
        }else{
            groupViewHolder.tvIndictor.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_expand_child, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            childViewHolder.item = (LinearLayout) convertView.findViewById(R.id.item);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        int size = playItems.get(groupPosition).getRules().size();
        if (size <= 0) {
            return null;
        }
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
    }
    static class ChildViewHolder {
        TextView tvTitle;
        LinearLayout item;
    }

    public interface RuleSelectCallback{
        void onRuleCallback(PlayItem playItem,SubPlayItem item);
    }

}
