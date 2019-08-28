package com.dudu.huodai.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dudu.huodai.R;

import java.util.List;

public class LoadMenuRevAdapter extends RecyclerView.Adapter<LoadMenuRevAdapter.MenuItemHolder> implements View.OnClickListener {

    private List<String> iconList;
    private Context mContext;

    public LoadMenuRevAdapter(List<String> iconList, Context mContext) {
        this.iconList = iconList;
        this.mContext = mContext;
    }

    public OnItemClickListener getmOnItemClickListener() {
        return mOnItemClickListener;
    }

    private OnItemClickListener mOnItemClickListener;

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(view, (int) view.getTag());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public MenuItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MenuItemHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.fra_loan_recy_menu_item, parent, false), this);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemHolder holder, int position) {
    /*    RequestOptions options = new RequestOptions();
        int size = (int) mContext.getResources().getDimension(R.dimen.x30);
        options.override(size, size); //设置加载的图片大小
        Glide.with(mContext).load(iconList.get(position)).apply(options).into(holder.icon);*/
        Glide.with(mContext).load(iconList.get(position)).into(holder.icon);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return iconList.size();
    }

    class MenuItemHolder extends RecyclerView.ViewHolder {
        ImageView icon;

        public MenuItemHolder(View itemView, View.OnClickListener listener) {
            super(itemView);
            itemView.setOnClickListener(listener);
            icon = itemView.findViewById(R.id.loan_menu_item);

        }

    }
}
