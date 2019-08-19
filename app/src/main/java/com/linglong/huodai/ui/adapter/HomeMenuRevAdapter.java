package com.linglong.huodai.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.linglong.baselib.http.HttpConstant;
import com.linglong.huodai.R;
import com.linglong.model.bean.NewHomeMenuBean;

import java.util.List;

public class HomeMenuRevAdapter extends RecyclerView.Adapter<HomeMenuRevAdapter.MenuItemHolder> implements View.OnClickListener {

    List<NewHomeMenuBean.LoanCategoriesBean> mulDataModelList;
    private Context mContext;
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

    public HomeMenuRevAdapter(Context mContext, List<NewHomeMenuBean.LoanCategoriesBean> mulDataModelList) {
        this.mulDataModelList = mulDataModelList;
        this.mContext = mContext;
    }

    public void setMulDataModelList(List<NewHomeMenuBean.LoanCategoriesBean> mulDataModelList) {
        this.mulDataModelList = mulDataModelList;
    }

    @NonNull
    @Override
    public MenuItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MenuItemHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.home_menu_item, parent, false), this);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemHolder holder, int position) {
        Glide.with(mContext).load(HttpConstant.BASE_URL + mulDataModelList.get(position).getIcon()).into(holder.image);
        // holder.name.setTypeface(ApplicationPrams.typeface);
        holder.name.setText(mulDataModelList.get(position).getName());
        holder.itemView.setTag(position);
        holder.image.setOnClickListener(view -> {
            if (mOnItemClickListener != null) {
                //注意这里使用getTag方法获取position
                mOnItemClickListener.onItemClick(view, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mulDataModelList.size();
    }

    class MenuItemHolder extends RecyclerView.ViewHolder {
        ImageView image;

        TextView name;

        public MenuItemHolder(View itemView, View.OnClickListener listener) {
            super(itemView);
            itemView.setOnClickListener(listener);
            image = itemView.findViewById(R.id.menu_icon);
            name = itemView.findViewById(R.id.menu_text);
        }

    }
}
