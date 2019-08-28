package com.dudu.huodai.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dudu.baselib.utils.MyLog;
import com.dudu.huodai.ApplicationPrams;
import com.dudu.huodai.LabelActivity;
import com.dudu.huodai.R;
import com.dudu.huodai.SubjectActivity;
import com.dudu.model.bean.AllStoryBean;

import java.util.List;

/**
 * createBy ${huanghao}
 * on 2019/8/26
 */
public class AllStoryRevAdapter extends RecyclerView.Adapter<AllStoryRevAdapter.MenuItemHolder> {

    private List<AllStoryBean.ClassificationBean> classificationBeans;

    private Context mContext;

    public AllStoryRevAdapter(Context mContext,List<AllStoryBean.ClassificationBean> classificationBeans) {
        this.mContext = mContext;
        this.classificationBeans=classificationBeans;
    }




    @NonNull
    @Override
    public MenuItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyLog.i("我来到了加载格局: view");
        return new MenuItemHolder(LayoutInflater.from(mContext).inflate(R.layout.all_story_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemHolder holder, int position) {
        MyLog.i("我来到了加载格局: "+position);
        AllStoryBean.ClassificationBean classificationBean = classificationBeans.get(position);
        holder.title.setText(classificationBean.getName());
        holder.itemRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        AllStoryItemAdapter allStoryItemAdapter = new AllStoryItemAdapter(mContext, classificationBean.getTag_list());
        allStoryItemAdapter.setOnItemClickListener((view, innerAllStortBean) -> {
            Intent intent=new Intent(mContext, LabelActivity.class);
            intent.putExtra(ApplicationPrams.key_id,innerAllStortBean.getId());
            intent.putExtra(ApplicationPrams.key_title,classificationBean.getTag_list().get(innerAllStortBean.getPos()).getName());
            mContext.startActivity(intent);
        });
        holder.itemRecyclerView.setAdapter(allStoryItemAdapter);

        if(position==0||position==1){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext, SubjectActivity.class);
                    intent.putExtra(ApplicationPrams.key_id,position);
                    intent.putExtra(ApplicationPrams.key_title,position==0?mContext.getResources().getString(R.string.subjecy_story):mContext.getResources().getString(R.string.serialization_story));
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return classificationBeans.size();
    }

    class MenuItemHolder extends RecyclerView.ViewHolder {

        TextView title;
        RecyclerView itemRecyclerView;

        public MenuItemHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.all_story_title);
            itemRecyclerView = itemView.findViewById(R.id.all_item_recyclerview);
        }

    }
}
