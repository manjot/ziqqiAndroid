package com.ziqqi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ziqqi.R;

import java.util.HashMap;
import java.util.List;

public class ExpandableAdapter extends BaseExpandableListAdapter {
    Context context;
    List<String> headers;
    HashMap<String, List<String>> children;

    public ExpandableAdapter(Context context, List<String> headers, HashMap<String, List<String>> children) {
        this.context = context;
        this.headers = headers;
        this.children = children;
    }

    @Override
    public int getGroupCount() {
        return headers.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return children.get(headers.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return headers.get(i);

    }

    @Override
    public Object getChild(int i, int i1) {
        return this.children.get(this.headers.get(i))
                .get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String headerTitle = (String) getGroup(i);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.expandable_header_view, null);
        }

        TextView tvHeader = view.findViewById(R.id.tv_header);
        ImageView ivHeaderIcon = view.findViewById(R.id.iv_header_icon);
        tvHeader.setText(headerTitle);

        if (b) {
            ivHeaderIcon.setRotation(180);
        } else {
            ivHeaderIcon.setRotation(0);
        }

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final String childText = (String) getChild(i, i1);

        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.expandable_child_view, null);
        }

        TextView tvChild = (TextView) view.findViewById(R.id.tv_child);

        tvChild.setText(childText);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
