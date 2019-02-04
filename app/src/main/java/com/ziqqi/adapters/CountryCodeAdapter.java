package com.ziqqi.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ziqqi.R;
import com.ziqqi.utils.FontCache;

public class CountryCodeAdapter extends BaseAdapter {
    Context context;
    int[] flags;
    Typeface regular;
    String[] countryNames;
    LayoutInflater inflater;

    public CountryCodeAdapter(Context context, int[] flags, String[] countryNames) {
        this.context = context;
        this.flags = flags;
        this.countryNames = countryNames;
        inflater = (LayoutInflater.from(context));
        regular = FontCache.get(context.getResources().getString(R.string.regular), context);
    }

    @Override
    public int getCount() {
        return flags.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.item_country_code, null);
        ImageView icon = view.findViewById(R.id.iv_flag);
        TextView names = view.findViewById(R.id.tv_code);
        names.setTypeface(regular);
        icon.setImageResource(flags[i]);
        names.setText(countryNames[i]);
        return view;
    }
}
