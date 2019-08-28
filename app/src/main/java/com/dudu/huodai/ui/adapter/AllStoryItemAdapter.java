package com.dudu.huodai.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dudu.baselib.utils.MyLog;
import com.dudu.huodai.R;
import com.dudu.model.bean.AllStoryBean;

import java.util.List;

/**
 * createBy ${huanghao}
 * on 2019/8/26
 */
public class AllStoryItemAdapter extends RecyclerView.Adapter<AllStoryItemAdapter.ItemHolder> implements View.OnClickListener{

    private Context mContext;
    List<AllStoryBean.ClassificationBean.TagListBean> tag_list;


    public AllStoryItemAdapter(Context mContext, List<AllStoryBean.ClassificationBean.TagListBean> tag_list) {
        this.tag_list=tag_list;
        this.mContext=mContext;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(mContext).inflate(R.layout.all_story_item_text,parent,false),this);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        MyLog.i("我来到了读取数据tag_list: "+tag_list.size());
        holder.title.setText(tag_list.get(position).getName());
        InnerAllStortBean innerAllStortBean=new InnerAllStortBean();
        innerAllStortBean.setId(tag_list.get(position).getId());
        innerAllStortBean.setPos(position);
        holder.itemView.setTag(innerAllStortBean);
    }

    @Override
    public int getItemCount() {
        return tag_list.size();
    }

    private OnItemClickListener mOnItemClickListener;

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(view, (InnerAllStortBean) view.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, InnerAllStortBean position);
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        TextView title;

        public ItemHolder(View itemView, View.OnClickListener listener) {
            super(itemView);
            itemView.setOnClickListener(listener);
            title = itemView.findViewById(R.id.tx_item);
        }
    }

    public class InnerAllStortBean{
        private int id;
        private int pos;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPos() {
            return pos;
        }

        public void setPos(int pos) {
            this.pos = pos;
        }
    }
}
