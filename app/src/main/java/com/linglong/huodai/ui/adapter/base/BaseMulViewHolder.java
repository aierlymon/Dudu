package com.linglong.huodai.ui.adapter.base;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import butterknife.ButterKnife;

/**
 * author huanghao
 * Created by 98733 on 2018/8/21.
 * info 这个是列表展示的适配器的调用
 */

public abstract class BaseMulViewHolder<T extends BaseMulDataModel> extends RecyclerView.ViewHolder {

    public BaseMulViewHolder(View itemView) {
        super(itemView);
        //这个不需要unbind只有fragment需要而已
        ButterKnife.bind(this,itemView);
    }

    public abstract void bindData(T dataModel, int position);

}
