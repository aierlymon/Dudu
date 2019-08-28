package com.dudu.huodai.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dudu.baselib.http.HttpConstant;
import com.dudu.baselib.utils.Utils;
import com.dudu.huodai.R;
import com.dudu.model.bean.StoryTable;

import java.util.List;

public class LoanBodyRevAdapter extends RecyclerView.Adapter<LoanBodyRevAdapter.BodyItemHold> implements View.OnClickListener {

    private Context mContext;
    private List<StoryTable> storyTableList;


    public LoanBodyRevAdapter(Context mContext, List<StoryTable> storyTableList) {
        this.mContext = mContext;
        this.storyTableList = storyTableList;
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
    public BodyItemHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BodyItemHold(LayoutInflater.from(mContext)
                .inflate(R.layout.fra_loan_bodyandtask_item, parent, false), this);
    }

    @Override
    public void onBindViewHolder(@NonNull BodyItemHold holder, int position) {
        holder.itemView.setTag(position);


        RequestOptions options = new RequestOptions();
        int size = (int) mContext.getResources().getDimension(R.dimen.x40);
        size=Utils.px2dip(mContext.getApplicationContext(),size);
        options.override(size, size); //设置加载的图片大小
        Glide.with(mContext).load(HttpConstant.PIC_BASE_URL + storyTableList.get(position).getPicture()).into(holder.icon);

    //    Glide.with(mContext).load(HttpConstant.PIC_BASE_URL + storyTableList.get(position).getPicture()).into(holder.icon);
        holder.big_info.setText(storyTableList.get(position).getTitle());
        holder.content_tx.setText(storyTableList.get(position).getGuide());
        holder.btn_ok_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "pos: " + position, Toast.LENGTH_SHORT).show();
            }
        });
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return storyTableList.size();
    }

    class BodyItemHold extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView big_info;
        TextView content_tx;
        Button btn_ok_item;

        public BodyItemHold(View itemView, View.OnClickListener listener) {
            super(itemView);
            itemView.setOnClickListener(listener);
            icon = itemView.findViewById(R.id.icon);
            big_info = itemView.findViewById(R.id.big_info);
            content_tx = itemView.findViewById(R.id.content_tx);
            btn_ok_item = itemView.findViewById(R.id.btn_ok_item);
        }

    }
}
