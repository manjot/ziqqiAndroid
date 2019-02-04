package com.ziqqi.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.CircularProgressDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ziqqi.R;
import com.ziqqi.model.bannerimagemodel.Payload;

import java.util.List;

public class ImageSliderAdapter extends PagerAdapter {
    private Context context;
    LayoutInflater inflater;
    List<Payload> imageModelList;
    CircularProgressDrawable drawable;

    public ImageSliderAdapter(Context context, List<Payload> imageModelList) {
        this.imageModelList = imageModelList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        drawable = new CircularProgressDrawable(context);
        drawable.start();
    }

    @Override
    public int getCount() {
        return imageModelList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View myImageLayout = inflater.inflate(R.layout.item_view_pager, container, false);
        ImageView myImage = myImageLayout.findViewById(R.id.iv_pager);
        Glide.with(container).load(imageModelList.get(position).getImagePath()).apply(RequestOptions.placeholderOf(drawable)).into(myImage);
        container.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
