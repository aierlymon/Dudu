package com.linglong.huodai.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linglong.huodai.R;
import com.linglong.huodai.mvp.model.postbean.LoanFraTypeBean;
import com.linglong.huodai.mvp.model.postbean.LoanMoneyBean;
import com.linglong.huodai.ui.adapter.base.BaseMulDataModel;
import com.linglong.huodai.ui.adapter.base.BaseMulViewHolder;

import java.util.ArrayList;
import java.util.List;

public class LoanSpinnerRevAdapter extends RecyclerView.Adapter<BaseMulViewHolder> implements View.OnClickListener {

    private List<BaseMulDataModel> infoList;


    public static final int TYPE = 1;
    public static final int MONEY = 2;
    private Context mContext;


    public LoanSpinnerRevAdapter(Context mContext) {
        this.infoList = new ArrayList<>();
        this.mContext=mContext;
    }


    @NonNull
    @Override
    public BaseMulViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE:
                return new SpinnerTypeHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.loan_spinner_recv_item, parent, false), this);
        }
        return new SpinnerMoneyHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.loan_spinner_recv_item, parent, false), this);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseMulViewHolder holder, int position) {
        holder.bindData(infoList.get(position), position);

        holder.itemView.setTag(infoList.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        if (infoList.get(position) instanceof LoanFraTypeBean) {
            return TYPE;
        }
        return MONEY;
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    public void setInfoList(List<BaseMulDataModel> contentList) {
        this.infoList = contentList;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, BaseMulDataModel position);
    }

    private OnItemClickListener mOnItemClickListener;

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(view, (BaseMulDataModel) view.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }


    class SpinnerTypeHolder extends BaseMulViewHolder<LoanFraTypeBean> {
        TextView info;

        public SpinnerTypeHolder(@NonNull View itemView, View.OnClickListener listener) {
            super(itemView);
            itemView.setOnClickListener(listener);
            info = itemView.findViewById(R.id.info);
        }

        @Override
        public void bindData(LoanFraTypeBean dataModel, int position) {
            if(position==0){
                info.setTextColor(mContext.getResources().getColor(R.color.my_login_color));
            }
            info.setText(dataModel.getName());
        }
    }

    class SpinnerMoneyHolder extends BaseMulViewHolder<LoanMoneyBean> {
        TextView info;

        public SpinnerMoneyHolder(View itemView, View.OnClickListener listener) {
            super(itemView);
            itemView.setOnClickListener(listener);
            info = itemView.findViewById(R.id.info);
        }

        @Override
        public void bindData(LoanMoneyBean dataModel, int position) {
            if(position==0){
                info.setTextColor(mContext.getResources().getColor(R.color.my_login_color));
            }
            info.setText(dataModel.getName());
        }
    }
}
