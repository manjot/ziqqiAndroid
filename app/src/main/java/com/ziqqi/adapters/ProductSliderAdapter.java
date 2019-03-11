package com.ziqqi.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ziqqi.R;
import com.ziqqi.model.productdetailsmodel.Payload;

import java.util.List;

public class ProductSliderAdapter extends PagerAdapter {
    private Context context;
    LayoutInflater inflater;
    List<String> imageModelList;

    public ProductSliderAdapter(Context context, List<String> imageModelList) {
        this.imageModelList = imageModelList;
        this.context = context;
        inflater = LayoutInflater.from(context);
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
        View myImageLayout = inflater.inflate(R.layout.item_product_slider, container, false);
        ImageView myImage = myImageLayout.findViewById(R.id.iv_pager);
        Glide.with(container).load(imageModelList.get(position)).apply(RequestOptions.placeholderOf(R.drawable.place_holder)).into(myImage);
        container.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
