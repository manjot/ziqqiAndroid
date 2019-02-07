package com.ziqqi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ziqqi.R;

public class NavigationDrawerAdapter extends BaseAdapter {
    int[] navItems, navIcons;
    Context context;

    public NavigationDrawerAdapter(Context context, int[] navIcons, int[] navItems) {
        this.navIcons = navIcons;
        this.context = context;
        this.navItems = navItems;
    }

    @Override
    public int getCount() {
        return navItems.length;
    }

    @Override
    public Object getItem(int i) {
        return navItems[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).
                    inflate(R.layout.item_navigation_drawer, viewGroup, false);
        }

        TextView tvItem = view.findViewById(R.id.tv_item);
        ImageView ivIcon = view.findViewById(R.id.iv_icon);

        tvItem.setText(context.getResources().getString(navItems[i]));
        ivIcon.setImageResource(navIcons[i]);
        return view;
    }
}
