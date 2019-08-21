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
import com.dudu.baselib.http.HttpConstant;
import com.dudu.baselib.utils.MyLog;
import com.dudu.huodai.R;
import com.dudu.model.bean.NewHomeMenuBean;

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
                .inflate(R.layout.common_body_item, parent, false), this);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemHolder holder, int position) {
        MyLog.i("HomeMenuRevAdapter 执行次数: "+position+"  mulDataModelList.size: "+mulDataModelList.size());
        Glide.with(mContext).load(HttpConstant.BASE_URL + mulDataModelList.get(position).getIcon()).into(holder.icon);
        // holder.name.setTypeface(ApplicationPrams.typeface);
        holder.title.setText(mulDataModelList.get(position).getName());
        holder.itemView.setTag(position);
        holder.icon.setOnClickListener(view -> {
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

        ImageView icon;
        TextView title;
        TextView info;

        public MenuItemHolder(View itemView, View.OnClickListener listener) {
            super(itemView);
            itemView.setOnClickListener(listener);
            icon = itemView.findViewById(R.id.menu_icon);
            title = itemView.findViewById(R.id.tx_menu_title);
            info=itemView.findViewById(R.id.tx_menu_info);
        }

    }
}
