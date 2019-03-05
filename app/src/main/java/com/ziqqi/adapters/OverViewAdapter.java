package com.ziqqi.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ziqqi.R;

public class OverViewAdapter extends PagerAdapter {
  //  private int[] image_resource={ R.drawable.ip3,R.drawable.ip4};
    private String [] heading={"h1","h2","h3"};
    private String [] des={"hjfsdhfsd","dvSdsvbvb","cbZzvzvz"};
    private String [] tit={"OVERVIEW","SPECIFICATION","REVIEW"};
    private Context ctx;
    private LayoutInflater layoutInflater;
    public OverViewAdapter(Context ctx){
        this.ctx=ctx;
    }

    @Override
    public int getCount() {
        return heading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view==(LinearLayout)o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=(LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view=layoutInflater.inflate(R.layout.overviewlayout,container,false);
        TextView t1=item_view.findViewById(R.id.tvheading);
        TextView t2=item_view.findViewById(R.id.tvdes);
        t1.setText(heading[position]);
        t2.setText(des[position]);
        container.addView(item_view);

        return item_view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }



    public View getTabView(int position) {
        View view = LayoutInflater.from(this.ctx).inflate(R.layout.tab_layout, null);
        TextView title = (TextView) view.findViewById(R.id.tvover);

        title.setText(tit[position]);
        title.setTextColor(000000);

        return view;
    }
}
