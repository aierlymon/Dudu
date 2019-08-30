package com.dudu.huodai.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dudu.baselib.http.HttpConstant;
import com.dudu.baselib.utils.Utils;
import com.dudu.huodai.R;

import java.util.List;

public class GameSmashAdapter extends RecyclerView.Adapter<GameSmashAdapter.GameSmashHodler> implements View.OnClickListener{


    private OnItemClickListener mOnItemClickListener;

    private Context mContext;
    List<String> list;

    public GameSmashAdapter(Context mContext, List<String> list) {
        this.mContext = mContext;
        this.list = list;
    }

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
    public GameSmashHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GameSmashHodler(LayoutInflater.from(mContext)
                .inflate(R.layout.fra_loan_bodyandtask_item, parent, false), this);
    }

    @Override
    public void onBindViewHolder(@NonNull GameSmashHodler holder, int position) {
        RequestOptions options = new RequestOptions();
        int size = (int) mContext.getResources().getDimension(R.dimen.x72);
        size= Utils.px2dip(mContext.getApplicationContext(),size);
        options.override(size, size); //设置加载的图片大小
        Glide.with(mContext).load(R.mipmap.gold_egg).into(holder.icon);


        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class GameSmashHodler extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView big_info;
        TextView content_tx;
        Button btn_ok_item;

        public GameSmashHodler(View itemView, View.OnClickListener listener) {
            super(itemView);
            itemView.setOnClickListener(listener);
            icon = itemView.findViewById(R.id.icon);
            big_info = itemView.findViewById(R.id.big_info);
            content_tx = itemView.findViewById(R.id.content_tx);
            btn_ok_item = itemView.findViewById(R.id.btn_ok_item);
        }

    }
}
