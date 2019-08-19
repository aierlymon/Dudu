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
import com.bumptech.glide.request.RequestOptions;
import com.linglong.baselib.http.HttpConstant;
import com.linglong.baselib.utils.MyLog;
import com.linglong.huodai.R;
import com.linglong.huodai.widget.CircleImageView;
import com.linglong.model.bean.HistoryBean;

import java.util.List;

public class HomeBodyHFRevAdapter extends RecyclerView.Adapter<HomeBodyHFRevAdapter.BodyItemHold> implements View.OnClickListener {

    private Context mContext;
    private List<HistoryBean> homeBodyBeanList;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    private OnItemClickListener mOnItemClickListener;

    public HomeBodyHFRevAdapter(Context mContext, List<HistoryBean> homeBodyBeanList) {
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
                .inflate(R.layout.home_body_item, parent, false), this);
    }

    @Override
    public void onBindViewHolder(@NonNull BodyItemHold holder, int position) {
        HistoryBean historyBean= homeBodyBeanList.get(position);
        String icon_url = HttpConstant.BASE_URL + homeBodyBeanList.get(position).getIcon();

        RequestOptions options = new RequestOptions();
        int size = (int) mContext.getResources().getDimension(R.dimen.x30);
        options.override(size, size); //设置加载的图片大小
        MyLog.i("body item icon: " + icon_url);
        Glide.with(mContext).load(icon_url).apply(options).into(holder.icon);

        //标题名字
        holder.title.setText(historyBean.getName());

        holder.limit.setText(""+historyBean.getLimitL());

        holder.max.setText("" + historyBean.getLimitH());

        holder.rate.setText(historyBean.getInterest());
        holder.date.setText(historyBean.getPeriod());
        holder.finaltext.setText(historyBean.getProfile());
        holder.time.setText(historyBean.getSpeed());
        holder.btn_request.setOnClickListener(view -> {
            if (mOnItemClickListener != null) {
                //注意这里使用getTag方法获取position
                mOnItemClickListener.onItemClick(view, position);
            }
        });
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(position);

    }

    @Override
    public int getItemCount() {
        return homeBodyBeanList.size();
    }

    class BodyItemHold extends RecyclerView.ViewHolder {
        private CircleImageView icon;
        private TextView title;
        private TextView limit;
        private TextView max;
        private TextView rate;
        private TextView finaltext;
        private TextView time;
        private ImageView btn_request;
        private TextView date;

        public BodyItemHold(@NonNull View itemView, View.OnClickListener listener) {
            super(itemView);

            icon = itemView.findViewById(R.id.icon);
            title = ((TextView) itemView.findViewById(R.id.title));
            limit = ((TextView) itemView.findViewById(R.id.limit));
            max = ((TextView) itemView.findViewById(R.id.max));
            rate = ((TextView) itemView.findViewById(R.id.rate));
            finaltext = ((TextView) itemView.findViewById(R.id.finaltext));
            time = ((TextView) itemView.findViewById(R.id.time));
            btn_request = itemView.findViewById(R.id.btn_request);
            date = ((TextView) itemView.findViewById(R.id.date));
        }
    }
}
