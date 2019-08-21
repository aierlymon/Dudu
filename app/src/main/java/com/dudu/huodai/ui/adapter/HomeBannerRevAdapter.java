package com.dudu.huodai.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dudu.huodai.R;

public class HomeBannerRevAdapter extends RecyclerView.Adapter<HomeBannerRevAdapter.BannerItemHold> implements View.OnClickListener {

    private String[] iconNames;

    private int[] icons;

    private Context mContext;

    public HomeBannerRevAdapter( Context context,String[] iconNames, int[] icons) {
        this.iconNames = iconNames;
        this.icons = icons;
        this.mContext = context;
    }

    @Override
    public void onClick(View view) {

    }

    @NonNull
    @Override
    public BannerItemHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BannerItemHold(LayoutInflater.from(mContext)
                .inflate(R.layout.home_banner_item, parent, false), this);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerItemHold holder, int position) {
        holder.iconName.setText(iconNames[position]);
        RequestOptions options = new RequestOptions();
        int size = (int) mContext.getResources().getDimension(R.dimen.x37);
        options.override(size, size); //设置加载的图片大小
        Glide.with(mContext).load(icons[position]).apply(options).into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return iconNames.length;
    }

    class BannerItemHold extends RecyclerView.ViewHolder {

        private ImageView icon;
        private TextView iconName;
        public BannerItemHold(@NonNull View itemView, View.OnClickListener listener) {
            super(itemView);
            itemView.setOnClickListener(listener);
            iconName=  itemView.findViewById(R.id.tx_iconName);
            icon=itemView.findViewById(R.id.image_icon);
        }


    }
}
