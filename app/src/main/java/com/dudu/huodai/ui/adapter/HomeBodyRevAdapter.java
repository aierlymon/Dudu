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
import com.dudu.baselib.http.HttpConstant;
import com.dudu.baselib.utils.MyLog;
import com.dudu.huodai.R;
import com.dudu.huodai.widget.CircleImageView;
import com.dudu.model.bean.NewHomeBodyBean;

import java.util.List;

public class HomeBodyRevAdapter extends RecyclerView.Adapter<HomeBodyRevAdapter.BodyItemHold> implements View.OnClickListener {

    private Context mContext;
    private List<NewHomeBodyBean.LoanProductBean> homeBodyBeanList;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    private OnItemClickListener mOnItemClickListener;

    public HomeBodyRevAdapter(Context mContext, List<NewHomeBodyBean.LoanProductBean> homeBodyBeanList) {
        this.mContext = mContext;
        this.homeBodyBeanList = homeBodyBeanList;
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(view, (int) view.getTag());
        }
    }


    @NonNull
    @Override
    public BodyItemHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BodyItemHold(LayoutInflater.from(mContext)
                .inflate(R.layout.common_body_item, parent, false), this);
    }

    @Override
    public void onBindViewHolder(@NonNull BodyItemHold holder, int position) {
        NewHomeBodyBean.LoanProductBean  loanProductBean= homeBodyBeanList.get(position);
        String icon_url = HttpConstant.BASE_URL + homeBodyBeanList.get(position).getIcon();

        Glide.with(mContext).load(icon_url).into(holder.icon);

        //标题名字
        holder.title.setText(loanProductBean.getName());

        holder.info.setText(""+loanProductBean.getProfile());

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
        return homeBodyBeanList.size();
    }

    class BodyItemHold extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;
        TextView info;

        public BodyItemHold(@NonNull View itemView, View.OnClickListener listener) {
            super(itemView);

            itemView.setOnClickListener(listener);
            icon = itemView.findViewById(R.id.menu_icon);
            title = itemView.findViewById(R.id.tx_menu_title);
            info=itemView.findViewById(R.id.tx_menu_info);
        }
    }
}
